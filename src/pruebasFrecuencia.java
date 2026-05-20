import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class pruebasFrecuencia {

    private int n =0;
    private int x=0;

    public ArrayList<Double> divisionIntervalos(int x){
        double tamañoIntervalo = 1.0/x;

//        double s=0;
//        ArrayList<Double> vector = new ArrayList<>();
//        vector.add(s);
//        for(int i =0; i<x; i++ ){
//           vector.add(s+=tamañoIntervalo);
//        }
//        vector.add(1.0);

        ArrayList<Double> limites = new ArrayList<>();
        double acumulado = 0.0;

        limites.add(0.0);
        for (int i = 1; i <= x; i++) {
            acumulado += tamañoIntervalo;
            // Redondeamos un poco para evitar errores de precisión de punto flotante
            limites.add(Math.round(acumulado * 10000.0) / 10000.0);
        }
        return limites;
    }

    public ArrayList<Integer> contarFrecuencias(List<Double> datos, ArrayList<Double> limites, int x) {

        ArrayList<Integer> frecuencia = new ArrayList<>();
        for (int i = 0; i < x; i++) {
            frecuencia.add(0);
        }

        for (Double numero : datos) {
            // Manejo especial para el 1.0 para que no se quede afuera
            if (numero == 1.0) {
                frecuencia.set(x - 1, frecuencia.get(x - 1) + 1);
                continue;
            }

            for (int i = 0; i < x; i++) {
                // Verificamos el intervalo [limite[i], limite[i+1])
                if (numero >= limites.get(i) && numero < limites.get(i + 1)) {
                    frecuencia.set(i, frecuencia.get(i) + 1);
                    break;
                }
            }
        }
        return frecuencia;
    }

    public Double calculoEstadistico(List<Double> datos) {
        x = (int) (Math.sqrt(datos.size()));
        ArrayList<Double> limites = divisionIntervalos(x);
        ArrayList<Integer> frecuenciaReal = contarFrecuencias(datos, limites, x);

        double frecuenciaEsperada = (double) datos.size() / x;
        double sumaChi = 0.0;

        for (Integer fo : frecuenciaReal) {
            // Fórmula: Σ (fo - fe)^2 / fe
            sumaChi += Math.pow((fo - frecuenciaEsperada), 2) / frecuenciaEsperada;
        }

        System.out.println("Frecuencia Esperada (fe): " + frecuenciaEsperada);
        System.out.println("Estadístico Chi-cuadrado calculado: " + sumaChi);

        return sumaChi;
    }

    public boolean logicaPruebaFrecuencia(List<Double> datos){
        System.out.println("--- Prueba de Frecuencias (Chi-cuadrado) ---");
        Double chiCalculado = calculoEstadistico(datos);

        Double chiTabla = chiCuadradoCritico(x-1,0.05);
        boolean aceptada = false;
        if (chiCalculado < chiTabla) {
            System.out.println("Resultado: No se rechaza H0 (Los números son uniformes).");
            aceptada=true;
        } else {
            System.out.println("Resultado: Se rechaza H0 (Los números NO son uniformes).");
        }

        return aceptada;
    }

    public static double chiCuadradoCritico(int gl, double alpha) {
        double z = cuantilNormal(1.0 - alpha);          // cuantil normal superior
        double h = 2.0 / (9.0 * gl);                   // factor de corrección
        double val = gl * Math.pow(1.0 - h + z * Math.sqrt(h), 3);
        return Math.max(val, 0.0);
    }
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
}
