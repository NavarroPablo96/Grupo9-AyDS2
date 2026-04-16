package eventos;

public class TurnoAsignado extends Evento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Turno turnoAsignado;

	public TurnoAsignado(String procesoOrigen, String procesoDestino,Turno asignado) {
		super(procesoOrigen, procesoDestino);
		this.turnoAsignado=asignado;
	}

	public Turno getTurno() {
		return turnoAsignado;
	}

}
