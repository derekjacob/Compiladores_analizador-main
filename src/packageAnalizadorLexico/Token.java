package packageAnalizadorLexico;

public class Token
{
    final TipoToken tipo;
    final String lexema;
    final Object literal;
    final int linea;
    public boolean leido = false;

    public Token(TipoToken tipo, String lexema, Object literal, int linea){
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.linea = linea;
    }
    public Token(TipoToken tipo, String lexema, Object literal){
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
        this.linea = 0;
    }
    @Override
    public boolean equals(Object o){
        if (!(o instanceof Token)){
            return false;
        }
        if(this.tipo == ((Token)o).tipo){
            return true;
        }
        return false;
    }
    @Override
    public String toString(){
        return tipo + " " + lexema + " ";
    }
    // Metodos Auxiliares
    public boolean esOperando(){
        switch (this.tipo){
            case ID:
            case NUMERO:
            case TRUE:
            case FALSE:
            case CADENA:
                return true;
            default:
                return false;
        }
    }
    public boolean esOperador(){
        switch (this.tipo){
            case MAS:
            case MENOS:
            case ASTERISCO:
            case DIAGONAL:
            case ASIGNACION:
            case MAYOR:
            case MAYORIGUAL:
            case MENOR:
            case MENORIGUAL:
            case IGUAL:
            case DIFERENTE:
            case AND:
            case OR:
                return true;
            default:
                return false;
        }
    }
    public boolean esOperadorLogico(){
        switch (this.tipo) {
            case MAYOR:
            case MAYORIGUAL:
            case MENOR:
            case MENORIGUAL:
            case IGUAL:
            case DIFERENTE:
            case AND:
            case OR:
                return true;
            default:
                return false;
        }
    }
    public boolean esOperadorAritmetico(){
        switch (this.tipo) {
            case MAS:
            case MENOS:
            case ASTERISCO:
            case DIAGONAL:
            case ASIGNACION:
                return true;
            default:
                return false;
        }
    }
    public boolean esPalabraReservada(){
        switch (this.tipo){
            //case AND:
            case CLASS:
            case ELSE:
            //case FALSE:
            case FOR:
            case FUNCTION:
            case IF:
            case NULL:
            //case OR:
            case PRINT:
            case RETURN:
            case SUPER:
            case THIS:
            //case TRUE:
            case VAR:
            case WHILE:
                return true;
            default:
                return false;
        }
    }
    public boolean esEstructuraDeControl(){
        switch (this.tipo){
            case IF:
            case ELSE:
            case FOR:
            case WHILE:
                return true;
            default:
                return false;
        }
    }
    public boolean precedenciaMayorIgual(Token t){
        return this.obtenerPrecedencia() >= t.obtenerPrecedencia();
    }
    private int obtenerPrecedencia(){
        switch (this.tipo){
            case ASTERISCO:
            case DIAGONAL:
                return 7;
            case MAS:
            case MENOS:
                return 6;
            case MAYOR:
            case MAYORIGUAL:
            case MENOR:
            case MENORIGUAL:
                return 5;
            case IGUAL:
            case DIFERENTE:
                return 4;
            case AND:
                return 3;
            case OR:
                return 2;
            case ASIGNACION:
                return 1;
        }
        return 0;
    }
    public int aridad(){
        switch (this.tipo) {
            case ASTERISCO:
            case DIAGONAL:
            case MAS:
            case MENOS:
            case ASIGNACION:
            case MAYOR:
            case MAYORIGUAL:
            case MENOR:
            case MENORIGUAL:
            case IGUAL:
            case DIFERENTE:
            case AND:
            case OR:
                return 2;
        }
        return 0;
    }
}
