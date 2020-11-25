package semantico;


import Claves.ClavesServices;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class TipoNull extends TipoReferencia {
    private Token tipo;
    
    public TipoNull(Token tipo){
        this.tipo = tipo;
    }

    @Override
    public Token getToken() {
        return this.tipo;
    }

    @Override
    public String getTipo() {
        return ClavesServices.TokenTypes.NULL.toString();
    }
     @Override
    public boolean esTipoValido() {
        return true;
    }

	@Override
	public boolean esCompatible(TipoBase e) {
		boolean toR = false;
		if(e.esTipoClase())
			toR = true;
		return toR;
	}
    
}
