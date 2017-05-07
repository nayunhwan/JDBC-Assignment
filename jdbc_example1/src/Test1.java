import java.sql.*;

public class Test1 {
    private static Connection dbTest;
    private String username = "nayunhwan";
    private String password = "dbsghks0";

    Test1(){
        connectDB();
    }

    private void connectDB(){

        try{
            // JDBC Driver Loading
            Class.forName("oracle.jdbc.OracleDriver");
            dbTest = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", username, password);
            System.out.println("Success Connect Database");

        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("Fail Connect Database");
            System.out.println("SQLException: "+e);
        } catch (Exception e){
            System.out.println("Exception: "+e);
        }
    }

    public void execute_query() throws SQLException{
        System.out.println("--------------------------------------");
        System.out.println("Problem1: Get Average speed of PC");
        String sqlStr = "select avg(speed) from pc";
        PreparedStatement stmt = dbTest.prepareStatement(sqlStr);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("avg(speed): " + rs.getString("avg(speed)"));
        }

        System.out.println("--------------------------------------");
        System.out.println("Problem2: Get price of pc more then 2000");
        sqlStr = "select price from pc where price >= 2000";
        stmt = dbTest.prepareStatement(sqlStr);
        rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("price: " + rs.getString("price"));
        }

        System.out.println("--------------------------------------");
        System.out.println("Problem3: get model, speed, hd of pc with having 6x or 8x cd or price less than 2000");
        sqlStr = "select model, speed, hd from pc where (cd = '6x' or cd = '8x') and price <= 2000";
        stmt = dbTest.prepareStatement(sqlStr);
        rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("model: " + rs.getString("model") + " | " +
            "speed: " + rs.getString("speed") + " | " + "hd: " + rs.getString("hd"));
        }


        rs.close();
        stmt.close();
    }

    public static void main(String[] args) {
        Test1 t1 = new Test1();

        try{
            t1.execute_query();
            dbTest.close();
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("SQLException: "+e);
        }
    }
}
