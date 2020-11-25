package semantico2expresiones;

import semantico.TipoBase;
import semantico2.ASTException;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public abstract class NodoExpresion {

    public abstract Token getToken();
    public abstract TipoBase check() throws ASTException;
    
    
}