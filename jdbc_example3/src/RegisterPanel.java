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
    private static Connection db;
    RegisterPanel(Connection db, MenuPanel menuPanel) {
        this.db = db;
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("등록/조회"));

        tabPanel.addTab("고객", new TabCustomer(db));
        tabPanel.addTab("매출", new TabSales());
        tabPanel.addTab("직원", new TabStaff());
        tabPanel.addTab("메뉴", new TabMenu(db, menuPanel));

        this.add(tabPanel, BorderLayout.CENTER);
    }
}
