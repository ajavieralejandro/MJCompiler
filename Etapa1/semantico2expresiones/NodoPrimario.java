package semantico2expresiones;

import semantico.TipoBase;
import semantico2.ASTException;
import token.Token;

/**
*
* @author Javier Amorosi
*/
public abstract class NodoPrimario extends NodoOperando {
   
   private Encadenado cadena;

   public Encadenado getCadena() {
       return cadena;
   }

   public void setCadena(Encadenado cadena) {
       this.cadena = cadena;
   }
   
  
   
}