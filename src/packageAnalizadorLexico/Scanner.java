package packageAnalizadorLexico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.spec.ChaCha20ParameterSpec;
import javax.lang.model.util.ElementScanner14;

import java.lang.String;

public class Scanner
{
    private final String source;

    private final List<Token> tokens = new ArrayList<>();

    private int linea = 1;

    private static final Map<String, TipoToken> palabrasReservadas;
    private static final Map<String, TipoToken> simbolos;
    private static final Map<String, TipoToken> simbolosDobles;
    static {
        // HashMap para palabras reservadas
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("class", TipoToken.CLASS);
        palabrasReservadas.put("else", TipoToken.ELSE );
        palabrasReservadas.put("false", TipoToken.FALSE );
        palabrasReservadas.put("for", TipoToken.FOR );
        palabrasReservadas.put("function", TipoToken.FUNCTION );
        palabrasReservadas.put("if", TipoToken.IF );
        palabrasReservadas.put("nulL", TipoToken.NULL );
        palabrasReservadas.put("or", TipoToken.OR );
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN );
        palabrasReservadas.put("super", TipoToken.SUPER );
        palabrasReservadas.put("this", TipoToken.THIS );
        palabrasReservadas.put("true", TipoToken.TRUE );
        palabrasReservadas.put("var", TipoToken.VAR );
        palabrasReservadas.put("while", TipoToken.WHILE );
        // HashMap para simbolos
        simbolos = new HashMap<>();
        simbolos.put("(", TipoToken.PARENTESIS_ABRE );
        simbolos.put(")", TipoToken.PARENTESIS_CIERRA );
        simbolos.put("{", TipoToken.LLAVE_ABRE );
        simbolos.put("}", TipoToken.LLAVE_CIERRA );
        simbolos.put(",", TipoToken.COMA );
        simbolos.put(".", TipoToken.PUNTO );
        simbolos.put(";", TipoToken.PUNTOYCOMA );
        simbolos.put("-", TipoToken.MENOS );
        simbolos.put("+", TipoToken.MAS );
        simbolos.put("*", TipoToken.ASTERISCO );
        simbolos.put("/", TipoToken.DIAGONAL );
        simbolos.put("!", TipoToken.ADMIRACION );
        simbolos.put("=", TipoToken.ASIGNACION );
        simbolos.put("<", TipoToken.MENOR );
        simbolos.put(">", TipoToken.MAYOR );
        // HashMap para simbolos de dos digitos.
        simbolosDobles = new HashMap<>();
        simbolosDobles.put("!=", TipoToken.DIFERENTE );
        simbolosDobles.put("==", TipoToken.IGUAL );
        simbolosDobles.put("<=", TipoToken.MENORIGUAL );
        simbolosDobles.put(">=", TipoToken.MAYORIGUAL );
    }

    Scanner(String source){
        this.source = source;
    }

    List<Token> scanTokens(){
        int posicion = 0;
        int estados = 0;
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
        String aux = "", aux2 = "", aux3 = "";
        // La linea leida es separada en caracteres.
        String[] caracteres = source.split("|");
        // Analisis de la linea leida caracter por caracter.
        while(posicion < caracteres.length) {
            aux2 = aux + caracteres[posicion];
            if(aux2 == " "){
                posicion++;

                aux2 = caracteres[posicion];
            }
            if (simbolos.containsKey(caracteres[posicion]) && (estados == 1 || estados == 3 || estados == 5)) {
                estados = 2;
            } else if (numeros.contains(caracteres[posicion]) && estados == 0) {
                estados = 5;
            } else if ((simbolos.containsKey(caracteres[posicion])) && estados == 0) {
                estados = 6;
            } else if (caracteres[posicion].equals(" ") && (estados == 1 || estados == 2)) {
                estados = 4;
            } else if (numeros.contains(caracteres[posicion]) && estados == 1) {
                estados = 3;
            } else if (caracteres[posicion].equals("\"") && estados == 0) {
                posicion++;
                while (caracteres[posicion].equals("\"") == false) {
                    aux2 += caracteres[posicion];
                    posicion++;
                }
                aux2 += caracteres[posicion];
                tokens.add(new Token(TipoToken.CADENA, aux2, null, linea));
                aux2 = "";
                estados = 0;
            } else if (estados == 0 && caracteres[posicion] !=" ") {
                estados = 1;
            } else if (estados == 0 && caracteres[posicion] ==" ") {
                estados = 0;
            }

            System.out.println("Cadracter leido: "+caracteres[posicion
                    ]+" Posicion: "+posicion+" Estado: "+estados+" cadena leida: \""+aux2+"\" Aux: \""+aux+"\"");
            if(caracteres[posicion].equals(" ") && estados == 4){
                tokens.add(new Token(TipoToken.ID, aux2, null, linea));
                aux2 = "";
                estados = 0;
            }
            // Comprobacion sobre el primer caracter leido despues de un token.
            if(numeros.contains(caracteres[posicion]) && estados == 5){
                aux2 = "";
                while(isNumeric(caracteres[posicion])){
                    aux2+= caracteres[posicion];
                    posicion++;
                }
                tokens.add(new Token(TipoToken.NUMERO, aux2, aux2, linea));
                posicion--;
                aux2 = "";
                estados = 0;
            }
            else if(aux2 != "" && numeros.contains(caracteres[posicion]) && estados == 3){
                while(caracteres[posicion]!=" " && !(simbolos.containsKey(caracteres[posicion]))){
                    posicion++;
                }
                tokens.add(new Token(TipoToken.ID,aux2,null,linea));
                posicion--;
                aux2 = "";
                estados = 0;
            }
            // Comprobación si la cadena almacenada en el auxiliar es una palabra reservada.
            else if(palabrasReservadas.containsKey(aux2) && estados == 1){
                try{
                    if(true){
                        tokens.add(new Token(palabrasReservadas.get(aux2),aux2,null,linea));
                        aux2 = "";
                        estados = 0;
                    }
                }catch(Exception a){}
            }
            // Comprobacion si el caracter actual es un simbolo.
            else if(simbolos.containsKey(caracteres[posicion]) && (estados == 6 || estados == 2)){
                if(caracteres[posicion].equals("!") || caracteres[posicion].equals("=") || caracteres[posicion].equals("<") || caracteres[posicion].equals(">")){
                    try{
                        aux3 = caracteres[posicion] + caracteres [posicion + 1];
                        if(simbolosDobles.containsKey(aux3)){
                            tokens.add(new Token(simbolosDobles.get(aux3),aux3,null,linea));
                            posicion++;
                            aux2 = "";
                            aux3 = "";
                            estados = 0;
                        }
                    }catch(Exception ex){
                        tokens.add(new Token(simbolos.get(caracteres[posicion]),caracteres[posicion],null,linea));
                        aux2 = "";
                        estados = 0;
                    }
                }else{
                    tokens.add(new Token(simbolos.get(caracteres[posicion]),caracteres[posicion],null,linea));
                    aux2 = "";
                    estados = 0;
                }
            }
            posicion++;
            aux = aux2;
            if(aux2 == " " || aux == " "){
                aux2 = "";
                aux = "";
            }
        }
            // Auxiliar utilizado para almacenar lo analizado hasta el momento, se reiniciará cuando se detecte un token.
            // Fin de la comprobación.
            
            
        
        linea++;
        tokens.add(new Token(TipoToken.EOF, "", null, linea));
        return tokens;
    }
    boolean isNumeric(String cadena) {

        boolean resultado;

        try {
            Integer.parseInt(cadena);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }
        return resultado;
    }
      
}
