package comunicacionConTerminales;

import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import eventos.ConexionTerminal;
import eventos.Evento;
import gestorFilaYTerminales.GestorFilaYTerminales;
import vista_controlador.ControladorServidor;

public class ComunicacionesConTerminales implements IRecibirEvento, IEnviarEvento{

	
	//PATRON SINGLETON
	private static ComunicacionesConTerminales instancia;
	
	
	private ComunicacionesConTerminales() {
        this.receptores = new ArrayList<>();
	}
	public static ComunicacionesConTerminales getInstance() {
		if(instancia==null) {
			instancia= new ComunicacionesConTerminales();
		}
		return instancia;
	}
	
	//PATRON OBSERVER - OBSERVABLE 
	// Esta clase es observable(IRecibirEvento) 
	// tiene una lista de Observadores(IReceptorEvento)
    //Logica observer Observable
    private ArrayList<IReceptorEvento> receptores;
	
    @Override
    public void suscribirse(IReceptorEvento receptor) {
        if (receptor != null && !receptores.contains(receptor)) {
            receptores.add(receptor);
        }
    }

    @Override
    public void desuscribirse(IReceptorEvento receptor) {
        receptores.remove(receptor);
    }

    public void notificarReceptores(Evento evento) {
    	for (IReceptorEvento receptor : receptores) {
            receptor.ArriboEvento(evento);
        }
    }

	private Map<Integer, EscuchadorTerminal> terminalesRegistro = new HashMap<>();
	private Map<Integer, EscuchadorTerminal> terminalesAtencion = new HashMap<>();
	private Map<Integer, EscuchadorTerminal> terminalesNotificacion = new HashMap<>();
	
	public int AgregarTerminal(ConexionTerminal primerEvento,EscuchadorTerminal term) {
		int resultado=-1;
		String tipo;
		tipo=primerEvento.getTipoTerminal();
		
		if("TERMINAL_REGISTRO".equals(tipo)) {
			System.out.println("Se conecto una terminal de tipo registro");
			resultado=GestorFilaYTerminales.getInstance().agregarTerminalRegistro();
			terminalesRegistro.put(resultado, term);
		} else if ("TERMINAL_ATENCION".equals(tipo)) {
		    System.out.println("Se conecto una terminal de tipo atencion");
		    resultado = GestorFilaYTerminales.getInstance().agregarTerminalAtencion();
		    terminalesAtencion.put(resultado, term);
		} else if ("TERMINAL_NOTIFICACION".equals(tipo)) {
		    System.out.println("Se conecto una terminal de tipo notificacion");
		    resultado = GestorFilaYTerminales.getInstance().agregarTerminalNotificacion();
		    terminalesNotificacion.put(resultado, term);
		} else {
		    System.out.println("Tipo de terminal desconocido: " + tipo);
		}
		
		return resultado;
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
	    	System.out.println("Se envia un evento a terminal= "+tipoTerminal+":"+numeroTerminal);
	        terminal.enviar(evento);
	    } else {
	        System.out.println("No se encontró la terminal: " + tipoTerminal + " #" + numeroTerminal);
	    }
	}
	
	@Override
	public void enviarEvento(Evento evento, String TerminalDestion) {
		// TODO Auto-generated method stub
		
	}
	
	//LOGICA SERVIDOR
	public void iniciarServidor(String ip, int puerto) {
	    new Thread(() -> {
	        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
	            System.out.println("Esperando conexion...");
	            //ControladorServidor.getInstance().estadoEscuchando("Escuchando en:" + puerto);
	            ControladorServidor.getInstance().estadoEscuchando("Escuchando en IP:  "+ip+" : "+puerto);
	            
	            
	            while (true) {
	                Socket socketCliente = serverSocket.accept();
	                
	                System.out.println("Nueva conexion entrante");
	                
	                EscuchadorTerminal escuchador = new EscuchadorTerminal(socketCliente);
	                
	                // (opcional pero recomendable) registrar el cliente
	                //registrarTerminal(escuchador);

	                new Thread(escuchador).start();
	            }

	        } catch (BindException e) {
	            e.printStackTrace();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }).start();
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
				GestorFilaYTerminales.getInstance().BajaTerminal(tipo, numero);				
			} else if ("TERMINAL_ATENCION".equals(tipo)) {
				terminalesAtencion.remove(numero);
				// Después de eliminar del map
				GestorFilaYTerminales.getInstance().BajaTerminal(tipo, numero);				
			} else if ("TERMINAL_NOTIFICACION".equals(tipo)) {
				terminalesNotificacion.remove(numero);
				// Después de eliminar del map
				GestorFilaYTerminales.getInstance().BajaTerminal(tipo, numero);
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

}
