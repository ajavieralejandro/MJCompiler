package semantico2;

import semantico.TipoBoolean;
import semantico2expresiones.NodoExpresion;

/**
 *
 * @author Javier Amorosi
 */
public class NodoIf extends NodoSentencia {
    
        
    private NodoExpresion condicion;
    private NodoSentencia then;
    private NodoSentencia nodoElse;
    

    public NodoExpresion getCondicion() {
        return condicion;
    }

    public void setCondicion(NodoExpresion condicion) {
        this.condicion = condicion;
    }

    public NodoSentencia getThen() {
        return then;
    }

    public void setThen(NodoSentencia then) {
        this.then = then;
    }

    public NodoSentencia getNodoElse() {
        return nodoElse;
    }

    public void setNodoElse(NodoSentencia nodoElse) {
        this.nodoElse = nodoElse;
    }

    @Override
    public void check() throws ASTException {
                    
        if(!this.condicion.check().esCompatible(new TipoBoolean()))
            throw new ASTException("Error Semantico: Se espera que la condición sea de tipo boolean"
                    + "en linea : "+this.condicion.getToken().getLine()+this.condicion.getToken().getError());
        //luego chequeo S1 Y S2
        //No tengo el token del then, aclararlo en decisiones de diseño 
        if(this.then==null)
            throw new ASTException("Error Semantico : Nodo then vacio en."+this.condicion.getToken().getError());
        this.then.check();
        //Si el nodo Else es distinto de null
        if(this.nodoElse!=null)
            this.nodoElse.check();
    }
}