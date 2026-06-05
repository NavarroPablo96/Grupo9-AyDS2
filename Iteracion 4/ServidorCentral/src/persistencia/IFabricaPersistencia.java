package persistencia;

public interface IFabricaPersistencia {

    IPersistenciaCola crearPersistenciaCola();
    IPersistenciaHistorial crearPersistenciaHistorial();
    IPersistenciaLlamado crearPersistenciaLlamado();
}
