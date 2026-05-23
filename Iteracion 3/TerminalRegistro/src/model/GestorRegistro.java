package model;

import comunicacion.IRegistro;

public class GestorRegistro implements IGestorRegistro{
	
	private int NumeroTerminal;
	private IRegistro comunicador;
	
	public GestorRegistro(IRegistro c) {
		this.comunicador = c;
	}


	//IGestorTerminal
	@Override
	public void registrarTurno(String dni) {
		//El evento que se deberia enviar deberia llamarse SolicitudTurno(DNI,hora,horaReal) 
		//Tiene todo lo necesario para crear el truno
		comunicador.nuevoTurno(dni,NumeroTerminal);
	}

	@Override
	public void setNumeroTerminal(int nro){
		this.NumeroTerminal = nro;
	}

	@Override
	public int getNumero() {
		return this.NumeroTerminal;
	}
	
	
	// public void CrearTurno(String dni, String hora,Date horaReal) {
		
	// 	Turno nuevo = new Turno(-1,dni, hora,horaReal);//TR=TerminalDeRegistro // TA=TerminalDeAtencion // TN=TerminalNotificacion
	// 	EventoNuevoTurno nuevoTurno = new EventoNuevoTurno("TR"+this.NumeroTerminal,"Servidor",nuevo);
	// 	ComunicacionEntreProcesos.getInstance().enviarEvento(nuevoTurno);
	// 	//Controlador.getInstance().ActualizarVista(nuevo);
	// }
}
