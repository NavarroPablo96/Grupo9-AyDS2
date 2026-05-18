package views;

import controllers.IControladorConexion;

public interface IVistaConexion {
	public void mostrar();
	public void cerrar();
	public void desactivarBoton(String escuchandoEn);
	public void setController(IControladorConexion c);
	//----------
	public String getIP_Cp();
	public String getIP_Sp();
	public String getIP_Cs();
	public String getIP_Ss();
	public int getPuerto_Cp();
	public int getPuerto_Sp();
	public int getPuerto_Cs();
	public int getPuerto_Ss();
	
	
	
}
