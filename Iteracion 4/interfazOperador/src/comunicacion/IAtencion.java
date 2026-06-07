package comunicacion;

import eventos.Turno;
import seguridad.ISeguridadStrategy;

public interface IAtencion {
    
    void llamarSiguiente(int nroTerminal);

    void renotificar(int nroTerminal, Turno ultimo);

    void setEncriptador(ISeguridadStrategy crypt);

}
