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
    private static Connection db;


    MenuPanel(Connection db, OrderPanel orderPanel) {
        this.db = db;
        this.orderPanel = orderPanel;

        this.setBorder(new TitledBorder("메뉴"));
        this.setLayout(new GridLayout(10, 2));
        for (int i = 0; i < 20; i++) {
            menus[i] = new JButton();
            this.add(menus[i]);
        }
        updateMenu();
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
            menus[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    orderPanel.addOrder(((JButton) e.getSource()).getText());
                }
            });
        }
        for (int i = list.size(); i < 20; i++) {
            menus[i].setText("");
        }
    }
}
