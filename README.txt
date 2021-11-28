Investment Portfolio Project
    Authored by Mazen Bahgat

Functionality:
    With this project an investor can create a new investment portfolio or import their 
    existing portfolio to the program by responding to simple prompts on the screen. This
    program can perform operations on and keep track of 2 types of investments 
    ( Stocks  and Mutual Funds) and give the user general information about the 
    whole portfolio. 
    
    Examples of operations that you can perform, using this program: 
    Buying & selling of new Stocks and Mutual Funds, or buying more of an already owned investment
    in your portfolio. 
    Updating the prices of all your investments to keep your data up-to-date with the market. 
    
    Examples of insights in your portfolio:
    Total and individual gain on investments on your portfolio.
    Investment search based on user-generated criteria (including price range, investment symbol and investment name keywords).


Download and Run information:
    Enter the main directory: Investment-Portfolio.

    Compile the java files by using the following commands in the same order:
        javac ePortfolio/*.java

    Now you can run the program using the following command:
        java ePortfolio.GUI [File Name]
    
    The file name is where the program will be getting the initial investments
    and the final portfolio will be output to it in the end of execution.

    Make sure your system DISPLAY variable is set to the correct output stream, otherwise
    you will be getting DISPLAY variable errors. If you are on WSL, you can download Xming for
    windows, run it, type the following command in your terminal "export DISPLAY=:0" and you 
    will now be able to run the GUI program through Xming.
    
    At first you will be prompted to choose an option from the "Commands" menu:
        Buy: lets you purhcase a new investment by adding it's information or add 
        to an existing investment with a new price and required quantity.
        Sell: Lets you sell part or all of one of your investments on your portfolio.
        Update: Lets you update all prices of all your investments.
        getGain: Informs you with the gain on all of your investments on your portfolio.
        Search: Outputs investments based on specific criteria like price range and symbol etc.
        Quit: Exits the program after saving the investments to the file initially opened for reading.

        Typing illegal data will always give you other chances to re-enter data.
        

API Specifications:
    This project consists of a main Portfolio class, an Abstract Investment class and 2 derived classes; 
    a Stock class and  Mutual Fund class. Each of the supporting classes only perform operations like buy, sell
    and get the gain on investment for their respective investment. The Portfolio class performs 
    all the operations concerned with multiple investment objects like keeping track of total gain 
    on investment. A GUI class is also included in the package for a more seamless user interface.
 
    The Portfolio class is what you will make the most use of if you were to extend the project. To see 
    all API specifications of the ePortfolio package, check the JavaDocs folder for full documentation.
     
