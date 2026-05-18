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

public class ComunicacionEntreProcesos implements IRecibirEvento, IEnviarEvento {
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

    private void notificarReceptores(Evento evento) {
    	for (IReceptorEvento receptor : receptores) {
            receptor.ArriboEvento(evento);
        }
    }
    
    //Logica de red:
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    
    
    public void conectar(String ip, int puerto) throws UnknownHostException, IOException {
    	socket = new Socket(ip, puerto);
		out = new ObjectOutputStream(socket.getOutputStream());		//IOException
		in = new ObjectInputStream(socket.getInputStream());		//IOException
		out.flush();
    	out.writeObject(new ConexionTerminal("terminalAtencion","Servidor","TERMINAL_ATENCION"));
    	out.flush();
    	// Hilo que escucha SIEMPRE todos los eventos que llegan desde el servidor
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
    	Controlador.getInstance().estadoConectadoAServidor("Conectados a Servidor");
    }
    
    public boolean estaConectado() {
        return socket != null && socket.isConnected() && !socket.isClosed() && out != null;
    }
    
    
    @Override
    public void enviarEvento(Evento evento) {
    	System.out.println("Se intenta enviar el evento: "+evento.getClass().getName());
        if (!estaConectado()) {
            System.out.println("No hay conexion activa con el Operador. No se envio el turno.");
            return;
        }
        try {
        	//puede tirar null pointer exception, if(out != null){ ...  }else{System.out.println("Falta establecer conexión");}
        	out.writeObject(evento);
        	out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /*public void iniciarServidor(int puerto) {
    new Thread(() -> {
    	try (ServerSocket serverSocket = new ServerSocket(puerto)) {
        	System.out.println("Esperando conexión...");	
        	Controlador.getInstance().estadoEscuchando("Escuchando en:"+puerto);	//le aviso al controlado que estamos escuchando así procede con las vistas
            socket = serverSocket.accept();
        	inTR = new ObjectInputStream(socket.getInputStream());
            System.out.println("Conexión establecida, esperando eventos...");
            while (true) {
                Evento evento = (Evento) inTR.readObject();
                notificarReceptores(evento);
            }
            

        } catch (BindException e) {
        	//Ya se está usando el puerto
            e.printStackTrace();
        }catch (Exception e) {
        	if(e instanceof SocketException) {
            	System.out.println("Exception-Monitor-iniciarServidor, Se desconecto la InterfazOperador");
            	System.out.println("Esperando conexión en puerto:" + puerto);
            	ComunicacionEntreProcesos.getInstance().iniciarServidor(puerto);
        	}
        	else {
        		e.printStackTrace();            		
        	}
        }
    }).start();
}*/

}
