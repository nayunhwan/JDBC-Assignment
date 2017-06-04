import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class MenuPanel extends JPanel {
    JButton menus[] = new JButton[20];

    MenuPanel() {
        this.setBorder(new TitledBorder("메뉴"));
        this.setLayout(new GridLayout(10, 2));
        for (int i = 0; i < 20; i++) {
            menus[i] = new JButton();
            this.add(menus[i]);
        }
    }
}
