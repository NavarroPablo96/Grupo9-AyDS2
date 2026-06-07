package gestorServidores;

import gestorSincronizacion.I_HeartBeat;
import gestorSincronizacion.I_Sync;

public interface IRedundanciaPasiva {

	public void notificarEstadoSincronizado();
	public void iniciarServidor(
		    String ipServidor,					int puertoServidor,
		    String ipSincronizador,	    		int puertoSincronizador, 
		    String ipClienteSecundario, 		int puertoClienteSecundario, 
		    String ipSincronizacionSecundario, 	int puertoSincronizacionSecundario,
		    String fabricaCarga,				String fabricaPersistencia,
			String tipoEncriptado, 				String clave);
	public void setSincronizado(I_Sync sincronizador);
	public void setI_HeartBeat(I_HeartBeat iHB);
	public boolean estaConectadoSincronizable();
	public void NotificarCaidaSincronizador();
	public boolean soySecundario();
	public void NotificarAusenciaHeartBeat();
}
