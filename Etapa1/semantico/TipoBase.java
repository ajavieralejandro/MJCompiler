package semantico;

import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public abstract class TipoBase {
    
    
    public abstract Token getToken();
    public abstract String getTipo();
    public abstract boolean esTipoValido();
    
}
