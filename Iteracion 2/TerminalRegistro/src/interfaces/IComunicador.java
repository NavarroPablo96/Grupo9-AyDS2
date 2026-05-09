package interfaces;

import java.io.IOException;
import java.net.UnknownHostException;

public interface IComunicador {
    
    void conectar(String ip, int puerto) throws UnknownHostException, IOException;
    
    void setReceptor(IReceptorEvento r);

}
