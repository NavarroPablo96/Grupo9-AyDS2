package eventos;

public class EventoLlamarSiguiente extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numeroTPAQueLlama;


    public EventoLlamarSiguiente(String origen, int numeroTerminal, String destino) {
		super(origen, destino);
		this.numeroTPAQueLlama=numeroTerminal;
	}
	public int getNumeroTPA() {
		return this.numeroTPAQueLlama;
	}
}