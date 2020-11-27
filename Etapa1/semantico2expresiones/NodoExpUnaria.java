package semantico2expresiones;

import Claves.ClavesServices;
import semantico.TipoBase;
import semantico.TipoBoolean;
import semantico.TipoInt;
import semantico2.ASTException;
import token.Token;

/**
*
* @author Javier Amorosi
*/
public class NodoExpUnaria extends NodoExpresion {
   
   private Token operador;
   private NodoExpresion expresion;
   

   public Token getOperador() {
       return operador;
   }

   public void setOperador(Token operador){
       this.operador = operador;
   }

   public NodoExpresion getExpresion() {
       return expresion;
   }

   public void setExpresion(NodoExpresion expresion) {
       this.expresion = expresion;
   }
   

   //Se asume que el token a enviar es el operador
   @Override
   public Token getToken() {
       return this.operador;
   }

   @Override
   public TipoBase check() throws ASTException {
       
       TipoBase toR = null;
       int tipoOperador = this.operador.getType();

       //Si el operador es un mas o un menos
       if(tipoOperador == ClavesServices.TokenTypes.MAS.ordinal() ||
               tipoOperador == ClavesServices.TokenTypes.MENOS.ordinal()){
           //Entonces la subexpresion deberan ser int y debo retornar int
           toR = this.expresion.check();
           if(!toR.esCompatible(new TipoInt()))
               throw new ASTException("Error semantico: Se esperaba una subexpresion de tipo Int."
              +this.operador.getError());
          
       }
       if(tipoOperador == ClavesServices.TokenTypes.NOT.ordinal()){
           toR = this.expresion.check();
           if(!toR.esCompatible(new TipoBoolean()))
                 throw new ASTException("Error semantico: el operador ! espero una subexpresion de tipo boolean "
                 +this.operador.getError());
       }
       return toR;
       
   }
   
}