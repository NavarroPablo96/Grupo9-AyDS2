package gestores;



import comunicacion.IAtencion;
import controller.ControladorOperador;
import interfaces.IReceptorEvento;
import seguridad.ISeguridadStrategy;
import eventos.Evento;
import eventos.EventoConexionExitosa;
import eventos.EventoFilaNoVacia;
import eventos.EventoFilaVacia;
import eventos.EventoRecuperacionRellamado;
import eventos.Turno;
import eventos.TurnoAsignado;

public class GestorLlamado implements ILlamado, IReceptorEvento{

	private int NumeroTerminal;
    private boolean FilaVacia;
    private Turno ultimoTurnoLlamado;
    private int CantidadDeVecesLlamado, CantidadEnEspera;
	private IAtencion apiServidor;
	private ControladorOperador controlador;
	private ISeguridadStrategy encriptador;

	public GestorLlamado(IAtencion apiServidor){
		this.apiServidor = apiServidor;
	}
	public void setControladorOperador(ControladorOperador controlador) {
		this.controlador = controlador;		
	}


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
	
	private void setFilaVacia(boolean vacia) {
		this.FilaVacia=vacia;
	}

	public void llamarSiguiente() {
		if(this.FilaVacia) {
			controlador.CartelFilaVacia();
		}
		else {
			this.apiServidor.llamarSiguiente(this.NumeroTerminal);
		}
	}

	@Override
	public void renotificar() {
		if(this.CantidadDeVecesLlamado<3) {
			this.CantidadDeVecesLlamado++;
			this.apiServidor.renotificar(this.NumeroTerminal, this.ultimoTurnoLlamado);
		}
		else {
			this.CantidadDeVecesLlamado=0;
			this.ultimoTurnoLlamado=null;
			controlador.seDebeLlamarSiguiente("El cliente ya fue llamado 3 veces");
		}
		controlador.actualizarVistaOperador();
	}

	@Override
	public void recibirEvento(Evento e) {
		System.out.println("Llega un evento:"  + e);
		if (e instanceof EventoConexionExitosa) {
	    	EventoConexionExitosa ent =(EventoConexionExitosa) e;
	    	this.NumeroTerminal=ent.getNumero();
	    	controlador.ActualizarVistaNumero(ent.getNumero());
	    }
		else if (e instanceof TurnoAsignado) {
			
			TurnoAsignado evento = (TurnoAsignado) e;
	        Turno turno = evento.getTurno();
	        this.ultimoTurnoLlamado=turno;
			this.ultimoTurnoLlamado.setDocumento(encriptador.desencriptar(this.ultimoTurnoLlamado.getDocumento()));
	        this.CantidadDeVecesLlamado=1;
	        System.out.println("Llego el TurnoAsignado DNI="+turno.getDocumento());
	        controlador.actualizarVistaOperador();
	    }
		else if(e instanceof EventoFilaNoVacia) {
			EventoFilaNoVacia EFNV = (EventoFilaNoVacia) e;
			this.CantidadEnEspera=EFNV.getCantTurno();
			this.setFilaVacia(false);
			controlador.estadoFilaNoVacia();
		}
	    else if(e instanceof EventoFilaVacia){
	    	this.CantidadEnEspera=0;
			this.setFilaVacia(true);
			controlador.estadoFilaVacia(); 	
	    }
	    else if(e instanceof EventoRecuperacionRellamado){
	    	EventoRecuperacionRellamado err=(EventoRecuperacionRellamado)e;
	    	this.ultimoTurnoLlamado = err.getTurno();
			this.ultimoTurnoLlamado.setDocumento(encriptador.desencriptar(this.ultimoTurnoLlamado.getDocumento()));
	    	this.CantidadDeVecesLlamado = err.getCantidadVecesLlamado();
	    	controlador.actualizarVistaOperador();
	    }
	    else{
	    	System.out.println("Llego un Evento");
	    	System.out.println("Tipo: " + e.getClass().getName());
	        System.out.println("Origen: " + e.getProcesoOrigen());
	        System.out.println("Destino: " + e.getProcesoDestino());
	    }
	}

	@Override
	public int getIntentos() {
		return this.CantidadDeVecesLlamado;
	}

	@Override
	public int getCantEspera() {
		return this.CantidadEnEspera;
	}

	@Override
	public Turno getUltimoTurno() {
		return this.ultimoTurnoLlamado;
	}

	@Override
	public void setEncriptadorApi(ISeguridadStrategy crypt){
		this.apiServidor.setEncriptador(crypt);
		this.encriptador=crypt;
	}
}
