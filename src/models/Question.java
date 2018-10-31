package Models;

import java.util.ArrayList;


public class Question 
{
	private int idq;
	private int idm;
	private int score;
	private String intitule;
	private String reponseAsense;
	private ArrayList<Maladie> maladiesAssociee = new ArrayList<Maladie>();
	
	public Question()
	{
		
	}
	
	public Question(String intitule, String reponseAsense, ArrayList<Maladie> maladiesAssociee, int idq)
	{
		this.intitule=intitule;
		this.reponseAsense=reponseAsense;
		this.idq=idq;
		this.score=10;
		this.maladiesAssociee=maladiesAssociee;
	}
	
	public Question(String intitule, String reponseAsense, Maladie maladieAssociee, int idq)
	{
		this.intitule=intitule;
		this.reponseAsense=reponseAsense;
		this.idq=idq;
		this.score=10;
		this.maladiesAssociee.add(maladieAssociee);
	}
	
	public Question(String intitule, String reponseAsense, ArrayList<Maladie> maladiesAssociee, int idq, int idm, int score)
	{
		this.intitule=intitule;
		this.reponseAsense=reponseAsense;
		this.idq=idq;
		this.idm=idm;
		this.score=score;
		this.maladiesAssociee=maladiesAssociee;
	}
	
	public Question(String intitule)
	{
		this.intitule=intitule;
		this.reponseAsense="Open";
	}
	
	public String getIntitule()
	{
		return this.intitule;
	}
	
	public int getScore()
	{
		return this.score;
	}
	public int getidq()
	{
		return this.idq;
	}
	
	public void setIntitule(String intitule)
	{
		this.intitule=intitule;
	}
	
	public void setReponseASense(String rep)
	{
		this.reponseAsense=rep;
	}
	
	public String getReponseAsense()
	{
		return this.reponseAsense;
	}
	public ArrayList<Maladie> getMaladiesAssociee()
	{
		return this.maladiesAssociee;
	}

}
