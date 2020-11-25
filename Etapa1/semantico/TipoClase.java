package semantico;

import Claves.ClavesServices;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class TipoClase extends TipoReferencia {
    
    private Token tipo;
    private final TDS ts;
    
    public TipoClase(Token tipo, TDS ts){
    	//Fixing bug in Object
    	if(tipo.getLexema().equals("Object"))
    		tipo.setLexema(ClavesServices.TokenTypes.OBJECT.toString());
        this.tipo = tipo;
        this.ts = ts;
    }

    @Override
    public Token getToken() {
        return this.tipo;
    }

    @Override
    public String getTipo() {
        return this.tipo.getLexema();
    }
    
     @Override
    public boolean esTipoValido() {
        return ts.getClases().containsKey(this.getTipo());
    }
     
     public boolean esTipoClase(){
         return true;
     }

	@Override
	public boolean esCompatible(TipoBase e) {
		boolean toR = false;
		if(e.getTipo().equals(ClavesServices.TokenTypes.NULL.toString()))
			toR = true;
		toR = ts.sonCompatibles(this, e);
		return toR;
	}
    
}
