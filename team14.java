//CS1555 Term Project


import java.io.*;
import oracle.sql.*;
import java.sql.*;
import java.text.*;
import java.util.*;

public class team14 {
	private Connection connection;
	private String username, password;
	private Scanner input;
	
	private static final String lineBreak = "\n ----------------------------------- \n";

	public static void main(String args[]) 
	{
		team14 doesNotMatterAtAll = new team14();
	}
	
	public team14() 
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
		ArrayList<String> choices = new ArrayList<String>();
		int choice = 0;
		switch(menu) 
		{
			default:
				choices.add("Administrator");
				choices.add("User");
				choices.add("Exit");
				choice = getChoice("Main menu", choices);
				break;
				
			case 1:
				choices.add("Add customer");
				choices.add("Show customer info, given customer name");
				choices.add("Find price for flights between two cities");
				choices.add("Find all routes between two cities");
				choices.add("Find all routes with available seats between two cities on given day");
				choices.add("Add reservation");
				choices.add("Show reservation info, given reservation number");
				choices.add("Buy ticket from existing reservation");
				choice = getChoice("User menu", choices);
				break;
				
			case 2:
				choices.add("Erase the database");
				choices.add("Load schedule information");
				choices.add("Load pricing information");
				choices.add("Load plane information");
				choices.add("Generate passenger manifest for specific fight on given day");
				choices.add("Generate passenger manifest for specific fight on given day");
				choice = getChoice("Administrator menu", choices);
				break;
		}
		
		System.out.println("\n\n" + choices.get(choice - 1));
		
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
					loadPriceInfo(getInput("What is the file name"));
					promptMenu(2);
					break;
				case 4:
					loadPlaneInfo(getInput("What is the file name"));
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
		try 
		{

			CallableStatement cs = connection.prepareCall("begin add_Customer(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); end;");
			
			String sal = getInput("What do you go by (Mr. , Mrs. etc)?");
			String first = getInput("First Name?");
			String last = getInput("Last Name?");
			
			//NEED TO TEST TO SEE IF USER ALREADY EXSISTS
			
			String street = getInput("Street Name?");
			String city = getInput("City Name?");
			String state = getInput("State Name?");
			String email = getInput("Email Address?");
			String cardNum = getInput("Credit Card Number?");
			String cardExp = getInput("Credit Card Expiration Date (yyyy-MM-dd)?");
			
			if(!isDateValid(cardExp))
				System.out.println("Card exp date is invalid format. Needs to be yyyy-MM-dd. Add Aborted");
			
			else
			{
				cs.setString(1, sal);
				cs.setString(2, first);
				cs.setString(3, last);
				cs.setString(4, cardNum);
				
				cs.setDate(5, java.sql.Date.valueOf(cardExp));
				cs.setString(6, street);
				cs.setString(7, city);
				cs.setString(8, state);
				cs.setString(9, email);
				
				cs.execute();
			}	
			
		}catch(Exception e) {System.out.println(e);}
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
				
				CallableStatement cs = connection.prepareCall("begin insertSchedule(?, ?, ?, ?, ?, ?, ?); end;");
				
				for(int i = 0; i < 7; i++)
					cs.setString(i+1, split[i]);
	
				cs.execute();
			}
		}catch(Exception e) {System.out.println(e);}
	}
		
	public void loadPriceInfo(String fn)
	{
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(fn));
			String line;
			String[] split;
			while((line = br.readLine()) != null)
			{
				split = line.split("\\s+");
				CallableStatement cs = connection.prepareCall("begin insertPricings(?, ?, ?, ?); end;");
				
				
				cs.setString(1, split[0]);
				cs.setString(2, split[1]);
				cs.setInt(3, Integer.parseInt(split[2]));
				cs.setInt(4, Integer.parseInt(split[3]));
	
				cs.execute();
			}
		}catch(Exception e) {System.out.println(e);}
	}
		
	public void loadPlaneInfo(String fn)
	{
		try 
		{
			BufferedReader br = new BufferedReader(new FileReader(fn));
			String line;
			String[] split;
			while((line = br.readLine()) != null)
			{
				split = line.split("\\s+");
				CallableStatement cs = connection.prepareCall("begin insertPlane(?, ?, ?, ?, ?); end;");
				
				
				cs.setString(1, split[0]);
				cs.setString(2, split[1]);
				cs.setInt(3, Integer.parseInt(split[2]));
				cs.setDate(4, java.sql.Date.valueOf(split[3]));
				cs.setInt(4, Integer.parseInt(split[3]));
	
				cs.execute();
			}
		}catch(Exception e) {System.out.println(e);}
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


	public int getChoice(String title, ArrayList<String> choices) 
	{
		System.out.println("\n" + title + "\n" + lineBreak);
		
		for (int i = 1; i <= choices.size(); i++) 
			System.out.println("  " + i + ") " + choices.get(i - 1));
		
		System.out.println(lineBreak);
		
		int choice;
		do 
		{
			choice = getNumericInput("Choose an Item");
		} while (choice <= 0 || choice > choices.size());
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