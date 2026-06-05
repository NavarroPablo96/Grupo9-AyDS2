package eventos;

public class EventoRellamar extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Turno turnoARellamar;
	private int numeroTPAQueLlama;

	public EventoRellamar(String procesoOrigen, int numeroTerminal, String procesoDestino, Turno t) {
		super(procesoOrigen, procesoDestino);
		this.turnoARellamar=t;
		this.numeroTPAQueLlama=numeroTerminal;
	}
	
	public Turno getTurno() {
		return this.turnoARellamar;
	}
	public int getNumeroTPA() {
		return this.numeroTPAQueLlama;
	}

}
