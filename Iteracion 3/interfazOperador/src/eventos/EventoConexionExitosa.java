package eventos;

public class EventoConexionExitosa extends Evento {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int numeroDeLaTerminal;

	public EventoConexionExitosa(String procesoOrigen, String procesoDestino,int numero) {
		super(procesoOrigen, procesoDestino);
		this.numeroDeLaTerminal=numero;
	}
	
	public int getNumero() {
		return this.numeroDeLaTerminal;
	}
}
