package packageAnalizadorLexico;

import java.util.List;

public class Parser{
    private final List<Token> tokens;

    private final Token andToken = new Token(TipoToken.AND,"and",null,0);
    private final Token classToken = new Token(TipoToken.CLASS,"class",null, 0);
    private final Token elseToken = new Token(TipoToken.ELSE,"else",null,0);
    private final Token falseToken = new Token(TipoToken.FALSE,"false",null,0);
    private final Token forToken = new Token(TipoToken.FOR,"for",null,0);
    private final Token functionToken = new Token(TipoToken.FUNCTION,"function",null,0);
    private final Token ifToken = new Token(TipoToken.IF,"if",null,0);
    private final Token nullToken = new Token(TipoToken.NULL,"null",null,0);
    private final Token orToken = new Token(TipoToken.OR,"or",null,0);
    private final Token printToken = new Token(TipoToken.PRINT,"print",null,0);
    private final Token returnToken = new Token(TipoToken.RETURN,"return",null,0);
    private final Token superToken = new Token(TipoToken.SUPER,"super",null,0);
    private final Token thisToken = new Token(TipoToken.THIS,"this",null,0);
    private final Token trueToken = new Token(TipoToken.TRUE,"true",null,0);
    private final Token varToken = new Token(TipoToken.VAR,"var",null,0);
    private final Token whileToken = new Token(TipoToken.WHILE,"while",null,0);
    private final Token idToken = new Token(TipoToken.ID,"",null,0);
    private final Token cadenaToken = new Token(TipoToken.CADENA,"",null,0);
    private final Token numeroToken = new Token(TipoToken.NUMERO,"",null,0);
    private final Token parentesisAbreToken = new Token(TipoToken.PARENTESIS_ABRE,"(",null,0);
    private final Token parentesisCierraToken = new Token(TipoToken.PARENTESIS_CIERRA,")",null,0);
    private final Token llaveAbreToken = new Token(TipoToken.LLAVE_ABRE,"{",null,0);
    private final Token llaveCierraToken = new Token(TipoToken.LLAVE_CIERRA,"}",null,0);
    private final Token comaToken = new Token(TipoToken.COMA,",",null,0);
    private final Token puntoToken = new Token(TipoToken.PUNTO,".",null,0);
    private final Token puntoYComaToken = new Token(TipoToken.PUNTOYCOMA,";",null,0);
    private final Token menosToken = new Token(TipoToken.MENOS,"-",null,0);
    private final Token masToken = new Token(TipoToken.MAS,"+",null,0);
    private final Token asteriscoToken = new Token(TipoToken.ASTERISCO,"*",null,0);
    private final Token diagonalToken = new Token(TipoToken.DIAGONAL,"/",null,0);
    private final Token admiracionToken = new Token(TipoToken.ADMIRACION,"!",null,0);
    private final Token diferenteToken = new Token(TipoToken.DIFERENTE,"!=",null,0);
    private final Token asignacionToken = new Token(TipoToken.ASIGNACION,"=",null,0);
    private final Token igualToken = new Token(TipoToken.IGUAL,"==",null,0);
    private final Token menorToken = new Token(TipoToken.MENOR,"<",null,0);
    private final Token menorIgualToken = new Token(TipoToken.MENORIGUAL,"<=",null,0);
    private final Token mayorToken = new Token(TipoToken.MAYOR,">",null,0);
    private final Token mayorIgualToken = new Token(TipoToken.MAYORIGUAL,">=",null,0);
    private final Token finCadena = new Token(TipoToken.EOF,"",null,0);

    private int i = 0;
    private boolean hayErrores = false;

    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
    }
    public boolean parse(){
        i = 0;
        preanalisis = tokens.get(i);
        PROGRAM();

        if(hayErrores && !preanalisis.equals(finCadena)){
            System.out.println("Error en la linea " + preanalisis.linea + ". No se esperaba el token "+ preanalisis.tipo);
            return false;
        }else if(!hayErrores && preanalisis.equals(finCadena)){
            System.out.println("Consulta valida");
            return true;
        }
        return false;
    }

    void PROGRAM(){
        if(hayErrores) return;
        if(preanalisis.equals(classToken) || preanalisis.equals(functionToken) || preanalisis.equals(varToken) || preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken) || preanalisis.equals(forToken) || preanalisis.equals(ifToken) || preanalisis.equals(printToken) || preanalisis.equals(returnToken) || preanalisis.equals(whileToken) || preanalisis.equals(llaveAbreToken)){
            DECLARATION(); 
        }
    }
    //DECLARACIONES
    void DECLARATION(){
        if(hayErrores) return;
        if(preanalisis.equals(classToken)){
            CLASS_DECL();
            DECLARATION();
        }
        else if(preanalisis.equals(functionToken)){
            FUN_DECL();
            DECLARATION();
        }
        else if(preanalisis.equals(varToken)){
            VAR_DECL();
            DECLARATION();
        }
        else if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken) || preanalisis.equals(forToken) || preanalisis.equals(ifToken) || preanalisis.equals(printToken) || preanalisis.equals(returnToken) || preanalisis.equals(whileToken) || preanalisis.equals(llaveAbreToken)){
            STATMENT();
            DECLARATION();
        }
    }
    void CLASS_DECL(){
        if(hayErrores) return;
        if(preanalisis.equals(classToken)){
            conincidir(classToken);
            conincidir(idToken);
            CLASS_INHER();
            conincidir(llaveAbreToken);
            FUNCTIONS();
            conincidir(llaveCierraToken);
        }
        else{
            hayErrores = true;
        }
    }
    void CLASS_INHER(){
        if(hayErrores) return;
        if(preanalisis.equals(menorToken)){
            conincidir(menorToken);
            conincidir(idToken);
        }
    }
    void FUN_DECL(){
        if(hayErrores) return;
        if(preanalisis.equals(functionToken)){
            conincidir(functionToken);
            FUNCTION();
        }else{
            hayErrores = true;
        }
    }
    void VAR_DECL(){
        if(hayErrores) return;
        if(preanalisis.equals(varToken)){
            conincidir(varToken);
            conincidir(idToken);
            VAR_INIT();
            conincidir(puntoYComaToken);
        }
        else{
            hayErrores = true;
        }
    }
    void VAR_INIT(){
        if(hayErrores) return;
        if(preanalisis.equals(asignacionToken)){
            conincidir(asignacionToken);
            EXPRESSION();
        }
    }
    //SENTENCIAS
    void STATMENT(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            EXPR_STMT();
        }
        else if(preanalisis.equals(forToken)){
            FOR_STMT();
        }
        else if(preanalisis.equals(ifToken)){
            IF_STMT();
        }
        else if(preanalisis.equals(printToken)){
            PRINT_STMT();
        }
        else if( preanalisis.equals(returnToken)){
            RETURN_STMT();
        }
        else if(preanalisis.equals(whileToken)){
            WHILE_STMT();
        }
        else if(preanalisis.equals(llaveAbreToken)){
            BLOCK();
        }
        else{
            hayErrores = true;
        }
    }
    void EXPR_STMT(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            EXPRESSION();
            conincidir(puntoYComaToken);
        }
        else{
            hayErrores = true;
        }
    }
    void FOR_STMT(){
        if(hayErrores) return;
        if(preanalisis.equals(forToken)){
            conincidir(forToken);
            conincidir(parentesisAbreToken);
            FOR_STMT_1();
            FOR_STMT_2();
            FOR_STMT_3();
            conincidir(parentesisCierraToken);
            STATMENT();
        }
        else{
            hayErrores = true;
        }
    }
    void FOR_STMT_1(){
        if(hayErrores) return;
            if(preanalisis.equals(varToken)){
                VAR_DECL();
            }
            else if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
                EXPR_STMT();
            }
            else if(preanalisis.equals(puntoYComaToken)){
                conincidir(puntoYComaToken);
            }
            else{
                hayErrores = true;
            }
    }
    void FOR_STMT_2(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            EXPRESSION();
            conincidir(puntoYComaToken);
        }else if(preanalisis.equals(puntoYComaToken)){
            conincidir(puntoYComaToken);
        }
        else{
            hayErrores = true;
        }
    }
    void FOR_STMT_3(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            EXPRESSION();
        }
    }
    void IF_STMT(){
        if(hayErrores) return;
        if(preanalisis.equals(ifToken)){
            conincidir(ifToken);
            conincidir(parentesisAbreToken);
            EXPRESSION();
            conincidir(parentesisCierraToken);
            STATMENT();
            ELSE_STATEMENT();
        }
        else{
            hayErrores = true;
        }
    }
    void ELSE_STATEMENT(){
        if(hayErrores) return;
        if(preanalisis.equals(elseToken)){
            conincidir(elseToken);
            STATMENT();
        }
    }
    void PRINT_STMT(){
        if(hayErrores) return;
        if(preanalisis.equals(printToken)){
            conincidir(printToken);
            EXPRESSION();
            conincidir(puntoYComaToken);
        }
        else{
            hayErrores = true;
        }
    }
    void RETURN_STMT(){
        if(hayErrores) return;
        if(preanalisis.equals(returnToken)){
            conincidir(returnToken);
            RETURN_EXP_OPC();
            conincidir(puntoYComaToken);
        }
        else{
            hayErrores = true;
        }
    }
    void RETURN_EXP_OPC(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            EXPRESSION();
        }
    }
    void WHILE_STMT(){
        if(hayErrores) return;
        if(preanalisis.equals(whileToken)){
            conincidir(whileToken);
            conincidir(parentesisAbreToken);
            EXPRESSION();
            conincidir(parentesisCierraToken);
            STATMENT();
        }
        else{
            hayErrores = true;
        }
    }
    void BLOCK(){
        if(hayErrores) return;
        if(preanalisis.equals(llaveAbreToken)){
            conincidir(llaveAbreToken);
            BLOCK_DECL();
            conincidir(llaveCierraToken);
        }
        else{
            hayErrores = true;
        }
    }
    void BLOCK_DECL(){
        if(hayErrores) return;
        if(preanalisis.equals(classToken) || preanalisis.equals(functionToken) || preanalisis.equals(varToken) || preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken) || preanalisis.equals(forToken) || preanalisis.equals(ifToken) || preanalisis.equals(printToken) || preanalisis.equals(returnToken) || preanalisis.equals(whileToken) || preanalisis.equals(llaveAbreToken)){
            DECLARATION();
            BLOCK_DECL();
        }
    }
    //ESPRESIONES
    void EXPRESSION(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            ASSIGNMENT();
        }
        else{
            hayErrores = true;
        }
    }
    void ASSIGNMENT(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            LOGIC_OR();
            ASSIGNMENT_OPC();
        }
        else{
            hayErrores = true;
        }
    }
    void ASSIGNMENT_OPC(){
        if(hayErrores) return;
        if(preanalisis.equals(asignacionToken)){
            conincidir(asignacionToken);
            EXPRESSION();
        }
    }
    void LOGIC_OR(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            LOGIC_AND();
            LOGIC_OR_2();
        }
        else{
            hayErrores = true;
        }
    }
    void LOGIC_OR_2(){
        if(hayErrores) return;
        if(preanalisis.equals(orToken)){
            conincidir(orToken);
            LOGIC_AND();
            LOGIC_OR_2();
        }
    }
    void LOGIC_AND(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            EQUALITY();
            LOGIC_AND_2();
        }
        else{
            hayErrores = true;
        }
    }
    void LOGIC_AND_2(){
        if(hayErrores) return;
        if(preanalisis.equals(andToken)){
            conincidir(andToken);
            EQUALITY();
            LOGIC_AND_2();
        }
    }
    void EQUALITY(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            COMPARISON();
            EQUALITY_2();
        }
        else{
            hayErrores = true;
        }
    }
    void EQUALITY_2(){
        if(hayErrores) return;
        if(preanalisis.equals(diferenteToken)){
            conincidir(diferenteToken);
            COMPARISON();
            EQUALITY_2();
        }
        else if(preanalisis.equals(igualToken)){
            conincidir(igualToken);
            COMPARISON();
            EQUALITY_2();
        }
    }
    void COMPARISON(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            TERM();
            COMPARISON_2();
        }
        else{
            hayErrores = true;
        }
    }
    void COMPARISON_2(){
        if(hayErrores) return;
        if(preanalisis.equals(mayorToken)){
            conincidir(mayorToken);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.equals(mayorIgualToken)){
            conincidir(mayorIgualToken);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.equals(menorToken)){
            conincidir(menorToken);
            TERM();
            COMPARISON_2();
        }
        else if(preanalisis.equals(menorIgualToken)){
            conincidir(menorIgualToken);
            TERM();
            COMPARISON_2();
        }
    }
    void TERM(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            FACTOR();
            TERM_2();
        }
        else{
            hayErrores = true;
        }
    }
    void TERM_2(){
        if(hayErrores) return;
        if(preanalisis.equals(menosToken)){
            conincidir(menosToken);
            FACTOR();
            TERM_2();
        }
        else if(preanalisis.equals(masToken)){
            conincidir(masToken);
            FACTOR();
            TERM_2();
        }
    }
    void FACTOR(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            UNARY();
            FACTOR_2();
        }
        else{
            hayErrores = true;
        }
    }
    void FACTOR_2(){
        if(hayErrores) return;
        if(preanalisis.equals(diagonalToken)){
            conincidir(diagonalToken);
            UNARY();
            FACTOR_2();
        }
        else if(preanalisis.equals(asteriscoToken)){
            conincidir(asteriscoToken);
            UNARY();
            FACTOR_2();
        }
    }
    void UNARY(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken)){
            conincidir(admiracionToken);
            UNARY();
        }
        else if(preanalisis.equals(menosToken)){
            conincidir(menosToken);
            UNARY();
        }
        else if(preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            CALL();
        }
        else{
            hayErrores = true;
        }
    }
    void CALL(){
        if(hayErrores) return;
        if(preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            PRIMARY();
            CALL_2();
        }
        else{
            hayErrores = true;
        }
    }
    void CALL_2(){
        if(hayErrores) return;
        if(preanalisis.equals(parentesisAbreToken)){
            conincidir(parentesisAbreToken);
            ARGUMENTS_OPC();
            conincidir(parentesisCierraToken);
            CALL_2();
        }
        else if(preanalisis.equals(puntoToken)){
            conincidir(puntoToken);
            conincidir(idToken);
            CALL_2();
        }
    }
    void CALL_OPC(){
        if(hayErrores) return;
        if(preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            CALL();
            conincidir(puntoToken);
        }
    }
    void PRIMARY(){
        if(hayErrores) return;
        if(preanalisis.equals(trueToken)){
            conincidir(trueToken);
        }
        else if(preanalisis.equals(falseToken)){
            conincidir(falseToken);
        }
        else if(preanalisis.equals(nullToken)){
            conincidir(nullToken);
        }
        else if(preanalisis.equals(thisToken)){
            conincidir(thisToken);
        }
        else if(preanalisis.equals(numeroToken)){
            conincidir(numeroToken);
        }
        else if(preanalisis.equals(cadenaToken)){
            conincidir(cadenaToken);
        }
        else if(preanalisis.equals(idToken)){
            conincidir(idToken);
        }
        else if(preanalisis.equals(parentesisAbreToken)){
            conincidir(parentesisAbreToken);
            EXPRESSION();
            conincidir(parentesisCierraToken);
        }
        else if(preanalisis.equals(superToken)){
            conincidir(superToken);
            conincidir(puntoToken);
            conincidir(idToken);
        }
        else{
            hayErrores = true;
        }
    }
    //OTRAS
    void FUNCTION(){
        if(hayErrores) return;
        if(preanalisis.equals(idToken)){
            conincidir(idToken);
            conincidir(parentesisAbreToken);
            PARAMETERS_OPC();
            conincidir(parentesisCierraToken);
            BLOCK();
        }
        else{
            hayErrores = true;
        }
    }
    void FUNCTIONS(){
        if(hayErrores) return;
        if(preanalisis.equals(idToken)){
            FUNCTION(); 
            FUNCTIONS();
        }
    }
    void PARAMETERS_OPC(){
        if(hayErrores) return;
        if(preanalisis.equals(idToken)){
            PARAMETERS();
        }
    }
    void PARAMETERS(){
        if(hayErrores) return;
        if(preanalisis.equals(idToken)){
            conincidir(idToken);
            PARAMETERS_2();
        }
        else{
            hayErrores = true;
        }
    }
    void PARAMETERS_2(){
        if(hayErrores) return;
        if(preanalisis.equals(comaToken)){
            conincidir(comaToken);
            conincidir(idToken);
            PARAMETERS_2();
        }
    }
    void ARGUMENTS_OPC(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            ARGUMENTS();
        }
    }
    void ARGUMENTS(){
        if(hayErrores) return;
        if(preanalisis.equals(admiracionToken) || preanalisis.equals(menosToken) || preanalisis.equals(trueToken) || preanalisis.equals(falseToken) || preanalisis.equals(nullToken) || preanalisis.equals(thisToken) || preanalisis.equals(numeroToken) || preanalisis.equals(cadenaToken) || preanalisis.equals(idToken) || preanalisis.equals(parentesisAbreToken) || preanalisis.equals(superToken)){
            EXPRESSION();
            ARGUMENTS_2();
        }
        else{
            hayErrores = true;
        }
    }
    void ARGUMENTS_2(){
        if(hayErrores) return;
        if(preanalisis.equals(comaToken)){
            conincidir(comaToken);
            EXPRESSION();
            ARGUMENTS_2();
        }
    }
    // FUNCION COINCIDIR
    void conincidir(Token t){
        if(hayErrores) return;
        if(preanalisis.tipo == t.tipo){
            i++;
            preanalisis = tokens.get(i);
        }
        else{
            hayErrores = true;
            System.out.println("Error en la linea " + preanalisis.linea + ". Se esperaba un " + t.tipo);
        }
    }
}