package semantico2;

import semantico.TipoBase;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public abstract class NodoExpresion {

    public abstract Token getToken();
    public abstract TipoBase check() throws ASTException;
    
    
}