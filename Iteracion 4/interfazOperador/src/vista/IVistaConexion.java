package vista;

import controller.IControladorConexion;

public interface IVistaConexion {
    void abrir();
    void cerrar();
    String getIp();
    int getPuerto();
    void setController(IControladorConexion c);
	String getIpSecundario();
	int getPuertoSecundario();
}
