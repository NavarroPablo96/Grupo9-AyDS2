package factory;

import seguridad.Cesar;
import seguridad.DES;
import seguridad.ISeguridadStrategy;
import seguridad.XOR;

public class SeguridadFactory {
    

    public static ISeguridadStrategy crearEncriptador(String tipo){
        if (tipo.equals("DES")){
            ISeguridadStrategy x = new DES();
            return x;
        }
        else if (tipo.equals("XOR")){
            ISeguridadStrategy x = new XOR();
            return x;            
        }

        ISeguridadStrategy x = new Cesar();
        return x;
    }
}
