package semantico2expresiones;

import java.util.ArrayList;

import semantico.Clase;
import semantico.Metodo;
import semantico.TDS;
import semantico.TipoBase;
import semantico2.ASTException;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class NodoLlamadaEstatica extends NodoPrimario {
    
    private Token idClase;
    private Token id;
    private ArrayList<NodoExpresion> actualArgs;
    private final TDS ts;
    
    public NodoLlamadaEstatica(){
        this.ts = TDS.getInstance();
    }

    public Token getIdClase() {
        return idClase;
    }

    public void setIdClase(Token idClase) {
        this.idClase = idClase;
    }

    public Token getId() {
        return id;
    }

    public void setId(Token id) {
        this.id = id;
    }

    public ArrayList<NodoExpresion> getActualArgs() {
        return actualArgs;
    }

    public void setActualArgs(ArrayList<NodoExpresion> actualArgs) {
        this.actualArgs = actualArgs;
    }
    

    @Override
    public Token getToken() {
        return this.id;
    }

    @Override
    public TipoBase check() throws ASTException {
        
        
        if(!ts.getClases().containsKey(this.idClase.getLexema()))
            throw new ASTException("Error semantico : La clase a la cual se le pide un metodo estático no esta declarada"
                    + this.idClase.getError());
        Clase aux = ts.getClases().get(this.idClase.getLexema());
        if(!aux.getMetodos().containsKey(this.id.getLexema()))
            throw new ASTException("Error semantico : la clase : "+aux.getName()+"no tiene ningun metodo : "+this.id.getLexema()
                                  +this.id.getError());
        Metodo met = aux.getMetodos().get(id.getLexema());
        if(!met.getFormaMetodo().equals("static"))
            throw new ASTException("Error semantico : El metodo : "+met.getNombre()+" no es de tipo estatico, se encontro tipo: "+met.getFormaMetodo()
                    +this.id.getError());
        
        //Ahora tengo que chequear los argumentos 
           //chequeo los arguementos
         ArrayList<TipoBase> list = new ArrayList<TipoBase>();
         for(NodoExpresion e : this.actualArgs){
             list.add(e.check());
         }
         
         
         if(list.size()!=met.getParametros().size())
             throw new ASTException("Error semantico : La cantidad de parametros que espera el metodo:"
                     +met.getNombre()+ "es distinta a la cantidad de parametros con la cual el metodo es invocado."
                      + this.id.getError());
         
            if(!met.igualTipoParametros(list))
             throw new ASTException("Error semantico : Los tipos con los que se invoca al constructor,"
                     + "no son los esperados por el constructor"+this.id.getError());
        
        
        return met.getRetorno();
        
        
    }
    
}