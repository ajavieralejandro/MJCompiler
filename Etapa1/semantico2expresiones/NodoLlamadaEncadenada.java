package semantico2expresiones;

import java.util.ArrayList;
import java.util.Map;

import semantico.Metodo;
import semantico.TDS;
import semantico.TipoBase;
import semantico2.ASTException;
import token.Token;

/**
*
* @author Javier Amorosi
*/
public class NodoLlamadaEncadenada extends Encadenado {
   
   private Token id;
   private final ArrayList<NodoExpresion> actualArgs;
   private final TDS ts;
   
   public NodoLlamadaEncadenada(Token id,ArrayList<NodoExpresion> actualArgs){
       super();
       this.id = id;
       this.ts = TDS.getInstance();
       this.actualArgs = actualArgs;
   }
   
   

   @Override
   public TipoBase check(TipoBase receptor) throws ASTException {
	   System.out.println("NodoLlamadaEncadenada");
       //tengo que chequear el tipo de los argumentos actuales
       //tengo que chequear que la llamada pertenezca a la clase del tipo receptor
       //tengo que llamar al encadenado con el tipo de la llamada si tengo encadenado...
       
       //obtengo los metodos actuales 
       Map<String,Metodo> metodos = ts.getClases().get(receptor.getTipo()).getMetodos();
 
       if(!metodos.containsKey(id.getLexema()))
           throw new ASTException("Error Semantico : La llamada : "+id.getLexema()+" no corresponde a un metodo"
                   + "de la clase : "+receptor.getTipo()+id.getError());
             
       
       Metodo aux = metodos.get(id.getLexema());
       if(aux.isVoid() && this.isLadoIzq())
             throw new ASTException("Error semantico : La llamada : "+id.getLexema()+" es de tipo void"
                   + "por ende no puede tener encadenado."+id.getError()+id.getError());
               
       //Ahora tengo que chequear los argumentos
           //chequeo los arguementos
        ArrayList<TipoBase> list = new ArrayList<TipoBase>();
        for(NodoExpresion e : this.actualArgs){
            list.add(e.check());
        }
        
      
        if(list.size()!=aux.getParametros().size())
            throw new ASTException("Error Semantico : La cantidad de parametros que espera el metodo:"+aux.getNombre()+
                    "es distinta a la cantidad de parametros con la cual el metodo es invocado"+this.id.getError());
        
           if(!aux.igualTipoParametros(list))
            throw new ASTException("Error Semantico : Los tipos con los que se invoca al constructor,"
                    + "no son los esperados por el constructor"+this.id.getError());
    
        
       //Casteo porque ya tengo seguridad que no es de tipo void
       if(this.getCadena()!=null)
           return this.getCadena().check(aux.getRetorno());
       else 
           return aux.getRetorno();
       
       
       
       
       
   }

   @Override
   public Token getId() {
       return id;
   }
   
}