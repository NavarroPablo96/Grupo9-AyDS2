package controllers;

import eventos.Turno;
import factory.SeguridadFactory;
import model.GestorRegistro;
import seguridad.ISeguridadStrategy;
import views.IVistaRegistro;

public class ControladorRegistro implements IControladorRegistro{
    
    private IVistaRegistro vista;
    
    private GestorRegistro modelo;

    public ControladorRegistro(IVistaRegistro vista, GestorRegistro modelo){
        this.vista = vista;
        this.modelo = modelo;

        vista.setController(this);

    }


	public void iniciar(int numeroTerminal){
		vista.abrir();
    	modelo.setNumeroTerminal(numeroTerminal);
    	vista.ActualizarTitulo(modelo.getNumero());

	}


    public void registrarTurno(){
        String dni = vista.getDni();
        if (!esDocumentoValido(dni)) {
        	vista.MensajeErrorDocumentoInvalido();
            return;
        }
        modelo.registrarTurno(dni);
        vista.borrarDni();

    }
    
    private boolean esDocumentoValido(String documento) {
        return documento != null && documento.matches("\\d{7,8}");
    }


	@Override
	public void errorDniExistente(String dni) {
    	this.vista.errorDniExistente(dni);
	}


	@Override
	public void turnoCreado(Turno nuevo) {
    	this.vista.turnoCreado(nuevo);
	}
	

    @Override
    public void setEncriptadorApi(String tipo, String clave){
        ISeguridadStrategy x = SeguridadFactory.crearEncriptador(tipo);

        x.setClave(clave);

        modelo.comunicador.setEncriptador(x);

    }
	
}
