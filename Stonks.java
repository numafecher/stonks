// Numa Fecher
// 07/04/2024
// CSE 122
// TA: 
// This class is a stock portfolio simulator. It allows a user to buy and sell shares of
// specified stocks and save their final portfolio to a file.

import java.util.*;
import java.io.*;

public class Stonks {
    public static final String STOCKS_FILE_NAME = "stonks.tsv";


    // Behavior: 
    //   - This method creates a menu of choices of actions to interact with the
    //     stock market for the user to choose from: buy, sell, save, quit.
    //   - Buying stocks will increase the number of stock of the ticker they own
    //     in their portfolio if their budget is large enough to purchase any.
    //   - Selling stocks will decrease the number of stock of the ticker they own
    //     in their portfolio.
    //   - Saving will save their portfolio to a user named text file.
    //   - Quitting will quit the programme and tell the user the final value of
    //     their portfolio.
    // Parameters:
    //   - User inputs
    // Returns:
    //   - No return value
    // Exceptions:
    //   - Invalid file name for saving portfolio
    //   - Invalid user input type (e.g. non-integer when integer is required,
    //     invalid user input for ticker name)
    public static void main(String[] args) throws FileNotFoundException {
        // TODO: write main method here
        Scanner console = new Scanner(System.in);
        Scanner fileScan = new Scanner(new File(STOCKS_FILE_NAME));

        // Initialising arrays:
        int numStocks = Integer.parseInt(fileScan.nextLine()); // First line states no. stocks
        String[] stocks = new String[numStocks];
        double[] prices = new double[numStocks];
        double[] portfolio = new double[numStocks];

        // Populating arrays from file:
        loadFile(fileScan, stocks, prices);

        // Print welcome line:
        System.out.println("Welcome to the CSE 122 Stocks Simulator!");
        System.out.println("There are " + numStocks + " stocks on the market:");
        for (int i = 0; i < numStocks; i++) {
            System.out.println(stocks[i] + ": " + prices[i]);
        }
        String choice = "";

        // Input options:
        while (!choice.equalsIgnoreCase("Q")) {
            System.out.println(); // Empty line break from previous selection
            System.out.println("Menu: (B)uy, (Se)ll, (S)ave, (Q)uit");
            System.out.print("Enter your choice: ");

            choice = console.nextLine();

            // Buy
            if (choice.equalsIgnoreCase("B")) {
                System.out.print("Enter the stock ticker: ");
                String ticker = console.nextLine();
                System.out.print("Enter your budget: ");
                double budget = Double.parseDouble(console.nextLine());

                buy(ticker, budget, stocks, prices, portfolio);
            // Sell
            } else if (choice.equalsIgnoreCase("Se")) {
                System.out.print("Enter the stock ticker: ");
                String ticker = console.nextLine();
                System.out.print("Enter the number of shares to sell: ");
                double amntToSell = Double.parseDouble(console.nextLine());

                sell(ticker, amntToSell, stocks, prices, portfolio);
            // Save
            } else if (choice.equalsIgnoreCase("S")) {
                System.out.print("Enter new portfolio file name: ");
                String fileName = console.nextLine();

                save(fileName, stocks, portfolio);
            // Invalid Input
            } else if (!choice.equalsIgnoreCase("Q")) {
                System.out.println("Invalid choice: " + choice);
                System.out.println("Please try again");
            }
        }
        // Quitting programme
        quit(prices, portfolio);
    }

    // TODO: write your methods here

    // loadFile: Loading file to populate stocks and prices
    // Behavior: 
    //   - This method loads a file to extract information on stocks and prices
    // Parameters:
    //   - fileScan: the user’s income this month
    //   - stocks: initialised array to be populated with stock tickers
    //   - prcies: initialised array to be populated with stock prices
    // Returns:
    //   - No return value
    // Exceptions:
    //   - No known exceptions
    public static void loadFile(Scanner fileScan, String[] stocks, double[] prices) {
        int index = 0;
        fileScan.nextLine(); // Skip over header line

        while (fileScan.hasNextLine()) {
            String line = fileScan.nextLine();
            Scanner lineScan = new Scanner(line);

            stocks[index] = lineScan.next();
            prices[index] = lineScan.nextDouble();

            index++;
        }
    }

    // buy: Buying stock
    // Behavior: 
    //   - This method buys a number of chosen stocks based on the inputted budget.
    // Parameters:
    //   - ticker: user's chosen stock to be bought
    //   - budget: user's avilable money to use to buy stocks
    //   - stocks: array of possible stocks to be bought
    //   - prices: array of prices of each stock of the corresponding index
    //   - portfolio: user's portfolio of owned stocks
    // Returns:
    //   - Does not return anything
    // Exceptions:
    //   - Dimensions of prices array is smaller than the dimensions of stocks array
    public static void buy(String ticker, double budget, String[] stocks, double[] prices, double[] portfolio) {
        if (budget < 5) {
            System.out.println("Budget must be at least $5");
        } else {
            int index = indexOf(ticker, stocks);

            double countShares = budget / prices[index];
            portfolio[index] += countShares;
            System.out.println("You successfully bought " + ticker + ".");
        }
    }

    // indexOf: Finding index
    // Behavior: 
    //   - This method finds the index of the specified ticker in the array of available stocks
    // Parameters:
    //   - ticker: chosen stock to be found
    //   - stocks: array of available stocks
    // Returns:
    //   - index: index location of the specified stock ticker in the stocks array
    // Exceptions:
    //   - No known exceptions
    public static int indexOf(String ticker, String[] stocks) {
        int index = -1;
        for (int i = 0; i < stocks.length; i++) {
            if (stocks[i].equals(ticker)) {
                index = i;
            }
        }
        return index;
    }

    // sell: Selling stock
    // Behavior: 
    //   - This method sells a number of chosen stocks determined by the user.
    // Parameters:
    //   - ticker: user's chosen stock to be sold
    //   - amntToSell: user's avilable money to use to buy stocks
    //   - stocks: array of possible stocks to be bought
    //   - prices: array of prices of each stock of the corresponding index
    //   - portfolio: user's portfolio of number of owned stocks
    // Returns:
    //   - Does not return anything
    // Exceptions:
    //   - Dimensions of portfolio array is smaller than dimensions of stocks array
    public static void sell(String ticker, double amntToSell, String[] stocks, double[] prices, double[] portfolio){
        int index = indexOf(ticker, stocks);
        if (portfolio[index] >= amntToSell) {
            portfolio[index] -= amntToSell;
            System.out.println("You successfully sold " + amntToSell + " shares of " + ticker + ".");
        } else {
            System.out.println("You do not have enough shares of " + ticker + " to sell " + amntToSell + " shares.");
        }
    }

    // save: Saving to file
    // Behavior: 
    //   - This method saves the number of each stock the user owns and their corresponding stock tickers.
    // Parameters:
    //   - fileName: user's chosen name for the file
    //   - stocks: array of possible stocks
    //   - portfolio: user's portfolio of number of owned stocks
    // Returns:
    //   - Does not return anything
    // Exceptions:
    //   - Invalid file name
    //   - Dimensions of portfolio array is smaller than dimensions of stocks array
    public static void save(String fileName, String[] stocks, double[] portfolio) throws FileNotFoundException {
        File outFile = new File(fileName);
        PrintStream out = new PrintStream(outFile);

        for (int i = 0; i < stocks.length; i++) {
            if (portfolio[i] > 0) {
               out.println(stocks[i] + " " + portfolio[i]);
            }
        }
    }

    // quit: Quitting programme
    // Behavior: 
    //   - This method finds and prints the total value of the user's portfolio.
    // Parameters:
    //   - prices: array of prices of each stock of the corresponding index
    //   - portfolio: user's portfolio of number of owned stocks
    // Returns:
    //   - Does not return anything
    // Exceptions:
    //   - Dimensions of portfolio array is smaller than dimensions of prices array
    public static void quit(double[] prices, double[] portfolio) {
        double portfolioValue = 0;
        for (int i = 0; i < prices.length; i++) {
            portfolioValue += prices[i] * portfolio[i];
        }
        System.out.println("Your portfolio is currently valued at: $" + portfolioValue);
    }
}
