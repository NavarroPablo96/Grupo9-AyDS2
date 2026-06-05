package persistencia;

import gestorFila.EstadoCola;
import gestorFila.Historial;
import gestorFila.RegistroRellamar;

public class GestorPersistencia {

    private IPersistenciaCola persistenciaGuardadoCola;
    private IPersistenciaHistorial persistenciaGuardadoHistorial;
    private IPersistenciaLlamado persistenciaGuardadoLlamado;
    private IPersistenciaCola persistenciaCargadoCola;
    private IPersistenciaHistorial persistenciaCargadoHistorial;
    private IPersistenciaLlamado persistenciaCargadoLlamado;
    
    

    public GestorPersistencia() {
    	
    }
    public void setPersistenciaGuardado(IFabricaPersistencia fabrica) {
    	this.persistenciaGuardadoCola = fabrica.crearPersistenciaCola();
    	this.persistenciaGuardadoHistorial= fabrica.crearPersistenciaHistorial();
    	this.persistenciaGuardadoLlamado = fabrica.crearPersistenciaLlamado();
    }
    
    public void setPersistenciaCargado(IFabricaPersistencia fabrica) {
    	this.persistenciaCargadoCola = fabrica.crearPersistenciaCola();
    	this.persistenciaCargadoHistorial= fabrica.crearPersistenciaHistorial();
    	this.persistenciaCargadoLlamado = fabrica.crearPersistenciaLlamado();
    }
    
    public void guardarCola(EstadoCola cola) {
    	this.persistenciaGuardadoCola.guardarCola(cola);
    }
    
    public EstadoCola cargarCola() {
    	return this.persistenciaCargadoCola.cargarCola();
    }
    
    public void guardarHistorial(Historial historial) {
    	this.persistenciaGuardadoHistorial.guardarHistorial(historial);
    }
    
    public Historial cargarHistorial() {
    	return this.persistenciaCargadoHistorial.cargarHistorial();
    }
    
    public void guardarLlamados(RegistroRellamar llamados) {
    	this.persistenciaGuardadoLlamado.guardarLlamados(llamados);
    }
    public RegistroRellamar cargarLlamados() {
    	return this.persistenciaCargadoLlamado.cargarLlamados();
    }
}