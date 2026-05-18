package comunicacionConTerminales;

public interface IConector {
	//Comunicacion Con Clientes:
	public void iniciarServidor(String ip, int puerto);
	//Comunicacion con Server Sincronizable
	public void iniciarSincronizador(String ip, int puerto);
	
	public boolean estaLibre(String ip, int puerto);
	
	public void conectarseASincronizador(String ip, int puerto);
	public void desconectarseDeSincronizador();
	public boolean estaConectadoSincronizable();
	public void ApagarServidorSecundario();
	
}
