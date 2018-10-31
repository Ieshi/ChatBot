package Controllers;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Models.Maladie;
import Models.MessagesBot;
import Models.Question;
import Models.Score;

import java.util.ArrayList;
import java.util.Random;

/*
 * Ce qu'il faut faire:
 * Regler le problème de la phase 2: pas assez de question ouverte donc mli kaykhtarha w makayl9ach un autre num il bloque check
 * Decider de ce qu'on va faire a la fin
 * il faut aussi compléter la base de donnée avec d'autres maladies des openquestion et des messages de fin
 * et enfin enfin le design de la merde
 */


public class ChatServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	String msgs;
	String msg;
	String author;
	Question msgBot= new Question();
	int nbrMessagesUser=0;

	ArrayList idqs = new ArrayList();
	ArrayList<Score> scoresUser = new ArrayList<Score>(); //Les scores de l'utilisateur pour chaque maladie
	ArrayList numUtilise = new ArrayList(); //Contient les numero utilise lors d'une phase
	ArrayList numUtilisePhase2 = new ArrayList(); //Contient les numero utilise lors d'une phase
	ArrayList numUtiliseDebutPhase2 = new ArrayList();
	ArrayList numUtiliseAutre = new ArrayList();
	int nbrQuestionsOuvertes=23;
	int cbQuestionsOuvertesApp=0;
	
	MessagesBot messagesBot = new MessagesBot();  //Contient tout les messages possible du bot
	String end="no";
	boolean openqst=false;
	
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
		if (end.contains("no"))
			debutDuTest();
		else
		{
			if (msg.contains("yes"))
			{
				end="no";
				nbrMessagesUser=0;
				scoresUser.clear();
				debutDuTest();
			}
			else
			{
				msgBot.setIntitule("Okay! I hope you get better.");
			}
		}
		
		System.out.println("Message bot de fin de tour: " + msgBot.getIntitule());


		resp.getWriter().write(author+"+"+msg+"+"+"+"+msgBot.getIntitule());
	}
	
	int selectRandomNumberNonUtilise(int bounds, ArrayList numUtil)
	{
		for (int i=0; i<numUtil.size(); i++)
			System.out.println("List numUtil Element: " + i + " valeur: " + numUtil.get(i));
		Random rand = new Random(); //On choisi un nombre au hasard entre 0 et bounds
		int randomNum = rand.nextInt(bounds);
		System.out.println("Random num choisen before while in selectRandomNumberNonUtilise: " + randomNum);
		int tmp = bounds+1;
		while(randomNum==0 || numUtil.contains(randomNum)) //Si c'est un 0 ou si il a déjà été utilisé
		{
			randomNum++; randomNum%=tmp;
			System.out.println("Random num considered in while selectRandomNumber:" + randomNum);
		}
		System.out.println("Random num choisen after while in selectRandomNumberNonUtilise: " + randomNum);
		return randomNum;
	}
	
	int selectRandomNumberNonUtiliseSpecPhase2(int bounds, ArrayList numUtil)
	{
		for (int i=0; i<numUtil.size(); i++)
			System.out.println("SpecPhase2: List numUtil Element: " + i + " valeur: " + numUtil.get(i));
		Random rand = new Random(); //On choisi un nombre au hasard entre 0 et bounds
		int randomNum = rand.nextInt(bounds);
		while(numUtil.contains(Integer.parseInt(idqs.get(randomNum).toString()))) //si il a déjà été utilisé
		{
			randomNum++; randomNum%=bounds;
			System.out.println("RandomNumSpecPhase2: " + randomNum);
		}
		
		return randomNum;
	}
	
	void messageBienvenue()
	{
		int randomNum=selectRandomNumberNonUtilise(10, numUtilise);
		messagesBot.setMessageBienvenue(randomNum);
		msgBot.setIntitule(messagesBot.getmessagesBienvenue());
	}
	
	void messagePhase1()
	{
		int randomNum=selectRandomNumberNonUtilise(15, numUtilise);
		System.out.println("Random Num Used Phase 1:" + randomNum);
		messagesBot.setMessagePhase1(randomNum);
	    msgBot.setIntitule(messagesBot.getphase1()); //Après s'être assuré que le message n'a pas déjà été envoyé on l'utilise
	    numUtilise.add(randomNum); //Et on ajoute le numero utilisé dans la liste
	    
	}
	
	void messagedebutPhase2()
	{
		int randomNum=selectRandomNumberNonUtilise(8, numUtiliseDebutPhase2);
		System.out.println("RandomNum Used debut phase 2: " + randomNum);
		numUtilise.add(randomNum);
		messagesBot.setDebutPhase2(randomNum);
		msgBot=messagesBot.getdebutPhase2();
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
		System.out.println("list: " + list.size() + " nom: " + nom + " compt: " + compt);
		while(trouv==false && compt<list.size())
		{
			if(list.get(compt).getMaladie().getNom().equals(nom))
				trouv=true;
			compt++;
		}
		return compt;
	}
	
	int getIndex(ArrayList<Question> list, int idq)
	{
		boolean trouv=false;
		int compt=0;
		System.out.println("list: " + list.size() + " idq: " + idq + " compt: " + compt);
		while(trouv==false && compt<list.size())
		{
			System.out.println("compt: " + compt);
			if(list.get(compt).getidq() == idq )
			{
				System.out.println("list.get(compt).getidq(): " + list.get(compt).getidq());
				System.out.println("idq: " + idq);
				
				trouv=true;
			}
			compt++;
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
		for(int k=0; k<msgBot.getMaladiesAssociee().size(); k++)
			System.out.println("Maladie phase2: " + msgBot.getMaladiesAssociee().get(k).getNom());
		System.out.println("------------------------------------------------------");
	if(openqst==false)
	{
		if(msg.toLowerCase().contains(msgBot.getReponseAsense().toLowerCase())) //Si la reponse de l'utilisateur est egale a la reponse attendue
		{
			for(int i=0; i<msgBot.getMaladiesAssociee().size(); i++)
			{
				if(!contient(scoresUser,msgBot.getMaladiesAssociee().get(i).getNom()))
				{
					scoresUser.add(new Score(new Maladie(msgBot.getMaladiesAssociee().get(i).getNom(), msgBot.getMaladiesAssociee().get(i).getScoreAcc(), msgBot.getMaladiesAssociee().get(i).getScoreRef(), msgBot.getMaladiesAssociee().get(i).getDesc()), msgBot.getScore()));
					System.out.println("new score added !");
					System.out.println(msgBot.getMaladiesAssociee().get(i).getNom());
				}
				else
				{
					int index=getIndex(scoresUser, msgBot.getMaladiesAssociee().get(i).getNom())-1;
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
					scoresUser.add(new Score(new Maladie(msgBot.getMaladiesAssociee().get(i).getNom(), msgBot.getMaladiesAssociee().get(i).getScoreAcc(), msgBot.getMaladiesAssociee().get(i).getScoreRef(), msgBot.getMaladiesAssociee().get(i).getDesc()), msgBot.getScore()*-1));
				}
				else
				{
					int index=getIndex(scoresUser, msgBot.getMaladiesAssociee().get(i).getNom())-1;
					System.out.println("Msg bot precedent score: " + msgBot.getScore());
					int nouveauScore=scoresUser.get(index).getScore()-msgBot.getScore();
					scoresUser.get(index).setScore(nouveauScore);
				}
			}
		}
	
		if(isAllNegativeOrZerosOrEmpty(scoresUser)) //Si tout est 0 ou negatif on revient au debut de la phase 2
		{
			System.out.println("in the isAllNegativeOrZerosOrEmpty(scoresUser)");
			messagedebutPhase2();
			System.out.println("ici");
		}
		if(!isAllNegativeOrZerosOrEmpty(scoresUser)) //Sinon on passe au coeur de la phase 2
		{
			System.out.println("hiiii---------");
			
			Random rand = new Random();
			int randomNum = rand.nextInt(3);
			System.out.println("randomNum choisen: " + randomNum);
			if(randomNum==1)
				coeurPhase2();
			else
				{
					if(cbQuestionsOuvertesApp<nbrQuestionsOuvertes)
						autreQuestion();
					else
						coeurPhase2();
				}
			
		}
	}
	else
	{
		Random rand = new Random(); //On choisi un nombre au hasard entre 0 et bounds
		int randomNum = rand.nextInt(3);
		System.out.println("randomNum choisen: " + randomNum);
		if(randomNum==1)
			coeurPhase2();
		else
		{
			if(cbQuestionsOuvertesApp<nbrQuestionsOuvertes)
				autreQuestion();
			else
				coeurPhase2();
		}
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
	
	void debutDuTest()
	{
		
		nbrMessagesUser++; //Nombre d'echange sensé avec l'utilisateur
		System.out.println("nbrMessagesUser = " + nbrMessagesUser);
		if(nbrMessagesUser==1)
			messageBienvenue();
		if(nbrMessagesUser>1 && nbrMessagesUser<=4) //Les trois prochains
			messagePhase1();
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
					end="yes";
					String txt = "You suffer from: ";
					for(int k=0; k<indexMaladieAtt.size(); k++)
					{
						txt+= scoresUser.get(Integer.parseInt(indexMaladieAtt.get(k).toString())).getMaladie().getNom() + " ";
						txt+=scoresUser.get(Integer.parseInt(indexMaladieAtt.get(k).toString())).getMaladie().getDesc() + " ";
						System.out.println("Maladie: " + scoresUser.get(Integer.parseInt(indexMaladieAtt.get(k).toString())).getMaladie().getNom() 
								+ ": " + scoresUser.get(Integer.parseInt(indexMaladieAtt.get(k).toString())).getMaladie().getDesc()
								+ " Score: " + scoresUser.get(Integer.parseInt(indexMaladieAtt.get(k).toString())).getScore());
					}
					msgBot.setIntitule(txt + ". Do you want to try again?");
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
		// crÃ©ation de l'objet message
			msg = msg.toLowerCase();
			msgs += msg;
			msgs = msgs.replace("user", "");
			msgs = msgs.replace("null", "");
			msgs = msgs.replace("message", "");
			msgs = msgs.replace(" ", "");
			System.out.println("Messages: " + msgs);
	}
	
	void autreQuestion()
	{
		System.out.println("In autre question");
		cbQuestionsOuvertesApp++;
		//D'autres questions pour faire parler le client
		int randomNum = selectRandomNumberNonUtilise(nbrQuestionsOuvertes, numUtiliseAutre);
		numUtiliseAutre.add(randomNum);
		messagesBot.setAutreQuestion(randomNum);
		msgBot.setIntitule(messagesBot.getAutreQuestion());
		openqst=true;
	}
	
	void coeurPhase2()
	{
		idqs.clear();
		messagesBot.getphase2().clear();
		Maladie maladie= maladieAvecPlusHautScore(scoresUser);
		System.out.println(maladie.getNom());
		messagesBot.setPhase2(maladie);
		
		for(int k=0; k<messagesBot.getphase2().size(); k++)
		{
			idqs.add(messagesBot.getphase2().get(k).getidq());
			System.out.println("Element: " + k + "idq: " + messagesBot.getphase2().get(k).getidq() 
					+ "Intitule: " + messagesBot.getphase2().get(k).getIntitule());
		}
		
		int tmp = idqs.size();
		System.out.println("tmp: " + tmp);
		int randomNum=selectRandomNumberNonUtiliseSpecPhase2(tmp, numUtilisePhase2);
		numUtilisePhase2.add(idqs.get(randomNum));
		
		int indexqst = getIndex(messagesBot.getphase2(), Integer.parseInt(idqs.get(randomNum).toString()))-1; //here prob
		
		msgBot=messagesBot.getphase2().get(indexqst);
		System.out.println("randomNum: " + randomNum);
		System.out.println("indexqst: " + indexqst);
		System.out.println("msgBot coeurPhase2: " + msgBot.getIntitule());
		System.out.println("messagesBot.getphase2().get(indexqst): "
							+ messagesBot.getphase2().get(indexqst).getIntitule());
		openqst=false;
	}
	
	
	//http://localhost:8081/Chat/chat
}