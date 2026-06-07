package eventos;

public class EventoActualizacionLlamarSiguiente extends EventoDeActualizacion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Turno TurnoLlamado;
	private int numeroTerminalQueLlama;
	public EventoActualizacionLlamarSiguiente(Turno t, int numeroTerminal) {
		this.TurnoLlamado=t;
		this.numeroTerminalQueLlama=numeroTerminal;
	}
	public Turno getTurnoLlamado() {
		return TurnoLlamado;
	}
	public int getNumeroTerminalQueLlama() {
		return numeroTerminalQueLlama;
	}
	
	
}
