package Echecs.Reseau;
import java.io.*;
import java.net.Socket;

/*
 * 
 * Réprésentation du client sur le serveur
 * 
 */
public class ServerClient {

    // Socket utilisé pour établir une connexion entre le client et le serveur
    private Socket socket;

    // Fkux d'entrée
    ObjectInputStream in;

    // Flux de sortie
    ObjectOutputStream out;

   // Constructeur de la classe Client, qui initialise le socket du client.
   
   public ServerClient(Socket socket) {
    
        this.socket = socket;
            
        try {
            // Initialisation du flux d'entrée pour recevoir des messages
            this.in = new ObjectInputStream(socket.getInputStream());

            // Initialisation du flux de sortie pour envoyer des messages
            this.out = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            // Gestion des erreurs liées à l'initialisation des flux.
            e.printStackTrace();
        }
    }

}
