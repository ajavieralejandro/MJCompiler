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
    
    //private Token ant;
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
        this.inicial();
        System.out.println("El analizador sintactico finalizo sin errores.");

    }
    //Regla 1
    private void inicial() throws AsintacticoException{
    	this.listaClases();
        if(this.actual.getType()!=ClavesServices.TokenTypes.EOF.ordinal())
            throw new AsintacticoException("Se esperaba fin de archivo y no se lo encontro.");

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
            throw new AsintacticoException("Se esperaba la palabra clave class y,"
                    + ""+this.actual.getError());
         if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
             throw new AsintacticoException("Se esperaba un idClase y,"
                     + ""+this.actual.getError());
         this.herencia();
         if(!this.match(ClavesServices.TokenTypes.PLA.ordinal()))
             throw new AsintacticoException("Error Sintactico : Se esperaba { y "
                     + ","+this.actual.getError());
        if(this.actual.getType()!=ClavesServices.TokenTypes.EOF.ordinal())
            this.listaMiembros();
        if(!this.match(ClavesServices.TokenTypes.PLC.ordinal()))
            throw new AsintacticoException("Error sintactico se esperaba llaves Cerradas }"
                    + " del Bloque de Clase y ,"+this.actual.getError());
           

       
       
        
 
    }
    //Regla 4    
    private void herencia() throws AsintacticoException{
              ////System.out.println("Herencia");

        //Si lo que viene es un EXTENDS...
        if(this.match(ClavesServices.TokenTypes.EXTENDS.ordinal())){
            if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
                throw new AsintacticoException("ERROR SINTACTICO : "
                        + "Se esperaba un idClase luego del extends..."+this.actual.getError());               
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
                throw new AsintacticoException("Error sintactico se esperaba protected, public ,private,un idClase ,"
                        + " static o dynamic y no se encontro ninguno"+this.actual.getError());

    }
    
    
    //Regla 7
    private void atributo()throws AsintacticoException{
        this.visibilidad();
        this.tipo();
        this.listaDecAtrs();
         if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO : se esperaba ; en la declaracion de atributos"
                    + "    y no se encontro ninguno."
                    +this.actual.getError());
        
    
    }
      //Cuando se ingresa a este metodo ya se sabe que el token actual es STATIC O DYNAMIC...
    //Regla 8
    private void metodo() throws AsintacticoException{
        this.formaMetodo();
        this.tipoMetodo();
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO : se esperaba un idMetVar y no se encontro ninguno."+this.actual.getError());
        this.argsFormales();
        this.bloque();
        
    }

     //Regla 9
    private void ctor() throws AsintacticoException{
        ////System.out.println("ctor");
        if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
            throw new AsintacticoException("Error sintactico se esperaba un idClase y no se ha encontrado."
                    +this.actual.getError());
        this.argsFormales();
        this.bloque();
        
    }
    //Regla 10
   private void visibilidad() throws AsintacticoException{
       
       if(!this.match(ClavesServices.TokenTypes.PUBLIC.ordinal()))
               if(!this.match(ClavesServices.TokenTypes.PRIVATE.ordinal()))
               throw new AsintacticoException("ERROR SINTACTICO :"
                       + " se esperaba public,protected o private."+this.actual.getError());
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
                   + "(Tipo del argumento) ,"+this.actual.getError());
       //this.array();

   }
    
    //Regla 13
    private void listaDecAtrs() throws AsintacticoException{
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO : SE ESPERABA IdMetVar "
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
                        throw new AsintacticoException("ERROR SINTACTICO : SE ESPERABA statico o dynamic"
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
                    throw new AsintacticoException("ERROR SINTACTICO SE ESPERABAN PARENTESIS ABIERTOS"
                            + " Y NO SE ENCONTRARON"+this.actual.getError());
        
        this.listaArgsFormalesAux();
        if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                    throw new AsintacticoException("ERROR SINTACTICO SE ESPERABAN PARENTESIS CERRADOS o , "
                            + " Y NO SE ENCONTRO!"+this.actual.getError());
            
        
    
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
            throw new AsintacticoException("Se esperaba un idMetVar y no se encontro ninguno, error sintactico."+this.actual.getError());
    
    }
    //Regla 22
    private void bloque() throws AsintacticoException{
        //System.out.println("bloque");
        if(!this.match(ClavesServices.TokenTypes.PLA.ordinal()))
            throw new AsintacticoException("Error sintactico se esperaba } y no se encontro."+this.actual.getError());
        this.sentenciaAux();
        if(!this.match(ClavesServices.TokenTypes.PLC.ordinal()))
            throw new AsintacticoException("Error sintactico se esperaba { y no se encontro."+this.actual.getError());
        
    
    }
    
    private void sentencia() throws AsintacticoException{
        //System.out.println("Sentencia");
        //System.out.println("El actual de sentencia es : "+this.actual.getLexema());
        //Si no hago match con punto y coma...
        if(!this.match(ClavesServices.TokenTypes.PPY.ordinal())){
            //Esta en siguientes de Asignación
            if(this.actual.getType()==ClavesServices.TokenTypes.idMetVar.ordinal()){
                //System.out.println("Estoy en este caso de sentencia");
                this.asignacionLlamada();
                if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
                    throw new AsintacticoException("Error SINTACTICO : se esperaba ; "
                        + "y no se encontro."+this.actual.getError());
            }
       
            }
            //Estoy en siguientes de tipo
            else if(siguientesTipo()){
                this.tipo();
                this.listaDecVars();
                 if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
                    throw new AsintacticoException("Error SINTACTICO : se esperaba ; "
                        + "y no se encontro."+this.actual.getError());
            }
            //Estoy en siguientes de if
            else if(this.actual.getType()==ClavesServices.TokenTypes.IF.ordinal()){
                //System.out.println("Estoy en el if de sentencia...");
                        this.sentenciaIf();
            }
            //Estoy en siguientes de While
            else if(this.actual.getType()==ClavesServices.TokenTypes.WHILE.ordinal())
                       this.sentenciaWhile();
            //Estoy en siguientes de bloque
            else if(this.actual.getType()==ClavesServices.TokenTypes.PLA.ordinal())
                        this.bloque();
            //Estoy en siguientes de return
            //Chequear POSIBLES PROBLEMAS CON MATCH
            else if(this.match(ClavesServices.TokenTypes.RETURN.ordinal())){
                       //System.out.println("Estoy en un return y lo encontre correctamente");
                       //System.out.println("El actual de sentencia es : "+this.actual.getLexema());
                       this.expresionAux();
                       if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
                            throw new AsintacticoException("Error SINTACTICO : se esperaba ; "
                                    + "y no se encontro."+this.actual.getError());
            }
            //Si no entro en ninguno de los anteriores tengo que reportar la excepcion            
            else throw new AsintacticoException("Error SINTACTICO :se esperaba una sentencia, "
                    + "o un ;(sentencia vacia) "
                        +this.actual.getError());
            
        }
    
    //Regla 23
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
				  || !this.match(ClavesServices.TokenTypes.IGUALIGUAL.ordinal())
				  || !this.match(ClavesServices.TokenTypes.IGUALMENOS.ordinal()))
			  throw new AsintacticoException("Error Sintactico : se esperaba un operador tipo asignacion");
	  }
	  
	  
	    //Regla 31
	    private void listaDecVars() throws AsintacticoException{
	            ////System.out.println("listaDecVars");
	        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
	                throw new AsintacticoException("Error Sintactico :"
	                                + " Se esperaba una idMetVar y no se la encontro,"+this.actual.getError());
	        this.listaDecVarsF(); 
	    }
	    //Regla 32
	    private void listaDecVarsF() throws AsintacticoException{
	        //        ////System.out.println("ListaDecVarsAux");
	        //Controlo Si existe el match con una coma... 
	        if(this.match(ClavesServices.TokenTypes.PC.ordinal()))
	            this.listaDecVars();
	    }
	    
	   
	       
	  
	  //Regla 42
	  private void acceso() throws AsintacticoException{
		  this.primario();
		  this.encadenado();
	  }
	  
 
        


    //Regla 26 
    private void sentenciaAux() throws AsintacticoException{
        //NO ESTOY EN EL CASO DE QUE SEA VACIO!!
        //System.out.println("sentencia Aux");
        if(this.actual.getType()!=ClavesServices.TokenTypes.PLC.ordinal() ){
            this.sentencia();
            this.sentenciaAux();
        }
    }
    //Regla 27 28 31 33 34
   
    //Regla 29
    private void sentenciaIf() throws AsintacticoException{
            ////System.out.println("sentenciaIf");
        if(this.match(ClavesServices.TokenTypes.IF.ordinal())){
            if(!this.match(ClavesServices.TokenTypes.PPA.ordinal()))
                throw new AsintacticoException("Error sintactico,se esperaba ( y no se encontro."+this.actual.getError());
            this.expresion();
             if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                 throw new AsintacticoException("Error sintactico,se esperaba ) y no se encontro."+this.actual.getError());
             this.sentencia();
             this.sentenciaElse();
        }
        else throw new AsintacticoException("Error sintactico,se esperaba if y no se encontro."+this.actual.getError());
    
    }
    //regla 30
    private void sentenciaElse() throws AsintacticoException{
            ////System.out.println("sentenciaElse");
        if(this.match(ClavesServices.TokenTypes.ELSE.ordinal()))
            this.sentencia();

    
    }
    //Regla 32
    private void sentenciaWhile() throws AsintacticoException{
            ////System.out.println("sentenciaWhile");
        if(this.match(ClavesServices.TokenTypes.WHILE.ordinal())){
                if(this.match(ClavesServices.TokenTypes.PPA.ordinal())){
                    this.expresion();
                if(this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                    this.sentencia();
                else throw new AsintacticoException("Error sintactico: se esperaba un ) y no se encontro,sentencia while mal formada."+this.actual.getError());
            }
                else throw new AsintacticoException("Error sintactico: se esperaba un ( y no se encontro,sentencia while mal formada."+this.actual.getError());
         
        }
        else throw new AsintacticoException("Error sintactico: se esperaba un while y no se encontro."+this.actual.getError());
    
    }
    //Regla 34
    private void expresion() throws AsintacticoException{
          this.expresionUnaria();
          this.expresionAux();
    
    }
    
    //Regla 37 y 38
    private void expresionUnaria() throws AsintacticoException{
    	if(this.siguientesOpUn())
    		this.opUn();
    	
    	this.operando();
    }
    
    //Regla 35
    private void expresionAux() throws AsintacticoException{
        //cadena no vacia
        if(this.actual.getType()!=ClavesServices.TokenTypes.PPY.ordinal())
            this.expresion();
        
    
    }
    //Regla 36
    private void operadorBinario() throws AsintacticoException{
    	if(!this.match(ClavesServices.TokenTypes.IGUAL.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.AND.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.IGUALIGUAL.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.DIST.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.MAY.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.MEN.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.MENIGUAL.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.MAYIGUAL.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.MAS.ordinal()) 
    			|| !this.match(ClavesServices.TokenTypes.MENOS.ordinal()) 
    			|| !this.match(ClavesServices.TokenTypes.POR.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.DIV.ordinal())
    			|| !this.match(ClavesServices.TokenTypes.MOD.ordinal())
    			
    			
    					)
    		throw new AsintacticoException("Error sintactico : esperaba operador binario");
    }
    



    //Regla 38
    private void idEncadenadoAux() throws AsintacticoException{
        if(this.actual.getType()==ClavesServices.TokenTypes.PP.ordinal()){
            this.idEncadenado();
            this.idEncadenadoAux();
        }
            
    
    }
    
    //Regla 39   
    private void idEncadenado() throws AsintacticoException{
        
        if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
            throw new AsintacticoException("Error sintactico : se esperaba '.' , "
                    + ""+this.actual.getError());
         if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error sintactico : se esperaba idMetVar , "
                    + ""+this.actual.getError());
        

    
    }
    
      //Regla 40
    private void llamada() throws AsintacticoException{
        
        if(!this.match(ClavesServices.TokenTypes.PPA.ordinal()))
            throw new AsintacticoException("ERROR SINTACTICO, se esperaba ( y,"+this.actual.getError());
        this.expresion();
        if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
            throw new AsintacticoException("Error Sintactico, se esperaba ),"+this.actual.getError());
    
    }
    

    //Regla 40
    private void operando() throws AsintacticoException{
       this.literal();
       
    }
    
    //Regla 41
    private void literal() throws AsintacticoException{
            ////System.out.println("literal");
         if(!this.match(ClavesServices.TokenTypes.TRUE.ordinal())){
            if(!this.match(ClavesServices.TokenTypes.FALSE.ordinal())){
                if(!this.match(ClavesServices.TokenTypes.NUM.ordinal())){
                    if(!this.match(ClavesServices.TokenTypes.NULL.ordinal())){
                        if(!this.match(ClavesServices.TokenTypes.charLiteral.ordinal())){
                            if(!this.match(ClavesServices.TokenTypes.stringLiteral.ordinal()))
                                throw new AsintacticoException("Error sintactico : se esperaba un TRUE FALSE NUM NULL charLiteral stringLiteral"+this.actual.getError());
                        }
                    }    
                }
            }
        }
    }
    
    //Regla 42
    private void expOr() throws AsintacticoException{
            ////System.out.println("expOr");
        this.expAnd();
        this.expOrR();
    
    }
    //Regla 43
    private void expOrR() throws AsintacticoException{
        if(this.match(ClavesServices.TokenTypes.OR.ordinal())){
            this.expAnd();
            this.expOrR();   
        }   
    }
    //Regla 44
    private void expAnd() throws AsintacticoException{
            ////System.out.println("expAnd");
        this.expIg();
        this.expAndR();
        
    
    }
     //Regla 45
    private void expAndR() throws AsintacticoException{
        ////System.out.println("expAndR");
         if(this.match(ClavesServices.TokenTypes.AND.ordinal())){
            this.expIg();
            this.expAndR();   
        }
    
    }
      //Regla 46
    private void expIg() throws AsintacticoException{
            ////System.out.println("expIg");
        this.expComp();
        this.expIgR();
        
    
    }
      //Regla 47
    private void expIgR() throws AsintacticoException{
        ////System.out.println("expIgR");
        if(this.actual.getType()==ClavesServices.TokenTypes.IGUALIGUAL.ordinal() ||
                this.actual.getType()==ClavesServices.TokenTypes.DIST.ordinal()){
            this.opIg();
            this.expComp();
            this.expIgR();
        
        }
    
    }
    
     //Regla 48
    private void expComp() throws AsintacticoException{
        ////System.out.println("expComp");
        this.expAd();
        this.expCompR();
        
    
    }
    //Regla 49
    private void expCompR() throws AsintacticoException{
        //si lo siguiente es un < >=, etc entonces =>
        if(this.siguientesComp()){            
                this.opComp();
                this.expAd();
        
        }
      
    
    }
    //Regla 50
    private void expAd() throws AsintacticoException{
        this.expMul();
        this.expAdR();
    
    }
    //Regla 51
    private void expAdR() throws AsintacticoException{
        //Si esta en siguientes de OpAd
        if(this.actual.getType()==ClavesServices.TokenTypes.MAS.ordinal() ||
           this.actual.getType()==ClavesServices.TokenTypes.MENOS.ordinal())
        {
            this.opAd();
            this.expMul();
            this.expAdR();
        }
    
    }
    //Regla 52
    private void expMul() throws AsintacticoException{
            ////System.out.println("expMul");
       this.expUn();
       this.expMulR();
    
    }
      //Regla 53
    private void expMulR() throws AsintacticoException{
          //Si esta en los siguientes de opMul
          if(this.actual.getType()==ClavesServices.TokenTypes.POR.ordinal() ||
                this.actual.getType()==ClavesServices.TokenTypes.DIV.ordinal()||
                this.actual.getType()==ClavesServices.TokenTypes.MOD.ordinal()){
              
              this.opMul();
              this.expUn();
              this.expMulR();
          }
    }
    
     //Regla 54
    private void expUn() throws AsintacticoException{
          //Si estoy en siguientes de opUn =>
          if(this.actual.getType()==ClavesServices.TokenTypes.MAS.ordinal() ||
           this.actual.getType()==ClavesServices.TokenTypes.MENOS.ordinal() || 
            this.actual.getType()==ClavesServices.TokenTypes.NOT.ordinal()){
              
             this.opUn();
             this.expUn();
         }
          //sino tengo que estar en operando
         else this.operando();
        
    
    }
    
    //Regla 55
    private void opIg() throws AsintacticoException{
            ////System.out.println("opIg");
        if(!this.match(ClavesServices.TokenTypes.IGUALIGUAL.ordinal()))
            if(!this.match(ClavesServices.TokenTypes.DIST.ordinal()))
                throw new AsintacticoException("Error Sintactico : Esperaba == o != ,"+this.actual.getError());
    
    }
       
    //Regla 56
    private void opComp() throws AsintacticoException{
            ////System.out.println("opComp");
        if(!this.match(ClavesServices.TokenTypes.MAY.ordinal())){
            if(!this.match(ClavesServices.TokenTypes.MAYIGUAL.ordinal())){
                if(!this.match(ClavesServices.TokenTypes.MEN.ordinal())){
                    if(!this.match(ClavesServices.TokenTypes.MENIGUAL.ordinal()))
                        throw new AsintacticoException("Error sintactico : se esperaba >,>=,<,<="+this.actual.getError());
                }
            
            }
        
        }
    
    }
    
    //Regla 57
    private void opAd() throws AsintacticoException{
        ////System.out.println("opAd");
        if(!this.match(ClavesServices.TokenTypes.MAS.ordinal()))
            if(!this.match(ClavesServices.TokenTypes.MENOS.ordinal()))
                throw new AsintacticoException("Error sintactico : se esperaba + o -."+this.actual.getError());
        
    
    }
    
    //Regla 58
    private void opUn() throws AsintacticoException{
            ////System.out.println("opUn");
        if(!this.match(ClavesServices.TokenTypes.MAS.ordinal()))
                if(!this.match(ClavesServices.TokenTypes.MENOS.ordinal() ))
                  if(!this.match(ClavesServices.TokenTypes.NOT.ordinal()))
                          throw new AsintacticoException("Error Sintactico : Se esperaba +,-,! ,"+this.actual.getError()); 
        
    
    }
    
     //Regla 59
    private void opMul()throws AsintacticoException{
        if(!this.match(ClavesServices.TokenTypes.POR.ordinal()))
            if(!this.match(ClavesServices.TokenTypes.DIV.ordinal()))
                if(!this.match(ClavesServices.TokenTypes.MOD.ordinal()))
                    throw new AsintacticoException("Error sintactico : se esperaba * o /."+this.actual.getError());
    
    }

     //Regla 62
    private void encadenadoAux() throws AsintacticoException{
        //Si lo que viene es un punto llamo a encadenado
        if(this.actual.getType()==ClavesServices.TokenTypes.PP.ordinal())
            this.encadenado();
    
    }

      //Regla 64 65 66
    private void primario() throws AsintacticoException{
        //Regla 64
        //System.out.println("Tengo que estar en primario");
        if(this.match(ClavesServices.TokenTypes.PPA.ordinal())){
            this.expresion();
            if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
            throw new AsintacticoException("Error Sintactico, se esperaba),"+this.actual.getError());
                
        }
        
        else if(this.actual.getType()==ClavesServices.TokenTypes.THIS.ordinal())
            this.accesoThis();
        else if(this.actual.getType()==ClavesServices.TokenTypes.idMetVar.ordinal())
            this.accesoVarLlamadaMetodo();
        else if(this.actual.getType()==ClavesServices.TokenTypes.idClase.ordinal())
            this.llamadaEstatica();
        else if(this.actual.getType()==ClavesServices.TokenTypes.NEW.ordinal())
            this.llamadaCtor();
        else throw new AsintacticoException("Error sintactico: se esperaba (,this,idMetVar, idClase o new"+this.actual.getError());
        
    
    }
    
     //Regla 67
    private void accesoVarLlamadaMetodo() throws AsintacticoException{
        //System.out.println("accesoVarLlamadaMetodo");
        //System.out.println("El actual es : "+this.actual.getLexema());
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error sintactico: se esperaba un idMetVar"+this.actual.getError());
        if(this.actual.getType()==ClavesServices.TokenTypes.PPA.ordinal())
            this.argsActuales();
    
    }
    
     //Regla 68
    private void accesoThis() throws AsintacticoException{
         //System.out.println("accesoThis");
         if(!this.match(ClavesServices.TokenTypes.THIS.ordinal()))
            throw new AsintacticoException("Error sintactico: se esperaba un THIS y no se encontro,"+this.actual.getError());
        //this.encadenadoAux();
    }
    
    //Regla 69
    private void llamadaEstatica() throws AsintacticoException{
        //Espero por un idClase
        if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
            throw new AsintacticoException("Error sintactico : "
                    + "se esperaba un idClase "+this.actual.getError());
        //Espero por un punto (PUNTUACIÓN PUNTO)
         if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
            throw new AsintacticoException("Error sintactico : "
                    + "se esperaba un '.'(Puntuación Punto) "+this.actual.getError());
         this.llamadaMetodo();
        

            
    
    }
    
      
    //Regla 70
    private void llamadaCtor() throws AsintacticoException{
        //System.out.println("Estoy en llamadaCtor");
        if(!this.match(ClavesServices.TokenTypes.NEW.ordinal()))
            throw new AsintacticoException("Error sintactico : se esperaba new,"+this.actual.getError());
        if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
            throw new AsintacticoException("Error sintactico : se esperaba idClase,"+this.actual.getError());
        this.argsActuales();
    }
    
   
    //Regla 71
    private void encadenado() throws AsintacticoException{
       this.llamadaOIDEncadenado();
       this.encadenadoAux();
    
    }
    //Regla 72
    private void llamadaOIDEncadenado() throws AsintacticoException{
          if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
            throw new AsintacticoException("Error sintactico: se esperaba un '.'(Puntuación Punto)"
                    + " y no se encontro,"+this.actual.getError());
          if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error sintactico: se esperaba un 'idMetVar'"
                    + " y no se encontro,"+this.actual.getError());
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
            throw new AsintacticoException("ERROR SINTACTICO: se esparaba idMetVar,"+this.actual.getError());
        this.argsActuales();
        //this.encadenadoAux();
        
    }
    //Regla 75  
    private void argsActuales() throws AsintacticoException{
            ////System.out.println("argsActuales");
        if(!this.match(ClavesServices.TokenTypes.PPA.ordinal()))
            throw new AsintacticoException("Error Sintactico: Se esperaba"
                    + "( ,"+this.actual.getError());
        this.listaExpresionAux();
        if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                throw new AsintacticoException("Error Sintactico: Se esperaba ) , "
                    + ""+this.actual.getError());
        
       

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
    
    
  
    
    private boolean siguientesComp(){
        return (this.actual.getType()==ClavesServices.TokenTypes.MAY.ordinal() ||
                this.actual.getType()==ClavesServices.TokenTypes.MAYIGUAL.ordinal() ||
                this.actual.getType()==ClavesServices.TokenTypes.MEN.ordinal() ||
                this.actual.getType()==ClavesServices.TokenTypes.MENIGUAL.ordinal());
    
    
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
    	return(aux==ClavesServices.TokenTypes.IGUALIGUAL.ordinal()
    			|| aux==ClavesServices.TokenTypes.IGUALMAS.ordinal()
    			|| aux==ClavesServices.TokenTypes.IGUALMENOS.ordinal());
    			
    }
    
    private boolean siguientesListaArgs(){
        int aux = this.actual.getType();
        return (aux==ClavesServices.TokenTypes.BOOLEAN.ordinal()
                || aux==ClavesServices.TokenTypes.CHAR.ordinal()
                || aux ==ClavesServices.TokenTypes.INT.ordinal()
                || aux == ClavesServices.TokenTypes.STRING.ordinal()
                || aux == ClavesServices.TokenTypes.idClase.ordinal());
    
    }
    
    
    private boolean siguientesOpUn(){
    	return this.actual.getType()==ClavesServices.TokenTypes.MAS.ordinal() ||
    	           this.actual.getType()==ClavesServices.TokenTypes.MENOS.ordinal() || 
    	            this.actual.getType()==ClavesServices.TokenTypes.NOT.ordinal();
    	
    }
    
    private boolean estaEnLiteral(){
        int aux = this.actual.getType();
        return (aux==ClavesServices.TokenTypes.TRUE.ordinal()
                || aux==ClavesServices.TokenTypes.FALSE.ordinal()
                || aux ==ClavesServices.TokenTypes.NUM.ordinal()
                || aux == ClavesServices.TokenTypes.NULL.ordinal()
                || aux == ClavesServices.TokenTypes.charLiteral.ordinal()
                || aux == ClavesServices.TokenTypes.stringLiteral.ordinal()
                );
    
    }

}