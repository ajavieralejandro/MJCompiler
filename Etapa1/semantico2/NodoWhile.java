package semantico2;
import semantico.TipoBoolean;

public class NodoWhile extends NodoSentencia {

	private NodoExpresion condicion;
    private NodoSentencia sentencia;

    public NodoExpresion getCondicion() {
        return condicion;
    }

    public void setCondicion(NodoExpresion condicion) {
        this.condicion = condicion;
    }

    public NodoSentencia getSentencia() {
        return sentencia;
    }

    public void setSentencia(NodoSentencia sentencia) {
        this.sentencia = sentencia;
    }
    
    @Override
    public void check() throws ASTException {

        if(!this.condicion.check().esCompatible(new TipoBoolean()))
            throw new ASTException("Error Semantico: Se espera que la condición dentro del while se ade tipo boolean"
                    + "en linea : "+this.condicion.getToken().getLine()+" columna : "+this.condicion.getToken().getRow());
        this.sentencia.check();
    }
}
