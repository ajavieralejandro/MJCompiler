package semantico2;

import semantico2expresiones.NodoExpresion;

public class NodoSentSimple extends NodoSentencia {
	
    private NodoExpresion expresion;


    public NodoExpresion getExpresion() {
        return expresion;
    }

    public void setExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    @Override
    public void check() throws ASTException {
        if(!this.expresion.check().esTipoValido())
            throw new ASTException("Error Semantico, la expresion no es de tipo valido en linea, tipo: "
                    + this.expresion.check().getTipo()+this.expresion.getToken().getError());
    }
}
