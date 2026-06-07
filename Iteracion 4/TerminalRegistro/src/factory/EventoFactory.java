package factory;

import java.util.Date;
import eventos.ConexionTerminal;
import eventos.Evento;
import eventos.EventoConexionExitosa;
import eventos.EventoDniExistente;
import eventos.EventoLlamarSiguiente;
import eventos.EventoSolicitudTurno;
import eventos.EventoTerminalCreada;
import eventos.EventoTurnoCreadoConExito;
import eventos.Turno;

public class EventoFactory {

    public enum TipoEvento {
        CONEXION_TERMINAL,
        CONEXION_EXITOSA,
        DNI_EXISTENTE,
        LLAMAR_SIGUIENTE,
        SOLICITUD_TURNO,
        TERMINAL_CREADA,
        TURNO_CREADO_EXITO
    }

    public static Evento crearEvento(TipoEvento tipo, String origen, String destino, Object... args) {
        switch(tipo) {
            case CONEXION_TERMINAL:
                return new ConexionTerminal(origen, destino, (String) args[0]);
            case CONEXION_EXITOSA:
                return new EventoConexionExitosa(origen, destino, (Integer) args[0]);
            case DNI_EXISTENTE:
                return new EventoDniExistente(origen, destino, (String) args[0]);
            case LLAMAR_SIGUIENTE:
                return new EventoLlamarSiguiente(origen, destino);
            case SOLICITUD_TURNO:
                return new EventoSolicitudTurno(origen, destino, (String) args[0], (String) args[1], (Date) args[2]);
            case TERMINAL_CREADA:
                return new EventoTerminalCreada(origen, destino, (Integer) args[0]);
            case TURNO_CREADO_EXITO:
                return new EventoTurnoCreadoConExito(origen, destino, (Turno) args[0]);
            default:
                throw new IllegalArgumentException("Tipo de evento desconocido: " + tipo);
        }
    }
}
