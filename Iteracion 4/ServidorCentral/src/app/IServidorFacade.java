package app;

import controllers.IActualizarServidor;

public interface IServidorFacade {
    void iniciar(ConfiguracionServidorDTO config);
    void setControladorActualizacion(IActualizarServidor controlador);
}
