package controllers;


import comunicacion.Comunicador;


import interfaces.IReceptorEvento;
import eventos.Evento;
import eventos.EventoConexionExitosa;

import interfaces.IComunicador;
import interfaces.IControladorConexion;
import interfaces.IVistaConexion;
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
        String ipPrimario = vista.getIp();
        int puertoPrimario = vista.getPuerto();
        String ipSecundario = vista.getIpSecundario();
        int puertoSecundario = vista.getPuertoSecundario();
        

        modelo.conectar(ipPrimario, puertoPrimario,ipSecundario,puertoSecundario,true);
    }

    public void iniciar(){
    	vista.abrir();
    }


    private void finalizar(){
    	vista.cerrar();
    }
    
    private void iniciarRegistro(int i) {
    	Registro vistaRegistro = new Registro();
    	GestorTerminal modeloRegistro = new GestorTerminal(Comunicador.getInstance());
    	ControladorRegistro controladorRegistro = new ControladorRegistro(vistaRegistro, modeloRegistro);
    	modelo.setReceptor(controladorRegistro);
    	controladorRegistro.iniciar(i);
    }

    public void recibirEvento(Evento e){
    	System.out.println("RecibirEvento ControladorConexion");
        if (e instanceof EventoConexionExitosa){
	    	EventoConexionExitosa ECE = (EventoConexionExitosa)e;
            finalizar();
            iniciarRegistro(ECE.getNumero());
        }
        else{
            System.out.println("error");
        }
    }
}
