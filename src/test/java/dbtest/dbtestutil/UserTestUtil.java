package dbtest.dbtestutil;

import dao.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
