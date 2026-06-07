package gestorFila;

import eventos.Turno;

public interface IEstadoFila {
	IColaTurno getCola();
	void setEstado(IColaTurno c,int numeroTurnoSiguiente, int cantidadPone, int cantidadSaca);
	int getCantidadSaca();
	int getCantidadPone();
	int getNumeroTurnoSiguiente();
	Historial getHistorial();
	RegistroRellamar getRegistro();
	void setHistorial(Historial historial);
	void setRegistro(RegistroRellamar llamados);
	public void actualizacionNuevoTurno(Turno t);
	public void actualizacionLlamarSiguiente(int numeroTerminalQueLlama, Turno turnoLlamado);
	void actualizacionRellamar(int numeroTerminalQueLlama, Turno turnoLlamado);
}
