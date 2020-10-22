/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Claves.ClavesServices;
import lexico.Alexico;
import buffer.Buffer;
import token.TokenException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class MiniJavaCompiler {
	
	protected int errores; 

     /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(("Bienvenido al Analizador Lexico"));
        String file = null;
        String fileOut = null;
        PrintWriter writer = null;
        if(args.length==1)
            file = args[0];
        if(args.length==2){
            file = args[0];
            fileOut=args[1];
        }
 
        if(file==null || args.length>2)
            System.out.println("Error en los parametros, vuelva a invocar el programa con el archivo correcto o la cantidad de parametros correcta.");
        else{
                System.out.println("Se va a leer el archivo : "+file);
                if(fileOut!=null)
                    System.out.println("El archivo donde se mostrata el resultado final sera¡ : "+fileOut);
            
            // TODO code application logic here
  
            Alexico lexico = null;
            int errores = 0;
           
            try{
                if(fileOut!=null)
                    writer = new PrintWriter(fileOut, "UTF-8");                       
               lexico = new Alexico(file);
                Token aux= null;
                try {
                    aux= lexico.nextToken();
                }
                catch(TokenException e) {
                	
                    System.out.println(e.getMessage()); 
                    errores++;
                    //Creo un token de Error para que no me ingrese en el ciclo 
                    aux = new Token(-1,"Error!");
                }


                while(aux==null || aux.getType()!=ClavesServices.TokenTypes.EOF.ordinal()){
                    if(fileOut==null){
                    	if(aux.getType()!=-1)
                    	System.out.println("("+ClavesServices.TokenTypes.values()[aux.getType()]+","+aux.getLexema()+","+aux.getLine()+")");
                    }
                    else{
                    	if(aux.getType()!=-1)
                        writer.println("("+ClavesServices.TokenTypes.values()[aux.getType()]+","+aux.getLexema()+","+aux.getLine()+")");
                    
                    }
                    
                    try {
                        aux= lexico.nextToken();
                    }
                    catch(TokenException e) {
                    	//Seteo el token a null si se produjo un error
                    	aux = new Token(-1,"Error!");
                        System.out.println(e.getMessage()); 
                        errores++;
                    }


            }
                if(errores==0)
                	System.out.print("El analizador Lexico llego a EOF sin encontrar errores.");
                else 
                	System.out.print("El analizador Lexico llego a EOF con : "+errores+"errores");

                	
                if(fileOut!=null)
                    writer.close();

            }
            catch (FileNotFoundException ex) {
                System.out.println("Error al crear el archivo. ");
            } catch (UnsupportedEncodingException ex) {
                System.out.println("Formato de codificacion especificado no valido para el tipo de archivo.");

            }
            
            catch(TokenException e){
                System.out.println(e.getMessage());
           
            }
        
        }
    
        
    
    }
    
   
    

}
