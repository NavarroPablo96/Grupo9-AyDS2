package eventos;

import java.util.Date;

public class EventoSolicitudTurno extends Evento {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String dni,hora;
	private Date horaReal;
	

	public EventoSolicitudTurno(String procesoOrigen, String procesoDestino, String dni, String hora, Date horaReal) {
		super(procesoOrigen, procesoDestino);
		this.dni=dni;
		this.hora=hora;
		this.horaReal=horaReal;
	}


	public Date getHoraReal() {
		return horaReal;
	}
	
	public String getDni() {
		return this.dni;
	}
	
	public String getHora() {
		return hora;
	}

}
