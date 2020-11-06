package semantico;



/**
 *
 * @author Javier Amorosi
 */
public class Metodo extends Unidad {
    
    private TipoBase retorno;
    private String formaMetodo; 
    //private String visibilidad;
    private boolean esfinal;
    
    public Metodo(){
        this.esfinal = false;
    }
 

 
    /*public String getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(String visibilidad) {
        this.visibilidad = visibilidad;
    */

    public boolean isEsfinal() {
        return esfinal;
    }

    public void setEsfinal(boolean esfinal) {
        this.esfinal = esfinal;
    }
   

    public TipoBase getRetorno() {
        return retorno;
    }

    public void setRetorno(TipoBase retorno) {
        this.retorno = retorno;
    }
    
    
    public String getFormaMetodo() {
        return formaMetodo;
    }

    public void setFormaMetodo(String formaMetodo) {
        this.formaMetodo = formaMetodo;
    }
    
    @Override
    public void ControlDeclaraciones() throws ASemanticoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
}