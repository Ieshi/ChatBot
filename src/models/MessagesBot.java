package Models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Models.Connexion;
import Models.Maladie;
import Models.Question;

public class MessagesBot
{
	String messagesBienvenue;
	Question debutPhase2;
	String phase1;
	ArrayList<Question> phase2 = new ArrayList<Question>();
	String autreQuestion;
	Connexion bd = new Connexion();
	
	public MessagesBot() 
	{

	}
	
	public String getmessagesBienvenue()
	{
		return messagesBienvenue;
	}
	
	public String getphase1()
	{
		return phase1;
	}
	public String getAutreQuestion()
	{
		return autreQuestion;
	}
	public Question getdebutPhase2()
	{
		return debutPhase2;
	}
	public ArrayList<Question> getphase2()
	{
		ArrayList<Question> msg = new ArrayList<Question>();
		msg=phase2;
		return msg;
	}
	public void setPhase2(Maladie maladie)
	{
		ArrayList qst = new ArrayList();
		ArrayList rep = new ArrayList();
		ArrayList idqs = new ArrayList();
		ArrayList idms = new ArrayList();
		ArrayList coefs = new ArrayList();
		ArrayList listenommaladie= new ArrayList();
		try 
		{
	//On commence par chercher les question pertinentes a la maladie
			ResultSet rsSelectByMaladie = bd.state.executeQuery("select *  from phase2avecmaladie where maladieAssociee='" + maladie.getNom().toUpperCase() + "';");
			while(rsSelectByMaladie.next())
			{
				qst.add(rsSelectByMaladie.getString("question"));
				rep.add(rsSelectByMaladie.getString("reponseAsense"));
				idqs.add(rsSelectByMaladie.getInt("idq"));
				idms.add(rsSelectByMaladie.getInt("idm"));
				coefs.add(rsSelectByMaladie.getInt("coef"));
			}
			rsSelectByMaladie.close();
	//Maintenant on cherche les maladies (autre que celle decouverte) qui pertinent a chaque question
			
			for(int i=0; i<qst.size(); i++)
			{
				ArrayList <Maladie> maladies = new ArrayList<Maladie>();
				ResultSet rsSelectByQuestion = bd.state.executeQuery("select maladieAssociee from phase2avecmaladie where question='" + qst.get(i).toString() + "'" );
				while(rsSelectByQuestion.next())
				{
					if(!listenommaladie.contains(rsSelectByQuestion.getString("maladieAssociee")))
						listenommaladie.add(rsSelectByQuestion.getString("maladieAssociee"));
				}
				rsSelectByQuestion.close();
				for(int j=0; j<listenommaladie.size(); j++)
				{
					ResultSet rs = bd.state.executeQuery("select * from maladie where nommaladie='" + listenommaladie.get(j).toString() + "';" );
					rs.next(); //Il ne doit y avoir qu'une et une seule ligne donc pas besoin de while
					maladies.add(new Maladie(rs.getString("nommaladie"), rs.getInt("scoreacc"), rs.getInt("scoreref"), rs.getString("descMaladie")));
					rs.close();
				}
				phase2.add(new Question(qst.get(i).toString(), rep.get(i).toString(), maladies, Integer.parseInt(idqs.get(i).toString()),Integer.parseInt(idms.get(i).toString()), Integer.parseInt(coefs.get(i).toString())));	
			}
			System.out.println(phase2);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void setDebutPhase2(int a)
	{
		try
		{
		String requeteSelect="select * from debutphase2 where idq=" + a + ";";
		ResultSet rs = bd.state.executeQuery(requeteSelect);
		
		rs.next(); //Il n'y qu'une question ayant l'id demandé
		String maladieAssociee = rs.getString("maladieAssocie");
		String question = rs.getString("questiondebutphase2");
		String rep = rs.getString("reponseAsense");
		int idq = rs.getInt("idq");
		rs.close();
		
			requeteSelect = "select * from maladie where nommaladie='" + maladieAssociee + "';";
			rs=bd.state.executeQuery(requeteSelect); //Normalement il n'y a qu'une seule ligne
			rs.next();
			Maladie maladie = new Maladie(rs.getString("nommaladie"), rs.getInt("scoreacc"), rs.getInt("scoreref"), rs.getString("descMaladie"));
			System.out.println("Debut Phase 2: Maladie: " + rs.getString("nommaladie") + "ScoreAcc: " + rs.getInt("scoreacc") + "ScoreRef: "+ rs.getInt("scoreref"));
			rs.close();
		
		debutPhase2 = new Question(question, rep, maladie, idq);
		
		System.out.println(debutPhase2);
		
	} catch (SQLException e) {
		e.printStackTrace();
	}
	}
	
	public void setAutreQuestion(int a)
	{
		String requeteSelect = "select replique from autrequestions where id=" + a + ";";
		try {
			ResultSet rs = bd.state.executeQuery(requeteSelect);
			rs.next(); //Il n'y a qu'une seule ligne normalement
			this.autreQuestion = rs.getString("replique");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void setMessageBienvenue(int a)
	{
		
		try {
			
			String requeteSelect = "select replique from repliquebienvenue where id=" + a + ";";
			ResultSet rs;
			rs = bd.state.executeQuery(requeteSelect);
			rs.next();//Il n'y a qu'une seule ligne
			messagesBienvenue = rs.getString("replique");

		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	public void setMessagePhase1(int a)
	{
		try {
		String requeteSelect="select questionPhase1 from phase1 where id=" + a + ";";
		ResultSet rs = bd.state.executeQuery(requeteSelect);
		rs.next();
		phase1 = rs.getString("questionPhase1");
		}catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	
	
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	String contenuMessage;
	String sentiment;
	
	public String enleverLesChosesInutiles(String txt) //Pour supprimer les . , prepositions etc
	{
		String txtOut=txt;
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation document = new Annotation(txtOut);

        pipeline.annotate(document);

        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);

        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                if(!pos.contains("NN") && !pos.equals("JJ") && !word.equals("not"))
                {
                	System.out.println(String.format("Print: word: [%s] pos: [%s] ", word, pos));
                    txtOut=txtOut.replace(word, ""); //On remplace ce qu'on veut enlever par du vide
                }
                
            }
        }
        
        return txtOut;
	}
	public void sentimentTest(String msg) throws IOException {
        this.contenuMessage = msg;
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation annotation = pipeline.process(contenuMessage);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            this.sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentiment + "\t" + sentence);
        }
    }
	
	public ArrayList<Maladie> detecterMaladie(String msg) throws SQLException
	{
		ArrayList<Maladie> maladiesAss = new ArrayList<Maladie>(); //Maladie associées au message donnée par le patient
		String symptomes= enleverLesChosesInutiles(msg); //On supprime les occurences inutiles
		Connexion BDD = new Connexion();
		//Pour l'instant je pars du principe qu'il n'y aura qu'un symptome dans un message
		String req="select maladie from SymptomesEtMaladies where symptome=" + symptomes + ";";
		ResultSet rs = BDD.state.executeQuery(req);
		while(rs.next())
		{
			Maladie maladie = new Maladie((String) (rs.getObject(1)));
			maladiesAss.add(maladie);
			System.out.println("Nom de la maladie: " + maladie.nom);
		}
		
		
		
		
		return maladiesAss;
	}
	
	public static void main(String[] args) throws IOException, SQLException {
		String msgUser;
		String allMsgs;
		String msgbot="I don't understand, I'm sorry.";
		
		ArrayList scores; //Le score obtenu au près de chaque maladie
		
		System.out.println("Bot >> Hi, my name is Botty."
				+ " what's your name?");
		System.out.print("User>> ");
		Scanner sc = new Scanner(System.in);
		msgUser = sc.nextLine();
		allMsgs = msgUser;
		while (msgUser.length()<1 || msgUser == null)
		{
			System.out.println("Bot >> Don't worry, just give me a pseudo if your not at ease.");
			System.out.print("User>> ");
			msgUser = sc.nextLine();
		}

		allMsgs = msgUser;
			
		System.out.println("Bot >> Hi " + msgUser + "\n what brings you here?");
		System.out.print("User>> ");
		msgUser = sc.nextLine();
		allMsgs += msgUser;
		System.out.println("Bot >> So would you say that you are here because you lost something? You are scared?"
				+ " Or you feel threatend?");
		System.out.print("User>> ");
		msgUser = sc.nextLine();
		allMsgs += msgUser;
		if(msgUser.contains("scared"));
			System.out.println("You are scared !");
		
		
		
		
		
		/*
			System.out.println("Bot>> I believe you're here because you think you have mental issues, "
					+ "but the truth is that sometimes it can be physical too. \n	"
					+ "So we're going to start with the easier. \n"
					+ "Do you feel pain in a particular location? Like your back, neck, abdomen? Headaches maybe?");
			System.out.print("User>> ");
			Scanner sc = new Scanner(System.in);
			msgUser = sc.nextLine();
			message msg = new message();
			msg.sentimentTest(msgUser);
			if(msg.sentiment.equals("Negative"))
			{
				System.out.println("Bot>> Oh dear! I hope you'll be ok by the end of this conversation ! \n"
						+ " The pain is normal in case of \"Huge stress\" due to your work, maybe? ");
				ArrayList<Maladie> maladie = msg.detecterMaladie(msg.contenuMessage);
				
			}
			if(msg.sentiment.equals("Positive"))
			{
				System.out.println("Bot>> I'm glad it has nothing to do with physical !");
			}
			*/
			
