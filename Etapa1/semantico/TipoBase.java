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
    public boolean esTipoClase(){
        return false;
    }
    public abstract boolean esCompatible(TipoBase e);
    public boolean equals(TipoBase e) {
    	return this.getTipo().equals(e.getTipo());
    	}
    
}
