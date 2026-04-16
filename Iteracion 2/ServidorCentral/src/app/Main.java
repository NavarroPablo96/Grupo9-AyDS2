package app;

import javax.swing.SwingUtilities;

import comunicacionConTerminales.ComunicacionesConTerminales;
import comunicacionConTerminales.IRecibirEvento;
import gestorFilaYTerminales.GestorFilaYTerminales;
import vista_controlador.ControladorServidor;
import vista_controlador.IActualizarServidor;

public class Main {

    public static void main(String[] args) {
    	IRecibirEvento notificador = ComunicacionesConTerminales.getInstance();
    	notificador.suscribirse(GestorFilaYTerminales.getInstance());
    	IActualizarServidor IAS=ControladorServidor.getInstance();
    	GestorFilaYTerminales.getInstance().setControlador(IAS);
    	SwingUtilities.invokeLater(() -> ControladorServidor.getInstance().initControl());
    }
}