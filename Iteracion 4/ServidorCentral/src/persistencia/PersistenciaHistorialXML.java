package persistencia;

import gestorFila.Historial;
import eventos.Turno;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.util.Date;

public class PersistenciaHistorialXML implements IPersistenciaHistorial {
    private String ARCHIVO = "";

    public PersistenciaHistorialXML(String archivo) {
    	this.ARCHIVO=archivo;
    }
    
    @Override
    public void guardarHistorial(Historial historial) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();

            Element root = doc.createElement("historial");
            doc.appendChild(root);

            if (historial != null) {

                Turno actual = historial.getTurnoActual();

                if (actual != null) {
                    Element act = doc.createElement("actual");
                    act.appendChild(crearTurnoXML(doc, actual));
                    root.appendChild(act);
                }

                Element lista = doc.createElement("listaHistorial");

                for (Turno t : historial.getHistorial()) {
                    lista.appendChild(crearTurnoXML(doc, t));
                }

                root.appendChild(lista);
            }

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(
                    new DOMSource(doc),
                    new StreamResult(new File(this.ARCHIVO))
            );

            System.out.println("PersistenciaHistorialXML - guardado OK");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Historial cargarHistorial() {
    	Historial historial = new Historial();

        try {
            File file = new File(this.ARCHIVO);
            if (!file.exists()) return historial;

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);


            NodeList actualNode = doc.getElementsByTagName("actual");
            if (actualNode.getLength() > 0) {
                Node tNode = ((Element) actualNode.item(0)).getElementsByTagName("turno").item(0);
                historial.setTurnoActual(parseTurno(tNode));
            }

            NodeList listaHistorial = doc.getElementsByTagName("listaHistorial");
            if (listaHistorial.getLength() > 0) {
                Element lista = (Element) listaHistorial.item(0);
                NodeList turnos = lista.getElementsByTagName("turno");
                for (int i = 0; i < turnos.getLength(); i++) {
                    Turno t = parseTurno(turnos.item(i));
                    historial.getHistorial().add(t);
                }
            }

            System.out.println("PersistenciaHistorialXML - cargado OK");
            return historial;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return historial;
    }

    // ---------------- HELPERS ----------------

    private Element crearTurnoXML(Document doc, Turno t) {

        Element turno = doc.createElement("turno");

        turno.setAttribute("numero", String.valueOf(t.getNumero()));
        turno.setAttribute("documento", t.getDocumento());
        turno.setAttribute("horaRegistro", t.getHoraRegistro());
        turno.setAttribute("terminal",String.valueOf(t.getTerminal()));
        if (t.getHoraHoraDeLlamado() != null) {
            turno.setAttribute("horaLlamado",
                    String.valueOf(t.getHoraHoraDeLlamado().getTime()));
        }

        return turno;
    }

    private Turno parseTurno(Node node) {

        Element e = (Element) node;

        int numero = Integer.parseInt(e.getAttribute("numero"));
        String documento = e.getAttribute("documento");
        String horaRegistro = e.getAttribute("horaRegistro");

        String horaLlamadoStr = e.getAttribute("horaLlamado");
        Date horaLlamado = null;
        
        if (horaLlamadoStr != null && !horaLlamadoStr.isEmpty()) {
            horaLlamado = new Date(Long.parseLong(horaLlamadoStr));
        }
        Turno turno = new Turno(numero,documento,horaRegistro,horaLlamado);
        String terminalStr = e.getAttribute("terminal");

        if (terminalStr != null && !terminalStr.isEmpty()) {
            turno.setNumeroTerminal(Integer.parseInt(terminalStr));
        }

        return turno;
    }
}