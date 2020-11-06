package semantico;

import token.Token;

/**
 *
 * @author Javier
 */
public abstract class Variable {

    private final Token token;

    public Token getToken() {
        return token;
    }
    private final String nombre;
    private final Tipo tipo;
    
    public Variable(Tipo tipo,String nombre,Token token){
        this.token = token;
        this.nombre = nombre;
        this.tipo = tipo;
    }
      public String getNombre() {
        return nombre;
    }

    public Tipo getTipo() {
        return tipo;
    }
    
    
    
    
}
