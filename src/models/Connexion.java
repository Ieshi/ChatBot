package Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Connexion
{
	 Connection con=null;
	 public Statement state;
	
	public Connexion()
	{
	try { 	
		  Class.forName("com.mysql.jdbc.Driver");
	      con = DriverManager.getConnection("jdbc:mysql://localhost:3306/ensadb", "root", "");  
	      state = con.createStatement();
	       
	    } catch (Exception e) 
		{
	      System.out.println(e.getMessage());
	    	}    
	}
}
