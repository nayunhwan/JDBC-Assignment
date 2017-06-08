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
public class TabStaff extends JPanel {

    JLabel labelStaffName = new JLabel("직원명");
    JTextField inputStaffName = new JTextField();
    JButton btnAddStaff = new JButton("직원등록");
    JButton btnFindStaff = new JButton("조회");
    JTextArea tareaStaff = new JTextArea();

    private LoginStatus loginStatus = null;
    private static Connection db;

    private class AddStaffFrame extends JFrame {
        JLabel labelStaffName = new JLabel("직원명");
        JLabel labelStaffGrade = new JLabel("직급");

        JButton btnSign = new JButton("등록");
        JButton btnCancel = new JButton("취소");

        JTextField inputStaffName = new JTextField();
        JComboBox<String> comboGrade = new JComboBox<>();

        AddStaffFrame() {
            this.setLayout(null);

            comboGrade.addItem("Supervisor");
            comboGrade.addItem("Staff");

            btnSign.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    signStaff();
                    dispose();
                }
            });

            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });

            labelStaffName.setBounds(20, 20, 100, 30);
            labelStaffGrade.setBounds(20, 70, 100, 30);
            inputStaffName.setBounds(130, 20, 100, 30);
            comboGrade.setBounds(130, 70, 100, 30);
            btnSign.setBounds(20, 120, 100, 30);
            btnCancel.setBounds(130, 120, 100, 30);

            this.add(labelStaffName);
            this.add(labelStaffGrade);
            this.add(inputStaffName);
            this.add(comboGrade);
            this.add(btnSign);
            this.add(btnCancel);

            this.setTitle("직원등록");
            this.setBounds(150, 150, 270, 200);
            this.setVisible(true);
        }


        private int getStaffCount() {
            String sqlStr = "Select Count(id) from staff";
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
                String sqlStr = "select Count(name) from staff where name = '" + name + "'";
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

        private void signStaff() {
            String name = inputStaffName.getText();
            String grade = (String)comboGrade.getSelectedItem();

            try {
                if(!isAlready(name)) {
                    int id = 1000 + getStaffCount();

                    String sqlStr = "insert into staff(name, id, grade) values (" +
                            "'" + name + "'," +
                            "" + id + ", " +
                            "'" + grade + "')";

                    PreparedStatement stmt = db.prepareStatement(sqlStr);
                    stmt.executeUpdate();
                    stmt.close();
                    JOptionPane.showMessageDialog(null, "등록되었습니다.");
                }
                else {
                    JOptionPane.showMessageDialog(null, "동명이인은 추가할 수 없습니다.");
                }
            }
            catch (Exception e){
                System.out.println(e);
            }

        }
    }

    TabStaff(Connection db) {
        this.db = db;
        this.setLayout(null);
        updateBtnAddEnable();
        labelStaffName.setBounds(15, 15, 100, 30);
        inputStaffName.setBounds(15, 50, 100, 30);
        inputStaffName.setEnabled(false);
        btnAddStaff.setBounds(150, 50, 90, 30);
        btnAddStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddStaffFrame addStaffFrame = new AddStaffFrame();
            }
        });
        btnFindStaff.setBounds(250, 50, 60, 30);
        btnFindStaff.setEnabled(false);
        btnFindStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findStaff();
            }
        });
        tareaStaff.setEditable(false);
        tareaStaff.setBounds(15, 90, 300, 200);
        tareaStaff.setBorder(new LineBorder(Color.gray, 2));
        this.add(labelStaffName);
        this.add(inputStaffName);
        this.add(btnAddStaff);
        this.add(btnFindStaff);
        this.add(tareaStaff);
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
        if(loginStatus != null) {
            inputStaffName.setEnabled(true);
            btnFindStaff.setEnabled(true);
        }
        else {
            inputStaffName.setEnabled(false);
            btnFindStaff.setEnabled(false);
        }
        updateBtnAddEnable();
    }

    public void updateBtnAddEnable() {
        if(loginStatus != null) {
            btnAddStaff.setEnabled(loginStatus.getGrade().toLowerCase().equals("supervisor"));
        }
        else {
            btnAddStaff.setEnabled(false);
        }
    }

    private void findStaff() {
        String name = inputStaffName.getText();
        try {
            String sqlStr = "select * from staff where name = '" + name + "'";
            String resultStr = "";
            PreparedStatement stmt = db.prepareStatement(sqlStr);
            ResultSet rs = stmt.executeQuery();

            rs.next();
            resultStr += "직원명 : " + rs.getString("name") + "\n";
            resultStr += "직  급 : " + rs.getString("grade") + "\n";
            resultStr += "총실적 : " + rs.getString("sales");

            tareaStaff.setText(resultStr);
        }
        catch (Exception e) {
            System.out.println(e);
            tareaStaff.setText("검색 결과 없음");
        }
    }


}
