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

	//搜索群
	public ArrayList<Group> getGroups(String groupName){
        ArrayList<Group> groups = new ArrayList<>();
		try {
			String sql = "select * from `group` where groupname like '%"+groupName+"%'";
			Connection conn = DBUtil.getDBUtil().getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()){
			    groups.add(new Group(rs));
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return groups;
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

    //获取群所有与发起的签到记录
    public ArrayList<com.example.mrc.attendencesystem.entity.GroupSignInMessage> getGroupSigns(int groupId){
        ArrayList<com.example.mrc.attendencesystem.entity.GroupSignInMessage> signInMessages = new ArrayList<>();
        try {
            String sql = "select * from signin where groupid=? and type=1";
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

    //获取所有群成员
	public ArrayList<User> getGroupMember(int groupid){
        ArrayList<User> res=new ArrayList<>();
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

	//判断是否该用户已经确认签到过了，如果已经签到返回该记录id 否则返回0
	public int judgeExistsConfirmSignIn(GroupSignInMessage signInMessage){
	    try {
	        String sql = "select * from signin where recordid=? and receiver=?";
	        Connection conn = DBUtil.getDBUtil().getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1,signInMessage.getRecordId());
	        ps.setString(2,signInMessage.getReceiverId());
	        ResultSet rs = ps.executeQuery();
	        if(rs.next()){
	            return rs.getInt("id");
            }
        }catch (SQLException e){
	        e.printStackTrace();
        }
        return 0;
    }


	public boolean insertConfirmSignInRecord(GroupSignInMessage signinMessage){
	    try {
	        int signId = judgeExistsConfirmSignIn(signinMessage);
            System.out.println(signId);
            if(signId == 0) {
                String sql = "insert into signin (recordid, type, groupid, originator, " +
                        "starttime, endtime, longitude, latitude, region, receiver," +
                        " rlongitude, rlatitude, result) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                Connection conn = DBUtil.getDBUtil().getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, signinMessage.getRecordId());
                ps.setInt(2, 2);
                ps.setInt(3, signinMessage.getGroupId());
                ps.setString(4, signinMessage.getOriginatorId());
                ps.setLong(5, signinMessage.getStartTime());
                ps.setLong(6, signinMessage.getEndTime());
                ps.setDouble(7, signinMessage.getLongitude());
                ps.setDouble(8, signinMessage.getLatitude());
                ps.setInt(9, signinMessage.getRegion());
                ps.setString(10, signinMessage.getReceiverId());
                ps.setDouble(11, signinMessage.getRlongitude());
                ps.setDouble(12, signinMessage.getRlatitude());
                ps.setInt(13, signinMessage.getResult());
                return ps.executeUpdate() > 0;
            }else {
	            String sql = "update signin set rlatitude=?, rlongitude=? where id=?";
	            Connection conn = DBUtil.getDBUtil().getConnection();
	            PreparedStatement ps = conn.prepareStatement(sql);
	            ps.setDouble(1,signinMessage.getRlatitude());
	            ps.setDouble(2,signinMessage.getRlongitude());
	            ps.setInt(3,signId);
	            return ps.executeUpdate() > 0;
            }
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

    //获取所有该条签到信息 包括发起签到和确认签到信息
    public ArrayList<GroupSignInMessage> getAllSingleSignInMessage(int signInId){
	    ArrayList<GroupSignInMessage> signInMessages = new ArrayList<>();
	    try {
	        String sql = "select * from signin where recordid=?";
	        Connection conn = DBUtil.getDBUtil().getConnection();
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setInt(1,signInId);
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()){
	            signInMessages.add(new GroupSignInMessage(rs));
            }
        }catch (SQLException e){
	        e.printStackTrace();
        }
        return signInMessages;
    }


}
