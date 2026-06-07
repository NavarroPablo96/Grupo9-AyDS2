package controller;

import comunicacion.IComunicador;
import vista.IVistaConexion;

public class ControladorConexion implements IControladorConexion{
    
    private IVistaConexion vista;
    private IComunicador modelo;
    private IControladorOperador controladorOperador;

    public ControladorConexion(IVistaConexion vista, IComunicador modelo){
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setController(this);
    }
    
    public void setControlador(IControladorOperador controladorOperador) {
    	this.controladorOperador=controladorOperador;
    }

    public void establecerConexion(){
        String ipPrimario = vista.getIp();
        int puertoPrimario = vista.getPuerto();
        String ipSecundario = vista.getIpSecundario();
        int puertoSecundario = vista.getPuertoSecundario();
        String tipoEncriptado = vista.getTipoEncriptado();
        String clave          = vista.getClave();       

        controladorOperador.setEncriptadorApi(tipoEncriptado, clave);

        modelo.conectar(ipPrimario, puertoPrimario,ipSecundario,puertoSecundario);
        estadoConectadoAServidor("Conectados a Servidor");
    }

    private void estadoConectadoAServidor(String string) {
		vista.cerrar();
		controladorOperador.abrir();
		
	}

	public void iniciar(){
    	vista.abrir();
    }


    public void finalizar(){
    	vista.cerrar();
    }
}
