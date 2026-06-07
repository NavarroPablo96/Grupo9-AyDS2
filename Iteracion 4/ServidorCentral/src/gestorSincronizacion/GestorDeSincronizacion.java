package gestorSincronizacion;

import eventos.Evento;
import eventos.EventoActualizacionLlamarSiguiente;
import eventos.EventoActualizacionNuevoTurno;
import eventos.EventoActualizacionRellamar;
import eventos.EventoHeartBeat;
import eventos.EventoSincronizacionEstado;
import eventos.EventoSolicitudHeartBeat;
import eventos.EventoSolicitudSincronizacion;
import gestorFila.Historial;
import gestorFila.IColaTurno;
import gestorFila.IEstadoFila;
import gestorFila.RegistroRellamar;
import gestorServidores.IEnviarEventoServidores;
import gestorServidores.IRedundanciaPasiva;

public class GestorDeSincronizacion implements I_Sync,I_HeartBeat,IActualizacion{
	private IEstadoFila gestorFila;
	private IEnviarEventoServidores enviarAOtroServidor;
	private IRedundanciaPasiva IRP=null;
	
	public GestorDeSincronizacion(IEstadoFila gestorFila, IEnviarEventoServidores enviarAOtroServidor,IRedundanciaPasiva igs) {
		this.gestorFila=gestorFila;
		this.enviarAOtroServidor=enviarAOtroServidor;
		this.IRP=igs;
	}
	
	//I_SYNC:
	@Override
	public void solicitarSincronizacion() {
		Evento e = new EventoSolicitudSincronizacion ("Servidor","Servidor");
		this.enviarAOtroServidor.enviarEventoASincronizador(e);
		System.out.println("EventoSolicitudSincronizacion enviado");
	}

	//I_SYNC:
	@Override
	public void enviarEstadoSistema() {
		int nts =gestorFila.getNumeroTurnoSiguiente();
		int cp = gestorFila.getCantidadPone();
		int cs = gestorFila.getCantidadSaca();
	    IColaTurno copiaCola = gestorFila.getCola().generarCopia();
	    		//copiarCola(gestorFila.getCola());
	    Historial historial = gestorFila.getHistorial().generarCopia();
	    RegistroRellamar llamados = gestorFila.getRegistro().generarCopia();

	    System.out.println("Enviar estado Sistema Mostrar historial:");
	    historial.mostrar();

	    
		Evento e = new EventoSincronizacionEstado(copiaCola,nts,cp,cs,historial,llamados);
		System.out.println("GestorSincronizacion43 - EventoSincronizacionEstado enviado");
		this.enviarAOtroServidor.enviarEventoASincrionizable(e);
	}

	//I_SYNC:
	@Override
	public void recibirEstadoSistema(IColaTurno iColaTurno, int nts, int cp, int cs,Historial historial,RegistroRellamar llamados) {
		gestorFila.setEstado(iColaTurno, nts, cp, cs);
		gestorFila.setHistorial(historial);
		gestorFila.setRegistro(llamados);
		this.IRP.notificarEstadoSincronizado();
	}

	//I_HeartBeat:
	@Override
	public void solicitarHeartBeat() {
		System.out.println("GestorSincro-54-envio SolicitudHeartBeat a Sincronizador");
		this.enviarAOtroServidor.enviarEventoASincronizador(new EventoSolicitudHeartBeat());
		iniciarMonitorHeartBeat();
	}

	private void iniciarMonitorHeartBeat() {
		System.out.println("Gestor Sincronizacion 72 - INICIAR MONITOR HEART BEAT");
	    Thread monitor = new Thread(() -> {
	    	if(PuedeDormir(3000)==false) {
	    		//Solo quiero que duerma un rato el hilo porque sale
	    		//False la condicion del while
	    	}
	        while (true) {
	            long ahora = System.currentTimeMillis();
	            long diferencia = ahora - ultimoHeartMillis;
	            // Si no llegó ningún heartbeat en 10 segundos
	            if (diferencia > 10000) {
	                System.out.println("No se reciben heartbeats. El primario cayo.");
	                // Convertirse en primario
	                this.IRP.NotificarAusenciaHeartBeat();
	                break;
	            }
	            //System.out.println("Ultimo HeartBeat recibido hace " + (diferencia / 1000.0) + " segundos");
	            if(PuedeDormir(1000)==false) {
	            	break;
	            }
	        }
	    });
	    monitor.start();
		
	}

	private boolean PuedeDormir(int segundos) {
		boolean NoHuboError = true;
        try {
            // Espera 3 segundo
            Thread.sleep(segundos);
        } catch (InterruptedException ex) {
            // Si el hilo es interrumpido, termina
            ex.printStackTrace();
            NoHuboError=false;
        }
        return NoHuboError;
	}

	//I_HeartBeat:
	@Override
	public void iniciarEnviosHeartBeat() {
	    Thread hiloHeartBeat = new Thread(() -> {
	        while (IRP.estaConectadoSincronizable()) {
	        	EventoHeartBeat e = new EventoHeartBeat();
	            this.enviarAOtroServidor.enviarEventoASincrionizable(e);
	            
	            if(PuedeDormir(3000)==false) {
	            	break;
	            }
	        }
	    });

	    // Inicia el hilo
	    hiloHeartBeat.start();
	}

	//I_HeartBeat:
	long ultimoHeartMillis;
	@Override
	public void recibirHeartbeat(EventoHeartBeat e) {
		ultimoHeartMillis = System.currentTimeMillis();
		//System.out.println("GestorSincro92-Llego HeartBeat");
		
	}

	@Override
	public void enviarActualizacion(Evento e) {
		this.enviarAOtroServidor.enviarEventoASincrionizable(e);
	}
	
	@Override
	public void recibirActualizacion(Evento e) {
		if(e instanceof EventoActualizacionNuevoTurno) {
			EventoActualizacionNuevoTurno EANT = (EventoActualizacionNuevoTurno)e;
			this.gestorFila.actualizacionNuevoTurno(EANT.getTurno());
		}
	    else if(e instanceof EventoActualizacionLlamarSiguiente) {
	    	EventoActualizacionLlamarSiguiente EALS = (EventoActualizacionLlamarSiguiente)e;
	    	this.gestorFila.actualizacionLlamarSiguiente(EALS.getNumeroTerminalQueLlama(),EALS.getTurnoLlamado());
	    }
	    else if (e instanceof EventoActualizacionRellamar) {
	    	EventoActualizacionRellamar EAR = (EventoActualizacionRellamar)e;
	    	this.gestorFila.actualizacionRellamar(EAR.getNumeroTerminalQueLlama(),EAR.getTurnoLlamado());
	    }
	}
	
	

}
