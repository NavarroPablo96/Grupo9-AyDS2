package model;

import interfaces.IRegistro;

public class GestorTerminal{
	
	private int NumeroTerminal;
	private IRegistro comunicador;
	
	public GestorTerminal(IRegistro c) {
		this.comunicador = c;
	}

	public void setNumeroTerminal(int nro){
		this.NumeroTerminal = nro;
	}

	public int getNumero() {
		return this.NumeroTerminal;
	}

	public void registrarTurno(String dni) {
		//El evento que se deberia enviar deberia llamarse SolicitudTurno(DNI,hora,horaReal) 
		//Tiene todo lo necesario para crear el truno
		comunicador.nuevoTurno(dni,NumeroTerminal);
	}
	
	
	// public void CrearTurno(String dni, String hora,Date horaReal) {
		
	// 	Turno nuevo = new Turno(-1,dni, hora,horaReal);//TR=TerminalDeRegistro // TA=TerminalDeAtencion // TN=TerminalNotificacion
	// 	EventoNuevoTurno nuevoTurno = new EventoNuevoTurno("TR"+this.NumeroTerminal,"Servidor",nuevo);
	// 	ComunicacionEntreProcesos.getInstance().enviarEvento(nuevoTurno);
	// 	//Controlador.getInstance().ActualizarVista(nuevo);
	// }
}
