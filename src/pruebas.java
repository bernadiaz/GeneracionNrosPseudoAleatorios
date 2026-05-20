import java.util.List;

public class pruebas {
    public boolean correrPruebas(List<Double> datos){

        pruebasPromedio pProm = new pruebasPromedio();
        pruebasFrecuencia pFrec = new pruebasFrecuencia();
        pruebasSeries pSerie = new pruebasSeries();

        if(pProm.pruebaPromedio(datos)){
            System.out.println("Aprobo prueba de prom");
            return true;
        }
        if(pFrec.logicaPruebaFrecuencia(datos)){
            System.out.println("Aprobo prueba de frec");
            return true;
        }
        if(pSerie.logicaPruebaSeries(datos)){
            System.out.println("Aprobo prueba de serie");
            return true;
        }

        return false;
    }
}
