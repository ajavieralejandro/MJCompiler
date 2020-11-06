package semantico;

import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public abstract class VarMetodo extends Variable {
    
    public VarMetodo(Tipo tipo, String nombre, Token token) {
        super(tipo, nombre, token);
    }
    
    
}
