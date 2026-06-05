package persistencia;

import gestorFila.Historial;

public interface IPersistenciaHistorial {

    void guardarHistorial(Historial historial);

    Historial cargarHistorial();
}
