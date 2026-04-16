package eventos;

public class EventoNumeroTerminal extends Evento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numeroDeLaTerminal;
	public EventoNumeroTerminal(String procesoOrigen, String procesoDestino,int numero) {
		super(procesoOrigen, procesoDestino);
		this.numeroDeLaTerminal=numero;
	}
	
	public int getNumero() {
		return this.numeroDeLaTerminal;
	}

}
