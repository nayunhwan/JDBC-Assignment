import javax.swing.*;
import java.awt.*;

/**
 * Created by nayunhwan on 2017. 5. 27..
 */
public class Login extends JFrame{
    JLabel label_id = new JLabel();
    JLabel label_pw = new JLabel();
    JTextField input_id = new JTextField();
    JPasswordField input_pw = new JPasswordField();
    JButton btn_enter = new JButton();
    JButton btn_find = new JButton("비밀번호 찾기");
    JPanel pan_input = new JPanel(new GridLayout(2,2));
    JPanel pan = new JPanel(new GridLayout(2,1));
    JPanel pan_btn = new JPanel(new GridLayout(1,1));
    JLabel label_noti = new JLabel();

    public Login(){
        setLayout(new GridLayout(2,1));

        label_id.setText("ID");
        label_id.setHorizontalAlignment(getWidth());
        label_pw.setText("PW");
        label_pw.setHorizontalAlignment(getWidth());
        label_noti.setText("ID와 PW를 입력해주세요.");
        label_noti.setHorizontalAlignment(getWidth());

        pan_input.add(label_id);
        pan_input.add(input_id);
        pan_input.add(label_pw);
        pan_input.add(input_pw);

        add(pan_input);
        btn_enter.setText("Login");
        pan.add(label_noti);
        pan_btn.add(btn_enter);
        pan.add(pan_btn);
        add(pan);

        setSize(500, 200);
        setVisible(true);
    }
}
