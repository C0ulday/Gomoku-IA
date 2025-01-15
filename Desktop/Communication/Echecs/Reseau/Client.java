package Echecs.Reseau;

import Echecs.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;



public class Client {
    

    private String serverAddress;
    private int serverPort;
    private Socket socket;

    private ObjectInputStream in;
    private ObjectOutputStream out; 

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        try {
            this.socket = new Socket(serverAddress, serverPort);

            // Fallait mettre la sortie d'abord puis l'entrée sinon c'est bloquant
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream()); 
            System.out.println("[CLIENT] : Connexion réussie au serveur.");
        } catch (IOException e) {
            System.err.println("[CLIENT] : Impossible de se connecter au serveur: " + e.getMessage());
        }
    }

    public Client(Socket socket){
        try { 
            this.socket = socket;
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
            System.out.println("Impossible de créer un Socket pour le Client " + e.getMessage());
        }
    }


    public void Ready(){

        System.out.println("[CLIENT] : Connecté au serveur à " + serverAddress + ":" + serverPort);

        // Envoyer ses infos de connexion
        Connection();

        // En attente du serveur
        new Thread(this::ReceiveMessage).start();

    
    }

    public void Connection(){

        String nom;
        String couleur;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Entrez votre nom : ");
        nom = scanner.nextLine();
        System.out.print("Choisissez votre couleur : ");
        couleur = scanner.nextLine();

        // Encapsulation du message à envoyer

        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String temps = maintenant.format(formatter);

        Message messNom = new Message("nom",""+socket,nom,temps);
        Message messCouleur= new Message("couleur",""+socket,couleur,temps);
        
        sendMessage(messNom);
        sendMessage(messCouleur);
        System.out.println("[CLIENT] : Informations envoyées au serveur.");
    }

    public void CheckConnection(){

        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String temps = maintenant.format(formatter);

        Message mess = new Message("CHECK",""+socket,"",temps);
        try{
            sendMessage(mess);

        } catch (IOException e){
            Error("Impossible de tester la connexion avec le serveur");
            e.printStackTrace();
        }

    }

    public void ReceiveMessage() {
        try {
            while (true) {
                Message message = (Message) in.readObject();
                switch (message.getType()) {
                    case "CONNECTION":
                        System.out.println("[CLIENT] : Prêt à jouer !");
                        break;
                    case "CHECK":
                        System.out.println("[CLIENT] : Connexion active.");
                        break;
                    case "RULES":
                        System.out.println("[CLIENT]-[RÈGLES] : " + message.getData());
                        break;
                    case "BOARD":
                        System.out.println("[CLIENT]-[BOARD] : " + message.getData());
                        break;
                    default:
                        System.out.println("[CLIENT] : Message inconnu reçu : " + message);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[CLIENT] : Connexion perdue ou erreur de réception: " + e.getMessage());
        } finally {
            closeResources();
        }
    }


    private void closeResources() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
            System.out.println("[CLIENT] : Ressources libérées.");
        } catch (IOException e) {
            System.err.println("[CLIENT] : Erreur lors de la fermeture des ressources: " + e.getMessage());
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
            out.flush();
        } catch (IOException e) {
            Error("Impossible d'envoyer le message au client : " + e.getMessage());
        }
    }
    public void Error(String message){

        String contenu = "[CLIENT]-[ERROR] :";
        contenu += message;
        System.out.println(contenu + "\n");

    }

    public ObjectInputStream getClientInputStream(){
        return in;
    }

    public ObjectOutputStream getClientOutputStream(){
        return out;
    }

    public Socket getSocket(){
        return socket;
    }

    
    public static void main(String[] args){

        String serverAddress = "localhost";
        int serverPort = 9090;
        System.out.println("Creation du client");
        Client client = new Client(serverAddress,serverPort);
        System.out.println("[CLIENT] : Tentative de connexion au serveur à " + serverAddress + ":" + serverPort + "...");
        client.Ready();

        Message mess = new Message("CONNECTION",client.getSocket().toString(),"","15/02/20029");
        client.sendMessage(mess);
        while (true) { 
            
        }
    }

}
