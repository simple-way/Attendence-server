package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.example.mrc.attendencesystem.entity.*;

public class UserDao {

    private static UserDao userDao;

    public synchronized static UserDao getUserDao() {
        if (userDao == null) {
            userDao = new UserDao();
        }
        return userDao;
    }

    public boolean login(User user) {
        String phoneNumber = user.getPhoneNumber();
        String password = user.getPassword();
        System.out.println(phoneNumber + "  " + password);
        try {
            String sql = "select * from user where phonenumber=? and password=?";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean register(User u) {
        try {
            String sql = "insert into user (phonenumber,password,student_id,username) values(?,?,?,?)";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getPhoneNumber());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getStudentId());
            ps.setString(4, u.getPhoneNumber());
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

    public boolean joinGroup(String phoneNumber, Group group){
        try {
            String sql = "insert into grouptouser (groupid, userid) VALUES (?,?)";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,group.getGroupId());
            ps.setInt(2,getUserId(phoneNumber));
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<UnReceivedMessage> judgeUnReceivedMessage(String phoneNumber) {
        ArrayList<UnReceivedMessage> unReceivedMessages = new ArrayList<>();
        try {
            String sql = "select * from tsmessage where receiverid=? and state=0";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, phoneNumber);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                unReceivedMessages.add(new UnReceivedMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return unReceivedMessages;
    }

    public void removeUnReceivedMessage(String phoneNumber){
        try {
            String sql = "update tsmessage set state=1 where receiverid=?";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,phoneNumber);
            ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public boolean insertUnReceivedMessage(UnReceivedMessage unReceivedMessage) {
        try {
            String sql = "insert into tsmessage (groupid, receiverid, state) VALUES (?,?,?)";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, unReceivedMessage.getGroupId());
            ps.setString(2, unReceivedMessage.getReceiverId());
            ps.setInt(3, 1);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delBuddy(String myAccount, String dfAccount) {
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

    public String getBuddy(String phontNumber) {
        String res = "";
        try {
            String sql = "select * from yq_buddy where baccount=" + phontNumber;
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String s = "";
                String sql2 = "select * from yq_user where uaccount=" + rs.getInt("bbuddy");
                Connection conn2 = DBUtil.getDBUtil().getConnection();
                PreparedStatement ps2 = conn2.prepareStatement(sql2);
                ResultSet rs2 = ps2.executeQuery();
                while (rs2.next()) {
                    s = rs2.getInt("uaccount") + "_" + rs2.getString("unick") + "_"
                            + rs2.getString("uavatar") + "_" + rs2.getString("utrends") + "_" + rs2.getInt("uisonline") + " ";
                }
                res += s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    //给电话号返回用户，如果不存在返回null
    public User getUser(String phoneNumber) {
        try {
            String sql = "select * from user where phonenumber=" + phoneNumber;
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.first())
                return new User(rs);
            else return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getUserId(String phoneNumber) {
        return getUser(phoneNumber).getId();
    }

    public boolean changeStateOnline(String phonenumber) {
        try {
            String sql = "update user set isonline=? where phonenumber=?";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, 1);
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
            if (r > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Group> getUserGroups(String phoneNumber) {
        ArrayList<Group> userGroups = new ArrayList<>();
        try {
            String sql = "select * from `group` where id in " +
                    "(select groupid from grouptouser where userid=?)";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, getUserId(phoneNumber));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                userGroups.add(new Group(rs));
            }
            return userGroups;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<GroupSignInMessage> getUserSigns(int groupId, int userId){
        ArrayList<GroupSignInMessage> signInMessages = new ArrayList<>();
        try {
            String sql = "select * from signin where groupid=? and receiver=?";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,groupId);
            ps.setInt(2,userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                signInMessages.add(new GroupSignInMessage(rs));
            }
            return signInMessages;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //添加普通消息
    public int addOrdinaryMessage(GroupMessage groupMessage){
        int id = 0;
        try {
            String sql = "INSERT INTO groupsmsgcontent (groupid, fromid, contenttype, content)" +
                    " VALUES (?,?,?,?)";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,groupMessage.getGroupId());
            ps.setString(2,groupMessage.getFromId());
            ps.setInt(3,groupMessage.getContentType());
            ps.setString(4,groupMessage.getContent());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                id = rs.getInt(1);
        }catch (SQLException e){
            e.printStackTrace();
        }
        return id;
    }

    public boolean addSignMessage(GroupSignInMessage signInMessage){
        try {
            GroupMessage groupMessage = new GroupMessage(signInMessage.getGroupId(),
                    signInMessage.getOriginatorId(),2,"签到");
            //返回插入的到普通消息表中的该消息Id
            int messageId = addOrdinaryMessage(groupMessage);
            String sql = "INSERT INTO signin (recordid,type, groupid, originator," +
                    "starttime,endtime, longitude, latitude, region)" +
                    "VALUES (?,?,?,?,?,?,?,?,?)";

            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1,messageId);
            ps.setInt(2,1);
            ps.setInt(3,signInMessage.getGroupId());
            ps.setString(4,signInMessage.getOriginatorId());
            ps.setLong(5,signInMessage.getStartTime());
            ps.setLong(6,signInMessage.getEndTime());
            ps.setDouble(7,signInMessage.getLongitude());
            ps.setDouble(8,signInMessage.getLatitude());
            ps.setInt(9, signInMessage.getRegion());
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    /*public boolean addSigninMessage(GroupSignInMessage signInMessage){
        try {
            String sql = "insert into signin(groupid,originator,longitude,latitude,region,receiver,"
                    + "rlongitude,rlatitude,state,result,done) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?)";
            Connection conn = DBUtil.getDBUtil().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, signInMessage.getGroupId());
            ps.setInt(2, signInMessage.getOriginatorId());
            ps.setDouble(3, signInMessage.getLongitude());
            ps.setDouble(4, signInMessage.getLatitude());
            ps.setDouble(5, signInMessage.getRegion());
            ps.setInt(6, signInMessage.getReceiverId());
            ps.setDouble(7, signInMessage.getRlongitude());
            ps.setDouble(8, signInMessage.getRlatitude());
            ps.setInt(9, signInMessage.get());
            ps.setInt(10, signInMessage.getResult());
            ps.setInt(11, signInMessage.getDone());
            result = ps.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }*/


}