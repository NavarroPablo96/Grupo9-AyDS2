package gestionDeSincronizacion;

import eventos.EventoHeartBeat;

public interface I_HeartBeat {
	void solicitarHeartBeat();
	void iniciarEnviosHeartBeat();
	void recibirHeartbeat(EventoHeartBeat e);
}
