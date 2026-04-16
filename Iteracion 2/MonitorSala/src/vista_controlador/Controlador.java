package vista_controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import comuEntreProcesos.ComunicacionEntreProcesos;
import gestorHistorial.GestorHistorial;


public class Controlador {
	
    private ConexionFrame conexionView;
    private VistaMonitor monitorView;
    

	private static Controlador instancia;

    private Controlador() {
        this.conexionView = new ConexionFrame();
        this.conexionView.setVisible(true);
        this.monitorView = new VistaMonitor();
        this.monitorView.setVisible(false);
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
        
        
    }
    
    public void estadoConectadoAServidor(String txt) {
    	conexionView.getBtnConectar().setEnabled(false);
    	conexionView.getBtnConectar().setText(txt);
    	conexionView.setVisible(false);
    	abrirVistaMonitor(); 		//se debería abrir la vista de Operador si estamos conectados al monitor
    								//aunque se haya perdido la conexion con la terminal cliente
    	
    }
    
    /**
     * Muestra la ventana principal del operador
     */
    private void abrirVistaMonitor() {
    	monitorView.setVisible(true);
        GestorHistorial gestor = GestorHistorial.getInstance();
        monitorView.actualizar(
                gestor.getUltimosTurnos(),
                gestor.getUltimoTurnoLlamado()
        );
    }
    
    public void actualizarVistaMonitor() {
    	GestorHistorial gestor = GestorHistorial.getInstance();
        monitorView.actualizar(
                gestor.getUltimosTurnos(),
                gestor.getUltimoTurnoLlamado()
        );   	
    }

	public void ActualizarVistaNumero(int numero) {
		monitorView.ActualizarTitulo(numero);
	}

}


