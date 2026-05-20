import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class pruebasSeries {

    // =========================================================================
    //  CÁLCULO DEL VALOR CRÍTICO CHI-CUADRADO (sin tablas externas)
    // =========================================================================

    /**
     * Aproximación del cuantil de la distribución normal estándar inversa.
     * Algoritmo de Abramowitz & Stegun (26.2.17) – error máximo ≈ 4.5e-4.
     *
     * @param p probabilidad acumulada (0 < p < 1)
     * @return z tal que P(Z ≤ z) = p
     */
    private static double cuantilNormal(double p) {
        double[] c = {2.515517, 0.802853, 0.010328};
        double[] d = {1.432788, 0.189269, 0.001308};

        double q = Math.min(p, 1.0 - p);
        double t = Math.sqrt(-2.0 * Math.log(q));
        double num = c[0] + t * (c[1] + t * c[2]);
        double den = 1.0 + t * (d[0] + t * (d[1] + t * d[2]));
        double z = t - num / den;

        return (p < 0.5) ? -z : z;        // reflejo si p < 0.5
    }

    /**
     * Cuantil de la distribución Chi-cuadrado mediante la aproximación
     * de Wilson-Hilferty (1931).
     * <p>
     * χ²(α, gl) ≈ gl · (1 − 2/(9·gl) + z_{1-α} · √(2/(9·gl)))³
     * <p>
     * Precisión más que suficiente para aplicaciones estadísticas con gl ≥ 2.
     *
     * @param gl    grados de libertad (entero positivo)
     * @param alpha nivel de significancia (p.ej. 0.05)
     * @return valor crítico χ²_{1-α, gl}
     */
    public static double chiCuadradoCritico(int gl, double alpha) {
        double z = cuantilNormal(1.0 - alpha);          // cuantil normal superior
        double h = 2.0 / (9.0 * gl);                   // factor de corrección
        double val = gl * Math.pow(1.0 - h + z * Math.sqrt(h), 3);
        return Math.max(val, 0.0);
    }

    // =========================================================================
    //  LÓGICA PRINCIPAL DE LA PRUEBA
    // =========================================================================

    /**
     * Divide [0,1) en k sub-intervalos iguales y devuelve los límites.
     */
    private ArrayList<Double> divisionIntervalos(int k) {
        double tam = 1.0 / k;
        ArrayList<Double> limites = new ArrayList<>();
        double acum = 0.0;
        limites.add(0.0);
        for (int i = 1; i <= k; i++) {
            acum += tam;
            limites.add(Math.round(acum * 10000.0) / 10000.0);
        }
        return limites;
    }

    /**
     * Forma pares NO solapados (R1,R2), (R3,R4), ...
     * Si la cantidad de números es impar, el último se ignora.
     */
    private ArrayList<int[]> formarPares(List<Double> datos) {
        ArrayList<int[]> pares = new ArrayList<>();
        for (int i = 0; i + 1 < datos.size(); i += 2) {
            pares.add(new int[]{i, i + 1});
        }
        return pares;
    }

    /**
     * Devuelve el índice de celda (0-based) en que cae un valor dentro de los
     * k sub-intervalos definidos por 'limites'. Manejo especial para 1.0.
     */
    private int obtenerIndiceCelda(double valor, ArrayList<Double> limites, int k) {
        if (valor == 1.0) return k - 1;
        for (int i = 0; i < k; i++) {
            if (valor >= limites.get(i) && valor < limites.get(i + 1)) {
                return i;
            }
        }
        return k - 1;
    }

    /**
     * Cuenta cuántos pares caen en cada celda de la grilla k×k.
     * Celda (fila, col) → índice = fila * k + col.
     */
    public ArrayList<Integer> contarFrecuenciasCeldas(List<Double> datos,
                                                      ArrayList<Double> limites,
                                                      int k) {
        int totalCeldas = k * k;
        ArrayList<Integer> frecuencia = new ArrayList<>();
        for (int i = 0; i < totalCeldas; i++) frecuencia.add(0);

        for (int[] par : formarPares(datos)) {
            int fila = obtenerIndiceCelda(datos.get(par[0]), limites, k);
            int col = obtenerIndiceCelda(datos.get(par[1]), limites, k);
            int idx = fila * k + col;
            frecuencia.set(idx, frecuencia.get(idx) + 1);
        }
        return frecuencia;
    }

    /**
     * Calcula el estadístico Chi-cuadrado y devuelve tanto su valor como los
     * grados de libertad (empaquetados en un double[]: [chiCalc, gl, fe]).
     */
    public double[] calculoEstadistico(List<Double> datos) {

        int k = (int) (Math.sqrt(((datos.size()) / 2.0) / 5.0));

        ArrayList<Double> limites = divisionIntervalos(k);
        ArrayList<Integer> frecReal = contarFrecuenciasCeldas(datos, limites, k);

        int nPares = datos.size() / 2;
        int totalCeldas = k * k;
        int gl = totalCeldas - 1;          // grados de libertad = k²-1
        double fe = (double) nPares / totalCeldas;

        System.out.println("\nNúmeros utilizados  : " + datos.size()
                + (datos.size() % 2 != 0
                ? " (el último fue ignorado por ser impar)" : ""));
        System.out.println("Pares formados      : " + nPares);
        System.out.println("Celdas de la grilla : " + k + " × " + k
                + " = " + totalCeldas);
        System.out.printf("Frecuencia esperada : %.4f%n", fe);

        // ---- Tabla de frecuencias observadas --------------------------------
        System.out.println("\n--- Tabla de frecuencias observadas (grilla "
                + k + "×" + k + ") ---");
        System.out.printf("%-8s %-22s %-22s %s%n",
                "Celda", "Ri ∈", "Ri+1 ∈", "Fo");

        double sumaChi = 0.0;
        for (int fila = 0; fila < k; fila++) {
            for (int col = 0; col < k; col++) {
                int idx = fila * k + col;
                int fo = frecReal.get(idx);
                sumaChi += Math.pow(fo - fe, 2) / fe;

                String rangoFila = String.format("[%.4f, %.4f)",
                        limites.get(fila), limites.get(fila + 1));
                String rangoCol = String.format("[%.4f, %.4f)",
                        limites.get(col), limites.get(col + 1));

                System.out.printf("%-8d %-22s %-22s %d%n",
                        idx + 1, rangoFila, rangoCol, fo);
            }
        }

        System.out.printf("%nEstadístico χ² calculado : %.6f%n", sumaChi);
        System.out.println("Grados de libertad (gl)   : k² − 1 = "
                + totalCeldas + " − 1 = " + gl);

        return new double[]{sumaChi, gl, fe};
    }

    /**
     * Punto de entrada principal para la prueba de la serie.
     * El valor crítico χ²tabla se calcula automáticamente a partir del nivel
     * de significancia α y los grados de libertad gl = k² − 1.
     */
    public boolean logicaPruebaSeries(List<Double> datos) {
        System.out.println("\n========================================");
        System.out.println("   PRUEBA DE LA SERIE (Chi-cuadrado)   ");
        System.out.println("========================================");

        // ---- 1. Cálculo del estadístico ─────────────────────────────────────
        double[] resultado = calculoEstadistico(datos);
        double chiCalculado = resultado[0];
        int gl = (int) resultado[1];

        double alpha = 0.05;

        // ---- 3. Valor crítico calculado automáticamente ─────────────────────
        double chiTabla = chiCuadradoCritico(gl, alpha);

        System.out.println("\n--- Análisis de los Grados de Libertad ---");
        System.out.println("  gl        = k² − 1 = " + gl);
        System.out.printf("  α         = %.4f%n", alpha);
        System.out.printf("  χ²tabla   = χ²(1−α=%.4f, gl=%d) = %.6f%n",
                1.0 - alpha, gl, chiTabla);
        System.out.printf("  χ²calc    = %.6f%n", chiCalculado);
        System.out.println("  (valor crítico obtenido por aproximación"
                + " de Wilson-Hilferty)");

        // ---- 4. Decisión ────────────────────────────────────────────────────
        System.out.println("\n--- Decisión ---");
        boolean aceptada = false;
        if (chiCalculado < chiTabla) {
            System.out.printf("  χ²calc (%.6f) < χ²tabla (%.6f)  →  "
                    + "NO se rechaza H₀%n", chiCalculado, chiTabla);
            System.out.println("  Conclusión: Los pares son independientes / "
                    + "uniformes en [0,1)².");
            aceptada = true;
        } else {
            System.out.printf("  χ²calc (%.6f) ≥ χ²tabla (%.6f)  →  "
                    + "Se RECHAZA H₀%n", chiCalculado, chiTabla);
            System.out.println("  Conclusión: Los pares NO son independientes / "
                    + "uniformes en [0,1)².");
        }
        return aceptada;
    }
}