package com.example.scaleserpentiproject.logica.oggetti;

public class Giocatore {
    private final String nome;
    private int posizione;
    private int turniStop;
    private int ultimoLancio;
    private boolean cartaDivietoSosta; //se true il giocatore ha la carta divieto di sosta

    public Giocatore(String nome) {
        this.nome = nome;
        this.posizione=0;
        this.turniStop=0;
        cartaDivietoSosta=false;
    }

    public String getNome() {
        return nome;
    }

    public int getPosizione() {
        return posizione;
    }

    public void setPosizione(int posizione) {
        this.posizione = posizione;
    }

    public int getTurniStop() {
        return turniStop;
    }

    public void setTurniStop(int turniStop) {
        this.turniStop = turniStop;
    }

    public int getUltimoLancio() {
        return ultimoLancio;
    }

    public void setUltimoLancio(int ultimoLancio) {
        this.ultimoLancio = ultimoLancio;
    }

    public boolean haCartaDivietoSosta() {
        return cartaDivietoSosta;
    }

    public void setCartaDivietoSosta(boolean cartaDivietoSosta) {
        this.cartaDivietoSosta = cartaDivietoSosta;
    }

    public void muovi(int num){
        this.posizione+=num;
    }

}
