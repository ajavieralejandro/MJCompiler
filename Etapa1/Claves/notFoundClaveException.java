/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Claves;

/**
 *
 * @author Javier Amorosi
 */
public class notFoundClaveException extends Exception {
    public notFoundClaveException(String msg){
    super(msg);
}
    notFoundClaveException(){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
