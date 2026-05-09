package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import eventos.EventoNuevoTurno;
import eventos.Turno;
import interfaces.IComunicador;
import interfaces.IRegistro;

public class GestorTerminal implements IRegistro{
	
	private int NumeroTerminal;
	private IComunicador comunicador;
	
	public GestorTerminal(IComunicador c) {
		this.comunicador = c;
	}

	public void nuevoTurno(String dni){
        
		Date horaReal = new Date();
        String hora = new SimpleDateFormat("HH:mm").format(horaReal);

		Turno nuevo = new Turno(-1,dni, hora,horaReal);//TR=TerminalDeRegistro // TA=TerminalDeAtencion // TN=TerminalNotificacion
		EventoNuevoTurno nuevoTurno = new EventoNuevoTurno("TR"+this.NumeroTerminal,"Servidor",nuevo);

		comunicador.enviarEvento(nuevoTurno);
	}

	public void setNumeroTerminal(int nro){
		this.NumeroTerminal = nro;
	}
	// public void CrearTurno(String dni, String hora,Date horaReal) {
		
	// 	Turno nuevo = new Turno(-1,dni, hora,horaReal);//TR=TerminalDeRegistro // TA=TerminalDeAtencion // TN=TerminalNotificacion
	// 	EventoNuevoTurno nuevoTurno = new EventoNuevoTurno("TR"+this.NumeroTerminal,"Servidor",nuevo);
	// 	ComunicacionEntreProcesos.getInstance().enviarEvento(nuevoTurno);
	// 	//Controlador.getInstance().ActualizarVista(nuevo);
	// }
}
