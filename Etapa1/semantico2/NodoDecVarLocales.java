package semantico2;

import java.util.ArrayList;
import java.util.HashMap;

import semantico.Tipo;
import semantico.VarLocal;

/**
*
* @author Javier Amorosi
*/
public class NodoDecVarLocales extends NodoSentencia {
   //Por ahora uso la clase variable...
   private ArrayList<VarLocal> variablesLocales;
   private final NodoBloque bloque;
   private Tipo _tipo;
   
   public NodoDecVarLocales(NodoBloque bloque){
       this.bloque = bloque;
       this.variablesLocales = new ArrayList<VarLocal>();
      
   }

   public ArrayList<VarLocal> getVariablesLocales() {
       return variablesLocales;
   }

   public void setVariablesLocales(ArrayList<VarLocal> variablesLocales) {
       this.variablesLocales = variablesLocales;
   }
   
   public void insertVariableLocal(VarLocal v) {
	   this.variablesLocales.add(v);
   }
   
   private void recorrerVariables () throws ASTException{
       HashMap<String,VarLocal> aux = new HashMap<String,VarLocal>();
       for(VarLocal v : this.variablesLocales){
           if(aux.containsKey(v.getNombre()))
               throw new ASTException("ERROR SEMANTICO : la variable : "+v.getNombre()+" esta repetida, linea : "+
                   v.getToken().getLine()+" columna : "+v.getToken().getRow());
           //Si no está la inserto en el mapeo
           aux.put(v.getNombre(), v);
       }
       
   }
   
   
   private void recorrerParametros () throws ASTException{
       

       for(VarLocal v : this.variablesLocales){
    	   System.out.println("La variable local v es :"+v.getNombre());
           if(this.bloque.getUnidadActual().getParamMap().containsKey(v.getNombre()))
           throw new ASTException("Error semantico : la variable con nombre : "+v.getNombre()+" esta repetida en el parametro "+
                   v.getToken().getError());
       
       }
   
   }
   
   private void recorrerVariablesBloques() throws ASTException{
       
       
       this.bloque.printVariablesBloque();
       
       for(VarLocal v : this.variablesLocales){
           //System.out.println("Estoy tirando exception aqui!!!!");
           if(this.bloque.getVariablesLocales().containsKey(v.getNombre()))
                 throw new ASTException("Error Semantico : la variable local :"
               +v.getToken().getLexema()
                +"fue declarada anteriormente en el bloque o en un bloque anterrios"+v.getToken().getError());
       
       }
       
   
   }
   
   
  
   

   @Override
   public void check() throws ASTException {
       
       //ya no tengo que hacerlo porque chequeo mientras inserto
       //this.recorrerVariables();
       this.recorrerParametros();
       //this.recorrerVariablesBloques();
      
       //Aparenente esto no es necesario...
       for(VarLocal v : this.variablesLocales){
                if(!
                        
                        
                        v.getTipo().esTipoValido())
                       throw new ASTException("Error Semantico : la variable local :"
                       +v.getToken().getLexema()+" DE TIPO : "+v.getTipo()+" no es tipo valido."
                      +v.getToken().getError());
  
                   //Después de estos chequeos se supone que tengo que insertar las variable?¡?¡
                   //Las inserto en las gramatica. 
                   //this.bloque.insertarVariableLocal(v);
      

       }
       
   }

public Tipo get_tipo() {
	return _tipo;
}

public void set_tipo(Tipo _tipo) {
	this._tipo = _tipo;
}
   
}