package gestorFilaYTerminales;

import java.util.Date;

import comunicacionConTerminales.Comunicador;
import controllers.IActualizarServidor;

import eventos.Evento;
import eventos.EventoDniExistente;
import eventos.EventoFilaNoVacia;
import eventos.EventoFilaVacia;
import eventos.EventoLlamarSiguiente;
import eventos.EventoNotificar;
import eventos.EventoRellamar;
import eventos.EventoSolicitudTurno;
import eventos.EventoTurnoCreadoConExito;
import eventos.Turno;
import eventos.TurnoAsignado;

import interfaces.IAtencion;
import interfaces.IRegistro;

public class GestorFila implements IRegistro,IAtencion,IEstadoFila{
	
	//INTERFAZ CON EL CONTROLADOR
	private IActualizarServidor ControladorServidor;
	public void setControlador(IActualizarServidor cs) {
		this.ControladorServidor=cs;
	}
	
	//PATRON SINGLETON
	private static GestorFila instancia;
	
	private GestorFila() {
		this.numeroTurnoSiguiente=0;
		this.cantidadPone=0;
		this.cantidadSaca=0;
	}
	public static GestorFila getInstance() {
		if(instancia==null) {
			instancia= new GestorFila();
		}
		return instancia;
	}
	
	//GESTION DE FILAS
	private IColaTurno fila;
	private int numeroTurnoSiguiente;
	private int cantidadPone;
	private int cantidadSaca;
	
	//IRegistro
	@Override
	public void nuevoTurno(EventoSolicitudTurno evento, String tipoTerminal, int numeroTerminal) {
		String TerminalOrigen = evento.getProcesoOrigen();
		Evento respuesta =null;
		
		if(this.fila.DniRegistrado(evento.getDni())==true) {
    		//Se debe enviar el evento 
    		respuesta = new EventoDniExistente("SERVIDOR",TerminalOrigen,evento.getDni());
    	}
    	else {
    		this.numeroTurnoSiguiente++;
    		Turno nuevo = new Turno(this.numeroTurnoSiguiente,evento.getDni(),evento.getHora(),evento.getHoraReal());
    		fila.pone(nuevo);
    		System.out.println("Se agrego un nuevo Turno a la fila");
    		System.out.println(fila.getCantidad());
    		System.out.println(fila.getListaTurnos());
    		this.cantidadPone++;
    		this.fila.ordenar();
    		respuesta = new EventoTurnoCreadoConExito("SERVIDOR",TerminalOrigen,nuevo);
	        System.out.println("Llego el EventoSolicitudTurno DNI="+evento.getDni());
	        ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
	        Comunicador.getInstance().publicarOperadores(new EventoFilaNoVacia("Servidor","Operador",this.fila.getCantidad()));
    	}
		//FUNCION PARA ENVIAR EVENTO
    	if(respuesta!=null) {
    		Comunicador.getInstance().enviarEvento(respuesta, tipoTerminal, numeroTerminal);
    	}
	}
	
	//IAtencion
	@Override
	public void LlamarSiguiente(EventoLlamarSiguiente E,String tipoTerminal,int numeroTerminal) {
		String TerminalOrigen = E.getProcesoOrigen();
		
		if (fila.getCantidad()==0) {
	        Comunicador.getInstance().publicarOperadores(new EventoFilaVacia("Servidor","Operadores"));
	    }
		else {
			Turno t = fila.saca();
			this.cantidadSaca++;
			t.setHoraDeLlamado(new Date());
			TurnoAsignado respuesta = new TurnoAsignado("SERVIDOR",TerminalOrigen,t);
			Comunicador.getInstance().enviarEvento(respuesta,tipoTerminal,numeroTerminal);
			
			EventoNotificar noti = new EventoNotificar(TerminalOrigen,numeroTerminal,"NOTIFICADORES",t);
			Comunicador.getInstance().publicarNotificadores(noti);
			ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
			if (fila.getCantidad()==0) {
    	        Comunicador.getInstance().publicarOperadores(new EventoFilaVacia("Servidor","Operadores"));
		    }
			else {
    	        Comunicador.getInstance().publicarOperadores(new EventoFilaNoVacia("Servidor","Operador",this.fila.getCantidad()));
			}
		}
	}
	
	//IAtencion
	@Override
	public void Rellamar(EventoRellamar Renoti) {
		Comunicador.getInstance().publicarNotificadores(Renoti);
	}
	
	//IEstadoFila
	@Override
	public IColaTurno getCola() {
		return this.fila;
	}
	@Override
	public int getCantidadSaca() {
		return this.cantidadSaca;
	}
	@Override
	public int getCantidadPone() {
		return this.cantidadPone;
	}
	@Override
	public int getCantidadTurnos() {
		return this.fila.getCantidad();
	}
	@Override
	public void setEstado(IColaTurno c, int cantidadTurnos, int cantidadPone, int cantidadSaca) {
		this.fila=c;
		this.cantidadPone =cantidadPone;
		this.cantidadSaca= cantidadSaca;
		this.numeroTurnoSiguiente=cantidadTurnos;
		System.out.println("GestorFila138-Se actualizo el estado de la cola turno="+c.getCantidad()+" IColaTurno.getCantidad="+this.fila.getCantidad());						
		ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
		//private int numeroTurnoSiguiente;
	}
	public void setCola(IColaTurno iCT) {
		this.fila=iCT;
	}
	
	
}
