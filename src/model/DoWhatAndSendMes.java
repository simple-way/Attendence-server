package model;
import java.io.ObjectOutputStream;
import java.util.List;

import com.example.mrc.attendencesystem.entity.Message;
import com.example.mrc.attendencesystem.entity.MessageType;

import dao.GroupDao;
import dao.UserDao;

public class DoWhatAndSendMes {
	static UserDao udao=new UserDao();
	static GroupDao gdao=new GroupDao();
	
	public static void sendMes(Message m){
		try{
			//取得接收人的通信线程
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getReceiver());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			//向接收人发送消息
			oos.writeObject(m);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void sendBuddyList(Message m){
		try{
			//操作数据库，返回好友列表，顺带群列表
			//String res=udao.getBuddy(m.getSender())+","+gdao.getGroup());
			//发送好友列表到客户端
			ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
			ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
			Message ms=new Message();
			ms.setType(MessageType.RET_ONLINE_FRIENDS);
			//ms.setContent(res);
			oos.writeObject(ms);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void delBuddy(Message m){
		try{
			if(udao.delBuddy(m.getSender(), m.getReceiver())){
				ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
				ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
				Message ms=new Message();
				ms.setType(MessageType.SUCCESS);
				oos.writeObject(ms);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*public static void sendGroupMes(Message m){
		
		try{
			List<String> list=gdao.getGroupMember(m.getReceiver());
			for(String raccount : list){
				//暂只支持向在线的群成员发送消息
				if(ManageServerConClient.isOnline(raccount)){
					ServerConClientThread scc=ManageServerConClient.getClientThread(m.getSender());
					ObjectOutputStream oos=new ObjectOutputStream(scc.s.getOutputStream());
					//只需改变接收者和发送者信息
					m.setSender(m.getReceiver());
					m.setReceiver(raccount);
					oos.writeObject(m);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/
}

