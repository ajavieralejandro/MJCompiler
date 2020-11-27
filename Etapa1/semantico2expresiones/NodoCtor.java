package semantico2expresiones;

import java.util.ArrayList;

import semantico.Clase;
import semantico.Ctor;
import semantico.TDS;
import semantico.Tipo;
import semantico.TipoBase;
import semantico2.ASTException;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class NodoCtor extends NodoPrimario {
    
    private final Token id;
    private final TDS ts;
    private ArrayList<NodoExpresion> actualArgs;

    public ArrayList<NodoExpresion> getActualArgs() {
        return actualArgs;
    }

    public void setActualArgs(ArrayList<NodoExpresion> actualArgs) {
        this.actualArgs = actualArgs;
    }
    
    public NodoCtor(Token id){
        this.id = id;
        this.ts = TDS.getInstance();
    }
    

    @Override
    public Token getToken() {
        return this.id;
    }

    @Override
    public Tipo check() throws ASTException {
        //primero es necesario chequear en la tabla de simbolos la clase del contructor
        if(!ts.getClases().containsKey(id.getLexema()))
            throw new ASTException("Error Semantico : La clase "+this.id.getLexema()+" a la que el constructor"
                    + "hace referencia no esta declarada."+this.id.getError());
        
        Clase aux = ts.getClases().get(id.getLexema());
        Ctor c = aux.getConstructor(this.actualArgs.size());
        //Esta excepción no deberia ocurrir nunca pero igual la atajamos
         if(c==null)
            throw new ASTException("Error Semantico : No existe constructor declarado en la clase : "
                                          +aux.getName()+"con la cantidad de parametros : "+this.actualArgs.size()
                                          +this.id.getError());
         //Ahora tengo que chequear que el tipo de los parametros sea coincidente con el de los del constructor
         //Tengo que chequear que se inserten en orden. 
         ArrayList<TipoBase> list = new ArrayList<TipoBase>();
         for(NodoExpresion e : this.actualArgs){
             list.add(e.check());
         }
         
         if(list.size()!=c.getParametros().size())
             throw new ASTException("Error Semantico : La cantidad de parametros que espera el constructor"
                     + "es distinta a la cantidad de parametros con la cual el constructor es invocado"
                     +this.id.getError());
         
         if(!c.igualTipoParametros(list))
             throw new ASTException("Error Semantico : Los tipos con los que se invoca al constructor,"
                     + "no son los esperados por el constructor"+this.id.getError());
        
         
         //retorno el tipo de la clase actual 
         return aux.getTipo();
         
        
        
    }
    
    
}