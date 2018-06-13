package model;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.mrc.attendencesystem.entity.Message;
import com.example.mrc.attendencesystem.entity.MessageType;
import com.example.mrc.attendencesystem.entity.User;

import dao.UserDao;

public class Server {

    public static ExecutorService mSocketExecutorService;
    private ServerSocket mServerSocket;
    private static Server sServer;
    private static boolean sIsStart = true;

    public synchronized static Server getServer(){
        if(sServer == null){
            sServer = new Server();
        }
        return sServer;
    }

	public Server(){
		try {
            mSocketExecutorService = Executors.newCachedThreadPool();
            mServerSocket=new ServerSocket(5469);
            start();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private void start(){
        System.out.println("服务器已启动 in "+new Date());
        try {
            while(sIsStart){
                System.out.println(mServerSocket.toString());
                Socket s=mServerSocket.accept();
                String socketAddress = s.getInetAddress().toString();
                System.out.println(socketAddress + "客户端已连接");
                //接受客户端发来的信息
                if (s.isConnected()) {
                    mSocketExecutorService.execute(new SocketTask(s));// 添加到线程池
                }
                /*ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
                User u=(User) ois.readObject();
                Message m=new Message();
                ObjectOutputStream oos=new ObjectOutputStream(s.getOutputStream());
                if(u.getOperation().equals("login")){ //登录
                    String phoneNumber=u.getPhoneNumber();
                    UserDao udao=new UserDao();
                    boolean b=udao.login(phoneNumber, u.getPassword());//连接数据库验证用户
                    if(b){
                        System.out.println("["+phoneNumber+"]上线了！");
                        //更改数据库用户状态
                        udao.changeStateOnline(phoneNumber, 1);
                        //得到个人信息
                        String user= phoneNumber;
                        //返回一个成功登陆的信息包，并附带个人信息
                        m.setType(MessageType.SUCCESS);
                        m.setContent(user);
                        oos.writeObject(m);
                        ServerConClientThread cct=new ServerConClientThread(s,phoneNumber);//单开一个线程，让该线程与该客户端保持连接
                        ManageServerConClient.addClientThread(u.getPhoneNumber(),cct);
                        cct.start();//启动与该客户端通信的线程
                    }else{
                        m.setType(MessageType.FAIL);
                        oos.writeObject(m);
                    }
                }else if(u.getOperation().equals("register")){
                    UserDao udao=new UserDao();
                    if(udao.register(u)){
                        //返回一个注册成功的信息包
                        m.setType(MessageType.SUCCESS);
                        oos.writeObject(m);
                    }
                }*/
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit(){
        try {
            sIsStart = false;
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    final class SocketTask implements Runnable {
        private Socket socket = null;
        private ServerThread in;
        private OutputThread out;
        private OutputThreadMap outputThreadMap;


        public SocketTask(Socket socket) {
            this.socket = socket;
            outputThreadMap = OutputThreadMap.getInstance();
            //socketThreadMap = SocketThreadMap.getInstance();
        }

        @Override
        public void run() {
            out = new OutputThread(socket, outputThreadMap);// 先实例化写消息线程,（把对应用户的写线程存入map缓存器中）
            in = new ServerThread(socket, out, outputThreadMap);// 再实例化读消息线程
            out.setStart(true);
            in.setStart(true);
            in.start();
            out.start();
        }
    }

}

