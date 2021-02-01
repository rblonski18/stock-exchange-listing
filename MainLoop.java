package blonski_201L_Assignment1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;  


public class MainLoop {
	
	public static void main(String []args) {
		
		// just my preference to have the main function as simple as possible. 
		Scanner input = new Scanner(System.in);
		Data companyList = readFile(input);
		loop(input, companyList);

	}
	
	// function that runs upon user selecting number 1 from the menu. 
	public static void one(Data companies) {
		
		// this function just displays all companies in the data set. 
		
		int numCompanies = companies.data.length;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		

		for(int i = 0; i < numCompanies; i++) {
			String date = df.format(companies.data[i].getDate());
			System.out.println(companies.data[i].getName() + 
					", symbol " + companies.data[i].getTicker() + 
					", started on " + date + 
					", listed on " + companies.data[i].getCode() + 
					",\n\t" + companies.data[i].getDescription() + "\n");
		}
	}
	
	// function that runs upon user selecting two from the menu. 
	public static void two(Scanner input, Data companies, int numCompanies) {
		
		// this function searches for companies in the data set by ticker. 
		
		System.out.println("What is the ticker of the company you would like to search for? ");
		
		String searchTicker = input.next();
		String tickerLowercase = searchTicker;
		tickerLowercase.toLowerCase();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		boolean found = false;
		// linear search because we don't know if the array is sorted. 
		// I could make a flag at the top of the loop function to say if it is sorted, 
		// and if so do a binary search, but I want to make sure I get the whole 
		// program working first 
		for(int i = 0; i < numCompanies; i++) {
			
			String tickerHolder = companies.data[i].getTicker();
			tickerHolder.toLowerCase();
			
			if(tickerLowercase.equals(tickerHolder)) {
				
				String date = df.format(companies.data[i].getDate());

				System.out.println(companies.data[i].getName() + 
						", symbol " + companies.data[i].getTicker() + 
						", started on " + date + 
						", listed on " + companies.data[i].getCode());
				found = true;
			}
		} 
		
		// if the person enters an invalid company, it should loop through again, per assignment sheet. 
		if(!found) {
			System.out.println(searchTicker + " could not be found. ");
			two(input, companies, numCompanies);
		}
	}
	
	// function that runs upon user selecting three from the menu
	public static void three(Scanner input, Data companies, int numCompanies) {
		
		// this function lets the user search for all stocks on an exchange. 
		
		System.out.println("What stock exchange would you like to search for? ");
		
		String exchangeName = input.next();
		String exchangeNameHolder = exchangeName;
		exchangeNameHolder.toLowerCase();
		boolean exchangeFound = false;
		
		ArrayList<String> companiesOnExchange = new ArrayList<String>();
		
		for(int i = 0; i < numCompanies; i++) {
			
			String exchangeHolder = companies.data[i].getCode();
			exchangeHolder.toLowerCase();
			
			if(exchangeNameHolder.equals(exchangeHolder)) {
				
				companiesOnExchange.add(companies.data[i].getTicker());
				exchangeFound = true;
				
			}
		}
		if(!exchangeFound) {
			
			System.out.println("No exchange named " + exchangeName + " found. ");
			three(input, companies, numCompanies);
			
		} else {
			
			// separated for grammar formatting - unnecessary but console output looks cleaner. 
			if(companiesOnExchange.size() == 1) {
				
				System.out.println(companiesOnExchange.get(0) + " found on the " + exchangeName + " exchange. ");
				loop(input, companies);
				
			} else if(companiesOnExchange.size() == 2) {
				
				System.out.println(companiesOnExchange.get(0) + " and " + 
						companiesOnExchange.get(1) + " found on the " +
						exchangeName + " exchange. ");
				loop(input, companies);
				
			} else {
				
				for(int i = 0; i < companiesOnExchange.size()-1; i++) {
					System.out.print(companiesOnExchange.get(i) + ", ");
				}
				
				System.out.print("and " + companiesOnExchange.get(companiesOnExchange.size()-1) + 
						" found on the " + exchangeName + " exchange. \n");
				
				loop(input, companies);
			}
		}
	}
	
	// this function runs upon user selecting four from the menu. 
	public static Data four(Scanner input, Data companies) {
		
		// this function allows the user to add a company that isn't already in the data set. 
		
		input.nextLine(); // grabs the "enter" from last input, so the line two below this one doesn't skip
		System.out.println("What is the name of the company you would like to add? ");
		String name = input.nextLine();
		
		// search to see if this name is anywhere in our list of companies
		for(int i = 0; i < companies.data.length; i++) {
			String compName = companies.data[i].getName();
			if(compName.equals(name)) {
				System.out.println("There is already an entry for " + name);
				return four(input, companies);
			}
		}
		
		// search for duplicate ticker
		System.out.println("What is the stock symbol of " + name + "?");
		String symbol = input.next();
		for(int i = 0; i < companies.data.length; i++) {
			String compTick = companies.data[i].getTicker();
			if(compTick.equals(symbol)) {
				System.out.println("There is already a ticker for " + symbol);
				return four(input, companies);
			}
		}
		
		// companies can have same start dates. 
		System.out.println("What is the start date of " + name + "?");
		String start = input.next();
		
		System.out.println("What exchange is " + name + " listed on? ");
		String exchange = input.next();
		
		// assignment sheet declares only NYSE and NASDAQ as valid inputs. 
		if(exchange.equals("NYSE") || exchange.equals("NASDAQ")) {
			// I tried to make it one if statement with the "!" character before both equals
			// above, but it wasn't working. First Java program I've written so 
			// maybe its some difference from C++ that I'm not aware of. Google
			// searches were inconclusive too. 
		} else {
			System.out.println("The stock needs to be listed on the NYSE or NASDAQ. ");
			return four(input, companies);
		}
		
		input.nextLine();
		System.out.println("What is the description of " + name + "?");
		String description = input.nextLine();
		
		int newArrLength = companies.data.length + 1;
		
		// I know I should've used ArrayList here, but I was having difficulty with it
		// when originally parsing the JSON, so I found this solution to be fine in terms
		// of getting the program to work as a whole. 
		Company newCompany = new Company(name, symbol, description, start, exchange);
		Company []newCompanyList = new Company[newArrLength];
		
		for(int i = 0; i < companies.data.length; i++) {
			newCompanyList[i] = companies.data[i];
		}
		newCompanyList[newArrLength-1] = newCompany;
		
		Data newCompanyData = new Data(newCompanyList);
		newCompanyData.fileName = companies.fileName;
		
		// output to the user confirmation of their new company input
		System.out.println("There is now a new entry for: ");
		System.out.println(newCompany.getName() + 
				", symbol " + newCompany.getTicker() + 
				", started on " + newCompany.getDate() + 
				", listed on " + newCompany.getCode() + 
				",\n\t" + newCompany.getDescription() + "\n");
		
		return newCompanyData;
	}
	
	// this function runs upon the user selecting five from the menu. 
	public static Data five(Scanner input, Data companies) {
		
		// this function allows the user to delete companies from the data set. 
		
		// letting user delete a company when there's none left causes crashes. 
		if(companies.data.length == 0) {
			System.out.println("There are no companies left to delete!");
			return companies;
		}
		
		// deleteMenu varies in size depending on how many companies are left
		String deleteMenu = "";
		for(int i = 0; i < companies.data.length; i++) {
			deleteMenu += "\t" + (i+1) + ") " + companies.data[i].getName() + "\n";
		}
		System.out.println(deleteMenu);
		
		System.out.println("Which company would you like to delete? ");
		int deleteNum = input.nextInt();

		if(deleteNum > companies.data.length) {
			System.out.println("That's not a valid input. ");
			return five(input, companies);
		}
		
		// also would have been better to use ArrayList, but refer to comment in function four
		Company []newCompanyList = new Company[companies.data.length-1];
		int x = 0;
		for(int i = 0; i < companies.data.length; i++) {
			if((i+1) == deleteNum) {
				x=1;
				continue;
			}
			newCompanyList[i-x] = companies.data[i];
		}
		
		Data newData = new Data(newCompanyList);
		newData.fileName = companies.fileName;
		return newData;
	}
	
	
	// this function runs upon the user selecting six from the menu
	public static Data six(Scanner input, Data companies) {
		
		// this function allows the user to sort the data alphabetically either way. 
		
		System.out.println("\n\t1)A to Z \n \t2)Z to A \n");

		System.out.println("What would you like to do? ");
		String inp = input.next();
		int choice = 0;
		try {
			choice = Integer.parseInt(inp);
		} catch(Exception e) {
			System.out.println("That is not a valid choice.");
			six(input, companies);
		}
		
		if(choice != 1 && choice != 2) {
			System.out.println("That is not a valid choice.");
			six(input, companies);
		}
		
		if(choice == 1) {
			// A to Z Bubble Sort
			for(int i = 0; i < companies.data.length-1; i++) {
				for(int j = 0; j < companies.data.length-1; j++) {
					String w1 = companies.data[j].getName();
					String w2 = companies.data[j+1].getName();
					if(w2.compareTo(w1) < 0) {
						Company temp = companies.data[j];
						companies.data[j] = companies.data[j+1];
						companies.data[j+1] = temp;
						
					}
				}
			}
		} else if(choice == 2) {
			// Z to A Bubble Sort
			for(int i = 0; i < companies.data.length-1; i++) {
				for(int j = 0; j < companies.data.length-1; j++) {
					String w1 = companies.data[j].getName();
					String w2 = companies.data[j+1].getName();
					if(w1.compareTo(w2) < 0) {
						Company temp = companies.data[j];
						companies.data[j] = companies.data[j+1];
						companies.data[j+1] = temp;
						
					}
				}
			}
		}
		return companies;
	}
	
	// this function runs upon user entering seven in menu
	public static void seven(Scanner input, Data companies) {
		
		// this function allows the user to save their edits before exiting the program. 
		// overwrites data that was in the input file if so. 
		
		System.out.println("\n \t 1) Yes \n \t 2) No \n Would you like to save your edits? ");
		String choice = input.next();
		int choiceInt = 0;
		try {
			choiceInt = Integer.parseInt(choice);
		} catch (Exception e) {
			System.out.println("That's not a valid choice. ");
			seven(input, companies);
		}
		if(choiceInt != 1 && choiceInt != 2) {
			System.out.println("That's not a valid choice. ");
			seven(input, companies);
		} 
		if(choiceInt == 1) {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			try {
				// write to file. 
				Writer writer = Files.newBufferedWriter(Paths.get(companies.fileName));
				gson.toJson(companies, writer);
				writer.close();
			} catch (Exception e) {
				System.out.println("Unable to write to file. Try again. ");
				seven(input, companies);
			}
			System.out.println("Thank you for using my program!");
			return;
		} else if(choiceInt == 2) {
			System.out.println("Thank you for using my program!");
			return;
		}
	}
	
	public static int loop(Scanner input, Data companies) {
		
		// actual loop that is running to display the menu and handle
		// user input choices. 
		
		int numCompanies = companies.data.length;

		String menu = 
				"\t 1) Display all public companies \n"
				+ "\t 2) Search for a stock (by ticker)\n "
				+ "\t 3) Search for all stocks on an exchange\n"
				+ "\t 4) Add a new company/stock\n"
				+ "\t 5) Remove a company\n"
				+ "\t 6) Sort companies\n"
				+ "\t 7) Exit\n"
				+ "What would you like to do?";
		
		System.out.println(menu);
		String choice = input.next();
		int choiceInt = 0;
		try {
			choiceInt = Integer.parseInt(choice);
		} catch(Exception e) {
			System.out.println("That is not a valid choice. ");
			return loop(input, companies);
		} 
		if(choiceInt == 1) {
			one(companies);
			return loop(input, companies);
		} else if(choiceInt == 2) {
			two(input, companies, numCompanies);
			return loop(input, companies);
		} else if(choiceInt == 3) {
			three(input, companies, numCompanies);
			return loop(input, companies);
		} else if(choiceInt == 4) {
			companies = four(input, companies);
			return loop(input, companies);
		} else if(choiceInt == 5) {
			companies = five(input, companies);
			return loop(input, companies);
		} else if(choiceInt == 6) {
			companies = six(input, companies);
			return loop(input, companies);
		} else if(choiceInt == 7) {
			seven(input, companies);
			return 0;
		} else {
			System.out.println("Please enter a valid input. ");
			return loop(input, companies);
		}
	}
	
	public static Data readFile(Scanner input) {
		
		// this is the function that reads the JSON file and returns
		// a Data object that contains the list of companies and it's filename. 
		
		String fileName = null;
		
		Data companyList;
		
		System.out.println("What is the name of the company file?");
		
		fileName = input.next();
		
		try {
			Gson gson = new Gson();
			
			Reader reader = Files.newBufferedReader(Paths.get(fileName));
						
			companyList = gson.fromJson(reader, Data.class);
			
			reader.close();
			
		} catch(NoSuchFileException e) {
			
			System.out.println("The file " + fileName + " could not be found. ");
			return readFile(input);
			
		} catch(IOException e) {
			
			System.out.println("The file " + fileName + " is not formatted properly. There was trouble parsing the file. ");
			return readFile(input);
	
		} 
		
		// just a loop that goes through and calls a method that ensures all data
		// is entered in for each company - if not, file not formatted correctly. 
		for(int i = 0; i < companyList.data.length; i++) {
			if(companyList.data[i].allClear()) continue;
			else {
				System.out.println("File not formatted properly. A data point is missing from a company.");
				return readFile(input);
			}
		}
		
		System.out.println("The file has been properly read. ");
		
		companyList.fileName = fileName;
		return companyList;
	}

}


class Company { // fully encapsulated class. 
	
	private String name;
	private String ticker;
	private String description;
	private Date startDate;
	private String exchangeCode;
	
	public Company(String name, String tick, String desc, String date, String code) {
		this.name = name;
		this.ticker = tick;
		this.description = desc;
		
		Date formattedDate = this.formatDate(date);
		
		this.startDate = formattedDate;
		this.exchangeCode = code;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String companyName) {
		this.name = companyName;
	}
	
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String tick) {
		this.ticker = tick;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String desc) {
		this.description = desc;
	}
	
	public Date getDate() {
		return startDate;
	}
	public void setDate(Date date) {
		this.startDate = date;
	}
	
	public Date formatDate(String date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date formattedDate = new Date();
		
		// eclipse is telling me to handle case of parsing error. 
		try {
			formattedDate = format.parse(date);
		}catch(Exception e) {
			System.out.println("There was trouble parsing the date. ");
		}
		return formattedDate;
	}
	
	public String getCode() {
		return exchangeCode;
	}
	public void setCode(String code) {
		this.exchangeCode = code;
	}
	
	public boolean allClear() {
		if(this.name == null) return false;
		else if(this.ticker == null) return false;
		else if(this.startDate == null) return false;
		else if(this.ticker == null) return false;
		else if(this.description == null) return false;
		else return true;
	}
}

class Data { // didn't make it encapsulated because I need to constantly deal with the company array. 
	public Company[] data;
	String fileName;
	Data(Company[] newList) {
		this.data = newList;
	}
}


