package semantico;
import Claves.ClavesServices;
import semantico2.ASTException;
import semantico2.NodoBloque;

import java.util.Hashtable;
import java.util.Map;
import token.Token;

public class TDS {
	private  Map<String, Clase> clases = null;
    private Clase claseActual;
    private Unidad unidadActual;
    private static TDS INSTANCE = null;
    private NodoBloque bloqueActual;



    public Clase getClaseActual() {
        return claseActual;
    }

    public void setClaseActual(Clase claseActual) {
        this.claseActual = claseActual;
    }
    
    public static TDS getInstance() {
        if (INSTANCE == null) createInstance();
        return INSTANCE;
    }
    
    private  static void createInstance() {
        if (INSTANCE == null) { 
            INSTANCE = new TDS();
        }
    }
    
    public void setBloqueActual(NodoBloque bloqueActual) {
        this.bloqueActual = bloqueActual;
    }
   
    
    
    private TDS(){
        this.clases = new Hashtable<>();
        try{
                //Inserto las clases que ya vienen creadas por defecto
                this.insertarObject();
                this.insertarSystem();
                //imprimo las clases que tengo
                //this.imprimir();
        }
        catch(ASemanticoException e){
            System.out.println(e.getMessage());
        }
     
        
    }
    
    //metodo encargado de insertar Object en la tabla de Símbolos
    private void insertarObject() throws ASemanticoException{
        
        Token aux = new Token(0,ClavesServices.TokenTypes.OBJECT.toString());
        Clase c = new Clase(aux,this);
        c.setConsolidada(true);
        //Es la única clase sin padre
        c.setPadre(null);
        
        //tengo que definirle un constructor 
        Ctor ctor = new Ctor();
        aux = new Token(0,ClavesServices.TokenTypes.OBJECT.toString());
        ctor.setId(aux);
        ctor.setPredefinido(true);
        ctor.setClase(c);
        c.insertarConstructor(ctor);
        
        this.insertarClase(c);
        
        
    }
    //metodo encargado de insertar System en la tabla de Símbolos
    private void insertarSystem() throws ASemanticoException{
        Token auxm;
        TipoBase toIns;
        Token aux = new Token(0,"System");
        Clase c = new Clase(aux,this);
        c.setConsolidada(true);
        c.setPadre(ClavesServices.TokenTypes.OBJECT.toString());
        //Agrego el constructor
        Ctor ctor = new Ctor();
        ctor.setId(aux);
        ctor.setPredefinido(true);
        ctor.setClase(c);
        c.insertarConstructor(ctor);
        
        
        this.insertarClase(c);
        
        //Inserto el metodo static int read
        //System.out.println("Voy a insertar el metodo : read ");
        Metodo m = new Metodo();
        m.setClase(c);
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        auxm = new Token(0,"read");
        m.setId(auxm);
        toIns = new TipoInt();
        m.setRetorno(toIns);
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        c.insertarMetodo(m);
        
        
        //Inserto el metodo static void printB(boolean b)
        //System.out.println("Voy a insertar el metodo : printB ");
        m = new Metodo();
        m.setClase(c);
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        auxm = new Token(0,"printB");
        m.setId(auxm);
        toIns = new TipoVoid();
        m.setRetorno(toIns);
        auxm = new Token(0,"b");
        m.insertarArgumento(new Parametro(new TipoBoolean(),auxm.getLexema(),aux));
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        //System.out.println("El metodo se llama : "+m.getNombre());
        c.insertarMetodo(m);
        
        //Inserto el metodo static void printC(char c)
        auxm = new Token(ClavesServices.TokenTypes.idMetVar.ordinal(),"printC");
        m = new Metodo();
        m.setId(auxm);
        m.setRetorno(new TipoVoid());
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        Parametro p1 = new Parametro(new TipoChar(),"c",new Token(ClavesServices.TokenTypes.CHAR.ordinal(),"c"));
        m.insertarArgumento(p1);
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        c.insertarMetodo(m);
        
        //inserto el metodo static void printI(int i)
        auxm = new Token(ClavesServices.TokenTypes.idMetVar.ordinal(),"printI");
        m = new Metodo();
        m.setId(auxm);
        m.setRetorno(new TipoVoid());
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        p1 = new Parametro(new TipoInt(),"i",new Token(ClavesServices.TokenTypes.INT.ordinal(),"i"));
        m.insertarArgumento(p1);
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        c.insertarMetodo(m);
        
        //inserto el metodo • static void printS(String s)
        auxm = new Token(ClavesServices.TokenTypes.idMetVar.ordinal(),"printS");
        m = new Metodo();
        m.setId(auxm);
        m.setRetorno(new TipoVoid());
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        p1 = new Parametro(new TipoString(),"s",new Token(ClavesServices.TokenTypes.STRING.ordinal(),"s"));
        m.insertarArgumento(p1);
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        c.insertarMetodo(m);
        
        //inserto el metodo • static void println()
        auxm = new Token(ClavesServices.TokenTypes.idMetVar.ordinal(),"println");
        m = new Metodo();
        m.setId(auxm);
        m.setRetorno(new TipoVoid());
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        c.insertarMetodo(m);
        
        //inserto el metodo • static void printBln(boolean b)
        auxm = new Token(ClavesServices.TokenTypes.idMetVar.ordinal(),"printBln");
        m = new Metodo();
        m.setId(auxm);
        m.setRetorno(new TipoVoid());
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        p1 = new Parametro(new TipoBoolean(),"b",new Token(ClavesServices.TokenTypes.BOOLEAN.ordinal(),"b"));
        m.insertarArgumento(p1);
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        c.insertarMetodo(m);
        
        //inserto el metodo • static void printCln(char c)
        auxm = new Token(ClavesServices.TokenTypes.idMetVar.ordinal(),"printCln");
        m = new Metodo();
        m.setId(auxm);
        m.setRetorno(new TipoVoid());
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        p1 = new Parametro(new TipoChar(),"c",new Token(ClavesServices.TokenTypes.CHAR.ordinal(),"c"));
        m.insertarArgumento(p1);
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        c.insertarMetodo(m);
        
        // INSERTO el metodo • static void printIln(int i):
        auxm = new Token(ClavesServices.TokenTypes.idMetVar.ordinal(),"printIln");
        m = new Metodo();
        m.setId(auxm);
        m.setRetorno(new TipoVoid());
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        p1 = new Parametro(new TipoInt(),"i",new Token(ClavesServices.TokenTypes.INT.ordinal(),"i"));
        m.insertarArgumento(p1);
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        c.insertarMetodo(m);
        
        //inserto el metodo static void printSln(String s)
        auxm = new Token(ClavesServices.TokenTypes.idMetVar.ordinal(),"printSln");
        m = new Metodo();
        m.setId(auxm);
        m.setRetorno(new TipoVoid());
        m.setFormaMetodo(ClavesServices.TokenTypes.STATIC.toString());
        p1 = new Parametro(new TipoString(),"s",new Token(ClavesServices.TokenTypes.STRING.ordinal(),"s"));
        m.insertarArgumento(p1);
        //m.setVisibilidad(ClavesServices.TokenTypes.PUBLIC.toString());
        c.insertarMetodo(m);
        
    }
    
    //No se encarga de chequear herencia circular. 
    public void insertarClase(Clase c) throws ASemanticoException{
        //Si la clase ya fue previamente declarada arrojo excepcion
        //System.out.println("Estoy en insertar clase"+c.getName());
        if(this.clases.containsKey(c.getName()))
            throw new ASemanticoException("ERROR Semantico : Clase "
                    + "ya declarada previamente, linea : "+c.getLine()+c.getToken().getError());
        //Sino la inserto
        this.clases.put(c.getName(), c); 
    }
    
        public Unidad getUnidadActual() {
        return unidadActual;
    }

    public void setUnidadActual(Unidad unidadActual) {
        this.unidadActual = unidadActual;
    }
   

    public Map<String, Clase> getClases() {
        return clases;
    }
    
    
   private void chequearMain() throws ASemanticoException{
        boolean main=false;
        for(Clase c: clases.values()){
            if(c.Main()){
                main=true;
                break;
            }
        }
        if(!main)
            throw new ASemanticoException("Error semantico: no se encontro metodo main. \n[Error:main|0]");      

    
    }

    
    public void imprimir(){
        for(Clase c : this.clases.values())
            c.imprimir();
    }
    
    //Chequeo de Herencia Circular de todas las Clases 
    private void herenciaCircular() throws ASemanticoException{
        for(Clase c : this.clases.values()){
            
            String padre = c.getPadre();
            String clase = c.getName();
          

      
            while(!clase.equals(ClavesServices.TokenTypes.OBJECT.toString()) 
            		&&!padre.equals(ClavesServices.TokenTypes.OBJECT.toString())
            		&&!padre.equals("Object")
            		&&!padre.equals("System")

                    && !padre.equals(clase) 
                    
                    )
                    {
                        //obtengo el padre del padre
                        //Antes de la herencia circular tengo que chequear que todas
                        //las clases esten declaradas
            			//System.out.println("Mi padre es : "+padre);
            	        Clase aux = clases.get(padre);
            	        if(aux!=null)
            	        	padre = aux.getPadre();
            	        else
            	        	padre = "Object";
            			//System.out.println("Ahora  es : "+padre);

            	       
                        
                        
                        

                    }
        
            
            if(padre!=null && padre.equals(clase))
                throw new ASemanticoException("Error Semantico : herencia Circular: en clase : "+clase
                +c.getToken().getError());
 
        }
    }
    
    private boolean existeClase(String clase) {
    	return this.clases.containsKey(clase);
    }
    
    public void chequeo() throws ASemanticoException,ASTException{
        //chequeo que no tenga mains
        this.chequearMain();
        for(Clase c : this.clases.values())
        	 if(!this.existeClase(c.getName()))
             	throw new ASemanticoException("Error Semantico : clase no declarada : "+c.getName()+c.getToken().getError());
        	
        //Antes chequeo que no haya herencia circular
        this.herenciaCircular();
    
        for(Clase c : this.clases.values()){
            if(!c.isConsolidada())
                c.consolidar();
            c.chequearTipos();
            c.controlDeclaraciones();
            c.controlSentencias();
        }
    }
    
    public boolean sonCompatibles(TipoBase c1, TipoBase c2){
    	
        String tipoC1 = c1.getTipo();
        String tipoC2 = c2.getTipo();
        
        //Puede ser que tenga que chequear que las clases también existas antes de realizar esta busqueda...
        	
        //Mientras que no sean iguales o el tipo de c2 no sea object
        while(!tipoC1.equals(tipoC2)
                && !tipoC2.equals("Object"))
        {
            //En algun momento llego a Object o a c1
            tipoC2 = this.clases.get(tipoC2).getPadre();
        
        }
        //Chequear Esta expresion
        return (tipoC1.equals(tipoC2));
    }
    
    //Si la clase c2 desciende de la clase c1... 
    //Testear este metodo
    /**
     * Chequea que la clase c1 descienda de c2
     * @param c1
     * @param c2
     * @return 
     */
    public boolean heredaDe(Clase c1,Clase c2){
        boolean toR = false;
        if(c1.getTipo().getTipo().equals("Object"))
            toR = true;
        if(c1.getTipo().getTipo().equals(c2.getTipo().getTipo()))
            toR = true;
        
        while(!toR && !c2.getTipo().getTipo().equals("Object")){
            c2 = this.getClases().get(c2.getPadre());
            if(c1.getTipo().getTipo().equals(c2.getTipo().getTipo()))
                toR = true;     
        }
        
        return toR;
        
    }
    
    public NodoBloque getBloqueActual() {
    	return this.bloqueActual;
    }
    
    

}



