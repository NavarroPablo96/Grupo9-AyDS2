package gestorFila;
import java.util.Date;
import java.util.List;

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

import gestorTerminales.IEnviarEventoClientes;
import persistencia.GestorPersistencia;
import seguridad.ISeguridadStrategy;

public class GestorFila implements IRegistro,IAtencion,IEstadoFila{


	private ISeguridadStrategy encriptador;
	
	//INTERFAZ CON EL CONTROLADOR
	private IActualizarServidor ControladorServidor;
	public void setControlador(IActualizarServidor cs) {
		this.ControladorServidor=cs;
	}
	
	//INTERFAZ GESTOR-TERMINALES
	private IEnviarEventoClientes gestorTerminales;
	public void setIEnviar(IEnviarEventoClientes gestorTerminales) {
		this.gestorTerminales=gestorTerminales;
	}
	
	//INTERFAZ GESTOR PERSISTENCIA:
	private GestorPersistencia gestorPersistencia;
	public void setGestorPersistencia(GestorPersistencia gestorPersistencia) {
		this.gestorPersistencia=gestorPersistencia;
	}
	
	//PATRON SINGLETON
	private static GestorFila instancia;
	
	private GestorFila() {
		this.numeroTurnoSiguiente=0;
		this.cantidadPone=0;
		this.cantidadSaca=0;
		this.llamados = new RegistroRellamar();
		this.historial = new Historial();
	}
	
	public static GestorFila getInstance() {
		if(instancia==null) {
			instancia= new GestorFila();
		}
		return instancia;
	}
	
	//INTERFAZ CON ICOLATURNO //GESTION DE FILAS  //
	private IColaTurno fila;
	private int numeroTurnoSiguiente;
	private int cantidadPone;
	private int cantidadSaca;
    private RegistroRellamar llamados;
    private Historial historial; 

	private void guardarEstadoCola() {
		System.out.println("GestorFila80 - GUARDAR ESTADOCOLA()");
		EstadoCola ec = new EstadoCola();

		IColaTurno filaEncriptada = this.encriptarFila(this.fila);

		ec.setCola((ColaTurno)filaEncriptada,this.numeroTurnoSiguiente,this.cantidadPone,this.cantidadSaca);
		this.gestorPersistencia.guardarCola(ec);
	}

	private IColaTurno encriptarFila(IColaTurno fila){
        for (eventos.Turno t : fila.getListaTurnos()) {
            t.setDocumento(encriptador.encriptar(t.getDocumento()));
        }
        return fila;	
	}

    // private IColaTurno desencriptarFila(IColaTurno fila){
    //     for (eventos.Turno t : fila.getListaTurnos()) {
    //         t.setDocumento(encriptador.desencriptar(t.getDocumento()));
    //     }
    //     return fila;
    // }
	
	private void guardarHistorial() {
		System.out.println("GestorFila89 - GUARDAR HISTORIAL()");
		Historial historialEncriptado = this.encriptarHistorial(this.historial);
		this.gestorPersistencia.guardarHistorial(historialEncriptado);
	}

	private Historial encriptarHistorial(Historial h){
		if (h.getTurnoActual() != null) {
			h.getTurnoActual().setDocumento(encriptador.encriptar(h.getTurnoActual().getDocumento()));
		}
		if (h.getHistorial() != null) {
			for (Turno t : h.getHistorial()) {
				t.setDocumento(encriptador.encriptar(t.getDocumento()));
			}
		}
		return h;
	}

	private Historial desencriptarHistorial(Historial h){
		if (h.getTurnoActual() != null) {
			h.getTurnoActual().setDocumento(encriptador.desencriptar(h.getTurnoActual().getDocumento()));
		}
		if (h.getHistorial() != null) {
			for (Turno t : h.getHistorial()) {
				t.setDocumento(encriptador.desencriptar(t.getDocumento()));
			}
		}
		return h;
	}

	private void guardarLlamados() {
		System.out.println("GestorFila94 - GUARDAR LLAMADOS()");
		RegistroRellamar encriptado = this.encriptarRellamados(this.llamados);
		this.gestorPersistencia.guardarLlamados(encriptado);
	}

	private RegistroRellamar encriptarRellamados(RegistroRellamar r){
		List<Llamado> llamados = r.getLlamados();
		for (Llamado ll : llamados) {
			Turno t = ll.getTurno();
			t.setDocumento(encriptador.encriptar(t.getDocumento()));
		}

		return r;
	}

	private RegistroRellamar desencriptarRellamados(RegistroRellamar r){
		List<Llamado> llamados = r.getLlamados();
		for (Llamado ll : llamados) {
			Turno t = ll.getTurno();
			t.setDocumento(encriptador.desencriptar(t.getDocumento()));
		}

		return r;
	}
	
	public void cargarEstado() {
		EstadoCola estadoCola = gestorPersistencia.cargarCola();
		if (estadoCola != null) {
			
			IColaTurno ict = estadoCola.getCola();
			if(ict!=null) {
				System.out.println("GestorFila92-cargarEstado() muestro la cola que llega desde la persistencia:");
				ict.mostrarCola();
				int nts = estadoCola.getNumeroTurnoSiguiente();
				int cantP = estadoCola.getCantidadPone();
				int cantS = estadoCola.getCantidadSaca();
				setEstado(ict,nts,cantP, cantS);
			}
			else {
				setEstado(new ColaTurno(),0,0,0);
			}
		}
		
		this.historial = this.gestorPersistencia.cargarHistorial();
		if(this.historial==null) {
			this.historial=new Historial();
		}
		else{
			this.historial = this.desencriptarHistorial(this.historial);
		}
		this.llamados = this.gestorPersistencia.cargarLlamados();
		if(this.llamados==null) {
			this.llamados = new RegistroRellamar();
		}
		else{
			this.llamados = this.desencriptarRellamados(this.llamados);
		}
		System.out.println("GestorFila 100 - CargarEstado() NumeroDeTurnoSiguiente="+this.numeroTurnoSiguiente);
	}
	
	
	//IRegistro
	@Override
	public void nuevoTurno(EventoSolicitudTurno evento, String tipoTerminal, int numeroTerminal) {
		String TerminalOrigen = evento.getProcesoOrigen();
		Evento respuesta =null;

		String dni = encriptador.desencriptar(evento.getDni());
		
		if(this.fila.DniRegistrado(dni)==true) {
    		//Se debe enviar el evento 
    		respuesta = new EventoDniExistente("SERVIDOR",TerminalOrigen,evento.getDni());
    	}
    	else {
    		this.numeroTurnoSiguiente++;
    		Turno nuevo = new Turno(this.numeroTurnoSiguiente,dni,evento.getHora(),evento.getHoraReal());
    		fila.pone(nuevo);
    		System.out.println("Se agrego un nuevo Turno a la fila");
    		System.out.println(fila.getCantidad());
    		System.out.println(fila.getListaTurnos());
    		this.cantidadPone++;
    		this.fila.ordenar();
    		respuesta = new EventoTurnoCreadoConExito("SERVIDOR",TerminalOrigen,nuevo);
	        System.out.println("Llego el EventoSolicitudTurno DNI="+dni);
	        ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
	        this.gestorTerminales.publicarOperadores(new EventoFilaNoVacia("Servidor","Operador",this.fila.getCantidad()));
    	}
		//FUNCION PARA ENVIAR EVENTO
    	if(respuesta!=null) {
    			guardarEstadoCola();
    		this.gestorTerminales.enviarEvento(respuesta, tipoTerminal, numeroTerminal);
    	}
	}

	//IAtencion
	@Override
	public void LlamarSiguiente(EventoLlamarSiguiente E,String tipoTerminal,int numeroTerminal) {
		String TerminalOrigen = E.getProcesoOrigen();
		
		if (fila.getCantidad()==0) {
	        this.gestorTerminales.publicarOperadores(new EventoFilaVacia("Servidor","Operadores"));
	        //Comunicador.getInstance().publicarOperadores(new EventoFilaVacia("Servidor","Operadores"));
	    }
		else {
			Turno t = fila.saca();
			this.cantidadSaca++;
			t.setHoraDeLlamado(new Date());
			TurnoAsignado respuesta = new TurnoAsignado("SERVIDOR",TerminalOrigen,t);
    		this.gestorTerminales.enviarEvento(respuesta, tipoTerminal, numeroTerminal);
			//Comunicador.getInstance().enviarEvento(respuesta,tipoTerminal,numeroTerminal);
			
			t.setDocumento(encriptador.encriptar(t.getDocumento()));
			EventoNotificar noti = new EventoNotificar(TerminalOrigen,numeroTerminal,"NOTIFICADORES",t);
    		this.gestorTerminales.publicarNotificadores(noti);

			t.setDocumento(encriptador.desencriptar(t.getDocumento()));
			//Comunicador.getInstance().publicarNotificadores(noti);
//ITE 4:
    		this.historial.llamarSiguiente(t,numeroTerminal);
    		this.llamados.llamarSiguiente(numeroTerminal, t);
			guardarEstadoCola();
			guardarHistorial();
			guardarLlamados();
			
    		
			ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
			if (fila.getCantidad()==0) {
		        this.gestorTerminales.publicarOperadores(new EventoFilaVacia("Servidor","Operadores"));
    	        //Comunicador.getInstance().publicarOperadores(new EventoFilaVacia("Servidor","Operadores"));
		    }
			else {
		        this.gestorTerminales.publicarOperadores(new EventoFilaNoVacia("Servidor","Operador",this.fila.getCantidad()));
    	        //Comunicador.getInstance().publicarOperadores(new EventoFilaNoVacia("Servidor","Operador",this.fila.getCantidad()));
			}
		}
	}

	//IAtencion
	@Override
	public void Rellamar(EventoRellamar Renoti) {
		Turno turnoRellamar = Renoti.getTurno();
		turnoRellamar.setDocumento(encriptador.encriptar(turnoRellamar.getDocumento()));
        this.gestorTerminales.publicarNotificadores(Renoti);

		turnoRellamar.setDocumento(encriptador.desencriptar(turnoRellamar.getDocumento()));
        turnoRellamar.setNumeroTerminal(Renoti.getNumeroTPA());
		this.historial.rellamar(turnoRellamar);
		this.llamados.rellamar(Renoti.getNumeroTPA());
		guardarHistorial();
		guardarLlamados();
		//Comunicador.getInstance().publicarNotificadores(Renoti);
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
		System.out.println("GestorFila200-setEstado(cola) - Se actualizo el estado de la cola cantidad_turno="+c.getCantidad());						
		ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
		//private int numeroTurnoSiguiente;
	}
	public void setCola(IColaTurno iCT) {
		this.fila=iCT;
	}

	public Historial getHistorial() {
		return this.historial;
	}

	public RegistroRellamar getRegistro() {
		return this.llamados;
	}

	public void setEncriptador(ISeguridadStrategy encriptador){
		this.encriptador = encriptador;
	}
	
}
