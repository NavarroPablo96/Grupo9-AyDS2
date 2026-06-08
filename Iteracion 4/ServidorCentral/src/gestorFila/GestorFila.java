package gestorFila;
import java.util.Date;
import java.util.List;

import controllers.IActualizarServidor;

import eventos.Evento;
import eventos.EventoActualizacionLlamarSiguiente;
import eventos.EventoActualizacionNuevoTurno;
import eventos.EventoActualizacionRellamar;
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
import gestorSincronizacion.IActualizacion;
import gestorTerminales.IEnviarEventoClientes;
import persistencia.GestorPersistencia;
import seguridad.ISeguridadStrategy;

public class GestorFila implements IRegistro,IAtencion,IEstadoFila{


	private ISeguridadStrategy encriptador;
	public ISeguridadStrategy getEncriptador() {
		return this.encriptador;
	}
	
	
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
	
	//INTERFAZ GESTOR ACTUALIZACION:
	private IActualizacion actualizadorServidorSecundario;
	public void setActualizador(IActualizacion gestorSincronizacion) {
		this.actualizadorServidorSecundario=gestorSincronizacion;
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
    
  //PERSISTENCIA---------------------------------------------------------------
	private void guardarEstadoCola() {
		System.out.println("GestorFila80 - GUARDAR ESTADOCOLA()");
		EstadoCola ec = new EstadoCola();

		IColaTurno filaParaEncriptar = fila.generarCopia();

		IColaTurno filaEncriptada = this.encriptarFila(filaParaEncriptar);

		ec.setCola((ColaTurno)filaEncriptada,this.numeroTurnoSiguiente,this.cantidadPone,this.cantidadSaca);

		if(this.gestorPersistencia!=null) {
			this.gestorPersistencia.guardarCola(ec);
		}
	}

	private IColaTurno encriptarFila(IColaTurno fila){
        for (eventos.Turno t : fila.getListaTurnos()) {
            t.setDocumento(encriptador.encriptar(t.getDocumento()));
        }
        return fila;	
	}

    private IColaTurno desencriptarFila(IColaTurno fila){
    	for (eventos.Turno t : fila.getListaTurnos()) {
    		t.setDocumento(encriptador.desencriptar(t.getDocumento()));
    	}
    	return fila;
	}
	
	private void guardarHistorial() {
		System.out.println("GestorFila89 - GUARDAR HISTORIAL()");
		Historial historialParaEncriptar = historial.generarCopia();
		Historial historialEncriptado = this.encriptarHistorial(historialParaEncriptar);
		if(this.gestorPersistencia!=null) {
			this.gestorPersistencia.guardarHistorial(historialEncriptado);
		}
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
		RegistroRellamar llamadosParaEncriptar = llamados.generarCopia();
		RegistroRellamar encriptado = this.encriptarRellamados(llamadosParaEncriptar);
		if(this.gestorPersistencia!=null) {
			this.gestorPersistencia.guardarLlamados(encriptado);
		}
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
		if(this.gestorPersistencia!=null) {
			EstadoCola estadoCola = gestorPersistencia.cargarCola();
			if (estadoCola != null) {
				
				IColaTurno ict = estadoCola.getCola();
				if(ict!=null) {
					System.out.println("GestorFila92-cargarEstado() muestro la cola que llega desde la persistencia:");
					ict.mostrarCola();
					int nts = estadoCola.getNumeroTurnoSiguiente();
					int cantP = estadoCola.getCantidadPone();
					int cantS = estadoCola.getCantidadSaca();
					
					System.out.println("GestorFila92-cargarEstado() muestro la cola desencriptada");
					IColaTurno colaDesencriptada = this.desencriptarFila(ict);
					colaDesencriptada.mostrarCola();
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
	}
	
	
	//IRegistro
	@Override
	public void nuevoTurno(EventoSolicitudTurno evento, String tipoTerminal, int numeroTerminal) {
		String TerminalOrigen = evento.getProcesoOrigen();
		Evento respuesta =null;
		
		String dni = encriptador.desencriptar(evento.getDni());
		System.out.println("GestorFila dni=encriptador.desencriptar(evento.getDni())="+dni);
		
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
			guardarEstadoCola();
			Turno turnoRta = new Turno(this.numeroTurnoSiguiente,evento.getDni(),evento.getHora(),evento.getHoraReal());
    		respuesta = new EventoTurnoCreadoConExito("SERVIDOR",TerminalOrigen,turnoRta);
	        System.out.println("Llego el EventoSolicitudTurno DNI="+dni);
	        ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
	        this.gestorTerminales.publicarOperadores(new EventoFilaNoVacia("Servidor","Operador",this.fila.getCantidad()));
	        this.actualizadorServidorSecundario.enviarActualizacion(new EventoActualizacionNuevoTurno(nuevo));
    	}
		//FUNCION PARA ENVIAR EVENTO
    	if(respuesta!=null) {
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

			Turno tEncriptado = new Turno(t.getNumero(), t.getDocumento(), t.getHoraRegistro(), t.getHoraHoraDeLlamado());
			tEncriptado.setDocumento(encriptador.encriptar(tEncriptado.getDocumento()));
			this.cantidadSaca++;
			t.setHoraDeLlamado(new Date());
			TurnoAsignado respuesta = new TurnoAsignado("SERVIDOR",TerminalOrigen,tEncriptado);
    		this.gestorTerminales.enviarEvento(respuesta, tipoTerminal, numeroTerminal);
			//Comunicador.getInstance().enviarEvento(respuesta,tipoTerminal,numeroTerminal);
			EventoNotificar noti = new EventoNotificar(TerminalOrigen,numeroTerminal,"NOTIFICADORES",tEncriptado);
    		this.gestorTerminales.publicarNotificadores(noti);
			//Comunicador.getInstance().publicarNotificadores(noti);
//ITE 3:
    		this.actualizadorServidorSecundario.enviarActualizacion(new EventoActualizacionLlamarSiguiente(t,numeroTerminal));
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
		Turno t = Renoti.getTurno();
		Turno tEncriptado = new Turno(t.getNumero(), t.getDocumento(), t.getHoraRegistro(), t.getHoraHoraDeLlamado());
		tEncriptado.setDocumento(encriptador.encriptar(tEncriptado.getDocumento()));
        this.gestorTerminales.publicarNotificadores(Renoti);
        t.setNumeroTerminal(Renoti.getNumeroTPA());
		this.historial.rellamar(t);
		this.llamados.rellamar(Renoti.getNumeroTPA());
		guardarHistorial();
		guardarLlamados();
		//Comunicador.getInstance().publicarNotificadores(Renoti);
		this.actualizadorServidorSecundario.enviarActualizacion(new EventoActualizacionRellamar(tEncriptado,Renoti.getNumeroTPA()));
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
	public int getNumeroTurnoSiguiente() {
		return this.numeroTurnoSiguiente;
	}
	@Override
	public void setEstado(IColaTurno c, int numeroTurnoSiguiente, int cantidadPone, int cantidadSaca) {
		this.fila=c;
		this.cantidadPone =cantidadPone;
		this.cantidadSaca= cantidadSaca;
		this.numeroTurnoSiguiente=numeroTurnoSiguiente;
		System.out.println("GestorFila200-setEstado(cola) - Se actualizo el estado de la cola NumSiguienteTurno="+this.numeroTurnoSiguiente);
		ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
		//private int numeroTurnoSiguiente;
		guardarEstadoCola();
	}
	
	@Override
	public void setHistorial(Historial historial) {
		System.out.println("Se actualizo el historial historial==null?"+(historial==null));
		this.historial=historial;
		this.historial.mostrar();
		guardarHistorial();
	}

	@Override
	public void setRegistro(RegistroRellamar registro) {
		this.llamados=registro;
		System.out.println("Se actualizo Registro llamados:");
		this.llamados.mostrar();
		guardarLlamados();
		
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

	@Override
	public void actualizacionNuevoTurno(Turno t) {
		this.numeroTurnoSiguiente++;	//Lo mismo que nuevoTurno pero más sencillo
		fila.pone(t);
		this.cantidadPone++;
		this.fila.ordenar();
		ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
		guardarEstadoCola();
	}

	@Override
	public void actualizacionLlamarSiguiente(int numeroTerminalQueLlama, Turno turnoLlamado) {
		//System.out.println("Se recibió una Actualizacion de llamar siguiente, estado antes de la actualización");
		//this.fila.mostrarCola();
		//this.historial.mostrar();
		//this.llamados.mostrar();

		fila.sacarEspecifico(turnoLlamado.getNumero());
		this.cantidadSaca++;
		this.historial.llamarSiguiente(turnoLlamado,numeroTerminalQueLlama);
		this.llamados.llamarSiguiente(numeroTerminalQueLlama, turnoLlamado);

		//System.out.println("Sistema despues de actulizacion llamar siguiente");
		//this.fila.mostrarCola();
		//this.historial.mostrar();
		//this.llamados.mostrar();
		ControladorServidor.actualizarTurnosVistaServidor(this.fila.getListaTurnos());
		
		
		guardarEstadoCola();
		guardarHistorial();
		guardarLlamados();
	}

	@Override
	public void actualizacionRellamar(int numeroTerminalQueLlama, Turno turnoLlamado) {
		this.historial.rellamar(turnoLlamado);
		this.llamados.rellamar(numeroTerminalQueLlama);
		
		guardarHistorial();
		guardarLlamados();		
	}

	public int getCantidadTurnos() {
		return this.fila.getCantidad();
	}
	
}
