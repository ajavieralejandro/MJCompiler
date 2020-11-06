package semantico;


import Claves.ClavesServices;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class TipoChar extends TipoPrimitivo {
    
    private Token tipo;
    
    public TipoChar(Token t){
        this.tipo = t;
    }
    
    public TipoChar(){
        this.tipo = null;
    }

    @Override
    public Token getToken() {
        return this.tipo;
    }
     @Override
    public boolean esTipoValido() {
        return true;
    }

    @Override
    public String getTipo() {
        return ClavesServices.TokenTypes.CHAR.toString();
    }
    
}
