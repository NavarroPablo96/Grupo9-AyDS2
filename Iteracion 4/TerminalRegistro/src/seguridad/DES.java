package seguridad;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DES implements ISeguridadStrategy{

    private String clave;
    private String ALGORITMO="DES";


    public DES(){
    }


    @Override
    public String encriptar(String mensaje){
        // DES requiere clave de 8 bytes. Ajustamos la clave para el ejemplo.
        SecretKeySpec secretKey = new SecretKeySpec(ajustarClave8Bytes(clave), ALGORITMO);
        try{
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        
            byte[] bytesEncriptados = cipher.doFinal(mensaje.getBytes());
            
            return Base64.getEncoder().encodeToString(bytesEncriptados);
        }
        catch(Exception e){}
        return "";
    }

    @Override
    public String desencriptar(String mensajeCifrado){
        SecretKeySpec secretKey = new SecretKeySpec(ajustarClave8Bytes(clave), ALGORITMO);

        try{
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            
            byte[] bytesDecodificados = Base64.getDecoder().decode(mensajeCifrado);
            byte[] bytesDesencriptados = cipher.doFinal(bytesDecodificados);

            return new String(bytesDesencriptados);
        }
        catch(Exception e){}
        return "";
    }

    private byte[] ajustarClave8Bytes(String clave){
        byte[] claveBytes = new byte[8];
        byte[] originalBytes = clave.getBytes();
        System.arraycopy(originalBytes, 0, claveBytes, 0, Math.min(originalBytes.length, 8));
        return claveBytes;
    }    

    public void setClave(String clave){
        this.clave = clave;
    }
}
