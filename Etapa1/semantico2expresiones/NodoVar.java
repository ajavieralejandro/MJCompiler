package semantico2expresiones;

import Claves.ClavesServices;
import semantico.TDS;
import semantico.TipoBase;
import semantico.VarLocal;
import semantico.VariableInstancia;
import semantico2.ASTException;
import semantico2.NodoBloque;
import token.Token;

/**
*
* @author Javier Amorosi
*/
public class NodoVar extends NodoPrimario {
   
   private boolean esLadoIzq;
   private final TDS ts;
   private final NodoBloque actual;
   private Token id;
   
   
   public NodoVar(NodoBloque _actual,Token _id, boolean izq) {
	   this.esLadoIzq = izq;
	   this.ts = TDS.getInstance();
	   this.actual = _actual;
	   this.id = _id;

   }
 

   public boolean isEsLadoIzq() {
       return esLadoIzq;
   }

   public void setEsLadoIzq(boolean esLadoIzq) {
       this.esLadoIzq = esLadoIzq;
   }
 
   
   public NodoVar(Token id,NodoBloque actual){
       this.id = id;
       this.ts = TDS.getInstance();
       this.actual = actual;
   
   }
  
   
   @Override
   public Token getToken() {
       return this.id;
   }

   
   @Override
   public TipoBase check() throws ASTException {
       TipoBase toR = null;
       //Si es una variabla de instancia...
       if(ts.getClaseActual().getVariablesInstancia().containsKey(this.id.getLexema())){
           
           //Si es una variable de instancia tengo que chequear 
           //que el metodo actual no sea estático 
           if(ts.getUnidadActual().getFormaMetodo().equals(ClavesServices.TokenTypes.STATIC.toString()))
               throw new ASTException("Error Semantico : No se pueden hacer uso de variables de instancia"
                       + "en los metodos declarados STATIC."+this.id.getError());
                      
           
           VariableInstancia aux = ts.getClaseActual().getVariablesInstancia().get(this.id.getLexema());
           if(aux.getVisibilidad().equals(ClavesServices.TokenTypes.PRIVATE.toString()) || aux.getVisibilidad().equals("private")){
               if(!ts.getClaseActual().equals(aux.getDeclarada()))
                   throw new ASTException("Error semantico : La variable : "+aux.getNombre()+" no puede ser referencia"
                           + "en la clase actual ya que fue declarada como privada en la clase : "+aux.getDeclarada()
                           +aux.getToken().getError());
               
           }
           
           if(aux.getVisibilidad().equals(ClavesServices.TokenTypes.PROTECTED.toString()) || aux.getVisibilidad().equals("protected")){
               if(!ts.heredaDe(ts.getClaseActual(),aux.getDeclarada()))
                   throw new ASTException("Error semantico : La variable : "+aux.getNombre()+" no puede ser referencia"
                           + "en la clase actual ya que fue declarada como protected en la clase : "+aux.getDeclarada()
                           +" y la clase actual no hereda de ella, "+
                           aux.getToken().getError());
      
           }
           
           toR = aux.getTipo();
  
       }
       
       else{
           //Si no es una variable de instancia la busco entre las variables locales
           if(this.actual.getVariablesLocales().containsKey(this.id.getLexema())){
               VarLocal aux = actual.getVariablesLocales().get(this.id.getLexema());
               toR = aux.getTipo();
               
           }
           else{
               
               //Si no es la busco entre los parametros
               if(ts.getUnidadActual().getParamMap().containsKey(this.id.getLexema())){
                   toR = ts.getUnidadActual().getParamMap().get(this.id.getLexema()).getTipo();
               
               }
               else
                   throw new ASTException("ERROR SEMANTICO : La variable :"+this.id.getLexema()+" a la que se hace referencia no esta declarada"
                          +this.id.getError());
           
           }

       }
       
       if(this.getCadena()!=null){
           return this.getCadena().check(toR);
       }
       else
      return toR;
   }
   
}