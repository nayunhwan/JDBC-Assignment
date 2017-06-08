import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class TabCustomer extends JPanel {

    JLabel labelCustomerName = new JLabel("고객명");
    JTextField inputCustomerName = new JTextField();
    JTextArea tareaCustomer = new JTextArea();
    JButton btnSign = new JButton("가입");
    JButton btnFind = new JButton("조회");

    private LoginStatus loginStatus;
    private static Connection db;

    private class SignCustomerFrame extends JFrame {
        JLabel labelCustomerName = new JLabel("고객명");
        JLabel labelBirthday = new JLabel("생일(4자리)");
        JLabel labelContact = new JLabel("연락처");

        JTextField inputCustomerName = new JTextField();
        JTextField inputBirthday = new JTextField();
        JTextField inputContact = new JTextField();

        JButton btnSign = new JButton("가입신청");
        JButton btnCancel = new JButton("취소");

        SignCustomerFrame() {
            this.setLayout(null);
            labelCustomerName.setBounds(20, 20, 100, 30);
            labelBirthday.setBounds(20, 70, 100, 30);
            labelContact.setBounds(20, 120, 100, 30);
            inputCustomerName.setBounds(130, 20, 100, 30);
            inputBirthday.setBounds(130, 70, 100, 30);
            inputContact.setBounds(130, 120, 100, 30);
            btnSign.setBounds(20, 170, 100, 30);
            btnSign.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    signCustomer();
                    dispose();
                }
            });
            btnCancel.setBounds(130, 170, 100, 30);
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            this.add(labelCustomerName);
            this.add(labelBirthday);
            this.add(labelContact);
            this.add(inputCustomerName);
            this.add(inputBirthday);
            this.add(inputContact);
            this.add(btnSign);
            this.add(btnCancel);

            this.setTitle("회원등록");
            this.setBounds(150, 150, 270, 250);
            this.setVisible(true);
        }

        private int getCustomerCount() {
            String sqlStr = "Select Count(id) from customer";
            int n = 0;
            try {
                PreparedStatement stmt = db.prepareStatement(sqlStr);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                n = Integer.parseInt(rs.getString("count(id)"));
            }
            catch (Exception e) { System.out.println(e); }
            return n;
        }

        private Boolean isAlready(String name) {
            Boolean already = false;

            try {
                String sqlStr = "select Count(name) from customer where name = '" + name + "'";
                PreparedStatement stmt = db.prepareStatement(sqlStr);
                ResultSet rs = stmt.executeQuery();
                rs.next();
                already = Integer.parseInt(rs.getString("Count(name)")) > 0;
            }
            catch (Exception e) {
                System.out.println(e);
            }
            return already;
        }

        private void signCustomer() {
            String name = inputCustomerName.getText();
            String birthday = inputBirthday.getText();
            String contact = inputContact.getText();

            try {
                if(!isAlready(name)) {
                    int id = 1000 + getCustomerCount();

                    String sqlStr = "insert into customer(name, id, birthday, contact, grade) values (" +
                            "'" + name + "', " +
                            "" + id + ", " +
                            "'" + birthday + "', " +
                            "" + contact + ", " +
                            "'" + "Normal" + "')";
                    PreparedStatement stmt = db.prepareStatement(sqlStr);
                    stmt.executeUpdate();
                    stmt.close();
                    JOptionPane.showMessageDialog(null, "등록되었습니다.");
                }
                else {
                    JOptionPane.showMessageDialog(null, "동명이인은 추가할 수 없습니다.");
                }
            }
            catch (Exception e) {
                System.out.println(e);
            }

        }
    }

    TabCustomer(Connection db) {
        this.db = db;
        this.setLayout(null);
        updateBtnAddEnable();
        labelCustomerName.setBounds(15, 15, 100, 30);
        inputCustomerName.setBounds(15, 50, 100, 30);
        inputCustomerName.setEnabled(false);

        tareaCustomer.setEnabled(false);
        tareaCustomer.setBounds(15, 90, 300, 200);
        tareaCustomer.setBorder(new LineBorder(Color.gray, 2));

        btnSign.setBounds(180, 50, 60, 30);
        btnSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SignCustomerFrame signCustomerFrame = new SignCustomerFrame();
            }
        });

        btnFind.setBounds(250, 50, 60, 30);
        btnFind.setEnabled(false);
        btnFind.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findCustomer();
            }
        });
        this.add(labelCustomerName);
        this.add(inputCustomerName);
        this.add(btnSign);
        this.add(btnFind);
        this.add(tareaCustomer);
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
        if(loginStatus != null) {
            btnFind.setEnabled(true);
            inputCustomerName.setEnabled(true);
        }
        else {
            btnFind.setEnabled(false);
            inputCustomerName.setEnabled(false);
        }
        updateBtnAddEnable();
    }

    public void updateBtnAddEnable() {
        if(loginStatus != null) {
            btnSign.setEnabled(loginStatus.getGrade().toLowerCase().equals("supervisor"));
        }
        else {
            btnSign.setEnabled(false);
        }
    }

    private void findCustomer() {
        String find = inputCustomerName.getText();
        try {
            String sqlStr = "select * from customer where name = '" + find + "'";
            String resultStr = "";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            resultStr += "고 객 명 : " + rs.getString("name") + "\n";
            resultStr += "고 객 ID : " + rs.getString("id") + "\n";
            resultStr += "생    일 : " + rs.getString("birthday") + "\n";
            resultStr += "전화번호 : " + rs.getString("contact") + "\n";
            resultStr += "고객등급 : " + rs.getString("grade") + "\n";
            resultStr += "총 구매금액 : " + rs.getString("sales") + "\n";

            tareaCustomer.setText(resultStr);

        }
        catch (Exception e) {
            System.out.println(e);
            tareaCustomer.setText("검색 결과 없음");
        }
    }
}
