package comunicacionConTerminales;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import eventos.ConexionTerminal;
import eventos.Evento;
import eventos.EventoFilaNoVacia;
import eventos.EventoFilaVacia;
import eventos.EventoNumeroTerminal;
import gestorFilaYTerminales.GestorFilaYTerminales;

public class EscuchadorTerminal implements Runnable {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String tipo; // "TR" o "TPA" (opcional según tu lógica)
    private int numero;
    private boolean activo = false;

    public EscuchadorTerminal(Socket socket) {
        this.socket = socket;
        this.numero=-2;
    }

    @Override
    public void run() {
        try {
        	// IMPORTANTE: primero Output, después Input		
			out = new ObjectOutputStream(socket.getOutputStream());		//IOException
			in = new ObjectInputStream(socket.getInputStream());		//IOException

			System.out.println("Terminal conectada: " + socket);
	    	ConexionTerminal primerEvento;
			primerEvento = (ConexionTerminal) in.readObject();		//ClassNotFountException //IOException
			tipo=primerEvento.getTipoTerminal();
			this.numero=ComunicacionesConTerminales.getInstance().AgregarTerminal(primerEvento,this);
            
			if(this.numero != -1) {//Este es el número de la terminal.
            	EventoNumeroTerminal ENT=new EventoNumeroTerminal("SERVIDOR","TERMINAL",this.numero) ;
            	this.activo=true;
            	System.out.println("Se agrego una terminal de tipo="+tipo+" numero="+this.numero);
            	System.out.println("Se envia El EventoNumeroTerminal");
            	enviar (ENT);
            }
            else {
            	// menos uno es porque no fue posible agregar la terminal 
            	//y corresponde cerrar el hilo no entrando al while
            	this.activo=false;
            }
			if ("TERMINAL_ATENCION".equals(tipo)) {
    		    int cantidad = GestorFilaYTerminales.getInstance().cantidadTurnos();
    		    Evento efilafilaVaciaoNO = null;
    		    if(cantidad>0) {
    		    	efilafilaVaciaoNO=new EventoFilaNoVacia("SERVIDOR","OPERADORES",cantidad);
    		    }
    		    else {
    		    	efilafilaVaciaoNO=new EventoFilaVacia("SERVIDOR","OPERADORES");
    		    }
    		    ComunicacionesConTerminales.getInstance().publicarOperadores(efilafilaVaciaoNO);
    		}
            

            while (activo) {
            	//En este punto ya se agrego la Terminal a la lista de correspondiente
            	//Hay 3 listas de terminal.
            	//En este while este hilo escucha a esta terimnal.
                Evento evento = (Evento) in.readObject();
            	ComunicacionesConTerminales.getInstance().notificarReceptores(evento);
            }

        } catch (Exception e) {
            System.out.println("Terminal desconectada: " + socket);
            //Se deberia avisar a GestorFilayTerminales.TerminalDesconectada(tipo,numero)
            ComunicacionesConTerminales.getInstance().BajaTerminal(tipo,numero);
        } finally {
            cerrarConexion();
        }
    }

    // Método para enviar mensajes A ESTE cliente
    public synchronized void enviar(Evento evento) {
        try {
            out.writeObject(evento);
            out.flush();
        } catch (IOException e) {
            System.out.println("Error enviando a: " + socket);
        }
    }

    public void cerrarConexion() {
        activo = false;
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters útiles
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public void setNumero(int numero) {
        this.numero = numero;
    }
    

    public Socket getSocket() {
        return socket;
    }
}