package app;

import comunicacion.Comunicador;
import comunicacion.IComunicador;
import comunicacion.API_Servidor;
import comunicacion.IAtencion;


import vista.IVistaConexion;
import vista.Conexion;
import vista.IVistaOperador;
import vista.VistaOperador;

import controller.ControladorConexion;
import controller.IControladorConexion;
import controller.ControladorOperador;
import controller.IControladorOperador;

import gestores.ILlamado;
import interfaces.IReceptorEvento;
import gestores.GestorLlamado;



public class Main {

    public static void main(String[] args) {
    	IComunicador comunicador = Comunicador.getInstance();
    	
    	IVistaConexion vistaConexion = new Conexion();
    	IControladorConexion controladorConexion = new ControladorConexion(vistaConexion, comunicador);
    	IVistaOperador vistaOperador = new VistaOperador();
    	IAtencion apiServer = new API_Servidor(comunicador);
    	GestorLlamado gestorLlamado = new GestorLlamado(apiServer);
    	
    	 
    	IControladorOperador controladorOperador = new ControladorOperador(vistaOperador,(ILlamado)gestorLlamado);
    	controladorConexion.setControlador(controladorOperador);
    	gestorLlamado.setControladorOperador((ControladorOperador)controladorOperador);
    	
    	comunicador.setReceptor((IReceptorEvento)gestorLlamado);
    	controladorConexion.iniciar();
    	//SwingUtilities.invokeLater(() -> Controlador.getInstance().initControl());
    }
}












