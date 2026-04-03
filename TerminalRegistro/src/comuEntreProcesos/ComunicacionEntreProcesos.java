package comuEntreProcesos;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import vista_controlador.Controlador;

public class ComunicacionEntreProcesos implements IEnviarEvento {
	//Logica Singleton
	private static ComunicacionEntreProcesos instancia;
	
    private ComunicacionEntreProcesos() {
    }
    
    public static ComunicacionEntreProcesos getInstance() {
        if (instancia == null) {
            instancia = new ComunicacionEntreProcesos();
        }
        return instancia;
    }
	
    
    //Logica de red:
    private Socket socket;
 // texto    PrintWriter / BufferedReader  
 //   private PrintWriter out;	//Estas clases solo envian texto
 //   private BufferedReader in;	//Necesitan metodos serializarEvento DeserializarEvento
//    ObjectOutputStream / ObjectInputStream  // objetos	Con estas clases se pueden enviar objetos directamente
    //Necesita que el objeto Evento implemente Serialiable
    private ObjectOutputStream outIO;	//Tengo que crear out para enviar a Interfaz de Operador
    
    
    public void conectar(String ip, int puerto) throws UnknownHostException, IOException {
    	socket = new Socket(ip, puerto);
    	outIO = new ObjectOutputStream(socket.getOutputStream());
    	outIO.flush();
    	Controlador.getInstance().estadoConectadoAOperador("Conectados a Operador");
    }
    
    
    @Override
    public void enviarEvento(Evento evento) {
    	System.out.println("Se intenta enviar el evento: "+evento.getClass().getName());
        try {
        	//puede tirar null pointer exception, if(out != null){ ...  }else{System.out.println("Falta establecer conexión");}
        	outIO.writeObject(evento);
        	outIO.flush();
            System.out.println("Se supone que se envio exitosamente xd");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
