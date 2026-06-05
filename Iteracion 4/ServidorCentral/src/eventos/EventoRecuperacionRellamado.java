package eventos;

public class EventoRecuperacionRellamado extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Turno turno;
    private int cantidadVecesLlamado;
    private int numeroTerminal;

	public EventoRecuperacionRellamado(Turno t,int cantidad, int numeroTerminal) {
		super("servidor", "terminalAtencion");
    	this.turno=t;
    	this.cantidadVecesLlamado=cantidad;
    	this.numeroTerminal=numeroTerminal;
	}
	
	public Turno getTurno() {
		return turno;
	}

	public int getCantidadVecesLlamado() {
		return cantidadVecesLlamado;
	}

	public int getNumeroTerminal() {
		return numeroTerminal;
	}
}
