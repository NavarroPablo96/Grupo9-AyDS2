package gestorServidores;

import gestorFila.GestorFila;
import gestorSincronizacion.I_HeartBeat;
import gestorSincronizacion.I_Sync;
import persistencia.FabricaJSON;
import persistencia.FabricaTXT;
import persistencia.FabricaXML;
import persistencia.GestorPersistencia;
import seguridad.Cesar;
import seguridad.DES;
import seguridad.ISeguridadStrategy;
import seguridad.XOR;

public class GestorServidores implements IRedundanciaPasiva{
	private IConector conector;
	private I_Sync sincronizador;
	private I_HeartBeat heart;
	
	//INTERFAZ GESTOR PERSISTENCIA:
	private GestorPersistencia gestorPersistencia;
	
	private String 	ipServidor,
					ipSincronizador,
					ipClienteSecundario,
					ipSincronizacionSecundario;
	private int 	puertoServidor,
					puertoSincronizador,
					puertoClienteSecundario, 
					puertoSincronizacionSecundario;
	
	private boolean soyPrimario;
	
	
	public GestorServidores(IConector comunicador,GestorPersistencia gestorPersistencia) {
		this.gestorPersistencia=gestorPersistencia;
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
		String ipSincronizacionSecundario,	int puertoSincronizacionSecundario,
		String fabricaRecuperar,			String fabricaPersistir,
		String tipoEncriptado, 				String clave)
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
		
		configuracionPersistencia(fabricaRecuperar,fabricaPersistir);
		configuracionEncriptado(tipoEncriptado, clave);
		
		System.out.println("En Gestor Servidores");
		if(hayServidorPrimario()) {
			if(hayServidorSecundario()==true) {
				System.out.println("HOla GestorServidores65");
			}
			else {
				System.out.println("El ip-puerto principal esta ocupado");
				this.soyPrimario=false;
				SoySecundario();
			}
		}
		else {
			System.out.println("El ip-puerto principal esta libre, entonces soy primario");
			this.soyPrimario=true;
			SoyPrimario();
		}
	}

	private void configuracionEncriptado(String tipo, String clave){
    	if ("XOR".equals(tipo)) {
			ISeguridadStrategy x = new XOR();
			x.setClave(clave);
    	    GestorFila.getInstance().setEncriptador(x);
    	}
    	else if ("DES".equals(tipo)) {
			ISeguridadStrategy x = new DES();
			x.setClave(clave);
    	    GestorFila.getInstance().setEncriptador(x);
    	}
    	else if ("Cesar".equals(tipo)) {
			ISeguridadStrategy x = new Cesar();
			x.setClave(clave);
    	    GestorFila.getInstance().setEncriptador(x);
    	}	
	}
	
	private void configuracionPersistencia(String fabricaRecuperar, String fabricaPersistir) {
		// ====================
    	// RECUPERAR
    	// ====================

    	if ("TXT".equals(fabricaRecuperar)) {
    	    gestorPersistencia.setPersistenciaCargado(new FabricaTXT());
    	}
    	else if ("JSON".equals(fabricaRecuperar)) {
    	    gestorPersistencia.setPersistenciaCargado(new FabricaJSON());
    	}
    	else if ("XML".equals(fabricaRecuperar)) {
    	    gestorPersistencia.setPersistenciaCargado(new FabricaXML());
    	}

    	// ====================
    	// GUARDAR
    	// ====================

    	if ("TXT".equals(fabricaPersistir)) {
    	    gestorPersistencia.setPersistenciaGuardado(new FabricaTXT());
    	}
    	else if ("JSON".equals(fabricaPersistir)) {
    	    gestorPersistencia.setPersistenciaGuardado(new FabricaJSON());
    	}
    	else if ("XML".equals(fabricaPersistir)) {
    	    gestorPersistencia.setPersistenciaGuardado(new FabricaXML());
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
			solicitarSincronizacion();
		}
		else {//Objetivo no requiere sincronizarse porque 
			//no hay Secundario. Solo empezar a funcionar normal.
	    	GestorFila.getInstance().cargarEstado();
			abrirServidores();
		}
	}

	private void SoySecundario() {
		System.out.println("Soy secundario");
		//Suponemos que está funcionando el primario atendiendo en 1234
		//Y suponemos que está funcionando el sincronizador en 2234
		//Soy el servidor secundario me conecto al sincronizador del primario.
		this.conector.conectarseASincronizador(ipSincronizador, puertoSincronizador);
		if(this.conector.estoyConectadoASincronizador()) {
			solicitarSincronizacion();			
		}
		else {
			
		}
		
	}
	
	private void solicitarSincronizacion() {
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
			//Esto se ejecuta cada vez que se recibe una sincronización, para avisarnos que estamos sincronizados.
			//Cuando solicitamos la primer sincronización puede que desiemos solicitar HeartBeat o iniciar el servidorDeClientes.
			//Pero si ya estamos haciendo algo de eso entonces no queremos hacer nada.
			this.estoyFuncionando=true;
			solicitarHeartBeat();
			
			/*if(this.soyPrimario) {
				this.conector.desconectarseDeSincronizador();
				funcionamientoPrimarioNormal();
			}
			else {
				funcionamientoSecundarioNormal();
			}*/
		}
		
	}
	
	private void abrirServidores() {
		//Se supone que nuestro servidor ya hizo la correspondiente Sincronización.
		//Suponemos que no hay servidor Secundario.
		this.conector.iniciarSincronizador(ipSincronizador,puertoSincronizador);
		
		this.conector.iniciarServidor(ipServidor, puertoServidor);
		System.out.println("ServidorPrimario-Iniciado");
	}

	private void solicitarHeartBeat() {
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
		//Se cayo el primario	//Esto notifica el comunicador
		System.out.println("GestorServidores - Se cayo el servidor que sincroniza");
	}

	@Override
	public void NotificarAusenciaHeartBeat() {
		//Se cayo el primario
		if(soyPrimario) {
			System.out.println("Se cayo el secundario, abrimos Servidor primario");
			this.conector.iniciarServidor(ipServidor, puertoServidor);
			this.conector.iniciarSincronizador(ipSincronizador, puertoSincronizador);			
		}
		else {
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
	
}
