import com.sun.deploy.panel.JavaPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Main extends JFrame {
//
//    private void addGrid(GridBagLayout gbl, GridBagConstraints gbc, Component c,
//                         int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty) {
//        gbc.gridx = gridx;
//        gbc.gridy = gridy;
//        gbc.gridwidth = gridwidth;
//        gbc.gridheight = gridheight;
//        gbc.weightx = weightx;
//        gbc.weighty = weighty;
//        gbl.setConstraints(c, gbc);
//        add(c);
//    }


    JLabel title = new JLabel("식당 주문관리");
    JPanel titlePanel = new JPanel();

    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Menu");
    JMenuItem openItem = new JMenuItem("Open");
    JMenuItem loginItem = new JMenuItem("Open");


    JPanel panelGird = new JPanel(new GridLayout(2, 2));


    TablePanel tablePanel = new TablePanel();
    OrderPanel orderPanel = new OrderPanel();
    MenuPanel menuPanel = new MenuPanel();
    RegisterPanel registerPanel = new RegisterPanel();


    public Main() {
        this.setLayout(new BorderLayout());

        // Title Panel
        title.setFont(title.getFont().deriveFont(32.0f));
        titlePanel.add(title);
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(new LineBorder(Color.black, 3));
        this.add(titlePanel, BorderLayout.NORTH);

        // Table Panel
        panelGird.add(tablePanel);
        // Order Panel
        panelGird.add(orderPanel);
        // Menu Panel
        panelGird.add(menuPanel);
        // Register Panel
        panelGird.add(registerPanel);

        this.add(panelGird, BorderLayout.CENTER);

        setSize(700, 850);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        // write your code here

        Main m = new Main();
//        Viewer v = new Viewer();
//        Login login = new Login();
    }
}
