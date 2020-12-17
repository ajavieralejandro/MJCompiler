package semantico2expresiones;

import Claves.ClavesServices;
import semantico.Tipo;
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
       System.out.println("Estoy por hacer el check de nodoExpresionBinaria");
       
       System.out.println("El operador es  : "+this.operador.getLexema());
    

    

       
       int aux = this.operador.getType();
       if(aux==ClavesServices.TokenTypes.MAS.ordinal()
          || aux==ClavesServices.TokenTypes.MENOS.ordinal()
          || aux== ClavesServices.TokenTypes.POR.ordinal()
          || aux == ClavesServices.TokenTypes.DIV.ordinal()
          || aux == ClavesServices.TokenTypes.MOD.ordinal()){
           
           System.out.println("Tengo que estar en este caso con MAS");
          
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
       
       System.out.println("Me estoy fijando que todo sea compatible con int");
       //System.out.println("El tipo del lado derecho es : "+this.Lder.check().getTipo());
       //System.out.println("El tipo del lado izquierdo es : "+this.Lizq.check().getTipo());
       
       TipoBase _tipoder = this.Lder.check();
       TipoBase _tipoizq = this.Lizq.check();
       
       System.out.println("El lado izquierdo es de tipo : "+_tipoizq.getTipo());
      
       System.out.println("El operador es  : "+this.operador.getLexema());
       if(this.Lder instanceof NodoLiteral)
           System.out.println("El lado derecho es instancia de nodoLiteral");
       if(this.Lder instanceof NodoExpUnaria){
           System.out.println("El lado derecho es instancia de ExpresionUnaria");
           NodoExpUnaria _test = (NodoExpUnaria) this.Lder;
           System.out.println("El token de la expresion Unaria es :"+_test.getToken());
           System.out.println("El operador es  :"+_test.getOperador());
           System.out.print("El tipo de la  expresion es :"+_test.getExpresion().check().getTipo());
       
       }
    
            
           //Tal vez se podria optimizar esto... 
           if(!_tipoder.esCompatible(new TipoInt())){
                   throw new ASTException("Error semantico: el operador "+this.operador.getLexema()
                   +" trabraja con subexpresiones int del lado derecho y encontro : "+_tipoder.getTipo()+" en el lado izquierdo."
                   +this.Lizq.getToken().getError());
           }
           
                       
           if(!this.Lizq.check().esCompatible(new TipoInt()))
                   throw new ASTException("Error semantico: el operador "+this.operador.getLexema()
                   +" trabraja con subexpresiones int del lado izquierdo y encontro : "+this.Lizq.check().getTipo()+" subexpresion incorrecta."
                		   +this.Lizq.getToken().getError());
           
           //Todavía no computo el resultado...
           //Refactorear
   }
   
}