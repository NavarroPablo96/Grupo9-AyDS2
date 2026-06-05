package eventos;

public class EventoTerminalCreada extends Evento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numeroDeLaTerminal;
	public EventoTerminalCreada(String procesoOrigen, String procesoDestino,int numero) {
		super(procesoOrigen, procesoDestino);
		this.numeroDeLaTerminal=numero;
	}
	
	public int getNumero() {
		return this.numeroDeLaTerminal;
	}

}
