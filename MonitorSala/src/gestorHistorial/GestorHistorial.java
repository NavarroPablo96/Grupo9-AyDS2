package gestorHistorial;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import comuEntreProcesos.ComunicacionEntreProcesos;
import comuEntreProcesos.Evento;
import comuEntreProcesos.EventoLlamarSiguiente;
import comuEntreProcesos.IReceptorEvento;
import comuEntreProcesos.IRecibirEvento;
import comuEntreProcesos.Turno;
import vista_controlador.Controlador;

public class GestorHistorial implements IReceptorEvento{
	private static GestorHistorial instancia;
	private static IRecibirEvento notificador;
	private List<Turno> historial;
	private Turno turnoActual;
    
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

	public static void main(String[] args) {
    	System.out.println("MONITOR");
    	notificador = ComunicacionEntreProcesos.getInstance();
    	notificador.suscribirse(GestorHistorial.getInstance());
    	
    	Controlador.getInstance().initControl();
    }

	public  List<Turno> getUltimosTurnos() {
		List<Turno> ultimos = new ArrayList<>();

        for (int i = 0; i < Math.min(4, historial.size()); i++) {
        	ultimos.add(historial.get(i));
        }

        return ultimos;
	}

	public Turno getUltimoTurnoLlamado() {
		return turnoActual;
	}

	@Override
	public void ArriboEvento(Evento e) {
	    if (e instanceof EventoLlamarSiguiente) {
	    	EventoLlamarSiguiente evento = (EventoLlamarSiguiente) e;
	        Turno turno = evento.getTurno();

	        if (turnoActual != null) {
	            historial.add(turnoActual);

	            // 2. Mantener máximo 4 elementos
	            if (historial.size() > 4) {
	                historial.remove(0); // elimina el más antiguo
	            }
	        }
	        
	        reproducirSonido();
	        
	        turnoActual = turno;
	        
	        Controlador.getInstance().actualizarVistaOperador();
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

}
