package com.example.scaleserpentiproject.logica.oggetti;


import com.example.scaleserpentiproject.logica.caselle.Casella;
import com.example.scaleserpentiproject.logica.caselle.CasellaPremio;
import com.example.scaleserpentiproject.logica.caselle.CasellaSosta;
import com.example.scaleserpentiproject.logica.caselle.Tipo;


@SuppressWarnings("EnhancedSwitchMigration")
public class ControllaCasellaCommand implements Command{
    private final Giocatore g;
    private final Casella casella;
    private final MazzoCarte mazzo;
    private final Partita partita;
    private Tipo carta;

    public ControllaCasellaCommand(Giocatore g, Casella c, MazzoCarte m, Partita partita){
        this.g=g;
        this.casella=c;
        this.mazzo=m;
        this.partita=partita;
    }

    @Override
    public void execute() {
        if(casella!=null){
            if(casella.containsTestaSerpente()){
                System.out.println("Il giocatore "+g.getNome()+ " è sulla testa del serpente");
                g.setPosizione(casella.getSerpente().getCoda());
                System.out.println("Il giocatore "+g.getNome()+ " scende alla posizione "+g.getPosizione());
                if(g.getUltimoLancio()==12)
                    partita.avanza(g);
                return;
            }
            if(casella.containsPiediScala()){
                System.out.println("Il giocatore "+g.getNome()+ " è sui piedi di una scala");
                g.setPosizione(casella.getScala().getCima());
                System.out.println("Il giocatore "+g.getNome()+ " sale alla posizione "+g.getPosizione());
                if(g.getUltimoLancio()==12)
                    partita.avanza(g);
                return;
            }
            switch (casella.getTipo()) {
                case PESCACARTA:
                    carta=this.mazzo.pescaCarta(g);
                    System.out.println("Il giocatore "+g.getNome()+" ha pescato una carta: " + carta);
                    switch (carta) {
                        case PANCHINA:
                            if(!g.haCartaDivietoSosta()) {
                                System.out.println("Il giocatore "+g.getNome()+" è sulla casella premio di tipo panchina posizionata sulla casella "+g.getPosizione());
                                g.setTurniStop(g.getTurniStop() + 1);
                                System.out.println("Il giocatore "+g.getNome()+" è fermo per un altro turno tot: "+g.getTurniStop());
                                mazzo.riponiNelMazzo(g,carta);
                            }
                            else {
                                System.out.println("Il giocatore "+g.getNome()+" dispone della carta divieto sosta la utilizza e la ripone nel mazzo");
                                mazzo.riponiNelMazzo(g,carta);
                            }
                            break;
                        case LOCANDA:
                            if(!g.haCartaDivietoSosta()){
                                System.out.println("Il giocatore "+g.getNome()+" è sulla casella sosta di tipo locanda posizionata sulla casella "+g.getPosizione());
                                g.setTurniStop(g.getTurniStop()+3);
                                System.out.println("Il giocatore "+g.getNome()+" è fermo per altri 3 turni tot: "+g.getTurniStop());
                                mazzo.riponiNelMazzo(g,carta);
                            }
                            else{
                                System.out.println("Il giocatore "+g.getNome()+" dispone della carta divieto sosta la utilizza e la ripone nel mazzo");
                                mazzo.riponiNelMazzo(g,carta);
                            }
                            break;
                        case DADI:
                            System.out.println("Il giocatore "+g.getNome()+" è sulla casella premio di tipo dadi posizionata sulla casella "+g.getPosizione());
                            mazzo.riponiNelMazzo(g,carta);
                            partita.avanza(g);
                            break;
                        case MOLLA:
                            System.out.println("Il giocatore "+g.getNome()+" è sulla casella premio di tipo molla posizionata sulla casella "+g.getPosizione());
                            mazzo.riponiNelMazzo(g,carta);
                            partita.avanzaDelLancioPrecedente(g,g.getUltimoLancio());
                            break;
                        case DIVIETOSOSTA:
                            System.out.println("Il giocatore "+g.getNome()+" ha pescato la carta divieto sosta e la conserva");
                            break;
                    }
                    break;
                case PREMIO:
                    CasellaPremio cp=(CasellaPremio) casella;
                    if(cp.getTipoCasella()==Tipo.DADI){
                        System.out.println("Il giocatore "+g.getNome()+" è sulla casella premio di tipo dadi posizionata sulla casella "+g.getPosizione());
                        partita.avanza(g);
                    }

                    if(cp.getTipoCasella()==Tipo.MOLLA){
                        System.out.println("Il giocatore "+g.getNome()+" è sulla casella premio di tipo molla posizionata sulla casella "+g.getPosizione());
                        partita.avanzaDelLancioPrecedente(g,g.getUltimoLancio());
                    }
                    break;

                case SOSTA:
                    CasellaSosta cs=(CasellaSosta) casella;
                    if(cs.getTipoCasella()==Tipo.LOCANDA){
                        if(!g.haCartaDivietoSosta()){
                            System.out.println("Il giocatore "+g.getNome()+" è sulla casella sosta di tipo locanda");
                            g.setTurniStop(g.getTurniStop()+3);
                            System.out.println("Il giocatore "+g.getNome()+" è fermo per altri 3 turni tot: "+g.getTurniStop());
                        }else{
                            System.out.println("Il giocatore "+g.getNome()+" utilizza la carta divieto di sosta per non rimanere bloccato e la riposiziona nel mazzo");
                            mazzo.riponiNelMazzo(g,Tipo.DIVIETOSOSTA);
                        }
                    }

                    if(cs.getTipoCasella()==Tipo.PANCHINA){
                        if(!g.haCartaDivietoSosta()){
                            System.out.println("Il giocatore "+g.getNome()+" è sulla casella sosta di tipo panchina");
                            g.setTurniStop(g.getTurniStop()+1);
                            System.out.println("Il giocatore "+g.getNome()+" è fermo per un altro turno tot: "+g.getTurniStop());
                        }else {
                            System.out.println("Il giocatore "+g.getNome()+" utilizza la carta divieto di sosta per non rimanere bloccato e la riposiziona nel mazzo");
                            mazzo.riponiNelMazzo(g,Tipo.DIVIETOSOSTA);
                        }
                    }
                    break;
            }
        }
    }

    public Tipo getCarta() {
        return this.carta;
    }
}
