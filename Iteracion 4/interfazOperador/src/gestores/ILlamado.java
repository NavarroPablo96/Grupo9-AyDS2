package gestores;

import eventos.Turno;
import seguridad.ISeguridadStrategy;

public interface ILlamado {
    int getIntentos();
    int getCantEspera();
    Turno getUltimoTurno();
    void llamarSiguiente();
    void renotificar();

    Turno getUltimoTurnoLlamado();
    int getCantidadEnEspera();
    int getCantidadDeVecesLlamado();

    void setEncriptadorApi(ISeguridadStrategy crypt);
}
