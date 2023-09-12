package com.example.scaleserpentiproject.logica.oggetti;

import com.example.scaleserpentiproject.logica.caselle.Casella;
import com.example.scaleserpentiproject.logica.caselle.TipoCasella;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionException;
import com.example.scaleserpentiproject.logica.exceptions.IllegalPositionRowException;
import com.example.scaleserpentiproject.logica.exceptions.OccupedBoxException;
import java.util.HashMap;
import java.util.Map;

//Originator del pattern Memento
@SuppressWarnings("unused")
public class Tabellone {
    private static Tabellone instance = null;
    private int numRighe; //mi servono nella classe Partita per vedere quanto è la dimensione
    private int numCol;   //del tabellone
    private Casella[][] tabellone;
    private Map<Integer,int[]>posizioneCaselle;//la chiave rappresenta il numero della casella mentre il valore ha una coppia indice di riga e di colonna
    private Map<Integer,Serpente> serpenti;
    private Map<Integer,Scala> scale;
    private Map<Integer,Casella> caselleSpeciali;

    private Tabellone() {
        //lo metto per evitare d'istanziare altre classi
    }

    public synchronized static Tabellone getInstance() {
        if (instance == null) {
            instance = new Tabellone();
        }
        return instance;
    }

    public void creaTabellone(int numRighe, int numColonne) {
        if((numRighe<5 || numColonne<5) || (numRighe>10 || numColonne>10))
            throw new IllegalArgumentException("Inserire un tabellone di almeno 5X5 e un massimo di 10X10");
        this.numRighe=numRighe;
        this.numCol=numColonne;
        tabellone = new Casella[numRighe][numColonne];
        posizioneCaselle=new HashMap<>();
        int num = 1;
        for (int row = numRighe - 1; row >= 0; row--) {
            if (row % 2 == 0) {
                for (int col = 0; col < numCol; col++) {
                    tabellone[row][col]=new Casella(num);
                    posizioneCaselle.put(num,new int[]{row,col});
                    num++;
                }
            } else {
                for (int col = numCol - 1; col >= 0; col--) {
                    tabellone[row][col]=new Casella(num);
                    posizioneCaselle.put(num,new int[]{row,col});
                    num++;
                }
            }
        }
        serpenti = new HashMap<>();
        scale = new HashMap<>();
        caselleSpeciali = new HashMap<>();
    }

    public int getNumRighe() {
        return numRighe;
    }

    public int getNumCol() {
        return numCol;
    }

    public int getNumCaselle(){
        return this.numRighe*this.numCol;
    }

    public Casella[][] getCaselle() {
        return tabellone;
    }

    public Map<Integer,Serpente> getSerpenti(){
        return this.serpenti;
    }

    public Map<Integer, Scala> getScale(){
        return this.scale;
    }

    public Map<Integer,int[]> getPosizioneCaselle(){
        return this.posizioneCaselle;
    }

    public Map<Integer, Casella> getCaselleSpeciali() {
        return caselleSpeciali;
    }

    public void addCasellaSpeciale(int numCasella, Casella casellaSpeciale) throws IllegalPositionException, OccupedBoxException {
        int[] pos = trovaPosizioneCasella(numCasella);
        if (pos == null)
            throw new IllegalPositionException("Inserire una posizione valida per la casella");
        if (tabellone[pos[0]][pos[1]].getTipo() != TipoCasella.NORMALE || tabellone[pos[0]][pos[1]].getSerpente() != null
                || tabellone[pos[0]][pos[1]].getScala() != null)
            throw new OccupedBoxException("Impossibile inserire la casella perché è presente un'altra casella speciale, un serpente o una scala");
        tabellone[pos[0]][pos[1]] = casellaSpeciale;
        caselleSpeciali.put(numCasella,casellaSpeciale);
    }

    public void removeElement(int numCasella) throws IllegalPositionException, OccupedBoxException { //metodo che si occupa di eliminare un elemento(serpente, scala, casella speciale)
        int[] pos = trovaPosizioneCasella(numCasella);
        if (pos == null)
            throw new IllegalPositionException("Inserire una posizione valida per la casella");
        if(tabellone[pos[0]][pos[1]].getTipo() != TipoCasella.NORMALE) {
            tabellone[pos[0]][pos[1]].setTipo(TipoCasella.NORMALE);
            caselleSpeciali.remove(numCasella);
        }
        if(tabellone[pos[0]][pos[1]].getSerpente() != null) {//bisogna rimuovere anche la testa del serpente o la cima della scala
            Serpente serpente = tabellone[pos[0]][pos[1]].getSerpente();
            int[] posTesta = trovaPosizioneCasella(serpente.getTesta());
            tabellone[pos[0]][pos[1]].setSerpente(null);
            tabellone[posTesta[0]][posTesta[1]].setSerpente(null);
            serpenti.remove(numCasella);
        }
        if(tabellone[pos[0]][pos[1]].getScala() != null) {
            Scala scala = tabellone[pos[0]][pos[1]].getScala();
            int[] posCima = trovaPosizioneCasella(scala.getCima());
            tabellone[pos[0]][pos[1]].setScala(null);
            tabellone[posCima[0]][posCima[1]].setScala(null);
            scale.remove(numCasella);
        }
    }

    public void addScala(Scala s) throws IllegalPositionException, IllegalPositionRowException, OccupedBoxException {
        if(s==null)
            throw new IllegalArgumentException("Scala inesistente");
        int[] posPiedi = trovaPosizioneCasella(s.getPiedi());
        int[] posCima = trovaPosizioneCasella(s.getCima());
        if (posPiedi == null || posCima == null) throw new IllegalPositionException(" Inserire delle posizioni valide alla scala");
        if(!(posCima[0]<posPiedi[0]))//devono essere in righe differenti
            throw new IllegalPositionRowException("Inserire la scala su righe differenti");
        if(tabellone[posPiedi[0]][posPiedi[1]].getTipo()!=TipoCasella.NORMALE || tabellone[posPiedi[0]][posPiedi[1]].getSerpente() != null || tabellone[posPiedi[0]][posPiedi[1]].getScala() != null)
            throw new OccupedBoxException("impossibile inserire i piedi della scala perchè è presente una casella speciale o un serpente");
        if(tabellone[posCima[0]][posCima[1]].getTipo()!=TipoCasella.NORMALE || tabellone[posCima[0]][posCima[1]].getSerpente() != null || tabellone[posCima[0]][posCima[1]].getScala() != null)
            throw new OccupedBoxException("impossibile inserire la cima della scala perchè è presente una casella speciale o un serpente");
        tabellone[posPiedi[0]][posPiedi[1]].setScala(s);
        tabellone[posCima[0]][posCima[1]].setScala(s);
        scale.put(s.getPiedi(), s);
    }

    public void addSerpente(Serpente s) throws IllegalPositionException, IllegalPositionRowException, OccupedBoxException {
        if(s==null)
            throw new IllegalArgumentException("Serpente inesistente");
        int[] posCoda = trovaPosizioneCasella(s.getCoda());
        int[] posTesta = trovaPosizioneCasella(s.getTesta());
        if (posCoda == null || posTesta == null) throw new IllegalPositionException(" Inserire delle posizioni valide al serpente");
        if(!(posTesta[0]<posCoda[0]))
            throw new IllegalPositionRowException("Inserire il serpente su righe differenti");
        if(tabellone[posCoda[0]][posCoda[1]].getTipo()!=TipoCasella.NORMALE || tabellone[posCoda[0]][posCoda[1]].getScala() != null || tabellone[posCoda[0]][posCoda[1]].getSerpente() != null)
            throw new OccupedBoxException("impossibile inserire la coda del serpente perchè è presente una casella speciale o un scala");
        if(tabellone[posTesta[0]][posTesta[1]].getTipo()!= TipoCasella.NORMALE || tabellone[posTesta[0]][posTesta[1]].getScala() != null || tabellone[posTesta[0]][posTesta[1]].getSerpente() != null)
            throw new OccupedBoxException("impossibile inserire la testa del serpente perchè è presente una casella speciale o una scala");
        tabellone[posCoda[0]][posCoda[1]].setSerpente(s);
        tabellone[posTesta[0]][posTesta[1]].setSerpente(s);
        serpenti.put(s.getTesta(),s);
    }

    public int[] trovaPosizioneCasella(int numCasella) {//utilizzato per rendere le ricerche più efficienti
        return posizioneCaselle.get(numCasella);
    }

    public TabelloneMemento salvaStato() {
        Map<Integer, Serpente> copiaSerpenti = new HashMap<>(serpenti);
        Map<Integer, Scala> copiaScale = new HashMap<>(scale);
        Map<Integer, Casella> copiaCaselleSpeciali = new HashMap<>(caselleSpeciali);
        return new TabelloneMemento(numRighe, numCol, copiaSerpenti, copiaScale,copiaCaselleSpeciali);
    }

    public void caricaStato(TabelloneMemento memento) {
        // Ripristina lo stato del tabellone dai valori memorizzati nel memento
        numRighe = memento.getNumRighe();
        numCol = memento.getNumCol();
        serpenti = memento.getSerpenti();
        scale = memento.getScale();
        caselleSpeciali = memento.getCaselleSpeciali();
    }
    private Casella[][] copiaTabellone() {
        // Effettua la copia del tabellone
        Casella[][] copia = new Casella[numRighe][numCol];
        for (int i = 0; i < numRighe; i++) {
            for (int j = 0; j < numCol; j++) {
                copia[i][j] = tabellone[i][j].clone();
            }
        }
        return copia;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<numRighe;i++){
            for(int j=0;j<numCol;j++){
                sb.append(tabellone[i][j].getNum());
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}