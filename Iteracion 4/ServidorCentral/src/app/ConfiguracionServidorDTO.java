package app;

public final class ConfiguracionServidorDTO {
    private final String ipServidor;
    private final int puertoServidor;
    private final String ipSincronizador;
    private final int puertoSincronizador;
    private final String ipClienteSecundario;
    private final int puertoClienteSecundario;
    private final String ipSincronizacionSecundario;
    private final int puertoSincronizacionSecundario;
    private final String fabricaCarga;
    private final String fabricaPersistencia;
    private final String tipoEncriptado;
    private final String clave;

    public ConfiguracionServidorDTO(String ipServidor, int puertoServidor, String ipSincronizador,
                                    int puertoSincronizador, String ipClienteSecundario, int puertoClienteSecundario,
                                    String ipSincronizacionSecundario, int puertoSincronizacionSecundario,
                                    String fabricaCarga, String fabricaPersistencia, String tipoEncriptado,
                                    String clave) {
        this.ipServidor = ipServidor;
        this.puertoServidor = puertoServidor;
        this.ipSincronizador = ipSincronizador;
        this.puertoSincronizador = puertoSincronizador;
        this.ipClienteSecundario = ipClienteSecundario;
        this.puertoClienteSecundario = puertoClienteSecundario;
        this.ipSincronizacionSecundario = ipSincronizacionSecundario;
        this.puertoSincronizacionSecundario = puertoSincronizacionSecundario;
        this.fabricaCarga = fabricaCarga;
        this.fabricaPersistencia = fabricaPersistencia;
        this.tipoEncriptado = tipoEncriptado;
        this.clave = clave;
    }

    public String getIpServidor() {
        return ipServidor;
    }

    public int getPuertoServidor() {
        return puertoServidor;
    }

    public String getIpSincronizador() {
        return ipSincronizador;
    }

    public int getPuertoSincronizador() {
        return puertoSincronizador;
    }

    public String getIpClienteSecundario() {
        return ipClienteSecundario;
    }

    public int getPuertoClienteSecundario() {
        return puertoClienteSecundario;
    }

    public String getIpSincronizacionSecundario() {
        return ipSincronizacionSecundario;
    }

    public int getPuertoSincronizacionSecundario() {
        return puertoSincronizacionSecundario;
    }

    public String getFabricaCarga() {
        return fabricaCarga;
    }

    public String getFabricaPersistencia() {
        return fabricaPersistencia;
    }

    public String getTipoEncriptado() {
        return tipoEncriptado;
    }

    public String getClave() {
        return clave;
    }
}
