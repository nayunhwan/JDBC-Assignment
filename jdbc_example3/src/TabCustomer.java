import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class TabCustomer extends JPanel {

    JLabel labelCustomerName = new JLabel("고객명");
    JTextField inputCustomerName = new JTextField();
    JTextArea tareaCustomer = new JTextArea();
    JButton btnSign = new JButton("가입");
    JButton btnFind = new JButton("조회");

    TabCustomer() {
        this.setLayout(null);
        labelCustomerName.setBounds(15, 15, 100, 30);
        inputCustomerName.setBounds(15, 50, 100, 30);
        tareaCustomer.setBounds(15, 90, 300, 200);
        tareaCustomer.setBorder(new LineBorder(Color.gray, 2));

        btnSign.setBounds(180, 50, 60, 30);
        btnFind.setBounds(250, 50, 60, 30);

        this.add(labelCustomerName);
        this.add(inputCustomerName);
        this.add(btnSign);
        this.add(btnFind);
        this.add(tareaCustomer);
    }
}