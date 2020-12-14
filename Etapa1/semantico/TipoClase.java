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
    
    public TipoClase(Token tipo){
    	//Fixing bug in Object
    	if(tipo.getLexema().equals("Object"))
    		tipo.setLexema(ClavesServices.TokenTypes.OBJECT.toString());
        this.tipo = tipo;
        this.ts = TDS.getInstance();
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
    	 boolean _toR = false;
    	 System.out.println("Estoy en el tipo valido de clase con : "+this.getTipo());
    	if(this.getTipo().equals("null") || this.getTipo().equals(ClavesServices.TokenTypes.NULL.toString()))
    		_toR = true;
    	else
    		_toR = ts.getClases().containsKey(this.getTipo());
    		
        return _toR;
    }
     
     public boolean esTipoClase(){
         return true;
     }

	@Override
	public boolean esCompatible(TipoBase e) {
		boolean toR = false;
		if(e.getTipo().equals(ClavesServices.TokenTypes.NULL.toString()))
			toR = true;
		if(e.esTipoClase() && this.esTipoClase())
			toR = ts.sonCompatibles(this, e);
		return toR;
	}
    
}
