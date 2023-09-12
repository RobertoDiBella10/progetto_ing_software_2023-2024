package com.example.scaleserpentiproject.logica.caselle;

public class CasellaPremio extends Casella{
    private final Tipo tipoCasella;

    public CasellaPremio(int id, Tipo tipoCasella) {
        super(id);
        super.setTipo(TipoCasella.PREMIO);
        if(tipoCasella==Tipo.LOCANDA || tipoCasella==Tipo.PANCHINA || tipoCasella==Tipo.DIVIETOSOSTA)//controlla se è dadi o molla
            throw new IllegalArgumentException("Inserire alla casella premio solo il tipo dadi o molla");
        this.tipoCasella = tipoCasella;
    }

    public Tipo getTipoCasella() {
        return tipoCasella;
    }
}
