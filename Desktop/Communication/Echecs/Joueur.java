package Echecs;

public class Joueur {
    

    private boolean estHumain = true;
    private String nom;
    private String couleur;
    private Chrono chrono;
    
    
    public Joueur(String nom, String couleur,Chrono chrono){
        this.nom = nom;
        this.couleur = couleur;
        this.chrono = new Chrono(0,10,0);
    }

    public String getCouleur(){
        return this.couleur;
    }

    public String getNom(){
        return this.nom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public void setCouleur(String couleur){
        this.couleur = couleur;
    }

    public void setIA(){
        estHumain = false;
    }

    public void setChrono(int h,int m,int s){
        chrono.setHeures(h);
        chrono.setMinutes(m);
        chrono.setSecondes(s);
    }

}
