package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
	
	public List<String> getGroupMember(String gaccount){
		List<String> res=new ArrayList<String>();
		try {
			String sql = "select * from yq_group_member where gaccount="+gaccount;
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				res.add(rs.getString("gmember"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
/*	public static void main(String[] args){
		String s=new GroupDao().getGroup();
		System.out.println(s);
	}*/
}
