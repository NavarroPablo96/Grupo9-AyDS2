package eventos;

import java.util.ArrayList;

public class EventoRecuperacionHistorial extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Turno turnoActual;
	private ArrayList<Turno> listaEnMonitor;
	
	public EventoRecuperacionHistorial(Turno actual,ArrayList<Turno> lista) {
		super("servidor", "notificador");
		this.turnoActual=actual;
		this.listaEnMonitor=lista;
	}

	public Turno getTurnoActual() {
		return turnoActual;
	}

	public ArrayList<Turno> getListaEnMonitor() {
		return listaEnMonitor;
	}
	
	
	
	
}
