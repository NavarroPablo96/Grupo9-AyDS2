package app;

import comunicacion.Comunicador;
import controllers.ControladorConexion;
import interfaces.IComunicador;
import interfaces.IVistaConexion;
import views.Conexion;

public class Main {

    public static void main(String[] args) {
        IComunicador comunicador = new Comunicador();
        IVistaConexion vistaConexion = new Conexion();
        ControladorConexion controladorConexion = new ControladorConexion(vistaConexion, comunicador);
        
        controladorConexion.iniciar();
    }
}