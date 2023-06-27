package packageAnalizadorLexico;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {

    private final Map<String, Object> values = new HashMap<>();

    boolean existeIdentificador(String identificador){
        return values.containsKey(identificador);
    }

    Object obtener(String identificador) {
        if (values.containsKey(identificador)) {
            return values.get(identificador);
        }
        return null;
    }

    void asignar(String identificador, Object valor){
        values.put(identificador, valor);
    }
    void actualizar(String identificador, Object valor){
        System.out.println(identificador);
        System.out.println(valor);
        values.remove(identificador);
        values.put(identificador,valor);
    }
    void imprimir(){
        System.out.println(Arrays.asList(values));
    }

}
