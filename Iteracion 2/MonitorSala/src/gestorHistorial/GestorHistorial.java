package gestorHistorial;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import comuEntreProcesos.IReceptorEvento;
//import comuEntreProcesos.IRecibirEvento;
import eventos.Evento;
import eventos.EventoNotificar;
import eventos.EventoNumeroTerminal;
import eventos.EventoRellamar;
import eventos.Turno;
import vista_controlador.Controlador;

public class GestorHistorial implements IReceptorEvento{
	private static GestorHistorial instancia;
	//private static IRecibirEvento notificador;
	
	private static final int MAX_TURNOS_EN_PANTALLA = 4;
	private List<Turno> historial;
	private Turno turnoActual;
	private int NumeroTerminal;
    
	private GestorHistorial() {
		this.historial = new ArrayList<>();
		this.turnoActual=null;
	}

	public static GestorHistorial getInstance() {
        if (instancia == null) {
            instancia = new GestorHistorial();
        }
        return instancia;
	}

	public  List<Turno> getUltimosTurnos() {
		return new ArrayList<>(historial);
	}

	public Turno getUltimoTurnoLlamado() {
		return turnoActual;
	}

	@Override
	public void ArriboEvento(Evento e) {
	    if (e instanceof EventoNotificar) {
	    	EventoNotificar evento = (EventoNotificar) e;
	        Turno turno = evento.getTurno();


	        if(turnoActual!=null) {
	        	historial.add(0, turnoActual);
	        }
	        this.turnoActual=turno;
	        if (historial.size() > MAX_TURNOS_EN_PANTALLA) {
	            historial.remove(historial.size() - 1); // elimina el más antiguo
	        }
	        
	        Controlador.getInstance().actualizarVistaMonitor();
	        reproducirSonido();
	    }
	    else if(e instanceof EventoNumeroTerminal) {
	    	EventoNumeroTerminal ent= (EventoNumeroTerminal) e;
	    	this.NumeroTerminal=ent.getNumero();
	    	Controlador.getInstance().ActualizarVistaNumero(ent.getNumero());
	    }
	    else if(e instanceof EventoRellamar) {
	    	EventoRellamar evento = (EventoRellamar) e;
	        Turno turnoRellamar = evento.getTurno();
	    	
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
	        Controlador.getInstance().actualizarVistaMonitor();
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
