
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

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

    private static Connection db;
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
        btnOrder.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                orderSendToDB();
                tablePanel.checkTable(getTableID());
            }
        });
        btnCancel.setBounds(230, 220, 100, 30);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearOrder();
                tablePanel.checkTable(getTableID());
            }
        });
        btnPay.setBounds(230, 270, 100, 30);

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
        }
        catch (Exception e) {
            System.out.println("e");
        }
        orderList = new ArrayList<>();
        tareaOrder.setText("");
    }

    public void updateOrderView() {
        tareaOrder.setText("");
        for(Order order : orderList) {
            tareaOrder.setText(tareaOrder.getText() + order.getMenu() + "\t" + order.getPrice() + "\n");
        }
        tareaOrder.setText(tareaOrder.getText() + "\n\n\n---------------------------\n" + "총 합계\t" + getSum() + "\n");
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
            updateOrderView();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void orderSendToDB() {

        try {
            String sqlStr;
            PreparedStatement stmt = null;
            String tableID = (String) comboTableName.getSelectedItem();
            for(Order order : orderList) {
                sqlStr = "insert into ORDER_TABLE values (" +
                        "'" + tableID + "', " +
                        "'" + order.getMenu() + "', " +
                        "" + order.getPrice() + ")" ;
                stmt = db.prepareStatement(sqlStr);
                stmt.executeUpdate();
            }
            stmt.close();

        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void showTableStatus(int tableID) {
        try {
            String sqlStr = "select * from ORDER_TABLE where table table_ID = " + tableID;
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String resultStr = rs.getString("Table_ID") + "\t" + rs.getString("menu") + "\t" + rs.getString("price");
                System.out.println(resultStr);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
