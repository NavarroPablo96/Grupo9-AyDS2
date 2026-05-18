package comunicacionConTerminales;

import eventos.Evento;

public interface IEnviarEventoServidor {
	//Se conecta A un Servidor para solicitar
	//
	void enviarEventoASincronizador(Evento e);
	
	void enviarEventoASincrionizable(Evento e);
}
