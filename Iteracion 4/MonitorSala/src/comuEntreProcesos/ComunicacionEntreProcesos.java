package comuEntreProcesos;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import eventos.ConexionTerminal;
import eventos.Evento;
import vista_controlador.Controlador;

public class ComunicacionEntreProcesos implements IRecibirEvento {
	//Logica Singleton
	private static ComunicacionEntreProcesos instancia;
	
    private ComunicacionEntreProcesos() {
        this.receptores = new ArrayList<>();
    }
    
    public static ComunicacionEntreProcesos getInstance() {
        if (instancia == null) {
            instancia = new ComunicacionEntreProcesos();
        }
        return instancia;
    }
	
    //Logica observer Observable
    private List<IReceptorEvento> receptores;

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

    private void notificarReceptores(Evento evento) {
        for (IReceptorEvento receptor : receptores) {
            receptor.ArriboEvento(evento);
        }
    }
    
    
    
    //Logica de red:
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public boolean estaConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed() && out != null;
    }
    
	//Conectar a primario y secundario con reintento: 
	private String ip ,ipSecundario;
	private int puerto, puertoSecundario;
	private boolean ConectarAPrimario;
	
	public void conectar(String ip, int puerto, String ipSecundario, int puertoSecundario) {
		this.ip=ip;
		this.puerto=puerto;
		this.ipSecundario=ipSecundario;
		this.puertoSecundario=puertoSecundario;
		this.ConectarAPrimario=true;
		conectarInterno(3);
	}
	private void conectarInterno(int intento) {
		if(ConectarAPrimario) {
			if(intento>0) {
				try {
					intento --;
					conectarPrimario();
				} catch (UnknownHostException e) {
					System.out.println("No fue posible establecer conexión con el servidor Primario");
					esperarReconexion();
					conectarInterno(intento);
				} catch (IOException e) {
					System.out.println("No fue posible establecer conexión con el servidor Primario");
					esperarReconexion();
					conectarInterno(intento);
				}
			}
			else {
				this.ConectarAPrimario=false;
				conectarInterno(3);
			}
		}
		else {
			if(intento>0) {
				try {
					intento --;
					conectarSecundario();
				} catch (UnknownHostException e) {
					System.out.println("No fue posible establecer conexión con el servidor Secundario");
					esperarReconexion();
					conectarInterno(intento);
				} catch (IOException e) {
					System.out.println("No fue posible establecer conexión con el servidor Secundario");
					esperarReconexion();
					conectarInterno(intento);
				}
			}
			else {
				this.ConectarAPrimario=true;
				conectarInterno(3);
			}
		}
		
	}
	private void esperarReconexion() {

	    try {

	        System.out.println("Reintentando conexión en 3 segundos...");
	        Thread.sleep(3000);

	    } catch (InterruptedException e) {

	        e.printStackTrace();
	    }
	}
	
	private void conectarSecundario() throws UnknownHostException, IOException {
		socket = new Socket(ipSecundario, puertoSecundario);
		out = new ObjectOutputStream(socket.getOutputStream());		//IOException
		in = new ObjectInputStream(socket.getInputStream());		//IOException
		out.flush();
    	out.writeObject(new ConexionTerminal("terminalNotificacion","Servidor","TERMINAL_NOTIFICACION"));
		out.flush();
		// Hilo que escucha SIEMPRE
		Controlador.getInstance().estadoConectadoAServidor("Conectados al Servidor");
		new Thread(() -> {
			try {
				System.out.println("Conectados a Servidor Secundario ip:puerto="+ipSecundario+":"+puertoSecundario);
				while (true) {
					Evento evento = (Evento) in.readObject();
					System.out.println("Llego un Evento "+evento);
					notificarReceptores(evento);					
				}
			} catch(IOException asd) { //| SocketException | EOFException | ConnectException 
		        System.out.println(" ");
				System.out.println("Se perdió la conexión con el servidor Secundario");
				conectar(ip,puerto,ipSecundario,puertoSecundario);
	        }catch (Exception e) {
		        e.printStackTrace();
				System.out.println("Hubo exception en ComunicacionEntreProcesos181");
			}
		}).start();
	}
	private void conectarPrimario() throws UnknownHostException, IOException {
		socket = new Socket(ip, puerto);
		out = new ObjectOutputStream(socket.getOutputStream());		//IOException
		in = new ObjectInputStream(socket.getInputStream());		//IOException
		out.flush();
    	out.writeObject(new ConexionTerminal("terminalNotificacion","Servidor","TERMINAL_NOTIFICACION"));
		out.flush();
		// Hilo que escucha SIEMPRE
		Controlador.getInstance().estadoConectadoAServidor("Conectados al Servidor");
		new Thread(() -> {
			try {
				System.out.println("Conectados a Servidor Primario ip:puerto="+ip+":"+puerto);
				while (true) {
					Evento evento = (Evento) in.readObject();
					System.out.println("Llego un Evento "+evento);
					notificarReceptores(evento);		
					
				}
			} catch(IOException asd) { //| SocketException | EOFException | ConnectException 
		        System.out.println(" ");
				System.out.println("Se perdió la conexión con el servidor Primario");
				conectar(ip,puerto,ipSecundario,puertoSecundario);
	        }catch (Exception e) {
		        e.printStackTrace();
				System.out.println("Hubo exception en ComunicacionEntreProcesos-209");
			}
		}).start();
	}
    
}
