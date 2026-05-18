package eventos;

public class EventoDniExistente extends Evento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dni;
	public EventoDniExistente(String procesoOrigen, String procesoDestino,String dni) {
		super(procesoOrigen, procesoDestino);
		this.dni=dni;
	}
	public String getDni() {
		return this.dni;
	}

}
