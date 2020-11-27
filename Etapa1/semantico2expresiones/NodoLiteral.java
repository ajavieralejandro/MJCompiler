package semantico2expresiones;

import semantico.Tipo;
import semantico2.ASTException;
import token.Token;

/*
* @author Javier Amorosi
*/
public class NodoLiteral extends NodoOperando {
   
   private Token operando;
   private Tipo tipo;
   
   //La idea es setear el tipo del literal en el analizador sintáctico
   public void setTipo(Tipo t){
       this.tipo = t;
   }
   
   public void setOperando(Token tk){
       this.operando = tk; 
   }
   


   public Token getToken() {
       return this.operando;
   }


   public Tipo check() throws ASTException {
       return this.tipo;
   }
   
}