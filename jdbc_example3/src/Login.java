import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by nayunhwan on 2017. 5. 27..
 */
public class Login extends JFrame{
    JLabel labelName = new JLabel("이름");
    JLabel labelStaffNo = new JLabel("사원번호");

    JTextField inputName = new JTextField();
    JTextField inputStaffNo = new JTextField();

    JButton btnLogin = new JButton("로그인");

    private static Connection db;

    public Login(Connection db){
        this.db = db;
        this.setLayout(null);

        labelName.setBounds(15, 15, 100, 30);
        labelStaffNo.setBounds(15, 50, 100, 30);
        inputName.setBounds(100, 15, 100, 30);
        inputName.setText("박수진");
        inputStaffNo.setBounds(100, 50, 100, 30);
        inputStaffNo.setText("1002");
        btnLogin.setBounds(220, 30, 100, 30);

        this.add(labelName);
        this.add(labelStaffNo);
        this.add(inputName);
        this.add(inputStaffNo);
        this.add(btnLogin);

        this.setBounds(100, 100, 350, 130);
        this.setTitle("사원 로그인");
        this.setVisible(true);
    }

    public LoginStatus getloginStatus() {
        LoginStatus loginStatus = null;
        try {
            String name = inputName.getText();
            String staffNo = inputStaffNo.getText();

            String sqlStr = "SELECT * FROM Staff Where name = '" + name + "' and id = " + staffNo;
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();



            while(rs.next()) {
                String staffName = rs.getString("name");
                String staffGrade = rs.getString("grade");
                loginStatus = new LoginStatus(staffName, staffGrade);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        return loginStatus;
    }
}
