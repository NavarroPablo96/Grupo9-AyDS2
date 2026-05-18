package interfaces;

import eventos.EventoSolicitudTurno;

public interface IRegistro {

	public void nuevoTurno(EventoSolicitudTurno evento,String tipoTerminal,int numeroTerminal);

}
