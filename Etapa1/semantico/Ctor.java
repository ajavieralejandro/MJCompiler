package semantico;


/**
 *
 * @author Javier Amorosi
 */
public class Ctor extends Unidad {
    
   
    
    private boolean predefinido;

    public boolean isPredefinido() {
        return predefinido;
    }

    public void setPredefinido(boolean predefinido) {
        this.predefinido = predefinido;
    }
    

    @Override
    public void ControlDeclaraciones() throws ASemanticoException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
