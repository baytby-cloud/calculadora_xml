
package mx.unison;

import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.List;

public class Calculadora {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Debes proporcionar el nombre del archivo XML.");
            return;
        }

        if (args.length < 2 || args.length > 3) {
            System.out.println("Uso: archivo.xml variable1 [variable2]");
            return;
        }

        File archivo = new File(args[0]);

        if (!archivo.isFile()) {
            System.out.println("Error: no se encuentra el archivo XML: " + args[0]);
            return;
        }

        try {
            List<Double> datos = leerVariable(archivo, args[1]);

            if (datos.isEmpty()) {
                System.out.println("La variable no existe o no contiene datos numéricos: " + args[1]);
                return;
            }

            System.out.println("Archivo XML encontrado: " + archivo.getName());
            System.out.println("Variable leída correctamente: " + args[1]);
            System.out.println("Cantidad de datos: " + datos.size());

        } catch (Exception e) {
            System.out.println("Error al leer el XML: " + e.getMessage());
        }
    }

    private static List<Double> leerVariable(File archivo, String variable)
            throws Exception {

        VariablesHandler handler = new VariablesHandler(variable);
        SAXParserFactory.newInstance()
                .newSAXParser()
                .parse(archivo, handler);

        return handler.obtenerValores();
    }
}
