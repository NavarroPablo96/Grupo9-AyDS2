package redundanciaPasiva;

import comunicacionConTerminales.IConector;
import gestionDeSincronizacion.I_HeartBeat;
import gestionDeSincronizacion.I_Sync;

public class GestorServidores implements IRedundanciaPasiva{
	private IConector conector;
	private I_Sync sincronizador;
	private I_HeartBeat heart;
	private String 	ipServidor,
					ipSincronizador,
					ipClienteSecundario,
					ipSincronizacionSecundario;
	private int 	puertoServidor,
					puertoSincronizador,
					puertoClienteSecundario, 
					puertoSincronizacionSecundario;
	
	private boolean soyPrimario;
	
	
	public GestorServidores(IConector comunicador) {
		this.conector= comunicador;
	}
	
	@Override
	public void setSincronizado(I_Sync sincronizador) {
		this.sincronizador=sincronizador;
		
	}
	
	@Override
	public void setI_HeartBeat(I_HeartBeat iHB) {
		this.heart=iHB;
	}

	@Override
	public void iniciarServidor(
		String ipServidor, 					int puertoServidor, 
		String ipSincronizador, 			int puertoSincronizador,
		String ipClienteSecundario, 		int puertoClienteSecundario, 
		String ipSincronizacionSecundario,	int puertoSincronizacionSecundario)
	{
		this.ipServidor=ipServidor;
		this.ipSincronizador=ipSincronizador;
		this.ipClienteSecundario=ipClienteSecundario;
		this.ipSincronizacionSecundario=ipSincronizacionSecundario;
		this.puertoServidor=puertoServidor;
		this.puertoSincronizador=puertoSincronizador;
		this.puertoClienteSecundario=puertoClienteSecundario;
		this.puertoSincronizacionSecundario=puertoSincronizacionSecundario;
		System.out.println(
			    "\n========== CONFIGURACION ==========" +
			    
			    "\nServidor Principal                 -> " + ipServidor + ":" + puertoServidor +
			    "\nSincronizador Principal       -> " + ipSincronizador + ":" + puertoSincronizador +
			    "\nServidor Secundario            -> " + ipClienteSecundario + ":" + puertoClienteSecundario +
			    "\nSincronizador Secundario  -> " + ipSincronizacionSecundario + ":" + puertoSincronizacionSecundario +
			    
			    "\n===================================\n"
			);
		System.out.println("En Gestor Servidores");
		if(hayServidorPrimario()) {
			System.out.println("El ip-puerto principal esta ocupado");
			this.soyPrimario=false;
			SoySecundario();
		}
		else {
			System.out.println("El ip-puerto principal esta libre, entonces soy primario");
			this.soyPrimario=true;
			SoyPrimario();
		}
	}
	
	private boolean hayServidorSecundario() {
		if(this.conector.estaLibre(ipClienteSecundario, puertoClienteSecundario)) {
			return false;
		}
		else {
			return true;			
		}
	}
	
	private boolean hayServidorPrimario() {
		if(this.conector.estaLibre(ipServidor, puertoServidor)) {
			return false;
		}
		else {
			return true;			
		}
	}
	
	private void SoyPrimario() {
		if(hayServidorSecundario()) {//OBjetivo : Sincronizarse y empezar a funcionar normal.
			//Supongo que el ip-puerto de sincronización Secundario está activo también ...
			this.conector.conectarseASincronizador(ipSincronizacionSecundario,puertoSincronizacionSecundario);
			Sincronizacion();
		}
		else {//Objetivo no requiere sincronizarse porque 
			//no hay Secundario. Solo empezar a funcionar normal.
			funcionamientoPrimarioNormal();
		}
	}

	private void SoySecundario() {
		System.out.println("Soy secundario");
		//Suponemos que está funcionando el primario atendiendo en 1234
		//Y suponemos que está funcionando el sincronizador en 2234
		//Soy el servidor secundario me conecto al sincronizador del primario.
		this.conector.conectarseASincronizador(ipSincronizador, puertoSincronizador);
		Sincronizacion();
	}
	
	private void Sincronizacion() {
		//IConector.ConectarseAlOtroServidor();
		//Para Enviar Y Recibir Eventos...
		//Cuando Reciba un Evento De Servidor
		//Tiene que tener su IReceptorEvento gestorEvento Listo.
		//I-Sync.SolicitarSincronizacion()
		
		//Y aca mágicamente IConector Debería Recibir un Evento 
			
		this.sincronizador.solicitarSincronizacion();
		System.out.println("Esperando sincronizacion");
/*		Primario									Secundario
 * 		enviar (EventoSolicitudSincronizacion)→		gestorEvento(recibeEvento) → I-Sync.enviarEstadoCola();
 * 								← envia(EventoSincronizacionEstado)
 * 		recibe (EventoSincronizacionEstado)
 * 		gestorEvento.recibeEvento(EventoSincronizacionEstado)
 * 		I_Sync.recibirEstadoCola(EventoSincronizacionEstado)
 * 		IEstadoFila.setCola(ColaTurno)
 * 		
 * */
	}

	private boolean estoyFuncionando=false;
	@Override
	public void notificarEstadoSincronizado() {
		
		if(this.estoyFuncionando==false) {
			this.estoyFuncionando=true;
			if(this.soyPrimario) {
				this.conector.desconectarseDeSincronizador();
				funcionamientoPrimarioNormal();
			}
			else {
				funcionamientoSecundarioNormal();
			}
		}
		
	}
	
	private void funcionamientoPrimarioNormal() {
		//Se supone que nuestro servidor ya hizo la correspondiente Sincronización.
		//Suponemos que no hay servidor Secundario.
		this.conector.iniciarSincronizador(ipSincronizador,puertoSincronizador);
		
		this.conector.iniciarServidor(ipServidor, puertoServidor);
		System.out.println("ServidorPrimario-Iniciado");
	}

	private void funcionamientoSecundarioNormal() {
		//Ya sincronizado:
		System.out.println("GestorServidores-160-Solicito heartbeat");
		this.heart.solicitarHeartBeat();
		//SECUNDARIO	→	1) Solicitar HeartBeat
		//PRIMARIO		→	2) Envio constante de HeartBeat
		//SECUNDARIO	→	3) Recibo constante de HeartBeat
			//3a) Evaluacion de cada HeartBeat Comparacion con
			//estado actual de la ICola
				//
		
		
	}

	@Override
	public boolean estaConectadoSincronizable() {
		return this.conector.estaConectadoSincronizable();
	}

	@Override
	public void NotificarCaidaSincronizador() {
		if(this.soyPrimario) {
			//no pasa nada, el secundario se desconecto
		}
		else {//Se cayo el primario
			System.out.println("Se cayo el primario, abrimos Servidor secundario");
			this.conector.iniciarServidor(ipClienteSecundario, puertoClienteSecundario);
			this.conector.iniciarSincronizador(ipSincronizacionSecundario, puertoSincronizacionSecundario);
		}
	}

	@Override
	public boolean soySecundario() {
		if(this.soyPrimario==true) {			
			return false;
		}
		else {
			return true;
		}
	}

	@Override
	public void apagarServidorSecundario() {
		this.conector.ApagarServidorSecundario();
	}


}
