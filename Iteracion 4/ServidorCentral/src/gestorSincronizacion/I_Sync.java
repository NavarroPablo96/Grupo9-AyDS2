package gestorSincronizacion;

import eventos.Evento;
import gestorFila.Historial;
import gestorFila.IColaTurno;
import gestorFila.RegistroRellamar;

public interface I_Sync {
	void solicitarSincronizacion();
	void enviarEstadoSistema();
	void recibirEstadoSistema(IColaTurno iColaTurno, int i, int j, int k,Historial h, RegistroRellamar l);
	void recibirActualizacion(Evento e);		//Esto es nuevo
}
