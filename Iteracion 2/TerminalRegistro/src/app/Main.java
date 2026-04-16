package app;

import javax.swing.SwingUtilities;

import comuEntreProcesos.ComunicacionEntreProcesos;
import gestorTurnos.GestorTurnos;
import vista_controlador.Controlador;

public class Main {

    public static void main(String[] args) {
    	ComunicacionEntreProcesos.getInstance().suscribirse(GestorTurnos.getInstance());
        SwingUtilities.invokeLater(() -> Controlador.getInstance().initControl());
    }
}