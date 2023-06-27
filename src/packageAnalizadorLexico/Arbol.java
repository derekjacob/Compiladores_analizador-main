package packageAnalizadorLexico;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Arbol {
    private final Nodo raiz;
    public TablaSimbolos tabla = new TablaSimbolos();
    public Arbol(Nodo raiz){
        this.raiz = raiz;
    }
    public Object res = null;
    public boolean varParaId = false;
    public boolean asignacionEnVar = false;

    public void recorrer(){
        for(Nodo n: raiz.getHijos()){
            /*System.out.println("FOR---------------------------");
            System.out.println(raiz.getHijos());
*/
            recorridoPostOrden(n);

            if((asignacionEnVar && varParaId)){
                //System.out.println("No hace nada");
                asignacionEnVar = false;
                varParaId = false;
            }else{
                if(tabla.existeIdentificador((String) raiz.getHijos().get(0).getHijos().get(0).getValue().literal)){
                    if(res == null){
                        int a = raiz.getHijos().indexOf(n);
                        if(raiz.getHijos().get(a).getHijos().get(1).getValue().tipo == TipoToken.ID){
                            tabla.actualizar((String) raiz.getHijos().get(a).getHijos().get(0).getValue().literal,tabla.obtener((String) raiz.getHijos().get(a).getHijos().get(1).getValue().literal));
                        }else{
                            tabla.actualizar((String) raiz.getHijos().get(a).getHijos().get(0).getValue().literal,raiz.getHijos().get(a).getHijos().get(1).getValue().literal);
                        }
                    }else {
                        int a = raiz.getHijos().indexOf(n);
                        tabla.actualizar((String) raiz.getHijos().get(a).getHijos().get(0).getValue().literal, res);
                        res = null;
                    }
                }
            }

        }
    }
    public void recorridoPostOrden(Nodo n){
        Token t = n.getValue();
        Token tDer = null;
        Token tIzq = null;
        if(n.getValue() == null){
            return;
        }
        if(n.getHijos()!=null){
            if (n.getHijos().size() == 2) {
                tDer = n.getHijos().get(1).getValue();
            }
            tIzq = n.getHijos().get(0).getValue();
        }
        if(n.getHijos() != null && n.getValue().tipo!=TipoToken.FOR) {
            tDer = null;
            if (n.getHijos().size() == 2 && n.getValue().tipo!=TipoToken.IF && n.getValue().tipo!=TipoToken.WHILE) {
                tDer = n.getHijos().get(1).getValue();
                tIzq = n.getHijos().get(0).getValue();

                recorridoPostOrden(n.getHijos().get(0));
                recorridoPostOrden(n.getHijos().get(1));
            } else {
                tIzq = n.getHijos().get(0).getValue();

                recorridoPostOrden(n.getHijos().get(0));
            }
        }
        switchRecorrido(n);
    }
    private void switchRecorrido(Nodo n){
        Token t = n.getValue();
        //System.out.println(t.tipo);
        switch (t.tipo){
            // Cambio de valor
            case ID:
                asignacionEnVar = true;
                break;
            // Operadores aritméticos
            case MAS:
            case MENOS:
            case ASTERISCO:
            case DIAGONAL:
                SolverAritmetico solverA = new SolverAritmetico(n);
                res = solverA.resolver(tabla);
                break;
            //Operadores logicos
            case IGUAL:
            case DIFERENTE:
            case MAYOR:
            case MENOR:
            case MAYORIGUAL:
            case MENORIGUAL:
            case AND:
            case OR:
                SolverLogico solverL = new SolverLogico(n);
                res = solverL.resolver(tabla);
                break;
            // Print
            case PRINT:
                varParaId = true;
                Object auxP = null;
                if(n.getHijos().get(0).getValue().esOperando()){
                    if(n.getHijos().get(0).getValue().tipo == TipoToken.ID){
                        auxP = tabla.obtener((String)n.getHijos().get(0).getValue().literal);
                        System.out.println(auxP);
                    }else{
                        System.out.println(n.getHijos().get(0).getValue().literal);
                    }
                }else if(n.getHijos().get(0).getValue().esOperador()){
                    System.out.println(res);
                    res = null;
                }
                break;
            // Variable
            case VAR:
                varParaId = true;
                if(n.getHijos()!=null) {
                    if (n.getHijos().size() == 1) {
                        n = n.getHijos().get(0);
                        if (n.getHijos().get(1).getValue().esOperando()) {
                            if (tabla.existeIdentificador((String) n.getHijos().get(0).getValue().literal)) {
                                Interprete.error(n.getValue().linea, "La variable \"" + n.getHijos().get(0).getValue().literal + "\" ya existe.");
                            } else {
                                tabla.asignar((String) n.getHijos().get(0).getValue().literal, n.getHijos().get(1).getValue().literal);
                                /*System.out.println("VARIABLE GUARDADA");
                                System.out.println("ID: " + (String) n.getHijos().get(0).getValue().literal + " Valor: " + n.getHijos().get(1).getValue().literal);
                                tabla.imprimir();*/
                            }

                        } else {
                            if (res == null) {
                                break;
                            } else {
                                if (tabla.existeIdentificador((String) n.getHijos().get(0).getValue().literal)) {
                                    Interprete.error(n.getValue().linea, "La variable \"" + n.getHijos().get(0).getValue().literal + "\" ya existe.");
                                } else {
                                    tabla.asignar((String) n.getHijos().get(0).getValue().literal, res);
                                    /*System.out.println("VARIABLE GUARDADA");
                                    System.out.println("ID: " + (String) n.getHijos().get(0).getValue().literal + " Valor: " + res);
                                    res = null;*/
                                }
                            }
                        }
                    }
                }
                break;
            case IF:
                Nodo aux = n;
                boolean seCumple = false;
                if (res!=null && !(res instanceof Double)){
                    seCumple = (boolean) res;
                    res = null;
                }
                if((res instanceof Double)){
                    Interprete.error(n.getValue().linea,"Codicion de estructura erronea");
                }
                int hijoSig = 1;
                if(seCumple == true){
                    while(n.getHijos().size() > hijoSig){
                        aux = n.getHijos().get(hijoSig);
                        if(aux.getValue().tipo != TipoToken.ELSE){
                            recorridoPostOrden(aux);
                            hijoSig++;
                        }
                        hijoSig++;
                    }
                }else{
                    while(n.getHijos().size() > hijoSig){
                        aux = n.getHijos().get(hijoSig);
                        hijoSig++;
                    }
                    if(aux.getValue().tipo == TipoToken.ELSE){
                        recorridoPostOrden(aux);
                    }
                }
                break;
            case WHILE:
                Nodo auxW = n;
                Nodo estatico = n;
                boolean seCumpleW = false;
                if (res!=null) seCumpleW = (boolean) res;
                res = null;
                int hijoSigW = 1;
                if(seCumpleW == true){
                    while(seCumpleW){
                        while(auxW.getHijos().size() > hijoSigW){
                            auxW = auxW.getHijos().get(hijoSigW);
                            recorridoPostOrden(auxW);
                            hijoSigW++;
                        }
                        auxW = estatico;
                        hijoSigW = 1;
                    }
                }
                break;
            case FOR:

                break;
        }
    }
}


