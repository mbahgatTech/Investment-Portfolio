package ePortfolio;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.awt.*;

/**
 * Represents a JFrame window for the ePortfolio program
 */
public class GUI extends JFrame {
    // dimensions
    public static final int WIDTH = 950;
    public static final int HEIGHT = 600;
    
    // Panels for each functionality and its messages
    private JPanel menuPanel, contentPanel, buyPanel, sellPanel,
    updatePanel, gainPanel, searchPanel, messagesPanel;
    private Portfolio myPortfolio; 
    private int investmentIndex = 0;



    /**
     * Constructor method that declares a new GUI frame.
     * @param fileName String object that contains the name of the file keeping records 
     * of portfolio.
     */
    public GUI(String fileName) {
        super("ePortfolio");
        myPortfolio = new Portfolio();
        loadInvestments(fileName);
        
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        
        // declare new panel for the "Commands"menu
        menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout());
        JMenu commands = new JMenu("Commands");
        
        // ADDING MENU ITMES TO COMMANDS //

        // Buy option
        JMenuItem buy = new JMenuItem("Buy");
        // anonymous actionListener resets all panels including Messages 
        // sets buyPanel to visible
        buy.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPanels();
                createMessages("Messages");
                buyPanel.add(messagesPanel);
                buyPanel.setVisible(true);
            }
        });
        
        // Sell option
        JMenuItem sell = new JMenuItem("Sell");
        // anonymous actionListener resets all panels including Messages 
        // sets sellPanel to visible
        sell.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPanels();
                createMessages("Messages");
                sellPanel.add(messagesPanel);
                sellPanel.setVisible(true);
            }
        });
        
        // Update option
        JMenuItem update = new JMenuItem("Update");
        // anonymous actionListener resets all panels including Messages 
        // sets updatePanel to visible
        update.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPanels();
                // click prev to make the first investment visible
                clickButton(updatePanel, "Prev");
                createMessages("Messages");
                updatePanel.add(messagesPanel);
                updatePanel.setVisible(true);
            }
        });
        
        // Get Gain option
        JMenuItem getGain = new JMenuItem("Get Gain");
        // anonymous actionListener resets all panels including Messages 
        // sets gainPanel to visible and sets messagesPanel label to Individual Gains
        getGain.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPanels();
                setGainField(gainPanel);
                createMessages("Individual Gains");
                setMessage(messagesPanel, myPortfolio.getIndividualGains());
                gainPanel.add(messagesPanel);
                gainPanel.setVisible(true);
            }
        });
        
        //Search option
        JMenuItem search = new JMenuItem("Search");
        // anonymous actionListener resets all panels including Messages 
        // sets searchPanel to visible and sets messagesPanel label to Serarch Results
        search.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetPanels();
                createMessages("Search Results");
                searchPanel.add(messagesPanel);
                searchPanel.setVisible(true);
            }
        });
        
        // quit option
        JMenuItem quit = new JMenuItem("Quit");
        // anonymous actionListener terminates program
        quit.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // saves investments to a file in case fileName wasn't null
                try {
                    if (fileName != null) {
                        myPortfolio.saveInvestments(fileName);
                    }
                }
                catch (Exception exc) {
                    // print exception message since program will terminate afetrwards
                    System.out.println(exc.getMessage());
                }
                System.exit(0);
            }
        });
        
        //add menuItems to the menu
        commands.add(buy);
        commands.add(sell);
        commands.add(update);
        commands.add(getGain);
        commands.add(search);
        commands.add(quit);
        
        // adding menu to a menuBar
        JMenuBar bar = new JMenuBar();
        bar.add(commands);
        bar.setPreferredSize(new Dimension(900, 50));
        setJMenuBar(bar);
        
        // create the initial panel (Welcome screen) and add it to the frame
        createContent();
        add(contentPanel);
        
        // create the other functionality panels only shown when chosen from
        // the commands menu bar and will be set visible by the actionListeners
        createBuy();
        add(buyPanel);
        buyPanel.setVisible(false);
        
        createSell();
        add(sellPanel);
        sellPanel.setVisible(false);
        
        createGain();
        add(gainPanel);
        gainPanel.setVisible(false);
        
        createUpdate();
        add(updatePanel);
        updatePanel.setVisible(false);
        
        createSearch();
        add(searchPanel);
        searchPanel.setVisible(false);
    }
    
    /**
     * Private method resets all functionality panels by setting their visibility
     * to false. It also removes the messagesPanel from each functionality panel
     * so a new one can be added again in the ActionListeners.
     */
    private void resetPanels() {
        // Do nothing on catching Exceptions, null pointers are only possible here
        try {
            buyPanel.remove(messagesPanel);
            sellPanel.remove(messagesPanel);
            updatePanel.remove(messagesPanel);   
            gainPanel.remove(messagesPanel);
            searchPanel.remove(messagesPanel);
        }
        catch (Exception e) {
            
        }
        
        // set visibility of all pannels to false
        contentPanel.setVisible(false);
        buyPanel.setVisible(false);
        sellPanel.setVisible(false);
        updatePanel.setVisible(false);
        gainPanel.setVisible(false);
        searchPanel.setVisible(false);
    }

    /**
     * Private method sets the initial interface with a welcome page and 
     * instructions to start operation.
     */
    private void createContent() {
        // split contentPanel into 2 halves vertically for welcome message and
        // instructions message
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(2,1));
        JTextArea welcome = new JTextArea("Welcome to ePortfolio.");
        welcome.setEditable(false);
        welcome.setFont(new Font("Courier", Font.PLAIN, 50));
        welcome.setLineWrap(true);
        welcome.setWrapStyleWord(true); // words can be written on a new line if space is limited
        contentPanel.add(welcome);
        
        // instruction text area
        JTextArea prompt = new JTextArea(
            "Choose a command from the \"Commands\" menu\n" + 
            "to buy or sell an investment, update prices\n" + 
            "for all investments, get gain for the\n" + 
            "portfolio, search for relevant investments,\n" + 
            "or quit the program.\n"
            );
        prompt.setAlignmentX(Component.CENTER_ALIGNMENT);
        prompt.setEditable(false);
        prompt.setLineWrap(false);
        prompt.setFont(new Font("Courier", Font.PLAIN, 35));
        
        // scrollable textarea for the instructions
        JScrollPane text = new JScrollPane(prompt);
        // scroll bars are only set visible when they're needed
        text.setHorizontalScrollBarPolicy(
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        text.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        text.setAlignmentX(Component.CENTER_ALIGNMENT);
        text.setBorder(null);
        contentPanel.add(text);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    }

    /**
     * Private method sets the buy functionality interface with 3 sections for
     * text fields, buttons and messages. 
     */
    private void createBuy() {
        buyPanel = new JPanel();
        buyPanel.setLayout(new GridLayout(2,1));
        JPanel functionality = new JPanel();
        functionality.setLayout(new GridLayout(1,2));

        // fields panel for all textfields and labels and the heading label
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(6,1));
        JLabel  title = new JLabel("Buying an investment\n");
        fields.add(title);

        JPanel type = new JPanel();
        type.setLayout(new FlowLayout());
        JLabel typeLabel = new JLabel("Type");
        type.add(typeLabel);

        // comboBox with 2 choices for investment type, Stock is default 
        String [] types = {"Stock", "MutualFund"};
        JComboBox<String> typeChoice = new JComboBox<>(types);
        typeChoice.setSelectedIndex(0);
        type.add(typeChoice);
        fields.add(type);
        
        // each field contains a label and an editable text field for user input
        // flowLayout is used for each field
        JPanel symbol = new JPanel();
        symbol.setLayout(new FlowLayout());
        JLabel symbolLabel = new JLabel("Symbol");
        JTextField symbolInput = new JTextField();
        symbolInput.setEditable(true);
        symbolInput.setColumns(8);
        symbol.add(symbolLabel);
        symbol.add(symbolInput);
        fields.add(symbol);

        JPanel name = new JPanel();
        name.setLayout(new FlowLayout());
        JLabel nameLabel = new JLabel("Name");
        JTextField nameInput = new JTextField();
        nameInput.setEditable(true);
        nameInput.setColumns(12);
        name.add(nameLabel);
        name.add(nameInput);
        // empty border used to align name with other fields
        name.setBorder(BorderFactory.createEmptyBorder(0,35,0,0));
        fields.add(name);

        // purchase quantity
        JPanel quantity = new JPanel();
        quantity.setLayout(new FlowLayout());
        JLabel quantityLabel = new JLabel("Quantity");
        JTextField quantityInput = new JTextField();
        quantityInput.setEditable(true);
        quantityInput.setColumns(6);
        quantity.add(quantityLabel);
        quantity.add(quantityInput);
        // empty border used to align quantity with other fields
        quantity.setBorder(BorderFactory.createEmptyBorder(0,-6,0,0));
        fields.add(quantity);

        // purchase price
        JPanel price = new JPanel();
        price.setLayout(new FlowLayout());
        JLabel priceLabel = new JLabel("Price");
        JTextField priceInput = new JTextField();
        priceInput.setEditable(true);
        priceInput.setColumns(6);
        price.add(priceLabel);
        price.add(priceInput);
        // empty border used to align price with other fields
        price.setBorder(BorderFactory.createEmptyBorder(0,-30,0,0));
        fields.add(price);
        // vertical dashed border to the right of fields
        fields.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, -1, -1, 5), 
                                                            BorderFactory.createDashedBorder(null, 5, 5)));
        functionality.add(fields);

        // buttons panel split into 2 rows 
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2,1));

        // reset button
        JPanel reset = new JPanel();
        reset.setLayout(new FlowLayout());
        JButton resetButton = new JButton("Reset");
        // anonymous actionListener clears all the text fields 
        // and resets comboBox choice to Stock
        resetButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields(buyPanel);
            }
        });
        reset.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        reset.setMaximumSize(new Dimension(300,200));
        reset.add(resetButton);
        buttons.add(reset);

        // buy button
        JPanel buy = new JPanel();
        JButton buyButton = new JButton("Buy");
        // anonymous actionListener performs purchase operation
        buyButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // textfields are used to fill their respective parameters to 
                    // call buy method on myPortfolio Portfolio object
                    setMessage(messagesPanel, myPortfolio.buy(((String)typeChoice.getSelectedItem()).strip(), 
                    symbolInput.getText().strip(), ((String)nameInput.getText()).strip(), 
                    ((String)quantityInput.getText()).strip(), ((String)priceInput.getText()).strip()));
                }
                catch (Exception exc) {
                    // incase an exception occurs, exception message is displayed
                    setMessage(messagesPanel, exc.getMessage());
                }
            }
        });
        buy.add(buyButton);
        buy.setMaximumSize(new Dimension(300,200));
        buy.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        buttons.add(buy);
        functionality.add(buttons);
        // dashed horizontal border is set below the fucntionalit panel (over messages when added)
        functionality.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, -1, 5, -1), 
        BorderFactory.createDashedBorder(null, 5, 5)));
        buyPanel.add(functionality);
    }
    
    /**
     * Private method sets the sell functionality interface with 3 sections for
     * text fields, buttons and messages. 
     */
    private void createSell() {
        sellPanel = new JPanel();
        sellPanel.setLayout(new GridLayout(2,1));
        JPanel functionality = new JPanel();
        functionality.setLayout(new GridLayout(1,2));
    
        // fields panel for all heading label and textfields for user input
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(4,1));
        JLabel  title = new JLabel("Selling an investment");
        fields.add(title);
    
        // symbol input panel
        JPanel symbol = new JPanel();
        symbol.setLayout(new FlowLayout());
        JLabel symbolLabel = new JLabel("Symbol");
        JTextField symbolInput = new JTextField();
        symbolInput.setEditable(true);
        symbolInput.setColumns(8);
        symbol.add(symbolLabel);
        symbol.add(symbolInput);
        fields.add(symbol);
    
        // quantity input panel
        JPanel quantity = new JPanel();
        quantity.setLayout(new FlowLayout());
        JLabel quantityLabel = new JLabel("Quantity");
        JTextField quantityInput = new JTextField();
        quantityInput.setEditable(true);
        quantityInput.setColumns(6);
        quantity.add(quantityLabel);
        quantity.add(quantityInput);
        //align panel with the other input panels using empty border
        quantity.setBorder(BorderFactory.createEmptyBorder(0,-6,0,0));
        fields.add(quantity);
    
        // price panel
        JPanel price = new JPanel();
        price.setLayout(new FlowLayout());
        JLabel priceLabel = new JLabel("Price");
        JTextField priceInput = new JTextField();
        priceInput.setEditable(true);
        priceInput.setColumns(6);
        price.add(priceLabel);
        price.add(priceInput);
        price.setBorder(BorderFactory.createEmptyBorder(0,-30,0,0));
        fields.add(price);
        fields.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, -1, -1, 5), 
                                                            BorderFactory.createDashedBorder(null, 5, 5)));
        functionality.add(fields);
        // all text fields are editable for user input
    
        // buttons panel split into 2 rows for 2 buttons
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(2,1));

        // reset button
        JPanel reset = new JPanel();
        reset.setLayout(new FlowLayout());
        JButton resetButton = new JButton("Reset");
        // actionListener clears all the text fields in the panel 
        resetButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields(sellPanel);
            }
        });
        reset.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        reset.setMaximumSize(new Dimension(300,200));
        reset.add(resetButton);
        buttons.add(reset);
    
        // sell button
        JPanel sell = new JPanel();
        JButton sellButton = new JButton("Sell");
        //actionListener performs sell operation on required investment
        sellButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // text fields are used for their respective parameters
                try {
                    setMessage(messagesPanel, myPortfolio.sell(symbolInput.getText().strip(),
                    quantityInput.getText().strip(), priceInput.getText().strip()));
                }
                catch (Exception exc) {
                    // exception message shown in messages text area
                    setMessage(messagesPanel, exc.getMessage());
                }
            }
        });
        sell.add(sellButton);
        sell.setMaximumSize(new Dimension(300,200));
        sell.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        buttons.add(sell);
        functionality.add(buttons);
        // dashed border under functionality panel
        functionality.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, -1, 5, -1), 
                                                                BorderFactory.createDashedBorder(null, 5, 5)));
        functionality.setVisible(true);
        sellPanel.add(functionality);
    }
    
    /**
     * Private method sets the update functionality interface with 3 sections for
     * text fields, buttons and messages. 
     */
    private void createUpdate() {
        investmentIndex = 0; // index for current investment, user is prompted to update
        updatePanel = new JPanel();
        updatePanel.setLayout(new GridLayout(2,1));
        JPanel functionality = new JPanel();
        functionality.setLayout(new GridLayout(1,2));
    
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(4,1));
        JLabel  title = new JLabel("Updating investments\n");
        fields.add(title);
    
        // symbol output panel
        // user can't edit this field without pressing next/prev
        JPanel symbol = new JPanel();
        symbol.setLayout(new FlowLayout());
        JLabel symbolLabel = new JLabel("Symbol");
        JTextField symbolInput = new JTextField();
        symbolInput.setEditable(false);
        symbolInput.setColumns(12);
        symbol.add(symbolLabel);
        symbol.add(symbolInput);
        fields.add(symbol);
    
        // name output panel, user can't edit this field without pressing next/prev
        JPanel name = new JPanel();
        name.setLayout(new FlowLayout());
        JLabel nameLabel = new JLabel("Name");
        JTextField nameInput = new JTextField();
        nameInput.setEditable(false);
        nameInput.setColumns(16);
        name.add(nameLabel);
        name.add(nameInput);
        name.setBorder(BorderFactory.createEmptyBorder(0,15,0,-25));
        fields.add(name);
    
        // price input panel
        JPanel price = new JPanel();
        price.setLayout(new FlowLayout());
        JLabel priceLabel = new JLabel("Price");
        JTextField priceInput = new JTextField();
        priceInput.setEditable(true);
        priceInput.setColumns(6);
        price.add(priceLabel);
        price.add(priceInput);
        price.setBorder(BorderFactory.createEmptyBorder(0,-50,0,20));
        fields.add(price);
        // dashed border to the right of the fields panel
        fields.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, -1, -1, 5), 
                                                            BorderFactory.createDashedBorder(null, 5, 5)));
        functionality.add(fields);
    
        // buttons section
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(3,1)); // 3 buttons

        // prev button
        JPanel prev = new JPanel();
        prev.setLayout(new FlowLayout());
        JButton prevButton = new JButton("Prev");
        // actionListener previews the name and symbol of the previous investment
        prevButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Investment> investments = myPortfolio.getArray();
                // fields are updated only if the current investment isn't the first
                if (investments.size() > 0) {
                    if (investmentIndex > 0) {
                        investmentIndex--;
                        clearFields(price); // clear price field
                    }

                    symbolInput.setText(investments.get(investmentIndex).getSymbol());
                    nameInput.setText(investments.get(investmentIndex).getName());
                }
            }
        });
        prev.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        prev.setMaximumSize(new Dimension(300,200));
        prev.add(prevButton);
        buttons.add(prev);
    
        // next button
        JPanel next = new JPanel();
        JButton nextButton = new JButton("Next");
        // actionListener previews the name and symbol of the next investment
        nextButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ArrayList<Investment> investments = myPortfolio.getArray();
                // fields are updated only if the current investment isn't the last
                if (investments.size() > 0) {
                    if (investmentIndex < investments.size() - 1) {
                        investmentIndex++;
                        clearFields(price);
                    }
                    symbolInput.setText(investments.get(investmentIndex).getSymbol());
                    nameInput.setText(investments.get(investmentIndex).getName());
                }
            }
        });
        next.add(nextButton);
        next.setMaximumSize(new Dimension(300,200));
        next.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        buttons.add(next);
        
        // save button
        JPanel save = new JPanel();
        JButton saveButton = new JButton("Save");
        // actionListener updates the price of the current investment 
        saveButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (myPortfolio.getArray().size() > 0)
                        myPortfolio.updatePortfolio(priceInput.getText(), investmentIndex);
                        setMessage(messagesPanel, myPortfolio.getArray().get(investmentIndex).toString());
                }
                catch (Exception exc) {
                    // exception message shown in the messages text area
                    setMessage(messagesPanel, exc.getMessage());
                }
            }
        });
        save.add(saveButton);
        save.setMaximumSize(new Dimension(300,200));
        save.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        buttons.add(save);

        functionality.add(buttons);
        functionality.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, -1, 5, -1), 
                                                                BorderFactory.createDashedBorder(null, 5, 5)));
        functionality.setVisible(true);
        updatePanel.add(functionality);
    
        prevButton.doClick();
    }

    /**
     * Private method sets the gain functionality interface with 2 sections for
     * text field and individual gains output. 
     */
    private void createGain() {
        gainPanel = new JPanel();
        // main panel split into 2 rows
        gainPanel.setLayout(new GridLayout(2,1));
        JPanel gainFunctionality = new JPanel();
        gainFunctionality.setLayout(new GridLayout(1,2));
    
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(2,1));
        JLabel  title = new JLabel("Getting total gain\n");
        fields.add(title);
    
        // gain field
        JPanel gain = new JPanel();
        gain.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel gainLabel = new JLabel("  Total gain");
        JTextField gainOutput = new JTextField();
        gainOutput.setEditable(false); // non-editable
        gainOutput.setColumns(15);
        gain.add(gainLabel);
        gain.add(gainOutput);
        fields.add(gain);

        gainFunctionality.add(fields);
        gainFunctionality.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, -1, 5, -1), 
                                                                BorderFactory.createDashedBorder(null, 5, 5)));
        gainPanel.add(gainFunctionality);
    }

    /**
     * Private method sets the search functionality interface with 3 sections for
     * text fields for user criteria, buttons and search results. 
     */
    private void createSearch() {
        searchPanel = new JPanel();
        searchPanel.setLayout(new GridLayout(2,1));
        JPanel functionality = new JPanel();
        functionality.setLayout(new GridLayout(1,2));
    
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(5,1));
        JLabel  title = new JLabel("Searching investments\n");
        fields.add(title);
    
        // symbol panel
        JPanel symbol = new JPanel();
        symbol.setLayout(new FlowLayout());
        JLabel symbolLabel = new JLabel("Symbol");
        JTextField symbolInput = new JTextField();
        symbolInput.setEditable(true); 
        symbolInput.setColumns(8);
        symbol.add(symbolLabel);
        symbol.add(symbolInput);
        symbol.setBorder(BorderFactory.createEmptyBorder(0,-2,0,0));
        fields.add(symbol);

        // name panel
        JPanel keywords = new JPanel();
        keywords.setLayout(new FlowLayout());
        JLabel keywordsLabel = new JLabel("<html>Name<br/>keywords</html>");
        JTextField keywordsInput = new JTextField();
        keywordsInput.setEditable(true);
        keywordsInput.setColumns(8);
        keywords.add(keywordsLabel);
        keywords.add(keywordsInput);
        keywords.setBorder(BorderFactory.createEmptyBorder(0,14,0,0));
        fields.add(keywords);
    
        // low price panel
        JPanel lowPrice = new JPanel();
        lowPrice.setLayout(new FlowLayout());
        JLabel lowPriceLabel = new JLabel("Low Price");
        JTextField lowPriceInput = new JTextField();
        lowPriceInput.setEditable(true);
        lowPriceInput.setColumns(6);
        lowPrice.add(lowPriceLabel);
        lowPrice.add(lowPriceInput);
        lowPrice.setBorder(BorderFactory.createEmptyBorder(0,-6,0,0));
        fields.add(lowPrice);

        // high price panel
        JPanel highPrice = new JPanel();
        highPrice.setLayout(new FlowLayout());
        JLabel highPriceLabel = new JLabel("High Price");
        JTextField highPriceInput = new JTextField();
        highPriceInput.setEditable(true);
        highPriceInput.setColumns(6);
        highPrice.add(highPriceLabel);
        highPrice.add(highPriceInput);
        highPrice.setBorder(BorderFactory.createEmptyBorder(0,-4,0,0));
        fields.add(highPrice);
        fields.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, -1, -1, 5), 
                                                            BorderFactory.createDashedBorder(null, 5, 5)));
    
        functionality.add(fields);
    
        //buttons panel
        JPanel buttons = new JPanel();
        // split into  2 buttons
        buttons.setLayout(new GridLayout(2,1));

        // reset button
        JPanel reset = new JPanel();
        reset.setLayout(new FlowLayout());
        JButton resetButton = new JButton("Reset");
        //actionListener clears all text fields in the searchPanel
        resetButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearFields(searchPanel);
            }
        });
        reset.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        reset.setMaximumSize(new Dimension(300,200));
        reset.add(resetButton);
        buttons.add(reset);
    
        // search button
        JPanel search = new JPanel();
        JButton searchButton = new JButton("Search");
        //actionListener displays results of search in messages text area, otherwise
        // it displays any exception that occurs
        searchButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    setMessage(messagesPanel, myPortfolio.search(symbolInput.getText().strip(), 
                    keywordsInput.getText().strip(), lowPriceInput.getText().strip(), 
                    highPriceInput.getText().strip()));
                }
                catch (Exception exc) {
                    setMessage(messagesPanel, exc.getMessage());
                }
            }
        });
        search.add(searchButton);
        search.setMaximumSize(new Dimension(300,200));
        search.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
        buttons.add(search);
        
        functionality.add(buttons);
        // dashed border under functionality panel
        functionality.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(-1, -1, 5, -1), 
                                                                BorderFactory.createDashedBorder(null, 5, 5)));
        functionality.setVisible(true);
        searchPanel.add(functionality);
    }

    /**
     * Private method sets the messagesPanel interface with a scrollable text area.
     * @param label String object containing the label of the text area.
     */
    private void createMessages(String label) {
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BorderLayout());
        
        // set label of text area
        JLabel messageLabel = new JLabel("   " + label);
        messagesPanel.add(messageLabel, BorderLayout.NORTH);
        
        JTextArea messageContent = new JTextArea(10, 200);
        messageContent.setEditable(false);

        JScrollPane messages = new JScrollPane(messageContent);
        messageContent.setVisible(true);
        // scroll bar always visible
        messages.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        messages.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messages.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10), 
                            BorderFactory.createLineBorder(Color.BLACK)));
        messagesPanel.add(messages, BorderLayout.CENTER);
    }

    /**
     * Private method appemd the text area(s) in a panel but the message String.
     * @param panel Component object searched for the required text area.
     * @param message String object containing the message desired in the text area
     */
    private void setMessage(Component panel, String message) {
        Component [] components;
        
        // get components inside panel if its a JPanel, JScrollPane or a JViewPort
        if (panel instanceof JPanel){
            components = ((JPanel)panel).getComponents();
        }
        else if (panel instanceof JScrollPane) {
            components = ((JScrollPane)panel).getComponents();
        }
        else if (panel instanceof JViewport) {
            components = ((JViewport)panel).getComponents();
        }
        else {
            return;
        }

        // iterates over all Components in panel
        for (Component i : components) {
            // recursively call setMessage on JPanel, JScrollPane and JViewPort
            if(i instanceof JPanel) {
                setMessage((JPanel)i, message);
            }
            else if (i instanceof JScrollPane) {
                setMessage((JScrollPane)i, message);
            }
            else if (i instanceof JViewport) {
                setMessage(((JViewport)i), message);
            }
            else if (i instanceof JTextArea) {
                ((JTextArea)i).append(message+"\n"); // append text to text area
                i.setVisible(true);
                return;
            }
        }
    }

    /**
     * Private method sets the gain field in gainPanel to the total gain on
     * investments in the myPortfolio Portfolio object. 
     * @param panel JPanel object searched for the required text field.
     */
    private void setGainField(JPanel panel) {
        Component [] components = panel.getComponents();

        // loops through all components in panel
        for (Component i : components) {
            if (i instanceof JTextField) {
                ((JTextField)i).setText(Double.toString(myPortfolio.getGain()));
            }
            else if (i instanceof JPanel) {
                // recursively call setGainField on JPanel objects within panel
                setGainField((JPanel)i);
            }
        }
    }

    /**
     * Private method clicks a button with text that matches label. 
     * @param panel JPanel object containing the button component
     * @param label String object containing the required text on the button to be clicked.
     */
    private void clickButton(JPanel panel, String label) {
        Component [] components = panel.getComponents();
        
        for (Component i : components) {
            if (i instanceof JButton && ((JButton)i).getText().equalsIgnoreCase(label)) {
                ((JButton)i).doClick();
                return;
            }
            else if (i instanceof JPanel){
                // recursively call clickButton on JPanel objects within panel
                clickButton(((JPanel)i), label);
            }
        }
    }
    
    /**
     * Private method clears all text fields. 
     * @param panel JPanel object containing the components to be cleared.
     */
    private static void clearFields(JComponent panel) {
        Component [] components;
        
        // get components inside panel if its a JPanel, JScrollPane or a JViewPort
        if (panel instanceof JPanel){
            components = ((JPanel)panel).getComponents();
        }
        else if (panel instanceof JScrollPane) {
            components = ((JScrollPane)panel).getComponents();
        }
        else if (panel instanceof JViewport) {
            components = ((JViewport)panel).getComponents();
        }
        else {
            return;
        }

        for (Component i : components) {
            // recursively call clearFields on JPanel components in panel
            if (i instanceof JPanel) {
                clearFields((JPanel) i);
            }
            else if (i instanceof JScrollPane) {
                clearFields((JScrollPane)i);
            }
            else if (i instanceof JViewport) {
                clearFields((JViewport)i);
            }
            else if (i instanceof JTextField) { // clear text fields
                ((JTextField)i).setText("");
            }
            else if(i instanceof JTextArea) {
                ((JTextArea)i).setText("");
            }
        }
    }

    /**
     * Method loads investments from a file to arrayList attribute of myPortfolio object
     * @param fileName String object containing the name of the file used for loading investments.
     */
    public void loadInvestments (String fileName) {
        try {
            myPortfolio.loadInvestments(fileName);
        }
        catch(Exception e) {
            // ignore exception if loading investmentsFailed (file will be overrident end of program)
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        GUI gui;

        // only loads investments from a file if it was given as a command line argument 
        if (args.length == 1) {
            gui = new GUI(args[0]);
        }
        else { 
            gui = new GUI(null);
        }
        gui.setVisible(true);
    }
}
