package com.example.mrc.attendencesystem.entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Group {
    int groupId;
    String groupName;
	String introduce;
	int adminId;

    public Group(String groupName, String introduce, int adminId) {
        this.groupName = groupName;
        this.introduce = introduce;
        this.adminId = adminId;
    }

    public Group(ResultSet resultSet){
        try {
            setGroupId(resultSet.getInt("id"));
            setGroupName(resultSet.getString("groupname"));
            setAdminId(resultSet.getInt("adminid"));
            setIntroduce(resultSet.getString("introduce"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
}
