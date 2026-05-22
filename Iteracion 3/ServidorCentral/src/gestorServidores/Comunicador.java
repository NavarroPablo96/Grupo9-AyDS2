package gestorServidores;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import controllers.IActualizarServidor;

import eventos.Evento;
import gestorEventos.IReceptorEvento;
import gestorTerminales.EscuchadorTerminal;
import gestorTerminales.IGestorTerminal;

public class Comunicador implements IEnviarEventoServidores,IConector{

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

	public void setReceptor(IReceptorEvento receptor) {
		this.receptor=receptor;
	}
	
	public void notificarReceptor(Evento evento) {
		receptor.ArriboEvento(evento);
	}
	
	//INTERFACES 
	private IRedundanciaPasiva gestorServidores=null;
	private IActualizarServidor ControladorServidor=null;
	private IGestorTerminal igt;
	

	public void setGestorServidores(IRedundanciaPasiva gestorServidores) {
		this.gestorServidores=gestorServidores;
	}

	public void setControlador(IActualizarServidor cs) {
		this.ControladorServidor=cs;
	}
	
	public void setGestorTerminal(IGestorTerminal igt) {
		this.igt=igt;
	}
	
	//IConector
	//void iniciarServidor(String ip, int puerto);
	//void iniciarSincronizador(String ip, int puerto);
	//LOGICA SERVIDOR

	
	public void iniciarServidor(String ip, int puerto){
	    new Thread(() -> {
	        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
	            System.out.println("Servidor Iniciado en "+ip+":"+puerto);
	            //ControladorServidor.getInstance().estadoEscuchando("Escuchando en:" + puerto);
	            this.ControladorServidor.estadoEscuchando("Escuchando Clientes en IP:  "+ip+" : "+puerto);
	            while (true) {
	                Socket socketCliente = serverSocket.accept();
	                
	                System.out.println("Nueva conexion entrante");
	                
	                EscuchadorTerminal escuchador = new EscuchadorTerminal(socketCliente,this.igt,receptor);
	                
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
