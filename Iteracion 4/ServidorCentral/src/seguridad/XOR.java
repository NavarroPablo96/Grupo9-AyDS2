package seguridad;

import java.util.Base64;

public class XOR implements ISeguridadStrategy{

    private String clave;

    public XOR(){
    }

    @Override
    public String encriptar(String mensaje) {
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < mensaje.length(); i++) {
            resultado.append((char) (mensaje.charAt(i) ^ clave.charAt(i % clave.length())));
        }
        // Usamos Base64 para evitar caracteres no imprimibles tras el XOR
        return Base64.getEncoder().encodeToString(resultado.toString().getBytes());
    }

    @Override
    public String desencriptar(String mensajeCifrado) {
        byte[] bytesDecodificados = Base64.getDecoder().decode(mensajeCifrado);
        String mensaje = new String(bytesDecodificados);
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < mensaje.length(); i++) {
            resultado.append((char) (mensaje.charAt(i) ^ clave.charAt(i % clave.length())));
        }
        return resultado.toString();
    }


    public void setClave(String clave){
        this.clave = clave;
    }

}
