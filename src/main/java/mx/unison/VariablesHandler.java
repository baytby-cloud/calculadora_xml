
package mx.unison;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class VariablesHandler extends DefaultHandler {

    private final String variableBuscada;
    private final List<Double> valores = new ArrayList<>();
    private boolean leyendoVariable = false;
    private final StringBuilder contenido = new StringBuilder();

    public VariablesHandler(String variableBuscada) {
        this.variableBuscada = variableBuscada;
    }

    @Override
    public void startElement(String uri, String localName,
                             String qName, Attributes attributes)
            throws SAXException {

        String nombre = localName.isEmpty() ? qName : localName;

        if (nombre.equals(variableBuscada)) {
            leyendoVariable = true;
            contenido.setLength(0);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {

        if (leyendoVariable) {
            contenido.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName,
                           String qName) throws SAXException {

        String nombre = localName.isEmpty() ? qName : localName;

        if (leyendoVariable && nombre.equals(variableBuscada)) {
            String texto = contenido.toString().trim();

            try {
                valores.add(Double.parseDouble(texto));
            } catch (NumberFormatException e) {
                // Ignorar valores vacíos o no numéricos.
            }

            leyendoVariable = false;
        }
    }

    public List<Double> obtenerValores() {
        return new ArrayList<>(valores);
    }
}
