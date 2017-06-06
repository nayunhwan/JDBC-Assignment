import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by nayunhwan on 2017. 5. 27..
 */
public class TablePanel extends JPanel {
    JButton tables[] = new JButton[21];

    private OrderPanel orderPanel;
    private static Connection db;

    public TablePanel(Connection db, OrderPanel orderPanel) {
        this.db = db;
        this.orderPanel = orderPanel;
        this.setLayout(new GridLayout(4, 5));
        this.setBorder(new TitledBorder("테이블 현황"));
        for (int i = 1; i <= 20; i++) {
            tables[i] = new JButton(String.valueOf(i));
            checkTable(i);
            tables[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int tableID = Integer.parseInt(((JButton)e.getSource()).getText());
                    orderPanel.comboTableName.setSelectedItem(String.valueOf(tableID));
                    orderPanel.updateOrderList(tableID);
                }
            });
            this.add(tables[i]);
        }
    }

    public void checkTable(int n) {
        try {
            String sqlStr = "Select count(table_id) from order_table where table_id = " + n;
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();
            rs.next();

            int c = Integer.parseInt(rs.getString("count(table_id)"));
            if(c > 0) {
                tables[n].setBackground(Color.YELLOW);
            }
            else {
                tables[n].setBackground(Color.WHITE);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}

