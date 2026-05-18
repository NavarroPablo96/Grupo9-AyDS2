package eventos;

import gestorFilaYTerminales.IColaTurno;

public class EventoSincronizacionEstado extends Evento {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IColaTurno cola;
	private int ct,cp,cs;

	public EventoSincronizacionEstado(IColaTurno cola, int ct, int cp, int cs) {
		super("Sincronizador", "Sincronizable");
		this.cola=cola;
		this.ct=ct;
		this.cp=cp;
		this.cs=cs;
	}

	public IColaTurno getCola() {
		return cola;
	}

	public int getCt() {
		return ct;
	}

	public int getCp() {
		return cp;
	}

	public int getCs() {
		return cs;
	}
}
