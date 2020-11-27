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
public class NodoExpBinaria extends NodoExpresion {
   
   private Token operador;
   private NodoExpresion Lizq;;
   private NodoExpresion Lder;


   public Token getOperador() {
       return operador;
   }

   public void setOperador(Token operador) {
       this.operador = operador;
   }

   public NodoExpresion getLizq() {
       return Lizq;
   }

   public void setLizq(NodoExpresion Lizq) {
       this.Lizq = Lizq;
   }

   public NodoExpresion getLder() {
       return Lder;
   }

   public void setLder(NodoExpresion Lder) {
       this.Lder = Lder;
   }
   
   @Override
   public TipoBase check() throws ASTException {
       int aux = this.operador.getType();
       if(aux==ClavesServices.TokenTypes.MAS.ordinal()
          || aux==ClavesServices.TokenTypes.MENOS.ordinal()
          || aux== ClavesServices.TokenTypes.POR.ordinal()
          || aux == ClavesServices.TokenTypes.DIV.ordinal()
          || aux == ClavesServices.TokenTypes.MOD.ordinal()){
          
           //chequeo que ambos lados sean de tipo int...
           this.checkInt();
           return new TipoInt();

          
       }
      
       if(aux == ClavesServices.TokenTypes.AND.ordinal()
               || aux == ClavesServices.TokenTypes.OR.ordinal()){
           TipoBase tr =this.Lder.check();
           TipoBase tl = this.Lizq.check();
           if(!tr.esCompatible(new TipoBoolean()))
               throw new ASTException("Error Semantico: el operador : "+this.operador.getLexema()
               +" trabaja con subexpresiones boolean del lado derecho y encontro :"+tr.getTipo()
               +tr.getToken().getError());
           
             if(!tl.esCompatible(new TipoBoolean()))
               throw new ASTException("Error Semantico: el operador : "+this.operador.getLexema()
               +" trabaja con subexpresiones boolean del lado derecho y encontro :"+tl.getTipo()
               +tl.getToken().getError());
             
             return new TipoBoolean();    
       }
       
       if(aux==ClavesServices.TokenTypes.IGUALIGUAL.ordinal() || 
               aux==ClavesServices.TokenTypes.NOT.ordinal()){
           //Trabajan con cualquier relacion de tipos así que no hay chequeos    
           return new TipoBoolean();
       
       }
       
       if(aux==ClavesServices.TokenTypes.MAY.ordinal()
          || aux==ClavesServices.TokenTypes.MAYIGUAL.ordinal()
          || aux==ClavesServices.TokenTypes.MEN.ordinal()
          || aux==ClavesServices.TokenTypes.MENIGUAL.ordinal()){
           
           //Chequeo que ambos lados sean de tipo Int. 
           this.checkInt();
           return new TipoBoolean();
           
           
       }
      
       //Tengo que tener cuidado con esto... tal vez sea mejor retornar el tipo void. 
       throw new UnsupportedOperationException("Error semantico : operador no asociado a ningun tipo valido "+this.operador.getError()); //To change body of generated methods, choose Tools | Templates.
   }
   


   @Override
   public Token getToken() {
       return this.operador;
   }
   
   private void checkInt() throws ASTException{
            
           //Tal vez se podria optimizar esto... 
           if(!this.Lder.check().esCompatible(new TipoInt()))
                   throw new ASTException("Error semantico: el operador "+this.operador.getLexema()
                   +" trabraja con subexpresiones int del lado derecho y encontro : "+this.Lder.check().getTipo()+", subexpresion incorrecta."
                   +this.Lizq.getToken().getError());
                       
           if(!this.Lizq.check().esCompatible(new TipoInt()))
                   throw new ASTException("Error semantico: el operador "+this.operador.getLexema()
                   +" trabraja con subexpresiones int del lado izquierdo y encontro : "+this.Lder.check().getTipo()+" subexpresion incorrecta."
                		   +this.Lder.getToken().getError());
           
           //Todavía no computo el resultado...
           //Refactorear
   }
   
}