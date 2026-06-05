package eventos;

public class EventoSolicitudSincronizacion extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EventoSolicitudSincronizacion(String procesoOrigen, String procesoDestino) {
		super(procesoOrigen, procesoDestino);
	}

}
