import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class TabMenu extends JPanel {

    JLabel labelMenuName = new JLabel("메뉴명");
    JTextField inputMenuName = new JTextField();
    JButton btnAddMenu = new JButton("메뉴 등록");
    JButton btnFindMenu = new JButton("조회");
    JTextArea tareaMenu = new JTextArea();

    TabMenu() {
        this.setLayout(null);

        labelMenuName.setBounds(15, 15, 100, 30);
        inputMenuName.setBounds(15, 50, 120, 30);
        btnAddMenu.setBounds(150, 50, 90, 30);
        btnFindMenu.setBounds(250, 50, 60, 30);
        tareaMenu.setBounds(15, 90, 300, 200);
        tareaMenu.setBorder(new LineBorder(Color.gray, 2));

        this.add(labelMenuName);
        this.add(inputMenuName);
        this.add(btnAddMenu);
        this.add(btnFindMenu);
        this.add(tareaMenu);
    }
}
