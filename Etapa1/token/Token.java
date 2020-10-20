/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package token;

/**
 *
 * @author Javier Amorosi
 */
public class Token {
    
    private int type;
    private int row;
    private String lexema;
    private int line;
    
    /*
    Se opto por definir dos constructures
    especificando el número de línea
    y sin especificar.
    */

    public Token(int type, String lexema) {
        this.type = type;
        this.lexema = lexema;
    }

    public Token(int type, int row, String lexema, int line) {
        this.type = type;
        this.row = row;
        this.lexema = lexema;
        this.line = line;
    }
    

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getLine() {
        return line;
    }
    
    public String getError(){
        String toR = " linea : "+this.getLine()+" columna : "+this.getRow()+ "se encontro: "+this.lexema;
        return toR;
    }

    public void setLine(int line) {
        this.line = line;
    }
    /**
     * Redefino el valor de equals para ser implementado por el ASintactico.
     * @param aux
     * @return el valor de equals 
     */
    public boolean equals (Token aux){
        boolean toR = false;
        if(this.getType()==aux.getType() && this.getLexema().equals(aux.getLexema()))
            toR = true;
        return toR;
    }
    
  
  
    
    
}
