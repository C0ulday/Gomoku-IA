public class Joueur
{
    	/**
	 * Defini si le Joueur est humain ou non
	 */

	protected boolean estHumain = true;
	
	/**
	 * Couleur du joueur
	 */
	protected String couleur;
    
	//Constructeur
	public Joueur(String c)
    {
        this.couleur = c;
    }
    public String getCouleur()
    {
        return this.couleur;
    }

    	/**
	 * Retourne si le joueur est humain
	 * @return True si il l'est sinon false
	 */
	public boolean estHumain(){
		return estHumain;
	}

	/** 
	 * Defini si le Joueur est humain
	 * @param b
	 */
	public void setHumain(boolean b){
		estHumain = b;
	}
    
}