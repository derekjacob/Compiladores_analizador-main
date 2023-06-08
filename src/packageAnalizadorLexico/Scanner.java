package packageAnalizadorLexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.lang.String;

public class Scanner {
    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private int linea = 0;

    private static final Map<String, TipoToken> palabrasReservadas;
    private static final Map<String, TipoToken> simbolos;
    private static final Map<String, TipoToken> simbolosDobles;
    private static boolean reservadaEncontrada = false, comentarioLinea = false, comentarioComplejo = false;

    static {
        // HashMap para palabras reservadas
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("class", TipoToken.CLASS);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("fun", TipoToken.FUNCTION);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("nulL", TipoToken.NULL);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("super", TipoToken.SUPER);
        palabrasReservadas.put("this", TipoToken.THIS);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);
        // HashMap para simbolos
        simbolos = new HashMap<>();
        simbolos.put("(", TipoToken.PARENTESIS_ABRE);
        simbolos.put(")", TipoToken.PARENTESIS_CIERRA);
        simbolos.put("{", TipoToken.LLAVE_ABRE);
        simbolos.put("}", TipoToken.LLAVE_CIERRA);
        simbolos.put(",", TipoToken.COMA);
        simbolos.put(".", TipoToken.PUNTO);
        simbolos.put(";", TipoToken.PUNTOYCOMA);
        simbolos.put("-", TipoToken.MENOS);
        simbolos.put("+", TipoToken.MAS);
        simbolos.put("*", TipoToken.ASTERISCO);
        simbolos.put("/", TipoToken.DIAGONAL);
        simbolos.put("!", TipoToken.ADMIRACION);
        simbolos.put("=", TipoToken.ASIGNACION);
        simbolos.put("<", TipoToken.MENOR);
        simbolos.put(">", TipoToken.MAYOR);
        // HashMap para simbolos de dos digitos.
        simbolosDobles = new HashMap<>();
        simbolosDobles.put("!=", TipoToken.DIFERENTE);
        simbolosDobles.put("==", TipoToken.IGUAL);
        simbolosDobles.put("<=", TipoToken.MENORIGUAL);
        simbolosDobles.put(">=", TipoToken.MAYORIGUAL);
    }

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        // Variables utilizadas para almacenar informacion
        int posicionActual = 0, estados = 0;
        String caracterLeido = "", cadenaLeida = "";
        // Lista para detectar los numeros
        List<String> numeros = new ArrayList<>();
        numeros.add("0");
        numeros.add("1");
        numeros.add("2");
        numeros.add("3");
        numeros.add("4");
        numeros.add("5");
        numeros.add("6");
        numeros.add("7");
        numeros.add("8");
        numeros.add("9");
        // La linea leida es separada en caracteres.
        String[] caracteres = source.split("|");
        // Analisis de la linea leida caracter por caracter.
        while (posicionActual < caracteres.length) {
            if (!(caracteres[posicionActual].equals("\n") || caracteres[posicionActual].equals("\r") || caracteres[posicionActual].equals(" "))) {
                if(comentarioLinea == false && comentarioComplejo == false) {
                    reservadaEncontrada = false;
                    if (caracteres[posicionActual].equals(" ") && estados != 1) {
                        posicionActual++;
                        cadenaLeida = "";
                        estados = 0;
                    }
                    if (!caracteres[posicionActual].equals(" ")) {
                        caracterLeido = caracteres[posicionActual];
                        cadenaLeida += caracterLeido;
                    }
                    if (simbolos.containsKey(caracteres[posicionActual])) {

                        if (estados == 0) {
                            cadenaLeida = "";
                            posicionActual = comprobarSimboloDoble(posicionActual, caracteres);
                        } else {
                            interrupcionPorSimbolo(posicionActual, caracteres, cadenaLeida);
                            posicionActual++;
                            posicionActual = comprobarSimboloDoble(posicionActual - 1, caracteres);
                            cadenaLeida = "";
                            estados = 0;
                        }
                    }
                    else if (caracteres[posicionActual].equals(" ") && estados == 1) {
                        if (reservadaEncontrada = verificarPalabraReservada(posicionActual, caracteres, cadenaLeida) == false) {
                            posicionActual = terminarIdentificador(posicionActual, caracteres, cadenaLeida);
                        } else {
                            posicionActual++;
                        }
                        cadenaLeida = "";
                        estados = 0;
                    } else if ((estados == 0 || estados == 1) && !EsNumerico(caracterLeido) && !simbolos.containsKey(caracteres[posicionActual]) && !caracterLeido.equals("\"")) {
                        estados = 1;
                        reservadaEncontrada = verificarPalabraReservada(posicionActual, caracteres, cadenaLeida);
                        if (reservadaEncontrada == true) {
                            cadenaLeida = "";
                            estados = 0;
                            posicionActual++;
                        }
                    } else if (estados == 0 && cadenaLeida.equals("\"")) {
                        cadenaLeida = "";
                        posicionActual = buscarFinalDeCadena(posicionActual, caracteres);
                    } else if (estados == 0 && numeros.contains(caracterLeido)) {
                        cadenaLeida = "";
                        posicionActual = comprobarLongitudDeNumero(posicionActual, caracteres);
                    } else if (estados == 1 && EsNumerico(caracterLeido) && reservadaEncontrada == false) {
                        estados = 0;
                        posicionActual = terminarIdentificador(posicionActual, caracteres, cadenaLeida);
                        cadenaLeida = "";
                    }
                    posicionActual++;
                }else{
                    if((caracteres[posicionActual-1] + caracteres[posicionActual]).equals("*/")){
                        comentarioComplejo = false;
                    }
                    try{
                        if(caracteres[posicionActual].equals("\n")){
                            comentarioLinea = false;
                            linea++;
                        }
                    }catch(Exception UltimoCaracracter){

                    }

                }
            } else {
                posicionActual++;
            }
            try{
                if(caracteres[posicionActual].equals("\n"))linea++;
            }catch(Exception UltimoCaracracter){
            }
        }
        if (estados == 1 && !(cadenaLeida.equals("\n") || cadenaLeida.equals("") || cadenaLeida.equals(" "))) {
            tokens.add(new Token(TipoToken.ID, cadenaLeida, null, linea));
        }
        tokens.add(new Token(TipoToken.EOF, "", null, linea));
        return tokens;

    }

    // Metodo para completar tokens cuando son interrumpidos por un simbolo
    void interrupcionPorSimbolo(int posicionUltimoCaracter, String[] caracteres, String cadenaPrevia){
        cadenaPrevia = cadenaPrevia.substring(0, cadenaPrevia.length()-1);
        if(palabrasReservadas.containsKey(cadenaPrevia) && simbolos.containsKey(caracteres[posicionUltimoCaracter])){
            tokens.add(new Token(palabrasReservadas.get(cadenaPrevia),cadenaPrevia,null,linea));
        }else if(simbolos.containsKey(caracteres[posicionUltimoCaracter])){
            tokens.add(new Token(TipoToken.ID, cadenaPrevia, null, linea));
        }
    }

    // Metodo que añade un token de identificador
    int terminarIdentificador(int posicionInterna, String[] caracteres, String cadenaPrevia){
        comprobarSimboloDoble(posicionInterna,caracteres);
        try{
            while(!caracteres[posicionInterna].equals(" ") && !simbolos.containsKey(caracteres[posicionInterna])){
                posicionInterna++;
                cadenaPrevia += caracteres[posicionInterna];
            }
            posicionInterna--;
            if(!cadenaPrevia.equals(""))tokens.add(new Token(TipoToken.ID, cadenaPrevia, null, linea));
            return posicionInterna;
        }catch(Exception EsElUltimoCaracter){
            if(!cadenaPrevia.equals(""))tokens.add(new Token(TipoToken.ID, cadenaPrevia, null, linea));
        }
        return posicionInterna;
    }

    // Metodo que verifica si los caracteres leidos son una palabra reservada
    boolean verificarPalabraReservada(int posicionInterna, String[] caracteres, String palabraReservada){
        try{
            if(palabrasReservadas.containsKey(palabraReservada) && caracteres[posicionInterna + 1].equals(" ")){
                tokens.add(new Token(palabrasReservadas.get(palabraReservada),palabraReservada,null,linea));
                return true;
            }
        }catch(Exception EsElUltimoCaracter){
            tokens.add(new Token(palabrasReservadas.get(palabraReservada),palabraReservada,null,linea));
            return true;
        }
        return false;
    }
    // Metodo que añade un token de cadena
    int buscarFinalDeCadena(int posicionInterna, String[] caracteres){
        String valorDeCadena = caracteres[posicionInterna];
        posicionInterna++;
        try{
            while(!caracteres[posicionInterna].equals("\"")){
                valorDeCadena += caracteres[posicionInterna];
                posicionInterna++;
            }
            valorDeCadena += caracteres[posicionInterna];
            tokens.add(new Token(TipoToken.CADENA, valorDeCadena, null, linea));
        }catch(Exception ultimoCaracter){
            Interprete.error(linea, "No se cierra la cadena");
        }
        return posicionInterna;
    }
    // Metodo que añade un token de numero
    int comprobarLongitudDeNumero(int posicionInterna, String[] caracteres){
        String numero = "";
        while(EsNumerico(caracteres[posicionInterna]) || caracteres[posicionInterna].equals(".")){
            numero  += caracteres[posicionInterna];
            posicionInterna++;
            if(posicionInterna == caracteres.length) break;
        }
        posicionInterna--;
        tokens.add(new Token(TipoToken.NUMERO,numero,null,linea));
        return posicionInterna;
    }
    // Metodo que añade un token de simbolo
    int comprobarSimboloDoble(int posicionInterna, String[] caracteres){
        String caracterActual = caracteres[posicionInterna];
        if(caracterActual.equals("!") || caracterActual.equals("<") || caracterActual.equals(">") || caracterActual.equals("=") || caracterActual.equals("/")){
            try{
                String caracterDoble = caracteres[posicionInterna] + caracteres[posicionInterna + 1];
                if(simbolosDobles.containsKey(caracterDoble)){
                    tokens.add(new Token(simbolosDobles.get(caracterDoble),caracterDoble,null,linea));
                    posicionInterna++;
                }
                //Comentario para una linea
                else if(caracterDoble.equals("//")){
                    posicionInterna = caracteres.length;
                    comentarioLinea = true;
                }
                else if(caracterDoble.equals("/*")){
                    posicionInterna = caracteres.length;
                    comentarioComplejo = true;
                }
                else{
                    tokens.add(new Token(simbolos.get(caracteres[posicionInterna]),caracteres[posicionInterna],null,linea));
                }
            }catch(Exception ultimoCaracter){
                tokens.add(new Token(simbolos.get(caracteres[posicionInterna]),caracteres[posicionInterna],null,linea));
            }
        } else if(simbolos.containsKey(caracteres[posicionInterna])){
            tokens.add(new Token(simbolos.get(caracteres[posicionInterna]),caracteres[posicionInterna],null,linea));
        }
        return posicionInterna;
    }
    // Comprobador de numeros
    boolean EsNumerico(String cadena) {
        boolean resultado;
        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException NoEsNumero) {
            resultado = false;
        }
        return resultado;
    }
}
