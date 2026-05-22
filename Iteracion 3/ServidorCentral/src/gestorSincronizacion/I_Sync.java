package gestorSincronizacion;

import gestorFila.IColaTurno;

public interface I_Sync {
	void solicitarSincronizacion();
	void enviarEstadoCola();
	void recibirEstadoCola(IColaTurno iColaTurno, int i, int j, int k);
}
