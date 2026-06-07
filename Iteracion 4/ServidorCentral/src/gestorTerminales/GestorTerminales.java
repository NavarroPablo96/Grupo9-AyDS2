package gestorTerminales;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import controllers.IActualizarServidor;
import eventos.ConexionTerminal;
import eventos.Evento;
import eventos.EventoFilaNoVacia;
import eventos.EventoFilaVacia;
import eventos.EventoRecuperacionHistorial;
import eventos.EventoRecuperacionRellamado;
import eventos.Turno;
import gestorFila.GestorFila;
import gestorFila.Historial;
import gestorFila.Llamado;
import gestorFila.RegistroRellamar;

public class GestorTerminales implements IEnviarEventoClientes,IGestorTerminal{
	//PATRON SINGLETON
	private static GestorTerminales instancia;
	private GestorTerminales() {
		this.ListTerminalesRegistro = Collections.synchronizedList(new ArrayList<Terminal>());
		this.ListTerminalesAtencion = Collections.synchronizedList(new ArrayList<Terminal>());
		this.ListTerminalesNotificacion = Collections.synchronizedList(new ArrayList<Terminal>());
	}
	public static GestorTerminales getInstance() {
		if(instancia==null) {
			instancia= new GestorTerminales();
		}
		return instancia;
	}
	
	//INTERFAZ CON EL CONTROLADOR
	private IActualizarServidor ControladorServidor;
	public void setControlador(IActualizarServidor cs) {
		this.ControladorServidor=cs;
	}
	
	//GESTION DE TERMINALES
	private List<Terminal> ListTerminalesRegistro,ListTerminalesAtencion,ListTerminalesNotificacion;
    
	
	
	
	private synchronized void BajaTerminalIntero(String tipo, int numero) {
		if (numero <= 0) {
	        System.out.println("Número inválido: " + numero);
	        return;
	    }
		else {
			List<Terminal> lista = null;
			
			if ("TERMINAL_REGISTRO".equals(tipo)) {
				lista = ListTerminalesRegistro;
				lista.removeIf(t -> t.getNumero() == numero);
				
			} else if ("TERMINAL_ATENCION".equals(tipo)) {
				lista = ListTerminalesAtencion;
				lista.removeIf(t -> t.getNumero() == numero);
			} else if ("TERMINAL_NOTIFICACION".equals(tipo)) {
				lista = ListTerminalesNotificacion;
				lista.removeIf(t -> t.getNumero() == numero);
			} else {
				System.out.println("Tipo desconocido: " + tipo);
				return;
			}
		}
		actualizarVistaServidor();
	}

	private void actualizarVistaServidor() {
		ControladorServidor.actualizarTerminalesVistaServidor(getListaTerminalesRegistro(), getListaTerminalesAtencion(), getListaTerminalesNotificacion());
		
	}
	public synchronized int agregarTerminal(String tipo) {
	    switch (tipo) {
        case "TERMINAL_REGISTRO":
            return agregarTerminalRegistro();
        case "TERMINAL_ATENCION":
            return agregarTerminalAtencion();
        case "TERMINAL_NOTIFICACION":
            return agregarTerminalNotificacion();
        default:
            return 0;
	    }
	}
	private synchronized int agregarTerminalRegistro() {
		int numero = obtenerNumeroLibre(ListTerminalesRegistro);
	    Terminal t = new Terminal("Terminal de Registro", numero);
	    ListTerminalesRegistro.add(t);
		actualizarVistaServidor();
	    return numero;
	}
	private synchronized int agregarTerminalAtencion() {
	    int numero = obtenerNumeroLibre(ListTerminalesAtencion);
	    Terminal t = new Terminal("Terminal de Puesto de Atención", numero);
	    ListTerminalesAtencion.add(t);
		actualizarVistaServidor();
	    return numero;
	}
	
	private synchronized int agregarTerminalNotificacion() {
	    int numero = obtenerNumeroLibre(ListTerminalesNotificacion);

	    Terminal t = new Terminal("Terminal de Notificación", numero);
	    ListTerminalesNotificacion.add(t);
		actualizarVistaServidor();
	    return numero;
	}
	
	
	private synchronized int obtenerNumeroLibre(List<Terminal> lista) {
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
	
	private List<String> getListaTerminalesRegistro() {
	    return ListTerminalesRegistro.stream()
	            .map(Terminal::toString)
	            .toList();
	}

	private List<String> getListaTerminalesAtencion() {
	    return ListTerminalesAtencion.stream()
	            .map(Terminal::toString)
	            .toList();
	}

	private List<String> getListaTerminalesNotificacion() {
	    return ListTerminalesNotificacion.stream()
	            .map(Terminal::toString)
	            .toList();
	}
	
	
	
    //

	private Map<Integer, EscuchadorTerminal> terminalesRegistro = new HashMap<>();
	private Map<Integer, EscuchadorTerminal> terminalesAtencion = new HashMap<>();
	private Map<Integer, EscuchadorTerminal> terminalesNotificacion = new HashMap<>();
	
	public int AgregarTerminal(ConexionTerminal primerEvento,EscuchadorTerminal term) {
		int resultado=-1;
		String tipo;
		tipo=primerEvento.getTipoTerminal();
		
		if("TERMINAL_REGISTRO".equals(tipo)) {
			System.out.println("Se conecto una terminal de tipo registro");
			resultado=GestorTerminales.getInstance().agregarTerminal(tipo);
					//.agregarTerminalRegistro();
			System.out.println("resultado = "+resultado);
			terminalesRegistro.put(resultado, term);
		} else if ("TERMINAL_ATENCION".equals(tipo)) {
		    System.out.println("Se conecto una terminal de tipo atencion");
			resultado=GestorTerminales.getInstance().agregarTerminal(tipo);
		    //resultado = GestorTerminales.getInstance().agregarTerminalAtencion();
		    terminalesAtencion.put(resultado, term);
		} else if ("TERMINAL_NOTIFICACION".equals(tipo)) {
		    System.out.println("Se conecto una terminal de tipo notificacion");
			resultado=GestorTerminales.getInstance().agregarTerminal(tipo);
			//resultado = GestorTerminales.getInstance().agregarTerminalNotificacion();
		    terminalesNotificacion.put(resultado, term);
		} else {
		    System.out.println("Tipo de terminal desconocido: " + tipo);
		}
		
		return resultado;
	}
	
	public void BajaTerminal(String tipo, int numero) {
		if (numero <= 0) {
		        System.out.println("Número de terminal inválido: " + numero);
		    return;
		}
		else {
			if ("TERMINAL_REGISTRO".equals(tipo)) {
				terminalesRegistro.remove(numero);
				// Después de eliminar del map
				BajaTerminalIntero(tipo, numero);				
			} else if ("TERMINAL_ATENCION".equals(tipo)) {
				terminalesAtencion.remove(numero);
				// Después de eliminar del map
				BajaTerminalIntero(tipo, numero);				
			} else if ("TERMINAL_NOTIFICACION".equals(tipo)) {
				terminalesNotificacion.remove(numero);
				// Después de eliminar del map
				BajaTerminalIntero(tipo, numero);
			} else {
				System.out.println("Tipo de terminal desconocido: " + tipo);
				return;
			}
			
		}
		
	}
	
	public void publicarOperadores(Evento evento) {

	    for (EscuchadorTerminal terminal : terminalesAtencion.values()) {
	        if (terminal != null) {
	            terminal.enviar(evento);
	        }
	    }
	}
	public void publicarNotificadores(Evento evento) {

	    for (EscuchadorTerminal terminal : terminalesNotificacion.values()) {
	        if (terminal != null) {
	            terminal.enviar(evento);
	        }
	    }
	}
	
    //enviarEvento(evento, "TERMINAL_ATENCION", 2);
	@Override
	public void enviarEvento(Evento evento,String tipoTerminal,int numeroTerminal) {
	    EscuchadorTerminal terminal = null;
	    
	    
	    if ("TERMINAL_REGISTRO".equals(tipoTerminal)||"TR".equals(tipoTerminal)) {
	        terminal = terminalesRegistro.get(numeroTerminal);
	    } else if ("TERMINAL_ATENCION".equals(tipoTerminal)||"TA".equals(tipoTerminal)) {
	        terminal = terminalesAtencion.get(numeroTerminal);
	    } else if ("TERMINAL_NOTIFICACION".equals(tipoTerminal)||"TN".equals(tipoTerminal)) {
	        terminal = terminalesNotificacion.get(numeroTerminal);
	    } else {
	        System.out.println("Tipo de terminal desconocido: " + tipoTerminal);
	        return;
	    }

	    
	    if (terminal != null) {
	    	terminal.enviar(evento);
	    } else {
	        System.out.println("No se encontró la terminal: " + tipoTerminal + " #" + numeroTerminal);
	    }
	}
	
	@Override
	public void TerminalAgregadaConExito(String tipo, int numero) {
		if ("TERMINAL_ATENCION".equals(tipo)) {
			int cantidad = GestorFila.getInstance().getCantidadTurnos();
		    Evento efilafilaVaciaoNO = null;
		    if(cantidad>0) {
		    	efilafilaVaciaoNO=new EventoFilaNoVacia("SERVIDOR","OPERADORES",cantidad);
		    }
		    else {
		    	efilafilaVaciaoNO=new EventoFilaVacia("SERVIDOR","OPERADORES");
		    }
		    enviarEvento(efilafilaVaciaoNO,tipo,numero);

		    //SE debe enviar el estado del operador, su turno y la cantidad de veces llamado.
		    RegistroRellamar registro = GestorFila.getInstance().getRegistro();
		    if(registro!=null) {
		    	Llamado llamado = registro.getLlamado(numero);
		    	if(llamado!=null) {
		    		System.out.println("GestorTerminales 274 - Creando EventoRecuperacionRellamado");
					Turno t = llamado.getTurno();
		    		EventoRecuperacionRellamado evr = new EventoRecuperacionRellamado(t,llamado.getCantidadVecesLlamado(),llamado.getNumeroTerminal());			
		    		enviarEvento(evr,tipo,numero);
		    	}
		    }
		    
		}
		else {
			if ("TERMINAL_NOTIFICACION".equals(tipo)) {
				/* Se debe enviar a esta terminal de notificacion
				 *	turno Actual 
				 *	y el resto del estado del historial
				*/
				Historial recuperacion=GestorFila.getInstance().getHistorial();
				if(recuperacion!=null) {
					Turno turAct  = recuperacion.getTurnoActual();
					ArrayList<Turno> listaEnMonitor = recuperacion.getHistorial();
					System.out.println("GestorTermianles 279 - turnoActual = "+turAct);
					EventoRecuperacionHistorial evr = new EventoRecuperacionHistorial(turAct,listaEnMonitor);
					enviarEvento(evr,tipo,numero);
				}
			}
		}

		
	}
	
	
	
}
