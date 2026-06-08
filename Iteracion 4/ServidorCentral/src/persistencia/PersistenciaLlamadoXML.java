package persistencia;

import gestorFila.Llamado;
import gestorFila.RegistroRellamar;
import eventos.Turno;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.util.Date;

public class PersistenciaLlamadoXML implements IPersistenciaLlamado {
    private String ARCHIVO = "";

    public PersistenciaLlamadoXML(String archivo) {
    	this.ARCHIVO=archivo;
    }
    
    
    @Override
    public void guardarLlamados(RegistroRellamar llamados) {
        File archivo = new File(this.ARCHIVO);

        if (archivo.exists()) {
            archivo.delete();
        }
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("llamados");
            doc.appendChild(root);

            if (llamados != null) {

                for (Llamado l : llamados.getLlamados()) {

                    Turno t = l.getTurno();

                    Element llamado = doc.createElement("llamado");

                    llamado.setAttribute("numero", String.valueOf(t.getNumero()));
                    llamado.setAttribute("documento", t.getDocumento());
                    llamado.setAttribute("horaRegistro", t.getHoraRegistro());

                    llamado.setAttribute("cantidadVeces",
                            String.valueOf(l.getCantidadVecesLlamado()));

                    llamado.setAttribute("terminal",
                            String.valueOf(l.getNumeroTerminal()));

                    if (t.getHoraHoraDeLlamado() != null) {
                        llamado.setAttribute("horaLlamado",
                                String.valueOf(t.getHoraHoraDeLlamado().getTime()));
                    }

                    root.appendChild(llamado);
                }
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(
                    new DOMSource(doc),
                    new StreamResult(new File(this.ARCHIVO))
            );

            System.out.println("PersistenciaLlamadoXML - guardado OK");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public RegistroRellamar cargarLlamados() {

    	RegistroRellamar registro = new RegistroRellamar();
        try {
            File file = new File(this.ARCHIVO);
            if (!file.exists()) return registro;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);


            NodeList lista = doc.getElementsByTagName("llamado");

            for (int i = 0; i < lista.getLength(); i++) {

                Element e = (Element) lista.item(i);

                int numero = Integer.parseInt(e.getAttribute("numero"));
                String documento = e.getAttribute("documento");
                String horaRegistro = e.getAttribute("horaRegistro");

                int cantidadVeces =
                        Integer.parseInt(e.getAttribute("cantidadVeces"));

                int terminal =
                        Integer.parseInt(e.getAttribute("terminal"));

                String horaLlamadoStr = e.getAttribute("horaLlamado");
                Date horaLlamado = null;

                if (horaLlamadoStr != null && !horaLlamadoStr.isEmpty()) {
                    horaLlamado = new Date(Long.parseLong(horaLlamadoStr));
                }

                Turno turno = new Turno(numero, documento, horaRegistro, horaLlamado);

                Llamado llamado = new Llamado(turno, cantidadVeces, terminal);

                registro.getLlamados().add(llamado);
            }

            System.out.println("PersistenciaLlamadoXML - cargado OK");
            return registro;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return registro;
    }
}