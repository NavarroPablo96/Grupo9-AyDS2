package gestorFila;

import java.util.ArrayList;
import java.util.List;

import eventos.Turno;

public class RegistroRellamar {

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
	
}
