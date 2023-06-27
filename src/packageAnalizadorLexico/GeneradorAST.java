package packageAnalizadorLexico;

import java.util.List;
import java.util.Stack;

public class GeneradorAST {

    private final List<Token> postfija;
    private final Stack<Nodo> pila;

    public GeneradorAST(List<Token> postfija){
        this.postfija = postfija;
        this.pila = new Stack<>();
    }

    public Arbol generarAST() {
        Stack<Nodo> pilaPadres = new Stack<>();
        Nodo raiz = new Nodo(null);
        pilaPadres.push(raiz);

        Nodo padre = raiz;

        for(Token t : postfija){
            /*System.out.println("TOKEN: "+t.tipo);
            System.out.println("PILA: "+pila);
            System.out.println("PILA PADRES: "+pilaPadres.peek().getValue());
            System.out.println("PADRE: "+padre.getValue());
            System.out.println();*/
            if(t.tipo == TipoToken.EOF){
                break;
            }

            if(t.esPalabraReservada()){
                Nodo n = new Nodo(t);

                padre = pilaPadres.peek();
                padre.insertarSiguienteHijo(n);

                pilaPadres.push(n);
                padre = n;

            }
            else if(t.esOperando()){
                Nodo n = new Nodo(t);
                pila.push(n);
                /*if(padre.getValue()==null){
                    padre = new Nodo(new Token(TipoToken.ASIGNACION,"=","=", t.linea));
                    pilaPadres.push(padre);
                }*/
            }
            else if(t.esOperador()){
                Nodo n = new Nodo(t);
                int aridad = t.aridad();
                for(int i=1; i<=aridad; i++){
                    Nodo nodoAux = pila.pop();
                    n.insertarHijo(nodoAux);
                }
                pila.push(n);

            }
            else if(t.tipo == TipoToken.PUNTOYCOMA){

                if (pila.isEmpty()){
                    /*
                    Si la pila esta vacía es porque t es un punto y coma
                    que cierra una estructura de control
                     */
                    pilaPadres.pop();
                    padre = pilaPadres.peek();
                }
                else{

                    Nodo n = pila.pop();
                    if(padre.getValue() == null){
                        padre.insertarSiguienteHijo(n);
                    }
                    else if(padre.getValue().tipo == TipoToken.VAR ||padre.getValue().tipo == TipoToken.ASIGNACION){

                        /*
                        En el caso del VAR, es necesario eliminar el igual que
                        pudiera aparecer en la raíz del nodo n.
                         */
                        if(n.getValue().tipo == TipoToken.ASIGNACION){
                            padre.insertarSiguienteHijo(n);
                        }
                        else if(n.getValue().tipo == TipoToken.ID){

                        }
                        else{
                            padre.insertarHijos(n.getHijos());
                        }
                        pilaPadres.pop();
                        padre = pilaPadres.peek();
                    }
                    else if(padre.getValue().tipo == TipoToken.PRINT){
                        padre.insertarSiguienteHijo(n);
                        pilaPadres.pop();
                        padre = pilaPadres.peek();
                    }
                    /*else if(padre.getValue().tipo == TipoToken.ASIGNACION){
                        Nodo n1 = pila.pop();
                        padre.insertarSiguienteHijo(n1);
                        padre.insertarSiguienteHijo(n);
                    }*/
                    else {
                        padre.insertarSiguienteHijo(n);
                        //System.out.println(n.getValue());
                    }

                }
            }
        }

        // Suponiendo que en la pila sólamente queda un nodo
        // Nodo nodoAux = pila.pop();

        return new Arbol(raiz);
    }
}

