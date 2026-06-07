package eventos;

public class EventoActualizacionRellamar extends EventoDeActualizacion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Turno TurnoLlamado;
	private int numeroTerminalQueLlama;
	
	public EventoActualizacionRellamar(Turno turnoRellamar, int numeroTPA) {
		this.TurnoLlamado=turnoRellamar;
		this.numeroTerminalQueLlama=numeroTPA;
	}
	public Turno getTurnoLlamado() {
		return TurnoLlamado;
	}
	public int getNumeroTerminalQueLlama() {
		return numeroTerminalQueLlama;
	}
}
