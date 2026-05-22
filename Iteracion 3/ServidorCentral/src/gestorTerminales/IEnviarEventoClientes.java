package gestorTerminales;

import eventos.Evento;

public interface IEnviarEventoClientes {

	void enviarEvento(Evento evento, String tipoTerminal, int numeroTerminal);
	public void publicarOperadores(Evento evento);
	public void publicarNotificadores(Evento evento);
}
