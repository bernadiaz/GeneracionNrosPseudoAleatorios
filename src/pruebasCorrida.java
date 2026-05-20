import java.util.ArrayList;
import java.util.List;

public class pruebasCorrida {

    //  CÁLCULO DEL VALOR CRÍTICO CHI-CUADRADO (Reutilizado)
    private static double cuantilNormal(double p) {
        double[] c = {2.515517, 0.802853, 0.010328};
        double[] d = {1.432788, 0.189269, 0.001308};

        double q = Math.min(p, 1.0 - p);
        double t = Math.sqrt(-2.0 * Math.log(q));
        double num = c[0] + t * (c[1] + t * c[2]);
        double den = 1.0 + t * (d[0] + t * (d[1] + t * d[2]));
        double z = t - num / den;

        return (p < 0.5) ? -z : z;
    }

    public static double chiCuadradoCritico(int gl, double alpha) {
        if (gl <= 0) return 0.0;
        double z = cuantilNormal(1.0 - alpha);
        double h = 2.0 / (9.0 * gl);
        double val = gl * Math.pow(1.0 - h + z * Math.sqrt(h), 3);
        return Math.max(val, 0.0);
    }

    //  LÓGICA PRINCIPAL DE LA PRUEBA DE CORRIDA
    public Boolean logicaPruebaCorrida(List<Double> datos) {
        System.out.println("\nPRUEBA DE CORRIDA ARRIBA Y ABAJO DE LA MEDIA");

        if (datos == null || datos.isEmpty()) {
            System.out.println("No hay datos para analizar.");
            return false;
        }

        int n = datos.size();

        // 1. Crear secuencia binaria S
        // 0 si ui <= 0.5 ; 1 si ui > 0.5
        List<Integer> S = new ArrayList<>();
        for (Double ui : datos) {
            if (ui <= 0.5) {
                S.add(0);
            } else {
                S.add(1);
            }
        }

        // 2. Determinar las longitudes de corrida (Frecuencia Observada)
        int actual = S.get(0);
        int longitud = 1;
        int maxLongitud = 0;
        List<Integer> longitudesObservadas = new ArrayList<>();

        for (int i = 1; i < n; i++) {
            if (S.get(i) == actual) {
                longitud++;
            } else {
                longitudesObservadas.add(longitud);
                if (longitud > maxLongitud) {
                    maxLongitud = longitud;
                }
                actual = S.get(i);
                longitud = 1;
            }
        }
        // Se añade la última corrida que quedó contando
        longitudesObservadas.add(longitud);
        if (longitud > maxLongitud) {
            maxLongitud = longitud;
        }

        // Armamos el arreglo de Frecuencias Observadas (índice 0 = longitud 1)
        int[] Fo = new int[maxLongitud];
        for (Integer l : longitudesObservadas) {
            Fo[l - 1]++;
        }

        System.out.println("N (tamaño de muestra) : " + n);
        System.out.println("Longitud máxima (x)   : " + maxLongitud);
        System.out.println("\n--- Cálculo de Frecuencias y Chi-Cuadrado ---");
        System.out.printf("%-10s %-10s %-15s %-15s\n", "Long. (i)", "Fo", "Fe", "((Fo-Fe)²)/Fe");

        // 3. Calcular Frecuencia Esperada (Fe) y Estadístico Chi-Cuadrado
        double chiCalculado = 0.0;

        for (int i = 1; i <= maxLongitud; i++) {
            int fo_i = Fo[i - 1];
            // Fórmula: Fe_i = (n - i + 3) / 2^(i+1)
            double fe_i = (n - i + 3.0) / Math.pow(2, i + 1);

            double contribucion = Math.pow(fo_i - fe_i, 2) / fe_i;
            chiCalculado += contribucion;

            System.out.printf("%-10d %-10d %-15.4f %-15.4f\n", i, fo_i, fe_i, contribucion);
        }

        // 4. Obtener Valor Crítico de la tabla
        // Grados de libertad = número de clases (x) - 1
        int gl = maxLongitud - 1;
        if (gl < 1) gl = 1; // Protección por si todos los números cayeran del mismo lado

        double alpha = 0.05;
        double chiTabla = chiCuadradoCritico(gl, alpha);

        System.out.printf("\nEstadístico χ² calculado : %.6f%n", chiCalculado);
        System.out.println("Grados de libertad (gl)   : x - 1 = " + gl);
        System.out.printf("χ²tabla (α=%.2f, gl=%d) : %.6f%n", alpha, gl, chiTabla);

        // 5. Conclusión y Toma de Decisión
        System.out.println("\n--- Decisión ---");
        if (chiCalculado < chiTabla) {
            System.out.println("Resultado: χ²calc < χ²tabla -> NO se rechaza la hipótesis nula.");
            System.out.println("Conclusión: Los números provienen de un universo uniformemente distribuido.");
            return true;
        } else {
            System.out.println("Resultado: χ²calc >= χ²tabla -> Se RECHAZA la hipótesis nula.");
            System.out.println("Conclusión: Los números NO provienen de un universo uniformemente distribuido.");
            return false;
        }
    }
}