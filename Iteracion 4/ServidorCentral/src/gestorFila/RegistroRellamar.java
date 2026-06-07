package gestorFila;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eventos.Turno;

public class RegistroRellamar implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Llamado> llamados ;

    public RegistroRellamar() {
    	this.llamados=new ArrayList<Llamado>();
    }
    
    
	public List<Llamado> getLlamados() {
		return llamados;
	}
	
	public void llamarSiguiente(int numeroTerminal,Turno turnoAtendiendo) {
	    // eliminar si ya existía uno para ese terminal
	    Llamado existente = buscarPorTerminal(numeroTerminal);
	    if (existente != null) {
	        llamados.remove(existente);
	    }
	    // crear nuevo llamado
	    Llamado nuevo = new Llamado(turnoAtendiendo, 1, numeroTerminal);
	    llamados.add(nuevo);
	    System.out.println("RegistroRellamar 41 - llamarSiguiente FIN");
	}


	public void rellamar(int numeroTerminal) {
	    Llamado actual = buscarPorTerminal(numeroTerminal);
	    if (actual == null) {
	        return; // o podrías crear uno nuevo si querés
	    }
	    actual.setCantidadVecesLlamado(
	            actual.getCantidadVecesLlamado() + 1
	    );
	    System.out.println("RegistroRellamar 41 - Rellamar FIN");
	}
	
	public Llamado getLlamado(int numeroTerminal) {
		return buscarPorTerminal(numeroTerminal);
	}
	
	private Llamado buscarPorTerminal(int numeroTerminal) {
	    for (Llamado l : llamados) {
	        if (l.getNumeroTerminal() == numeroTerminal) {
	            return l;
	        }
	    }
	    return null;
	}

	public RegistroRellamar generarCopia() {
	    RegistroRellamar copia = new RegistroRellamar();
	    for (Llamado l : this.llamados) {
	        copia.getLlamados().add(l);
	    }
	    return copia;
	}


	public void mostrar() {
	    System.out.println("=== RegistroRellamar ===");
	    System.out.println("Cantidad de llamados: " + llamados.size());
	    for (Llamado l : llamados) {
	        System.out.println("Terminal: " + l.getNumeroTerminal()+ " | Turno: " + l.getTurno().getNumero()
	        		+ " | Veces llamado: " + l.getCantidadVecesLlamado());
	    }
	}
	
}
