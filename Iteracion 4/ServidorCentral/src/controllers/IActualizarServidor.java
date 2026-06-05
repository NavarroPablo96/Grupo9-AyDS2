package controllers;

import java.util.List;

import eventos.Turno;

public interface IActualizarServidor {
	//public void actualizarVistaServidor();
	void actualizarTerminalesVistaServidor(
		    List<String> TerminalesRegistro,
		    List<String> TerminalesAtencion,
		    List<String> TerminalesNotificacion);
	public void actualizarTurnosVistaServidor(List<Turno> Turnos);
	public void estadoEscuchando(String escuchandoEn);
}
