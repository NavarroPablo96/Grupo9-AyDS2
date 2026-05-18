package comunicacionConTerminales;

import eventos.Evento;
import eventos.EventoHeartBeat;
import eventos.EventoLlamarSiguiente;
import eventos.EventoRellamar;
import eventos.EventoSincronizacionEstado;
import eventos.EventoSolicitudHeartBeat;
import eventos.EventoSolicitudSincronizacion;
import eventos.EventoSolicitudTurno;
import gestionDeSincronizacion.I_HeartBeat;
import gestionDeSincronizacion.I_Sync;
import interfaces.IAtencion;
import interfaces.IReceptorEvento;
import interfaces.IRegistro;

public class GestorEventos implements IReceptorEvento{

	//PATRON SINGLETON
	private static GestorEventos instancia;
	private GestorEventos() {
	}
	public static GestorEventos getInstance() {
		if(instancia==null) {
			instancia= new GestorEventos();
		}
		return instancia;
	}
	
	private IRegistro registro;
	public void setIRegistro(IRegistro ir) {
		this.registro=ir;
	}
	private IAtencion atencion;	
	public void setIAtencion(IAtencion ia) {
		this.atencion=ia;
	}
	private I_Sync sincronizador;
	public void setI_Sync(I_Sync is) {
		this.sincronizador=is;
	}
	private I_HeartBeat heart;
	public void setI_HeartBeat(I_HeartBeat ihb) {
		this.heart=ihb;
	}
	
	
	@Override
	public void ArriboEvento(Evento e) {    	
    	if(e instanceof EventoSolicitudTurno) {
    		String TerminalOrigen = e.getProcesoOrigen();
        	String tipoTerminal = obtenerTipo(TerminalOrigen);
        	int numeroTerminal=obtenerNumero(TerminalOrigen);
			System.out.println("Gestor Eventos - Llego el evento EventoSolicitudTurno");
			EventoSolicitudTurno evento = (EventoSolicitudTurno)e;
        	registro.nuevoTurno(evento,tipoTerminal,numeroTerminal);
		}
	    else if(e instanceof EventoLlamarSiguiente) {
			String TerminalOrigen = e.getProcesoOrigen();
	    	String tipoTerminal = obtenerTipo(TerminalOrigen);
	    	int numeroTerminal=obtenerNumero(TerminalOrigen);
			System.out.println("Gestor Eventos - Llego el evento EventoLlamarSiguiente");
	    	EventoLlamarSiguiente evento = (EventoLlamarSiguiente)e;
        	atencion.LlamarSiguiente(evento,tipoTerminal,numeroTerminal);
	    }
	    else if (e instanceof EventoRellamar) {
			System.out.println("Gestor Eventos67 - Llego el evento EventoRellamar");
	    	EventoRellamar Renoti = (EventoRellamar)e;
	    	atencion.Rellamar(Renoti);
	    }
	    else if (e instanceof EventoSolicitudSincronizacion) {
			System.out.println("Gestor Eventos72 - Llego el evento EventoSolicitudSincronizacion");
			sincronizador.enviarEstadoCola();
	    }
	    else if (e instanceof EventoSincronizacionEstado) {
	    	EventoSincronizacionEstado estado = (EventoSincronizacionEstado)e;
	    	System.out.println("GestorEventos-77: estado.getCola().getCantidad()="+estado.getCola().getCantidad());
	    	sincronizador.recibirEstadoCola(estado.getCola(),estado.getCt(),estado.getCp(),estado.getCs());
	    }
	    else if (e instanceof EventoSolicitudHeartBeat) {
	    	heart.iniciarEnviosHeartBeat();
	    }
	    else if (e instanceof EventoHeartBeat) {
	    	EventoHeartBeat latido=(EventoHeartBeat)e;
	    	heart.recibirHeartbeat(latido);
	    }	    	
	    else {
	    	System.out.println("Llego un Evento");
	    	System.out.println("Tipo: " + e.getClass().getName());
	        System.out.println("Origen: " + e.getProcesoOrigen());
	        System.out.println("Destino: " + e.getProcesoDestino());
	    }
	}

	private String obtenerTipo(String origen) {
	    return origen.substring(0, 2);
	}

	private int obtenerNumero(String origen) {
	    return Integer.parseInt(origen.substring(2));
	}

	
	
    
}
