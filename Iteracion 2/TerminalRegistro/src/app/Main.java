package app;

import javax.swing.SwingUtilities;

import vista_controlador.Controlador;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> Controlador.getInstance().initControl());
    }
}