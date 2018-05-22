/**
 * 管理客户端连接的类
 */
package model;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class ManageServerConClient {
	public static HashMap hm=new HashMap<String,ServerConClientThread>();
	
	//添加一个客户端通信线程
	public static void addClientThread(String phoneNumber, ServerConClientThread cc){
		hm.put(phoneNumber,cc);
	}
	//得到一个客户端通信线程
	public static ServerConClientThread getClientThread(String i){
		return (ServerConClientThread)hm.get(i);
	}
	
	public static void removeClientThread(String phoneNumber) {
		hm.remove(phoneNumber);
	}
	//返回当前在线人的情况
	public static List getAllOnLineUserid(){
		List list=new ArrayList();
		//使用迭代器完成
		Iterator it=hm.keySet().iterator();
		while(it.hasNext()){
			list.add((int) it.next());
		}
		return list;
	}
	
	public static boolean isOnline(String a){
		List list=new ArrayList();
		//使用迭代器完成
		Iterator it=hm.keySet().iterator();
		while(it.hasNext()){
			int account=(int) it.next();
			if(a.equals(account)){
				return true;
			}
		}
		return false;
	}
}
