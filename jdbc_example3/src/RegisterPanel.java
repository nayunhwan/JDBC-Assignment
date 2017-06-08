import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.Connection;

/**
 * Created by nayunhwan on 2017. 6. 4..
 */
public class RegisterPanel extends JPanel {

    JTabbedPane tabPanel = new JTabbedPane();

    private TabCustomer tabCustomer;
    private TabSales tabSales;
    private TabStaff tabStaff;
    private TabMenu tabMenu;

    private LoginStatus loginStatus = null;

    private static Connection db;
    RegisterPanel(Connection db, MenuPanel menuPanel) {
        this.db = db;
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("등록/조회"));

        tabCustomer = new TabCustomer(db);
        tabSales = new TabSales(db);
        tabStaff = new TabStaff(db);
        tabMenu = new TabMenu(db, menuPanel);

        tabPanel.addTab("고객", tabCustomer);
        tabPanel.addTab("매출", tabSales);
        tabPanel.addTab("직원", tabStaff);
        tabPanel.addTab("메뉴", tabMenu);

        this.add(tabPanel, BorderLayout.CENTER);
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.loginStatus = loginStatus;
        tabCustomer.setLoginStatus(loginStatus);
        tabSales.setLoginStatus(loginStatus);
        tabStaff.setLoginStatus(loginStatus);
        tabMenu.setLoginStatus(loginStatus);
    }

    public TabCustomer getTabCustomer() {
        return this.tabCustomer;
    }

    public TabSales getTabSales() {
        return this.tabSales;
    }

    public TabStaff getTabStaff() {
        return this.tabStaff;
    }

    public TabMenu getTabMenu() {
        return this.tabMenu;
    }
}
