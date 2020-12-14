package semantico2;

import Claves.ClavesServices;
import semantico.Metodo;
import semantico.Tipo;
import semantico.TipoBase;
import semantico2expresiones.NodoExpresion;

/**
 *
 * @author Javier Amorosi
 */
public class NodoReturn extends NodoSentencia {
    
    private NodoExpresion expresion;
    private Metodo metodo;
    
    public NodoReturn(Metodo metodo){
        this.metodo = metodo;
    }

    public NodoExpresion getExpresion() {
        return expresion;
    }

    public void setExpresion(NodoExpresion expresion) {
        this.expresion = expresion;
    }

    @Override
    public void check() throws ASTException {
    	System.out.println("Estoy chequeando un nodo return");
        //Si la expresion es null entonces el tipo de retorno debera ser  void?? chequear esto!!!
        //Chequear la representación del tipo void en la tabla de símbolos
        if(this.expresion==null
            && !this.metodo.getRetorno().getTipo().equals(ClavesServices.TokenTypes.VOID.toString()))
            throw new ASTException("Error Semantico :"
                                    +" el metodo :  "+this.metodo.getNombre()+
                                    "  espara un tipo de retorno : "
                                    +this.metodo.getRetorno().getTipo()+
                                     this.metodo.getToken().getError());
        
        
        TipoBase aux = this.expresion.check();
        System.out.println("El valor de aux es : "+aux.getTipo());
            
        if(!aux.esTipoValido())
            throw new ASTException("Error Semantico : La expresión de retorno no es de tipo valido, en"
                    +aux.getToken().getError());
        if(!this.metodo.getRetorno().esCompatible(aux))
            throw new ASTException("ERROR SEMANTICO : La expresión de retorno no es compatible con el tipo de retorno:"+this.metodo.getRetorno().getTipo()
            		+aux.getToken().getError());
        
    }
    
}
