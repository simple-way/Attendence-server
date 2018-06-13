package dao;
import com.example.mrc.attendencesystem.entity.Group;
import com.example.mrc.attendencesystem.entity.GroupMessage;
import com.example.mrc.attendencesystem.entity.GroupSignInMessage;
import com.example.mrc.attendencesystem.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GroupDao {

    private static GroupDao mGroupDao;

    public static synchronized GroupDao getGroupDao(){
        if(mGroupDao == null){
            mGroupDao = new GroupDao();
        }
        return mGroupDao;
    }

    //TODO 搜索群未完成
	//搜索群
	public ArrayList<Group> getGroups(String groupName){
        ArrayList<Group> groups = new ArrayList<>();
		try {
			String sql = "select * from group where groupname like ?";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, groupName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
			    Group group = new Group(rs);
			    groups.add(group);
            }
            return groups;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean outGroup(int groupId,int userId){
	    try {
            String sql = "delete from grouptouser where grouptouser.groupid=? and userid=?";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, groupId);
            ps.setInt(2,userId);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
	        e.printStackTrace();
        }
        return false;
    }

    public ArrayList<GroupSignInMessage> getGroupSigns(int groupId){
        ArrayList<GroupSignInMessage> signInMessages = new ArrayList<>();
        try {
            String sql = "select * from signin where groupid=?";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,groupId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                signInMessages.add(new GroupSignInMessage(rs));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return signInMessages;
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

	public Set<String> getGroupMemberName(int groupId){
	    Set<String> groupMemberNames = new HashSet<>();
	    try {
	        String sql = "SELECT * FROM user WHERE id IN (SELECT userid FROM grouptouser WHERE groupid=?)";
	        Connection conn = DBUtil.getDBUtil().getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1,groupId);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()){
	            groupMemberNames.add(rs.getString("phonenumber"));
            }
            return groupMemberNames;
        }catch (SQLException e){
	        e.printStackTrace();
        }
        return null;
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

	public boolean insertMessageIntoGroup(GroupMessage groupMessage){
	    int groupId = groupMessage.getGroupId();
	    String fromId = groupMessage.getFromId();
	    int contentType = groupMessage.getContentType();
	    String content = groupMessage.getContent();
		try {
			String sql = "insert into groupsmsgcontent (groupid,fromid,contenttype,content) values (?,?,?,?)";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1,groupId);
			ps.setString(2,fromId);
			ps.setInt(3,contentType);
			ps.setString(4,content);
			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean insertSignInRecord(GroupSignInMessage signinMessage){
	    try {
	        String sql = "insert into signin ()";
	        Connection conn= DBUtil.getDBUtil().getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
        }catch (SQLException e){
	        e.printStackTrace();
        }
        return false;
    }

    //创建一个群
    public boolean addGroup(Group group){
	    try {
	        String sql = "insert into group (groupname, adminid, introduce) values(?,?,?)";
	        Connection conn = DBUtil.getDBUtil().getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1,group.getGroupName());
	        ps.setInt(2,group.getAdminId());
	        ps.setString(3,group.getIntroduce());
	        return ps.executeUpdate() > 0;
        }catch (SQLException e){
	        e.printStackTrace();
        }
        return false;
    }

    //删除一个群
    public boolean deleteGroup(int groupId){
        try {
            String sql = "delete from group where id=?";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,groupId);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 分页查询群内消息 每次每页放20条数据
     * @param groupId 群ID
     * @param messageId 当前界面最老消息Id 如果为-1则为初次进入群界面
     * @return 返回分页消息列表
     */
	public ArrayList<GroupMessage> pagingQueryGroupMessage(int groupId, int messageId){
	    ArrayList<GroupMessage> groupMessages = new ArrayList<>();
	    try {
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps;
	        if(messageId == -1){
	            String sql = "SELECT * FROM groupsmsgcontent WHERE groupid = ? ORDER BY id DESC LIMIT 20";
	            ps = conn.prepareStatement(sql);
	            ps.setInt(1,groupId);
            }else {
                //非当前页
                String sql = "SELECT * FROM (select * from groupsmsgcontent  where groupid=? limit ?,20) AS a\n" +
                        "ORDER BY a.id DESC";
                ps = conn.prepareStatement(sql);
                ps.setInt(1,groupId);
                ps.setInt(2,(messageId - 20) > 0 ? (messageId - 20) : 0);
            }
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()){
	            groupMessages.add(new GroupMessage(rs));
            }
            System.out.println(groupMessages.size());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return groupMessages;
    }


}
