package comunicacionConTerminales;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import controllers.IActualizarServidor;
import gestorFilaYTerminales.Terminal;

public class GestorTerminales {
	//PATRON SINGLETON
	private static GestorTerminales instancia;
	private GestorTerminales() {
		this.terminalesRegistro = Collections.synchronizedList(new ArrayList<Terminal>());
		this.terminalesAtencion = Collections.synchronizedList(new ArrayList<Terminal>());
		this.terminalesNotificacion = Collections.synchronizedList(new ArrayList<Terminal>());
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
	private List<Terminal> terminalesRegistro,terminalesAtencion,terminalesNotificacion;
    
	
	
	
	public synchronized void BajaTerminal(String tipo, int numero) {
		if (numero <= 0) {
	        System.out.println("Número inválido: " + numero);
	        return;
	    }
		else {
			List<Terminal> lista = null;
			
			if ("TERMINAL_REGISTRO".equals(tipo)) {
				lista = terminalesRegistro;
				lista.removeIf(t -> t.getNumero() == numero);
				
			} else if ("TERMINAL_ATENCION".equals(tipo)) {
				lista = terminalesAtencion;
				lista.removeIf(t -> t.getNumero() == numero);
			} else if ("TERMINAL_NOTIFICACION".equals(tipo)) {
				lista = terminalesNotificacion;
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
		int numero = obtenerNumeroLibre(terminalesRegistro);
	    Terminal t = new Terminal("Terminal de Registro", numero);
	    terminalesRegistro.add(t);
		actualizarVistaServidor();
	    return numero;
	}
	private synchronized int agregarTerminalAtencion() {
	    int numero = obtenerNumeroLibre(terminalesAtencion);
	    Terminal t = new Terminal("Terminal de Puesto de Atención", numero);
	    terminalesAtencion.add(t);
		actualizarVistaServidor();
	    return numero;
	}
	
	private synchronized int agregarTerminalNotificacion() {
	    int numero = obtenerNumeroLibre(terminalesNotificacion);

	    Terminal t = new Terminal("Terminal de Notificación", numero);
	    terminalesNotificacion.add(t);
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
	    return terminalesRegistro.stream()
	            .map(Terminal::toString)
	            .toList();
	}

	private List<String> getListaTerminalesAtencion() {
	    return terminalesAtencion.stream()
	            .map(Terminal::toString)
	            .toList();
	}

	private List<String> getListaTerminalesNotificacion() {
	    return terminalesNotificacion.stream()
	            .map(Terminal::toString)
	            .toList();
	}
	
}
