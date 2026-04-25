package vista_controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import comuEntreProcesos.ComunicacionEntreProcesos;
import gestorInterfazOperador.GestorFila;





public class Controlador {
	
    private ConexionFrame conexionView;
    private VistaOperador operadorView;

	private static Controlador instancia;

    private Controlador() {
        this.conexionView = new ConexionFrame();
        this.conexionView.setVisible(true);
        this.operadorView = new VistaOperador();
        this.operadorView.setVisible(false);
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
        operadorView.getBtnLlamar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestorFila.getInstance().llamarSiguiente();
            }
        });
        operadorView.getBtnNotificar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GestorFila.getInstance().ReNotificar();
            }
        });
        
    }
    
    public void CartelFilaVacia() {
    	operadorView.getBtnLlamar().setText("Fila Vacia");
    	operadorView.getBtnLlamar().setEnabled(false);
		JOptionPane.showMessageDialog(
        		operadorView,
                "No hay ningún cliente en la fila, intente más tarde.",
                "La Fila está vacía",
                JOptionPane.WARNING_MESSAGE
        );
        return;

    }
    
    
    public void estadoFilaVacia() {
    	GestorFila.getInstance().setFilaVacia(true);
    	actualizarVistaOperador();
    }
    
    
    public void estadoFilaNoVacia() {
    	System.out.println("Controlador.estadoFilaNoVacia()");
    	GestorFila.getInstance().setFilaVacia(false);
    	operadorView.getBtnLlamar().setText("Llamar siguiente");
    	operadorView.getBtnLlamar().setEnabled(true);	
    	actualizarVistaOperador();
	}

    /**
     * Muestra la ventana principal del operador
     */
    private void abrirVistaOperador() {
        operadorView.setVisible(true);
        GestorFila gestor = GestorFila.getInstance();
        operadorView.actualizar(
        		gestor.getUltimoTurnoLlamado(),
                gestor.getCantidadEnEspera(),
                gestor.getCantidadDeVecesLlamado()
        );
    }
    
    public void actualizarVistaOperador() {
        GestorFila gestor = GestorFila.getInstance();
        if(gestor.getUltimoTurnoLlamado()==null) {
        	operadorView.getBtnNotificar().setEnabled(false);
        }
        else {
        	operadorView.getBtnNotificar().setEnabled(true);
        }
        System.out.println("ACTUALZIANDO Vista OPERADOR - "+gestor.getCantidadEnEspera());
        operadorView.actualizar(
                gestor.getUltimoTurnoLlamado(),
                gestor.getCantidadEnEspera(),
                gestor.getCantidadDeVecesLlamado()
        );    	
    }

	public void estadoConectadoAServidor(String txt) {
    	conexionView.setVisible(false);
    	abrirVistaOperador();
	}

	public void ActualizarVistaNumero(int numeroTerminal) {
		operadorView.ActualizarTitulo(numeroTerminal);
	}

	public void seDebeLlamarSiguiente(String string) {
    	operadorView.getBtnNotificar().setEnabled(false);
        JOptionPane.showMessageDialog(
        		operadorView,
                "El cliente ya fue llamado 3 veces, se debe proceder con el siguiente.",
                "Cliente llamado muchas veces",
                JOptionPane.WARNING_MESSAGE
        );
        return;
	}

}


