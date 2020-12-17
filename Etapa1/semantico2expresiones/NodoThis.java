package semantico2expresiones;

import Claves.ClavesServices;
import semantico.Clase;
import semantico.TDS;
import semantico.TipoBase;
import semantico.Unidad;
import semantico2.ASTException;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class NodoThis extends NodoPrimario {
    
    private final Token tk;
    private final Unidad actual;
    private final Clase _claseActual;
    

    
    public NodoThis(Token tk,Unidad actual, Clase claseActual){
        this.tk = tk;
        this.actual = actual;
        this._claseActual = claseActual;
    }

    @Override
    public Token getToken() {
        return this.tk;
    }

    @Override
    public TipoBase check() throws ASTException {
        TipoBase _toR = null;
        //chequeo que la unidad actual no sea static
        if(this.actual.getFormaMetodo().equals(ClavesServices.TokenTypes.STATIC))
            throw new ASTException("Error Semantico : No pueden haber referencias a this dentro de un metodo static"
                    + "en linea : "+this.tk.getLine()+" columna : "+this.tk.getRow());
        
        //Retorno el tipo de la clase actual...
        if(this.chain())
            _toR = this.getCadena().check(this._claseActual.getTipo());
        else
            _toR = this._claseActual.getTipo();
        
        return _toR;
        
    }
    
}