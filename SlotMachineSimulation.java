//Theese are required to create scanner objects and to read/write files
import java.util.*;
import java.io.*;

public class SlotMachineSimulation
{
public static void main(String[] args) throws IOException
{
	//Creates scanner object and a variable for the main menu switch
	Scanner keyboard = new Scanner(System.in);
	int mainMenu;
	
	//Friendly little welcome
	System.out.print("Welcome to the Slot Machine Simulator!");
	
	//Do while loop that decides what the program will execute and checks for proper input
	do{
		//Prints the menu
		System.out.print("\n\nActions:\n1. Start a new game" +
		"\n2. Load saved game\n3. View scores\n4. Exit\n\nPlease select an action: ");
		
		//records user's menu choice
		mainMenu = keyboard.nextInt();
		
		//do while loop that checks user input for validity
		do{
			if(mainMenu!=1 && mainMenu!=2 && mainMenu!=3 && mainMenu!=4)
			{
				System.out.print("Invalid selection. Please Select an action: ");
				mainMenu = keyboard.nextInt();
			}
		}while(mainMenu!=1 && mainMenu!=2 && mainMenu!=3 && mainMenu!=4);
		
		//switch case that reroutes the program to the right place
		switch (mainMenu){
			//Case 1 plays the game as a new user
			case 1:
				GamePlay(0);
				break;
			//Case 2 attempts to load data
			case 2:
				GamePlay(1);
				break;
			//Case 3 attempts to load scores
			case 3:
				LoadScores();
				break;
			//Case 4 exits the program
			case 4:
				break;
		}
	}while (mainMenu!=4);
	
	//farewell
	System.out.println("\n\nGoodbye!");
}



//*************************************************************//
//*****************Supporting Methods**************************//
//*************************************************************//



//GamePlay() decides wether or not the game is loaded or new based on the menu choice
//an integer is assigned depending on the menu choice made
//This method returns an array of data for the rest of the program to function properly
public static void GamePlay(int gameMode) throws IOException
{
	//Needed for scanner object
	Scanner keyboard = new Scanner(System.in);
	
	//initializes the array that will be returned from another method
	double[] game;
	String[] NameMoney;
	
	//initialization of misc. needed variables
	String name=" ", moneyString;
	double money=0, bet=0;
	
	//gameMode0 means that the user chose to start a new game
	//this if statement records the user's name for a new game
	if (gameMode==0)
	{
		System.out.print("\nBefore the game begins, please enter your name: ");
		name = keyboard.nextLine();
		money = 100;
		System.out.printf("\nGame start! You will begin with $%.2f. Enter a negative value to quit the game.\nGood luck, %s!", money, name);
		System.out.printf("\n\nYou currently have: $%.2f\nHow much would you like to bet? ", money);
		bet = keyboard.nextDouble();
	}
	
	//If a user wanted to try to load a game the else statement is used
	//if there is saved data then the user will be sent back to the main menu
	else if (gameMode==1)
	{
		//Retrieves the name and money from savedGame.txt
		NameMoney = LoadName();
		name = NameMoney[0];
		moneyString = NameMoney[1];
		
		//this converts the string for money into a value.
		//this seemed to be the most efficient and easiest way to do this, at least to me
		money = Double.valueOf(moneyString);
		
		//This loop starts the game for loaded data and prepares the bet to be sent to finish the game
		//an if loop is used because negative money will be returned if there is no current saved data
		if (money>0)
		{
			System.out.printf("\n\nYou currently have: $%.2f\nHow much would you like to bet? ", money);
			bet = keyboard.nextDouble();
		}
	}
	
	//this the array that is returned from the GenerateGame method
	game = GenerateGame(bet, money);
	
	//This calls the SaveFunction method and breaks the array's data into the needed parts
	SaveFunction(name, game[0], game[1]);

	return;
}



//Generate Game is the busiest method of the game
//This contains all of the game play for the most part
public static double[] GenerateGame(double bet, double money)
{
	//Initialization of misc. needed variables, scanner, etc
	String name=" ", moneyString, slot1, slot2, slot3;
	double winnings, matches;
	Scanner keyboard = new Scanner(System.in);
	
	//this while loop plays the game
	while (bet>0)
	{
		//This do while loop with imbedded for loop makes sure you don't bet more money than you have
		do{
			if (bet > money)
			{
				System.out.print("\nYour bet is greater than your current total\nPlease enter a valid amount: ");
				bet = keyboard.nextDouble();
			}
		}while (bet > money);
		
		//Once the program has made it this far these 3 variables are generated using another method
		//Further details at the method
		slot1 = slotGenerator();
		slot2 = slotGenerator();
		slot3 = slotGenerator();
		
		//This prints the slots display neatly
		System.out.printf("\n------------------------------\n%-10s%-10s%-10s\n------------------------------",slot1, slot2, slot3);
		
		//these are simple calculations to see how many matches and determine the winnings
		//the money after playing is also calculated
		winnings = Winnings(slot1, slot2, slot3, bet);
		matches = Matched(slot1, slot2, slot3);
		money += winnings;
		
		//This if statement increases the winnings to zero if the winnings are < zero from the winnings method calculation
		if (winnings < 0)
		{
			winnings = 0;
		}
		
		//Printer for the matches and winngins
		System.out.printf("\n\nNumber of matches: %.0f. You win: $%.2f", matches, winnings);
		
		//As long as you still have money you can bet. This if statment determines that
		if (money > 0)
		{
			System.out.printf("\n\nYou currently have: $%.2f\nHow much would you like to bet? ", money);
			bet = keyboard.nextDouble();
		}
		//If you ran out of money this else statement returns your zero money and bet
		else
		{
			double[] game = {money, bet};
			return(game);
		}
		
	}
	//this statement, and the one 5 lines above generates the array that contains the data needed to run correctly
	double[] game = {money, bet};
	return(game);
}



//The SaveFunction method determines if you want to save your progress or not
public static void SaveFunction(String name, double money, double bet) throws IOException
{
	//Initialization of a needed variable and scanner
	double saveGame;
	Scanner keyboard = new Scanner(System.in);
	
	//this if statment prompts you if you want to save if you enter a negative value for your bet
	if (bet < 0)
	{
		//this do while loop prevents the user from entering invalid input
		do{
			System.out.print("\n\nWould you like to save your game? Enter 1 for \"yes\", 0 for \"no\": ");
			saveGame = keyboard.nextInt();
			while (saveGame != 0 && saveGame != 1)
			{
				System.out.print("\n\nInvalid input, please enter 1 for \"yes\", 0 for \"no\": ");
				saveGame = keyboard.nextInt();
			}
		}while (saveGame != 0 && saveGame != 1);
		
		//this if statment lets the user know their progress is saved and calls the actual save method
		if (saveGame == 1)
		{
			System.out.print("\n\nYour current game has been saved.");
			SaveGame(name, money);
		}
		//if the user decides to not save, this else statment lets the user know their data has been recorded
		else
		{
			System.out.print("\n\nGame over! Your score has been written to scores.txt, " + name + "!");
			WriteScores(name, money);
		}
	}
	//This else if statement  records the user's score since they have lost all of their money
	else if (money == 0)
	{
		System.out.print("\n\nGame over! You've gone broke.\nYour score has been written to scores.txt, " + name +"!");
		WriteScores(name, money);
	}
	return;
}



//This method generates a random number and assigns that number a string for the slot machine
public static String slotGenerator()
{
	//This creates the random number generator and it is then used (only numbers 0-5)
	Random rn = new Random();
	int x = rn.nextInt(6);
	//initialization of the soon to be returned string
	String slot;
	
	//this switch case statement assigns the randomly generated number a String name
	switch (x){
		case 1:
			slot = "Cherries";
			break;
		case 2:
			slot = "Oranges";
			break;
		case 3:
			slot = "Plums";
			break;
		case 4:
			slot = "Bells";
			break;
		case 5:
			slot = "Melons";
			break;
		default:
			slot = "Bars";
	}
	//this returns the string generated
	return(slot);
}



//This method determins the number of matches  using a simple if else if statment and returns the number of matches
public static double Matched(String slot1, String slot2, String slot3)
{	
	//this is the initialization of the soon to be returned double
	double match;
	
	//if all 3 slots are the same, there are 2 matches
	if (slot1.equals(slot2) && slot2.equals(slot3))
	{
		match = 2;
	}
	//if any 2 match, there is 1 match
	else if (slot1.equals(slot2)||slot1.equals(slot3)||slot2.equals(slot3))
	{
		match = 1;
	}
	//otherwise, there are none :( sorry
	else
	{
		match = (0);
	}
	//return the number of matches to be displayed and used for winnings calculations
	return(match);
}


//This method calculates the winnings.
public static double Winnings(String slot1, String slot2, String slot3, double bet)
{
	//intializes the soon to be returned double
	double winnings;
	
	//if everything matches 3x the money bet is won
	if (slot1.equals(slot2) && slot2.equals(slot3))
	{
		winnings = bet*3;
	}
	//if 2 match, 2x the money bet is won
	else if (slot1.equals(slot2)||slot1.equals(slot3)||slot2.equals(slot3))
	{
		winnings = bet*2;
	}
	//otherwise money is lost. Winnings is a negative number here for the money calculation
	//it is just easier to reapply a 0 value after using the negative winnings to calculate the money the user has
	else
	{
		winnings = (0-bet);
	}
	return(winnings);
}



//This method attempts to load and display scores
public static void LoadScores() throws IOException
{
	//assigns the file to be read
	File scores = new File("scores.txt");
	
	//if the file does not exist a nice little message is displayed saying so without crashing the program
	if (!scores.exists())
	{
		System.out.print("\nThere are no scores to display at this time!");
		return;
	}
	//if the file exists the data is read
	else
	{
		//creates an input reader and displays the data nicely
		Scanner inputReader = new Scanner(scores);
		System.out.print("\n\nName\t\t\tScore\n----\t\t\t-----\n");
		
		//this while loop makes sure that all of the scores are displayed
		while(inputReader.hasNext())
		{
			String name = inputReader.nextLine();
			String score = inputReader.nextLine();
			System.out.printf("%-24s$%s\n",name, score);
		}
		inputReader.close();
	}
	
	return;
}



//this method writes the scores to a text file
public static void WriteScores(String name, double score) throws IOException
{
	//the file writer ammends the scores.txt file, or creates it if needed.
	FileWriter fwriter = new FileWriter("scores.txt", true);
	PrintWriter scoresWriter = new PrintWriter(fwriter);
	scoresWriter.println(name);
	//this writes the money as a string but with 2 decimal places.
	//for some reason using printf("%.2f\n",score) would not work and returned nasty errors
	scoresWriter.printf("%.2f" + "\n",score);
	scoresWriter.close();
}



//This method simply saves the game to a text file
public static void SaveGame(String name, double score) throws IOException
{
	//this printer saves the game over the file that exists or creates a new document if the file does not exist
	PrintWriter GameSave = new PrintWriter("savedGame.txt");
	GameSave.println(name);
	//this printf is used just as in the WriteScores method
	GameSave.printf("%.2f" + "\n",score);
	GameSave.close();
}



//This method attempts to load the name of saved data
//I used an array here after realizing I should jsut do that instead of
//having multiple methods called to do such a simple read.
public static String[] LoadName() throws IOException
{
	//this finds the file
	File game = new File("savedGame.txt");
	//initializes elements that will be in the array
	//in case of no load data irrelevant data is returned
	String name = "name", moneyString = "-10";
	
	//if the file doesn't exist a nice message is displayed instead of an error
	if (!game.exists())
	{
		System.out.print("\nNo saved game at this time");
	}
	//if the file exists the name is read
	else
	{
		Scanner inputReader = new Scanner(game);
		name = inputReader.nextLine();
		moneyString = inputReader.nextLine();
		inputReader.close();
	}
	
	//this creates teh array that is sent back
	String[] NameMoney = {name, moneyString};
	
	return(NameMoney);
}
}