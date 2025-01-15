package Echecs;


public class Chrono {
    private int heures;
    private int minutes;
    private int secondes;


    public Chrono(int h, int m, int s){
        heures = h;
        minutes = m;
        secondes = s;

    }

    public int getHeures(){
        return heures;
    }

    public int getMinutes(){
        return minutes;
    }

    public int getSecondes(){
        return secondes;
    }

    public void setHeures(int h){
        heures = h;
    }

    public void setMinutes(int m){
        minutes = m;
    }
    
    public void setSecondes(int s){
        secondes = s;
    }

    public String toString(){

		String contenu = "";

        if(heures < 10){
            contenu += "0"+heures;
        } else{
            contenu = contenu + heures + ":";
        }
		if(minutes < 10){
			contenu += "0"+minutes;
		}else{
			contenu = contenu + minutes + ":";
		}
		
		if(secondes < 10){
			contenu += "0"+secondes;
		}else{
			contenu += secondes;
		}
		return contenu;
	}
}
