package gestorFilaYTerminales;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import eventos.Turno;
import vista_controlador.IActualizarServidor;
import comunicacionConTerminales.ComunicacionesConTerminales;
import comunicacionConTerminales.IReceptorEvento;
import eventos.Evento;
import eventos.TurnoAsignado;
import eventos.EventoDniExistente;
import eventos.EventoFilaNoVacia;
import eventos.EventoFilaVacia;
import eventos.EventoLlamarSiguiente;
import eventos.EventoNotificar;
import eventos.EventoNuevoTurno;
import eventos.EventoRellamar;
import eventos.EventoTurnoCreadoConExito;

public class GestorFilaYTerminales implements IReceptorEvento{
	
	//INTERFAZ CON EL CONTROLADOR
	private IActualizarServidor ControladorServidor;
	public void setControlador(IActualizarServidor cs) {
		this.ControladorServidor=cs;
	}
	
	//PATRON SINGLETON
	private static GestorFilaYTerminales instancia;
	
	private GestorFilaYTerminales() {
		this.fila = new ArrayList<Turno>();
		this.numeroTurnoSiguiente=0;
		this.terminalesRegistro = new ArrayList<Terminal>();
		this.terminalesAtencion = new ArrayList<Terminal>();
		this.terminalesNotificacion = new ArrayList<Terminal>();
		
	}
	public static GestorFilaYTerminales getInstance() {
		if(instancia==null) {
			instancia= new GestorFilaYTerminales();
		}
		return instancia;
	}
	
	//GESTION DE FILAS
	private List<Turno> fila;
	
	private int numeroTurnoSiguiente;

	private void ordenarFila() {
        fila.sort(Comparator.comparingInt(Turno::getNumero));
    }
	
	private boolean DniRegistrado(String documento) {
		return fila.stream()
	            .anyMatch(t -> t.getDocumento().equals(documento));
	}
	
	public int cantidadTurnos() {
	    return (fila != null) ? fila.size() : 0;
	}

	//ARRIBO DE EVENTO - Al servidor llegan eventos de NuevoTurno-LlamarSiguiente-
	@Override
	public void ArriboEvento(Evento e) {
		
		String TerminalOrigen = e.getProcesoOrigen();
    	String tipoTerminal = obtenerTipo(TerminalOrigen);
    	int numeroTerminal=obtenerNumero(TerminalOrigen);
    	
		
	    if (e instanceof EventoNuevoTurno) {
	        EventoNuevoTurno evento = (EventoNuevoTurno) e;
	        Turno turno = evento.getTurno();
	        
	        if(turno.getNumero()==-1) {	// SI Numero es -1 entonces es una solicitud de turno.
	        	
	        	System.out.println("PROCESO ORIGEN =" + TerminalOrigen);
	        	Evento respuesta=null;
	        	
	        	
	        	if(DniRegistrado(turno.getDocumento())==true) {
	        		//Se debe enviar el evento 
	        		respuesta = new EventoDniExistente("SERVIDOR",TerminalOrigen,turno.getDocumento());
	        		
	        	}
	        	else {
	        		//
	        		this.numeroTurnoSiguiente++;
	        		turno.setNumero(this.numeroTurnoSiguiente);
	        		fila.add(turno);	        // agregar a la lista
	    	        ordenarFila();
	    	        respuesta = new EventoTurnoCreadoConExito("SERVIDOR",TerminalOrigen,turno);
	    	        System.out.println("Llego el EventoNuevoTurno DNI="+turno.getDocumento());
	    	        ControladorServidor.actualizarVistaServidor();
	    	        ComunicacionesConTerminales.getInstance().publicarOperadores(new EventoFilaNoVacia("Servidor","Operador",cantidadTurnos()));
	        	}
	        	//FUNCION PARA ENVIAR EVENTO
	        	if(respuesta!=null) {
	        		ComunicacionesConTerminales.getInstance().enviarEvento(respuesta, tipoTerminal, numeroTerminal);
	        	}
	        }
	    }
	    else if(e instanceof EventoLlamarSiguiente) {
	    	if (fila.isEmpty()) {
    	        ComunicacionesConTerminales.getInstance().publicarOperadores(new EventoFilaVacia("Servidor","Operadores"));
		    }
			else {
				Turno t = fila.remove(0);
				t.setHoraDeLlamado(new Date());
				TurnoAsignado respuesta = new TurnoAsignado("SERVIDOR",TerminalOrigen,t);
				ComunicacionesConTerminales.getInstance().enviarEvento(respuesta,tipoTerminal,numeroTerminal);
				
				EventoNotificar noti = new EventoNotificar(TerminalOrigen,numeroTerminal,"NOTIFICADORES",t);
				ComunicacionesConTerminales.getInstance().publicarNotificadores(noti);
				ControladorServidor.actualizarVistaServidor();
				if (fila.isEmpty()) {
	    	        ComunicacionesConTerminales.getInstance().publicarOperadores(new EventoFilaVacia("Servidor","Operadores"));
			    }
				else {
	    	        ComunicacionesConTerminales.getInstance().publicarOperadores(new EventoFilaNoVacia("Servidor","Operador",cantidadTurnos()));
				}
			}
	    	
	    }
	    else if (e instanceof EventoRellamar) {
	    	EventoRellamar Renoti = (EventoRellamar)e;
			ComunicacionesConTerminales.getInstance().publicarNotificadores(Renoti);
	    }
	    else {
	    	System.out.println("Llego un Evento");
	    	System.out.println("Tipo: " + e.getClass().getName());
	        System.out.println("Origen: " + e.getProcesoOrigen());
	        System.out.println("Destino: " + e.getProcesoDestino());
	    }
	}
	
	private String obtenerTipo(String origen) {
	    return origen.substring(0, 2);
	}

	private int obtenerNumero(String origen) {
	    return Integer.parseInt(origen.substring(2));
	}

	//GESTION DE TERMINALES
	private List<Terminal> terminalesRegistro,terminalesAtencion,terminalesNotificacion;
    
	
	
	public List<Turno> getListaTurnos() {
		return this.fila;
	}
	
	
	public List<String> getListaTerminalesRegistro() {
	    return terminalesRegistro.stream()
	            .map(Terminal::toString)
	            .toList();
	}

	public List<String> getListaTerminalesAtencion() {
	    return terminalesAtencion.stream()
	            .map(Terminal::toString)
	            .toList();
	}

	public List<String> getListaTerminalesNotificacion() {
	    return terminalesNotificacion.stream()
	            .map(Terminal::toString)
	            .toList();
	}
	
	public int agregarTerminalRegistro() {
		int numero = obtenerNumeroLibre(terminalesRegistro);
	    Terminal t = new Terminal("Terminal de Registro", numero);
	    terminalesRegistro.add(t);
	    ControladorServidor.actualizarVistaServidor();
	    return numero;
	}
	public int agregarTerminalAtencion() {
	    int numero = obtenerNumeroLibre(terminalesAtencion);
	    Terminal t = new Terminal("Terminal de Puesto de Atención", numero);
	    terminalesAtencion.add(t);
	    ControladorServidor.actualizarVistaServidor();
	    return numero;
	}
	public int agregarTerminalNotificacion() {
	    int numero = obtenerNumeroLibre(terminalesNotificacion);

	    Terminal t = new Terminal("Terminal de Notificación", numero);
	    terminalesNotificacion.add(t);
	    ControladorServidor.actualizarVistaServidor();
	    return numero;
	}
	private int obtenerNumeroLibre(List<Terminal> lista) {
	    List<Integer> usados = lista.stream()
	            .map(Terminal::getNumero)
	            .sorted()
	            .toList();

	    int esperado = 1;

	    for (int num : usados) {
	        if (num != esperado) {
	            return esperado;
	        }
	        esperado++;
	    }

	    return esperado;
	}
	
	public void BajaTerminal(String tipo, int numero) {
		if (numero <= 0) {
	        System.out.println("Número inválido: " + numero);
	        return;
	    }
		else {
			List<Terminal> lista = null;
			
			if ("TERMINAL_REGISTRO".equals(tipo)) {
				lista = terminalesRegistro;
				lista.removeIf(t -> t.getNumero() == numero);
			    ControladorServidor.actualizarVistaServidor();
			} else if ("TERMINAL_ATENCION".equals(tipo)) {
				lista = terminalesAtencion;
				lista.removeIf(t -> t.getNumero() == numero);
			    ControladorServidor.actualizarVistaServidor();
			} else if ("TERMINAL_NOTIFICACION".equals(tipo)) {
				lista = terminalesNotificacion;
				lista.removeIf(t -> t.getNumero() == numero);
			    ControladorServidor.actualizarVistaServidor();
			} else {
				System.out.println("Tipo desconocido: " + tipo);
				return;
			}
		}
	}
}
