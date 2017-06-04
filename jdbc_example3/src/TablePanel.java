import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by nayunhwan on 2017. 5. 27..
 */
public class TablePanel extends JPanel {
    JButton tables[] = new JButton[20];

    public TablePanel() {
        this.setLayout(new GridLayout(4, 5));
        this.setBorder(new TitledBorder("테이블 현황"));
        for (int i = 0; i < 20; i++) {
            tables[i] = new JButton(String.valueOf(i + 1));
            tables[i].setBackground(Color.WHITE);
            this.add(tables[i]);
        }
    }
}

