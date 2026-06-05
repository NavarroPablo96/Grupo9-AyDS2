package persistencia;

public class FabricaJSON implements IFabricaPersistencia{

	public FabricaJSON() {
		
	}
	@Override
	public IPersistenciaCola crearPersistenciaCola() {
		return new PersistenciaColaJSON();
	}


	@Override
	public IPersistenciaHistorial crearPersistenciaHistorial() {
		return new PersistenciaHistorialJSON();
	}


	@Override
	public IPersistenciaLlamado crearPersistenciaLlamado() {
		return new PersistenciaLlamadoJSON();
	}
}
