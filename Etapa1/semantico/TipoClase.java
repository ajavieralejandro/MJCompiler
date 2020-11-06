package semantico;

import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class TipoClase extends TipoReferencia {
    
    private Token tipo;
    private final TDS ts;
    
    public TipoClase(Token tipo, TDS ts){
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
    
}
