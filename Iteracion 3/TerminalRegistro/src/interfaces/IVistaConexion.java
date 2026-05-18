package interfaces;

public interface IVistaConexion {
    void abrir();
    void cerrar();
    String getIp();
    int getPuerto();
    void setController(IControladorConexion c);
	String getIpSecundario();
	int getPuertoSecundario();
}
