package semantico;


import Claves.ClavesServices;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class TipoInt extends TipoPrimitivo {
    
    private Token tipo;
    
    public TipoInt(Token tipo){
        this.tipo = tipo;
    }
    
    public TipoInt(){
    
    }

    @Override
    public Token getToken() {
        return this.tipo;
    }
     @Override
    public boolean esTipoValido() {
        return true;
    }
    
    public String getTipo(){
        return ClavesServices.TokenTypes.INT.toString();
    }

	@Override
	public boolean esCompatible(TipoBase e) {
            System.out.println("Estoy viendo si soy compatible, tengo : "+this.getTipo());
		boolean toR = false;
		if(e.getTipo().equals(ClavesServices.TokenTypes.INT.toString()))
			toR = true;
                System.out.println("Estoy retornando : "+toR);
		return toR;
	}
    
}
