package gestionDeSincronizacion;

import comunicacionConTerminales.IEnviarEventoServidor;
import gestorFilaYTerminales.IColaTurno;
import gestorFilaYTerminales.IEstadoFila;
import redundanciaPasiva.IRedundanciaPasiva;
import eventos.Evento;
import eventos.EventoHeartBeat;
import eventos.EventoSincronizacionEstado;
import eventos.EventoSolicitudHeartBeat;
import eventos.EventoSolicitudSincronizacion;

public class GestorDeSincronizacion implements I_Sync,I_HeartBeat{
	private IEstadoFila gestorFila;
	private IEnviarEventoServidor enviarAOtroServidor;
	private IRedundanciaPasiva IRP=null;
	private boolean EsperandoSincro;
	
	public GestorDeSincronizacion(IEstadoFila gestorFila, IEnviarEventoServidor enviarAOtroServidor,IRedundanciaPasiva igs) {
		this.gestorFila=gestorFila;
		this.enviarAOtroServidor=enviarAOtroServidor;
		this.IRP=igs;
		this.EsperandoSincro=false;
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
	public void enviarEstadoCola() {
		int ct =gestorFila.getCantidadTurnos();
		int cp = gestorFila.getCantidadPone();
		int cs = gestorFila.getCantidadSaca();
	    IColaTurno copiaCola = gestorFila.getCola().generarCopia();
	    		//copiarCola(gestorFila.getCola());
	    
	    //ACA HAY QUE ver si soy secundario
	    //Si soy secundario tengo que apagar el servidor con Clientes
	    //Y despues enviar el estado de la cola.
	    if(IRP.soySecundario()) {
	    	IRP.apagarServidorSecundario();
	    }
	    
	    
		Evento e = new EventoSincronizacionEstado(copiaCola,ct,cp,cs);
		System.out.println("GestorSincronizacion43 - EventoSincronizacionEstado enviado");
		this.enviarAOtroServidor.enviarEventoASincrionizable(e);
	}

	//I_SYNC:
	@Override
	public void recibirEstadoCola(IColaTurno iColaTurno, int ct, int cp, int cs) {
		gestorFila.setEstado(iColaTurno, ct, cp, cs);
		this.IRP.notificarEstadoSincronizado();

		this.EsperandoSincro=false;
	}

	//I_HeartBeat:
	@Override
	public void solicitarHeartBeat() {
		System.out.println("GestorSincro-54-envio SolicitudHeartBeat a Sincronizador");
		this.enviarAOtroServidor.enviarEventoASincronizador(new EventoSolicitudHeartBeat());
		
	}

	//I_HeartBeat:
	@Override
	public void iniciarEnviosHeartBeat() {
	    Thread hiloHeartBeat = new Thread(() -> {
	        while (IRP.estaConectadoSincronizable()) {
	    	EventoHeartBeat e = new EventoHeartBeat(0, 0, 0);
	            e.setCantidadTurnos(gestorFila.getCantidadTurnos());
	            e.setCantidadPone(gestorFila.getCantidadPone());
	            e.setCantidadSaca(gestorFila.getCantidadSaca());
	            this.enviarAOtroServidor.enviarEventoASincrionizable(e);

	            try {
	                // Espera 1 segundo
	                Thread.sleep(3000);
	            } catch (InterruptedException ex) {
	                // Si el hilo es interrumpido, termina
	                ex.printStackTrace();
	                break;
	            }
	        }
	    });

	    // Inicia el hilo
	    hiloHeartBeat.start();
	}

	//I_HeartBeat:
	@Override
	public void recibirHeartbeat(EventoHeartBeat e) {
		System.out.println("GestorSincro92-Llego HeartBeat turno="+e.getCantidadTurnos()+" pone="+e.getCantidadPone()+" saca="+e.getCantidadSaca());
		int turno=e.getCantidadTurnos();
		int pone=e.getCantidadPone();
		int saca=e.getCantidadSaca();
		
		if(this.EsperandoSincro==true) {
			System.out.println("Llego heartBeat pero estoy esperando una actualizacion");
		}
		else {
			if(requiereSincronizacion(turno,pone,saca)) {
				System.out.println("Se requiere sincronizar");
				this.EsperandoSincro=true;
				solicitarSincronizacion();
			}
			else {
				System.out.println("No se requiere sincronizar");
			}
		}
		
		
	}

	private boolean requiereSincronizacion(int turno, int pone, int saca) {
		return (gestorFila.getCantidadTurnos()!=turno)||(gestorFila.getCantidadPone()!=pone)||(gestorFila.getCantidadSaca()!=saca);
	}
	
	

}
