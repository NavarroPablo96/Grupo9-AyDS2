package eventos;

import gestorFila.Historial;
import gestorFila.IColaTurno;
import gestorFila.RegistroRellamar;

public class EventoSincronizacionEstado extends Evento {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IColaTurno cola;
	private int nts,cp,cs;
	private Historial historial;
	private RegistroRellamar llamados;


	public EventoSincronizacionEstado(IColaTurno cola, int nts, int cp, int cs,Historial h, RegistroRellamar l) {
		super("Sincronizador", "Sincronizable");
		this.cola=cola;
		this.nts=nts;
		this.cp=cp;
		this.cs=cs;
		this.historial=h;
		this.llamados=l;
	}

	public IColaTurno getCola() {
		return cola;
	}

	public int getNts() {
		return nts;
	}

	public int getCp() {
		return cp;
	}

	public int getCs() {
		return cs;
	}

	public Historial getHistorial() {
		return historial;
	}

	public RegistroRellamar getLlamados() {
		return llamados;
	}
	
}
