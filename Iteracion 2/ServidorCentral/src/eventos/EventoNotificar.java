package eventos;

public class EventoNotificar extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Turno TurnoLlamado;
	private int numeroTPAQueLlama;
	public EventoNotificar(String procesoOrigen, int numeroTerminal, String procesoDestino, Turno turno) {
		super(procesoOrigen, procesoDestino);
		this.TurnoLlamado=turno;
		this.numeroTPAQueLlama=numeroTerminal;
	}
	
	public Turno getTurno() {
		return this.TurnoLlamado;
	}
	public int getNumeroTPA() {
		return this.numeroTPAQueLlama;
	}
}
