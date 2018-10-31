package Models;


public class Maladie 
{
	String nom;
	int scoreAcc; //Score où le patient a 100% de chances d'avoir la maladie 
	int scoreRef; //Score où le patient a 100% de ne pas avoir la maladie
	String desc;
	
	
	public Maladie(String nom, int sacc, int sref, String desc)
	{
		this.nom=nom;
		this.scoreAcc=sacc;
		this.scoreRef=sref;
		this.desc=desc;
	}
	public Maladie(String nom)
	{
		this.nom=nom;
	}
	
	public String getDesc()
	{
		return this.desc;
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
