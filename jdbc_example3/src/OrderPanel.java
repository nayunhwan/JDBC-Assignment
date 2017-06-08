
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class OrderPanel extends JPanel {
    JScrollPane scrollPane = new JScrollPane();
    JTextArea tareaOrder = new JTextArea();
    JLabel labelCustomerName = new JLabel("고객명");
    JTextField inputCustomerName = new JTextField();
    JLabel labelTableName = new JLabel("테이블명");
    JComboBox<String> comboTableName = new JComboBox<>();
    JButton btnOrder = new JButton("주문");
    JButton btnCancel = new JButton("취소");
    JButton btnPay = new JButton("결제");


    private TablePanel tablePanel;
    private TabSales tabSales;

    private static Connection db;
    private LoginStatus loginStatus = null;
    private ArrayList<Order> orderList = new ArrayList<>();

    private class Order {
        private String menu;
        private int price;

        Order(String menu, int price) {
            this.menu = menu;
            this.price = price;
        }

        public String getMenu() {
            return this.menu;
        }
        public int getPrice() {
            return this.price;
        }
    }

    OrderPanel(Connection db) {
        this.db = db;
        this.setBorder(new TitledBorder("주문 내역"));
        this.setLayout(null);
        tareaOrder.setEditable(false);
        scrollPane.setViewportView(tareaOrder);

        for (int i = 0; i < 20; i++) {
            comboTableName.addItem((i + 1) +"");
        }
        setLoginStatus(loginStatus);
        scrollPane.setBounds(15, 18, 200, 330);
        labelCustomerName.setBounds(230, 15, 100, 30);
        inputCustomerName.setBounds(230, 50, 100, 30);
        labelTableName.setBounds(230, 85, 100, 30);
        comboTableName.setBounds(230, 110, 100, 30);
        comboTableName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int tableID = Integer.parseInt((String)((JComboBox)e.getSource()).getSelectedItem());

                updateOrderList(tableID);
            }
        });
        btnOrder.setBounds(230, 170, 100, 30);
        btnOrder.setEnabled(false);
        btnOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderSendToDB();

            }
        });
        btnCancel.setBounds(230, 220, 100, 30);
        btnCancel.setEnabled(false);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearOrder();

            }
        });
        btnPay.setBounds(230, 270, 100, 30);
        btnPay.setEnabled(false);
        btnPay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pay();
                tabSales.updateSalesView();
            }
        });

        this.add(scrollPane);
        this.add(labelTableName);
        this.add(inputCustomerName);
        this.add(labelTableName);
        this.add(comboTableName);
        this.add(btnOrder);
        this.add(btnCancel);
        this.add(btnPay);
    }

    public void setTablePanel(TablePanel tablePanel) {
        this.tablePanel = tablePanel;
    }
    public void setTabSales(TabSales tabSales) {
        this.tabSales = tabSales;
    }
    public void setLoginStatus(LoginStatus loginStatus){
        this.loginStatus = loginStatus;
        if(loginStatus != null) {
            inputCustomerName.setEnabled(true);
            comboTableName.setEnabled(true);
            btnOrder.setEnabled(true);
        }
        else {
            inputCustomerName.setEnabled(false);
            comboTableName.setEnabled(false);
            btnOrder.setEnabled(false);
        }
        updateBtnEnable();
    }

    public int getSum() {
        int sum = 0;
        for(Order order : orderList) {
            sum += order.getPrice();
        }
        return sum;
    }

    public int getTableID() {
        return Integer.parseInt((String)comboTableName.getSelectedItem());
    }

    public void updateBtnEnable() {
        if(orderList.size() > 0) {
            btnCancel.setEnabled(true);
            btnPay.setEnabled(true);
        }
        else {
            btnCancel.setEnabled(false);
            btnPay.setEnabled(false);
        }
    }

    public void addOrder(String menu) {
        try {

            String sqlStr = "select price from menu where name = '" + menu + "'";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            int price = Integer.parseInt(rs.getString("price"));
            orderList.add(new Order(menu, price));
            stmt.close();

            updateOrderView();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void clearOrder() {
        int tableId = getTableID();
        try {
            String sqlStr = "DELETE FROM ORDER_TABLE where table_id = " + tableId;
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            stmt.executeUpdate();
            stmt.close();
            orderList = new ArrayList<>();
            updateBtnEnable();
            tareaOrder.setText("");
            tablePanel.checkTable(getTableID());
        }
        catch (Exception e) {
            System.out.println("e");
        }

    }

    public void updateOrderView() {
        tareaOrder.setText("");
        for(Order order : orderList) {
            tareaOrder.setText(tareaOrder.getText() + order.getMenu() + "\t" + order.getPrice() + "\n");
        }
        int sum = getSum();
        if(sum > 0) {
            tareaOrder.setText(tareaOrder.getText() + "\n\n\n----------------------------------\n" + "총 합계\t" + sum + "\n");
        }
    }

    public void updateOrderList(int tableID) {
        orderList = new ArrayList<>();

        try {
            String sqlStr = "select menu, price from ORDER_TABLE where table_id = " + tableID;
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String menu = rs.getString("menu");
                int price = Integer.parseInt(rs.getString("price"));
                orderList.add(new Order(menu, price));
            }
            stmt.close();
            updateOrderView();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getOrderCount() {
        int n = 0;
        try {
            String sqlStr = "Select count(table_id) From ORDER_TABLE Where table_id = " + getTableID();
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            n = rs.getInt("count(table_id)");
            stmt.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return n;
    }

    public void orderSendToDB() {
        try {

            int n = getOrderCount();
            if(n == orderList.size()){
                JOptionPane.showMessageDialog(null, "새롭게 추가된 메뉴가 없습니다.");
            }
            else {
                String sqlStr;
                PreparedStatement stmt = null;
                int tableID = getTableID();
                for(int i = n; i < orderList.size(); i++) {
                    Order order = orderList.get(i);
                    sqlStr = "insert into ORDER_TABLE values (" +
                            "" + tableID + ", " +
                            "'" + order.getMenu() + "', " +
                            "" + order.getPrice() + ")" ;
                    stmt = db.prepareStatement(sqlStr);
                    stmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(null, "주문되었습니다.");
                stmt.close();
                updateBtnEnable();
                tablePanel.checkTable(getTableID());

            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getCustomerGrade(String name) {
        String grade = null;
        try {
            String sqlStr = "SELECT Grade FROM Customer Where name = '" + name + "'";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            grade = rs.getString("Grade");
            stmt.close();
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return grade;
    }

    public double discountPercent(String name) {
        String grade = getCustomerGrade(name);
        double p = 1;
        if(grade.toLowerCase().equals("gold")) { p = 0.7; }
        else if(grade.toLowerCase().equals("silver")) { p = 0.8; }
        else if(grade.toLowerCase().equals("bronze")) { p = 0.9; }
        return p;
    }

    public boolean isCustomer(String name) {
        boolean result = false;
        try {
            String sqlStr = "SELECT Count(name) FROM Customer WHERE name = '" + name + "'";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            if(rs.getInt("Count(name)") != 0) result = true;
        }
        catch (Exception e) {

            System.out.println("isCustomer" + e);
        }
        return result;
    }

    public void pay() {
        try {
            String customerName = inputCustomerName.getText();
            String sqlStr;
            double p = 1;
            PreparedStatement stmt = null;
            int sum = getSum();
            if(!customerName.equals("") && isCustomer(customerName)) {
                p = discountPercent(customerName);
                sum = Math.round((long)(getSum() * p));
                sqlStr = "UPDATE Customer SET SALES = SALES + " + sum;
                stmt = db.prepareStatement(sqlStr);
                stmt.executeUpdate();
            }
            if(loginStatus != null) {
                sqlStr = "UPDATE Staff SET SALES = SALES + " + sum;
                stmt = db.prepareStatement(sqlStr);
                stmt.executeUpdate();
            }
            if(customerName.equals("") || (!customerName.equals("") && isCustomer(customerName))) {
                String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                for (Order order : orderList) {
                    sqlStr = "INSERT into SALES values (" +
                            "to_Date('" + date + "', 'yyyy-mm-dd'), " +
                            "'" + order.getMenu() + "', " +
                            "" + Math.round((long) (order.getPrice() * p)) + ")";
                    stmt = db.prepareStatement(sqlStr);
                    stmt.executeUpdate();
                }
                stmt.close();
                JOptionPane.showMessageDialog(null, "결제되었습니다.");
                clearOrder();
                updateBtnEnable();
            }
            else {
                JOptionPane.showMessageDialog(null, "이름을 확인해 주세요.");
            }


        }
        catch (Exception e) {
            System.out.println("Pay: " + e);
        }
    }

}
