package persistencia;

import java.io.File;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import eventos.Turno;
import gestorFila.ColaTurno;
import gestorFila.EstadoCola;

public class PersistenciaColaXML implements IPersistenciaCola {

	@Override
	public void guardarCola(EstadoCola estado) {
	    try {
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.newDocument();
	        Element raiz = doc.createElement("cola");
	        doc.appendChild(raiz);
	        // METADATOS
	        Element metadatos = doc.createElement("metadatos");
	        raiz.appendChild(metadatos);
	        Element cantidadPone = doc.createElement("cantidadPone");
	        cantidadPone.setTextContent(String.valueOf(estado.getCantidadPone()));
	        metadatos.appendChild(cantidadPone);
	        
	        Element cantidadSaca = doc.createElement("cantidadSaca");
	        cantidadSaca.setTextContent(String.valueOf(estado.getCantidadSaca()));
	        metadatos.appendChild(cantidadSaca);
	        Element numeroTurnoSiguiente = doc.createElement("numeroTurnoSiguiente");
	        numeroTurnoSiguiente.setTextContent(String.valueOf(estado.getNumeroTurnoSiguiente()));
	        metadatos.appendChild(numeroTurnoSiguiente);

	        // TURNOS
	        Element turnos = doc.createElement("turnos");
	        raiz.appendChild(turnos);
	        ColaTurno cola = estado.getCola();
	        for (Turno t : cola.getListaTurnos()) {
	            Element turno = doc.createElement("turno");
	            Element numero = doc.createElement("numero");
	            numero.setTextContent(String.valueOf(t.getNumero()));
	            turno.appendChild(numero);

	            Element documento = doc.createElement("documento");
	            documento.setTextContent(t.getDocumento());
	            turno.appendChild(documento);

	            Element horaRegistro = doc.createElement("horaRegistro");
	            horaRegistro.setTextContent( t.getHoraRegistro());
	            turno.appendChild(horaRegistro);
	            Element horaLlamado = doc.createElement("horaLlamado");

	            if (t.getHoraHoraDeLlamado() != null) {
	                horaLlamado.setTextContent(
	                        String.valueOf(
	                                t.getHoraHoraDeLlamado().getTime()));
	            }
	            turno.appendChild(horaLlamado);
	            turnos.appendChild(turno);
	        }
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty( OutputKeys.INDENT, "yes");
	        transformer.transform(new DOMSource(doc), new StreamResult(new File("cola.xml")));
	        System.out.println( "PersistenciaColaXML - Cola guardada");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@Override
	public EstadoCola cargarCola() {
	    File archivo = new File("cola.xml");
	    EstadoCola estado = new EstadoCola();
	    if (!archivo.exists()) {
	        return estado;
	    }
	    try {
	        DocumentBuilderFactory factory =DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder =factory.newDocumentBuilder();
	        Document doc = builder.parse(archivo);
	        doc.getDocumentElement().normalize();
	        
	        // METADATOS
	        Element metadatos =(Element) doc.getElementsByTagName("metadatos").item(0);
	        int cantidadPone =Integer.parseInt(metadatos.getElementsByTagName("cantidadPone").item(0).getTextContent());
	        int cantidadSaca =Integer.parseInt(metadatos.getElementsByTagName("cantidadSaca").item(0).getTextContent());
	        int numeroTurnoSiguiente =Integer.parseInt(metadatos.getElementsByTagName("numeroTurnoSiguiente").item(0).getTextContent());
	        
	        ColaTurno cola = new ColaTurno();
	        NodeList listaTurnos =doc.getElementsByTagName("turno");
	        for (int i = 0; i < listaTurnos.getLength(); i++) {
	            Element turnoXML =(Element) listaTurnos.item(i);
	            
	            int numero =Integer.parseInt(turnoXML.getElementsByTagName("numero").item(0).getTextContent());
	            String documento =turnoXML.getElementsByTagName("documento").item(0).getTextContent();
	            String horaRegistro =turnoXML.getElementsByTagName("horaRegistro").item(0).getTextContent();
	            Date horaLlamado = null;
	            String textoHoraLlamado =turnoXML.getElementsByTagName("horaLlamado").item(0).getTextContent();
	            if (!textoHoraLlamado.isEmpty()) {
	                horaLlamado =new Date(Long.parseLong(textoHoraLlamado));
	            }
	            
	            Turno turno = new Turno(numero,documento,horaRegistro,horaLlamado);
	            cola.pone(turno);
	        }
	        estado.setCola(cola,numeroTurnoSiguiente,cantidadPone,cantidadSaca);
	        System.out.println("PersistenciaColaXML - Cola cargada");
	        return estado;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return estado;
	}

}
