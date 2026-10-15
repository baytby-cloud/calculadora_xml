package mx.unison;

import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.List;

public class Calculadora {

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.out.println("Uso: archivo.xml variable1 [variable2]");
            System.out.println("Ejemplo: user_behavior_dataset.xml age screen_time_hours");
            return;
        }

        File archivo = new File(args[0]);

        if (!archivo.isFile()) {
            System.out.println("Error: no se encuentra el archivo XML: " + args[0]);
            return;
        }

        try {
            List<Double> datos1 = leerVariable(archivo, args[1]);

            if (datos1.isEmpty()) {
                System.out.println("Error: la variable no existe o no contiene datos numéricos: " + args[1]);
                return;
            }

            System.out.println("Archivo XML encontrado: " + archivo.getName());
            mostrarEstadisticas(args[1], datos1);

            if (args.length == 3) {
                List<Double> datos2 = leerVariable(archivo, args[2]);

                if (datos2.isEmpty()) {
                    System.out.println("Error: la variable no existe o no contiene datos numéricos: " + args[2]);
                    return;
                }

                mostrarEstadisticas(args[2], datos2);

                if (datos1.size() != datos2.size()) {
                    System.out.println("Error: las variables tienen diferente cantidad de datos.");
                    return;
                }

                double correlacion = calcularCorrelacion(datos1, datos2);
                System.out.println("\nCoeficiente de correlación entre "
                        + args[1] + " y " + args[2] + ": " + correlacion);
            }

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

    private static void mostrarEstadisticas(String nombre, List<Double> datos) {
        double suma = 0;
        double minimo = datos.get(0);
        double maximo = datos.get(0);

        for (double valor : datos) {
            suma += valor;
            minimo = Math.min(minimo, valor);
            maximo = Math.max(maximo, valor);
        }

        double media = suma / datos.size();
        double sumaCuadrados = 0;

        for (double valor : datos) {
            sumaCuadrados += Math.pow(valor - media, 2);
        }

        double varianza = sumaCuadrados / datos.size();
        double desviacion = Math.sqrt(varianza);

        System.out.println("\nEstadísticas de la variable: " + nombre);
        System.out.println("Cantidad de datos: " + datos.size());
        System.out.println("Media: " + media);
        System.out.println("Varianza: " + varianza);
        System.out.println("Desviación estándar: " + desviacion);
        System.out.println("Valor mínimo: " + minimo);
        System.out.println("Valor máximo: " + maximo);
    }

    private static double calcularCorrelacion(
            List<Double> x, List<Double> y) {

        int n = x.size();

        double sumaX = 0;
        double sumaY = 0;

        for (int i = 0; i < n; i++) {
            sumaX += x.get(i);
            sumaY += y.get(i);
        }

        double mediaX = sumaX / n;
        double mediaY = sumaY / n;

        double numerador = 0;
        double sumaCuadradosX = 0;
        double sumaCuadradosY = 0;

        for (int i = 0; i < n; i++) {
            double diferenciaX = x.get(i) - mediaX;
            double diferenciaY = y.get(i) - mediaY;

            numerador += diferenciaX * diferenciaY;
            sumaCuadradosX += diferenciaX * diferenciaX;
            sumaCuadradosY += diferenciaY * diferenciaY;
        }

        double denominador = Math.sqrt(sumaCuadradosX * sumaCuadradosY);

        if (denominador == 0) {
            System.out.println("No se puede calcular la correlación porque una variable es constante.");
            return Double.NaN;
        }

        return numerador / denominador;
    }
}