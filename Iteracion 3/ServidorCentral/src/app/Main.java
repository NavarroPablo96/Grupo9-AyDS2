package app;


import comunicacionConTerminales.Comunicador;
import comunicacionConTerminales.GestorEventos;
import comunicacionConTerminales.GestorTerminales;
import comunicacionConTerminales.IConector;
import comunicacionConTerminales.IEnviarEventoServidor;

import gestorFilaYTerminales.GestorFila;
import gestorFilaYTerminales.IEstadoFila;
import gestorFilaYTerminales.IColaTurno;
import gestorFilaYTerminales.ColaTurno;

import gestionDeSincronizacion.GestorDeSincronizacion;
import gestionDeSincronizacion.I_HeartBeat;
import gestionDeSincronizacion.I_Sync;
import interfaces.IReceptorEvento;

import redundanciaPasiva.IRedundanciaPasiva;
import redundanciaPasiva.GestorServidores;

import controllers.ControladorServidor;
import controllers.ControladorConexion;
import controllers.IActualizarServidor;
import controllers.IControladorConexion;

import views.IVistaServidor;
import views.IVistaConexion;
import views.Conexion;
import views.InterfazGraficaServidor;

public class Main {

    public static void main(String[] args) {
    	IReceptorEvento receptor = GestorEventos.getInstance();
    	GestorEventos.getInstance().setIRegistro(GestorFila.getInstance());
    	GestorEventos.getInstance().setIAtencion(GestorFila.getInstance());
    	
    	IColaTurno ICT=new ColaTurno();
    	GestorFila.getInstance().setCola(ICT);
    	
    	
    	Comunicador.getInstance().setReceptor(receptor);
    	
    	
    	
    	//Tengo que suscribir a GESTORFILAYTERMINALES AL GESTOR EVENTOS GESTOR EVENTOS VA A TENER muchas interfaces..
    	//notificador.suscribirse(GestorFilaYTerminales.getInstance());

    	IVistaConexion cView = new Conexion();
    	IConector comunicador = Comunicador.getInstance();
    	IEnviarEventoServidor EnviarAOtroServidor = Comunicador.getInstance();
    	IEstadoFila gestorFila = GestorFila.getInstance();
    	
    	IRedundanciaPasiva IRP = new GestorServidores(comunicador);
    	Comunicador.getInstance().setGestorServidores(IRP);
    	I_Sync sincronizador= new GestorDeSincronizacion(gestorFila,EnviarAOtroServidor,IRP);
    	GestorEventos.getInstance().setI_Sync(sincronizador);
    	IRP.setSincronizado(sincronizador);
    	
    	I_HeartBeat IHB = (I_HeartBeat)sincronizador;
    	GestorEventos.getInstance().setI_HeartBeat(IHB);
    	IRP.setI_HeartBeat(IHB);
    	
    	IControladorConexion ICC = new ControladorConexion(cView,IRP);
    	
    	IVistaServidor sView = new InterfazGraficaServidor();
    	IActualizarServidor IAS= new ControladorServidor(sView,cView);
    	Comunicador.getInstance().setControlador(IAS);
    	GestorFila.getInstance().setControlador(IAS);
    	GestorTerminales.getInstance().setControlador(IAS);
    	
    	
    	
    	ICC.Iniciar();
    	
    }
}