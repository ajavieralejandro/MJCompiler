package semantico;


import Claves.ClavesServices;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class TipoVoid extends TipoBase {
    
    private Token tipo;
    
    public TipoVoid(Token tipo){
        this.tipo = tipo;
    }
    
    public TipoVoid(){
    
    }
  

    @Override
    public Token getToken() {
        return this.tipo;
    }

    @Override
    public String getTipo() {
        return ClavesServices.TokenTypes.VOID.toString();
    }
    
     @Override
    public boolean esTipoValido() {
        return true;
    }
    
}
