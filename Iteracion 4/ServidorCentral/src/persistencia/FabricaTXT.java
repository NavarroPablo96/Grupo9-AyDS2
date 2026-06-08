package persistencia;

public class FabricaTXT implements IFabricaPersistencia{
	private String nombreCola,nombreHistorial,nombreLlamado; // Nombres de los archivos "cola.txt" 
	
	public FabricaTXT(boolean primario){
		if(primario) {
			this.nombreCola="colaPrimario.txt";
			this.nombreHistorial="historialPrimario.txt";
			this.nombreLlamado="llamadosPrimario.txt";
		}
		else {
			this.nombreCola="colaSecundario.txt";
			this.nombreHistorial="historialSecundario.txt";
			this.nombreLlamado="llamadosSecundario.txt";
		}
	}


	@Override
	public IPersistenciaCola crearPersistenciaCola() {
		return new PersistenciaColaTXT(this.nombreCola);
	}


	@Override
	public IPersistenciaHistorial crearPersistenciaHistorial() {
		return new PersistenciaHistorialTXT(this.nombreHistorial);
	}


	@Override
	public IPersistenciaLlamado crearPersistenciaLlamado() {
		return new PersistenciaLlamadoTXT(this.nombreLlamado);
	}

}
