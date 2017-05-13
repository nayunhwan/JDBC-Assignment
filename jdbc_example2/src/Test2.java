import java.sql.*;

public class Test2 {
    private static Connection dbTest;
    private String username = "nayunhwan";
    private String password = "dbsghks0";

    Test2(){
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
        System.out.println("Problem1: CD가‘8x’이고 RAM이 24이상인 PC의 제조업체, 모델, 가격을 구하시오.");
        String sqlStr = "select maker, model, price from pc natural join product where cd='8x' and ram >= 24";
        PreparedStatement stmt = dbTest.prepareStatement(sqlStr);
        ResultSet rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("Maker: " + rs.getString("maker") + " | " + "Model: " + rs.getString("model")
            + " | " + "Price: " + rs.getString("price"));
        }

        System.out.println("--------------------------------------");
        System.out.println("Problem2: SCREEN이 11초과이고 제조업체가 ‘D’나 ‘G’인 LAPTOP의 총 합계를 구하시오.");
        sqlStr = "select sum(price) from laptop natural join product where screen > 11 and (maker = 'D' or maker = 'G')";
        stmt = dbTest.prepareStatement(sqlStr);
        rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("SUM(Price): " + rs.getString("sum(price)"));
        }

        System.out.println("--------------------------------------");
        System.out.println("Problem3: PC의 hd가 2.4초과인 것과 laptop의 speed가 130초과인 모델의 총 개수를 구하시오.");
        sqlStr = "select count(model) from ((select model from pc where hd > 2.4) union (select model from laptop where speed > 130))";
        stmt = dbTest.prepareStatement(sqlStr);
        rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("Count(Model): " + rs.getString("count(model)"));
        }

        System.out.println("--------------------------------------");
        System.out.println("Problem4: Cd가‘8x이고 PC중 한개이상의 LAPTOP보다 SPEED가 큰 모델번호와 가격을 구하시오.");
        sqlStr = "select model, price from pc where cd = '8x' and speed > some (select speed from laptop)";
        stmt = dbTest.prepareStatement(sqlStr);
        rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("Model: " + rs.getString("model") + " | " + "Price: " + rs.getString("Price"));
        }

        System.out.println("--------------------------------------");
        System.out.println("Problem5: 적어도 1GB용량을 지닌 하드디스크가 내장된 랩탑의 속도와 그 제조업체를 구하라.");
        sqlStr = "select maker, speed from laptop natural join product where hd >= 1";
        stmt = dbTest.prepareStatement(sqlStr);
        rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("Maker: " + rs.getString("maker") + " | " + "Speed: " + rs.getString("speed"));
        }

        System.out.println("--------------------------------------");
        System.out.println("Problem6: 지금 model 2005 제품을 가지고 있는데, 이보다 더 속도가 빠른 PC나 LAPTOP을 사려고 한다. 어떤 제품들이 있는가??");
        sqlStr = "select model from pc where speed > (select speed from laptop where model = 2005) union select model from laptop where speed > (select speed from laptop where model = 2005)";
        stmt = dbTest.prepareStatement(sqlStr);
        rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("model: " + rs.getString("model"));
        }

        System.out.println("--------------------------------------");
        System.out.println("Problem7: 'D’제조업체의 PRINTER중 컬러 출력이 가능한 제품의 모델과 타입 (아래 그림에 없지만 포함) 가격을 보여라.");
        sqlStr = "select printer.model, price from printer join product on printer.model = product.model where maker = 'D' and color='true'";
        stmt = dbTest.prepareStatement(sqlStr);
        rs = stmt.executeQuery();

        while(rs.next()){
            System.out.println("Model: " + rs.getString("model") + " | " + "Price: " + rs.getString("price"));
        }

        rs.close();
        stmt.close();
    }

    public static void main(String[] args) {
        Test2 t2 = new Test2();

        try{
            t2.execute_query();
            dbTest.close();
        } catch (SQLException e){
            e.printStackTrace();
            System.out.println("SQLException: "+e);
        }
    }
}
