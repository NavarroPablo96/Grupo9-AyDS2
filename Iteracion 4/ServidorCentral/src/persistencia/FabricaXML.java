package persistencia;

public class FabricaXML implements IFabricaPersistencia{

	public FabricaXML() {
		
	}
	@Override
	public IPersistenciaCola crearPersistenciaCola() {
		return new PersistenciaColaXML();
	}


	@Override
	public IPersistenciaHistorial crearPersistenciaHistorial() {
		return new PersistenciaHistorialXML();
	}


	@Override
	public IPersistenciaLlamado crearPersistenciaLlamado() {
		return new PersistenciaLlamadoXML();
	}

}
