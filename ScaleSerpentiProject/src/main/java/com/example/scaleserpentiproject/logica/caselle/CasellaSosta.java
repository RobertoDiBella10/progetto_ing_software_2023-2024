package com.example.scaleserpentiproject.logica.caselle;

public class CasellaSosta extends Casella{
    private final Tipo tipoCasella;

    public CasellaSosta(int id, Tipo tipoCasella) {
        super(id);
        super.setTipo(TipoCasella.SOSTA);
        if(tipoCasella==Tipo.DADI || tipoCasella==Tipo.MOLLA || tipoCasella==Tipo.DIVIETOSOSTA)//controlla se è locanda o panchina
            throw new IllegalArgumentException("Inserire alla casella sosta solo il tipo locanda o panchina");
        this.tipoCasella = tipoCasella;
    }

    public Tipo getTipoCasella() {
        return tipoCasella;
    }
}
