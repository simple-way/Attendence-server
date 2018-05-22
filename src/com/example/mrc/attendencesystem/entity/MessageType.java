package com.example.mrc.attendencesystem.entity;

public class MessageType {
	public static final String SUCCESS="1";//表明是否成功
	public static final String FAIL="2";//表明失败
	public static final String COM_MES="3";//普通信息包
	public static final String GET_ONLINE_FRIENDS="4";//要求在线好友的包
	public static final String RET_ONLINE_FRIENDS="5";//返回在线好友的包
	public static final String LOGIN="7";//请求验证登陆
	public static final String ADD_BUDDY="8";//添加好友
	public static final String DEL_BUDDY="9";//删除好友
	public static final String GROUP_MES="10";//群消息
}
