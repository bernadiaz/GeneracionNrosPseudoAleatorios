import java.util.ArrayList;
import java.util.List;

public class generacionAleatorios {

    private long semilla;
    private long a;
    private long c;
    private long m;
    private int nrosPrecision;
    private int iteraciones;

    public generacionAleatorios() {
        this.semilla = System.currentTimeMillis();
        // 2. Usamos constantes estándar probadas matemáticamente (Estándar POSIX/C)
        this.m = 2147483648L;     // 2^31
        this.a = 1103515245L;
        this.c = 12345L;
        this.nrosPrecision = 4;
        this.iteraciones=5000;
    }

    public List<Double> congruencialMixto() {
        System.out.println("--- Generador Congruencial Mixto ---");

        // --- LÓGICA DEL ALGORITMO ---
        List<Double> resultados = new ArrayList<>();
        long n = semilla;

        for (int i = 0; i < iteraciones; i++) {
            // Fórmula: (a * Xn + c) mod m
            n = (a * n + c) % m;

            // Normalización con redondeo dinámico
            double valorGenerado = redondear((double) n / m, nrosPrecision);
            resultados.add(valorGenerado);
        }

        //scanner.close();
        return resultados;
    }

    private static double redondear(double valor, int decimales) {
        double factor = Math.pow(10, decimales);
        return Math.round(valor * factor) / factor;
    }
}
