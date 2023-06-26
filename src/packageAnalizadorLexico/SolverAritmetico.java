package packageAnalizadorLexico;

public class SolverAritmetico {
    private final Nodo nodo;
    public SolverAritmetico(Nodo nodo) {
        this.nodo = nodo;
    }
    public Object resolver(TablaSimbolos tabla){
        return resolver(nodo,tabla);
    }
    private Object resolver(Nodo n,TablaSimbolos tabla){
        // No tiene hijos, es un operando
        if(n.getHijos() == null){
            if(n.getValue().tipo == TipoToken.NUMERO || n.getValue().tipo == TipoToken.CADENA){
                return n.getValue().literal;
            }
            else if(n.getValue().tipo == TipoToken.ID){
                if(tabla.existeIdentificador((String) n.getValue().literal)){
                    return tabla.obtener((String)n.getValue().literal);
                }else{
                    Interprete.error(n.getValue().linea,"Variable \""+ n.getValue().literal+"\" indefinida");
                }
            }
        }else{
            Nodo izq = n.getHijos().get(0);
            Nodo der = n.getHijos().get(1);
            Object resultadoIzquierdo = resolver(izq,tabla);
            Object resultadoDerecho = resolver(der,tabla);
            if (resultadoIzquierdo instanceof Double && resultadoDerecho instanceof Double) {
                switch (n.getValue().tipo) {
                    case MAS:
                        return ((Double) resultadoIzquierdo + (Double) resultadoDerecho);
                    case MENOS:
                        return ((Double) resultadoIzquierdo - (Double) resultadoDerecho);
                    case ASTERISCO:
                        return ((Double) resultadoIzquierdo * (Double) resultadoDerecho);
                    case DIAGONAL:
                        return ((Double) resultadoIzquierdo / (Double) resultadoDerecho);

                }
            }else if (resultadoIzquierdo instanceof String && resultadoDerecho instanceof String){
                if (n.getValue().tipo == TipoToken.MAS){
                    return (String) ((String) resultadoIzquierdo).concat((String)resultadoDerecho);
                }
            }else{
                if(resultadoIzquierdo != null && resultadoDerecho != null){
                    Interprete.error(n.getValue().linea,"Operacion de distintos tipos");
                }else{
                    Interprete.error(n.getValue().linea,"Operacion de distintos tipos");
                }
            }
        }
        return null;
    }
}

