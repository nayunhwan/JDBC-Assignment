import com.sun.deploy.panel.JavaPanel;

import javax.swing.*;
import java.awt.MenuBar;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class Main extends JFrame {

    private JLabel title = new JLabel("식당 주문관리");
    private JPanel titlePanel = new JPanel();
    private JFrame frame = new JFrame();

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menu = new JMenu("Menu");
    private JMenuItem openItem = new JMenuItem("Open");
    private JMenuItem loginItem = new JMenuItem("Login");

    private JPanel panelGird = new JPanel(new GridLayout(2, 2));

    private TablePanel tablePanel;
    private OrderPanel orderPanel;
    private MenuPanel menuPanel;
    private RegisterPanel registerPanel;

    private static Connection db;
    private String username = "nayunhwan";
    private String password = "dbsghks0";

    private int staffId = 1000;

    private void connectDB(){
        try{
            // JDBC Driver Loading
            Class.forName("oracle.jdbc.OracleDriver");
            db = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", username, password);
            System.out.println("Success Connect Database");

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Fail Connect Database");
            System.out.println("SQLException: "+e);
        } catch (Exception e){
            System.out.println("Exception: "+e);
        }
    }

    private void createTable(){
        try {
            System.out.println("Create Table");
            String sqlStr = "create table staff (" +
                    "NAME varchar2(10) Not NULL," +
                    "ID number Not NULL," +
                    "GRADE varchar2(15) Not NULL)";

            Statement createStmt = db.createStatement();
            createStmt.executeUpdate(sqlStr);

            sqlStr = "create table customer (" +
                    "NAME varchar2(10) Not NULL," +
                    "BIRTHDAY varchar2(10) Not NULL," +
                    "CONTACT number Not NULL," +
                    "GRADE varchar2(20) Not NULL)";
            createStmt = db.createStatement();
            createStmt.executeUpdate(sqlStr);

            sqlStr = "create table menu (" +
                    "NAME varchar2(30) Not NULL," +
                    "PRICE number Not NULL)";
            createStmt = db.createStatement();
            createStmt.executeUpdate(sqlStr);

            createStmt.close();
        }
        catch (Exception e){
            System.out.println(e);
        }
        finally {
            System.out.println("DONE: Create Table");
        }
    }


    private void dropTable() {
        try {
            System.out.println("Drop Table");
            String sqlStr = "drop table staff";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            stmt.executeUpdate();

            sqlStr = "drop table customer";
            stmt = db.prepareStatement(sqlStr);
            stmt.executeUpdate();

            sqlStr = "drop table menu";
            stmt = db.prepareStatement(sqlStr);
            stmt.executeUpdate();

            stmt.close();
            System.out.println("DONE: Drop Table");
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }


    private void insert(String table, String[] dataArr) {
        System.out.println("Insert Data to " + table.toUpperCase() + "::" + String.join(", ", dataArr));
        try {
            String sqlStr;
            PreparedStatement stmt;
            if(table.toLowerCase().equals("staff")) {

                sqlStr = "insert into staff values (" +
                        "'" + dataArr[0] + "', " +
                        "" + staffId++ + ", " +
                        "'" + dataArr[1] + "')";
                stmt = db.prepareStatement(sqlStr);
                stmt.executeUpdate();
                stmt.close();
            }
            else if(table.toLowerCase().equals("customer")) {
                sqlStr = "insert into customer values (" +
                        "'" + dataArr[0] + "', " +
                        "'" + dataArr[1] + "', " +
                        "" + dataArr[2] + ", " +
                        "'" + dataArr[3] + "')";
                stmt = db.prepareStatement(sqlStr);
                stmt.executeUpdate();
                stmt.close();
            }
            else if(table.toLowerCase().equals("menu")) {
                sqlStr = "insert into menu values (" +
                        "'" + dataArr[0] + "', " +
                        "" + dataArr[1] + ")";
                stmt = db.prepareStatement(sqlStr);
                stmt.executeUpdate();
                stmt.close();
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }


    public Main() {
        connectDB();
        tablePanel = new TablePanel(db);
        orderPanel = new OrderPanel(db);
        menuPanel = new MenuPanel(db);
        registerPanel = new RegisterPanel(db, menuPanel);

        this.setLayout(new BorderLayout());
        openItem.setMnemonic('O');

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dropTable();
                createTable();
                JFileChooser filechooser = new JFileChooser();
                int ret = filechooser.showOpenDialog(null);

                if (ret == JFileChooser.APPROVE_OPTION) {
                    try (BufferedReader br = new BufferedReader(new FileReader(filechooser.getSelectedFile()))) {
                        String text = null;

                        text = br.readLine();
                        int n = Integer.parseInt(text);

                        // Get Data of Customer
                        for (int i = 0; i < n; i++) {
                            text = br.readLine();
                            String dataArr[] = text.split("\t");
                            insert("customer", dataArr);
                        }

                        text = br.readLine();
                        n = Integer.parseInt(text);
                        // Get Data of Staff
                        for(int i = 0; i < n; i++) {
                            text = br.readLine();
                            String dataArr[] = text.split("\t");
                            insert("staff", dataArr);
                        }

                        text = br.readLine();
                        n = Integer.parseInt(text);
                        // Get Data of Menu
                        for(int i = 0; i < n; i++) {
                            text = br.readLine();
                            String dataArr[] = text.split("\t");
                            insert("menu", dataArr);
                        }

                        menuPanel.updateMenu();
                    } catch (Exception err) {
                        System.out.println("Error: " + err);
                    }
                }
            }
        });

        loginItem.setMnemonic('L');

        menu.add(openItem);
        menu.add(loginItem);
        menuBar.add(menu);

        // Title Panel
        title.setFont(title.getFont().deriveFont(32.0f));
        titlePanel.add(title);
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new LineBorder(Color.black, 3));
        this.add(titlePanel, BorderLayout.NORTH);

        // Table Panel
        panelGird.add(tablePanel);
        // Order Panel
        panelGird.add(orderPanel);
        // Menu Panel
        panelGird.add(menuPanel);
        // Register Panel
        panelGird.add(registerPanel);

        this.add(panelGird, BorderLayout.CENTER);

        this.setJMenuBar(menuBar);

        setSize(700, 850);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {

        Main m = new Main();
//        Login login = new Login();
    }
}
