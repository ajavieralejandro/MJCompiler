import lexico.Alexico;
import semantico.ASemanticoException;
import Sintaxis.*;
import buffer.Buffer;
import Claves.ClavesServices;
import token.TokenException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class MiniJavaCompiler {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(("Bienvenido al Analizador Sintactico"));
        String file = null;
        String fileOut = null;
        PrintWriter writer = null;
        if(args.length==1)
            file = args[0];
 
        if(file==null || args.length>1)
            System.out.println("Error en los parametros, vuelva a invocar el programa con el archivo correcto o la cantidad de parametros correcta.");
        else{
                System.out.println("Se va a leer el archivo : "+file);
                if(fileOut!=null)
                    System.out.println("El archivo donde se mostrata el resultado final serÃ¡ : "+fileOut);
            

           
            try{
                if(fileOut!=null)
                    writer = new PrintWriter(fileOut, "UTF-8");                       
              
                Asintactico nuevo = new Asintactico(file);
                nuevo.analize();
                //cierro el archivo de escritura si existe
                if(fileOut!=null)
                    writer.close();

            }
            catch (FileNotFoundException ex) {
                System.out.println("Error al crear el archivo. ");
            } catch (UnsupportedEncodingException ex) {
                System.out.println("Formato de codificaciÃ³n especificado no valido para el tipo de archivo.");

            }

            catch(TokenException e){
                System.out.println(e.getMessage());
            }
            catch(AsintacticoException e){
                System.out.println(e.getMessage());
            }
            catch(ASemanticoException e){
                System.out.println(e.getMessage());
            }
        
        }
        
    
    }
    
}
