package app;

import comunicacion.API_Servidor;
import comunicacion.ComunicadorRegistro;
import comunicacion.IComunicador;

import views.Conexion;
import views.IVistaConexion;
import views.IVistaRegistro;
import views.Registro;
import controllers.ControladorConexion;
import controllers.ControladorRegistro;
import controllers.IControladorConexion;
import controllers.IControladorRegistro;

import gestorEventos.IReceptorEvento;
import model.GestorRegistro;
import gestorEventos.GestorEventos;

public class Main {

    public static void main(String[] args) {
        IComunicador comunicador = ComunicadorRegistro.getInstance();
        
        IVistaConexion vistaConexion = new Conexion();		//vista
        IControladorConexion controladorConexion = new ControladorConexion(vistaConexion, comunicador);	//controlador
        
        IVistaRegistro vistaRegistro = new Registro();
        GestorRegistro modeloRegistro = new GestorRegistro(API_Servidor.getInstance());
        IControladorRegistro controladorRegistro = new ControladorRegistro(vistaRegistro, modeloRegistro);
        
        
        IReceptorEvento gestorEventos = new GestorEventos(controladorConexion,controladorRegistro);
        comunicador.setReceptor(gestorEventos);
        
        controladorConexion.iniciar();
        
        
    	
    }
}