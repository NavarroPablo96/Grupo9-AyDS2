package comunicacion;

import eventos.EventoLlamarSiguiente;
import eventos.EventoRellamar;
import eventos.Turno;

public class API_Servidor implements IAtencion{
    
    private IComunicador conexion;

    public API_Servidor(IComunicador conexion){
        this.conexion = conexion;
    }


    public void llamarSiguiente(int nroTerminal){
		String Origen = "TA" + nroTerminal;
		EventoLlamarSiguiente evento = new EventoLlamarSiguiente(Origen,nroTerminal, "Notificador");
		conexion.enviarEvento(evento);
    }
    
    public void renotificar(int nroTerminal, Turno ultimo){
		String Origen = "TA" + nroTerminal;
		EventoRellamar evento = new EventoRellamar(Origen, nroTerminal, "Notificador",ultimo);
		conexion.enviarEvento(evento);
    }
    
}
