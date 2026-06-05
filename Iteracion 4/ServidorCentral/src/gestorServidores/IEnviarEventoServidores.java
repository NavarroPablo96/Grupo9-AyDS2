package gestorServidores;

import eventos.Evento;

public interface IEnviarEventoServidores {
	//Se conecta A un Servidor para solicitar
	//
	void enviarEventoASincronizador(Evento e);
	
	void enviarEventoASincrionizable(Evento e);
}
