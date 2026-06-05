package persistencia;

import gestorFila.EstadoCola;

public interface IPersistenciaCola {
    void guardarCola(EstadoCola cola);

    EstadoCola cargarCola();
}



/*package persistencia;


public interface IPersistencia {

    void guardarEstado(EstadoSistema estado);

    EstadoSistema cargarEstado();

}
*/
/*public interface IPersistencia {

    void guardarCola(IColaTurno cola);

    void guardarHistorial(List<Turno> historial);

    void guardarLlamados(List<Llamado> llamados);

    IColaTurno cargarCola();

    List<Turno> cargarHistorial();

    List<Llamado> cargarLlamados();
    
    
}*/