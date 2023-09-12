package com.example.scaleserpentiproject.logica.caselle;
import com.example.scaleserpentiproject.logica.oggetti.Scala;
import com.example.scaleserpentiproject.logica.oggetti.Serpente;


public class Casella implements Cloneable{
    private final int num;
    private TipoCasella tipo;
    private Serpente serpente;
    private Scala scala;
    public Casella(int num) {
        if(num <= 0) {
            throw new IllegalArgumentException("numero casella non valido(inserire un valore maggiore di 0");
        }
        this.num = num;
        tipo =TipoCasella.NORMALE;
    }

    public int getNum() {
        return num;
    }

    public TipoCasella getTipo() {
        return tipo;
    }

    public void setTipo(TipoCasella tipoCasella) {
        this.tipo = tipoCasella;
    }

    public Serpente getSerpente() {
        return serpente;
    }

    public void setSerpente(Serpente serpente) {
        this.serpente = serpente;
    }

    public Scala getScala() {
        return scala;
    }

    public void setScala(Scala scala) {
        this.scala = scala;
    }

    public boolean containsTestaSerpente(){
        return serpente!=null && serpente.getTesta()==num;
    }

    public boolean containsPiediScala(){
        return scala!=null && scala.getPiedi()==num ;
    }
    @Override
    public String toString() {
        return ".";
    }

    @Override
    public Casella clone() {
        try {
            Casella clone = (Casella) super.clone();
            if (serpente != null) {
                clone.serpente = new Serpente(serpente.getTesta(), serpente.getCoda());
            }
            if (scala != null) {
                clone.scala = new Scala(scala.getPiedi(), scala.getCima());
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}