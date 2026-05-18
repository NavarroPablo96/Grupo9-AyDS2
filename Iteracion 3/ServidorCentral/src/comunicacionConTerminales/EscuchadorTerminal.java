package comunicacionConTerminales;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


import eventos.Evento;
import eventos.ConexionTerminal;
import eventos.EventoConexionExitosa;
import eventos.EventoFilaNoVacia;
import eventos.EventoFilaVacia;

import gestorFilaYTerminales.GestorFila;

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
		try {
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    private void AgregarTerminal() {
    	// IMPORTANTE: primero Output, después Input		

		System.out.println("EscuchadorTerminal-39-Terminal conectada: " + socket);
    	ConexionTerminal primerEvento=null;
		try {
			primerEvento = (ConexionTerminal) in.readObject();
		} catch (ClassNotFoundException | IOException e) {//ClassNotFountException //IOException
			e.printStackTrace();
		}
		tipo=primerEvento.getTipoTerminal();
		this.numero=Comunicador.getInstance().AgregarTerminal(primerEvento,this);
        
		if(this.numero != -1) {//Este es el número de la terminal.
        	EventoConexionExitosa ENT=new EventoConexionExitosa("SERVIDOR","TERMINAL",this.numero) ;
        	this.activo=true;
        	System.out.println("Se agrego una terminal de tipo="+tipo+" numero="+this.numero);
        	enviar (ENT);
        }
        else {
        	// menos uno es porque no fue posible agregar la terminal 
        	//y corresponde cerrar el hilo no entrando al while
        	this.activo=false;
        }
		if ("TERMINAL_ATENCION".equals(tipo)) {
		    int cantidad = GestorFila.getInstance().getCantidadTurnos();
		    Evento efilafilaVaciaoNO = null;
		    if(cantidad>0) {
		    	efilafilaVaciaoNO=new EventoFilaNoVacia("SERVIDOR","OPERADORES",cantidad);
		    }
		    else {
		    	efilafilaVaciaoNO=new EventoFilaVacia("SERVIDOR","OPERADORES");
		    }
		    Comunicador.getInstance().publicarOperadores(efilafilaVaciaoNO);
		}
    }

    @Override
    public void run() {
    	
    	
        try {
        	AgregarTerminal();            
        	//En este punto ya se agrego la Terminal a la lista de correspondiente
        	//Hay 3 listas de terminal.
        	//En este while este hilo escucha a esta terimnal.

            while (activo) {
                Evento evento = (Evento) in.readObject();
            	Comunicador.getInstance().notificarReceptor(evento);
            }

        } catch (Exception e) {
            System.out.println("Terminal desconectada: " + socket);
            //Se deberia avisar a GestorFilayTerminales.TerminalDesconectada(tipo,numero)
            Comunicador.getInstance().BajaTerminal(tipo,numero);
        } finally {
            cerrarConexion();
        }
    }

    // Método para enviar mensajes A ESTE cliente
    public synchronized void enviar(Evento evento) {
    	System.out.println("EscuchadorTerminal-99-Se envia evento:"+evento.getClass());
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