import java.util.List;

public class pruebas {
    public boolean correrPruebas(List<Double> datos){

        pruebasPromedio pProm = new pruebasPromedio();
        pruebasFrecuencia pFrec = new pruebasFrecuencia();
        pruebasSeries pSerie = new pruebasSeries();
        pruebasKS pKS = new pruebasKS();
        pruebasCorrida pCorrida = new pruebasCorrida();

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
        if(pKS.logicaPruebaKS(datos)){
            System.out.println("Aprobo prueba KS");
            return true;
        }
        if(pCorrida.logicaPruebaCorrida(datos)){
            System.out.println("Aprobo prueba corrida");
            return true;
        }


        return false;
    }
}
