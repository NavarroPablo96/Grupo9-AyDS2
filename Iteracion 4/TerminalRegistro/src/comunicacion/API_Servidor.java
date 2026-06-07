package comunicacion;

import java.text.SimpleDateFormat;
import java.util.Date;

import eventos.EventoSolicitudTurno;
import seguridad.ISeguridadStrategy;

public class API_Servidor implements IRegistro{
	
	//PONER ENCRIPTADO ACA

	private ISeguridadStrategy encriptador;

	private static API_Servidor instancia=null;
	private API_Servidor(){
		
	}
	public static API_Servidor getInstance() {
		if(instancia ==null) {
			instancia = new API_Servidor();
		}
		return instancia;
	}
	
	
	@Override
	public void nuevoTurno(String dni, int NumeroTerminal) {

		Date horaReal = new Date();
        String hora = new SimpleDateFormat("HH:mm").format(horaReal);
		dni = encriptador.encriptar(dni);
		EventoSolicitudTurno solicitud = new EventoSolicitudTurno("TR"+NumeroTerminal,"Servidor",dni, hora, horaReal);
		ComunicadorRegistro.getInstance().enviarEvento(solicitud);
		
	}

	@Override
	public void setEncriptador(ISeguridadStrategy encriptador){
		this.encriptador = encriptador;
	}

}
