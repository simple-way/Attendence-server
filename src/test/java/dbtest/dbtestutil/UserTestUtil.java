package dbtest.dbtestutil;

import com.example.mrc.attendencesystem.entity.GroupMessage;
import dao.DBUtil;

import java.sql.*;

public class UserTestUtil {
    public static ResultSet getUser(Connection connection,String phoneNumber){
        try {
            String sql = "select * from user where phonenumber=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1,phoneNumber);
            return ps.executeQuery();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }
    public static boolean userOffLine(Connection connection,String phoneNumber) {
        try {
            String sql = "update user set isonline =? where phonenumber=?";
            connection.setAutoCommit(false);
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, 0);
            ps.setString(2, phoneNumber);
            int r = ps.executeUpdate();
            if(r > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public static void changeStateOnline(Connection connection,String phonenumber,int state){
        try {
            String sql = "update user set isonline=? where phonenumber=?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, state);
            ps.setString(2, phonenumber);
            int r = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int addOrdinaryMessage(Connection connection,GroupMessage groupMessage){
        int id = 0;
        try {
            String sql = "INSERT INTO groupsmsgcontent (groupid, fromid, contenttype, content)" +
                    " VALUES (?,?,?,?)";
            String a;
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,groupMessage.getGroupId());
            ps.setString(2,groupMessage.getFromId());
            ps.setInt(3,1);
            ps.setString(4,groupMessage.getContent());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();
            System.out.println(rs.getInt(1));
            /*for (int i = 0; i < 10; i++) {
                rs.next();
                System.out.println(rs.getInt(1));
            }*/
        }catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }
}
