package app;

import javax.swing.SwingUtilities;

import comuEntreProcesos.ComunicacionEntreProcesos;
import comuEntreProcesos.IRecibirEvento;
import gestorInterfazOperador.GestorFila;
import vista_controlador.Controlador;

public class Main {

    public static void main(String[] args) {
        IRecibirEvento notificador = ComunicacionEntreProcesos.getInstance();
        notificador.suscribirse(GestorFila.getInstance());
        SwingUtilities.invokeLater(() -> Controlador.getInstance().initControl());
    }
}