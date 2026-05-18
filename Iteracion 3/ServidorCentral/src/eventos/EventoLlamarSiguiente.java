package eventos;

public class EventoLlamarSiguiente extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    public EventoLlamarSiguiente(String origen, String destino,Turno turno) {
		super(origen, destino);
	}

}