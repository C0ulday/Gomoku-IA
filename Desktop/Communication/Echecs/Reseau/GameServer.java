package Echecs.Reseau;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Echecs.Joueur;
import Echecs.Message;
import Echecs.Chrono;
import Echecs.Reseau.ServerPlayer;

/*
 * Cette classe permet de faire l'interface entre le Serveur et les clients
 */
public class GameServer {

    private ArrayList<Client> clients;
    private ServerPlayer jBlanc;
    private ServerPlayer jNoir;

    public GameServer() {
        clients = new ArrayList<Client>();
    }

    // Ajouter un joueur
    public void addJoueur(Client client) {
        this.clients.add(client);
        System.out.println("[SERVER]-[INFO] : Ajout d'un nouveau joueur");
    }

    /* Lancement d’une partie avec dénomination du numéro du joueur */
    /* Lancement d’une partie avec dénomination du numéro du joueur */
    public void Ready() {
        if (clients.size() == 2) {

            /* Définition des joueurs */
            try {
                String nomBlanc = ReceiveStringMessage(clients.get(0), "nom");
                String couleurBlanc = ReceiveStringMessage(clients.get(0), "couleur");
                jBlanc = new ServerPlayer(clients.get(0), new Joueur(nomBlanc, couleurBlanc, new Chrono(0, 10, 0)));

                String nomNoir = ReceiveStringMessage(clients.get(1), "nom");
                String couleurNoir = ReceiveStringMessage(clients.get(1), "couleur");
                jNoir = new ServerPlayer(clients.get(1), new Joueur(nomNoir, couleurNoir, new Chrono(0, 10, 0)));

                System.out.println("[SERVER]-[INFO] : Deux joueurs prêts à s'affronter !");

                // Démarrer la partie
                startGame();
                
            } catch (IOException | ClassNotFoundException e) {
                Error("Erreur lors de la réception des informations des joueurs : " + e.getMessage());
            }
        } else {
            Error("Il faut deux joueurs pour lancer une partie !");
        }
    }

    // Démarrer la partie
    private void startGame() {
        // Logique de démarrage de la partie
        System.out.println("[SERVER]-[INFO] : Partie commencée !");
    }

    // Recevoir un message du client
    public void ReceiveMessage(Client client) {
        try {
            ObjectInputStream in = client.getClientInputStream();
            Message message;

            while (true) {
                message = (Message) in.readObject();

                switch (message.getType()) {
                    case "CONNECTION":
                        System.out.println("[SERVER] : " + message.getSender() + " prêt à jouer !");
                        break;
                    case "CHECK":
                        System.out.println("[SERVER] : Connexion active avec " + message.getSender());
                        break;
                    case "RULES":
                        System.out.println("[SERVER]-[RULES] : " + message.getData());
                        break;
                    case "BOARD":
                        System.out.println("[SERVER]-[BOARD] : " + message.getData());
                        break;
                    default:
                        System.out.println("[SERVER] : Message inconnu reçu.");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            Error("Impossible de lire le message du client : " + e.getMessage());
        }
    }
       // Recevoir un message contenant une chaîne spécifique (nom ou couleur d'un joueur)
       public String ReceiveStringMessage(Client client, String type) throws IOException, ClassNotFoundException {
        ObjectInputStream in = client.getClientInputStream();
        Message messageString = (Message) in.readObject();

        if (type.equals(messageString.getType())) {
            return messageString.getData();
        } else {
            throw new IOException("Type de message inattendu. Attendu : " + type + ", Reçu : " + messageString.getType());
        }
    }

    // Envoyer un message à un client spécifique
    public void sendMessage(Client client, Message message) {
        try {
            ObjectOutputStream out = client.getClientOutputStream();
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            Error("Impossible d'envoyer le message au client : " + e.getMessage());
        }
    }

    // Méthode pour afficher un message d'erreur
    public void Error(String message) {
        String contenu = "[SERVER]-[ERROR] : ";
        contenu += message;
        System.out.println(contenu + "\n");
    }


    public int getNombreClients(){
        return clients.size();
    }
}
