import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class TabStaff extends JPanel {

    JLabel labelStaffName = new JLabel("직원명");
    JTextField inputStaffName = new JTextField();
    JButton btnAddStaff = new JButton("직원등록");
    JButton btnFindStaff = new JButton("조회");
    JTextArea tareaStaff = new JTextArea();

    TabStaff() {
        this.setLayout(null);
        labelStaffName.setBounds(15, 15, 100, 30);
        inputStaffName.setBounds(15, 50, 100, 30);
        btnAddStaff.setBounds(150, 50, 90, 30);
        btnFindStaff.setBounds(250, 50, 60, 30);
        tareaStaff.setBounds(15, 90, 300, 200);
        tareaStaff.setBorder(new LineBorder(Color.gray, 2));
        this.add(labelStaffName);
        this.add(inputStaffName);
        this.add(btnAddStaff);
        this.add(btnFindStaff);
        this.add(tareaStaff);
    }

}
