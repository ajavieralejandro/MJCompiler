package semantico2;

import Claves.ClavesServices;
import semantico.Clase;
import semantico.TDS;
import semantico.Tipo;
import semantico.TipoBase;
import semantico.Unidad;
import semantico2expresiones.Encadenado;
import semantico2expresiones.NodoExpresion;
import semantico2expresiones.NodoPrimario;
import semantico2expresiones.NodoVar;
import token.Token;

public class NodoSentenciaAsignacion extends NodoSentencia {
	 private NodoExpresion lIzq;
	   private NodoExpresion Lder;
	   private final TDS ts;
	   private final NodoBloque actual;
           private Token operador;

    public Token getOperador() {
        return operador;
    }

    public void setOperador(Token operador) {
        this.operador = operador;
    }
	   public NodoExpresion getlIzq() {
	        return lIzq;
	    }

	    public void setlIzq(NodoExpresion lIzq){
	        this.lIzq = lIzq;
	    }

	    public NodoExpresion getLder() {
	        return Lder;
	    }

	    public void setLder(NodoExpresion Lder) {
	        this.Lder = Lder;
	    }
	    
	   
	    
	    public NodoSentenciaAsignacion(NodoBloque actual){
	        this.ts = TDS.getInstance();
	        this.actual = actual;
	       
	    
	    }
	    
	    
	    
	    
	    

	    @Override
	    public void check() throws ASTException {
                
                
             
	        //id1 es el nombre una variable local, o sino parametro del metodo actual, o sino de una variable
	        //de instancia visible de la clase actual
	        String var1 = this.lIzq.getToken().getLexema();
	        //Lo primero chequeo que el accseo izquierdo sea de un tipo valido
                TipoBase _tipoIzq = this.lIzq.check();
                if(!_tipoIzq.esTipoValido())
                    throw new ASTException("Error semantico : el tipo :"+_tipoIzq.getTipo()
                            +" de la asignación, no es un tipo valido."+_tipoIzq.getToken().getError());
                
                //Ahora chequeo que pasa si no tiene encadenado 
                if(this.getOperador().getType()==ClavesServices.TokenTypes.IGUALMAS.ordinal()
                        || this.getOperador().getType()==ClavesServices.TokenTypes.IGUALMENOS.ordinal()){
                    
                    //Puedo llegar a tener un problema acá así que es conveniento testear
                    System.out.println("Estoy recibiendo el tipo : "+this.lIzq.check().toString());
                    
                    //Tenemos que el operador += y -= requieren tipo int de ambos lados 
                    if(!this.lIzq.check().getTipo().equals(ClavesServices.TokenTypes.INT.toString()))
                        throw new ASTException("Error semantico : los operadores de asignacion += y -= requieren "
                                + "que el lado izquierdo sea de tipo int"
                                + " y se encontro : "+this.lIzq.check().getTipo()
                                + this.lIzq.getToken().getError()
                        );
                    if(!this.Lder.check().getTipo().equals(ClavesServices.TokenTypes.INT.toString()))
                        throw new ASTException("Error semantico : los operadores de asignacion += y -= requieren "
                                + "que el lado izquierdo sea de tipo int"
                                + " y se encontro : "+this.Lder.check().getTipo()
                                + this.Lder.getToken().getError()
                        );
                
                }
                if(!this.lIzq.chain()){
                    
                    if(!actual.getVariablesLocales().containsKey(var1)){
	            Unidad uniActual = this.ts.getUnidadActual();
	            //no esta en los parametros locales
	            if(!uniActual.getParamMap().containsKey(var1)){
	                //Y tampoco esta en las variables de instancia...
	                if(!ts.getClaseActual().getVariablesInstancia().containsKey(var1))
	                    throw new ASTException("Error Semantico : La variable: "+var1+" a la que hace referencia la asignacion"
	                            + " no esta declara como variable local, ni en los parametros, ni en la clase actual. "
	                            + this.lIzq.getToken().getError());
	            }
	   
	        }
                
                }
                else{
                    //Ahora chequeo que pasa si tengo encadenado
                    NodoPrimario _aux = (NodoPrimario) this.lIzq;
                    if(!_aux.getCadena().isId())
                        throw new ASTException("Error semantico : el acceso en la expresion"
                                + " no es de tipo variable."+_aux.getCadena().getId().getError());
                
                }
       
                //Ahora necesito chequear que los tipos sean compatibles
	        TipoBase _tipoDer = this.Lder.check();
                   if(!_tipoDer.esTipoValido())
                    throw new ASTException("Error semantico : el tipo :"+_tipoDer.getTipo()
                            +" de la asignación, no es un tipo valido."+_tipoDer.getToken().getError());
              
	        if(!_tipoIzq.esCompatible(_tipoDer))
	            throw new ASTException("Error Semantico : el tipo :  "+_tipoDer.getTipo()+" no es compatible con  el tipo"
		                    + " : "+_tipoIzq.getTipo()+" de la expresion a la izquierda."+_tipoIzq.getToken().getError());
	        
	                
	        
	    }
	    
	    private void chequeoEncadenado(Clase claseIzq, Encadenado derecho)throws ASTException{
	        
	          if(!claseIzq.getVariablesInstancia().containsKey(derecho.getId().getLexema()))
	                throw new ASTException("ERROR AST : "+derecho.getId().getLexema()+" no es una variable de instancia "
	                        + "de la clase : "+claseIzq.getName()+" en linea : "+derecho.getId().getLine()+ " columna : "+derecho.getId().getRow());
	          //TENGO QUE CHEQUEAR QUE LA VARIABLE PUEDA SER REFERENCIADA EN EL CONTEXTO ACTUAL
	          
	          if(derecho.getCadena()!=null){
	              Tipo aux = claseIzq.getVariablesInstancia().get(derecho.getId().getLexema()).getTipo();
	              if(!aux.esTipoClase())
	                       throw new ASTException("ERROR AST: "+aux.getTipo()+" no es de tipo clase, por ende no puede tener encadenado"
	                               + " en linea : "+derecho.getId().getLine()+" columna : "+derecho.getId().getRow());
	              Clase claseAux = ts.getClases().get(aux.getTipo());
	              Encadenado cadenaAux = derecho.getCadena();
	              this.chequeoEncadenado(claseAux,cadenaAux);
	                
	          
	          }
	                        
	          
	         
	    
	    }
	    
}
