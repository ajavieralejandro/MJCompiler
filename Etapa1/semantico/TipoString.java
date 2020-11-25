package semantico;

import Claves.ClavesServices;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class TipoString extends TipoPrimitivo {
    private Token tipo;
    
    public TipoString(Token tipo){
        this.tipo = tipo;
    }
    
    public TipoString(){
        this.tipo = null;
    }
    

    @Override
    public Token getToken() {
        return this.tipo;
    }

    @Override
    public String getTipo() {
        return ClavesServices.TokenTypes.STRING.toString();
    }
    
     @Override
    public boolean esTipoValido() {
        return true;
    }

	@Override
	public boolean esCompatible(TipoBase e) {
		boolean toR = false;
		if(e.getTipo().equals(ClavesServices.TokenTypes.STRING.toString()))
			toR = true;
		return toR;
	}
    
}
