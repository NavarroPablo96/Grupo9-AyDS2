package persistencia;

public class FabricaJSON implements IFabricaPersistencia{

	
	private String nombreCola,nombreHistorial,nombreLlamado; // Nombres de los archivos "cola.txt" 
	
	public FabricaJSON(boolean primario){
		if(primario) {
			this.nombreCola="colaPrimario.json";
			this.nombreHistorial="historialPrimario.json";
			this.nombreLlamado="llamadosPrimario.json";
		}
		else {
			this.nombreCola="colaSecundario.json";
			this.nombreHistorial="historialSecundario.json";
			this.nombreLlamado="llamadosSecundario.json";
		}
	}

	@Override
	public IPersistenciaCola crearPersistenciaCola() {
		return new PersistenciaColaJSON(this.nombreCola);
	}


	@Override
	public IPersistenciaHistorial crearPersistenciaHistorial() {
		return new PersistenciaHistorialJSON(this.nombreHistorial);
	}


	@Override
	public IPersistenciaLlamado crearPersistenciaLlamado() {
		return new PersistenciaLlamadoJSON(this.nombreLlamado);
	}
}
