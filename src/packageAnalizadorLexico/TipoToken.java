package packageAnalizadorLexico;

public enum TipoToken
{
    // Palabras reservadas:
    AND,
    CLASS,
    ELSE,
    FALSE,
    FOR,
    FUNCTION,
    IF,
    NULL,
    OR,
    PRINT,
    RETURN,
    SUPER,
    THIS,
    TRUE,
    VAR,
    WHILE,
    // Indentificador:
    ID,
    // Cadena:
    CADENA,
    // Numero:
    NUMERO,
    // Comentario
    COMENTARIO, 
    // Signo del lenguaje:
    PARENTESIS_ABRE,
    PARENTESIS_CIERRA,
    LLAVE_ABRE,
    LLAVE_CIERRA,
    COMA,
    PUNTO,
    PUNTOYCOMA,
    MENOS,
    MAS,
    ASTERISCO,
    DIAGONAL,
    ADMIRACION,
    DIFERENTE,
    ASIGNACION,
    IGUAL,
    MENOR,
    MENORIGUAL,
    MAYOR,
    MAYORIGUAL,
    // Final de cadena:
    EOF
}
