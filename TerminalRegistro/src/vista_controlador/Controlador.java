package vista_controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import comuEntreProcesos.ComunicacionEntreProcesos;
import comuEntreProcesos.Turno;
import gestorTurnos.GestorTurnos;





public class Controlador {
	
    private ConexionFrame conexionView;
    private TerminalRegistro terminalView;
    

	private static Controlador instancia;

    private Controlador() {
        this.conexionView = new ConexionFrame();
        this.conexionView.setVisible(true);
        this.terminalView = new TerminalRegistro();
        this.terminalView.setVisible(false);
    }
    
    public static Controlador getInstance() {
        if (instancia == null) {
            instancia = new Controlador();
        }
        return instancia;
    }

    /**
     * Inicializa los listeners de los botones
     */
    public void initControl() {
        
        // Botón "Conectar" del emisor
        conexionView.getBtnConectar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = conexionView.getTxtEmisorIP().getText();
                int puerto = Integer.parseInt(conexionView.getTxtEmisorPuerto().getText());
                System.out.println("Conectando a IP: " + ip + " Puerto: " + puerto);
                
                try {
					ComunicacionEntreProcesos.getInstance().conectar(ip, puerto);
				} catch (IOException e1) {
					System.out.println("No fue posible conectarse al monitor");
					//e1.printStackTrace();
				}

            }
        });
        terminalView.getBtnRegistrar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //GestorTurnos.getInstance().llamarSiguiente();
            	System.out.println("Intentando crear un turno");
            	String dni  = terminalView.getTxtDNI().getText();
            	Date horaReal = new Date();
                String hora = new SimpleDateFormat("HH:mm").format(horaReal);
                terminalView.getTxtDNI().setText("");
                
                GestorTurnos.getInstance().CrearTurno(dni,hora,horaReal);
            }
        });
        
    }
    
    public void estadoConectadoAOperador(String txt) {
    	conexionView.getBtnConectar().setEnabled(false);
    	conexionView.getBtnConectar().setText(txt);
    	conexionView.setVisible(false);
    	abrirVistaOperador(); 		//se debería abrir la vista de Operador si estamos conectados al monitor
    								//aunque se haya perdido la conexion con la terminal cliente
    	
    }

    /**
     * Muestra la ventana principal del operador
     */
    private void abrirVistaOperador() {
    	terminalView.setVisible(true);
    }

	public void ActualizarVista(Turno nuevo) {
		terminalView.ActualizaVista(nuevo);
	}

}


