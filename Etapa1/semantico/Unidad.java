package semantico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public abstract class Unidad {
    
    private String nombre; 
    private Token id;
    private final Map<String,Parametro> parametros;
    private Clase clase;


    public Unidad(){
        
        this.parametros = new HashMap();

    }
    
    public void chequeoTipos() throws ASemanticoException{
         for(Parametro p : this.parametros.values()){
            if(!p.getTipo().esTipoValido())
                throw new ASemanticoException("Error semantico: "
                        + "Parametro no es de tipo valido,"+
                        p.getTipo().getToken().getError());
        }
    
    }
    
       
    public boolean igualParametros(Collection<Parametro> c){
        boolean toR = true;
        for(Parametro p : c){
            if(!this.parametros.get(p.getNombre()).equals(p))
                toR = false;
                break;
        }
        
        return toR;
    }
    
    public Collection<Parametro> getParametros(){
        return this.parametros.values();
    }
    
    public Token getId() {
        return id;
    }

    public void setId(Token id) {
        this.id = id;
        this.nombre = id.getLexema();
    }
    
      public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Clase getClase() {
        return clase;
    }
    
    public Token getToken(){
        return this.id;
    }

    public void setClase(Clase clase) {
        this.clase = clase;
    }
    
    public void insertarArgumento(Parametro v) throws ASemanticoException{
        //Chequeamos que el nombre del parametro no sea el mismo que otro
        if(this.parametros.containsKey(v.getNombre()))
            throw new ASemanticoException("Nombre de parametro repetido en linea :"
                    +v.getToken().getLine()+" ,valor :"+v.getToken().getLexema());
        //Se asume que se insertan en orden
        v.setUbicacion(this.parametros.size());
        this.parametros.put(v.getNombre(), v);
      

    }
    
    
    public abstract void ControlDeclaraciones() throws ASemanticoException;
        
}
