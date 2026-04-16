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
    
    public void conectar(String ip, int puerto) throws UnknownHostException, IOException {
    	socket = new Socket(ip, puerto);
		out = new ObjectOutputStream(socket.getOutputStream());		//IOException
		in = new ObjectInputStream(socket.getInputStream());		//IOException
		
    	out.flush();
    	out.writeObject(new ConexionTerminal("terminalNotificacion","Servidor","TERMINAL_NOTIFICACION"));
    	out.flush();
    	// Hilo que escucha SIEMPRE
    	new Thread(() -> {
    	    try {
    	        while (true) {
    	            Evento evento = (Evento) in.readObject();
    	            notificarReceptores(evento);
    	        }
    	    } catch (Exception e) {
    	    	e.printStackTrace();
    	        System.out.println("Se perdió la conexión con el servidor");
    	    }
    	}).start();
    	Controlador.getInstance().estadoConectadoAServidor("Conectados al Servidor");
    }

    public boolean estaConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed() && out != null;
    }
    
}
