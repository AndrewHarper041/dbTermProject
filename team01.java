//CS1555 Term Project
//Nicholas Amoscato -- naa46@pitt.edu
//Ryan Sandhaus -- rjs90@pitt.edu

import java.io.*;
import oracle.sql.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class team01 {
	private Connection connection;
	private String username, password;
	private Scanner input;
	
	private static final String lineBreak = "\n ----------------------------------- \n";

	public static void main(String args[]) 
	{
		team01 doesNotMatterAtAll = new team01();
	}
	
	public team01() 
	{
		//Ill fix it later Zain
		String username = "adh41";
		String password = "3666155";
		
		try 
		{
			DriverManager.registerDriver (new oracle.jdbc.driver.OracleDriver());
			connection = DriverManager.getConnection("jdbc:oracle:thin:@db10.cs.pitt.edu:1521:dbclass", username, password);
			input = new Scanner(System.in);
			
			System.out.println("");
			promptMenu(0);
		} catch(SQLException e){System.err.println("Didn't connect: " + e.toString());}
	}


	public void promptMenu(int menu) 
	{
		String[] choices = new String[10];
		int choice = 0;
		switch(menu) 
		{
			default:
				choices[0] = "Administrator";
				choices[1] = "User";
				choices[2] = "Exit";
				choice = getChoice("Main menu", choices);
				break;
				
			case 1:
				choices[0] = "Add customer";
				choices[1] = "Show customer info, given customer name";
				choices[2] = "Find price for flights between two cities";
				choices[3] = "Find all routes between two cities";
				choices[4] = "Find all routes with available seats between two cities on given day";
				choices[5] = "Add reservation";
				choices[6] = "Show reservation info, given reservation number";
				choices[7] = "Buy ticket from existing reservation";
				choice = getChoice("User menu", choices);
				break;
				
			case 2:
				choices[0] = "Erase the database";
				choices[1] = "Load schedule information";
				choices[2] = "Load pricing information";
				choices[3] = "Load plane information";
				choices[4] = "Generate passenger manifest for specific fight on given day";
				choice = getChoice("Administrator menu", choices);
				break;
		}
		
		System.out.println("\n\n" + choices[choice - 1]);
		
		if(menu == 0) 
		{
			switch(choice) 
			{
				case 1:
					promptMenu(2);
					break;
					
				case 2:	
					promptMenu(1);
					break;
			}
		}
		
		//USER SWITCH
		else if (menu == 1) 
		{
			switch(choice) 
			{
				case 1:
				addCustomer();
				promptMenu(1);
					break;
				case 2:
					showCustomer();
					promptMenu(1);
					break;
				case 3:
					findPrice();
					promptMenu(1);
					break;
				case 4:
					findRoutes();
					promptMenu(1);
					break;
				case 5:
					findAvailableRoutes();
					promptMenu(1);
					break;
				case 6:
					addReservation();
					promptMenu(1);
					break;
				case 7:
					showReservation();
					promptMenu(1);
					break;
				case 8:
					buyTicket();
					promptMenu(1);
					break;
					
				default:
					System.out.println("Not a valid coice");
					promptMenu(1);
					break;
			}
		} 
		
		//ADMIN SWITCH
		else if (menu == 2) 
		{
			switch(choice) 
			{
				case 1:
					eraseDatabase();
					promptMenu(2);
					break;
				case 2:
					loadScheduleInfo(getInput("What is the file name"));
					promptMenu(2);
					break;
				case 3:
					loadPriceInfo();
					promptMenu(2);
					break;
				case 4:
					loadPlaneInfo();
					promptMenu(2);
					break;				
				case 5:
					generateManifest();
					promptMenu(2);
					break;
				default:
					System.out.println("Not a valid coice");
					promptMenu(2);
					break;
			}
		} 

	}
	
	public void addCustomer()
	{
	
	}
		
	public void showCustomer()
	{
	
	}
		
	public void findPrice()
	{
	
	}
		
	public void findRoutes()
	{
	
	}
		
	public void findAvailableRoutes()
	{
	
	}
		
	public void addReservation()
	{
	
	}
		
	public void showReservation()
	{
	
	}
		
	public void buyTicket()
	{
	
	}
		
	public void eraseDatabase()
	{
	
	}
		
	public void loadScheduleInfo(String fn)
	{
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(fn));
			String line;
			String[] split;
			while((line = br.readLine()) != null)
			{
				split = line.split("\\s+");
				CallableStatement cs = connection.prepareCall("begin put_product(?, ?, ?, ?, ?, ?, ?); end;");
				
				for(int i = 0; i < 7; i++)
					cs.setString(i+1, split[i]);
	
				cs.execute();
			}
		}catch(Exception e) {System.out.println(e);}
	}
		
	public void loadPriceInfo()
	{
	
	}
		
	public void loadPlaneInfo()
	{
	
	}
		
	public void generateManifest()
	{
	
	}	
	
	public int getNumericInput(String prompt) 
	{
		while (true) 
		{
			try 
			{
				String input = getInput(prompt);
				if (input.isEmpty()) 
					return 0;
				
				else 
					return Integer.parseInt(input);
				
			} catch (NumberFormatException e) {continue;}	
		}
	}
	
	public String getInput(String prompt) 
	{
		boolean lengthCheck;
		String str;
		do 
		{
			System.out.print(prompt + ": ");
			str = input.nextLine().trim();                     
		} while(str.isEmpty());
		return str;
	}


	public int getChoice(String title, String[] choices) 
	{
		System.out.println("\n" + title + "\n" + lineBreak);
		
		for (int i = 1; i <= choices.length; i++) 
			System.out.println("  " + i + ") " + choices[i - 1]);
		
		System.out.println(lineBreak);
		
		int choice;
		do 
		{
			choice = getNumericInput("Choose an Item");
		} while (choice <= 0 || choice > choices.length);
		return choice;
	}

	public void handleSQLException(Exception e) 
	{
		System.err.println("Error running database query: " + e.toString());
		e.printStackTrace();
		System.exit(1);
	}
	

	public ResultSet query(String query) {
		try 
		{
			Statement s = connection.createStatement();
			ResultSet result = s.executeQuery(query);
			if (result.isBeforeFirst()) 
				return result;
			else 
				return null;
		} catch (SQLException e) {handleSQLException(e);return null;}
	}
	
	public PreparedStatement getPreparedQuery(String query) 
	{
		try 
		{
			return connection.prepareStatement(query);
		} catch (SQLException e) {handleSQLException(e);return null;}
	}

	public ResultSet query(PreparedStatement ps, List<String> parameters) 
	{
		try 
		{
			for (int i = 1; i <= parameters.size(); i++) 
				ps.setString(i, parameters.get(i - 1));
				
			ResultSet result = ps.executeQuery();
			
			if (result.isBeforeFirst()) 
				return result;
			else 
				return null;
		} catch (SQLException e) {handleSQLException(e);return null;}
	}
	

	public ResultSet query(PreparedStatement ps, String str) 
	{
		return query(ps, Arrays.asList(str));
	}
	

	public int queryUpdate(PreparedStatement ps, List<String> parameters) 
	{
		try 
		{
			for (int i = 1; i <= parameters.size(); i++) 
				ps.setString(i, parameters.get(i - 1));
				
			return ps.executeUpdate();
		} 
		catch (SQLException e) {handleSQLException(e);return -1;}
	}
	
	public int queryUpdate(PreparedStatement ps, String str) 
	{
		return queryUpdate(ps, Arrays.asList(str));
	}

	

	private static boolean isDateValid(String date, String format) 
	{
        try 
		{
            DateFormat df = new SimpleDateFormat(format);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {return false;}
	}
	

	private static boolean isDateValid(String date) 
	{
		return isDateValid(date, "dd-MM-yyyy/hh:mm:ssa");
	}
	

	public void updateDate() 
	{
		String date;
		boolean exit = false ;
		do 
		{
			date = getInput("Please enter a date or 'exit' to exit (must match dd-mm-yyyy/hh:mi:ssam)").toUpperCase();
			if(date.equals("EXIT")) 
			{
				exit = true ;
				break ;
			}
		} while(!isDateValid(date));
		
		if(!exit) {
			queryUpdate(getPreparedQuery("update sys_time set my_time = to_date(?, 'dd-mm-yyyy/hh:mi:ssam')"), date);
			System.out.println("\nDate successfully changed!\n") ;
		}
	}


}