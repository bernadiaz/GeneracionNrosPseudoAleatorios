import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class pruebasKS {

    // Cambiamos 'void' por 'boolean'
    public boolean logicaPruebaKS(List<Double> datos) {
        System.out.println("\n========================================");
        System.out.println("   PRUEBA DE KOLMOGOROV-SMIRNOV (K-S)   ");
        System.out.println("========================================");

        int n = datos.size();

        // 1. Clonar y ordenar
        List<Double> datosOrdenados = new ArrayList<>(datos);
        Collections.sort(datosOrdenados);

        double dMax = 0.0;

        // 2. Calcular Dn = MAX |Fn(xi) - ui|
        for (int i = 0; i < n; i++) {
            double ui = datosOrdenados.get(i);
            double fn = (double) (i + 1) / n;
            double diferencia = Math.abs(fn - ui);

            if (diferencia > dMax) {
                dMax = diferencia;
            }
        }

        System.out.println("Cantidad de datos analizados (n) : " + n);
        System.out.printf("Estadístico calculado (D_max)    : %.6f%n", dMax);

        // 3. Valor crítico
        double alpha = 0.05;
        double dCritico = 1.36 / Math.sqrt(n);

        System.out.printf("Nivel de significancia (alpha)   : %.2f%n", alpha);
        System.out.printf("Valor crítico tabla (1.36/√n)    : %.6f%n", dCritico);

        // 4. Decisión con RETORNOS BOOLEANOS
        System.out.println("\n--- Decisión ---");
        if (dMax < dCritico) {
            System.out.printf("  D_max (%.6f) < D_critico (%.6f) -> NO se rechaza H0%n", dMax, dCritico);
            System.out.println("  Resultado: Los números siguen una distribución Uniforme en (0,1).");
            return true;  // <--- Retorna TRUE si pasa la prueba
        } else {
            System.out.printf("  D_max (%.6f) >= D_critico (%.6f) -> Se RECHAZA H0%n", dMax, dCritico);
            System.out.println("  Resultado: Los números NO siguen una distribución Uniforme en (0,1).");
            return false; // <--- Retorna FALSE si falla la prueba
        }
    }
}