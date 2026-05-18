package comunicacionConTerminales;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import controllers.IActualizarServidor;

import eventos.ConexionTerminal;
import eventos.Evento;
import interfaces.IEnviarEvento;
import interfaces.IReceptorEvento;
import redundanciaPasiva.IRedundanciaPasiva;

public class Comunicador implements IEnviarEvento,IEnviarEventoServidor,IConector{

	
	//PATRON SINGLETON
	private static Comunicador instancia;
	
	
	private Comunicador() {
		this.conectado=false;
	}
	public static Comunicador getInstance() {
		if(instancia==null) {
			instancia= new Comunicador();
		}
		return instancia;
	}
	
    //Logica Hay un receptor que se encarga de atajar todos los eventos.
	private IReceptorEvento receptor;
	private IActualizarServidor ControladorServidor=null;
	private IRedundanciaPasiva gestorServidores=null;

	public void setGestorServidores(IRedundanciaPasiva gestorServidores) {
		this.gestorServidores=gestorServidores;
	}
	public void setReceptor(IReceptorEvento receptor) {
		this.receptor=receptor;
	}

    public void notificarReceptor(Evento evento) {
    	receptor.ArriboEvento(evento);
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
	
	public void BajaTerminal(String tipo, int numero) {
		if (numero <= 0) {
		        System.out.println("Número de terminal inválido: " + numero);
		    return;
		}
		else {
			if ("TERMINAL_REGISTRO".equals(tipo)) {
				terminalesRegistro.remove(numero);
				// Después de eliminar del map
				GestorTerminales.getInstance().BajaTerminal(tipo, numero);				
			} else if ("TERMINAL_ATENCION".equals(tipo)) {
				terminalesAtencion.remove(numero);
				// Después de eliminar del map
				GestorTerminales.getInstance().BajaTerminal(tipo, numero);				
			} else if ("TERMINAL_NOTIFICACION".equals(tipo)) {
				terminalesNotificacion.remove(numero);
				// Después de eliminar del map
				GestorTerminales.getInstance().BajaTerminal(tipo, numero);
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
	public void setControlador(IActualizarServidor cs) {
		this.ControladorServidor=cs;
	}
	
	//IConector
	//void iniciarServidor(String ip, int puerto);
	//void iniciarSincronizador(String ip, int puerto);
	//LOGICA SERVIDOR

	private boolean ServidorEncendido;
	@Override
	public void ApagarServidorSecundario() {
		this.ServidorEncendido=false;
	}
	public void iniciarServidor(String ip, int puerto){
	    new Thread(() -> {
	        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
	            System.out.println("Servidor Iniciado en "+ip+":"+puerto);
	            //ControladorServidor.getInstance().estadoEscuchando("Escuchando en:" + puerto);
	            this.ControladorServidor.estadoEscuchando("Escuchando Clientes en IP:  "+ip+" : "+puerto);
	            this.ServidorEncendido=true;
	            while (this.ServidorEncendido) {
	                Socket socketCliente = serverSocket.accept();
	                
	                System.out.println("Nueva conexion entrante");
	                
	                EscuchadorTerminal escuchador = new EscuchadorTerminal(socketCliente);
	                
	                // (opcional pero recomendable) registrar el cliente
	                //registrarTerminal(escuchador);

	                new Thread(escuchador).start();
	            }

	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	    }).start();
	}
	
	
	//Abrimos estos Socket Redundantes (socketServidorR,outR,inR)
	//Para  I-Sync.solicitarSincronizacion()
	private Socket socketServidorR;
	private ObjectOutputStream outR;
	private ObjectInputStream inR;
	private boolean conectado=false;
	
	//Se envia solicitudes de Sincronizacion.
	@Override
	public void enviarEventoASincronizador(Evento e) {
		if(conectado) {
			try {
				outR.writeObject(e);
				outR.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else {
			System.out.println("Se requiere que antes se conecte al OtroServidor");
		}

	}
	@Override
	public void desconectarseDeSincronizador() {
		try {
			outR.close();
			inR.close();
			socketServidorR.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.conectado=false;
	}
	//Se conecta Al Servidor para enviar solicitudes,
	//Pero en la siguiente función recibimos EventoSincronizacionEstado
	@Override
	public void conectarseASincronizador(String ip, int puerto) {
		try {
			socketServidorR= new Socket(ip,puerto);
			outR = new ObjectOutputStream(socketServidorR.getOutputStream());
			inR = new ObjectInputStream(socketServidorR.getInputStream());
			outR.flush();
			this.ControladorServidor.estadoEscuchando("Conectado a Sincronizador en IP:  "+ip+" : "+puerto);
			this.conectado=true;
			System.out.println("Conectado a Servidor Sincronizador");
			new Thread(() -> {
				Evento evento=null;
				while(this.conectado) {
					try {
						evento = (Evento) inR.readObject();
						notificarReceptor(evento);
					} catch (ClassNotFoundException | IOException e) {
						System.out.println("Se cerro la conexion con Sincronizador");
						this.conectado=false;
						this.gestorServidores.NotificarCaidaSincronizador();
					}
				}
			}).start();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Aca en cambio, Esperamos en el puerto Sincronizacion, 
	//para recibir solicitudes de Sincronizacion.
	
	private Socket socketSS;//socketServerSincronizable;
	private ObjectOutputStream outSS;
	private ObjectInputStream inSS;
	private boolean SincronizableConectado=false;
	
	@Override
	public void iniciarSincronizador(String ip, int puerto) {
		SincronizableConectado=false;

	    Thread hilo = new Thread(() -> {

	        try (ServerSocket serverSocket = new ServerSocket(puerto)) {

	            System.out.println("Sincronizador Iniciado en " + ip + ":" + puerto);

	            socketSS = serverSocket.accept();
	            SincronizableConectado=true;
	            inSS = new ObjectInputStream(socketSS.getInputStream());
	            outSS = new ObjectOutputStream(socketSS.getOutputStream());
	            while (true) {

	                Evento evento = (Evento) inSS.readObject();
	                notificarReceptor(evento);
	            }

	        } catch (EOFException e) {
				//e.printStackTrace();
	        	System.out.println("Se perdio la conexion con el Servidor Sincronizable.");
	        } catch (ClassNotFoundException | IOException e) {
				//e.printStackTrace();
	        	System.out.println("Se perdio la conexion con el Servidor Sincronizable.");
	        } catch(Exception cualquiera) {
	        	//cualquiera.printStackTrace();
	        	System.out.println("Se perdio la conexion con el Servidor Sincronizable");
	        }
	        System.out.println("Se vuelve a abrir el Sincrionizador: ");
	        iniciarSincronizador(ip,puerto);
	        this.SincronizableConectado=false;

	    });

	    hilo.start();
	}
	
	@Override
	public boolean estaConectadoSincronizable() {
		return this.SincronizableConectado;
	}
	
	public void enviarEventoASincrionizable(Evento e) {
		try {
			outSS.writeObject(e);
			outSS.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	@Override
	public boolean estaLibre(String ipServidor, int puertoServidor) {
		boolean respuesta=true;
		try {
			ServerSocket serverSocket = new ServerSocket(puertoServidor);
			serverSocket.close();
		} catch (IOException e) {
			respuesta=false;
		}
		return respuesta;
	}
	
}
