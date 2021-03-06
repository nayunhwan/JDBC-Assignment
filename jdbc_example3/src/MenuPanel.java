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
public class MenuPanel extends JPanel {
    JButton menus[] = new JButton[20];

    private ArrayList<String> list;
    private OrderPanel orderPanel;
    private LoginStatus loginStatus = null;
    private static Connection db;

    MenuPanel(Connection db, OrderPanel orderPanel) {
        this.db = db;
        this.orderPanel = orderPanel;
        this.setBorder(new TitledBorder("메뉴"));
        this.setLayout(new GridLayout(10, 2));
        for (int i = 0; i < 20; i++) {
            menus[i] = new JButton();
            menus[i].setEnabled(false);
            menus[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    orderPanel.addOrder(((JButton) e.getSource()).getText());
                }
            });
            this.add(menus[i]);
        }
        updateMenu();
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
        if(loginStatus != null) {
            for(JButton menu : menus) {
                if(!menu.getText().equals("")) menu.setEnabled(true);
            }
        }
        else {
            for(JButton menu : menus) {
                menu.setEnabled(false);
            }
        }
    }

    public ArrayList<String> getMenu() {
        list = new ArrayList<>();
        try {
            String sqlStr;
            PreparedStatement stmt;

            sqlStr = "select name from menu";
            stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                list.add(rs.getString("name"));
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    public int getMenuCount() {
        return getMenu().size();
    }

    public void updateMenu() {
        list = getMenu();
        for (int i = 0; i < list.size(); i++) {
            menus[i].setText(list.get(i));
            if(loginStatus != null) menus[i].setEnabled(true);
        }
        for (int i = list.size(); i < 20; i++) {
            menus[i].setText("");
            menus[i].setEnabled(false);
        }
    }
}
