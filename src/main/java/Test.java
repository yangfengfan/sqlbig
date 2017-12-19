import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Test {
    static String url = "jdbc:postgresql://10.10.100.32:5432/electricdb";
    static String usr = "electric";
    static String psd = "electric";

    public static void main(String[] args) {
        Connection conn = null;
        try {
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, usr, psd);
            Statement statement = conn.createStatement();
            String sql = "SELECT name FROM collections_boer WHERE createtime < '2017-12-01 00:00:00' AND deleteflag = '0' AND loop_id in\n" +
                    "(SELECT id FROM loop_boer WHERE deleteflag = '0' AND electricproject_id in\n" +
                    "(SELECT id FROM electricproject_boer WHERE deleteflag = '0' AND name LIKE '%兴港%')\n" +
                    ")";
            ResultSet rs = statement.executeQuery(sql);
            String name = null;
            List<List<Object>> dataListhead1 = new ArrayList<List<Object>>();
            List<Object> rowListhead1 = null;
                         while(rs.next()){
                             name = rs.getString("name");
                             String sqlBack = "SELECT * FROM epvalueofcollection_boer WHERE createtime < '2017-12-01 00:00:00' AND deleteflag = '0' AND datapoint_id = "+"'"+name+"'";
                             ResultSet rsBack = statement.executeQuery(sqlBack);
                             while (rsBack.next()) {
                                 ResultSetMetaData data1 = rsBack.getMetaData();
                                 int rowL1 = data1.getColumnCount();
                                 rowListhead1 = new ArrayList<Object>();
                                 for ( int a=1;a <= rowL1; a++) {
                                     rowListhead1.add(data1.getColumnLabel(a));
                                     dataListhead1.add(rowListhead1);
                                 }

                             }
                         }
            rs.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            //数据库驱动类异常处理
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch (SQLException e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            System.out.println("数据库数据成功获取！！");
        }
    }

}