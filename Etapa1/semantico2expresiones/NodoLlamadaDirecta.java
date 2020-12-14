package semantico2expresiones;

import java.util.ArrayList;

import semantico.Metodo;
import semantico.TDS;
import semantico.TipoBase;
import semantico2.ASTException;
import token.Token;

public class NodoLlamadaDirecta extends NodoPrimario {
    
    private final Token id;
    private ArrayList<NodoExpresion> actualArgs;
    private final TDS ts;
    
    public NodoLlamadaDirecta(Token id){
        this.id = id;
        this.ts = TDS.getInstance();
    }
    
    public void setActualArgs(ArrayList<NodoExpresion> array){
        this.actualArgs = array;
    }
    
    public ArrayList<NodoExpresion> getActualArgs(){
    	return this.actualArgs;
    }
    
    

    @Override
    public Token getToken() {
        return this.id;
    }
    

    @Override
    public TipoBase check() throws ASTException {
  
        Metodo actual = this.ts.getClaseActual().getMetodos().get(this.id.getLexema());
        if(actual==null)
            throw new ASTException("Error semantico : El metodo que se esta invocando no se encuentra en la clase actual.  "
                                    +this.id.getError());
        //chequeo los arguementos
         ArrayList<TipoBase> list = new ArrayList<TipoBase>();
         for(NodoExpresion e : this.actualArgs){
             list.add(e.check());
         }
         
         
         if(list.size()!=actual.getParametros().size())
             throw new ASTException("Error semantico : La cantidad de parametros que espera el metodo"
                     + "es distinta a la cantidad de parametros con la cual el metodo es invocado. "
                      + this.id.getError());
         
            if(!actual.igualTipoParametros(list))
             throw new ASTException("Error Semantico : Los tipos con los que se invoca al constructor,"
                     + "no son los esperados por el constructor."+this.id.getError());
        
            return actual.getRetorno();
         
         
        
        
    }
    
}