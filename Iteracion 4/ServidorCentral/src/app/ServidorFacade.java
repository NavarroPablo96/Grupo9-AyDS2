package app;

import gestorEventos.GestorEventos;
import gestorEventos.IReceptorEvento;
import gestorFila.ColaTurno;
import gestorFila.GestorFila;
import gestorFila.IColaTurno;
import gestorFila.IEstadoFila;
import gestorServidores.Comunicador;
import gestorServidores.GestorServidores;
import gestorServidores.IConector;
import gestorServidores.IEnviarEventoServidores;
import gestorServidores.IRedundanciaPasiva;
import gestorSincronizacion.GestorDeSincronizacion;
import gestorSincronizacion.I_HeartBeat;
import gestorSincronizacion.I_Sync;
import gestorTerminales.GestorTerminales;
import gestorTerminales.IEnviarEventoClientes;
import gestorTerminales.IGestorTerminal;
import persistencia.GestorPersistencia;
import controllers.IActualizarServidor;

public class ServidorFacade implements IServidorFacade {
    private final IReceptorEvento receptor;
    private final IEnviarEventoClientes enviador;
    private final IGestorTerminal gestorTerminales;
    private final IColaTurno ICT;
    private final GestorPersistencia gestorPersistencia;
    private final IConector comunicador;
    private final IEnviarEventoServidores enviarAOtroServidor;
    private final IEstadoFila gestorFila;
    private final IRedundanciaPasiva IRP;
    private final I_Sync sincronizador;
    private final I_HeartBeat IHB;

    public ServidorFacade() {
        this.receptor = GestorEventos.getInstance();
        GestorEventos.getInstance().setIRegistro(GestorFila.getInstance());
        GestorEventos.getInstance().setIAtencion(GestorFila.getInstance());
        
        this.enviador = GestorTerminales.getInstance();
        this.gestorTerminales = GestorTerminales.getInstance();
        Comunicador.getInstance().setGestorTerminal(gestorTerminales);
        GestorFila.getInstance().setIEnviar(enviador);
        
        this.ICT = new ColaTurno();
        GestorFila.getInstance().setCola(ICT);
        
        Comunicador.getInstance().setReceptor(receptor);
        
        this.gestorPersistencia = new GestorPersistencia();
        GestorFila.getInstance().setGestorPersistencia(gestorPersistencia);
        
        this.comunicador = Comunicador.getInstance();
        this.enviarAOtroServidor = Comunicador.getInstance();
        this.gestorFila = GestorFila.getInstance();
        
        this.IRP = new GestorServidores(comunicador, gestorPersistencia);
        Comunicador.getInstance().setGestorServidores(IRP);
        
        this.sincronizador = new GestorDeSincronizacion(gestorFila, enviarAOtroServidor, IRP);
        GestorEventos.getInstance().setI_Sync(sincronizador);
        IRP.setSincronizado(sincronizador);
        
        this.IHB = (I_HeartBeat) sincronizador;
        GestorEventos.getInstance().setI_HeartBeat(IHB);
        IRP.setI_HeartBeat(IHB);
    }

    @Override
    public void iniciar(ConfiguracionServidorDTO config) {
        this.IRP.iniciarServidor(
            config.getIpServidor(),
            config.getPuertoServidor(),
            config.getIpSincronizador(),
            config.getPuertoSincronizador(),
            config.getIpClienteSecundario(),
            config.getPuertoClienteSecundario(),
            config.getIpSincronizacionSecundario(),
            config.getPuertoSincronizacionSecundario(),
            config.getFabricaCarga(),
            config.getFabricaPersistencia(),
            config.getTipoEncriptado(),
            config.getClave()
        );
    }

    @Override
    public void setControladorActualizacion(IActualizarServidor controlador) {
        Comunicador.getInstance().setControlador(controlador);
        GestorFila.getInstance().setControlador(controlador);
        GestorTerminales.getInstance().setControlador(controlador);
    }
}
