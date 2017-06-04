import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class TabSales extends JPanel {

    JLabel labelPeriod = new JLabel("기간");
    JComboBox<String> comboDate = new JComboBox<String>();
    JTextArea tareaSales = new JTextArea();

    TabSales() {
        this.setLayout(null);
        labelPeriod.setBounds(15, 15, 100, 30);
        comboDate.setBounds(150, 15, 100, 30);
        tareaSales.setBounds(15, 50, 300, 240);
        tareaSales.setBorder(new LineBorder(Color.gray, 2));

        this.add(labelPeriod);
        this.add(comboDate);
        this.add(tareaSales);
    }
}
