package controllers;

import javax.swing.SwingUtilities;

import eventos.Evento;
import eventos.EventoConexionExitosa;
import interfaces.IComunicador;
import interfaces.IControladorConexion;
import interfaces.IVistaConexion;
import interfaces.IReceptorEvento;
import model.GestorTerminal;
import views.Registro;

public class ControladorConexion implements IControladorConexion, IReceptorEvento{
    
    private IVistaConexion vista;
    private IComunicador modelo;

    public ControladorConexion(IVistaConexion vista, IComunicador modelo){
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setController(this);
    }

    public void establecerConexion(){
        String ip = vista.getIp();
        int puerto = vista.getPuerto();

        try{
            modelo.conectar(ip, puerto);

            finalizar();

        } catch (Exception e){}
        
    }

    public void iniciar(){
        SwingUtilities.invokeLater(() -> {
            vista.abrir();
        });
    }


    private void finalizar(){
        vista.cerrar();
        SwingUtilities.invokeLater(() -> {
        	Registro vistaRegistro = new Registro();
        	GestorTerminal modeloRegistro = new GestorTerminal(modelo);
        	ControladorRegistro controladorRegistro = new ControladorRegistro(vistaRegistro, modeloRegistro);
        	modelo.setReceptor(controladorRegistro);
        	
        	controladorRegistro.iniciar();
        });
    }

    public void recibirEvento(Evento e){
    	System.out.println("RecibirEvento ControladorConexion");
        if (e instanceof EventoConexionExitosa){
            finalizar();
        }
        else{
            System.out.println("error");
        }
    }

}
