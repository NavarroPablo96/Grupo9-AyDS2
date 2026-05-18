package interfaces;

public interface IComunicador {
    
    void conectar(String ip, int puerto, String ipSecundario, int puertoSecundario,boolean ConectarAPrimario);
    
    void setReceptor(IReceptorEvento r);

}
