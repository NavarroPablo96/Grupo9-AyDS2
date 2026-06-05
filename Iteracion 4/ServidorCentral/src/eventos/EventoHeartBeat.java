package eventos;

public class EventoHeartBeat extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cantidadTurnos,cantidadPone,cantidadSaca;

	public EventoHeartBeat(int cantidadTurnos, int cantidadPone, int cantidadSaca) {
		super("Sincronizador", "Sincronizable");
		this.cantidadTurnos=cantidadTurnos;
		this.cantidadPone=cantidadPone;
		this.cantidadSaca=cantidadSaca;
	}

	public int getCantidadTurnos() {
		return cantidadTurnos;
	}

	public void setCantidadTurnos(int cantidadTurnos) {
		this.cantidadTurnos = cantidadTurnos;
	}

	public int getCantidadPone() {
		return cantidadPone;
	}

	public void setCantidadPone(int cantidadPone) {
		this.cantidadPone = cantidadPone;
	}

	public int getCantidadSaca() {
		return cantidadSaca;
	}

	public void setCantidadSaca(int cantidadSaca) {
		this.cantidadSaca = cantidadSaca;
	}
}
