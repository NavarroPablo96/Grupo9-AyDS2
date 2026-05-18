package app;

import javax.swing.SwingUtilities;

import comuEntreProcesos.ComunicacionEntreProcesos;
import comuEntreProcesos.IRecibirEvento;
import gestorHistorial.GestorHistorial;
import vista_controlador.Controlador;

public class Main {

    public static void main(String[] args) {
        IRecibirEvento notificador = ComunicacionEntreProcesos.getInstance();
        notificador.suscribirse(GestorHistorial.getInstance());
        SwingUtilities.invokeLater(() -> Controlador.getInstance().initControl());
    }
}