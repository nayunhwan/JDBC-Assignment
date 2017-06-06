import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class TabMenu extends JPanel {

    JLabel labelMenuName = new JLabel("메뉴명");
    JTextField inputMenuName = new JTextField();
    JButton btnAddMenu = new JButton("메뉴 등록");
    JButton btnFindMenu = new JButton("조회");
    JTextArea tareaMenu = new JTextArea();

    private static Connection db;
    private MenuPanel menuPanel;

    private class AddMenuFrame extends JFrame {
        JLabel labelMenuName = new JLabel("메뉴명");
        JLabel labelPrice = new JLabel("가격");
        JTextField inputMenuName = new JTextField();
        JTextField inputPrice = new JTextField();

        JButton btnAdd = new JButton("등록");
        JButton btnCancel = new JButton("취소");

        AddMenuFrame() {
            this.setLayout(null);
            this.setTitle("메뉴등록");

            labelMenuName.setBounds(20, 20, 100, 30);
            labelPrice.setBounds(20, 70, 100, 30);
            inputMenuName.setBounds(130, 20, 100, 30);
            inputPrice.setBounds(130, 70, 100, 30);
            btnAdd.setBounds(20, 120, 100, 30);

            btnAdd.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    addMenu();
                    dispose();
                }
            });
            btnCancel.setBounds(130, 120, 100, 30);
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            this.add(labelMenuName);
            this.add(labelPrice);
            this.add(inputMenuName);
            this.add(inputPrice);
            this.add(btnAdd);
            this.add(btnCancel);

            this.setBounds(150, 150, 270, 200);
            this.setVisible(true);
        }


        private int getMenuCount() {
            String sqlStr = "Select Count(id) from menu";
            int n = 0;
            try {
                PreparedStatement stmt = db.prepareStatement(sqlStr);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                n = Integer.parseInt(rs.getString("count(id)"));
            }
            catch (Exception e) { System.out.println(e); }
            return n;
        }

        private void addMenu() {
            String name = inputMenuName.getText();
            String price = inputPrice.getText();
            // Limit for 20 Menu items
            if (menuPanel.getMenuCount() < 20) {
                // Limit overlap
                if (!menuPanel.getMenu().contains(name)) {
                    try {
                        int id = 1000 + getMenuCount();
                        String sqlStr = "insert into menu values (" +
                                "'" + name + "', " +
                                "" + id + ", " +
                                "" + price + ")";

                        PreparedStatement stmt = db.prepareStatement(sqlStr);
                        stmt.executeUpdate();
                        stmt.close();
                        JOptionPane.showMessageDialog(null, "등록되었습니다.");
                        menuPanel.updateMenu();
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }

                }
                else {
                    JOptionPane.showMessageDialog(null, "동일한 메뉴는 추가할 수 없습니다.");
                }
            }
            else {
                JOptionPane.showMessageDialog(null, "메뉴의 개수는 20개를 넘을 수 없습니다.");
            }
        }
    }

    TabMenu(Connection db, MenuPanel menuPanel) {
        this.db = db;
        this.menuPanel = menuPanel;

        this.setLayout(null);

        labelMenuName.setBounds(15, 15, 100, 30);
        inputMenuName.setBounds(15, 50, 120, 30);
        btnAddMenu.setBounds(150, 50, 90, 30);
        btnAddMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMenuFrame addMenuFrame = new AddMenuFrame();
            }
        });

        btnFindMenu.setBounds(250, 50, 60, 30);
        btnFindMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findMenu();
            }
        });
        tareaMenu.setBounds(15, 90, 300, 200);
        tareaMenu.setEditable(false);
        tareaMenu.setBorder(new LineBorder(Color.gray, 2));

        this.add(labelMenuName);
        this.add(inputMenuName);
        this.add(btnAddMenu);
        this.add(btnFindMenu);
        this.add(tareaMenu);
    }

    private void findMenu() {
        String find = inputMenuName.getText();
        String sqlStr = "Select name, price from menu where name = '" + find + "'";
        String resultStr = "";

        try {
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            resultStr += "메뉴명 : " + rs.getString("name") + "\n";
            resultStr += "가격 : " + rs.getString("price");
            tareaMenu.setText(resultStr);
        }
        catch (Exception e){
            System.out.println(e);
            tareaMenu.setText("검색 결과 없음");
        }

    }
}
