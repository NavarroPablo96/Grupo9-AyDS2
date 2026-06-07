package app;

import controllers.ControladorServidor;
import controllers.ControladorConexion;
import controllers.IActualizarServidor;
import controllers.IControladorConexion;
import views.IVistaServidor;
import views.IVistaConexion;
import views.Conexion;
import views.InterfazGraficaServidor;

public class MainServidor {

    public static void main(String[] args) {
    	// 1. Crear Vistas
    	IVistaConexion cView = new Conexion();
    	IVistaServidor sView = new InterfazGraficaServidor();
    	
    	// 2. Crear Facade
    	IServidorFacade facade = new ServidorFacade();
    	
    	// 3. Crear Controladores
    	IActualizarServidor IAS = new ControladorServidor(sView, cView);
    	IControladorConexion ICC = new ControladorConexion(cView, facade);
    	
    	// 4. Registrar controlador de actualización en el Facade
    	facade.setControladorActualizacion(IAS);
    	
    	// 5. Iniciar la aplicación
    	ICC.Iniciar();
    }
}