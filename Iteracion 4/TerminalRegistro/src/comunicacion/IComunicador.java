package comunicacion;

import gestorEventos.IReceptorEvento;

public interface IComunicador {
    
    void conectar(String ip, int puerto, String ipSecundario, int puertoSecundario);
    
    void setReceptor(IReceptorEvento r);
}
