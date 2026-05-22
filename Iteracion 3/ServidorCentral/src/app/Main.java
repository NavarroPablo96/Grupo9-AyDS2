package app;


import gestorEventos.GestorEventos;
import gestorEventos.IReceptorEvento;
import gestorFila.ColaTurno;
import gestorFila.GestorFila;
import gestorFila.IColaTurno;
import gestorFila.IEstadoFila;
import gestorServidores.Comunicador;
import gestorServidores.GestorServidores;
import gestorServidores.IConector;
import gestorServidores.IEnviarEventoServidores;
import gestorServidores.IRedundanciaPasiva;
import gestorSincronizacion.GestorDeSincronizacion;
import gestorSincronizacion.I_HeartBeat;
import gestorSincronizacion.I_Sync;
import gestorTerminales.GestorTerminales;
import gestorTerminales.IEnviarEventoClientes;
import gestorTerminales.IGestorTerminal;
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
    	
    	
    	IEnviarEventoClientes enviador = GestorTerminales.getInstance();
    	IGestorTerminal gestorTerminales=GestorTerminales.getInstance();
    	Comunicador.getInstance().setGestorTerminal(gestorTerminales);
    	GestorFila.getInstance().setIEnviar(enviador);
    	IColaTurno ICT=new ColaTurno();
    	GestorFila.getInstance().setCola(ICT);
    	
    	
    	Comunicador.getInstance().setReceptor(receptor);
    	
    	
    	
    	//Tengo que suscribir a GESTORFILAYTERMINALES AL GESTOR EVENTOS GESTOR EVENTOS VA A TENER muchas interfaces..
    	//notificador.suscribirse(GestorFilaYTerminales.getInstance());

    	IVistaConexion cView = new Conexion();
    	IConector comunicador = Comunicador.getInstance();
    	IEnviarEventoServidores EnviarAOtroServidor = Comunicador.getInstance();
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