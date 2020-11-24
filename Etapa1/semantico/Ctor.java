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
    
    public void ChequearTipos() throws ASemanticoException{
    	
    }
    

    @Override
    public void ControlDeclaraciones() throws ASemanticoException {
  
    	if(!this.isPredefinido() && !this.getClase().getName().equals(this.getNombre()))
    		throw new ASemanticoException("Error Semantico : El nombre del constructor no coincide con el de la clase, se encontro :"+this.getNombre()+"."+this.getId().getError());

    }
 
    
}
