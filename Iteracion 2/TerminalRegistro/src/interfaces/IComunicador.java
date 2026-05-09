package interfaces;

import java.io.IOException;
import java.net.UnknownHostException;

import eventos.Evento;

public interface IComunicador {
    
    void conectar(String ip, int puerto) throws UnknownHostException, IOException;

    void enviarEvento(Evento evento);
    
    void setReceptor(IReceptorEvento r);

}
