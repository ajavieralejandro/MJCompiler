package semantico;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class Parametro extends VarMetodo {
    
    private int ubicacion;

    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
    }
    
    public Parametro(Tipo tipo, String nombre, Token token) {
        super(tipo, nombre, token);
    }
    
    public boolean equals(Parametro p){
        boolean toR = false;
        if(p!=null){
            if(this.getNombre().equals(p.getNombre())
                    && this.getTipo().getTipo().equals(p.getTipo().getTipo())
                    && this.getUbicacion()== p.getUbicacion())
                toR = true;
        }
        return toR;
    }
    
}
