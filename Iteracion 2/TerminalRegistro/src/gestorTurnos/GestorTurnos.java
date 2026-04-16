package gestorTurnos;

import java.util.Date;

import comuEntreProcesos.ComunicacionEntreProcesos;
import comuEntreProcesos.IReceptorEvento;
import eventos.Evento;
import eventos.EventoDniExistente;
import eventos.EventoNuevoTurno;
import eventos.EventoNumeroTerminal;
import eventos.EventoTurnoCreadoConExito;
import eventos.Turno;
import vista_controlador.Controlador;

public class GestorTurnos implements IReceptorEvento{
	
	private static GestorTurnos instancia;
	private int NumeroTerminal;
	
	private GestorTurnos() {
	}

	public static GestorTurnos getInstance() {
        if (instancia == null) {
            instancia = new GestorTurnos();
        }
        return instancia;
	}
	
	public void CrearTurno(String dni, String hora,Date horaReal) {
		
		Turno nuevo = new Turno(-1,dni, hora,horaReal);//TR=TerminalDeRegistro // TA=TerminalDeAtencion // TN=TerminalNotificacion
		EventoNuevoTurno nuevoTurno = new EventoNuevoTurno("TR"+this.NumeroTerminal,"Servidor",nuevo);
		ComunicacionEntreProcesos.getInstance().enviarEvento(nuevoTurno);
		//Controlador.getInstance().ActualizarVista(nuevo);
	}

	@Override
	public void ArriboEvento(Evento e) {
	    if (e instanceof EventoNumeroTerminal) {
	    	EventoNumeroTerminal ent =(EventoNumeroTerminal) e;
	    	this.NumeroTerminal=ent.getNumero();
	    	Controlador.getInstance().ActualizarVistaNumero(ent.getNumero());
	    }
	    else if(e instanceof EventoDniExistente){
	    	EventoDniExistente Ede=(EventoDniExistente) e;
	    	Controlador.getInstance().DocumentoYaRegistrado(Ede.getDni());
	    }
	    else if(e instanceof EventoTurnoCreadoConExito){
	    	EventoTurnoCreadoConExito ETCCE = (EventoTurnoCreadoConExito) e;
	    	Turno nuevo=ETCCE.getTurno();
	    	Controlador.getInstance().ActualizarVista(nuevo);
	    	System.out.println("El turno fue creado con éxito");
	    }
	    else{
	    	System.out.println("EVENTO DESCONOCIDO - TR-GestorTurnos-ArriboEvento");
	    	
	    }

	}

}
