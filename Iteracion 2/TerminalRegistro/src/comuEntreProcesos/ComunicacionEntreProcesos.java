package comuEntreProcesos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import eventos.ConexionTerminal;
import eventos.Evento;
import vista_controlador.Controlador;

public class ComunicacionEntreProcesos implements IEnviarEvento,IRecibirEvento {
	//Logica Singleton
	private static ComunicacionEntreProcesos instancia;
	
    private ComunicacionEntreProcesos() {
    	this.receptores=new ArrayList<>();
    }
    
    public static ComunicacionEntreProcesos getInstance() {
        if (instancia == null) {
            instancia = new ComunicacionEntreProcesos();
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
    
    //Logica de red:
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    
    public void conectar(String ip, int puerto) throws UnknownHostException, IOException {
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
    	            Evento evento = (Evento) in.readObject();
    	            notificarReceptores(evento);
    	        }
    	    } catch (Exception e) {
    	        System.out.println("Se perdió la conexión con el servidor");
    	    }
    	}).start();
    	Controlador.getInstance().estadoConectadoAServidor("Conectados al Servidor");
    }

    public boolean estaConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed() && out != null;
    }
    
    
    @Override
    public void enviarEvento(Evento evento) {
    	System.out.println("Se intenta enviar el evento: "+evento.getClass().getName());
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


}
