package eventos;

public class EventoRellamar extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Turno turnoARellamar;

	public EventoRellamar(String procesoOrigen, String procesoDestino, Turno t) {
		super(procesoOrigen, procesoDestino);
		this.turnoARellamar=t;
	}
	
	public Turno getTurno() {
		return this.turnoARellamar;
	}

}
