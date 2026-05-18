package controllers;


import eventos.Evento;
import eventos.EventoConexionExitosa;
import eventos.EventoDniExistente;
import eventos.EventoTurnoCreadoConExito;
import eventos.Turno;
import interfaces.IControladorRegistro;
import interfaces.IReceptorEvento;
import interfaces.IVistaRegistro;
import model.GestorTerminal;

public class ControladorRegistro implements IControladorRegistro, IReceptorEvento{
    
    private IVistaRegistro vista;
    
    private GestorTerminal modelo;

    public ControladorRegistro(IVistaRegistro vista, GestorTerminal modelo){
        this.vista = vista;
        this.modelo = modelo;

        vista.setController(this);

    }


	public void iniciar(int numeroTerminal){
		vista.abrir();
    	modelo.setNumeroTerminal(numeroTerminal);
    	vista.ActualizarTitulo(modelo.getNumero());

	}


    public void registrarTurno(){
        String dni = vista.getDni();
        if (!esDocumentoValido(dni)) {
        	vista.MensajeErrorDocumentoInvalido();
            return;
        }
        modelo.registrarTurno(dni);
        vista.borrarDni();

    }
    
    private boolean esDocumentoValido(String documento) {
        return documento != null && documento.matches("\\d{7,8}");
    }

	public void recibirEvento(Evento e) {
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
	}



	@Override
	public void iniciar() {
		// TODO Auto-generated method stub
		
	}
	
	
}
