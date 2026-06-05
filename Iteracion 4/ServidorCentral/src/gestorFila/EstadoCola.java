package gestorFila;

import java.io.Serializable;

public class EstadoCola implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ColaTurno cola;
	private int numeroTurnoSiguiente;
	private int cantidadPone;
	private int cantidadSaca;
	
	public EstadoCola() {
		
	}
	
	public ColaTurno getCola() {
		return cola;
	}
	public void setCola(ColaTurno cola, int numeroTurnoSiguiente, int cantidadPone, int cantidadSaca) {
		this.cola = cola;
		this.numeroTurnoSiguiente=numeroTurnoSiguiente;
		this.cantidadPone=cantidadPone;
		this.cantidadSaca=cantidadSaca;
	}
	public int getCantidadPone() {
		return cantidadPone;
	}

	public int getCantidadSaca() {
		return cantidadSaca;
	}
	public int getNumeroTurnoSiguiente() {
		return numeroTurnoSiguiente;
	}
}
