package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.mrc.attendencesystem.entity.User;

public class UserDao {
	public boolean login(String phoneNumber, String password) {
		System.out.println(phoneNumber+"  "+password);
		try {
			String sql = "select * from user where phonenumber=? and password=?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, phoneNumber);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			if (rs != null && rs.next() == true) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean register(User u) {
		try {
			String sql = "insert into user (phonenumber,email,password,student_id,username) values(?,?,?,?,?)";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, u.getPhoneNumber());
			ps.setString(2, u.getEmail());
			ps.setString(3, u.getPassword());
			ps.setString(4, u.getStudentId());
			ps.setString(5, u.getPhoneNumber());
			int r = ps.executeUpdate();
			System.out.println(u.getPhoneNumber());
			if (r > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public boolean delBuddy(String myAccount,String dfAccount){
		try {
			String sql = "delete  from yq_buddy where baccount=? and bbuddy=?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, myAccount);
			ps.setString(2, dfAccount);
			int r = ps.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getBuddy(String phontNumber){
		String res="";
		try {
			String sql = "select * from yq_buddy where baccount="+phontNumber;
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				String s="";
				String sql2 = "select * from yq_user where uaccount="+rs.getInt("bbuddy");
				Connection conn2 = DBUtil.getDBUtil().getConnection();
				PreparedStatement ps2 = conn2.prepareStatement(sql2);
				ResultSet rs2 = ps2.executeQuery();
				while(rs2.next()){
					s=rs2.getInt("uaccount")+"_"+rs2.getString("unick")+"_"
							+rs2.getString("uavatar")+"_"+rs2.getString("utrends")+"_"+rs2.getInt("uisonline")+" ";
				}
				res+=s;	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	public String getUser(String phoneNumber){
		String res="";
		try {
			String sql = "select * from user where phonenumber="+phoneNumber;
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				res=res+rs.getString("phonenumber")+"_"+rs.getString("username")+"_"
						+rs.getString("age")+"_"+rs.getString("student_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public boolean changeState(String phonenumber,int state){
		try {
			String sql = "update user set isonline=? where phonenumber=?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, state);
			ps.setString(2, phonenumber);
			int r = ps.executeUpdate();
			if (r > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean userOffLine(String phoneNumber) {
		try {
			String sql = "update user set isonline =? where phonenumber=?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
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
	
}