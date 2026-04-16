package vista_controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import comunicacionConTerminales.ComunicacionesConTerminales;
import gestorFilaYTerminales.GestorFilaYTerminales;

public class ControladorServidor implements IActualizarServidor{
		
	private InterfazGraficaServidor ServidorView;
	private ConexionFrame  conexionView;
	private String Escuchando;
	
	
	
	private static ControladorServidor instancia;
	private ControladorServidor() {
		this.conexionView= new ConexionFrame();
		this.conexionView.setVisible(true);
		
		this.ServidorView= new InterfazGraficaServidor();
		this.ServidorView.setVisible(false);
		
	}
	public static ControladorServidor getInstance() {
		if(instancia ==null) {
			instancia=new ControladorServidor();
		}
		return instancia;
	}
	
	public void initControl() {
		//Boton Escuchar
		conexionView.getBtnEscuchar().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String ip = conexionView.getTxtReceptorIP().getText();
                int puerto = Integer.parseInt(conexionView.getTxtReceptorPuerto().getText());
                
                System.out.println("Intentando escuchar en IP: " + ip + " Puerto: " + puerto);
                ComunicacionesConTerminales.getInstance().iniciarServidor(ip,puerto);
			}
		});
		
		
	}
	public void estadoEscuchando(String escuchandoEn) {
		this.Escuchando=escuchandoEn;
    	conexionView.getBtnEscuchar().setEnabled(false);
    	conexionView.getBtnEscuchar().setText(escuchandoEn);
    	conexionView.setVisible(false);
		ServidorView.setVisible(true);
		actualizarVistaServidor();
		
	}
	public void actualizarVistaServidor() {
		ServidorView.setVisible(true);
        GestorFilaYTerminales gestor = GestorFilaYTerminales.getInstance();
        ServidorView.actualizar(
                gestor.getListaTurnos(),
                gestor.getListaTerminalesRegistro(),
                gestor.getListaTerminalesAtencion(),
                gestor.getListaTerminalesNotificacion(),
                this.Escuchando
        );
	}
	
}
