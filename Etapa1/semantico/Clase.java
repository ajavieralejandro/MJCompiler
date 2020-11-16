package semantico;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Claves.ClavesServices;
import token.Token;

/**
 *
 * @author Javier Amorosi
 */
public class Clase {
    
    private String padre;
    private boolean consolidada; 
    private Token token ;
    private final TDS ts;
    private final Map<String,VariableInstancia> variablesInstancia;
    private final List<Ctor> constructores;
    private final Map<String,Metodo> metodos;
    
    
    
    
     public Clase(Token token,TDS ts){
        this.ts = ts;
        this.token = token;
        this.variablesInstancia = new HashMap<>();
        this.constructores = new ArrayList<Ctor>();
        this.metodos = new HashMap<String, Metodo>();
        this.consolidada = false;
        
        
        }
     
     
    

    public boolean isConsolidada() {
        return consolidada;
    }

    public void setConsolidada(boolean consolidada) {
        this.consolidada = consolidada;
    }
     
     //Por el momento permite agregar un solo constructor
     public void insertarConstructor(Ctor c) throws ASemanticoException{
         if(this.constructores.size()>0)
             throw new ASemanticoException("Error Semantico : constructor duplicado ,"
             + c.getId().getError());
         this.constructores.add(c);
     
     }
     
     public void controlDeclaraciones() throws ASemanticoException {
    	 for(Ctor c : this.constructores)
    		 c.ControlDeclaraciones();
    	 for (Unidad u : this.metodos.values())
    		 u.ControlDeclaraciones();
     }
    
    

    public boolean Main() {
        return this.metodos.containsKey("main");
    }


    public String getPadre() {
        return padre;
    }

    public void setPadre(String padre) {
        this.padre = padre;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }
    
       
    
    public void insertarVariableInstancia(VariableInstancia v) throws ASemanticoException{
        if(this.variablesInstancia.containsKey(v.getNombre()))
            throw new ASemanticoException("ERROR SEMANTICO : variable repetida"
                    + v.getToken().getError() );
        this.variablesInstancia.put(v.getNombre(), v);
    }
        
        
    public String getName() {
        return this.token.getLexema();
    }
    
    public int getLine(){
        return this.token.getLine();
    }
    
    public int getRow(){
        return this.token.getRow();
    }
    
    public void insertarMetodo(Metodo m) throws ASemanticoException{
        
        if(this.metodos.containsKey(m.getNombre()))
            throw new ASemanticoException("Error Semantico: metodo repetido"
                    + " , "+m.getToken().getError() );
        //Inserto el metodo en el mapeo 
        this.metodos.put(m.getNombre(), m);
        
    
    }
    
    public void imprimir(){
        
        System.out.println("-------------------------");
        System.out.print("Clase : ");
        System.out.println(this.getName());
        System.out.print("Padre : ");
        System.out.println(this.getPadre());
        System.out.println("Variables : ");
        for(VariableInstancia v : this.variablesInstancia.values()){
            System.out.println("-------------------------");
            System.out.println("    nombre : "+v.getNombre());
            System.out.println("    visibilidad : "+v.getVisibilidad());
            System.out.println("    Tip : "+v.getTipo().getTipo());
            System.out.println("-------------------------");
        }
        
        System.out.println("Constructores : ");
        for(Ctor c : this.constructores){
            System.out.println("nombre : "+c.getNombre());
            System.out.println("Parametros : ");
            for(Parametro p : c.getParametros()){
                System.out.println("    nombre : "+p.getNombre());
                System.out.println("    ubicacion : "+p.getUbicacion());
                System.out.println("    tipo : "+p.getTipo().getTipo());
            
            }
        }
        
        
        System.out.println("Metodos : ");
        
        for(Metodo m : this.metodos.values()){
            System.out.println("-------------------------");
            System.out.println("    nombre : "+m.getNombre());
            //System.out.println("    visibilidad : "+m.getVisibilidad());
            System.out.println("    Tipo : "+m.getRetorno().getTipo());
            System.out.println("    Forma Metodo : "+m.getFormaMetodo());
            System.out.println("-------------------------");
             System.out.println("Parametros : ");
             
            for(Parametro p : m.getParametros()){
                System.out.println("-------------------------");
                System.out.println("    nombre : "+p.getNombre());
                System.out.println("    ubicacion : "+p.getUbicacion());
                System.out.println("    tipo : "+p.getTipo().getTipo());
                System.out.println("-------------------------");

            
            }
            System.out.println("-------------------------");

        
        }
        System.out.println("Fin de la clase");
        System.out.println("-------------------------");

        

        
    }
    
    public void chequearTipos() throws ASemanticoException{
        
        //Chequeo los tipos de las variables
        for(VariableInstancia v : this.variablesInstancia.values()){
            if(!v.getTipo().esTipoValido())
                throw new ASemanticoException("Error semantico: "
                        + "La variable de instancia no es de tipo valido,"+
                        v.getTipo().getToken().getError());
        }
        
        //Chequeo los tipos de los metodos
        for(Metodo m : this.metodos.values())
            m.chequeoTipos();
        //Chequeo los tipos de los constructores
        for(Ctor c : this.constructores)
            c.chequeoTipos();
        
        
    }
    
    public void consolidar() throws ASemanticoException{
        //Si no tengo constructor agrego uno por defecto
        
        if(this.constructores.size()==0){
            Ctor nuevo = new Ctor();
            nuevo.setPredefinido(true);
            nuevo.setClase(this);
            //Le inserto el token de la clase
            nuevo.setId(this.getToken());
            this.constructores.add(nuevo);
        }
        //Chequeo si mi padre está consolidado
        //SE CONSIDERA POR DEFECTO QUE LA CLASE OBJECT YA ESTA CONSOLIDADA....CHEQUEAR
        if(this.padre!=ClavesServices.TokenTypes.OBJECT.toString() && this.padre!= null && this.consolidada!= true &&
        		!this.padre.equals("Object")){
        System.out.println("Estoy en : "+this.getName()+"y voy a controlar que mi padre este declarado :"+this.getPadre());
        Clase clasePadre = ts.getClases().get(this.padre);
        if(clasePadre==null)
            throw new ASemanticoException("ERRORSEMANTICO : La clase padre no esta declarada"+this.token.getError());
        if(clasePadre.isConsolidada()){
            //inserto todas las variables que tengo de mi padre
            for(VariableInstancia var : clasePadre.variablesInstancia.values()){
                //Puede ser que tenga que crear otro metodo, chequear esto...
                this.insertarVariableAncestro(var);
            }
            //System.out.println("Procedo a insertar los metodos de la clase padre que heredo ...");
            for(Metodo met : clasePadre.metodos.values()){
                this.insertarRevisado(met);

            }
            
        

            this.consolidada = true;


        }
        else{
        	clasePadre.consolidar();
            this.consolidar();
        }
        }
        else 
            this.consolidada = true;
   
    }
    
    
    private void insertarVariableAncestro(VariableInstancia v) throws ASemanticoException{
        if(this.variablesInstancia.containsKey(v.getNombre()))
            throw new ASemanticoException("Error Semantico : atributo con el mismo"
                    + " nombre que ancestro"+this.variablesInstancia.get(v.getNombre()).getToken().getError());
        this.variablesInstancia.put(v.getNombre(), v);
    }
    
    public void insertarRevisado(Metodo met)throws ASemanticoException{
       
      
        if(this.metodos.containsKey(met.getNombre())){
            //Si se da este caso el metodo esta redefinido deben coincidir los parametros y el tipo de retorno
            Metodo aux = this.metodos.get(met.getNombre());
            //Reporto error si el metodo es final
            if(met.isEsfinal())
                throw new ASemanticoException("Error semantico : la clase redefine un metodo final : "
                    + ""+aux.getId().getError());
            //Reporto error si las  formas metodos  no coinciden
            if(!aux.getFormaMetodo().equals(met.getFormaMetodo()))
                throw new ASemanticoException("Error Semantico : Las formas del metodo redefinido no coincide en linea : "+met.getId().getLine()+met.getId().getError());
            //Reporto error si no tiene la misma cantidad de parametros
            if(aux.getParametros().size()!=met.getParametros().size())
                throw new ASemanticoException("Error Semantico : la cantidad de parametros en el metodo sobreescrito no coincide con el original metodo :"
                        + " "+aux.getNombre()+" en linea : "+aux.getId().getLine()+aux.getId().getError());
            //Ahora tengo que revisar que los tipos y la ubicación de los parametros sean iguales...
            if(!aux.igualParametros(met.getParametros()))
                throw new ASemanticoException("Error Semantico : los tipos de parametros y su ubicacion en el metodo sobreescrito no coincide con el original metodo :"
                        + " "+aux.getNombre()+" en linea : "+aux.getId().getLine()+aux.getId().getError());
            
        }
        //Si paso todos los ifs... agrego el metodo    
        this.metodos.put(met.getNombre(),met);
    }
    
   

}
