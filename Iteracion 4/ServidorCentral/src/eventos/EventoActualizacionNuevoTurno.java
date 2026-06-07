package eventos;

public class EventoActualizacionNuevoTurno extends EventoDeActualizacion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Turno nuevo;
	public EventoActualizacionNuevoTurno(Turno nuevoCreado) {
		super();
		this.nuevo=nuevoCreado;
	}
	public Turno getTurno() {
		return nuevo;
	}
	

}
