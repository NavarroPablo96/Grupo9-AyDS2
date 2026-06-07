package model;

import comunicacion.IRegistro;

public class GestorRegistro implements IGestorRegistro{
	
	private int NumeroTerminal;
	public IRegistro comunicador;
	
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
	
	
	
}
