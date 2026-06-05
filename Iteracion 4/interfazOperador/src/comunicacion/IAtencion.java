package comunicacion;

import eventos.Turno;

public interface IAtencion {
    
    void llamarSiguiente(int nroTerminal);

    void renotificar(int nroTerminal, Turno ultimo);

}
