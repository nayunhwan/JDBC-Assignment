
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class OrderPanel extends JPanel {
    JScrollPane scrollPane = new JScrollPane();
    JTextArea tareaOrder = new JTextArea();
    JLabel labelCustomerName = new JLabel("고객명");
    JTextField inputCustomerName = new JTextField();
    JLabel labelTableName = new JLabel("테이블명");
    JComboBox<String> comboTableName = new JComboBox<>();
    JButton btnOrder = new JButton("주문");
    JButton btnCancle = new JButton("취소");
    JButton btnPay = new JButton("결제");

    OrderPanel() {

        this.setBorder(new TitledBorder("주문 내역"));
        this.setLayout(null);
        tareaOrder.setEditable(false);
        scrollPane.setViewportView(tareaOrder);

        for (int i = 0; i < 20; i++) {
            comboTableName.addItem((i + 1) +"");
        }

        scrollPane.setBounds(15, 18, 200, 330);
        labelCustomerName.setBounds(230, 15, 100, 30);
        inputCustomerName.setBounds(230, 50, 100, 30);
        labelTableName.setBounds(230, 85, 100, 30);
        comboTableName.setBounds(230, 110, 100, 30);
        btnOrder.setBounds(230, 170, 100, 30);
        btnCancle.setBounds(230, 220, 100, 30);
        btnPay.setBounds(230, 270, 100, 30);

        this.add(scrollPane);
        this.add(labelTableName);
        this.add(inputCustomerName);
        this.add(labelTableName);
        this.add(comboTableName);
        this.add(btnOrder);
        this.add(btnCancle);
        this.add(btnPay);
    }
}
