package dao;
import com.example.mrc.attendencesystem.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupDao {
	//获取所有
	public String getGroup(String groupName){
		String g="";
		try {
			String sql = "select * from group where groupname like ?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, groupName);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				g=g+rs.getInt("gaccount")+"_"+rs.getString("gnick")+"_"+rs.getString("gtrends")+" ";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return g;
	}
	
	public String getGroupNick(int gaccount){
		String g="";
		try {
			String sql = "select * from yq_group where gaccount="+gaccount;
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				g=rs.getString("gnick");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return g;
	}
	
	public List<User> getGroupMember(int groupid){
		List<User> res=new ArrayList<>();
		try {
			String sql = "SELECT * FROM user WHERE id IN (SELECT userid FROM grouptouser WHERE groupid=?)";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,groupid);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				res.add(new User(rs));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public boolean insertMessageIntoGroup(int groupId,int fromId,int contentType, String content){
		try {
			String sql = "insert into groupsmsgcontent (groupid,fromid,contenttype,content) values (?,?,?,?)";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,groupId);
			ps.setInt(2,fromId);
			ps.setInt(3,contentType);
			ps.setString(4,content);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}


/*	public static void main(String[] args){
		String s=new GroupDao().getGroup();
		System.out.println(s);
	}*/
}
