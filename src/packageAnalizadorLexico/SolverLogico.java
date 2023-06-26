package packageAnalizadorLexico;

public class SolverLogico {
    private final Nodo nodo;
    private boolean hayErrores = false;
    public SolverLogico(Nodo nodo) {
        this.nodo = nodo;
    }
    public Object resolver(TablaSimbolos tabla){
        return resolver(nodo,tabla);
    }
    private Object resolver(Nodo n,TablaSimbolos tabla){
        // No tiene hijos, es un operando
        if(n.getHijos() == null){
            if(n.getValue().tipo == TipoToken.FALSE || n.getValue().tipo == TipoToken.TRUE){
                return n.getValue().literal;
            }
            else if(n.getValue().tipo == TipoToken.ID){
                if(tabla.existeIdentificador((String) n.getValue().literal)){
                    return tabla.obtener((String)n.getValue().literal);
                }else{
                    Interprete.error(n.getValue().linea,"Variable \""+ n.getValue().literal+"\" indefinida");
                    hayErrores = true;
                }
            }
        }else if(hayErrores == false){
            Nodo izq = n.getHijos().get(0);
            Nodo der = n.getHijos().get(1);
            Object resultadoIzquierdo = null;
            Object resultadoDerecho= null;
            if (izq.getValue().esOperando()||izq.getValue().esOperador()){
                if(izq.getValue().tipo == TipoToken.CADENA || izq.getValue().tipo == TipoToken.NUMERO){
                    resultadoIzquierdo = izq.getValue().literal;
                }else{
                    resultadoIzquierdo = resolver(izq,tabla);
                }
            }
            if (der.getValue().esOperando()||der.getValue().esOperador()){
                if(der.getValue().tipo == TipoToken.CADENA || der.getValue().tipo == TipoToken.NUMERO){
                    resultadoDerecho = der.getValue().literal;
                }else{
                    resultadoDerecho = resolver(der,tabla);
                }
            }
            if (!hayErrores) {
                //System.out.println(resultadoDerecho);
                //System.out.println(resultadoIzquierdo);
                if (resultadoIzquierdo.getClass() == resultadoDerecho.getClass()) {
                    if((n.getValue().tipo!=TipoToken.AND || n.getValue().tipo!=TipoToken.OR)){
                        switch (n.getValue().tipo) {
                            case IGUAL:
                                return (resultadoIzquierdo.equals(resultadoDerecho));
                            case DIFERENTE:
                                return (!resultadoIzquierdo.equals(resultadoDerecho));
                            case MAS:
                            case MENOS:
                            case ASTERISCO:
                            case DIAGONAL:
                                SolverAritmetico solverA = new SolverAritmetico(n);
                                return solverA.resolver(tabla);
                        }
                    }
                    else if(resultadoIzquierdo instanceof Double && resultadoDerecho instanceof Double && (n.getValue().tipo!=TipoToken.AND && n.getValue().tipo!=TipoToken.OR)){

                        switch (n.getValue().tipo){
                            case MAYOR:
                                return ((double) resultadoIzquierdo > (double) resultadoDerecho);
                            case MENOR:
                                return ((double) resultadoIzquierdo < (double) resultadoDerecho);
                            case MAYORIGUAL:
                                return ((double) resultadoIzquierdo >= (double) resultadoDerecho);
                            case MENORIGUAL:
                                return ((double) resultadoIzquierdo <= (double) resultadoDerecho);
                            case MAS:
                            case MENOS:
                            case ASTERISCO:
                            case DIAGONAL:
                                SolverAritmetico solverA = new SolverAritmetico(n);
                                return solverA.resolver(tabla);
                        }
                    }
                    else if(((boolean) resultadoIzquierdo.equals(true)||(boolean) resultadoIzquierdo.equals(false))&& ((boolean) resultadoDerecho.equals(true) && (boolean) resultadoDerecho.equals(false))){
                        switch (n.getValue().tipo) {
                            case AND:
                                return (Boolean.parseBoolean((String)resultadoIzquierdo) && (Boolean.parseBoolean((String)resultadoDerecho)));
                            case OR:
                                return (Boolean.parseBoolean((String)resultadoIzquierdo) || (Boolean.parseBoolean((String)resultadoDerecho)));
                            case MAS:
                            case MENOS:
                            case ASTERISCO:
                            case DIAGONAL:
                                SolverAritmetico solverA = new SolverAritmetico(n);
                                return solverA.resolver(tabla);
                        }
                    }else{
                        if (resultadoIzquierdo != null && resultadoDerecho != null) {
                            Interprete.error(n.getValue().linea, "Valores incorrectos para operador");
                        }
                    }
                } else {
                    if (resultadoIzquierdo != null && resultadoDerecho != null) {
                        Interprete.error(n.getValue().linea, "Operacion de distintos tipos");
                    }
                }
            }
        }
        return null;
    }
}

