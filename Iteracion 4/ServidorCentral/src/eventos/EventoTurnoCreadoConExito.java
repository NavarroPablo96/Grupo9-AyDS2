package eventos;

public class EventoTurnoCreadoConExito extends Evento{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Turno turno;
	public EventoTurnoCreadoConExito(String procesoOrigen, String procesoDestino,Turno turno) {
		super(procesoOrigen, procesoDestino);
		this.turno=turno;
	}
	
	public Turno getTurno() {
		return this.turno;
	}

}
