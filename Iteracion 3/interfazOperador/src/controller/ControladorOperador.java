package controller;

import gestores.ILlamado;
import vista.IVistaOperador;

public class ControladorOperador implements IControladorOperador{

    private IVistaOperador vista;
    private ILlamado gestorLlamado;

    public ControladorOperador(IVistaOperador vista, ILlamado gestorLlamado){
        this.vista = vista;
        this.gestorLlamado = gestorLlamado;
        vista.setControlador(this);
        abrirVistaOperador();
    }

    //IControladorOperador se apreta el boton llamar siguiente
    @Override
    public void llamarCliente(){
        gestorLlamado.llamarSiguiente();
        vista.estadoLlamando();
    }

    //IControladorOperador
    @Override
    public void rellamarCliente() {
        gestorLlamado.renotificar();
    }


    @Override
    public void estadoFilaVacia() {
    	actualizarVistaOperador();
    }
    
    
    @Override
    public void estadoFilaNoVacia() {
    	System.out.println("ControladorOperador.estadoFilaNoVacia()");
    	vista.estadoFilaNoVacia();
    	actualizarVistaOperador();
	}

	public void ActualizarVistaNumero(int numeroTerminal) {
		vista.ActualizarVistaNumero(numeroTerminal);
	}

	public void estadoConectadoAServidor(String txt) {
        //vista conexion
        // c.setVisible(false);
    	// abrirVistaOperador();
	}

    private void abrirVistaOperador() {
        vista.actualizar(
        		gestorLlamado.getUltimoTurnoLlamado(),
                gestorLlamado.getCantidadEnEspera(),
                gestorLlamado.getCantidadDeVecesLlamado()
        );
    }
    
    public void actualizarVistaOperador() {
        if(gestorLlamado.getUltimoTurnoLlamado()==null) {
        	vista.ActivarBotonNotificar(false);
        }
        else {
        	vista.ActivarBotonNotificar(true);
        }
        System.out.println("ACTUALZIANDO Vista OPERADOR - "+gestorLlamado.getCantidadEnEspera());
        vista.actualizar(
                gestorLlamado.getUltimoTurnoLlamado(),
                gestorLlamado.getCantidadEnEspera(),
                gestorLlamado.getCantidadDeVecesLlamado()
        );    	
    }

    public void seDebeLlamarSiguiente(String string) {
    	vista.CartelSeDebeLlamarSiguiente();

	}

    public void CartelFilaVacia() {
    	vista.CartelFilaVacia();
    }

	@Override
	public void abrir() {
		this.vista.abrir();
	}

	@Override
	public void cerrar() {
		this.vista.cerrar();
		
	}


}
