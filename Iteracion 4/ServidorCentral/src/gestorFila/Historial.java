package gestorFila;

import java.io.Serializable;
import java.util.ArrayList;
import eventos.Turno;

public class Historial implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int MAX_TURNOS_EN_PANTALLA = 4;
	
	private ArrayList<Turno>historial ;
	private Turno turnoActual;

	
	public Historial(){
		this.historial = new ArrayList<Turno>();
		this.turnoActual=null;
	}


	public void llamarSiguiente(Turno t, int numeroTerminal) {
		t.setNumeroTerminal(numeroTerminal);
		if(turnoActual!=null) {
        	historial.add(0, turnoActual);
        }
        this.turnoActual=t;
        if (historial.size() > MAX_TURNOS_EN_PANTALLA) {
            historial.remove(historial.size() - 1); // elimina el más antiguo
        }
        System.out.println("Historial30- LlamarSiguiente FIn histo.Size="+historial.size());
	}
	
	public void rellamar(Turno turnoRellamar) {
		if(turnoActual==null) {
        	turnoActual=turnoRellamar;
        }
        else {
        	if(turnoActual.getNumero() != turnoRellamar.getNumero()){
        		Turno encontrado = null;	//Es para ver si está en el historial.

        	    for (Turno t : historial) {
        	        if (t.getNumero() == turnoRellamar.getNumero()) {
        	            encontrado = t;
        	            break;
        	        }
        	    }
        	    
        	    if (encontrado != null) {
        	    	// encontra es el turno que estaba en el historial
        	        historial.remove(encontrado);
        	    }
        	    historial.add(0, turnoActual);
        	    turnoActual = turnoRellamar;	        	    	
        		
        	}
        }
		System.out.println("Historial57- ReLLamar FIn");
	}


	public Turno getTurnoActual() {
		return turnoActual;
	}


	public ArrayList<Turno> getHistorial() {
		return this.historial;
	}


	public void setTurnoActual(Turno turno) {
		this.turnoActual=turno;
	}
	
	public void mostrar() {
		System.out.println("Turno Actual #"+this.turnoActual.getNumero()+" Dni:"+this.turnoActual.getDocumento());
		System.out.println("Historial.size()="+this.historial.size());
	}


	public Historial generarCopia() {
	    Historial copia = new Historial();
	    copia.turnoActual = this.turnoActual;
	    for (Turno t : this.historial) {
	        copia.historial.add(t);
	    }
	    return copia;
	}
}
