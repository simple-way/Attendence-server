package model;

import java.net.Socket;
import java.util.HashMap;

/***
 *  所有成功连接的socket实体类 包括一个socket，一个用户账号
 */
public class SocketThreadMap {
	private HashMap<Integer, Socket> map;
	private Socket socket;//连接的socket
	private int userId;//用户账号
	public static SocketThreadMap instance;
	// 私有构造器，防止被外面实例化改对像
	private SocketThreadMap() {
		map = new HashMap<Integer, Socket>();
	}

	// 单例模式像外面提供该对象
	public synchronized static SocketThreadMap getInstance() {
		if (instance == null) {
			instance = new SocketThreadMap();
		}
		return instance;
	}
	
	public SocketThreadMap(Socket socket,int userId)
	{
		this.socket = socket;
		this.userId = userId;
	}
	// 添加写线程的方法
	public synchronized void add(Integer id, Socket socket) {
		this.userId = id;
		this.socket = socket;
		map.put(userId, socket);
	}

	// 移除写线程的方法
	public synchronized void remove(Integer id) {
		map.remove(id);
	}

	// 取出Socket的方法,群聊的话，可以遍历取出对应Socket
	public synchronized Socket getById(Integer id) {
		return map.get(id);
		
	}
	public int getMapSize()
	{
		return map.size();
	}
	
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket mSocket) {
		this.socket = mSocket;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int mUserId) {
		this.userId = mUserId;
	}
}
