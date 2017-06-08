import com.sun.deploy.panel.JavaPanel;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import sun.rmi.runtime.Log;

import javax.net.ssl.SSLContext;
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

    private int staffID = 1000;
    private int customerID = 1000;
    private int menuID = 1000;

    private LoginStatus loginStatus = null;

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
            System.out.println("Create STAFF TABLE");
            String sqlStr = "create table staff (" +
                    "NAME varchar2(10) Not NULL," +
                    "ID number Not NULL," +
                    "GRADE varchar2(15) Not NULL," +
                    "SALES number Default 0)";

            Statement createStmt = db.createStatement();
            createStmt.executeUpdate(sqlStr);

            System.out.println("Create CUSTOMER TABLE");
            sqlStr = "create table customer (" +
                    "NAME varchar2(10) Not NULL," +
                    "ID number Not NULL," +
                    "BIRTHDAY varchar2(10) Not NULL," +
                    "CONTACT number Not NULL," +
                    "GRADE varchar2(20) Not NULL," +
                    "SALES number Default 0)";
            createStmt = db.createStatement();
            createStmt.executeUpdate(sqlStr);

            System.out.println("Create MENU TABLE");
            sqlStr = "create table menu (" +
                    "NAME varchar2(50) Not NULL," +
                    "ID number Not NULL," +
                    "PRICE number Not NULL)";
            createStmt = db.createStatement();
            createStmt.executeUpdate(sqlStr);

            System.out.println("Create ORDER TABLE");
            sqlStr = "create table order_table (" +
                    "TABLE_ID number Not NULL," +
                    "MENU varchar2(50) Not NULL," +
                    "PRICE number Not NULL)";
            createStmt = db.createStatement();
            createStmt.executeUpdate(sqlStr);

            System.out.println("Create SALES TABLE");
            sqlStr = "create table sales (" +
                    "ORDER_DATE date Not NULL," +
                    "MENU varchar2(50) Not NULL," +
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

            sqlStr = "drop table order_table";
            stmt = db.prepareStatement(sqlStr);
            stmt.executeUpdate();

            sqlStr = "drop table sales";
            stmt = db.prepareStatement(sqlStr);
            stmt.executeUpdate();

            stmt.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }
        finally {
            System.out.println("DONE: Drop Table");
        }
    }


    private void insert(String table, String[] dataArr) {

        System.out.println("Insert Data to " + table.toUpperCase() + "::" + String.join(", ", dataArr));
        try {
            String sqlStr;
            PreparedStatement stmt;
            if(table.toLowerCase().equals("staff")) {

                sqlStr = "insert into staff(name, id, grade) values (" +
                        "'" + dataArr[0] + "', " +
                        "" + staffID++ + ", " +
                        "'" + dataArr[1] + "') ";
                stmt = db.prepareStatement(sqlStr);
                stmt.executeUpdate();
                stmt.close();
            }
            else if(table.toLowerCase().equals("customer")) {

                sqlStr = "insert into customer values (" +
                        "'" + dataArr[0] + "', " +
                        "" + customerID++ + ", " +
                        "'" + dataArr[1] + "', " +
                        "" + dataArr[2] + ", " +
                        "'" + dataArr[3] + "', ";

                int initSales = 0;
                if(dataArr[3].toLowerCase().equals("gold")) initSales = 1000000;
                else if(dataArr[3].toLowerCase().equals("silver")) initSales = 500000;
                else if(dataArr[3].toLowerCase().equals("bronze")) initSales = 300000;

                sqlStr += initSales + ")";

                stmt = db.prepareStatement(sqlStr);
                stmt.executeUpdate();
                stmt.close();
            }
            else if(table.toLowerCase().equals("menu")) {
                sqlStr = "insert into menu values (" +
                        "'" + dataArr[0] + "', " +
                        "" + menuID++ + ", " +
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

    public void init() {
        orderPanel = new OrderPanel(db);
        tablePanel = new TablePanel(db, orderPanel);
        menuPanel = new MenuPanel(db, orderPanel);
        registerPanel = new RegisterPanel(db, menuPanel);
        orderPanel.setTablePanel(tablePanel);
        orderPanel.setTabSales(registerPanel.getTabSales());
    }

    public Main() {
        connectDB();
        init();

        this.setLayout(new BorderLayout());
        openItem.setMnemonic('O');

        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                staffID = 1000;
                customerID = 1000;
                menuID = 1000;
                dropTable();
                createTable();
                loginStatus = null;
                updateLoginStatus(null);
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

                        for(int i = 0; i < 20; i++) {
                            tablePanel.checkTable(i+1);
                        }
                        orderPanel.clearOrder();
                        menuPanel.updateMenu();
                    } catch (Exception err) {
                        System.out.println("Error: " + err);
                    }
                }
            }
        });

        loginItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login(db);
                login.btnLogin.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LoginStatus newloginStatus = login.getloginStatus();
                        if(newloginStatus != null) {
                            System.out.println(newloginStatus.getGrade());
                            updateLoginStatus(newloginStatus);
                            JOptionPane.showMessageDialog(null, newloginStatus.getName() + "님으로 로그인되었습니다.");
                            login.dispose();
                        }
                        else {
                            JOptionPane.showMessageDialog(null, "로그인 실패");
                        }

                    }
                });
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

    public void updateLoginStatus(LoginStatus loginStatus) {
        if(loginStatus == null) System.out.println("NULL");
        else {
            System.out.println("Login" + loginStatus.getName() + loginStatus.getGrade());
        }
        this.loginStatus = loginStatus;
//        System.out.println(loginStatus.getGrade());
        tablePanel.setLoginStatus(loginStatus);
        orderPanel.setLoginStatus(loginStatus);
        menuPanel.setLoginStatus(loginStatus);
        registerPanel.setLoginStatus(loginStatus);
    }
    public static void main(String[] args) {

        Main m = new Main();
//        Login login = new Login();
    }
}
