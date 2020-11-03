/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexico;


import buffer.Buffer;
import Claves.ClavesServices;
import java.io.FileNotFoundException;
import token.Token;
import token.TokenException;

/**
 *
 * @author Javier Amorosi
 */
public class Alexico {
    private  Token EOF;
    private int beginRow;
    private int cantTab; 
    private String lexema;
    private String estado;
    private Character charActual;
    private Buffer buffer;
    private ClavesServices mapServices;
    private boolean isEOF;
    
    //inicializo el Buffer, el lexema y el charActual.
    public Alexico(String fileName)throws TokenException{
        try{
            this.cantTab  = 0;
            this.beginRow = 0;
            this.charActual = null;
            this.lexema="";
            this.buffer = new Buffer(fileName);
            this.mapServices = new ClavesServices();
            this.EOF = new Token(ClavesServices.TokenTypes.EOF.ordinal(),"EOF");
            this.isEOF = false;
        }
        catch(FileNotFoundException e){
            throw new TokenException("No se pudo abrir el archivo : "+fileName);
        
        }
        
    }
    
    public Token nextToken() throws TokenException{
            //Leo y reconozco el estado si el caracter actual es nulo...
    		if(this.isEOF)
    			return this.EOF;
            if(this.charActual==null){
                this.consumirCaracter();
                //si l	lego a fin de archivo después de consumir carcter devuelvo el token fin de archivo
                if(this.charActual==null){
                	this.isEOF = true;
                    this.EOF.setLine(this.buffer.getLine());
                    return this.EOF ;
                }
              
            }
                                
            //si no reconocio el estado entonces quiere decir que hay un error.
            if(this.estado==null)
                throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : "+this.charActual+" no es simbolo valido \n [ERROR:"+this.charActual+"|"+this.buffer.getLine()+"] ");
            //Construyo el Token a ser retornado.    
            Token toR = null;
            //cicla hasta armar el Token a retornar si llega a fin de archivo retorna el respectivo token EOF.
            while((toR==null)){

                switch(estado){
                    
                    //Estado inicial para reconocer un caracter
                    case"caracter":{
                        this.consumirCaracter();                       
                        this.estado = "caracter2";
                        this.lexema+=this.charActual;
                        if(this.charActual=='\\'){
                        	//Trato de agregar el \ a los caracteres
                        	//this.lexema+='\\';
                            this.estado="caracterAux";
                        }
                        if (this.charActual=='\'') {
                            char aux = this.charActual;
                            this.limpiarBuffer();
                            this.buffer.consumirLinea();
                            throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" :"
                            		+ " "+aux+" no es un valido para formar un caracter \n"
                            				+ "[ERROR:"+aux+"|"+this.buffer.getLine()+"] ");                        	
                        }
                        
                        	
                      

                               
                        break;
                     
                    
                    }
                    case"caracterAux":{
                        this.consumirCaracter();
                       
                        if(this.charActual=='\'')
                            this.lexema = "" + this.charActual;
                        this.estado = "caracter2";
                        break;
                    
                    }
                    
                    case"caracter2":{

                        this.consumirCaracter();
             
                        if(this.charActual!=null && this.charActual=='\''){
                            toR = new Token(ClavesServices.TokenTypes.charLiteral.ordinal(),this.beginRow,this.lexema,this.buffer.getLine());
                            //LIMPIO EL BUFFER
                            this.limpiarBuffer();
                        }
                        else {
                            char aux = this.charActual;
                            this.limpiarBuffer();
                            this.buffer.consumirLinea();
                            throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : "+aux+" se esperaba ' para cerrar el caracter \n[ERROR:"+aux+"|"+this.buffer.getLine()+"] ");


                        	
                        }
                        break;
                    }
                  
                  /*REFACTORIZAR DE ACUERDO A LA DEFINICIÓN...
                    */  
                   case"identificadorClase":{
                       this.consumirCaracter();
                       //si es un espacio en blanco salto de línea fin de archivo o puntuacion
                       if(this.charActual==' ' ||   this.charActual=='\'' || this.charActual=='\t' || this.charActual=='\n' 
                               || this.charActual==null || esPuntuacion(this.charActual) || this.charActual=='"'){
                             toR = this.mapServices.getClaveToken(this.lexema);
                             char aux = this.charActual;
                                if(toR == null)
                                    toR = new Token(ClavesServices.TokenTypes.idClase.ordinal(),this.beginRow,this.lexema,this.buffer.getLine());
                                else{
                                    toR.setRow(this.beginRow);
                                    toR.setLine(this.buffer.getLine());
                                }
                                if(esPuntuacion(aux) || this.charActual=='\'' || this.charActual=='"')
                                    this.setState(this.charActual);
                                
                                else
                                this.limpiarBuffer();
                       }
                       else if(!Character.isDigit(this.charActual) && this.charActual!='_' && this.charActual!='%'  && !Character.isLetter(this.charActual)) {
                           char aux = this.charActual;
                    	   this.limpiarBuffer();
                           this.buffer.consumirLinea();
                           throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : "+aux+" se esperaba un caracter una letra o underscore \n[ERROR:"+aux+"|"+this.buffer.getLine()+"] ");
                       }
                        break;

                    }
                   
                   case"identificador":{

                      
                       this.consumirCaracter();

                       if(this.charActual==' ' ||   this.charActual=='\''  ||  this.charActual=='\t' || this.charActual=='\n' || this.charActual==null 
                               || esPuntuacion(this.charActual) || this.charActual=='"'){
                           //
                             toR = this.mapServices.getClaveToken(this.lexema);
                                if(toR == null)
                                    toR = new Token(ClavesServices.TokenTypes.idMetVar.ordinal(),this.beginRow,this.lexema,this.buffer.getLine());
                                else{
                                    toR.setRow(this.beginRow);
                                    toR.setLine(this.buffer.getLine());
                                }
                                if(this.esPuntuacion(this.charActual) ||  this.charActual=='\'' || this.charActual=='"')
                                    this.setState(this.charActual);
                                else    
                                    this.limpiarBuffer();
                       }
                       else if(!Character.isDigit(this.charActual)&& this.charActual!='_' && this.charActual!='%' && !Character.isLetter(this.charActual)){
                    	   char aux = this.charActual;
                    	   this.limpiarBuffer();
                           this.buffer.consumirLinea();
                           throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : "+aux+" se esperaba un caracter una letra o underscore \n[ERROR:"+aux+"|"+this.buffer.getLine()+"] ");
                           

                    	   
                       }
                        break;

                   }


                    case "comentario1":{

                        //Estoy en el estado de leer / de comentario 
                        this.consumirCaracter();
                         //llego a fin de archivo
                        if(this.charActual==null){
                        	this.isEOF = true;
                            this.EOF.setLine(this.buffer.getLine());
                            toR = this.EOF;
                        }
                        else{
                            //chequeo si es un comentario de una línea o multilinea. 
                            //El compilador sugiere refactorizar por un switch!!! a tener en cuenta...
                            if(charActual=='/')
                                this.estado = "comentario2";
                            else if(this.charActual=='*')
                                    this.estado = "comentarioM";
                                else
                                    this.estado=Claves.ClavesServices.TokenTypes.DIV.toString();
                                    //estoy en el caso de que leo un token de DIVISION!!


                        }    
                               
                                //throw new TokenException("Se esperaba / o * y no se encontro ninguno en linea : "+this.buffer.getLine()+"columna :"+this.buffer.getRow());
                        break;

                    }

                 

                    case "comentario2":{

                       this.consumirCaracter();
                        //llego a fin de archivo
                        if(this.charActual==null) {
                        	this.isEOF = true;
                            throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : comentario multilinea sin cerrar \n[ERROR:|"+this.buffer.getLine()+"] ");
                            }
                        if(this.charActual=='\n'){
                            this.limpiarBuffer();
                            this.consumirCaracter();
                        }
                        break;

                    }
                    
                    case "comentarioM":{
                        this.consumirCaracter();
                         //llego a fin de archivo
                        if(this.charActual==null) {
                        	this.isEOF = true;
                            throw new TokenException("Error léxico en linea :  comentario multilinea sin cerrar \n[ERROR:|"+this.buffer.getLine()+"] ");
                            }
                        else{
                            if(this.charActual=='*')
                                this.estado="comentarioMFinal";
                        }
                        break;
                      
                    
                    }
                    
                    case "comentarioMFinal":{
                        this.consumirCaracter();
                        if(this.charActual==null) {
                        	this.isEOF = true;
                            throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : comentario multilinea sin cerrar \n[ERROR:|"+this.buffer.getLine()+"] ");
                 
                        }
                        
                        if(this.charActual!='*') 
                        	this.estado="comentarioM";
                        //Lego al fin del comentario multilinea
                        if(this.charActual=='/') {
                        	this.limpiarBuffer();
                        	this.consumirCaracter();
                        	
                        }
                        

                       
                        break;
                           
                    
                    }
                    
                    case "string1":{
                        this.consumirCaracter();
                        //Si leo el salto de línea entonces informo el error
                        if(this.charActual=='\n') {
                        	char aux = this.charActual;
                        	this.limpiarBuffer();
                            this.buffer.consumirLinea();
                            throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : se esperaba"+'"'+" para cerrar la cadena \n[ERROR:|"+this.buffer.getLine()+"] ");

                        }
                         
                        //Si leó " entonces tengo que terminar la cadena
                         if(this.charActual=='"')
                            this.estado = "string2";
                         else{
                             //Chequeo si es multinea
                             if(this.charActual=='\\')
                                this.estado="stringM";
                             else
                             //Si llego a Fin de archivo en este estado es ERROR LEXICO!! Cambiar...
                             if(this.charActual == null){
                                 this.EOF.setLine(this.buffer.getLine());
                                 return this.EOF;
                             }
                             else
                                 //En caso de que no sea multilinea ni fin de archivo acumulo en el lexema. 
                                 this.lexema += this.charActual;
                         }
                        break;
                    }
                    
                    case"stringM" : {
                        this.consumirCaracter();
                        if(this.charActual=='n')
                            this.lexema+='\n';
                        else{
                            this.lexema+='\\';
                            this.lexema+=this.charActual;
                        
                        }
                        this.estado = "string1";
                        break;
                            
                    
                    }

                    case "string2":{
                        toR = new Token(ClavesServices.TokenTypes.stringLiteral.ordinal(),this.beginRow,this.lexema,this.buffer.getLine());
                        //limpio el Lexema actual
                        this.limpiarBuffer();
                        this.consumirCaracter();
                        break;
                    }
                    
                    case "entero":{

                       if(Character.isDigit(this.charActual))
                            this.consumirCaracter();
                       //Si es un caracter devuelvo la excepción de entero mal formado...
                       else if(Character.isLetter(this.charActual)) {
                    	   char aux = this.charActual;
                    	   this.limpiarBuffer();
                           this.buffer.consumirLinea();
                           throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : "+aux+" se esperaba un digito \n[ERROR:"+aux+"|"+this.buffer.getLine()+"] ");
                       }
                       //si no es un caracter entonces tengo que retornar el token resultante
                            else{
                                toR =  new Token(ClavesServices.TokenTypes.NUM.ordinal(),this.beginRow,this.lexema,this.buffer.getLine());
                                //no siempre tengo que limpiar el buffer...
                                this.setState(this.charActual);
                            }
                       
                       break;

                   }
                   //Estados de Puntuacion : 
                   //Puntuacion Parentesis abierto
                    case "PPA":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.PPA.ordinal(),this.beginRow,"(",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                    //Puntuacion parentesis cerrado
                    case "PPC":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.PPC.ordinal(),this.beginRow,")",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                    //Puntuacion corchete abierto
                    case "PCA":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.PCA.ordinal(),this.beginRow,"[",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                    //puntuacion corchete que cierra
                    case "PCC":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.PCC.ordinal(),this.beginRow,"]",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                    //Puntuacion Llave que abre
                    case "PLA":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.PLA.ordinal(),this.beginRow,"{",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                    //puntuacion llave que cierra
                    case "PLC":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.PLC.ordinal(),this.beginRow,"}",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                    
                    //puntuación punto y coma
                    case "PPY":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.PPY.ordinal(),this.beginRow,";",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                    //puntuacion coma
                    case "PC":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.PC.ordinal(),this.beginRow,",",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                    //puntuacion punto
                     case "PP":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.PP.ordinal(),this.beginRow,".",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                     
                    //Estados de OPERADOS
                    case "MAY":{
                         this.consumirCaracter();
                         char aux = this.charActual;
                         if(this.charActual=='=')
                             toR = new Token(Claves.ClavesServices.TokenTypes.MAYIGUAL.ordinal(),
                                             this.beginRow,">=",this.buffer.getLine());
                         else
                              toR = new Token(Claves.ClavesServices.TokenTypes.MAY.ordinal(),
                                             this.beginRow,">",this.buffer.getLine());
                         
                         //guardo el caracter que acabo de limpiar...
                         if(this.charActual!='=')
                             this.setState(aux);
                         else 
                             this.limpiarBuffer();
                         
                             
                         break;
                     }
                     
                    case "MEN":{
                         this.consumirCaracter();
                         char aux = this.charActual;
                         if(this.charActual=='=')
                             toR = new Token(Claves.ClavesServices.TokenTypes.MENIGUAL.ordinal(),
                                             this.beginRow,"<=",this.buffer.getLine());
                         else
                              toR = new Token(Claves.ClavesServices.TokenTypes.MEN.ordinal(),
                                             this.beginRow,"<",this.buffer.getLine());
                         
                         //guardo el caracter que acabo de limpiar...
                         if(this.charActual!='=')
                             this.setState(aux);
                         else 
                             this.limpiarBuffer();
                         
                             
                         break;
                     }
                    //Reconoce el IGUAL de asinacion como el igual igual de operador...tienen tipos diferentes 
                    case "IGUAL":{
                         this.consumirCaracter();
                         
                         if(this.charActual=='='){
                             toR = new Token(Claves.ClavesServices.TokenTypes.IGUALIGUAL.ordinal(),
                                             this.beginRow,"==",this.buffer.getLine());
                             this.limpiarBuffer();
                         }
                         else{
                              toR = new Token(Claves.ClavesServices.TokenTypes.IGUAL.ordinal(),
                                             this.beginRow,"=",this.buffer.getLine());
                              this.setState(this.charActual);
                         }
                         
                             
                         break;
                     }
                    case "NOT":{
                         this.consumirCaracter();
                         char aux = this.charActual;
                         if(this.charActual=='=')
                             toR = new Token(Claves.ClavesServices.TokenTypes.DIST.ordinal(),
                                             this.beginRow,"!=",this.buffer.getLine());
                         else
                              toR = new Token(Claves.ClavesServices.TokenTypes.NOT.ordinal(),
                                             this.beginRow,"!",this.buffer.getLine());
                         
                         //guardo el caracter que acabo de limpiar...
                         if(aux!='=')
                             this.setState(aux);
                         else 
                             this.limpiarBuffer();
                         
                             
                         break;
                     }
                    case "AND":{
                        this.consumirCaracter();
                        if(this.charActual=='&')
                            toR = new Token(Claves.ClavesServices.TokenTypes.AND.ordinal(),this.beginRow,"&&",this.buffer.getLine());
                        else {
                        	char aux = this.charActual;
                        	this.limpiarBuffer();
                            this.buffer.consumirLinea();
                            throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : "+aux+" se esperaba & \n[ERROR:"+aux+"|"+this.buffer.getLine()+"] ");

                        }
                        this.limpiarBuffer();
                        break;
                    }
                    
                     case "OR":{
                        this.consumirCaracter();
                        if(this.charActual=='|')
                            toR = new Token(Claves.ClavesServices.TokenTypes.OR.ordinal(),this.beginRow,"||",this.buffer.getLine());
                        else {
                        	char aux = this.charActual;
                        	this.limpiarBuffer();
                            this.buffer.consumirLinea();
                            throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : "+aux+" se esperaba | \n[ERROR:"+aux+"|"+this.buffer.getLine()+"] ");

                        }
                        this.limpiarBuffer();
                        break;
                    }
                     
                    
                    
                    case "MAS":{
                    	this.consumirCaracter();
                    	if(this.charActual=='=') {
                            toR = new Token(Claves.ClavesServices.TokenTypes.IGUALMAS.ordinal(),this.beginRow,"+=",this.buffer.getLine());
                            this.limpiarBuffer();
                            
                    	}
                    	else {
                    		toR = new Token(Claves.ClavesServices.TokenTypes.MAS.ordinal(),this.beginRow,"+",this.buffer.getLine());
                    		this.setState(this.charActual);
                    	}
                        break;
                    }
                    case "MENOS":{
                    	this.consumirCaracter();
                    	if(this.charActual=='=') {
                            toR = new Token(Claves.ClavesServices.TokenTypes.IGUALMENOS.ordinal(),this.beginRow,"-=",this.buffer.getLine());
                            this.limpiarBuffer();
                            
                    	}
                    	else {
                            toR = new Token(Claves.ClavesServices.TokenTypes.MENOS.ordinal(),this.beginRow,"-",this.buffer.getLine());
                            this.setState(this.charActual);
                    		
                    	}
                        break;
                    }
                    case "DIV":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.DIV.ordinal(),this.beginRow,"/",this.buffer.getLine());
                        //restauro el valor actual en el caso de que venga de un comentario y haya consumido otro caracter...
                        this.setState(this.charActual);
                        break;
                    }
                    
                    case "POR":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.POR.ordinal(),this.beginRow,"*",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                     
                    case "MOD":{
                        toR = new Token(Claves.ClavesServices.TokenTypes.MOD.ordinal(),this.beginRow,"%",this.buffer.getLine());
                        this.limpiarBuffer();
                        break;
                    }
                   
                   
                   
                    case "blanco":{
                        //hago distinciones sobre las columnas...
                        switch(this.charActual){
                            
                            //Se asume que el tab son 8 espacios... ya sumo uno en set State
                            case '\n':{
                                this.cantTab=0;
                                break;
                            }
                            case'\t':{
                                this.cantTab++;
                                break;
                            }
                            
                            default:break;
                        }
                        this.limpiarBuffer();
                        this.consumirCaracter();
                        break;
                    }

                    case "nulo":{
                        //en el caso de que haya llegado a fin de archivo.
                        this.EOF.setLine(this.buffer.getLine());
                        toR = this.EOF;
                        break;

                    }
                    default:{
                    	char aux = this.charActual;
                    	this.limpiarBuffer();
                        this.buffer.consumirLinea();
                        throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : "+aux+" no es simbolo valido \n [ERROR:"+aux+"|"+this.buffer.getLine()+"] ");
        
                    }
                }
            }

            return toR;
    }
    
    //Actualiza el caracter Actual.
    //si el estado es nulo reconoce el estado...
    //Si forma parte del estado valdo entonces lo adiciona al lexema
    //Es un problema que se encargue este método de agregarlo al lexema
    //Ya que tiene que hacer comparasiones y eso insume tiempo !!!
    //Futuro Refactoring!!!
    private void consumirCaracter() throws TokenException{
            this.charActual = this.buffer.nextBuffer();    
            //si el estado es nulo entonces lo reconoce
            if(this.estado == null ){
                this.reconocerEstado();
                //falla en el caso del tabl por eso se hace *8 la cantidad de tabs...
                
            }
            //en cualquiera de estos casos no lo agrega al lexema...
            if(this.mapServices.estadoValido(this.estado)&& this.charActual!=null
                    && this.charActual!='\t' && this.charActual!='\n' && this.charActual!=' ' 
                    && !this.esPuntuacion(this.charActual) &&  this.charActual!='\''
                    &&  this.charActual!='"')
                this.lexema+=this.charActual;
      
    }
    
    public Token getEOF(){
        this.EOF.setLine(this.buffer.getLine());
        return this.EOF;
    }
    
    private void limpiarBuffer(){
        this.lexema="";
        this.estado=null;
        this.charActual=null;
    }
    //metodo que responde tanto para la puntuación como para los operandos...
    private boolean esPuntuacion(char c){
        switch(c){
            case';':return true;
            case')':return true;
            case'(':return true;
            case'{':return true;
            case'}':return true;
            case'[':return true;
            case']':return true;
            case',':return true;
            case'.':return true;
            //operadores
            case'!':return true;
            case'=':return true;
            case'+':return true;
            case'-':return true;
            case'*':return true;
            case'/':return true;
            case'>':return true;
            case'<':return true;
            case'&':return true;
            case'|':return true;
        
            default:return false;
            
        }
    }
    
    
    private void setState(char aux) throws TokenException{
        this.limpiarBuffer();
        this.charActual=aux;
        this.reconocerEstado();
        //si reconozco un nuevo estado devo actualizar las columnas
        if(this.mapServices.estadoValido(this.estado)&& this.charActual!='\t' && this.charActual!='\n' && this.charActual!=' ' && !this.esPuntuacion(this.charActual))
                this.lexema+=this.charActual;

    }
    
    private void reconocerEstado ()throws TokenException{
        //Se cambio esto para evitar lo anterior...
            this.estado = this.mapServices.mapeoEstados().get(this.charActual);
            if(this.estado==null) {
            	char aux = this.charActual;
            	this.limpiarBuffer();
                this.buffer.consumirLinea();
                throw new TokenException("Error léxico en linea : "+this.buffer.getLine()+" : "+aux+" no es simbolo valido \n [ERROR:"+aux+"|"+this.buffer.getLine()+"] ");
            }
            else 
                this.beginRow = this.buffer.getRow() + this.cantTab*8 - this.cantTab;
    
    }

}
