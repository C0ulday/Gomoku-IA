package Echecs.Reseau;

import Echecs.Joueur;



public class ServerPlayer {
    
    private Client client;
    private Joueur joueur;

    public ServerPlayer(Client client,Joueur joueur){
        this.client = client;
        this.joueur = joueur;
    }


    public Client getClient(){
        return this.client;
    }

    public Joueur getJoueur(){
        return this.joueur;
    }


}
