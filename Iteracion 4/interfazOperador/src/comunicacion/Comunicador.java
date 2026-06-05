package comunicacion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import eventos.ConexionTerminal;
import eventos.Evento;
import interfaces.IReceptorEvento;

public class Comunicador implements IComunicador{

    private IReceptorEvento receptor; //gestor eventos
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    

    private static Comunicador instancia;
    private Comunicador() {
	}
    public static Comunicador getInstance() {
    	if(instancia==null) {
    		instancia=new Comunicador();
    	}
    	return instancia;
    }

    public boolean estaConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed() && out != null;
    }
    
	@Override
	public void enviarEvento(Evento evento) {
    	System.out.println("Se envia el evento: "+evento.getClass().getName());
        if (!estaConectado()) {
            System.out.println("No hay conexion con el servidor. No se envio el turno.");
            return;
        }
        try {
        	//puede tirar null pointer exception, if(out != null){ ...  }else{System.out.println("Falta establecer conexión");}
        	out.writeObject(evento);
        	out.flush();
        } catch (Exception e) {
        	
        	this.socket=null;
            System.out.println("No hay conexion con el servidor. No se envio el turno.");
        }
    }

	@Override
	public void setReceptor(IReceptorEvento r) {
		this.receptor=r;
	}

	
	//Conectar a primario y secundario con reintento: 
	private String ip ,ipSecundario;
	private int puerto, puertoSecundario;
	private boolean ConectarAPrimario;
	@Override
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
		out.writeObject(new ConexionTerminal("terminalAtencion","Servidor","TERMINAL_ATENCION"));
		out.flush();
		// Hilo que escucha SIEMPRE
		new Thread(() -> {
			try {
				System.out.println("Conectados a Servidor Secundario ip:puerto="+ipSecundario+":"+puertoSecundario);
				while (true) {
					Evento evento = (Evento) in.readObject();
					System.out.println("Llego un Evento"+evento);
					receptor.recibirEvento(evento);
					
				}
			} catch(SocketException asd) {
	        	System.out.println(" ");
				System.out.println("Se perdió la conexión con el servidor Secundario");
				conectar(ip,puerto,ipSecundario,puertoSecundario);
	        }catch (Exception e) {
		        e.printStackTrace();
				System.out.println("Hubo exception en ComunicacionEntreProcesos155");
			}
		}).start();
	}
	private void conectarPrimario() throws UnknownHostException, IOException {
		socket = new Socket(ip, puerto);
		out = new ObjectOutputStream(socket.getOutputStream());		//IOException
		in = new ObjectInputStream(socket.getInputStream());		//IOException
		out.flush();
		out.writeObject(new ConexionTerminal("terminalAtencion","Servidor","TERMINAL_ATENCION"));
		out.flush();
		// Hilo que escucha SIEMPRE
		new Thread(() -> {
			try {
				System.out.println("Conectados a Servidor Primario ip:puerto="+ip+":"+puerto);
				while (true) {
					Evento evento = (Evento) in.readObject();
					System.out.println("Llego un Evento"+evento);
					receptor.recibirEvento(evento);
					
				}
			} catch(SocketException asd) {
	        	System.out.println(" ");
				System.out.println("Se perdió la conexión con el servidor Primario");
				conectar(ip,puerto,ipSecundario,puertoSecundario);
	        }catch (Exception e) {
		        e.printStackTrace();
				System.out.println("Hubo exception en ComunicacionEntreProcesos-182");
			}
		}).start();
	}


    
}
