package semantico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import semantico2.ASTException;
import semantico2.NodoBloque;
import semantico2.NodoSentencia;
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
    private NodoBloque bloque;


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
    
    public Map<String,Parametro> getParamMap(){
    	return this.parametros;
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
                    +v.getToken().getLine()+" ,valor :"+v.getToken().getLexema()+v.getToken().getError());
        //Se asume que se insertan en orden
        v.setUbicacion(this.parametros.size());
        this.parametros.put(v.getNombre(), v);
      

    }
    
    //Testear, hay que tener la certeza de que las dos colecciones tienen la misma cantidad
    public boolean igualTipoParametros(Collection<TipoBase> tipos){
        
        boolean toR = true;
        Iterator<TipoBase> it = tipos.iterator();
        for(Parametro p : this.getParametros()){
            if(it.hasNext())
                if(!p.getTipo().esCompatible(it.next())){
                    toR = false;
                    break;
                }           
        }
        
        return toR;
    
    }
    
    
    public abstract void controlDeclaraciones() throws ASemanticoException;
    public void controlSentencias() throws ASTException{
    	if(this.bloque!=null)
	    	for(NodoSentencia n : this.bloque.getSentencias()) {
	    		System.out.println("El valor de n es : "+n);
	    		if(n!=null)
	    			n.check();
	    	}
    }
    public abstract String getFormaMetodo();

	public NodoBloque getBloque() {
		return bloque;
	}

	public void setBloque(NodoBloque bloque) {
		this.bloque = bloque;
	}

        
}
