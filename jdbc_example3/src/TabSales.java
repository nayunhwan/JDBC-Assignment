import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.interfaces.RSAKey;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class TabSales extends JPanel {

    JScrollPane scrollPane = new JScrollPane();
    JLabel labelPeriod = new JLabel("기간");
    JComboBox<String> comboDate = new JComboBox<String>();
    JTextArea tareaSales = new JTextArea();

    private static Connection db;
    private LoginStatus loginStatus = null;
    private ArrayList<String> dateList = new ArrayList<>();

    TabSales(Connection db) {
        this.db = db;
        this.setLayout(null);
        tareaSales.setEditable(false);
        scrollPane.setViewportView(tareaSales);
        updateDateList();
        labelPeriod.setBounds(15, 15, 100, 30);
        comboDate.setBounds(150, 15, 100, 30);

        comboDate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateSalesView();
            }
        });
        updateComboBoxEnable();
//        comboDate.setEnabled(false);
        scrollPane.setBounds(15, 50, 300, 240);
        scrollPane.setBorder(new LineBorder(Color.gray, 2));
        updateSalesView();


        this.add(labelPeriod);
        this.add(comboDate);
        this.add(scrollPane);
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
        updateComboBoxEnable();
        updateSalesView();
    }

    public void updateComboBoxEnable() {
        if(loginStatus != null) {
            boolean isSupervisor = loginStatus.getGrade().toLowerCase().equals("supervisor");
            comboDate.setEnabled(isSupervisor);
            if(!isSupervisor) tareaSales.setText("");
        }
        else {
            comboDate.setEnabled(false);
            tareaSales.setText("");
        }
    }

    public void updateDateList() {
        comboDate.removeAllItems();
        try {
            String sqlStr = "SELECT DISTINCT Order_date from sales";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comboDate.addItem(rs.getString("Order_date").substring(0, 10));
                System.out.println(rs.getString("Order_date").substring(0, 10));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

    private int getDateSum(String date) {
        int sum = 0;
        try {
            String sqlStr = "SELECT sum(price) FROM sales WHERE order_date = '" + date + "'";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            sum = rs.getInt("sum(price)");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return sum;
    }

    private int getDuringSum(String date) {
        int sum = 0;
        try {
            String sqlStr = "SELECT sum(price) FROM sales WHERE Order_date <= '" + date + "'";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            sum = rs.getInt("sum(price)");
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return sum;
    }


    private ArrayList<String> getSellerList(String selector) {
        ArrayList<String> list = new ArrayList<>();
        if(!(selector.toLowerCase().equals("max") || selector.toLowerCase().equals("min"))) return list;
        try {
            String sqlStr = "SELECT menu from sales Group by menu having count(menu) = (" +
                    "select " + selector +"(count(menu)) from sales group by menu)";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                list.add(rs.getString("menu"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }


    public void updateSalesView() {

        if(loginStatus != null && loginStatus.getGrade().toLowerCase().equals("supervisor")) {
            updateDateList();
            String resultStr = "";
            String date = (String) comboDate.getSelectedItem();
            int dateSum = getDateSum(date);
            ArrayList<String> bestSellerList = getSellerList("max");

            resultStr += "일 매출 : " + dateSum + "\n";
            resultStr += "----------------------------------------\n";
            resultStr += "가장 많이 팔린 메뉴\n";

            for (String menu : bestSellerList) {
                resultStr += menu + "\n";
            }

            resultStr += "\n";

            ArrayList<String> worstSellerList = getSellerList("min");
            resultStr += "가장 적게 팔린 메뉴\n";

            for (String menu : worstSellerList) {
                resultStr += menu + "\n";
            }

            resultStr += "----------------------------------------\n";
            int totalSum = getDuringSum(date);
            resultStr += "누적 매출 : " + totalSum + "\n";

            tareaSales.setText(resultStr);
        }
    }
}
