package semantico2expresiones;

import java.util.Map;

import Claves.ClavesServices;
import semantico.Clase;
import semantico.TDS;
import semantico.TipoBase;
import semantico.VariableInstancia;
import semantico2.ASTException;
import token.Token;

/**
*
* @author Javier Amorosi
*/
public class NodoIdEncadenado extends Encadenado {
   
   private final Token id;
   private final TDS ts;
   
   public NodoIdEncadenado(Token id){
       super();
       this.id = id;
       this.ts = TDS.getInstance();
   }
   

   @Override
   public TipoBase check(TipoBase receptor) throws ASTException{
       
       Map<String,Clase> map = ts.getClases();
       TipoBase toR = null;
       //Si el tipo receptor no es de tipo clase...
       if(!map.containsKey(receptor.getTipo()))
           throw new ASTException("Error semantico : El tipo receptor : "+receptor.getTipo()+ "no es de tipo clase."
           +receptor.getToken().getError());
       
       //ahora me tengo que fijar que el id corresponda a una variable de instancia de la clase
       Clase aux = map.get(receptor.getTipo());
       if(!aux.getVariablesInstancia().containsKey(id.getLexema()))
           throw new ASTException("Error Semantico : el id  "+this.id.getLexema()+"no es una variabla de instancia de"
                   + "la clase :"+receptor.getTipo()+this.id.getError());
       else{
           //ahora que se que existe el id tengo que chequear la visibilidad 
           VariableInstancia var =  aux.getVariablesInstancia().get(this.id.getLexema());
           toR = var.getTipo();
           if((var.getVisibilidad().equals(ClavesServices.TokenTypes.PRIVATE.toString()) || var.getVisibilidad().equals("private"))
                   && !this.ts.getClaseActual().getName().equals(var.getDeclarada().getName()))
               throw new ASTException("Error Semantico : el id  "+this.id.getLexema()+"es una variable privada"
                   + "de la clase :"+receptor.getTipo()+" y no puede ser referenciada."+this.id.getError());
           //Si la variable es protejida me tengo que fijar que la clase actual sea decendiente
           if(var.getVisibilidad().equals(ClavesServices.TokenTypes.PROTECTED.toString())|| var.getVisibilidad().equals("protected")){
               //Si la clase actual no hereda del tipo al que hago referencia no puedo usarlo...
               if(!ts.heredaDe(aux, ts.getClaseActual()))
                   throw new ASTException("Error Semantico : el id  "+this.id.getLexema()+"es una variable protected"
                   + "de la clase :"+receptor.getTipo()+" y no puede ser referenciada, en la clase actual. "+this.id.getError());
           }
      
       }        
       //Ahora paso todos los chequeos de tipos
       //chequeo el encadenado con el tipo clase actual...
       
       //Chequear esta condición... 
       if(this.getCadena()!=null)
           return this.getCadena().check(toR);
       else
           return toR;
                   
                   
       
       
   }

   @Override
   public Token getId() {
       return this.id;
   }
   
}