package seguridad;

public class Cesar implements ISeguridadStrategy{
    private int desplazamiento;
    private int aux;

    public Cesar(){
    }

    @Override
    public String encriptar(String mensaje) {
        StringBuilder resultado = new StringBuilder();
        for (char caracter : mensaje.toCharArray()) {
            if (Character.isLetter(caracter)) {
                char base = Character.isUpperCase(caracter) ? 'A' : 'a';
                caracter = (char) ((caracter - base + desplazamiento) % 26 + base);
            }
            resultado.append(caracter);
        }
        return resultado.toString();
    }

    @Override
    public String desencriptar(String mensajeCifrado) {

        this.aux = this.desplazamiento;
        this.desplazamiento = 26 - (desplazamiento % 26);
        String decrypt = encriptar(mensajeCifrado);

        this.desplazamiento = aux;

        return decrypt;
    }


    //clave = numero de 0 a 99, o string "messi" por ejemplo
    public void setClave(String clave){
        if (clave.length()>=3){
           this.desplazamiento = clave.length(); 
        }
        else{
            this.desplazamiento = Integer.parseInt(clave);
        }
        
    }
}
