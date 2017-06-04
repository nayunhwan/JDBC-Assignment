import java.awt.BorderLayout;
import java.util.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Viewer implements ActionListener {
    // ¢¯?¢¥??? ©ø???
    Calendar cal = Calendar.getInstance();
    String today = cal.get(Calendar.YEAR) + "/"
            + (cal.get(Calendar.MONTH) + 1) + "/"
            + cal.get(Calendar.DATE);

    // ??????¨¬???¨ö¨¬
    Connection db;
    String sql;
    PreparedStatement stmt;
    ResultSet rs;
    int customer_id = 1000;
    int menu_id = 1000;

    int dataLoaded = 0;

    String authority; // ???? ¡¤?¡¾¡¿?? ¡¾???
    String currentStaff; // ???? ¡¤?¡¾¡¿???? ??¢¯©ª

    JFrame frame = new JFrame();

    // ?¢¬??¨¡©÷
    JLabel title = new JLabel("¨ö?¢¥? ??©ö¢ç¡Æ?¢¬¢ç");

    // 0. ¢¬¨­¢¥¨¬©ö?
    JMenuBar bar = new JMenuBar();
    // 0-1. 1?¡À¢¬¨­¢¥¨¬
    JMenu menu = new JMenu("Menu");
    // 0-2. 2?¡À¢¬¨­¢¥¨¬
    JMenuItem open = new JMenuItem("Open");
    JMenuItem login = new JMenuItem("Log in");
    // 0-3. ¢¬¨­¢¥¨¬©ö??? ¨ú¨¡????
    String filePath;

    // ¨¡¨¢©ø???
    JPanel main_panel = new JPanel();
    JPanel title_panel = new JPanel();
    JPanel grid_panel = new JPanel();
    JPanel table_panel = new JPanel();
    JPanel order_panel = new JPanel();
    JPanel menu_panel = new JPanel();
    JPanel sign_panel = new JPanel();

    // 1. ?¡¿??¨¬????©÷ ¨¡¨¢©ø?
    JButton[] tables = new JButton[20];

    // 2. ??©ö¢ç©ø?¢¯¨£ ¨¡¨¢©ø?
    JTextArea t_order = new JTextArea();
    String t_order_string = "<?©¬¡Æ¢®??©ö¢ç>\n";
    JLabel l_customer_name = new JLabel("¡Æ?¡Æ¢¥¢¬?");
    JTextField f_customer_name = new JTextField();
    JLabel l_table_name = new JLabel("?¡¿??¨¬?¢¬?");
    JComboBox<String> c_table_name = new JComboBox<String>();
    JButton b_order = new JButton("??©ö¢ç");
    JButton b_cancle = new JButton("??¨ù?");
    JButton b_pay = new JButton("¡Æ???");

    // 3. ¢¬¨­¢¥¨¬ ¨¡¨¢©ø?
    JButton[] menus = new JButton[20];

    // 4. ??¡¤?/?¢Ò?¢¬ ¨¡¨¢©ø?
    JTabbedPane tp = new JTabbedPane();
    JPanel tab_customer = new JPanel();
    // 4-1. ¡Æ?¡Æ¢¥ ¨¡¨¢©ø?
    JLabel l_customer_name4 = new JLabel("¡Æ?¡Æ¢¥¢¬?");
    JTextField f_customer_name4 = new JTextField();
    JButton b_sign = new JButton("¡Æ¢®??");
    JButton b_find = new JButton("?¢Ò?¢¬");
    JTextArea t_customer = new JTextArea();
    // 4-2. ¢¬??? ¨¡¨¢©ø?
    JPanel tab_sales = new JPanel();
    JLabel l_period = new JLabel("¡¾?¡Æ?");
    JComboBox<String> c_date = new JComboBox<String>();
    JTextArea t_sales_area = new JTextArea();
    // 4-3. ?¡À¢¯©ª ¨¡¨¢©ø?
    JPanel tab_staff = new JPanel();
    JLabel l_staff_name = new JLabel("?¡À¢¯©ª¢¬?");
    JTextField f_staff_name = new JTextField();
    JButton b_add_staff = new JButton("?¡À¢¯©ª??¡¤?");
    JButton b_find_staff = new JButton("?¢Ò?¢¬");
    JTextArea t_staff_area = new JTextArea();
    // 4-4. ¢¬¨­¢¥¨¬ ¨¡¨¢©ø?
    JPanel tab_menu = new JPanel();
    JLabel l_menu_name = new JLabel("¢¬¨­¢¥¨¬¢¬?");
    JTextField f_menu_name = new JTextField();
    JButton b_menu_add = new JButton("¢¬¨­¢¥¨¬??¡¤?");
    JButton b_find_m = new JButton("?¢Ò?¢¬");
    JTextArea t_menu_area = new JTextArea();

    // ??¨ù¨¬??
    public Viewer() {
        this.db = db;
        // JMenuBar
        open.setMnemonic('O');
        login.setMnemonic('L');
        open.addActionListener(this);
        login.addActionListener(this);
        menu.add(open); // JMenu¢¯¢® Item ¨¬??©ª
        menu.add(login);
        bar.add(menu); // JMenuBar¢¯¢® JMenu ¨¬??©ª

        // title_panel
        title.setFont(new Font("??¡¾??¨ù", 1, 40));
        title_panel.add(title);
        title_panel.setBackground(Color.WHITE);
        title_panel.setBorder(new LineBorder(Color.BLACK, 3));

        // grid_panel
        grid_panel.setLayout(new GridLayout(2, 2));

        // 1. ?¡¿??¨¬? ???©÷ ¨¡¨¢©ø?
        // table_panel
        table_panel.setBorder(new TitledBorder("?¡¿??¨¬? ???©÷"));
        table_panel.setLayout(new GridLayout(4, 5));
        // ?¡¿??¨¬? ©ö?¨¡¡Æ ??¨ù¨¬
        for (int i = 0; i < 20; i++) {
            tables[i] = new JButton((i + 1) + "");
            tables[i].setBackground(Color.WHITE);
            tables[i].setFont(new Font("??¡¾??¨ù", 1, 20));
            tables[i].addActionListener(this);
            table_panel.add(tables[i]);
        }

        // 2. ??©ö¢ç©ø?¢¯¨£ ¨¡¨¢©ø?
        // order_panel
        order_panel.setBorder(new TitledBorder("??©ö¢ç ©ø?¢¯¨£"));
        order_panel.setLayout(null);
        t_order.setBorder(new LineBorder(Color.gray, 2));
        t_order.setEditable(false);
        JScrollPane scroll = new JScrollPane();
        scroll.setViewportView(t_order);
        for (int i = 0; i < 20; i++) {
            c_table_name.addItem((i + 1) + "");
        }
        c_table_name.addActionListener(this);
        b_order.addActionListener(this);
        b_order.setBackground(Color.WHITE);
        b_cancle.addActionListener(this);
        b_cancle.setBackground(Color.WHITE);
        b_pay.addActionListener(this);
        b_pay.setBackground(Color.WHITE);
        // ©ö?¢¯???
        scroll.setBounds(15, 18, 200, 330);
        l_customer_name.setBounds(230, 15, 100, 30);
        f_customer_name.setBounds(230, 50, 100, 30);
        l_table_name.setBounds(230, 85, 100, 30);
        c_table_name.setBounds(230, 110, 100, 30);
        b_order.setBounds(230, 170, 100, 30);
        b_cancle.setBounds(230, 220, 100, 30);
        b_pay.setBounds(230, 270, 100, 30);
        // order_panel¢¯¢® ??¨¡¡À©ø?¨¡¢ç?? ¨¬??©ª
        order_panel.add(scroll);
        order_panel.add(l_customer_name);
        order_panel.add(f_customer_name);
        order_panel.add(l_table_name);
        order_panel.add(c_table_name);
        order_panel.add(b_order);
        order_panel.add(b_cancle);
        order_panel.add(b_pay);

        // 3. ¢¬¨­¢¥¨¬ ¨¡¨¢©ø?
        // menu_panel
        menu_panel.setBorder(new TitledBorder("¢¬¨­¢¥¨¬"));
        menu_panel.setLayout(new GridLayout(10, 2));
        // ¢¬¨­¢¥¨¬ ©ö?¨¡¡Æ ??¨ù¨¬
        for (int i = 0; i < 20; i++) {
            menus[i] = new JButton();
            menus[i].addActionListener(this);
            menus[i].setBackground(Color.WHITE);
            menu_panel.add(menus[i]);
        }

        // 4. ??¡¤?/?¢Ò?¢¬ ¨¡¨¢©ø?
        // sign_panel
        sign_panel.setBorder(new TitledBorder("??¡¤?/?¢Ò?¢¬"));
        sign_panel.setLayout(new BorderLayout());
        // 4-1. ¡Æ?¡Æ¢¥ ¨¡¨¢©ø?
        tab_customer.setLayout(null);
        l_customer_name4.setBounds(15, 15, 100, 30);
        f_customer_name4.setBounds(15, 50, 100, 30);
        b_sign.setBounds(180, 50, 60, 30);
        b_find.setBounds(250, 50, 60, 30);
        t_customer.setBounds(15, 90, 300, 200);
        t_customer.setBorder(new LineBorder(Color.gray, 2));
        b_sign.addActionListener(this);
        b_find.addActionListener(this);
        b_sign.setBackground(Color.WHITE);
        b_find.setBackground(Color.WHITE);
        tab_customer.add(l_customer_name4);
        tab_customer.add(f_customer_name4);
        tab_customer.add(b_sign);
        tab_customer.add(b_find);
        tab_customer.add(t_customer);
        // 4-2. ¢¬??? ¨¡¨¢©ø?
        tab_sales.setLayout(null);
        l_period.setBounds(15, 15, 100, 30);
        c_date.setBounds(150, 15, 100, 30);
        t_sales_area.setBounds(15, 50, 300, 240);
        t_sales_area.setBorder(new LineBorder(Color.gray, 2));
        tab_sales.add(l_period);
        tab_sales.add(c_date);
        tab_sales.add(t_sales_area);
        // 4-3. ?¡À¢¯©ª ¨¡¨¢©ø?
        tab_staff.setLayout(null);
        l_staff_name.setBounds(15, 15, 100, 30);
        f_staff_name.setBounds(15, 50, 100, 30);
        b_add_staff.setBounds(150, 50, 90, 30);
        b_find_staff.setBounds(250, 50, 60, 30);
        t_staff_area.setBounds(15, 90, 300, 200);
        t_staff_area.setBorder(new LineBorder(Color.gray, 2));
        b_add_staff.addActionListener(this);
        b_find_staff.addActionListener(this);
        b_add_staff.setBackground(Color.WHITE);
        b_find_staff.setBackground(Color.WHITE);
        tab_staff.add(l_staff_name);
        tab_staff.add(f_staff_name);
        tab_staff.add(b_add_staff);
        tab_staff.add(b_find_staff);
        tab_staff.add(t_staff_area);
        // 4-4. ¢¬¨­¢¥¨¬ ¨¡¨¢©ø?
        tab_menu.setLayout(null);
        l_menu_name.setBounds(15, 15, 100, 30);
        f_menu_name.setBounds(15, 50, 120, 30);
        b_menu_add.setBounds(150, 50, 90, 30);
        b_find_m.setBounds(250, 50, 60, 30);
        t_menu_area.setBounds(15, 90, 300, 200);
        t_menu_area.setBorder(new LineBorder(Color.gray, 2));
        b_menu_add.addActionListener(this);
        b_find_m.addActionListener(this);
        b_menu_add.setBackground(Color.WHITE);
        b_find_m.setBackground(Color.WHITE);
        tab_menu.add(l_menu_name);
        tab_menu.add(f_menu_name);
        tab_menu.add(b_menu_add);
        tab_menu.add(b_find_m);
        tab_menu.add(t_menu_area);
        // tab¢¯¢® ¢¯?¡¤?¡Æ¢®?? ¨¡¨¢©ø? ¡Æ?¡Æ? ¨¬??©ª
        tp.addTab("¡Æ?¡Æ¢¥", tab_customer);
        tp.addTab("¢¬???", tab_sales);
        tp.addTab("?¡À¢¯©ª", tab_staff);
        tp.addTab("¢¬¨­¢¥¨¬", tab_menu);
        sign_panel.add(tp, BorderLayout.CENTER);

        // grid_panel¢¯¢® panel ¨¬??©ª
        grid_panel.add(table_panel);
        grid_panel.add(order_panel);
        grid_panel.add(menu_panel);
        grid_panel.add(sign_panel);

        // main_panel
        main_panel.setLayout(new BorderLayout());
        main_panel.add(title_panel, BorderLayout.NORTH);
        main_panel.add(grid_panel, BorderLayout.CENTER);

        // ??¡¤©ö??¢¯¢® ¨¬??©ª
        frame.setJMenuBar(bar); // JMenuBar
        frame.add(main_panel);

        // ??¡¤©ö?? ¨ù©ø?¢´
        frame.setTitle("¨ö?¢¥? ¡Æ?¢¬¢ç ¨ö?¨ö¨¬?? (???? ¡Æ?¢¬¢ç?? : ¨ú©ª?¨ö)");
        frame.setBounds(0, 0, 700, 850);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }


    // actionPerformed


    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
