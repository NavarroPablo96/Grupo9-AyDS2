package gestorFilaYTerminales;

import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

import eventos.Turno;

public class ColaTurno implements IColaTurno {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Turno> fila;

	public ColaTurno(){
		inicia();
	}

	public synchronized List<Turno>  getListaTurnos() {
		return fila;
	}

	public int getCantidadTurnos() {
	    return (fila != null) ? fila.size() : 0;
	}
	
	public void ordenar() {
        fila.sort(Comparator.comparingInt(Turno::getNumero));
	}

	public boolean DniRegistrado(String documento) {
		return fila.stream()
	            .anyMatch(t -> t.getDocumento().equals(documento));
	}

	@Override
	public void inicia() {
		this.fila = new ArrayList<Turno>();
		
	}

	@Override
	public synchronized void pone(Turno t) {
		this.fila.add(t);		
	}

	@Override
	public synchronized Turno saca() {
		return fila.remove(0);
	}

	@Override
	public synchronized int getCantidad() {
	    return (fila != null) ? fila.size() : 0;
	}

	@Override
	public synchronized void mostrarCola() {

	    System.out.println("----- COLA DE TURNOS -----");

	    if (fila == null || fila.isEmpty()) {
	        System.out.println("La cola esta vacia");
	        return;
	    }

	    for (Turno t : fila) {
	        System.out.println(
	            "Turno: " + t.getNumero() +
	            " | DNI: " + t.getDocumento()
	        );
	    }

	    System.out.println("Cantidad total: " + fila.size());
	}

	@Override
	public IColaTurno generarCopia() {
	    ColaTurno copia = new ColaTurno();

	    for (Turno t : this.fila) {
	        copia.pone(t);
	    }

	    return copia;
	}
	
}
