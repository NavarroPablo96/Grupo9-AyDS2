package controllers;

import javax.swing.SwingUtilities;

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


	public void iniciar(){
        SwingUtilities.invokeLater(() -> {
            vista.abrir();
        });
	}


    public void registrarTurno(){
        String dni = vista.getDni();

        modelo.nuevoTurno(dni);

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
	    	EventoConexionExitosa ECE = (EventoConexionExitosa)e;
	    	modelo.setNumeroTerminal(ECE.getNumero());
	    	vista.ActualizarTitulo(ECE.getNumero());
	    	
	    }
	    else{
	    	System.out.println("EVENTO DESCONOCIDO - TR-GestorTurnos-ArriboEvento");
	    	
	    }
	}
	
	
}
