package comunicacionConTerminales;

import eventos.Evento;

public interface IEnviarEvento {

	void enviarEvento(Evento evento, String tipoTerminal, int numeroTerminal);
	void enviarEvento(Evento evento, String TerminalDestion);
}
