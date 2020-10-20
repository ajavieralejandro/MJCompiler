/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Claves;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class ClavesServices {
    
    
    //Las que terminan en C y se omitieron es porque es idem pero cierra...
    //PPA significa Puntuación Parentesis Abre---
    //PLA puntuacion llave abierta
    //PPY puntuacion punto y coma
    //PC puntuacion coma
    //PP puntuacion punto
    //PCA puntuacion corchete abierto
    public enum TokenTypes{
        //Palabras claves
        CLASS,STRING,PUBLIC,IF,THIS,EXTENDS,BOOLEAN,PRIVATE,ELSE,NEW,
        STATIC,CHAR,VOID,WHILE,TRUE,DYNAMIC,INT,NULL,RETURN,FALSE,
        MAIN,FINAL,PROTECTED,
        //identificadores
        idClase,idMetVar,
        //puntuacion
        NUM,PPA,PPC,PLA,PLC,PPY,PC,PP,PCA,PCC,EOF
        //operadores
        ,MAY,MEN,NOT,IGUAL,MAYIGUAL,MENIGUAL,DIST,MAS,MENOS,POR,DIV,AND,OR,IGUALIGUAL,MOD,
        //Literales
        charLiteral,stringLiteral,
        //ASIGNACION
        IGUALMAS,IGUALMENOS,
        //USADOS POR LA TS
        ARRAY,OBJECT,
        ;
   
    
    }
    
    //Arreglo de palabras claves en miniJava.
    //Se utilizo el Diccionario ya que primeros y siguientes pueden tener más de un token type por cada cadena. 

    private Map<String,Token> claves;
    private Map<Character,String> mapeoEstados;
    /*
    Inicializa el mapeo con los valores de las palabras claves.
    */
    public ClavesServices(){
        ClavesServices.TokenTypes.values();
        
        
       //Inicializo el Mapeo de palabras claves y Tokens
        this.claves = new HashMap<String,Token>();
        this.claves.put("class",new Token(TokenTypes.CLASS.ordinal(),"class"));
        //this.claves.put("protected",new Token(TokenTypes.PROTECTED.ordinal(),"protected"));
        //this.claves.put("final",new Token(TokenTypes.FINAL.ordinal(),"final"));
        this.claves.put("boolean",new Token(TokenTypes.BOOLEAN.ordinal(),"boolean"));
        this.claves.put("String",new Token(TokenTypes.STRING.ordinal(),"String"));
        this.claves.put("public",new Token(TokenTypes.PUBLIC.ordinal(),"public"));
        this.claves.put("if",new Token(TokenTypes.IF.ordinal(),"if"));
        this.claves.put("this",new Token(TokenTypes.THIS.ordinal(),"this"));
        this.claves.put("extends",new Token(TokenTypes.EXTENDS.ordinal(),"extends"));
        this.claves.put("private",new Token(TokenTypes.PRIVATE.ordinal(),"private"));
        this.claves.put("else",new Token(TokenTypes.ELSE.ordinal(),"else"));
        this.claves.put("new",new Token(TokenTypes.NEW.ordinal(),"new"));
        this.claves.put("static",new Token(TokenTypes.STATIC.ordinal(),"static"));
        this.claves.put("char",new Token(TokenTypes.CHAR.ordinal(),"char"));
        this.claves.put("void",new Token(TokenTypes.VOID.ordinal(),"void"));
        this.claves.put("while",new Token(TokenTypes.WHILE.ordinal(),"while"));
        this.claves.put("true",new Token(TokenTypes.TRUE.ordinal(),"true"));
        this.claves.put("dynamic",new Token(TokenTypes.DYNAMIC.ordinal(),"dynamic"));
        this.claves.put("int",new Token(TokenTypes.INT.ordinal(),"int"));
        this.claves.put("null",new Token(TokenTypes.NULL.ordinal(),"null"));
        this.claves.put("return",new Token(TokenTypes.RETURN.ordinal(),"return"));
        this.claves.put("false",new Token(TokenTypes.FALSE.ordinal(),"false"));
        //inicializo el mapeo de estados
        this.mapeoEstados = new HashMap<Character,String>();
        this.mapeoEstados.put('/',"comentario1");
        this.mapeoEstados.put('"',"string1");
        int i;
        for(i=0;i<10;i++){
            this.mapeoEstados.put(Character.forDigit(i,10),"entero");
            
        }
        this.mapeoEstados.put(null,"nulo");
        this.mapeoEstados.put('\n',"blanco");
        this.mapeoEstados.put('\t',"blanco");
        this.mapeoEstados.put(' ',"blanco");
        //puntuacion
        this.mapeoEstados.put('(',ClavesServices.TokenTypes.PPA.toString());
        this.mapeoEstados.put(')',ClavesServices.TokenTypes.PPC.toString());
        this.mapeoEstados.put('{',ClavesServices.TokenTypes.PLA.toString());
        this.mapeoEstados.put('}',ClavesServices.TokenTypes.PLC.toString());
        this.mapeoEstados.put(';',ClavesServices.TokenTypes.PPY.toString());
        this.mapeoEstados.put(',',ClavesServices.TokenTypes.PC.toString());
        this.mapeoEstados.put('.',ClavesServices.TokenTypes.PP.toString());
        //this.mapeoEstados.put('[',ClavesServices.TokenTypes.PCA.toString());
        //this.mapeoEstados.put(']',ClavesServices.TokenTypes.PCC.toString());
        //Operados
        this.mapeoEstados.put('>',ClavesServices.TokenTypes.MAY.toString());
        this.mapeoEstados.put('<',ClavesServices.TokenTypes.MEN.toString());
        this.mapeoEstados.put('!',ClavesServices.TokenTypes.NOT.toString());
        this.mapeoEstados.put('=',ClavesServices.TokenTypes.IGUAL.toString());
        this.mapeoEstados.put('+',ClavesServices.TokenTypes.MAS.toString());
        this.mapeoEstados.put('-',ClavesServices.TokenTypes.MENOS.toString());
        this.mapeoEstados.put('*',ClavesServices.TokenTypes.POR.toString());
        //this.mapeoEstados.put('/',ClavesServices.TokenTypes.DIV.toString());
        this.mapeoEstados.put('&',ClavesServices.TokenTypes.AND.toString());
        this.mapeoEstados.put('|',ClavesServices.TokenTypes.OR.toString());
        this.mapeoEstados.put('%',ClavesServices.TokenTypes.MOD.toString());



    
        
        char c;
        for(c='A';c<='Z';c++){
            this.mapeoEstados.put(c,"identificadorClase");
        }
         for(c='a';c<='z';c++){
            this.mapeoEstados.put(c,"identificador");
        }
        this.mapeoEstados.put('\'',"caracter");
   
    }
    
    /**
     *
     * @return
     */
    public Map<Character,String> mapeoEstados(){
        return this.mapeoEstados;

    }
    /*
    Retorna el Token asociado al Lema, sin la columna, arroja una excepción
    en caso de no encontrar la palabra clave.
    */
   
    public Token getClaveToken(String lexema){
     
         Token toR=  this.claves.get(lexema);
         return toR;
        
 
    }
    
    public String getClaveEstado(Character c){
        return this.mapeoEstados.get(c);
    }
    
   
    
   
    
    public boolean estadoValido(String c){
       //    this.estado!="blanco" && this.estado!="nulo" && this.estado!="comentario" &&this.estado!="literal"&& this.estado!="string1"
       //FIJARME DE QUE NO CONTAMINE LA LÓGICA
       
    if(c==null)
        return false;
    switch(c){
            case"blanco":return false;
            case "nulo":return false;
            case"comentario1":return false;
            case"caracter":return false;
            case"caracter2":return false;
            case"string1":return false;
            case"stringM":return false;
            
            default: return true;
        }
    
    }
    
    
    
}
