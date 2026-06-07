package controllers;

import app.ConfiguracionServidorDTO;
import app.IServidorFacade;
import views.IVistaConexion;

public class ControladorConexion implements IControladorConexion {
	private IVistaConexion vista;
	private IServidorFacade facade;
	
	public ControladorConexion(IVistaConexion cV, IServidorFacade facade) {
		this.facade = facade;
		this.vista = cV;
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
		String fabricaParaRecuperar			= vista.getFabricaRecuperar();
		String fabricaParaGuardar			= vista.getFabricaGuardar(); 
		String tipoEncriptado 				= vista.getTipoEncriptado();
		String clave 						= vista.getClave();

		System.out.println("Boton Escuchar -> ServidorFacade");
		ConfiguracionServidorDTO config = new ConfiguracionServidorDTO(
				ipClientePrimario,			puertoClientePrimario,
				ipSincronizacionPrimario,	puertoSincronizacionPrimario,
				ipClienteSecundario,		puertoClienteSecundario,
				ipSincronizacionSecundario,	puertoSincronizacionSecundario,
				fabricaParaRecuperar,		fabricaParaGuardar,
				tipoEncriptado, 			clave
		);
		this.facade.iniciar(config);
	}
}

