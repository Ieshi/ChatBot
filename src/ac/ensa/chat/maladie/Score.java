package ac.ensa.chat.maladie;

public class Score 
{
	//Un Score concerne aussi une maladie donc on peut dire que c'est un couple (maladie, note)
	private Maladie maladie;
	private int score; 
	
	public Score(Maladie maladie, int score)
	{
		this.maladie=maladie;
		this.score=score;
	}
	public Maladie getMaladie()
	{
		return this.maladie;
	}
	public int getScore()
	{
		return this.score;
	}
	public void setScore(int s)
	{
		this.score=s;
	}

}
