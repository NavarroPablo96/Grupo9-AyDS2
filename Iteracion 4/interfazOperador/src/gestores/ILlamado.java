package gestores;

import eventos.Turno;

public interface ILlamado {
    int getIntentos();
    int getCantEspera();
    Turno getUltimoTurno();
    void llamarSiguiente();
    void renotificar();

    Turno getUltimoTurnoLlamado();
    int getCantidadEnEspera();
    int getCantidadDeVecesLlamado();
}
