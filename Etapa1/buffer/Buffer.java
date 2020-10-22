/*
 * 
 */
package buffer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase encargada de leer del buffer del archivo.
 * @author Javier Amorosi
 */
public class Buffer {
    
    private String fileName;
    private String line;
    private int lineNumber;
    private int i;
    private BufferedReader bufferedReader;
    private FileReader fileReader;
    private boolean isEOF;
    
    public Buffer(String fileName) throws FileNotFoundException{
      
            //inicializa el número de línea en 0.
            this.isEOF = false;
            this.lineNumber = 0;
            this.fileName = fileName;
            this.fileReader=  new FileReader(this.fileName);
            // Always wrap FileReader in BufferedReader.
            this.bufferedReader = new BufferedReader(fileReader);
            i=0;
            this.consumirLinea();
       
    }
    
    
    public Character nextBuffer(){
        //si todavía no leyo ninguna línea la consume.
    	
        Character toR;
        if(line==null && !this.isEOF)
            consumirLinea();  
        //si la linea es nula o llegue a fin de archivo
        if(this.isEOF || line==null )
            toR= null;
        else if(this.i<line.length())
            toR = line.charAt(i++);
        else{
            //seteo la linea como nula y retorno el salto de línea. 
            this.line = null;
            toR ='\n';
        }
           
        return toR;
  
    }

    /**
     * 
     * @return columna del caracter que leyó
     */
    public int getRow(){
        return this.i;
    }
    
    /**
     * 
     * @return linea del caracter que leyo 
     */
    
    public int getLine(){
        return this.lineNumber;
    }
    
    
    public void consumirLinea(){
        this.i = 0;
          try {
            // FileReader reads text files in the default encoding.
          
            this.line = bufferedReader.readLine();
           
            
     
            //llego a fin de archivo y hay que cerrar el buffer
            if(this.line == null) {
               bufferedReader.close();
               this.isEOF = true;
            }
            else
                this.lineNumber++;
                              
        }
        
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");
            
            // Or we could just do this: 
            // ex.printStackTrace();
        }
    }
 
}
