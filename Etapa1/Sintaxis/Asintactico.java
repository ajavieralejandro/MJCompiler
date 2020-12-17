/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sintaxis;

import java.util.ArrayList;

import semantico2.ASTException;
import semantico2.NodoBloque;
import semantico2.NodoDecVarLocales;
import semantico2.NodoIf;
import semantico2.NodoPComa;
import semantico2.NodoReturn;
import semantico2.NodoSentencia;
import semantico2.NodoWhile;
import semantico2expresiones.Encadenado;
import semantico2expresiones.NodoCtor;
import semantico2expresiones.NodoExpBinaria;
import semantico2expresiones.NodoExpParentizada;
import semantico2expresiones.NodoExpUnaria;
import semantico2expresiones.NodoExpresion;
import semantico2expresiones.NodoIdEncadenado;
import semantico2expresiones.NodoLiteral;
import semantico2expresiones.NodoLlamadaDirecta;
import semantico2expresiones.NodoLlamadaEncadenada;
import semantico2expresiones.NodoLlamadaEstatica;
import semantico2expresiones.NodoPrimario;
import semantico2expresiones.NodoThis;
import semantico2expresiones.NodoVar;
import semantico2.NodoSentenciaAsignacion;

import java.util.List;


import Claves.ClavesServices;
import semantico.Clase;
import semantico.Ctor;
import semantico.Metodo;
import semantico.Parametro;
import semantico.TDS;
import semantico.Tipo;
import semantico.TipoBase;
import semantico.TipoBoolean;
import semantico.TipoChar;
import semantico.TipoClase;
import semantico.TipoInt;
import semantico.TipoPrimitivo;
import semantico.TipoString;
import semantico.TipoVoid;
import semantico.Unidad;
import semantico.VarLocal;
import semantico.VariableInstancia;
import lexico.Alexico;
import token.Token;
import token.TokenException;
import semantico.ASemanticoException;
import semantico2.NodoSentSimple;

/**
 *
 * @author Javier Amorosi
 */
public class Asintactico {
	//Variables correspondientes al AS
	private final TDS tds;
    
    private Token actual; 
    private Alexico alexico;
    
    public Asintactico(String file)throws TokenException{
            this.alexico = new Alexico(file);
            this.tds = TDS.getInstance();
            actual = null;
    }
            
       
    

    
    
    public void analize() throws TokenException,AsintacticoException,ASemanticoException,ASTException{
        //Consumo el primer token y empiezo el analisis...
        actual = alexico.nextToken();
        if(this.actual.getType()==ClavesServices.TokenTypes.EOF.ordinal())
        	throw new AsintacticoException("Error: archivo vacio."+this.actual.getError());
        this.inicial();
        this.tds.chequeo();
        System.out.println("Compilacion exitosa  \n[SinErrores].");

    }
    //Regla 1
    private void inicial() throws AsintacticoException,ASemanticoException{
    	this.listaClases();
        if(this.actual.getType()!=ClavesServices.TokenTypes.EOF.ordinal())
            throw new AsintacticoException("Error Sintactico en linea :+"+this.actual.getLine()+" Se esperaba fin de archivo y no se lo encontro."+this.actual.getError());

    }
    //Regla 2
    private void listaClases() throws AsintacticoException,ASemanticoException{
        ////System.out.println("Regla 2");
        if(this.actual.getType() != ClavesServices.TokenTypes.EOF.ordinal()){
            this.clase();
            this.listaClases();
        }
 
    }
    //Regla 3
    private void clase() throws AsintacticoException,ASemanticoException{
    
        if(!this.match(ClavesServices.TokenTypes.CLASS.ordinal()))
            throw new AsintacticoException("Error Sintactico en linea : "
        +this.actual.getLine()+"Se esperaba la palabra clave class y se encontro:"+this.actual.getLexema()+this.actual.getError());
        //Busco el idClase
        Token idClase = this.actual;
        Clase c = new Clase(idClase,this.tds);
        this.tds.insertarClase(c);
        this.tds.setClaseActual(c);
        
         if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
             throw new AsintacticoException("Error Sintactico : Se esperaba un idClase y se encontro :"+this.actual.getLexema()+this.actual.getError());
         this.herencia();
         if(!this.match(ClavesServices.TokenTypes.PLA.ordinal()))
             throw new AsintacticoException("Error Sintactico : Se esperaba { y se encontro: "+this.actual.getLexema()+this.actual.getError());
        if(this.actual.getType()!=ClavesServices.TokenTypes.EOF.ordinal())
            this.listaMiembros();
        if(!this.match(ClavesServices.TokenTypes.PLC.ordinal()))
            throw new AsintacticoException("Error Sintactico: se esperaba llaves Cerradas }"
                    + " del Bloque de Clase y se encontro : "+this.actual.getLexema()+this.actual.getError());
           

       
       
        
 
    }
    //Regla 4    
    private void herencia() throws AsintacticoException{

        if(this.match(ClavesServices.TokenTypes.EXTENDS.ordinal())){
        	Token padre = this.actual;
        	this.tds.getClaseActual().setPadre(padre.getLexema());
            if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
                throw new AsintacticoException("Error Sintactico : "
                        + "Se esperaba un idClase luego del extends y se encontro : "+this.actual.getLexema()+this.actual.getError());               
        }
        //Si no hereda de ninguna entonces hereda de Object
        else
            this.tds.getClaseActual().setPadre(ClavesServices.TokenTypes.OBJECT.toString());
        

    }
    
    //Regla 5
    private void listaMiembros() throws AsintacticoException,ASemanticoException{

        //Si lo que viene es distinto a Puntuación Llave que cierra
        if(this.actual.getType()!=ClavesServices.TokenTypes.PLC.ordinal()){
               this.miembro();
               this.listaMiembros();
        }
        
    }
    //Regla 6
    private void miembro() throws AsintacticoException,ASemanticoException{
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
                throw new AsintacticoException("Error Sintactico: se esperaba protected, public ,private,un idClase ,"
                        + " static o dynamic y se encontro : "+this.actual.getLexema()+this.actual.getError());

    }
    
    
    //Regla 7
    private void atributo()throws AsintacticoException,ASemanticoException{
        Token visibilidad = this.visibilidad();
        Tipo tipo = this.tipo();
        List<Token> tokenList = new ArrayList<Token>();
        this.listaDecAtrs(tokenList);
         if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
            throw new AsintacticoException("Error Sintactico : se esperaba ; en la declaracion de atributos"
                    + " y se encontro : "+this.actual.getLexema()
                    +this.actual.getError());
         for(Token t : tokenList) {
        	  //Tengo que encontrar el tipo y no tratarlo como String...
             VariableInstancia aux = new VariableInstancia(tipo,t.getLexema(),t);
             aux.setVisibilidad(visibilidad.getLexema());
             aux.setDeclarada(tds.getClaseActual());
             //Tengo que ver si necesito imprimir las variables de instancias
             //aux.imprimir();
             this.tds.getClaseActual().insertarVariableInstancia(aux);
        	 
         }
        
    
    }
      //Cuando se ingresa a este metodo ya se sabe que el token actual es STATIC O DYNAMIC...
    //Regla 8
    private void metodo() throws AsintacticoException,ASemanticoException{
    	Metodo aIns = new Metodo();
    	this.tds.setUnidadActual(aIns);
        this.formaMetodo();
        TipoBase retorno = this.tipoMetodo();
        //Codigo intercalado
        Token aux = this.actual;
        aIns.setId(aux);
        aIns.setRetorno(retorno);
        
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error Sintactico : se esperaba un idMetVar y se encontro : "+this.actual.getLexema()+this.actual.getError());
        this.argsFormales();
        aIns.setBloque(this.bloque());
        //Inserto el metodo
        this.tds.getClaseActual().insertarMetodo(aIns);
        
    }

     //Regla 9
    private void ctor() throws AsintacticoException,ASemanticoException{
    	Ctor aIns = new Ctor();
        ////System.out.println("ctor");
    	Token aux = this.actual;
    	aIns.setId(aux);
    	aIns.setClase(this.tds.getClaseActual());
        if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
            throw new AsintacticoException("Error Sintactico se esperaba un idClase y se encontro : ."+this.actual.getLexema()
                    +this.actual.getError());
        this.tds.setUnidadActual(aIns);
        this.argsFormales();
        aIns.setBloque(this.bloque());
        this.tds.getClaseActual().insertarConstructor(aIns);
        
    }
    //Regla 10
   private Token visibilidad() throws AsintacticoException{
       Token toR = this.actual;
       if(!this.match(ClavesServices.TokenTypes.PUBLIC.ordinal()))
               if(!this.match(ClavesServices.TokenTypes.PRIVATE.ordinal()))
               throw new AsintacticoException("Error Sintactico :"
                       + " se esperaba public,protected o private y se encontro : "+this.actual.getLexema()+this.actual.getError());
       return toR;
   }
   
   //Regla 11
   private Tipo tipo() throws AsintacticoException{
	   Tipo toR = null;
	   Token aux = this.actual;
       if(this.match(ClavesServices.TokenTypes.idClase.ordinal()))
    	   toR = new TipoClase(aux);
       
       else
    	   toR = this.tipoPrimitivo();
       return toR;
            
   }
   
   //Regla 12
   private TipoPrimitivo tipoPrimitivo()throws AsintacticoException{
	   Token aux = this.actual;
       TipoPrimitivo toR = null;
       if(match(ClavesServices.TokenTypes.INT.ordinal()))
           toR = new TipoInt(aux);
       else 
           if(match(ClavesServices.TokenTypes.CHAR.ordinal()))
               toR = new TipoChar(aux);
           else
               if(match(ClavesServices.TokenTypes.BOOLEAN.ordinal()))
               toR = new TipoBoolean(aux);
               else
                   if(match(ClavesServices.TokenTypes.STRING.ordinal()))
                   toR = new TipoString(aux);
                   else
                	   throw new AsintacticoException("Error Sintactico:  se Esperaba int char boolean o string"
                               + " y se encontro :  ,"+this.actual.getLexema()+this.actual.getError());
       
       return toR;
	   
      

   }
    
    //Regla 13
    private void listaDecAtrs(List<Token> tokenList) throws AsintacticoException{
    
    	tokenList.add(this.actual);
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error Sintactico : Se esperaba  IdMetVar y se encontro "+this.actual.getLexema()
                    +this.actual.getError());
        this.listaDecAtrsF(tokenList);
        
    }
    
    //Regla 14
    private void listaDecAtrsF(List<Token> tokenList) throws AsintacticoException{
        //Si hace match con la coma
        if(this.match(ClavesServices.TokenTypes.PC.ordinal()))
            this.listaDecAtrs(tokenList);
        
    }
    
    //Regla 15
    private void  formaMetodo() throws AsintacticoException{
    	Metodo aux = (Metodo) this.tds.getUnidadActual();
        if(this.match(ClavesServices.TokenTypes.STATIC.ordinal()))
        	aux.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        else
        	if(this.match(ClavesServices.TokenTypes.DYNAMIC.ordinal()))
            	aux.setFormaMetodo(ClavesServices.TokenTypes.DYNAMIC.toString());
            	else
            		 throw new AsintacticoException("Error Sintactico :Se esperaba statico o dynamic y se encontro : "+this.actual.getLexema()
                     +this.actual.getError());
        
    }
    
    //Regla 16
    private TipoBase tipoMetodo() throws AsintacticoException{
        
        if(!this.match(ClavesServices.TokenTypes.VOID.ordinal()))
            return this.tipo();
        else
        	return new TipoVoid(this.actual);
    }

    
    //Regla 17
    private void argsFormales() throws AsintacticoException,ASemanticoException{
                
        if(!this.match(ClavesServices.TokenTypes.PPA.ordinal()))
                    throw new AsintacticoException("Error Sintactico: Se esperaban parentesis abiertos y se econtro :"+this.actual.getLexema()
                            +this.actual.getError());
        
        this.listaArgsFormalesAux();
        if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                    throw new AsintacticoException("Error Sintactico: se esperaba ) o , y se encontro : "+this.actual.getLexema()
                           +this.actual.getError());
            
        
    
    }
    
    //Regla 18
    private void listaArgsFormalesAux() throws AsintacticoException,ASemanticoException{
        //Si lo que vienen no son paréntesis que cierran...
        if(this.actual.getType()!=ClavesServices.TokenTypes.PPC.ordinal())
            this.listaArgsFormales();
     

    }
    //Tengo que considerar el caso en que los argumentos sean vacios...
    //Regla 19
    private void listaArgsFormales() throws AsintacticoException,ASemanticoException{
        this.argFormal();
        this.listaArgsFormalesF();
        
    
    }
    //Regla 20
    private void listaArgsFormalesF() throws AsintacticoException,ASemanticoException{
        if(this.match(ClavesServices.TokenTypes.PC.ordinal()))
            this.listaArgsFormales();
    }
 
    // Regla 21
    private void argFormal() throws AsintacticoException,ASemanticoException{
        Tipo tipoIns = this.tipo();
        //Codigo ASemanticoc 
        Token aux = this.actual;
        Parametro p = new Parametro(tipoIns,aux.getLexema(),aux);
        Unidad m = this.tds.getUnidadActual();
        m.insertarArgumento(p);
        
        
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error Sintactico : se esperaba idMetVar y se encontro : "+this.actual.getLexema()+this.actual.getError());
    
    }
    //Regla 22
    private NodoBloque bloque() throws AsintacticoException{
    	//Creo y seteo el bloque actual
    	NodoBloque _bloqueactual = new NodoBloque();
    	_bloqueactual.setUnidadActual(tds.getUnidadActual());
    	tds.setBloqueActual(_bloqueactual);
        if(!this.match(ClavesServices.TokenTypes.PLA.ordinal()))
            throw new AsintacticoException("Error Sintactico :  se esperaba } y se encontro : "+this.actual.getLexema()+this.actual.getError());
         this.sentenciaAux();        
        if(!this.match(ClavesServices.TokenTypes.PLC.ordinal()))
            throw new AsintacticoException("Error Sintactico :  se esperaba { y se encontro: "+this.actual.getLexema()+this.actual.getError());
        return _bloqueactual;
        
    
    }
    //Regla 23 a 28
    private NodoSentencia sentencia() throws AsintacticoException{
    	NodoSentencia _toR = null;
    	System.out.println("Estoy en sentencia...");
        if(!this.match(ClavesServices.TokenTypes.PPY.ordinal())){
            if(this.actual.getType()==ClavesServices.TokenTypes.idMetVar.ordinal()){
            	System.out.println("Estoy en asignacionLlamda");

            	//Inserto el nodo sentencia 
                _toR = this.asignacionLlamada();
                this.tds.getBloqueActual().getSentencias().add(_toR);
                if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
                    throw new AsintacticoException("Error Sintactico : se esperaba ; "
                        + "y  se encontro."+this.actual.getLexema()+this.actual.getError());
            }
            //Estoy en siguientes de tipo
            else if(siguientesTipo()){
            	System.out.println("Estoy en ListaDecVars");
            	NodoDecVarLocales aux = new NodoDecVarLocales(this.tds.getBloqueActual());
            	
                Tipo _toIns = this.tipo();
                aux.set_tipo(_toIns);
                this.listaDecVars(aux);
                 if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
                    throw new AsintacticoException("Error Sintactico : se esperaba ; "
                        + "y se encontro."+this.actual.getLexema()+this.actual.getError());
                 _toR = aux;
            }
            //Estoy en siguientes de if
            else if(this.actual.getType()==ClavesServices.TokenTypes.IF.ordinal()){
            	System.out.println("Estoy en if");
            	_toR = this.sentenciaIf();
            }
            
            //siguientes llamada
            else if(this.siguientesAcceso()){
            	System.out.println("Estoy en asignacion llamada");
            	_toR = this.asignacionLlamada();
            	
            }
  
            //Estoy en siguientes de While
            else if(this.actual.getType()==ClavesServices.TokenTypes.WHILE.ordinal()) {
            	System.out.println("Estoy en while");
                       _toR = this.sentenciaWhile();
            }
            //Estoy en siguientes de bloque
            else if(this.actual.getType()==ClavesServices.TokenTypes.PLA.ordinal()) {
            			System.out.println("Estoy en bloque");
            			//tal vez tenga que hacer algo
                        _toR = this.bloque();
            }
            //Estoy en siguientes de return
            else if(this.match(ClavesServices.TokenTypes.RETURN.ordinal())){
            	System.out.println("Estoy en return");
            		   NodoReturn _aux = new NodoReturn((Metodo) tds.getUnidadActual());
            		   
                       _aux.setExpresion(this.ExpresionOVacio());
                       _toR = _aux;
                       if(!this.match(ClavesServices.TokenTypes.PPY.ordinal()))
                            throw new AsintacticoException("Error Sintactico : en linea :"+this.actual.getLine()+" se esperaba ; "
                                    + "y se encontro : "+this.actual.getLexema()+this.actual.getError())	;
            }
            //Si no entro en ninguno de los anteriores tengo que reportar la excepcion            
            else throw new AsintacticoException("Error Sintactico :se esperaba una sentencia, "
                    + "o un ;(sentencia vacia) "
                        +this.actual.getError());
            
        }
        else 
        	_toR = new NodoPComa();
        System.out.println(_toR);
        return  _toR;
        
    }
    
    //Regla 29
	  private NodoSentencia asignacionLlamada() throws AsintacticoException{
		NodoSentencia _toR = null;
		//Esto lo tengo que ver
	  	NodoSentSimple _aux1 = new NodoSentSimple();
                _aux1.setExpresion(this.acceso());
                _toR = _aux1;
	  	if(this.siguientesTipoAsignacion()) {
	  		NodoSentenciaAsignacion _aux2 = new NodoSentenciaAsignacion(this.tds.getBloqueActual());
                        _aux2.setlIzq(_aux1.getExpresion());
	  		//ahora las asignaciones tienen un tipo
	  		_aux2.setOperador(this.tipoAsignacion());
	  		_aux2.setLder(this.expresion());
                        _toR = _aux2;
	  	}
	  	return _toR;
	  		
	  }
	  //Regla 30
	  
	  private Token tipoAsignacion() throws AsintacticoException{
              
              Token _toR = this.actual;
		
		  if(!this.match(ClavesServices.TokenTypes.IGUALMAS.ordinal()) 
				  && !this.match(ClavesServices.TokenTypes.IGUAL.ordinal())
				  && !this.match(ClavesServices.TokenTypes.IGUALMENOS.ordinal()))
			  throw new AsintacticoException("Error Sintactico :"+this.actual.getLine()+" se esperaba un operador tipo asignacion y se encontro : "+this.actual.getLexema()+this.actual.getError());
                  
                  return _toR;
	  }
	  
	  private void llamada() throws AsintacticoException{
		  this.acceso();
	  }
	  
	  
	    //Regla 31
	    private void listaDecVars(NodoDecVarLocales aux) throws AsintacticoException{
	    	VarLocal _toIns = new VarLocal(aux.get_tipo(),this.actual.getLexema(),this.actual);
	    	aux.insertVariableLocal(_toIns);
	    	tds.getBloqueActual().insertarVariableLocal(_toIns);
	        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
	                throw new AsintacticoException("Error Sintactico :"
	                                + " Se esperaba una idMetVar y se la encontro,"+this.actual.getLexema()+this.actual.getError());
	        this.listaDecVarsF(aux); 
	    }
	    //Regla 32
	    private void listaDecVarsF(NodoDecVarLocales aux) throws AsintacticoException{

	        if(this.match(ClavesServices.TokenTypes.PC.ordinal()))
	            this.listaDecVars(aux);
	    }
	    
	    
	    //Regla 34
	    private NodoExpresion expresion() throws AsintacticoException{
	    	System.out.println("Llegue a expresion, estoy bien!");
	    	NodoExpresion _toR = null;
	        _toR = this.expresionUnaria();
	        System.out.println("toR1 expresion es : "+_toR);
	        _toR = this.expresionAux(_toR);
	        System.out.println("toR2 expresion es : "+_toR);
	        System.out.println("Estoy retornando de expresion :"+_toR);
	        return _toR;
	         
	    
	    }
	    
	    private NodoExpresion ExpresionOVacio() throws AsintacticoException{
	    	System.out.println("Estoy en expresion o vacio");
	    	NodoExpresion _toR = null;
	    	if(this.siguientesAcceso() || this.siguientesOpUn() || this.siguientesLiteral()){
	    		System.out.println("Tengo que venir aca");
	    		_toR = this.expresion();
	    	}
	    	System.out.println("Retorno :"+_toR);
	    	return _toR;

	    }
	    
	    //Regla 35
	    private NodoExpresion expresionAux(NodoExpresion nodo) throws AsintacticoException{
	    	System.out.println("Estoy en expresion aux");
	    	
	    	NodoExpresion _toR = null;
	    	if(this.siguientesOpBinario()) {
	    		System.out.println("Estoy en los siguientes de operador binario");
	    		NodoExpBinaria _aux = new NodoExpBinaria();
	    		_aux.setOperador(this.operadorBinario());
	    		_aux.setLizq(nodo);
	        	_aux.setLder(this.expresionUnaria());
	        	_toR = this.expresionAux(_aux);
	    		
	    	}
	    	if(_toR==null)
	    		_toR = nodo;
	    	return _toR;
	
	        
	       
	        
	    
	    }
	    //Regla 36
	    private Token operadorBinario() throws AsintacticoException{
	    	Token _toR = this.actual;
	    	//Esto puede necesitar un refactor
	    	if(!this.match(ClavesServices.TokenTypes.IGUAL.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.AND.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.IGUALIGUAL.ordinal())
	    			&& !this.match(ClavesServices.TokenTypes.OR.ordinal())
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
	    		throw new AsintacticoException("Error Sintactico : esperaba operador binario y se encontro :"+this.actual.getLexema()+this.actual.getError());
	    	return _toR;
	    }
	    
	    
	    //Regla 37 y 38
	    private NodoExpresion expresionUnaria() throws AsintacticoException{
	    	NodoExpresion _toR = null;
                boolean _flag = false;
	    	NodoExpUnaria aux = new NodoExpUnaria();
	    	System.out.println("eL TOKEN actual es :"+this.actual.getLexema());
	    	if(this.siguientesOpUn()) {
                    _flag = true;
                        //Tengo un operador entonces no soy un literal
	    		System.out.println("Estoy en los siguientes de operador unario con :"+this.actual.getLexema());
	    		aux.setOperador(this.opUn());
                        _toR = aux;

	    		
	    	}
	    	aux.setExpresion(this.operando());
                if(!_flag){
                    //Si no tengo operador entonces estoy aca y listo
                    _toR = aux.getExpresion();
                }
                    
	    	System.out.println("Retorno :"+_toR);
	    	return _toR;
	    }
	    
	    //Regla 39
	    
	    private Token opUn() throws AsintacticoException{
	    System.out.println("Estoy en opUn y el token actual es :"+this.actual);
	    Token toR = this.actual;
        if(!this.match(ClavesServices.TokenTypes.MAS.ordinal()))
                if(!this.match(ClavesServices.TokenTypes.MENOS.ordinal() ))
                  if(!this.match(ClavesServices.TokenTypes.NOT.ordinal()))
                          throw new AsintacticoException("Error Sintactico : Se esperaba +,-,! , y se encontro : "+this.actual.getLexema()+this.actual.getError());
        System.out.println("Estoy retornando:"+toR);
        return toR;
        
    
    }
	    

	    //Regla 40
	    private NodoExpresion operando() throws AsintacticoException{
	       NodoExpresion toR = null;
	       if(this.siguientesLiteral())
	    	   toR = this.literal();
	       else
	    	   toR = this.acceso();
	       return toR;
	       
	    }
	    
	    //Regla 41
	    private NodoLiteral literal() throws AsintacticoException{
	    	
	    	//Devuelvo el literal con la expresion 
	    	NodoLiteral _toR = new NodoLiteral();
	    	_toR.setOperando(this.actual);
	    
	    	if(this.match(ClavesServices.TokenTypes.TRUE.ordinal()))
	    		_toR.setTipo(new TipoBoolean());
	    	else
	            if(this.match(ClavesServices.TokenTypes.FALSE.ordinal()))
		    		_toR.setTipo(new TipoBoolean());
	            else 
	                if(this.match(ClavesServices.TokenTypes.NUM.ordinal()))
	                	_toR.setTipo(new TipoInt());
	                else
	                    if(this.match(ClavesServices.TokenTypes.NULL.ordinal()))
	                    	_toR.setTipo(new TipoClase(_toR.getToken()));
	                    else
	                        if(this.match(ClavesServices.TokenTypes.charLiteral.ordinal()))
	                        	_toR.setTipo(new TipoChar());
	                        else
	                            if(this.match(ClavesServices.TokenTypes.stringLiteral.ordinal()))
	                            	_toR.setTipo(new TipoString());
	                            else
	                                throw new AsintacticoException("Error Sintactico : se esperaba un TRUE FALSE NUM NULL charLiteral stringLiteral y se encontro :"+this.actual.getLexema()+this.actual.getError());
                    
	         return _toR;
	    }
	        
	  
	  //Regla 42
	  private NodoPrimario acceso() throws AsintacticoException{
		  NodoPrimario _toR = this.primario();
		  _toR.setCadena(this.encadenado());
		  return _toR;
	  }
	  
	    
	    //Regla 43 a 46

	    private NodoPrimario primario() throws AsintacticoException{
	    	NodoPrimario _toR = null;
	        if(this.match(ClavesServices.TokenTypes.PPA.ordinal())){
	        	NodoExpParentizada aux = new NodoExpParentizada();
	            aux.setExpresion(this.expresion());
	            _toR = aux;
	            if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
	            throw new AsintacticoException("Error Sintactico, se esperaba),y se encontro :"+this.actual.getLexema()	+this.actual.getError());
	                
	        }
	           
	        else if(this.actual.getType()==ClavesServices.TokenTypes.THIS.ordinal())
	            _toR = this.accesoThis();
	        else if(this.actual.getType()==ClavesServices.TokenTypes.idMetVar.ordinal())
	            _toR = this.accesoVarMetodo();
	        else if(this.actual.getType()==ClavesServices.TokenTypes.STATIC.ordinal())
	            _toR = this.llamadaEstatica();
	        else if(this.actual.getType()==ClavesServices.TokenTypes.NEW.ordinal())
	            _toR = this.accesoCtor();
	        //else if(this.actual.getType()==ClavesServices.TokenTypes.STATIC.ordinal())
	        	//this.accesoStatic();
	        else throw new AsintacticoException("Error Sintactico : se esparaba this, idMetVar,idClase, new o static y se encontro : "+this.actual.getLexema()+this.actual.getError());
	        
	        return _toR;
	        
	    
	    }
	    
	 
	     //Regla 47
	    private NodoPrimario accesoVarMetodo() throws AsintacticoException{
	    	NodoPrimario _toR = null;
	    	Token _aux = this.actual;
	        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
	            throw new AsintacticoException("Error Sintactico: se esperaba un idMetVar y se encontro : "+this.actual.getLexema()+this.actual.getError());
	        _toR = new NodoVar(tds.getBloqueActual(),_aux,false);
	        if(this.actual.getType()==ClavesServices.TokenTypes.PPA.ordinal()) {
	        	NodoLlamadaDirecta _nodoLlamada = new NodoLlamadaDirecta(_aux);
	            _nodoLlamada.setActualArgs(this.argsActuales());
	            _toR = _nodoLlamada;
	        }
	        return _toR;
	    
	    }
	    /*
	    private void accesoStatic() throws AsintacticoException{
	    	if(!this.match(ClavesServices.TokenTypes.STATIC.ordinal()))
	    		throw new AsintacticoException("Error Sintactico : se esperaba static y se encontro : "+this.actual.getLexema()+"."+this.actual.getError());
	    	if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
	    		throw new AsintacticoException("Error Sintactico : se esperaba un idClase y se encontro : "+this.actual.getLexema()+"."+this.actual.getError());
	    	if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
	    		throw new AsintacticoException("Error Sintactico : se esperaba un punto '.' y se encontro : "+this.actual.getLexema()+"."+this.actual.getError());
	    	this.accesoMetodo();
	    	
	    	
	    	
	    }
	    
	    private void accesoMetodo() throws AsintacticoException{
	    	if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
	    		throw new AsintacticoException("Error Sintactico : se esperaba idMetVar y se encontro : "+this.actual.getLexema()+"."+this.actual.getError());
	    	this.argsActuales();
	    	
	    }
	    */
	    
	     //Regla 49
	    private NodoThis accesoThis() throws AsintacticoException{
	    	NodoThis _toR = new NodoThis(this.actual,this.tds.getUnidadActual(),this.tds.getClaseActual());
	         if(!this.match(ClavesServices.TokenTypes.THIS.ordinal()))
	            throw new AsintacticoException("Error Sintactico: se esperaba un THIS y  se encontro,"+this.actual.getLexema()+this.actual.getError());
	         return _toR;
	        //this.encadenadoAux();
	    }
	    
	    //Regla 51
	    private NodoLlamadaEstatica llamadaEstatica() throws AsintacticoException{
	    	if(!this.match(ClavesServices.TokenTypes.STATIC.ordinal()))
	    		throw new AsintacticoException("Error Sintactico : se esperaba static y se encontro : "+this.actual.getLexema()+"."+this.actual.getError());
	    	NodoLlamadaEstatica _toR = new NodoLlamadaEstatica();
	        //Espero por un idClase
	    	_toR.setIdClase(this.actual);
	        if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
	            throw new AsintacticoException("Error Sintactico : "
	                    + "se esperaba un idClase y se encontro :  "+this.actual.getLexema()+this.actual.getError());
	        //Espero por un punto (PUNTUACIÓN PUNTO)
	         if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
	            throw new AsintacticoException("Error Sintactico : "
	                    + "se esperaba un '.'(Puntuación Punto)  y se encontro :"+this.actual.getLexema()+this.actual.getError());
	         NodoLlamadaDirecta _aux = this.llamadaMetodo();
	         _toR.setId(_aux.getToken());
	         _toR.setActualArgs(_aux.getActualArgs());
	         _aux = null;
	         return _toR;
	        

	            
	    
	    }
	    //Regla 52
	    private NodoCtor accesoCtor() throws AsintacticoException{
	    	NodoCtor _toR = null;
	        if(!this.match(ClavesServices.TokenTypes.NEW.ordinal()))
	            throw new AsintacticoException("Error Sintactico : se esperaba new y se encontro :"+this.actual.getLexema()+this.actual.getError());
	        _toR = new NodoCtor(this.actual);
	        if(!this.match(ClavesServices.TokenTypes.idClase.ordinal()))
	            throw new AsintacticoException("Error Sintactico : se esperaba idClase y se encontro :"+this.actual.getLexema()+this.actual.getError());
	        _toR.setActualArgs(this.argsActuales());
	        return _toR;
	    }
	    
	  
 
        


    //Regla 26 
    private void sentenciaAux() throws AsintacticoException{
        
        if(this.actual.getType()!=ClavesServices.TokenTypes.PLC.ordinal() ){
            NodoSentencia _toIns = this.sentencia();
        	System.out.println("La sentencia a insertar es : "+_toIns);
            this.tds.getBloqueActual().addSentencia(_toIns);
            this.sentenciaAux();
        }
    }
    //Regla 27 28 31 33 34
   
    //Regla 29
    private NodoIf sentenciaIf() throws AsintacticoException{
    	NodoIf _toR = new NodoIf();
        if(this.match(ClavesServices.TokenTypes.IF.ordinal())){
            if(!this.match(ClavesServices.TokenTypes.PPA.ordinal()))
                throw new AsintacticoException("Error Sintactico,se esperaba ( y  se encontro."+this.actual.getLexema()+this.actual.getError());
             _toR.setCondicion(this.expresion());
             if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                 throw new AsintacticoException("Error Sintactico,se esperaba ) y  se encontro "+this.actual.getLexema()+this.actual.getError());
             _toR.setThen(this.sentencia());
             _toR.setNodoElse(this.sentenciaElse());
        }
        else throw new AsintacticoException("Error Sintactico,se esperaba if y  se encontro :"+this.actual.getLexema()+this.actual.getError());
        return _toR;
    
    }
    //regla 30
    private NodoSentencia sentenciaElse() throws AsintacticoException{
    	NodoSentencia _toR = null;
        if(this.match(ClavesServices.TokenTypes.ELSE.ordinal()))
            _toR = this.sentencia();
        return _toR;

    
    }
    //Regla 32
    private NodoWhile sentenciaWhile() throws AsintacticoException{
    	NodoWhile _toR = new NodoWhile();
        if(this.match(ClavesServices.TokenTypes.WHILE.ordinal())){
                if(this.match(ClavesServices.TokenTypes.PPA.ordinal())){
                    _toR.setCondicion(this.expresion());
                if(this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                    _toR.setSentencia(this.sentencia());
                else throw new AsintacticoException("Error Sintactico: se esperaba un ) y se encontro:"+this.actual.getLexema()+this.actual.getError());
            }
                else throw new AsintacticoException("Error Sintactico: se esperaba un ( y se encontro:"+this.actual.getLexema()+this.actual.getError());
         
        }
        else throw new AsintacticoException("Error Sintactico: se esperaba un while y  se encontro."+this.actual.getLexema()+this.actual.getError());
        return _toR;
    
    }

    
 

    
    


     //Regla 62
    private Encadenado encadenadoAux() throws AsintacticoException{
        //Si lo que viene es un punto llamo a encadenado
    	Encadenado _toR = null;
        if(this.actual.getType()==ClavesServices.TokenTypes.PP.ordinal())
            _toR = this.encadenado();
        return _toR;
    
    }


    
   
    //Regla 71
    private Encadenado encadenado() throws AsintacticoException{
       Encadenado _toR = null;
       if(this.actual.getType()==ClavesServices.TokenTypes.PP.ordinal()){
    	   _toR = this.varOMetodoEncadenado();
    	   _toR.setCadena(this.encadenado());
    	   
       }
       return _toR;
    
    }
    
    private Encadenado varOMetodoEncadenado() throws AsintacticoException{
    	Encadenado _toR = null;
        if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
            throw new AsintacticoException("Error Sintactico : se esperaba '.' y se encontro :"+this.actual.getLexema()+this.actual.getError());
        Token _aux = this.actual;
    	_toR = new NodoIdEncadenado(_aux);
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error Sintactico : se esperaba idMetVar y se encontro :"+this.actual.getLexema()+this.actual.getError());
        if(this.actual.getType()==ClavesServices.TokenTypes.PPA.ordinal()) {
        	ArrayList<NodoExpresion> _args = this.argsActuales();
        	_toR = new NodoLlamadaEncadenada(_aux,_args);
        	
        }
        return _toR;
    	
    }
    //Regla 72
    private void llamadaOIDEncadenado() throws AsintacticoException{
          if(!this.match(ClavesServices.TokenTypes.PP.ordinal()))
            throw new AsintacticoException("Error Sintactico: se esperaba un '.'(Puntuación Punto)"
                    + "  se encontro :"+this.actual.getLexema()+this.actual.getError());
          if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error Sintactico: se esperaba un 'idMetVar'"
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
    private NodoLlamadaDirecta llamadaMetodo() throws AsintacticoException{
            ////System.out.println("llamadaMetodo");
    	NodoLlamadaDirecta _toR = new NodoLlamadaDirecta(this.actual);
        if(!this.match(ClavesServices.TokenTypes.idMetVar.ordinal()))
            throw new AsintacticoException("Error Sintactico: se esparaba idMetVar, y se encontro : "+this.actual.getLexema()+this.actual.getError());
        _toR.setActualArgs(this.argsActuales());
        return _toR;
        
    }
    //Regla 75  
    private ArrayList<NodoExpresion> argsActuales() throws AsintacticoException{
    	ArrayList<NodoExpresion> _toR = new ArrayList<NodoExpresion>();
    	
        if(!this.match(ClavesServices.TokenTypes.PPA.ordinal()))
            throw new AsintacticoException("Error Sintactico: Se esperaba"
                    + "( , y se encontro : "+this.actual.getLexema()+this.actual.getError());
        this.listaExpresionAux(_toR);
        if(!this.match(ClavesServices.TokenTypes.PPC.ordinal()))
                throw new AsintacticoException("Error Sintactico: Se esperaba ) , "
                    + "y se encontro : "+this.actual.getLexema()+this.actual.getError());
        return _toR;
        
       

    }
    //Regla 76
    private void listaExpresionAux(ArrayList<NodoExpresion> _list) throws AsintacticoException{
        //Si leo parentesis que cierran entonces es vacio 
        if(this.actual.getType()!=ClavesServices.TokenTypes.PPC.ordinal())
            this.listaExpresion(_list);
    
    }
    
    //Regla 77
    private void listaExpresion(ArrayList<NodoExpresion> _list) throws AsintacticoException{
            ////System.out.println("listaExpresion");
        _list.add(this.expresion());
        if(this.match(ClavesServices.TokenTypes.PC.ordinal())){
            this.listaExpresion(_list);
            this.listaExpresionF(_list);
        }
    }
    
    //Regla 78
    private void listaExpresionF(ArrayList<NodoExpresion> _list) throws AsintacticoException{
        if(this.match(ClavesServices.TokenTypes.PC.ordinal()))
            this.listaExpresion(_list);
    
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