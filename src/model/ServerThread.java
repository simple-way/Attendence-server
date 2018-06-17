package model;

import com.example.mrc.attendencesystem.entity.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dao.*;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

//import org.json.JSONObject

/**
 * 服务器读消息线程
 */
public class ServerThread extends Thread {

    private Socket socket;
    private Gson gson;
    private OutputThread out;// 传递进来的写消息线程，因为我们要给用户回复消息啊
    private OutputThreadMap map;// 写消息线程缓存器
    private SocketThreadMap socketThreadMap;// 写消息线程缓存器
    private DataInputStream inputStream;// 对象输入流
    private InputStreamReader iReader;
    private boolean isStart = true;// 是否循环读消息
    private long lastReceiveHeart;

    public ServerThread(Socket socket, OutputThread out, OutputThreadMap map) {
        this.socket = socket;
        this.out = out;
        this.map = map;
        try {
            inputStream = new DataInputStream(socket.getInputStream());// 实例化对象输入流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setStart(boolean isStart) {// 提供接口给外部关闭读消息线程
        this.isStart = isStart;
    }

    @Override
    public void run() {
        // TODO 心跳包这里写的不对
        try {
            while (isStart) {
                iReader = new InputStreamReader(inputStream, "UTF-8");
                char[] buffer = new char[1024];
                int count = 0;
                String phone = null;
                StringBuffer sBuilder = new StringBuffer();
                while ((count = iReader.read(buffer, 0, buffer.length)) > -1) {
                    sBuilder.append(buffer, 0, count);
                    if (count < 1024 && count != 0) {
                        break;
                    }
                }
                System.out.println("tran once" + sBuilder.toString());

                gson = new GsonBuilder().setPrettyPrinting() // 格式化输出（序列化）
                        .setDateFormat("yyyy-MM-dd HH:mm:ss") // 日期格式化输出
                        .create();
                JsonReader jsonReader = new JsonReader(new StringReader(sBuilder.toString()));// 其中jsonContext为String类型的Json数据
                jsonReader.setLenient(true);
                TranObject readObject = gson.fromJson(jsonReader, TranObject.class);
                if (readObject != null) {
                    lastReceiveHeart = System.currentTimeMillis();
                    phone = readObject.getFromUser();
                    System.out.println("phone:" + phone);
                    if (readObject.getType() != TranObjectType.HEART_TEST) {
                        System.out.println("msg :" + sBuilder.toString());
                    }

                }
                /*if (System.currentTimeMillis() - lastReceiveHeart > 100000) {
                    try {
                        if (phone != null) {
                            UserDao.getUserDao().userOffLine(phone);
                            //new UserDao().updateStatus(0, phone);
                        }

                        socket.close();
                    } catch (IOException e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }*/
                TranObject serverResult = execute(readObject);
                pushMessage(readObject);// 执行推送的消息
                if (serverResult != null) {
                    map.getAll().forEach(System.out::println);
                    System.out.println("当前out地址" +map.getById(phone));
                    map.getById(phone).setMessage(serverResult);
                    //out.setMessage(serverResult);
                }
            }
            if (iReader != null) {
                iReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            // TODO: handle exception
            /*if (iReader != null) {
                iReader.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }*/
        }

    }

    // 处理客户端发送过来的消息
    private TranObject execute(TranObject readObject) {
        // String responseString = null;

        boolean flag;
        UserDao userService = UserDao.getUserDao();
        GroupDao groupDao = GroupDao.getGroupDao();
        if (readObject != null) {
            String phone = readObject.getFromUser();
            // System.out.println("开始接受心跳包"+socket.getInetAddress().getHostAddress());
            if (readObject.getType() == TranObjectType.HEART_TEST) {
                if (phone != null) {
                    userService.changeStateOnline(phone);
                }
                // System.out.println("开始接受心跳包");
                lastReceiveHeart = System.currentTimeMillis();

                TranObject heartObject = new TranObject(TranObjectType.HEART_TEST);
                return heartObject;
            }
            switch (readObject.getType()) {
                case REGISTER:// 如果用户是注册
                    User registerUser = readObject.getUser();
                    System.out.println("REGISTER");
                    boolean sign = userService.register(registerUser);
                    // 给用户回复消息
                    TranObject registerTranObject = new TranObject(TranObjectType.REGISTER);
                    registerTranObject.setSuccess(sign);
                    return registerTranObject;
                case REGISTER_TEST: // 检验账号是否存在
                    User checkUser = readObject.getUser();
                    System.out.println("REGISTER_TEST");
                    flag = userService.getUser(checkUser.getPhoneNumber()) != null;
                    // 给用户回复消息
                    TranObject checkTranObject = new TranObject(TranObjectType.REGISTER_TEST);
                    checkTranObject.setSuccess(flag);
                    return checkTranObject;
                case LOGIN:
                    User loginUser = readObject.getUser();
                    System.out.println("LOGIN");
                    flag = userService.login(loginUser);
                    TranObject loginObject = new TranObject(TranObjectType.LOGIN);
                    if (flag) {// 如果登录成功 将OutputStream 放入Map中
                        map.add(loginUser.getPhoneNumber(), out);
                        ArrayList<UnReceivedMessage> unReceivedMessages =
                                userService.judgeUnReceivedMessage(loginUser.getPhoneNumber());
                        userService.removeUnReceivedMessage(loginUser.getPhoneNumber());
                        if (unReceivedMessages.size() > 0) {
                            loginObject.setUnReceivedMessages(unReceivedMessages);
                        }
                    }
                    loginObject.setUser(loginUser);
                    loginObject.setSuccess(flag);
                    return loginObject;
                /*case RESET_PASSWORD:
                    User resetUser = (User) readObject.getUser();
                    System.out.println("RESET_PASSWORD");
                    flag = userService.resetPassword(resetUser);
                    TranObject resetObject = new TranObject(TranObjectType.RESET_PASSWORD);
                    if (flag) {// 如果登录成功
                        resetObject.setSuccess(flag);
                    } else {
                    }
                    return resetObject;*/
                case LOGOUT:
                    String outPhone = readObject.getFromUser();
                    System.out.println("LOGOUT");
                    flag = userService.userOffLine(outPhone);
                    if (flag) {// 如果更新状态成功移除相应的socket写线程
                        map.remove(outPhone);
                    }
                    return null;
                /*case UPDATE_USER:
                    User updateUser = (User) readObject.getUser();
                    System.out.println("UPDATE_USER");
                    TranObject updateObject = new TranObject(TranObjectType.UPDATE_USER);
                    if (userService.updateUser(updateUser)) {
                        System.out.println("UPDATE_USER " + true);
                        updateObject.setSuccess(true);
                    } else {
                    }
                    return updateObject;*/
                /*case SET_IMAGEPATH:
                    User imageUser = (User) readObject.getUser();
                    System.out.println("SET_IMAGEPATH");
                    flag = userService.updateImage(imageUser);
                    TranObject imageObject = new TranObject(TranObjectType.SET_IMAGEPATH);
                    if (flag) {
                        imageObject.setSuccess(flag);
                    } else {
                    }*/
                case GET_GROUPS:// 获取用户加入的群
                    String stuIdJ = readObject.getFromUser();
                    System.out.println("GET_GROUPS");
                    /*ArrayList<Group> groups = null;
                    groups = groupDao.getOwnerGroups(stuIdJ);
                    ArrayList<Group> groupsList = groupDao.getJoinGroups(stuIdJ, groups);*/
                    ArrayList<Group> groupsList = userService.getUserGroups(stuIdJ);
                    TranObject jGroupsObject = new TranObject(TranObjectType.GET_GROUPS);
                    jGroupsObject.setGroupList(groupsList);
                    jGroupsObject.setSuccess(true);
                    return jGroupsObject;
                case ADD_GROUP:// 添加群
                    Group groupA = readObject.getGroup();
                    System.out.println("ADD_GROUP");
                    TranObject addGroup = new TranObject(TranObjectType.ADD_GROUP);
                    if (groupDao.addGroup(groupA)) {
                        addGroup.setSuccess(true);
                    } else {
                        addGroup.setSuccess(false);
                    }
                    return addGroup;
                case DELETE_GROUP:// 删除群
                    String groupID = readObject.getFromUser();
                    System.out.println("DELETE_GROUP");
                    TranObject deleteGroup = new TranObject(TranObjectType.DELETE_GROUP);
                    if (groupDao.deleteGroup(Integer.valueOf(groupID))) {
                        System.out.println("DELETE_GROUP true");
                        deleteGroup.setSuccess(true);
                    /*
					 * List<OutputThread> outputList = map.getAll(); for(int
					 * i=0;i<outputList.size();i++) { outputList.get(i).setMessage(deleteGroup); }
					 */
                    } else {
                        System.out.println("DELETE_GROUP false");
                        deleteGroup.setSuccess(false);
                    }
                    return deleteGroup;
                case SEARCH_GROUP:
                    Group group = readObject.getGroup();
                    System.out.println("SEARCH_GROUP");
                    TranObject sGroup = new TranObject(TranObjectType.SEARCH_GROUP);
                    ArrayList<Group> sgroups = groupDao.getGroups(group.getGroupName());
                    boolean hasGroups = sgroups != null && sgroups.size() > 0;
                    if (hasGroups) {
                        sGroup.setSuccess(true);
                        sGroup.setGroupList(sgroups);
                    } else
                        sGroup.setSuccess(false);
                    return sGroup;
                case OUT_GROUP:// 退出群
                    String oStuId = readObject.getFromUser();// 获取用户Id
                    String oGroupId = readObject.getToUser();// 获取要加入的群的id
                    System.out.println("OUT_GROUP");
                    TranObject outGroup = new TranObject(TranObjectType.OUT_GROUP);
                    if (groupDao.outGroup(Integer.valueOf(oGroupId), Integer.valueOf(oStuId))) {
                        outGroup.setSuccess(true);
                    } else {
                        outGroup.setSuccess(false);
                    }
                    return outGroup;
                case GET_USER_SIGN_RECORD://获取个人签到记录
                    String usrStuId = readObject.getFromUser();// 获取用户Id
                    String srGroupId = readObject.getToUser();// 获取群的id
                    ArrayList<GroupSignInMessage> signInfos = userService.getUserSigns(Integer.valueOf(srGroupId), Integer.valueOf(usrStuId));
                    System.out.println("GET_USER_SIGN_RECORD");
                    TranObject usrObject = new TranObject(TranObjectType.GET_USER_SIGN_RECORD);
                    usrObject.setSuccess(true);
                    usrObject.setSignInfoslist(signInfos);
                    return usrObject;
                case GET_GROUP_SIGN_RECORD://获取群的历史签到记录
                    String grGroupId = readObject.getFromUser();// 获取群Id
                    ArrayList<GroupSignInMessage> gsignInfos = groupDao.getGroupSigns(Integer.valueOf(grGroupId));
                    System.out.println("GET_GROUP_SIGN_RECORD");
                    TranObject grObject = new TranObject(TranObjectType.GET_USER_SIGN_RECORD);
                    grObject.setSuccess(true);
                    grObject.setSignInfoslist(gsignInfos);
                    return grObject;
                /*case SEND_SIGN_RESPONSE:// 发送签到结果
                    GroupSignInMessage signInfo = readObject.getSignInfo();
                    boolean result = userService.addSigninMessage(signInfo);
                    if (result) {
                        System.out.println("您已签到成功");
                    }*/
                case GET_GROUP_MESSAGE:
                    GroupRequest request = readObject.getRequest();
                    ArrayList<GroupMessage> groupMessages = groupDao.pagingQueryGroupMessage(request.getGroupId(), request.getCurrentPage());
                    TranObject getGroupMessageResult = new TranObject(TranObjectType.GET_GROUP_MESSAGE);
                    getGroupMessageResult.setGroupMessageArrayList(groupMessages);
                    getGroupMessageResult.setSuccess(true);
                    return getGroupMessageResult;
                default:
                    break;
            }

        }

        return readObject;
    }


    /**
     * 处理需要互相推送的消息
     *
     * @param readObject
     */
    private void pushMessage(TranObject readObject) {
        UserDao userDao = UserDao.getUserDao();
        GroupDao groupDao = GroupDao.getGroupDao();
        //MessageDao messageDao = DaoFactory.getMessageDaoInstance();
        //SignDao signDao = DaoFactory.getSignDaoInstance();
        if (readObject != null) {
            switch (readObject.getType()) {
                /*case SEND_JOIN_REQUEST:// 请求加入群
                    Message st_Message = readObject.getMessage();
                    System.out.println("SEND_JOIN_REQUEST");
                    TranObject st_joinGroup = new TranObject(TranObjectType.GET_JOIN_REQUEST);
                    String st_receiver_id = st_Message.getReceiver_id();// 获取接受者的id
                    OutputThread outputThread = map.getById(st_receiver_id);// 取出对应接受者的写线程
                    messageDao.addMessage(st_Message);
                    if (outputThread != null) {
                        //ArrayList<Message> st_Messages = messageDao.getMessage(st_Message, 0);
                        ArrayList<Message> st_Messages = new ArrayList<>();
                        st_Messages.add(st_Message);
                        st_joinGroup.setMessages(st_Messages);
                        st_joinGroup.setSuccess(true);
                        outputThread.setMessage(st_joinGroup);
                    }
                    break;
                case SEND_JOIN_RESPONSE:// 处理加入群
                    Message s_Message = readObject.getMessage();
                    System.out.println("SEND_JOIN_RESPONSE");
                    if (s_Message.getResponse_type() == 1)// 1表示同意的应答
                    {
                        System.out.println("同意");
                        if (groupDao.checkJoin(s_Message.getGroup_id(), s_Message.getSender_id())) {
                            s_Message.setMes_content("您已经是群成员了");
                        } else {
                            //将成员加入群中
                            groupDao.joinGroup(s_Message.getGroup_id(), s_Message.getSender_id());
                        }

                    }
                    TranObject s_joinGroup = new TranObject(TranObjectType.GET_JOIN_RESPONSE);
                    String s_send_id = s_Message.getReceiver_id();// 获取接受者的id
                    OutputThread s_outputThread = map.getById(s_send_id);// 取出对应接受者的写线程
                    messageDao.addMessage(s_Message);
                    if (s_outputThread != null) {
                        System.out.println("s_outputThread");
                        //ArrayList<Message> s_Messages = messageDao.getResponse(s_Message);
                        ArrayList<Message> s_Messages = new ArrayList<>();

                        s_Messages.add(s_Message);
                        s_joinGroup.setMessages(s_Messages);
                        s_joinGroup.setSuccess(true);
                        s_outputThread.setMessage(s_joinGroup);
                    }
                    break;
                case SEND_SIGN_REQUEST:// 发起签到通知
                    Message ss_Message = readObject.getMessage();
                    System.out.println("SEND_JOIN_GROUP");
                    TranObject ss_joinGroup = new TranObject(TranObjectType.GET_SIGN_REQUEST);
                    List<String> outputList = groupDao.getMemberCount(ss_Message.getGroup_id());
                    ss_Message.setGroupCount(String.valueOf(outputList.size()));
                    System.out.println(outputList.size());
                    for (int i = 0; i < outputList.size(); i++) {
                        ArrayList<Message> ss_Messages = messageDao.getMessage(ss_Message, 0);
                        ss_joinGroup.setMessages(ss_Messages);
                        ss_joinGroup.setSuccess(true);
                        OutputThread ss_outputThread = map.getById(outputList.get(i));// 取出对应接受者的写线程
                        if (ss_outputThread != null) {
                            ss_outputThread.setMessage(ss_joinGroup);
                        }

                    }*/
                case SEND_GROUP_MESSAGE:
                    //GroupMessage req
                    GroupMessage sendGroupMessage = readObject.getSendGroupMessage();
                    if (sendGroupMessage.getContentType() == 1) {//普通消息
                        userDao.addOrdinaryMessage(sendGroupMessage);
                    } else {//签到消息
                        GroupSignInMessage signInMessage = readObject.getSignInfo();
                        userDao.addSignMessage(signInMessage);
                    }
                    //将群内离线用户添加一条离线信息
                    Set<String> allOfflineMembers = map.getOfflineGroupMember(readObject.getGroup().getGroupId());
                    for(String offlineMember : allOfflineMembers){
                        userDao.insertUnReceivedMessage(new UnReceivedMessage(sendGroupMessage.getGroupId(),offlineMember));
                    }
                    //发送消息给群内在线的成员 仅有一个通知 通知该成员有某个群的一条新消息
                    HashMap<String, OutputThread> outputThreads = OutputThreadMap.getInstance().getOnlineGroupMemberThread(readObject.getGroup().getGroupId());
                    for (String phoneNumber : outputThreads.keySet()) {
                        //System.out.println(phoneNumber);
                        TranObject tranObject = new TranObject(TranObjectType.SEND_GROUP_MESSAGE);
                        tranObject.setToUser(phoneNumber);
                        tranObject.setSendGroupMessage(sendGroupMessage);
                        tranObject.setSuccess(true);
                        outputThreads.get(phoneNumber).setMessage(tranObject, phoneNumber);
                    }
                /*case SEND_SIGN_RESPONSE:// 发送签到结果
                    GroupSignInMessage signInfo = readObject.getSignInfo();
                    boolean result = userDao.addSigninMessage(signInfo);
                    if (result) {
                        System.out.println("您已签到成功");
                    }
                default:
                    break;
            }
        }*/
            }
        }
    }
}