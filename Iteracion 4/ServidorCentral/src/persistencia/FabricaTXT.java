package persistencia;

public class FabricaTXT implements IFabricaPersistencia{
	
	public FabricaTXT(){
		
	}


	@Override
	public IPersistenciaCola crearPersistenciaCola() {
		return new PersistenciaColaTXT();
	}


	@Override
	public IPersistenciaHistorial crearPersistenciaHistorial() {
		return new PersistenciaHistorialTXT();
	}


	@Override
	public IPersistenciaLlamado crearPersistenciaLlamado() {
		return new PersistenciaLlamadoTXT();
	}

}
