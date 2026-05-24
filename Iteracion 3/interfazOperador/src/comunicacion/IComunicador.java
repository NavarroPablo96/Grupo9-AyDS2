package comunicacion;

import eventos.Evento;
import interfaces.IReceptorEvento;

public interface IComunicador {
    
    void conectar(String ip, int puerto, String ipSecundario, int puertoSecundario);
    
    void setReceptor(IReceptorEvento r);

    void enviarEvento(Evento evento);

}
