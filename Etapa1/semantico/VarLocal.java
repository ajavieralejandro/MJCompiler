package semantico;

import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class VarLocal extends VarMetodo {
    
    public VarLocal(Tipo tipo, String nombre, Token token) {
        super(tipo, nombre, token);
    }
    
}