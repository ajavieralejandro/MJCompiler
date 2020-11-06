package semantico;


import Claves.ClavesServices;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class TipoBoolean extends TipoPrimitivo {
    
    private final Token tipo;
    
    public TipoBoolean(Token t){
        this.tipo = t;
    }
    
    public TipoBoolean(){
        this.tipo = null;
    
    }
    
   

    @Override
    public String getTipo() {
        return ClavesServices.TokenTypes.BOOLEAN.toString();
    }

    @Override
    public Token getToken() {
        return this.tipo;
    }

    @Override
    public boolean esTipoValido() {
        return true;
    }
    
}
