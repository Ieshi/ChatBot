package ac.ma.ensa.chat.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ac.ensa.chat.maladie.*;
import ac.ma.ensa.chat.message.MessagesBot;
import ac.ma.ensa.chat.message.Question;

import java.util.ArrayList;
import java.util.Random;

public class ChatServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String msgs;
	String msg;
	String author;
	String msgFin = "";
	Question msgBot= new Question();
	int nbrMessagesUser=0;
	ArrayList<Score> scoresUser = new ArrayList<Score>(); //Les scores de l'utilisateur pour chaque maladie
	ArrayList numUtilise = new ArrayList(); //Contient les numero utilise lors d'une phase
	ArrayList numUtilisePhase2 = new ArrayList(); //Contient les numero utilise lors d'une phase
	MessagesBot messagesBot = new MessagesBot();  //Contient tout les messages possible du bot
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		req.getRequestDispatcher("WEB-INF/chat.jsp").forward(req, resp);
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		
		
		resp.setCharacterEncoding("utf-8");
			msg = req.getParameter("msg");
			author = req.getParameter("author"); 
			
			nbrMessagesUser++; //Nombre d'echange avec l'utilisateur
			
			
			
			
			if(nbrMessagesUser==1) //Le premier message
				messageBienvenue();
			if(nbrMessagesUser>1 && nbrMessagesUser<=4) //Les trois prochains
				messagePhase1();
			if(nbrMessagesUser==5 && !numUtilise.isEmpty())
			{
				numUtilise.clear(); //On supprime le contenu de numUtilise pour une utilisation avenir
				 System.out.println("2");
			}
			if(nbrMessagesUser==5)
				messagedebutPhase2();
			if(nbrMessagesUser>5)
			{
				ArrayList indexMaladieAtt = new ArrayList();
				if(unDesScoresAtteindScoreAcc(scoresUser, indexMaladieAtt))
				{
					//Message de sortie fin du test
					Maladie maladie = maladieAvecPlusHautScore(scoresUser);
					int index = indexMaladieAvecPlusHautScore(scoresUser);
					if(indexMaladieAtt.contains(index))
					{
						String txt = "C'est fini! vous souffrez de ";
						for(int k=0; k<indexMaladieAtt.size(); k++)
						{
							txt+=" et de " + scoresUser.get(Integer.parseInt(indexMaladieAtt.get(k).toString())).getMaladie().getNom();
							System.out.println("Maladie: " + scoresUser.get(Integer.parseInt(indexMaladieAtt.get(k).toString())).getMaladie().getNom() + " Score: " + scoresUser.get(Integer.parseInt(indexMaladieAtt.get(k).toString())).getScore());
						}
						msgBot.setIntitule(txt + ". Voulez vous effectuer un autre test?");
					}
					else
					{
						onContinue();
					}
					
				}
				else
				{
					onContinue();
				}
					
			}
				
			resp.getWriter().write(author+"+"+msg+"+"+"+"+msgBot.getIntitule());
				

				// crÃ©ation de l'objet message
				msg = msg.toLowerCase();
				msgs += msg;
				msgs = msgs.replace("user", "");
				msgs = msgs.replace("null", "");
				msgs = msgs.replace("message", "");
				msgs = msgs.replace(" ", "");
				System.out.println("Messages: " + msgs);
		
	}
	
	int selectRandomNumberNonUtilise(int bounds, ArrayList numUtil)
	{
		Random rand = new Random(); //On choisi un nombre au hasard entre 0 et bounds
		int randomNum = rand.nextInt(bounds);
		while(numUtil.contains(randomNum)) //Tant que le numero a déjà été utilisé
		{
			randomNum++;	randomNum %= bounds; //On incrémente et on s'assure qu'il existe bien dans la liste de message
		}
		
		return randomNum;
	}
	
	void messageBienvenue()
	{
		int randomNum=selectRandomNumberNonUtilise(5, numUtilise);
		msgBot.setIntitule(messagesBot.getmessagesBienvenue().get(randomNum));
	}
	
	void messagePhase1()
	{
		int randomNum=selectRandomNumberNonUtilise(messagesBot.getphase1().size(), numUtilise);
	    msgBot.setIntitule(messagesBot.getphase1().get(randomNum)); //Après s'être assuré que le message n'a pas déjà été envoyé on l'utilise
	    numUtilise.add(randomNum); //Et on ajoute le numero utilisé dans la liste
	    
	}
	
	void messagedebutPhase2()
	{
		int randomNum=selectRandomNumberNonUtilise(messagesBot.getdebutPhase2().size(), numUtilise);
		numUtilise.add(randomNum);
		msgBot=messagesBot.getdebutPhase2().get(randomNum);
	}
	
	boolean isAllNegativeOrZerosOrEmpty(ArrayList<Score> score)

	{
		boolean tmp=true;
		int compt=0;
		if(score.isEmpty())
			return true;
		while(tmp==true && compt!=score.size())
		{
			if((score.get(compt).getScore())>0)
				tmp=false;
			compt++;
		}
		return tmp;
	}
	
	boolean contient(ArrayList<Score> list, String nom) //Ca marche mwhaahahahhaha !
	{
		boolean trouv=false;
		String tmpnom=nom.toLowerCase();
		int compt=0;
		while(trouv==false && compt<list.size())
		{
			String tmp = list.get(compt).getMaladie().getNom().toLowerCase();
			if(tmp.contains(tmpnom))
			{
				trouv=true;
			}
			compt++;
		}
		return trouv;
	}
	
	int getIndex(ArrayList<Score> list, String nom)
	{
		boolean trouv=false;
		int compt=0;
		while(trouv=false && compt<list.size())
		{
			if(list.get(compt).getMaladie().getNom().equals(nom))
				trouv=true;
		}
		return compt;
	}
	
	Maladie maladieAvecPlusHautScore(ArrayList<Score> scores)
	{
		int maxScore=scores.get(0).getScore();
		int index=0;
		for (int i=1; i<scores.size(); i++)
		{
			if(maxScore<scores.get(i).getScore())
			{
				maxScore=scores.get(i).getScore();
				index=i;
			}
		}
		
		Maladie maladie = scores.get(index).getMaladie();
		
		return maladie;
	}
	
	void onContinue()
	{
		if(msg.toLowerCase().contains(msgBot.getReponseAsense().toLowerCase())) //Si la reponse de l'utilisateur est egale a la reponse attendue
		{
			for(int i=0; i<msgBot.getMaladiesAssociee().size(); i++)
			{
				if(!contient(scoresUser,msgBot.getMaladiesAssociee().get(i).getNom()))
				{
					scoresUser.add(new Score(new Maladie(msgBot.getMaladiesAssociee().get(i).getNom()), msgBot.getScore()));
					System.out.println("new score added !");
					System.out.println(msgBot.getMaladiesAssociee().get(i).getNom());
				}
				else
				{
					int index=getIndex(scoresUser, msgBot.getMaladiesAssociee().get(i).getNom());
					int nouveauScore=scoresUser.get(index).getScore()+msgBot.getScore();
					scoresUser.get(index).setScore(nouveauScore);
					System.out.println("score added !");
					System.out.println(msgBot.getMaladiesAssociee().get(i).getNom());
				}
			}
		}
		if(!msg.toLowerCase().contains(msgBot.getReponseAsense().toLowerCase())) //Si la reponse de l'utilisateur n'est pas celle attendue on reduit le score de la maladie concidérée
		{
			for(int i=0; i<msgBot.getMaladiesAssociee().size(); i++)
			{
				if(!contient(scoresUser,msgBot.getMaladiesAssociee().get(i).getNom()))
				{
					scoresUser.add(new Score(new Maladie(msgBot.getMaladiesAssociee().get(i).getNom()), msgBot.getScore()*-1));
				}
				else
				{
					int index=getIndex(scoresUser, msgBot.getMaladiesAssociee().get(i).getNom());
					System.out.println("Msg bot precedent score: " + msgBot.getScore());
					int nouveauScore=scoresUser.get(index).getScore()-msgBot.getScore();
					scoresUser.get(index).setScore(nouveauScore);
				}
			}
		}
	
		if(isAllNegativeOrZerosOrEmpty(scoresUser)) //Si tout est 0 ou negatif on revient au debut de la phase 2
		{
			messagedebutPhase2();
		}
		if(!isAllNegativeOrZerosOrEmpty(scoresUser)) //Sinon on passe au coeur de la phase 2
		{
			System.out.println("hiiii---------");
			Maladie maladie= maladieAvecPlusHautScore(scoresUser);
			System.out.println(maladie.getNom());
			messagesBot.setPhase2(maladie);
			int randomNum=selectRandomNumberNonUtilise(messagesBot.getphase2().size(), numUtilisePhase2);
			numUtilisePhase2.add(randomNum);
			msgBot=messagesBot.getphase2().get(randomNum);
		}

	}
	
	boolean unDesScoresAtteindScoreAcc(ArrayList<Score> list, ArrayList indexScoreAtt)
	{
		boolean att=false;
		for(int i=0; i<list.size(); i++)
		{
			int scoreAccMaladie = list.get(i).getMaladie().getScoreAcc();
			int scoreActuelMaladie = list.get(i).getScore();
			System.out.println("Maladie concidérée: " + list.get(i).getMaladie().getNom() + " ScoreAcc: " + scoreAccMaladie + " Score Actuel: " + scoreActuelMaladie);
			if(scoreActuelMaladie>=scoreAccMaladie)
			{
				att=true;
				indexScoreAtt.add(i);
			}
		}
		
		return att;
	}
	
	int indexMaladieAvecPlusHautScore(ArrayList<Score> scores)
	{
		int maxScore=scores.get(0).getScore();
		int index=0;
		for (int i=1; i<scores.size(); i++)
		{
			if(maxScore<scores.get(i).getScore())
			{
				maxScore=scores.get(i).getScore();
				index=i;
			}
		}
		
		return index;
	}
	
	//http://localhost:8081/Chat/chat
}