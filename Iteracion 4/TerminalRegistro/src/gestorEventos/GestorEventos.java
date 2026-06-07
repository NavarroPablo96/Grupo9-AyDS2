package gestorEventos;

import controllers.IControladorConexion;
import controllers.IControladorRegistro;
import eventos.Evento;
import eventos.EventoConexionExitosa;
import eventos.EventoDniExistente;
import eventos.EventoTurnoCreadoConExito;
import eventos.Turno;
import factory.SeguridadFactory;
import seguridad.ISeguridadStrategy;

public class GestorEventos implements IReceptorEvento{
	
	IControladorConexion cConexion;
	IControladorRegistro cRegistro;
	ISeguridadStrategy encriptador;
	
	public GestorEventos(IControladorConexion controladorConexion, IControladorRegistro controladorRegistro){
		this.cConexion=controladorConexion;
		this.cRegistro=controladorRegistro;
    }
	

	@Override
    public void recibirEvento(Evento e){
    	System.out.println("RecibirEvento ControladorConexion");
        if (e instanceof EventoConexionExitosa){
	    	EventoConexionExitosa ECE = (EventoConexionExitosa)e;
			this.cRegistro.setEncriptadorApi(this.cConexion.getTipoEncriptado(), this.cConexion.getClave());
			ISeguridadStrategy crypt = SeguridadFactory.crearEncriptador(this.cConexion.getTipoEncriptado());
			setEncriptador(crypt);
			encriptador.setClave(cConexion.getClave());			
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
			nuevo.setDocumento(encriptador.desencriptar(nuevo.getDocumento()));
	    	this.cRegistro.turnoCreado(nuevo);
	    	System.out.println("El turno fue creado con éxito");
	    }
        else{
            System.out.println("Gestor Eventos 28 : evento desconocido");
        }
    }
	
	private void setEncriptador(ISeguridadStrategy encriptador){
		this.encriptador = encriptador;
	}

}
