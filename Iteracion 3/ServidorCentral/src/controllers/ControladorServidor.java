package controllers;

import java.util.List;

import eventos.Turno;
import views.IVistaConexion;
import views.IVistaServidor;

public class ControladorServidor implements IActualizarServidor{

	private IVistaServidor vista;
	private IVistaConexion vistaConexion;
	private String Escuchando;

	
	
	public ControladorServidor(IVistaServidor ivs,IVistaConexion ivc) {
		this.vista=ivs;
		this.vista.cerrar();
		//this.vista.mostrar();//Deberia Comentar esta linea. no?
		this.vistaConexion=ivc;
		
	}
	



	@Override
	public void estadoEscuchando(String escuchandoEn) {
		this.Escuchando=escuchandoEn;
		vistaConexion.desactivarBoton(escuchandoEn);
		this.vistaConexion.cerrar();
    	this.vista.mostrar();
    	this.vista.setTituloServidor(escuchandoEn);
	}




/*	@Override
	public void actualizarVistaServidor() {
		this.vista.mostrar();
		GestorFila gestor = GestorFila.getInstance();
        GestorTerminales gesTerm = GestorTerminales.getInstance();
        this.vista.actualizar(
        		gestor.getListaTurnos(),
                gesTerm.getListaTerminalesRegistro(),
                gesTerm.getListaTerminalesAtencion(),
                gesTerm.getListaTerminalesNotificacion(),
                this.Escuchando);
	}*/




	@Override
	public void actualizarTerminalesVistaServidor(List<String> TerminalesRegistro, List<String> TerminalesAtencion,
			List<String> TerminalesNotificacion) {
		this.vista.mostrar();
		this.vista.actualizarTerminalesVistaServidor(TerminalesRegistro, TerminalesAtencion, TerminalesNotificacion);
	}



	@Override
	public void actualizarTurnosVistaServidor(List<Turno> Turnos) {
		this.vista.mostrar();
		this.vista.actualizarTurnosVistaServidor(Turnos);
		
	}
	
}
