package semantico2;

import java.util.ArrayList;
import java.util.HashMap;

import semantico.TDS;
import semantico.Unidad;
import semantico.VarLocal;

public class NodoBloque extends NodoSentencia {
	   
    private ArrayList<NodoSentencia> sentencias;
    private NodoBloque padre;
    private Unidad unidadActual;
    private HashMap<String,VarLocal> variablesLocales;
    private final TDS ts;
    
    
    public NodoBloque(){
        
        this.variablesLocales = new HashMap<String,VarLocal>();
        sentencias = new ArrayList<NodoSentencia>();
        this.ts = TDS.getInstance();
        

    
    }
    

    
    public Unidad getUnidadActual() {
        return unidadActual;
    }
    
    public void printVariablesBloque(){
        for(VarLocal aux : this.variablesLocales.values()){
            System.out.println(aux.getNombre());
        }
    
    }

    public void setUnidadActual(Unidad unidadActual) {
        this.unidadActual = unidadActual;
    }
   


    public ArrayList<NodoSentencia> getSentencias() {
        return sentencias;
    }

    public void setSentencias(ArrayList<NodoSentencia> sentencias) {
        this.sentencias = sentencias;
    }

    public NodoBloque getPadre() {
        return padre;
    }

    public void setPadre(NodoBloque padre) {
        this.padre = padre;
    }

    public HashMap<String,VarLocal> getVariablesLocales() {
        
        return variablesLocales;
    }

    public void setVariablesLocales(HashMap<String,VarLocal> variablesLocales) {
        this.variablesLocales = variablesLocales;
    }
    
    public void addSentencia(NodoSentencia e){
        this.sentencias.add(e);
    }
    
    //Los chequeos correspondientes ya estan hechos en la declaraciones de variables
    public void insertarVariableLocal(VarLocal v){
        this.variablesLocales.put(v.getNombre(), v);

    
    }
   
 
    //Creo que faltan chequear las variables locales ...
    @Override
    public void check() throws ASTException {
        for(NodoSentencia s : this.sentencias
                ){
            if(s.isBloque())
                ts.setBloqueActual((NodoBloque)s);
            s.check();
        }
        
        
      
    }
    
}
