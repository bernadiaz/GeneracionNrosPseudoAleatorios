import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class pruebasPromedio {

    private Double Za;

    public static Double promedioAritmetico(List<Double> datos){
        ArrayList<Double> datosArray = new ArrayList<>();
        datosArray.addAll(datos);

        double suma=0;

        if (datos == null || datos.isEmpty()) return 0.0;

        for(Double dato : datosArray){
            suma+=dato;
        }

        return suma/datos.size();
    }

    public Boolean pruebaPromedio(List<Double> datos){
        double za = 1.96;
        double varianzaR = Math.sqrt(1.0 / 12.0);

        double z0 = (promedioAritmetico(datos)-0.5)*Math.sqrt(datos.size())/(Math.sqrt(1.0/12.0));
        //Si ponemos el otro podemos tener un error. Error de truncamiento a 0
        // double z0 = (promedioAritmetico(datos)-0.5)*Math.sqrt(datos.size())/(Math.sqrt(1/12));

        System.out.println("Estadístico calculado (Z0): " + z0);

        boolean aceptada = Math.abs(z0) <= za;

        if (aceptada) {
            System.out.println("Resultado: No se rechaza H0 (Los números son uniformes).");
        } else {
            System.out.println("Resultado: Se rechaza H0 (Los números NO son uniformes).");
        }

        return aceptada;
    }

}
