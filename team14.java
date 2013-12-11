//CS1555 Term Project


import java.io.*;
import oracle.sql.*;
import java.sql.*;
import java.text.*;
import java.util.*;

/*TODO:
Admin:
1.Works
2.Works but does insert double
3.Works but does insert double
4.Works but does insert double
5.Works
Customer:
1.Works
2.Works
3.Works
4.
5.
6.
7.Works
8.Works
*/

public class team14 
{
	private Connection connection;
	private Scanner sc;
	
	private static final String lineBreak = "\n --------------------------------------------------------------- \n";

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
			sc = new Scanner(System.in);
			
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
				System.out.println(choice);
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
				
				String street = getInput("Street Name?");
				String city = getInput("City Name?");
				String state = getInput("State Name?");
				String email = getInput("Email Address?");
				String cardNum = getInput("Credit Card Number?");
				String cardExp = getInput("Credit Card Expiration Date (MM/dd/yyyy)?");
				String phoneNum = getInput("Phone number (xxxxxxxxxx)?");
				java.sql.Date date = strToDate(cardExp);
				
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
					cs.setString(9, phoneNum);
					cs.setString(10, email);
					
					cs.execute();
				}        
				
		}catch(Exception e) {System.out.println(e);}
	}     
	
		
	public void showCustomer()
	{
		String fn = getInput("First Name?");
		String ln = getInput("Last Name?");
		ResultSet rs = query("SELECT * FROM Customer WHERE '" + fn + "' = first_name AND '" + ln + "' = last_name");
		printRS(rs);
		/* Query
		
		SELECT * FROM Customer WHERE nfirst_name = first_name and nlast_name = last_name
		
		*/ 
	}
		
	public void findPrice()
	{
		String a = getInput("From city?");
		String b = getInput("To city?");
		
		ResultSet rs1 = query("SELECT high_price, low_price FROM PRICE WHERE '" + a + "' = departure_city and '" + b + "' = arrival_city");
		ResultSet rs2 = query("SELECT high_price, low_price FROM PRICE WHERE '" + b + "' = departure_city and '" + a + "' = arrival_city");
		
		if(rs1 != null)
			printRS(rs1);
			
		else if(rs2 != null)
			printRS(rs2);
		
		else
			System.out.println("No Flights Found");
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
		int flightNum;
		int legs = 0;
		String date;
		while((flightNum = getNumericInput("Flight number of leg? 0 if all legs given.")) != 0)
		{
			if(legs == 4)
				break;

			date = getInput("Date of the flight? (MM/dd/yyyy)");
			legs++;
			
			ResultSet rs = query("SELECT Count(DISTINCT cid) FROM Reservation r JOIN Reservation_detail d ON r.reservation_number = d.reservation_number WHERE d.flight_date = '" + strToDate(date) + "' and d.flight_number = '" + flightNum + "'");
			printRS(rs);
			
		}
		if(legs == 0)
			System.out.println("No flights selected");
	}
		
	public void showReservation()
	{
		String reserveNum = getInput("Which reservation number?");
		ResultSet rs = query("SELECT * FROM Flight f JOIN (SELECT Flight_number FROM   Reservation_detail WHERE '" + reserveNum + "' = reservation_number) t ON f.Flight_number = t.Flight_number");
		if(rs != null)
			printRS(rs);
		else
			System.out.println("No reservations found with that number");
	}
		
	public void buyTicket()
	{
		String num = getInput("Buy which reservation?");
		query("UPDATE Reservation SET ticketed = 'Y' WHERE reservation_number = '" + num + "'");
		/*
		(UPDATE Reservation SET ticketed = 'Y' WHERE reservation_number = '" + num + "')
		*/
	}
		
	public void eraseDatabase()
	{

		String ans = getInput("Are you sure you want to erase the database? Enter \"Yes\" to confirm.");
		if(ans.equals("Yes"))
		{
			query("DELETE Flight");
			query("DELETE Plane");
			query("DELETE Price");
			query("DELETE Customer");
			query("DELETE Reservation");
			query("DELETE Reservation_Detail");
			query("DELETE Current_d");
		}

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
				
				CallableStatement cs = connection.prepareCall("call insertSchedule(?, ?, ?, ?, ?, ?, ?)");
				
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
				
				CallableStatement cs = connection.prepareCall("call insertPricing(?, ?, ?, ?)");
				
				
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
				CallableStatement cs = connection.prepareCall("call insertPlane(?, ?, ?, ?, ?)");

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
		String flightDate = getInput("What date? (MM/dd/yyy)");
		printRS(query("SELECT c.Salutation, c.first_name, c.last_name FROM Customer c JOIN (SELECT cid FROM Reservation r JOIN Reservation_detail rd ON r.Reservation_Number = rd.Reservation_number WHERE rd.flight_date = to_date('" + flightDate + "','mm/dd/yyyy') and rd.flight_number = '" + flightNum + "') t ON c.cid = t.cid"));
		
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
				System.out.println(input);
				if (input.isEmpty()) 
					return 0;
				else 
					return Integer.parseInt(input);
			} catch(Exception e){System.out.println(e); continue;}	
		}
		
	}
	
	public String getInput(String prompt) 
	{
		boolean lengthCheck;
		String str;
		do 
		{
			System.out.print(prompt + ": ");
			str = sc.nextLine().trim();                     
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
	

	public ResultSet query(String query) 
	{
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
			if(isDateValid(date))
			{
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");;
				java.util.Date parsed;	
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
	
	public void printRS(ResultSet rs)
	{
		try
		{
			try
			{
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnsNumber = rsmd.getColumnCount();
				while(rs.next()) 
				{
					for(int i = 1; i <= columnsNumber; i++)
						System.out.print(rsmd.getColumnName(i) + "  ");
					
					
					System.out.print(lineBreak);
					
					for(int i = 1; i <= columnsNumber; i++) 
					{
						if(i > 1)
							System.out.print(",  ");
						
						String columnValue = rs.getString(i);
						System.out.print(columnValue + " ");
					}
					
					System.out.println("");
				}
			}catch(NullPointerException e){System.out.println("No Passengers on that flight");}
		}
		catch(Exception e){System.out.println(e);}
	}

	private static boolean isDateValid(String date) 
	{
        try 
		{
            DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            format.parse(date);
            return true;
        } catch (ParseException e){return false;}
	}
}