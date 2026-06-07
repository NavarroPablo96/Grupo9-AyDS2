package seguridad;


public interface ISeguridadStrategy {
    String encriptar(String mensaje);
    String desencriptar(String mensajeCifrado); 

    void setClave(String clave); 
}
