package semantico2expresiones;

import semantico.TipoBase;
import semantico2.ASTException;
import token.Token;

/**
*
* @author Javier Amorosi
*/
public abstract class Encadenado {
   private Encadenado cadena;
   private boolean ladoIzq;
 

   public Encadenado(){
       this.cadena = null;
       this.ladoIzq = false;
   }

   public Encadenado getCadena() {
       return cadena;
   }

   public void setCadena(Encadenado cadena) {
       this.cadena = cadena;
   }

   public boolean isLadoIzq() {
       return ladoIzq;
   }

   public void setLadoIzq(boolean ladoIzq) {
       this.ladoIzq = ladoIzq;
   }
   
   public boolean isId(){
       boolean _toR = false;
       //Si tengo cadena entonces retorno la cadena
       if(this.cadena!=null)
           _toR =  this.cadena.isId();
       return _toR;
   }
   
   
   public abstract TipoBase check(TipoBase receptor) throws ASTException;
   
   public abstract Token getId();
   
   
 
   
   
   
}