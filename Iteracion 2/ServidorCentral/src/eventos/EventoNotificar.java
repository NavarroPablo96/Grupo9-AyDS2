package eventos;

public class EventoNotificar extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Turno TurnoLlamado;
	public EventoNotificar(String procesoOrigen, String procesoDestino, Turno turno) {
		super(procesoOrigen, procesoDestino);
		this.TurnoLlamado=turno;
	}
	
	public Turno getTurno() {
		return this.TurnoLlamado;
	}
}
