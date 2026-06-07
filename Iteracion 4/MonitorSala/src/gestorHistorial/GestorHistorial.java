package gestorHistorial;

import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import comuEntreProcesos.IReceptorEvento;
//import comuEntreProcesos.IRecibirEvento;
import eventos.Evento;
import eventos.EventoNotificar;
import eventos.EventoRecuperacionHistorial;
import eventos.EventoConexionExitosa;
import eventos.EventoRellamar;
import eventos.Turno;
import seguridad.ISeguridadStrategy;
import vista_controlador.Controlador;

public class GestorHistorial implements IReceptorEvento{
	private static GestorHistorial instancia;
	private ISeguridadStrategy encriptador;
	
	public void setEncriptador(ISeguridadStrategy encriptador){
		this.encriptador = encriptador;
	}
	
	private static final int MAX_TURNOS_EN_PANTALLA = 4;
	private ArrayList<Turno> historial;
	private Turno turnoActual;
	private int NumeroTerminal;
    
	private GestorHistorial() {
		this.historial = new ArrayList<Turno>();
		this.turnoActual=null;
	}

	public static GestorHistorial getInstance() {
        if (instancia == null) {
            instancia = new GestorHistorial();
        }
        return instancia;
	}

	public  ArrayList<Turno> getUltimosTurnos() {
		return this.historial;
	}

	public Turno getUltimoTurnoLlamado() {
		return turnoActual;
	}

	@Override
	public void ArriboEvento(Evento e) {
	    if (e instanceof EventoNotificar) {
	    	EventoNotificar evento = (EventoNotificar) e;
	        Turno turno = evento.getTurno();

			turno.setDocumento(encriptador.desencriptar(turno.getDocumento()));

	        turno.setNumeroTerminal(evento.getNumeroTPA());

			

	        if(turnoActual!=null) {
	        	historial.add(0, turnoActual);
	        }
	        this.turnoActual=turno;
	        if (historial.size() > MAX_TURNOS_EN_PANTALLA) {
	            historial.remove(historial.size() - 1); // elimina el más antiguo
	        }
	        
	        Controlador.getInstance().actualizarVistaMonitor("Evento Notificar");
	        reproducirSonido();
	    }
	    else if(e instanceof EventoConexionExitosa) {
	    	EventoConexionExitosa ent= (EventoConexionExitosa) e;
	    	this.NumeroTerminal=ent.getNumero();
	    	Controlador.getInstance().ActualizarVistaNumero(ent.getNumero());
	    }
	    else if(e instanceof EventoRellamar) {
	    	EventoRellamar evento = (EventoRellamar) e;
	        Turno turnoRellamar = evento.getTurno();

			turnoRellamar.setDocumento(encriptador.desencriptar(turnoRellamar.getDocumento()));

	        turnoRellamar.setNumeroTerminal(evento.getNumeroTPA());

	        if(turnoActual==null) {
	        	turnoActual=turnoRellamar;
	        }
	        else {
	        	if(turnoActual.getNumero() != turnoRellamar.getNumero()){
	        		Turno encontrado = null;	//Es para ver si está en el historial.

	        	    for (Turno t : historial) {
	        	        if (t.getNumero() == turnoRellamar.getNumero()) {
	        	            encontrado = t;
	        	            break;
	        	        }
	        	    }
	        	    
	        	    if (encontrado != null) {
	        	    	// encontra es el turno que estaba en el historial
	        	        historial.remove(encontrado);
	        	    }
	        	    historial.add(0, turnoActual);
	        	    turnoActual = turnoRellamar;	        	    	
	        		
	        	}
	        }
	        reproducirSonido();
	        Controlador.getInstance().actualizarVistaMonitor("Evento Rellamar");
	    	System.out.println("FIN EJECUCIÓN EVENTO RELLAMAR");
	    }
	    else if(e instanceof EventoRecuperacionHistorial) {
	    	EventoRecuperacionHistorial erh= (EventoRecuperacionHistorial)e;
	    	this.turnoActual = erh.getTurnoActual();
	    	
	    	ArrayList<Turno> lista = erh.getListaEnMonitor();
			this.historial = new ArrayList<Turno>();
	    	int i =0;
	    	for (Turno t : lista) {
	    		this.historial.add(i,t);
	    		i++;
	    		
	    		if (historial.size() > MAX_TURNOS_EN_PANTALLA) {
	    			historial.remove(historial.size() - 1); // elimina el más antiguo
	    		}
    	    }
	        Controlador.getInstance().actualizarVistaMonitor("EventoRecuperacionHistorial");
	    	System.out.println("FIN EJECUCIÓN EventoRecuperacionHistorial");
	    }
	    else {
	    	System.out.println("Llego un Evento");
	    	System.out.println("Tipo: " + e.getClass().getName());
	        System.out.println("Origen: " + e.getProcesoOrigen());
	        System.out.println("Destino: " + e.getProcesoDestino());
	    }
	}
	
	public static void reproducirSonido() {
		try {
            File archivo = new File("notificacion.wav");             
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivo);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}

	public int getNumeroTerminal() {
		return NumeroTerminal;
	}

}
