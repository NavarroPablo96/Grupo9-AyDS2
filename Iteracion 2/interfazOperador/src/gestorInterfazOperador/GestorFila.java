package gestorInterfazOperador;


import comuEntreProcesos.ComunicacionEntreProcesos;
import comuEntreProcesos.IReceptorEvento;
import eventos.Evento;
import eventos.EventoFilaNoVacia;
import eventos.EventoFilaVacia;
import eventos.EventoLlamarSiguiente;
import eventos.EventoNumeroTerminal;
import eventos.EventoRellamar;
import eventos.Turno;
import eventos.TurnoAsignado;
import vista_controlador.Controlador;

public class GestorFila implements IReceptorEvento {

	private static GestorFila instance;
	private int NumeroTerminal;
    private boolean FilaVacia;

    private GestorFila() {
        this.ultimoTurnoLlamado = null;
        this.CantidadEnEspera = 0;
    }

    public static GestorFila getInstance() {
        if (instance == null) {
            instance = new GestorFila();
        }
        return instance;
    }
    

    private Turno ultimoTurnoLlamado;
    private int CantidadDeVecesLlamado,CantidadEnEspera;


    // Último turno llamado
    public Turno getUltimoTurnoLlamado() {
        return ultimoTurnoLlamado;
    }

    // Cantidad en espera
    public int getCantidadEnEspera() {
        return this.CantidadEnEspera;
    }

    // 🔹 Cantidad atendidos
    public int getCantidadDeVecesLlamado() {
        return CantidadDeVecesLlamado;
    }
   
	@Override
	public void ArriboEvento(Evento e) {
		System.out.println("Llega un evento:"  + e);
		if (e instanceof EventoNumeroTerminal) {
	    	EventoNumeroTerminal ent =(EventoNumeroTerminal) e;
	    	this.NumeroTerminal=ent.getNumero();
	    	Controlador.getInstance().ActualizarVistaNumero(ent.getNumero());
	    }
		else if (e instanceof TurnoAsignado) {
			
			TurnoAsignado evento = (TurnoAsignado) e;
	        Turno turno = evento.getTurno();
	        this.ultimoTurnoLlamado=turno;
	        this.CantidadDeVecesLlamado=1;
	        System.out.println("Llego el TurnoAsignado DNI="+turno.getDocumento());
	        Controlador.getInstance().actualizarVistaOperador();
	    }
		else if(e instanceof EventoFilaNoVacia) {
			EventoFilaNoVacia EFNV = (EventoFilaNoVacia) e;
			this.CantidadEnEspera=EFNV.getCantTurno();
			Controlador.getInstance().estadoFilaNoVacia();
		}
	    else if(e instanceof EventoFilaVacia){
	    	this.CantidadEnEspera=0;
			Controlador.getInstance().estadoFilaVacia(); 	
	    }
	    else{
	    	System.out.println("Llego un Evento");
	    	System.out.println("Tipo: " + e.getClass().getName());
	        System.out.println("Origen: " + e.getProcesoOrigen());
	        System.out.println("Destino: " + e.getProcesoDestino());
	    }
	}
	
	public void setFilaVacia(boolean vacia) {
		this.FilaVacia=vacia;
	}

	public void llamarSiguiente() {
		if(this.FilaVacia) {
			Controlador.getInstance().CartelFilaVacia();
		}
		else {
			String Origen = "TA" + this.NumeroTerminal;
			EventoLlamarSiguiente evento = new EventoLlamarSiguiente(Origen,this.NumeroTerminal, "Notificador");
			ComunicacionEntreProcesos.getInstance().enviarEvento(evento);
		}
	}

	public void ReNotificar() {
		String Origen = "TA" + this.NumeroTerminal;
		if(this.CantidadDeVecesLlamado<3) {
			this.CantidadDeVecesLlamado++;
			EventoRellamar evento = new EventoRellamar(Origen,this.NumeroTerminal,"Notificador",this.ultimoTurnoLlamado);
			ComunicacionEntreProcesos.getInstance().enviarEvento(evento);
		}
		else {
			this.CantidadDeVecesLlamado=0;
			this.ultimoTurnoLlamado=null;
			Controlador.getInstance().seDebeLlamarSiguiente("El cliente ya fue llamado 3 veces");
		}
		Controlador.getInstance().actualizarVistaOperador();
	}

    
    

}
