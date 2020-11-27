package semantico;

import Claves.ClavesServices;

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

	@Override
	public String getFormaMetodo() {
		//Se decide que es dynamic aunque esto no sea cierto, para que las unidades no ingresen
		return ClavesServices.TokenTypes.DYNAMIC.toString();
	}
 
    
}
