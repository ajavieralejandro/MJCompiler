/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sintaxis;

import Claves.ClavesServices;
import lexico.Alexico;
import token.Token;
import token.TokenException;

/**
 *
 * @author Javier Amorosi
 */
public class Asintactico {
    
    private Token actual; 
    private Alexico alexico;
    
    public Asintactico(String file)throws TokenException{
            //this.ant = null;
            this.alexico = new Alexico(file);
            
            actual = null;
    }
            
       
    

    
    
    public void analize() throws TokenException,AsintacticoException{
        //Consumo el primer token y empiezo el analisis...
        actual = alexico.nextToken();
        if(this.actual.getType()==ClavesServices.TokenTypes.EOF.ordinal())
        	throw new AsintacticoException("Error: archivo vacio.");
        this.inicial();
        System.out.println("Compilacion exitosa  \n[SinErrores].");

    }
    //Regla 1
    private void inicial() throws AsintacticoException{
    	this.listaClases();
        if(this.actual.getType()!=ClavesServices.TokenTypes.EOF.ordinal())
            throw new AsintacticoException("Error sintactico en linea :+"+this.actual.getLine()+" Se esperaba fin de archivo y no se lo encontro.\n[ERROR:\"+aux+\"|\"+this.buffer.getLine()+\"]");

    }
    //Regla 2
    private void listaClases() throws AsintacticoException{
        ////System.out.println("Regla 2");
        if(this.actual.getType() != ClavesServices.TokenTypes.EOF.ordinal()){
            this.clase();
            this.listaClases();
        }
 
    }
    //Regla 3
    private void clase() throws AsintacticoException{
    
        if(!this.match(ClavesServices.TokenTypes.CLASS.ordinal()))
            throw new AsintacticoException("Error Sintactico en linea : "
        +this.actual.getLine()+"Se esperaba la palabra clave class y se encontro:"+this.actual.getLexema()+"\n[ERROR:"+this.actual.getLexema()+"|"+this.actual.getLine()+"]");
         if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
             throw new AsintacticoException("Error Sintactico : Se esperaba un idClase y se encontro :"+this.actual.getLexema()+"\n[ERROR:\""+this.actual.getLexema()+"|\""+this.actual.getLine()+"]");
         this.herencia();
         if(!this.match(ClavesServices.TokenTypes.PLA.ordinal()))
             throw new AsintacticoException("Error Sintactico : Se esperaba { y se encontro: "+this.actual.getLexema()+this.actual.getError());
        if(this.actual.getType()!=ClavesServices.TokenTypes.EOF.ordinal())
            this.listaMiembros();
        if(!this.match(ClavesServices.TokenTypes.PLC.ordinal()))
            throw new AsintacticoException("Error sintactico: se esperaba llaves Cerradas }"
                    + " del Bloque de Clase y se encontro : "+this.actual.getLexema()+this.actual.getError());
           

       
       
        
 
    }
    //Regla 4    
    private void herencia() throws AsintacticoException{

        if(this.match(ClavesServices.TokenTypes.EXTENDS.ordinal())){
            if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
                throw new AsintacticoException("ERROR SINTACTICO : "
                        + "Se esperaba un idClase luego del extends y se encontro : "+this.actual.getLexema()+this.actual.getError());               
        }

    }
    
    //Regla 5
    private void listaMiembros() throws AsintacticoException{

        //Si lo que viene es distinto a Puntuación Llave que cierra
        if(this.actual.getType()!=ClavesServices.TokenTypes.PLC.ordinal()){
               this.miembro();
               this.listaMiembros();
        }
        
    }
    //Regla 6
    private void miembro() throws AsintacticoException{
               ////System.out.println("miembro");

      
        if(this.actual.getType() == ClavesServices.TokenTypes.PUBLIC.ordinal() 
                || this.actual.getType() == ClavesServices.TokenTypes.PRIVATE.ordinal()
                || this.actual.getType() == ClavesServices.TokenTypes.PROTECTED.ordinal() )
                    this.atributo();
        else
            if(this.actual.getType()== ClavesServices.TokenTypes.idClase.ordinal())
                        this.ctor();
            else if(this.actual.getType() == ClavesServices.TokenTypes.STATIC.ordinal() 
                    || this.actual.getType() == ClavesServices.TokenTypes.DYNAMIC.ordinal())
                        this.metodo();
            else 
                throw new AsintacticoException("Error sintactico: se esperaba protected, public ,private,un idClase ,"
                        + " static o dynamic y se encontro : "+this.actual.getLexema()+this.actual.getError());

    }
    
    
    //Regla 7
    private void atributo()throws AsintacticoException{
        this.visibilidad();
        this.tipo();
        this.listaDecAtrs();
         if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO : se esperaba ; en la declaracion de atributos"
                    + " y se encontro : "+this.actual.getLexema()
                    +this.actual.getError());
        
    
    }
      //Cuando se ingresa a este metodo ya se sabe que el token actual es STATIC O DYNAMIC...
    //Regla 8
    private void metodo() throws AsintacticoException{
        this.formaMetodo();
        this.tipoMetodo();
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO : se esperaba un idMetVar y se encontro : "+this.actual.getLexema()+this.actual.getError());
        this.argsFormales();
        this.bloque();
        
    }

     //Regla 9
    private void ctor() throws AsintacticoException{
        ////System.out.println("ctor");
        if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
            throw new AsintacticoException("Error sintactico se esperaba un idClase y se encontro : ."+this.actual.getLexema()
                    +this.actual.getError());
        this.argsFormales();
        this.bloque();
        
    }
    //Regla 10
   private void visibilidad() throws AsintacticoException{
       
       if(!this.match(ClavesServices.TokenTypes.PUBLIC.ordinal()))
               if(!this.match(ClavesServices.TokenTypes.PRIVATE.ordinal()))
               throw new AsintacticoException("ERROR SINTACTICO :"
                       + " se esperaba public,protected o private y se encontro : "+this.actual.getLexema()+this.actual.getError());
   }
   
   //Regla 11
   private void tipo() throws AsintacticoException{
       if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
           this.tipoPrimitivo();
            
   }
   
   //Regla 12
   private void tipoPrimitivo()throws AsintacticoException{
       
       boolean aux = match(ClavesServices.TokenTypes.INT.ordinal());
       if(!aux)
           aux = match(ClavesServices.TokenTypes.CHAR.ordinal());
       if(!aux)
           aux = match(ClavesServices.TokenTypes.BOOLEAN.ordinal());
       if(!aux)
           aux = match(ClavesServices.TokenTypes.STRING.ordinal());
       if(!aux)
           throw new AsintacticoException("ERROR SINTACTICO:  se Esperaba int char boolean o string"
                   + " y se encontro :  ,"+this.actual.getLexema()+this.actual.getError());

   }
    
    //Regla 13
    private void listaDecAtrs() throws AsintacticoException{
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO : Se esperaba  IdMetVar y se encontro "+this.actual.getLexema()
                    +this.actual.getError());
        this.listaDecAtrsF();
        
    }
    
    //Regla 14
    private void listaDecAtrsF() throws AsintacticoException{
        //Si hace match con la coma
        if(this.match(ClavesServices.TokenTypes.PC.ordinal()))
            this.listaDecAtrs();
        
    }
    
    //Regla 15
    private void  formaMetodo() throws AsintacticoException{
        if(!this.match(ClavesServices.TokenTypes.STATIC.ordinal()))
            if(!this.match(ClavesServices.TokenTypes.DYNAMIC.ordinal()))
                        throw new AsintacticoException("ERROR SINTACTICO :Se esperaba statico o dynamic y se encontro : "+this.actual.getLexema()
                                +this.actual.getError());

    }
    
    //Regla 16
    private void tipoMetodo() throws AsintacticoException{
        
        if(!this.match(ClavesServices.TokenTypes.VOID.ordinal()))
            this.tipo();
    }

    
    //Regla 17
    private void argsFormales() throws AsintacticoException{
                
        if(!this.match(ClavesServices.TokenTypes.PPA.ordinal()))
                    throw new AsintacticoException("ERROR SINTACTICO: Se esperaban parentesis abiertos y se econtro :"+this.actual.getLexema()
                            +this.actual.getError());
        
        this.listaArgsFormalesAux();
        if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                    throw new AsintacticoException("ERROR SINTACTICO: se esperaba ) o , y se encontro : "+this.actual.getLexema()
                           +this.actual.getError());
            
        
    
    }
    
    //Regla 18
    private void listaArgsFormalesAux() throws AsintacticoException{
        //Si lo que vienen no son paréntesis que cierran...
        if(this.actual.getType()!=ClavesServices.TokenTypes.PPC.ordinal())
            this.listaArgsFormales();
     

    }
    //Tengo que considerar el caso en que los argumentos sean vacios...
    //Regla 19
    private void listaArgsFormales() throws AsintacticoException{
        this.argFormal();
        this.listaArgsFormalesF();
        
    
    }
    //Regla 20
    private void listaArgsFormalesF() throws AsintacticoException{
        if(this.match(ClavesServices.TokenTypes.PC.ordinal()))
            this.listaArgsFormales();
    }
 
    // Regla 21
    private void argFormal() throws AsintacticoException{
        this.tipo();
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO : se esperaba idMetVar y se encontro : "+this.actual.getLexema()+this.actual.getError());
    
    }
    //Regla 22
    private void bloque() throws AsintacticoException{
        if(!this.match(ClavesServices.TokenTypes.PLA.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO :  se esperaba } y se encontro : "+this.actual.getLexema()+this.actual.getError());
        this.sentenciaAux();
        if(!this.match(ClavesServices.TokenTypes.PLC.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO :  se esperaba { y se encontro: "+this.actual.getLexema()+this.actual.getError());
        
    
    }
    //Regla 23 a 28
    private void sentencia() throws AsintacticoException{
        if(!this.match(ClavesServices.TokenTypes.PPY.ordinal())){
            if(this.actual.getType()==ClavesServices.TokenTypes.idMetVar.ordinal()){
                this.asignacionLlamada();
                if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
                    throw new AsintacticoException("Error SINTACTICO : se esperaba ; "
                        + "y  se encontro."+this.actual.getLexema()+this.actual.getError());
            }
            //Estoy en siguientes de tipo
            else if(siguientesTipo()){
                this.tipo();
                this.listaDecVars();
                 if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
                    throw new AsintacticoException("Error SINTACTICO : se esperaba ; "
                        + "y se encontro."+this.actual.getLexema()+this.actual.getError());
            }
            //Estoy en siguientes de if
            else if(this.actual.getType()==ClavesServices.TokenTypes.IF.ordinal()){
                        this.sentenciaIf();
            }
            
            //siguientes llamada
            else if(this.siguientesAcceso()){
            	this.asignacionLlamada();
            	
            }
  
            //Estoy en siguientes de While
            else if(this.actual.getType()==ClavesServices.TokenTypes.WHILE.ordinal())
                       this.sentenciaWhile();
            //Estoy en siguientes de bloque
            else if(this.actual.getType()==ClavesServices.TokenTypes.PLA.ordinal())
                        this.bloque();
            //Estoy en siguientes de return
            else if(this.match(ClavesServices.TokenTypes.RETURN.ordinal())){
                       this.ExpresionOVacio();
                       if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
                            throw new AsintacticoException("Error SINTACTICO : en linea :"+this.actual.getLine()+" se esperaba ; "
                                    + "y se encontro : "+this.actual.getLexema()+this.actual.getError())	;
            }
            //Si no entro en ninguno de los anteriores tengo que reportar la excepcion            
            else throw new AsintacticoException("Error SINTACTICO :se esperaba una sentencia, "
                    + "o un ;(sentencia vacia) "
                        +this.actual.getError());
            
        }
    }
    
    //Regla 29
	  private void asignacionLlamada() throws AsintacticoException{
	  	this.acceso();
	  	if(this.siguientesTipoAsignacion()) {
	  		this.tipoAsignacion();
	  		this.expresion();
	  	}
	  		
	  }
	  //Regla 30
	  
	  private void tipoAsignacion() throws AsintacticoException{
		
		  if(!this.match(ClavesServices.TokenTypes.IGUALMAS.ordinal()) 
				  && !this.match(ClavesServices.TokenTypes.IGUAL.ordinal())
				  && !this.match(ClavesServices.TokenTypes.IGUALMENOS.ordinal()))
			  throw new AsintacticoException("Error Sintactico :"+this.actual.getLine()+" se esperaba un operador tipo asignacion y se encontro : "+this.actual.getLexema()+this.actual.getError());
	  }
	  
	  private void llamada() throws AsintacticoException{
		  this.acceso();
	  }
	  
	  
	    //Regla 31
	    private void listaDecVars() throws AsintacticoException{
	        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
	                throw new AsintacticoException("Error Sintactico :"
	                                + " Se esperaba una idMetVar y se la encontro,"+this.actual.getLexema()+this.actual.getError());
	        this.listaDecVarsF(); 
	    }
	    //Regla 32
	    private void listaDecVarsF() throws AsintacticoException{

	        if(this.match(ClavesServices.TokenTypes.PC.ordinal()))
	            this.listaDecVars();
	    }
	    
	    
	    //Regla 34
	    private void expresion() throws AsintacticoException{
	          this.expresionUnaria();
	          this.expresionAux();
	    
	    }
	    
	    private void ExpresionOVacio() throws AsintacticoException{
	    
	    	if(this.siguientesAcceso() || this.siguientesOpUn() || this.siguientesLiteral()){
	    		this.expresion();
	    	}

	    }
	    
	    //Regla 35
	    private void expresionAux() throws AsintacticoException{
	    	
	    	if(this.siguientesOpBinario()) {
	    		this.operadorBinario();
	        	this.expresionUnaria();
	        	this.expresionAux();
	    		
	    	}
	
	        
	       
	        
	    
	    }
	    //Regla 36
	    private void operadorBinario() throws AsintacticoException{
	    	//Esto puede necesitar un refactor
	    	if(!this.match(ClavesServices.TokenTypes.IGUAL.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.AND.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.IGUALIGUAL.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.DIST.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.MAY.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.MEN.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.MENIGUAL.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.MAYIGUAL.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.MAS.ordinal()) 
	    			&& !this.match(ClavesServices.TokenTypes.MENOS.ordinal()) 
	    			&& !this.match(ClavesServices.TokenTypes.POR.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.DIV.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.MOD.ordinal())
	    			
	    			
	    					)
	    		throw new AsintacticoException("Error sintactico : esperaba operador binario");
	    }
	    
	    
	    //Regla 37 y 38
	    private void expresionUnaria() throws AsintacticoException{
	    	
	    	if(this.siguientesOpUn())
	    		this.opUn();
	    	this.operando();
	    }
	    
	    //Regla 39
	    
	    private void opUn() throws AsintacticoException{
        if(!this.match(ClavesServices.TokenTypes.MAS.ordinal()))
                if(!this.match(ClavesServices.TokenTypes.MENOS.ordinal() ))
                  if(!this.match(ClavesServices.TokenTypes.NOT.ordinal()))
                          throw new AsintacticoException("Error Sintactico : Se esperaba +,-,! , y se encontro : "+this.actual.getLexema()+this.actual.getError()); 
        
    
    }
	    

	    //Regla 40
	    private void operando() throws AsintacticoException{
	       if(this.siguientesLiteral())
	    	   this.literal();
	       else
	    	   this.acceso();
	       
	    }
	    
	    //Regla 41
	    private void literal() throws AsintacticoException{
	    	
	         if(!this.match(ClavesServices.TokenTypes.TRUE.ordinal())){
	            if(!this.match(ClavesServices.TokenTypes.FALSE.ordinal())){
	                if(!this.match(ClavesServices.TokenTypes.NUM.ordinal())){
	                    if(!this.match(ClavesServices.TokenTypes.NULL.ordinal())){
	                        if(!this.match(ClavesServices.TokenTypes.charLiteral.ordinal())){
	                            if(!this.match(ClavesServices.TokenTypes.stringLiteral.ordinal()))
	                                throw new AsintacticoException("Error sintactico : se esperaba un TRUE FALSE NUM NULL charLiteral stringLiteral y se encontro :"+this.actual.getLexema()+this.actual.getError());
	                        }
	                    }    
	                }
	            }
	        }
	    }
	    

	    



	    
	   
	       
	  
	  //Regla 42
	  private void acceso() throws AsintacticoException{
		  this.primario();
		  this.encadenado();
	  }
	  
	    
	    //Regla 43 a 46

	    private void primario() throws AsintacticoException{
	        if(this.match(ClavesServices.TokenTypes.PPA.ordinal())){
	            this.expresion();
	            if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
	            throw new AsintacticoException("Error Sintactico, se esperaba),"+this.actual.getError());
	                
	        }
	           
	        else if(this.actual.getType()==ClavesServices.TokenTypes.THIS.ordinal())
	            this.accesoThis();
	        else if(this.actual.getType()==ClavesServices.TokenTypes.idMetVar.ordinal())
	            this.accesoVarMetodo();
	        else if(this.actual.getType()==ClavesServices.TokenTypes.idClase.ordinal())
	            this.llamadaEstatica();
	        else if(this.actual.getType()==ClavesServices.TokenTypes.NEW.ordinal())
	            this.accesoCtor();
	        else if(this.actual.getType()==ClavesServices.TokenTypes.STATIC.ordinal())
	        	this.accesoStatic();
	        else throw new AsintacticoException("Error Sintactico : se esparaba this, idMetVar,idClase, new o static y se encontro : "+this.actual.getLexema()+this.actual.getError());
	        
	    
	    }
	    
	 
	     //Regla 47
	    private void accesoVarMetodo() throws AsintacticoException{
	        
	        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
	            throw new AsintacticoException("Error sintactico: se esperaba un idMetVar y se encontro : "+this.actual.getLexema()+this.actual.getError());
	        if(this.actual.getType()==ClavesServices.TokenTypes.PPA.ordinal())
	            this.argsActuales();
	    
	    }
	    
	    private void accesoStatic() throws AsintacticoException{
	    	if(!this.match(ClavesServices.TokenTypes.STATIC.ordinal()))
	    		throw new AsintacticoException("Error");
	    	if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
	    		throw new AsintacticoException("Error2");
	    	if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
	    		throw new AsintacticoException("Error 3");
	    	this.accesoMetodo();
	    	
	    	
	    	
	    }
	    
	    private void accesoMetodo() throws AsintacticoException{
	    	if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
	    		throw new AsintacticoException("Error 4");
	    	this.argsActuales();
	    	
	    }
	    
	     //Regla 49
	    private void accesoThis() throws AsintacticoException{
	         if(!this.match(ClavesServices.TokenTypes.THIS.ordinal()))
	            throw new AsintacticoException("Error sintactico: se esperaba un THIS y  se encontro,"+this.actual.getLexema()+this.actual.getError());
	        //this.encadenadoAux();
	    }
	    
	    //Regla 51
	    private void llamadaEstatica() throws AsintacticoException{
	        //Espero por un idClase
	        if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
	            throw new AsintacticoException("Error sintactico : "
	                    + "se esperaba un idClase y se encontro :  "+this.actual.getLexema()+this.actual.getError());
	        //Espero por un punto (PUNTUACIÓN PUNTO)
	         if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
	            throw new AsintacticoException("Error sintactico : "
	                    + "se esperaba un '.'(Puntuación Punto)  y se encontro :"+this.actual.getLexema()+this.actual.getError());
	         this.llamadaMetodo();
	        

	            
	    
	    }
	    //Regla 52
	    private void accesoCtor() throws AsintacticoException{
	        if(!this.match(ClavesServices.TokenTypes.NEW.ordinal()))
	            throw new AsintacticoException("Error sintactico : se esperaba new y se encontro :"+this.actual.getLexema()+this.actual.getError());
	        if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
	            throw new AsintacticoException("Error sintactico : se esperaba idClase y se encontro :"+this.actual.getLexema()+this.actual.getError());
	        this.argsActuales();
	    }
	    
	  
 
        


    //Regla 26 
    private void sentenciaAux() throws AsintacticoException{
        
        if(this.actual.getType()!=ClavesServices.TokenTypes.PLC.ordinal() ){
            this.sentencia();
            this.sentenciaAux();
        }
    }
    //Regla 27 28 31 33 34
   
    //Regla 29
    private void sentenciaIf() throws AsintacticoException{
        if(this.match(ClavesServices.TokenTypes.IF.ordinal())){
            if(!this.match(ClavesServices.TokenTypes.PPA.ordinal()))
                throw new AsintacticoException("Error sintactico,se esperaba ( y  se encontro."+this.actual.getLexema()+this.actual.getError());
             this.expresion();
             if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                 throw new AsintacticoException("Error sintactico,se esperaba ) y  se encontro "+this.actual.getLexema()+this.actual.getError());
             this.sentencia();
             this.sentenciaElse();
        }
        else throw new AsintacticoException("Error sintactico,se esperaba if y  se encontro :"+this.actual.getLexema()+this.actual.getError());
    
    }
    //regla 30
    private void sentenciaElse() throws AsintacticoException{
        if(this.match(ClavesServices.TokenTypes.ELSE.ordinal()))
            this.sentencia();

    
    }
    //Regla 32
    private void sentenciaWhile() throws AsintacticoException{
        if(this.match(ClavesServices.TokenTypes.WHILE.ordinal())){
                if(this.match(ClavesServices.TokenTypes.PPA.ordinal())){
                    this.expresion();
                if(this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                    this.sentencia();
                else throw new AsintacticoException("Error sintactico: se esperaba un ) y se encontro:"+this.actual.getLexema()+this.actual.getError());
            }
                else throw new AsintacticoException("Error sintactico: se esperaba un ( y se encontro:"+this.actual.getLexema()+this.actual.getError());
         
        }
        else throw new AsintacticoException("Error sintactico: se esperaba un while y  se encontro."+this.actual.getLexema()+this.actual.getError());
    
    }

    
 

    
    


     //Regla 62
    private void encadenadoAux() throws AsintacticoException{
        //Si lo que viene es un punto llamo a encadenado
        if(this.actual.getType()==ClavesServices.TokenTypes.PP.ordinal())
            this.encadenado();
    
    }

    

    

    
 
      

    
   
    //Regla 71
    private void encadenado() throws AsintacticoException{
       if(this.actual.getType()==ClavesServices.TokenTypes.PP.ordinal()){
    	   this.varOMetodoEncadenado();
    	   this.encadenado();
    	   
       }    	   
    
    }
    
    private void varOMetodoEncadenado() throws AsintacticoException{
        if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
            throw new AsintacticoException("Error sintactico : se esperaba new y se encontro :"+this.actual.getLexema()+this.actual.getError());
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error sintactico : se esperaba idClase y se encontro :"+this.actual.getLexema()+this.actual.getError());
        if(this.actual.getType()==ClavesServices.TokenTypes.PPA.ordinal())
        	this.argsActuales();
    	
    }
    //Regla 72
    private void llamadaOIDEncadenado() throws AsintacticoException{
          if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
            throw new AsintacticoException("Error sintactico: se esperaba un '.'(Puntuación Punto)"
                    + "  se encontro :"+this.actual.getLexema()+this.actual.getError());
          if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error sintactico: se esperaba un 'idMetVar'"
                    + " y se encontro : "+this.actual.getLexema()+this.actual.getError());
         this.argsActualesF();
    
    }
    //Regla 73
    private void argsActualesF() throws AsintacticoException{
        //Si lo que viene son parentesis que abren llamo a argsActuales
        if(this.actual.getType()==ClavesServices.TokenTypes.PPA.ordinal())
            this.argsActuales();
    
    }
    
    //Regla 74
    private void llamadaMetodo() throws AsintacticoException{
            ////System.out.println("llamadaMetodo");
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO: se esparaba idMetVar, y se encontro : "+this.actual.getLexema()+this.actual.getError());
        this.argsActuales();
        //this.encadenadoAux();
        
    }
    //Regla 75  
    private void argsActuales() throws AsintacticoException{
            ////System.out.println("argsActuales");
        if(!this.match(ClavesServices.TokenTypes.PPA.ordinal()))
            throw new AsintacticoException("Error Sintactico: Se esperaba"
                    + "( , y se encontro : "+this.actual.getLexema()+this.actual.getError());
        this.listaExpresionAux();
        if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                throw new AsintacticoException("Error Sintactico: Se esperaba ) , "
                    + "y se encontro : "+this.actual.getLexema()+this.actual.getError());
        
       

    }
    //Regla 76
    private void listaExpresionAux() throws AsintacticoException{
        //Si leo parentesis que cierran entonces es vacio 
        if(this.actual.getType()!=ClavesServices.TokenTypes.PPC.ordinal())
            this.listaExpresion();
    
    }
    
    //Regla 77
    private void listaExpresion() throws AsintacticoException{
            ////System.out.println("listaExpresion");
        this.expresion();
        if(this.match(ClavesServices.TokenTypes.PC.ordinal())){
            this.listaExpresion();
            this.listaExpresionF();
        }
    }
    
    //Regla 78
    private void listaExpresionF() throws AsintacticoException{
        if(this.match(ClavesServices.TokenTypes.PC.ordinal()))
            this.listaExpresion();
    
    }
    
    

   

    
    private boolean siguientesTipo(){
        return (this.actual.getType()== ClavesServices.TokenTypes.BOOLEAN.ordinal() ||
                this.actual.getType()== ClavesServices.TokenTypes.CHAR.ordinal() ||
                this.actual.getType()== ClavesServices.TokenTypes.INT.ordinal()
                || this.actual.getType()== ClavesServices.TokenTypes.idClase.ordinal() ||
                this.actual.getType()== ClavesServices.TokenTypes.STRING.ordinal()
                ||this.actual.getType()== ClavesServices.TokenTypes.VOID.ordinal());
       
    }
    
        /**
     * Actualizo el Token Actual, imprimo el mensaje en caso de darse una excepciÃ³n
     */
    
    private void consumir(){
        try{
                this.actual = this.alexico.nextToken();
                        
            }
        catch(TokenException e){
            //System.out.print(e.getMessage());
        }
    }
    /**
     * Comparo los tipos de Tokens y si hacen match actualiza el Token Actual
     * @param aux tipo de Token 
     * @return si hizo match y consumio
     */
    
    private boolean match(int aux){
        boolean toR =  (this.actual.getType()== aux);
        if(toR)
            //this.ant = actual;
            this.consumir();
        
        return toR;
    }
    
    private boolean siguientesTipoAsignacion() {
    	int aux = this.actual.getType();
    	return(aux==ClavesServices.TokenTypes.IGUAL.ordinal()
    			|| aux==ClavesServices.TokenTypes.IGUALMAS.ordinal()
    			|| aux==ClavesServices.TokenTypes.IGUALMENOS.ordinal());
    			
    }

    
    private boolean siguientesOpUn(){
    	return this.actual.getType()==ClavesServices.TokenTypes.MAS.ordinal() ||
    	           this.actual.getType()==ClavesServices.TokenTypes.MENOS.ordinal() || 
    	            this.actual.getType()==ClavesServices.TokenTypes.NOT.ordinal();
    	
    }
    
    private boolean siguientesAcceso(){
    	int _toCompare = this.actual.getType();
    	return (_toCompare==ClavesServices.TokenTypes.THIS.ordinal() 
    			|| _toCompare==ClavesServices.TokenTypes.idMetVar.ordinal()
    			|| _toCompare==ClavesServices.TokenTypes.STATIC.ordinal()
    			|| _toCompare==ClavesServices.TokenTypes.NEW.ordinal()
    			|| _toCompare==ClavesServices.TokenTypes.PPA.ordinal()
    			);
    }
    
    private boolean siguientesLiteral() {
    	boolean toR = false;
    	int _toCompare= this.actual.getType();
    	if(_toCompare == ClavesServices.TokenTypes.NUM.ordinal())
    		toR = true;
    	else if(_toCompare == ClavesServices.TokenTypes.charLiteral.ordinal())
    		toR = true;
    	else if(_toCompare == ClavesServices.TokenTypes.stringLiteral.ordinal())
        	toR = true;
    	else if(_toCompare == ClavesServices.TokenTypes.FALSE.ordinal())
        	toR = true;
    	else if(_toCompare == ClavesServices.TokenTypes.TRUE.ordinal())
        	toR = true;
    	else if(_toCompare == ClavesServices.TokenTypes.NULL.ordinal())
        	toR = true;
    	return toR;
    }
    private boolean siguientesOpBinario() {
    	int _toCompare = this.actual.getType();
    	return(_toCompare==ClavesServices.TokenTypes.OR.ordinal()
    			|| _toCompare==ClavesServices.TokenTypes.AND.ordinal()
    			|| _toCompare==ClavesServices.TokenTypes.IGUALIGUAL.ordinal()
    			|| _toCompare==ClavesServices.TokenTypes.DIST.ordinal()
    			|| _toCompare==ClavesServices.TokenTypes.MAY.ordinal()
    			|| _toCompare==ClavesServices.TokenTypes.MEN.ordinal()
    			|| _toCompare==ClavesServices.TokenTypes.MAYIGUAL.ordinal()
    	    	|| _toCompare==ClavesServices.TokenTypes.MENIGUAL.ordinal()
    	    	|| _toCompare==ClavesServices.TokenTypes.MAS.ordinal()
    	    	|| _toCompare==ClavesServices.TokenTypes.MENOS.ordinal()
    	    	|| _toCompare==ClavesServices.TokenTypes.POR.ordinal()
    	    	|| _toCompare==ClavesServices.TokenTypes.DIV.ordinal()
    	    	|| _toCompare==ClavesServices.TokenTypes.MOD.ordinal()
    			);
    	
    	
    }
    


}