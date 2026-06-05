package gestorEventos;

import controllers.IControladorConexion;
import controllers.IControladorRegistro;
import eventos.Evento;
import eventos.EventoConexionExitosa;
import eventos.EventoDniExistente;
import eventos.EventoTurnoCreadoConExito;
import eventos.Turno;

public class GestorEventos implements IReceptorEvento{
	
	IControladorConexion cConexion;
	IControladorRegistro cRegistro;
	
	public GestorEventos(IControladorConexion controladorConexion, IControladorRegistro controladorRegistro){
		this.cConexion=controladorConexion;
		this.cRegistro=controladorRegistro;
    }
	

	@Override
    public void recibirEvento(Evento e){
    	System.out.println("RecibirEvento ControladorConexion");
        if (e instanceof EventoConexionExitosa){
	    	EventoConexionExitosa ECE = (EventoConexionExitosa)e;
	    	this.cConexion.finalizar();
	    	this.cRegistro.iniciar(ECE.getNumero());
        }
        else if(e instanceof EventoDniExistente){
	    	EventoDniExistente evento = (EventoDniExistente) e;
	    	this.cRegistro.errorDniExistente(evento.getDni());
	    }
	    else if(e instanceof EventoTurnoCreadoConExito){
	    	EventoTurnoCreadoConExito evento = (EventoTurnoCreadoConExito) e;
	    	Turno nuevo=evento.getTurno();
	    	this.cRegistro.turnoCreado(nuevo);
	    	System.out.println("El turno fue creado con éxito");
	    }
        else{
            System.out.println("Gestor Eventos 28 : evento desconocido");
        }
    }
	
/*	public void recibirEvento(Evento e) {
		System.out.println("RecibirEvento-ControladorRegistro");
        if(e instanceof EventoDniExistente){
	    	EventoDniExistente evento = (EventoDniExistente) e;
	    	this.vista.errorDniExistente(evento.getDni());
	    }
	    else if(e instanceof EventoTurnoCreadoConExito){
	    	EventoTurnoCreadoConExito evento = (EventoTurnoCreadoConExito) e;
	    	Turno nuevo=evento.getTurno();
	    	this.vista.turnoCreado(nuevo);
	    	System.out.println("El turno fue creado con éxito");
	    }
	    else if(e instanceof EventoConexionExitosa){
	    	System.out.println("HOLA EventoConexionExitosa En ControladorRegistro");	    	
	    }
	    else{
	    	System.out.println("EVENTO DESCONOCIDO - TR-GestorTurnos-ArriboEvento");
	    	
	    }
	}*/
	
	
}
