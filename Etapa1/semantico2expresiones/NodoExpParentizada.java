package semantico2expresiones;

import semantico.TipoBase;
import semantico2.ASTException;
import token.Token;

/**
*
* @author Javier Amorosi
*/
public class NodoExpParentizada extends NodoPrimario {
   private  NodoExpresion expresion;

   public NodoExpresion getExpresion() {
       return expresion;
   }

   public void setExpresion(NodoExpresion expresion) {
       this.expresion = expresion;
   }
   

   @Override
   public Token getToken() {
       return this.expresion.getToken();
   }

   @Override
   public TipoBase check() throws ASTException {
       return this.expresion.check();
   }
   
  
  
}