package controllers;

import redundanciaPasiva.IRedundanciaPasiva;
import views.IVistaConexion;

public class ControladorConexion implements IControladorConexion{
	private IVistaConexion vista;
	private IRedundanciaPasiva gestorServidores;
	
	public ControladorConexion(IVistaConexion cV,IRedundanciaPasiva gS) {
		this.gestorServidores=gS;
		this.vista=cV;
		this.vista.mostrar();
		this.vista.setController(this);
	}
	
	@Override
	public void Iniciar() {
		vista.mostrar();
		//Boton Escuchar
	}

	@Override
	public void establecerConexion() {
		String ipClientePrimario			= vista.getIP_Cp();
		String ipSincronizacionPrimario		= vista.getIP_Sp();
		String ipClienteSecundario			= vista.getIP_Cs();
		String ipSincronizacionSecundario	= vista.getIP_Ss();
		int puertoClientePrimario			= vista.getPuerto_Cp();
		int puertoSincronizacionPrimario	= vista.getPuerto_Sp();
		int puertoClienteSecundario			= vista.getPuerto_Cs();
		int puertoSincronizacionSecundario	= vista.getPuerto_Ss();

		System.out.println("Boton Escuchar ->GestorServidores");
		this.gestorServidores.iniciarServidor(
				ipClientePrimario,			puertoClientePrimario,
				ipSincronizacionPrimario,	puertoSincronizacionPrimario,
				ipClienteSecundario,		puertoClienteSecundario,
				ipSincronizacionSecundario,	puertoSincronizacionSecundario);
	}
}
