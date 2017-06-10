import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by nayunhwan on 2017. 6. 10..
 */
public class DBLogin extends JFrame{
    JLabel labelName = new JLabel("이름");
    JLabel labelStaffNo = new JLabel("비밀번호");

    JTextField inputName = new JTextField();
    JPasswordField inputPassword = new JPasswordField();

    JButton btnLogin = new JButton("로그인");

    DBLogin() {

        this.setLayout(null);

        labelName.setBounds(15, 15, 100, 30);
        labelStaffNo.setBounds(15, 50, 100, 30);
        inputName.setBounds(100, 15, 100, 30);
        inputPassword.setBounds(100, 50, 100, 30);
        btnLogin.setBounds(220, 30, 100, 30);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = inputName.getText();
                String password = new String(inputPassword.getPassword());

                Main m = new Main(username, password);
                dispose();
            }
        });

        this.add(labelName);
        this.add(labelStaffNo);
        this.add(inputName);
        this.add(inputPassword);
        this.add(btnLogin);

        this.setBounds(100, 100, 350, 130);
        this.setTitle("DB 로그인");
        this.setVisible(true);
    }


    public static void main(String[] args) {
        new DBLogin();
    }

}
