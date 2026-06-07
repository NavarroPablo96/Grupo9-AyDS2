package controllers;

import comunicacion.IComunicador;
import views.IVistaConexion;

public class ControladorConexion implements IControladorConexion{
    
    private IVistaConexion vista;
    private IComunicador modelo;
    private String tipoEncriptado;
    private String clave;

    public ControladorConexion(IVistaConexion vista, IComunicador modelo){
        this.vista = vista;
        this.modelo = modelo;
        this.vista.setController(this);
    }

    public void establecerConexion(){
        String ipPrimario = vista.getIp();
        int puertoPrimario = vista.getPuerto();
        String ipSecundario = vista.getIpSecundario();
        int puertoSecundario = vista.getPuertoSecundario();
        
        this.tipoEncriptado = vista.getTipoEncriptado();
        this.clave = vista.getClave();


        modelo.conectar(ipPrimario, puertoPrimario,ipSecundario,puertoSecundario);
    }

    public void iniciar(){
    	vista.abrir();
    }


    public void finalizar(){
    	vista.cerrar();
    }

    @Override
    public String getTipoEncriptado(){
        return this.tipoEncriptado;
    }

    @Override
    public String getClave(){
        return this.clave;
    }
}
