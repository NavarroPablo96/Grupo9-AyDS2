package eventos;

public class EventoFilaNoVacia extends Evento{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int CantidadTurno;

	public EventoFilaNoVacia(String procesoOrigen, String procesoDestino,int cant) {
		super(procesoOrigen, procesoDestino);
		this.CantidadTurno=cant;
	}
	
	public int getCantTurno() {
		return this.CantidadTurno;
	}

}
