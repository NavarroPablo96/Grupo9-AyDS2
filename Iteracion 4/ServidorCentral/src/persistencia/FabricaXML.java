package persistencia;

public class FabricaXML implements IFabricaPersistencia{

private String nombreCola,nombreHistorial,nombreLlamado; // Nombres de los archivos "cola.txt" 
	
	public FabricaXML(boolean primario){
		if(primario) {
			this.nombreCola="colaPrimario.xml";
			this.nombreHistorial="historialPrimario.xml";
			this.nombreLlamado="llamadosPrimario.xml";
		}
		else {
			this.nombreCola="colaSecundario.xml";
			this.nombreHistorial="historialSecundario.xml";
			this.nombreLlamado="llamadosSecundario.xml";
		}
	}
	@Override
	public IPersistenciaCola crearPersistenciaCola() {
		return new PersistenciaColaXML(this.nombreCola);
	}


	@Override
	public IPersistenciaHistorial crearPersistenciaHistorial() {
		return new PersistenciaHistorialXML(this.nombreHistorial);
	}


	@Override
	public IPersistenciaLlamado crearPersistenciaLlamado() {
		return new PersistenciaLlamadoXML(this.nombreLlamado);
	}

}
