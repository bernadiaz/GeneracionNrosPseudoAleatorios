import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        // 1. Generación de números aleatorios
        generacionAleatorios g = new generacionAleatorios();
        List<Double> resultados = g.congruencialMixto();
        System.out.println("\nNúmeros generados: " + resultados);

        // 2. Prueba del Promedio
        //pruebasPromedio pp = new pruebasPromedio();
        //pp.pruebaPromedio(resultados);

        // 3. Prueba de Frecuencias (Chi-cuadrado uniforme 1D)
        pruebasFrecuencia pf = new pruebasFrecuencia();
        pf.logicaPruebaFrecuencia(resultados, scanner);

        // 4. Prueba de la Serie (Chi-cuadrado en grilla k x k)
        //pruebasSeries ps = new pruebasSeries();
        //ps.logicaPruebaSeries(resultados);

        scanner.close();
    }
}