package comunicacion;

import eventos.EventoLlamarSiguiente;
import eventos.EventoRellamar;
import eventos.Turno;
import seguridad.ISeguridadStrategy;

public class API_Servidor implements IAtencion{
    
    private IComunicador conexion;
    private ISeguridadStrategy encriptador;

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
    Turno ultimoCopia = new Turno(ultimo.getNumero(), ultimo.getDocumento(), ultimo.getHoraRegistro(), ultimo.getHoraHoraDeLlamado());
    ultimoCopia.setDocumento(encriptador.encriptar(ultimoCopia.getDocumento()));

		EventoRellamar evento = new EventoRellamar(Origen, nroTerminal, "Notificador",ultimoCopia);
		conexion.enviarEvento(evento);
    }

    public void setEncriptador(ISeguridadStrategy encriptador){
      this.encriptador = encriptador;
    }
    
}
