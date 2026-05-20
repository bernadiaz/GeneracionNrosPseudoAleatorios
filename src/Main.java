import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Double> datos = null;
        boolean aceptada = false;

        while(!aceptada){
            generacionAleatorios g = new generacionAleatorios();
            datos = g.congruencialMixto();
            pruebas p=new pruebas();

            aceptada = p.correrPruebas(datos);
        }

        System.out.println("\nNúmeros generados: " + datos);

    }
}