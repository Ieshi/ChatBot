package ac.ensa.chat.maladie;


public class Maladie 
{
	String nom;
	int scoreAcc; //Score où le patient a 100% de chances d'avoir la maladie 
	int scoreRef; //Score où le patient a 100% de ne pas avoir la maladie
	
	
	public Maladie(String nom, int sacc, int sref)
	{
		this.nom=nom;
		this.scoreAcc=sacc;
		this.scoreRef=sref;
	}
	public Maladie(String nom)
	{
		this.nom=nom;
	}
	
	void setNom(String nom)
	{
		this.nom=nom;
	}
	
	void setScoreAcc(int sacc)
	{
		this.scoreAcc=sacc;
	}
	
	void setScoreRef(int sref)
	{
		this.scoreAcc=sref;
	}
	
	public String getNom()
	{
		return nom;
	}
	
	public int getScoreAcc()
	{
		return scoreAcc;
	}
	
	public int getScoreRef()
	{
		return scoreRef;
	}


}
