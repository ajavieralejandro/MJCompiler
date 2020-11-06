package semantico;

import token.Token;

/**
 *
 * @author Javier Amorosi
 * 
 */
public class VariableInstancia extends Variable {
    
    private String visibilidad;

 
    public VariableInstancia(Tipo tipo, String nombre,Token token) {
        super(tipo, nombre,token);
    }
    
       public String getVisibilidad() {
        return visibilidad;
    }

    public void setVisibilidad(String visibilidad) {
        this.visibilidad = visibilidad;
    }
    
    public void imprimir(){
        System.out.println("Variable de instancia : "+this.getNombre());
        System.out.println("visibilidad : "+this.getVisibilidad());
        System.out.println("Tipo : "+this.getTipo().getTipo());

        

    }
    
    
}
