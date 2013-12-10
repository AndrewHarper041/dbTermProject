//CS1555 Term Project


import java.io.*;
import oracle.sql.*;
import java.sql.*;
import java.text.*;
import java.util.*;

/*TODO:
Admin:
1. SQL java.sql.SQLSyntaxErrorException: ORA-00933: SQL command not properly ended, confimation works
2.Works but does insert double
3.Works but does insert double
4.Works but does insert double
5.Illegal relational operator.
Customer:
1.Missing in or out parameters on SQL
2.Missing expression
3.
4.
5.
6.
7.
8.
*/

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
			insert into customer values('123456789', 'MR', 'Josh', 'Frey', '4444444444444444', to_date('08/31/1991','mm/dd/yyyy'), 'ward', 'pittsburgh', 'PA', '7174494601', 'jtf15@pitt.edu');

			
			String street = getInput("Street Name?");
			String city = getInput("City Name?");
			String state = getInput("State Name?");
			String email = getInput("Email Address?");
			String cardNum = getInput("Credit Card Number?");
			String cardExp = getInput("Credit Card Expiration Date (MM/dd/yyyy)?");
			
			java.sql.Date date = strToDate("cardExp");
			
			if(date != null);
			{
				cs.setString(1, sal);
				cs.setString(2, first);
				cs.setString(3, last);
				cs.setString(4, cardNum);
				
				cs.setDate(5, date);
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
		String fn = getInput("First Name?");
		String ln = getInput("Last Name?");
		query("SELECT * FROM Customer WHERE first_name = '" + fn + "' AND WHERE last_name = \'" + ln + "';");
		/* Query
		
		SELECT * FROM Customer WHERE nfirst_name = first_name and nlast_name = last_name
		
		*/ 
	}
		
	public void findPrice()
	{
		String a = getInput("From city?");
		String b = getInput("To city?");
		
		ResultSet rs1 = query("SELECT high_price, low_price FROM PRICE WHERE " + a + " = departure_city and " + b + " = arrival_city");
		ResultSet rs2 = query("SELECT high_price, low_price FROM PRICE WHERE " + b + " = departure_city and " + a + " = arrival_city");
		//ResultSet rs3 = query("SELECT high_price, low_price FROM PRICE WHERE " + a + " = departure_city and " + b + " = arrival_city");
		/* Query
		
			SELECT high_price, low_price FROM PRICE WHERE ndeparture_city = departure_city and narrival_city = arrival_city
			
			SELECT high_price, low_price FROM PRICE WHERE narrival_city = departure_city and ndeparture_city = arrival_city
		
			SELECT high_price, low_price FROM PRICE WHERE ndeparture_city = departure_city and narrival_city = arrival_city
		*/
	}
		
	public void findRoutes()
	{
		String a = getInput("From city?");
		String b = getInput("To city?");
		
	}
		
	public void findAvailableRoutes()
	{
		String a = getInput("From city?");
		String b = getInput("To city?");
		String c = getInput("On what data (yyyy-mm-dd)?");
		
	}
		
	public void addReservation()
	{
		
	}
		
	public void showReservation()
	{
		String reserveNum = getInput("Which reservation?");
		query("SELECT * FROM Flight f JOIN (SELECT Flight_number FROM   Reservation_detail WHERE " + reserveNum + " = reservation_number) t ON f.Flight_number = t.Flight_number");

	}
		
	public void buyTicket()
	{
		
	}
		
	public void eraseDatabase()
	{
		try
		{
			String ans = getInput("Are you sure you want to erase the database? Enter \"Yes\"to confirm.");
			String db = connection.getMetaData().getURL();
			if(ans.equals("Yes"))
				query("DELETE * FROM Flight; " + 
						"DELETE * FROM Plane; "+
						"DELETE * FROM Price; "+
						"DELETE * FROM Customer; "+
						"DELETE * FROM Resrvation; "+
						"DELETE * FROM Reservation_Detail; " +
						"DELETE * FROM c_date ; ");

		//String confirmation = getInput("Are you sure you want to delete the database? (Y/N)");
		//if(confirmation == 'Y'){
			/*Queries
			DELETE * FROM Flight;
			DELETE * FROM Plane;
			DELETE * FROM Price;
			DELETE * FROM Customer;
			DELETE * FROM Resrvation;
			DELETE * FROM Reservation_Detail;
			DELETE * FROM c_date ;
			
			
			
			*/
		
		//}
		
		} catch (SQLException e) {handleSQLException(e);}
		
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
				
				CallableStatement cs = connection.prepareCall("call insertSchedule(?, ?, ?, ?, ?, ?, ?); end;");
				
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
				
				CallableStatement cs = connection.prepareCall("call insertPricing(?, ?, ?, ?); end;");
				
				
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
			
			java.sql.Date d;
			
			while((line = br.readLine()) != null)
			{
				split = line.split("\\s+");
				CallableStatement cs = connection.prepareCall("call insertPlane(?, ?, ?, ?, ?); end;");

				d = strToDate(split[3]);
				
				cs.setString(1, split[0]);
				cs.setString(2, split[1]);
				cs.setInt(3, Integer.parseInt(split[2]));
				cs.setDate(4, d);
				cs.setInt(5, Integer.parseInt(split[4]));
	
				cs.execute();
			}
		}catch(Exception e) {System.out.println(e);}
	}
		
	public void generateManifest()
	{
		String flightNum = getInput("What flight number?");
		String flightDate = getInput("What date?");
		//Join with customer on cid and print where date and fn match
		
		//THROWS ERROR java.sql.SQLSyntaxErrorException invalid relation operator
		System.out.println("SELECT Salutation, first_name, last_name FROM Customer c JOIN (SELECT cid FROM Reservation r JOIN Reservation_detail rd ON r.Reservation_Number = rd.Reservation_number WHERE rd." + flightDate + " = ndate and rd." + flightNum + " = nflight_number) t ON c.cid = t.cid;");
		query("SELECT Salutation, first_name, last_name FROM Customer c JOIN (SELECT cid FROM Reservation r JOIN Reservation_detail rd ON r.Reservation_Number = rd.Reservation_number WHERE rd." + flightDate + " = ndate and rd." + flightNum + " = nflight_number) t ON c.cid = t.cid;");
		
		/*
			The Query:
			
			SELECT Salutation, first_name, last_name
				  FROM Customer c JOIN (SELECT cid
										FROM Reservation r JOIN Reservation_detail rd ON r.Reservation_Number = rd.Reservation_number
										WHERE rd.flight_date =ndate and rd.flight_number = nflight_number) t ON c.cid = t.cid;
		*/
		
		
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
	
	public boolean hasValue(String val, String field, String table)
	{
		try
		{
			ResultSet rs = query("SELECT COUNT(*) FROM " + table + " WHERE " + field + " = " + val);
			rs.next();
			if(rs.getInt(1) > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {handleSQLException(e);}
		return false;
	}
	
	public java.sql.Date strToDate(String date)
	{
		try
		{
		
			if(isDateValid(date, "MM/dd/yyyy"))
			{
				SimpleDateFormat format;
				java.util.Date parsed;	
				format = new SimpleDateFormat("MM/dd/yyyy");
				parsed = format.parse(date);
				
				return new java.sql.Date(parsed.getTime());
			}
			
			else
			{
				System.out.println("Invalid date, format must be MM/dd/yyyy.");
				return null;
			}
		}catch(ParseException e){System.out.println(e); return null;}
				
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

}