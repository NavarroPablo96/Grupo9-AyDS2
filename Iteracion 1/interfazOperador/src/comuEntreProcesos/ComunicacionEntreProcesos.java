package comuEntreProcesos;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

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
 // texto    PrintWriter / BufferedReader  
 //   private PrintWriter out;	//Estas clases solo envian texto
 //   private BufferedReader in;	//Necesitan metodos serializarEvento DeserializarEvento
//    ObjectOutputStream / ObjectInputStream  // objetos	Con estas clases se pueden enviar objetos directamente
    //Necesita que el objeto Evento implemente Serialiable
    private ObjectOutputStream outMS;	//Tengo que crear in y out para TR terminal Registro
    private ObjectInputStream inTR;	// y para MS = Monitor Sala
    

    public void iniciarServidor(int puerto) {
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
    }
    
    public void conectar(String ip, int puerto) throws UnknownHostException, IOException {
    	socket = new Socket(ip, puerto);
    	outMS = new ObjectOutputStream(socket.getOutputStream());
    	outMS.flush();
        
    	Controlador.getInstance().estadoConectadoAMonitor("Conectados a Monitor");
    }
    
    
    @Override
    public void enviarEvento(Evento evento) {
    	try {
        	//puede tirar null pointer exception, if(out != null){ ...  }else{System.out.println("Falta establecer conexión");}
        	outMS.writeObject(evento);
        	outMS.flush();
    	} catch (Exception e) {
            e.printStackTrace();
        }
    }
}
