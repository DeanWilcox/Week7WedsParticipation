/**
 * Main - Dean Wilcox - 6 December 2023
 */

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scraper extends JFrame {
  
    JTextField urlTextField;
    DefaultTableModel tableModel;
    JTable jTable;
    JComboBox<String> regexComboBox;
    JButton btn;
    HashSet<String> matches = new HashSet<String>();
    JButton resetButton;

    public Scraper() {
        super("Scrape the application");

        setLayout(new BorderLayout());

        urlTextField = new JTextField("Enter URL");
        add(urlTextField, BorderLayout.NORTH);

        // String columns[] = {"ID", "NAME", "SALARY"};
        // String data[][] = {{"1", "Dean", "65000"}, {"2", "Alan", "63000"}, {"3", "James", "45000"}};

        tableModel = new DefaultTableModel();
        jTable = new JTable(tableModel);

        tableModel.addColumn("Line #");
        tableModel.addColumn("RESULT");

        JScrollPane scrollPane = new JScrollPane(jTable);
        add(scrollPane);

        JPanel southJPanel = new JPanel();

        regexComboBox = new JComboBox<String>();
        regexComboBox.addItem("\\d{3}\\-\\d{3}\\-\\d{4}");
        regexComboBox.addItem("[0-9]");
        regexComboBox.addItem("[A-Za-z0-9\\.]+\\@[A-Za-z0-9]+\\.[A-Za-z0-9]+");

        //regexTextField = new JTextField("Enter a regex", 35);
        southJPanel.add(regexComboBox);

        btn = new JButton("Click me!!");
        btn.addActionListener(this::SearchPage);
        //btn.addActionListener(e -> SearchPage(event)); same as the above line
        resetButton = new JButton("Reset");

        southJPanel.add(btn);

        add(southJPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 700);
        setVisible(true);
        setLocation(300, 100);
    }

    public void Reset(ActionEvent e) {
        tableModel.setRowCount(0);
        matches.clear();
    }

    public void SearchPage(ActionEvent e) {
        tableModel.setRowCount(0);

        try{
        URL url = new URL(urlTextField.getText());

        URLConnection urlConnection = url.openConnection();
        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader streamReader = new InputStreamReader(inputStream);

        BufferedReader bufferedReader = new BufferedReader(streamReader);

        String line = null;

        while((line = bufferedReader.readLine()) != null){
            // regex pattern matching
            Pattern pattern = Pattern.compile(regexComboBox.getSelectedItem().toString());
            Matcher match = pattern.matcher(line);
            //add to our table

            if(match.find()){
                
                if(matches.contains(match.group())){
                    //do nothing
                } else {
                    var grouping = match.group();
                    matches.add(match.group());
                    tableModel.addRow(new Object[]{String.valueOf(tableModel.getRowCount() + 1), match.group()});
                }
                
            }
        }

        } catch(Exception exception){

        }

    }
}
