package comunicacion;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eventos.ConexionTerminal;
import eventos.Evento;
import eventos.EventoSolicitudTurno;
import interfaces.IComunicador;
import interfaces.IReceptorEvento;
import interfaces.IRegistro;

public class Comunicador implements IComunicador,IRegistro{

    private IReceptorEvento receptor; //controlador
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
    
    
    //@Override
    //public void enviarEvento(Evento evento) {
    private void enviarEvento(Evento evento) {
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


	@Override
	public void nuevoTurno(String dni, int NumeroTerminal) {

		Date horaReal = new Date();
        String hora = new SimpleDateFormat("HH:mm").format(horaReal);
		EventoSolicitudTurno solicitud = new EventoSolicitudTurno("TR"+NumeroTerminal,"Servidor",dni, hora, horaReal);
		enviarEvento(solicitud);
		
	}

	private String ip ,ipSecundario;
	private int puerto, puertoSecundario;
	@Override
	public void conectar(String ip, int puerto, String ipSecundario, int puertoSecundario,boolean ConectarAPrimario) {
		this.ip=ip;
		this.puerto=puerto;
		this.ipSecundario=ipSecundario;
		this.puertoSecundario=puertoSecundario;
		if(ConectarAPrimario) {
			try {
				conectarPrimario();
			} catch (UnknownHostException e) {
				System.out.println("Se perdió la conexión con el servidor Primario");
				esperarReconexion();
				conectar(ip,puerto, ipSecundario, puertoSecundario,false);
			} catch (IOException e) {
				System.out.println("Se perdió la conexión con el servidor Primario");
				esperarReconexion();
				conectar(ip,puerto, ipSecundario, puertoSecundario,false);
			}
		}
		else {
			try {
				conectarSecundario();
			} catch (UnknownHostException e) {
				System.out.println("Se perdió la conexión con el servidor Secundario");
				esperarReconexion();
				conectar(ip,puerto, ipSecundario, puertoSecundario,true);
			} catch (IOException e) {
				System.out.println("Se perdió la conexión con el servidor Secundario");
				esperarReconexion();
				conectar(ip,puerto, ipSecundario, puertoSecundario,true);
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
		out.writeObject(new ConexionTerminal("terminalRegistro","Servidor","TERMINAL_REGISTRO"));
		out.flush();
		// Hilo que escucha SIEMPRE
		new Thread(() -> {
			try {
				while (true) {
					System.out.println("Conectados a Servidor Secundario ip:puerto="+ipSecundario+":"+puertoSecundario);
					Evento evento = (Evento) in.readObject();
					System.out.println("Llego un Evento"+evento);
					receptor.recibirEvento(evento);
					
				}
			} catch (Exception e) {
				System.out.println("Se perdió la conexión con el servidor Secundario");
				conectar(ip,puerto,ipSecundario,puertoSecundario,false);
				
			}
		}).start();
	}
	private void conectarPrimario() throws UnknownHostException, IOException {
		socket = new Socket(ip, puerto);
		out = new ObjectOutputStream(socket.getOutputStream());		//IOException
		in = new ObjectInputStream(socket.getInputStream());		//IOException
		out.flush();
		out.writeObject(new ConexionTerminal("terminalRegistro","Servidor","TERMINAL_REGISTRO"));
		out.flush();
		// Hilo que escucha SIEMPRE
		new Thread(() -> {
			try {
				while (true) {
					System.out.println("Conectados a Servidor Primario ip:puerto="+ip+":"+puerto);
					Evento evento = (Evento) in.readObject();
					System.out.println("Llego un Evento"+evento);
					receptor.recibirEvento(evento);
					
				}
			} catch (Exception e) {
				System.out.println("Se perdió la conexión con el servidor Primario");
				conectar(ip,puerto,ipSecundario,puertoSecundario,false);
				
			}
		}).start();
	}

    
}
