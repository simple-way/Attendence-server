package model;

import dao.GroupDao;

import java.util.*;

/**
 * 存放写线程的
 */
public class OutputThreadMap {
	private HashMap<String, OutputThread> map;
	private static OutputThreadMap instance;

	// 私有构造器，防止被外面实例化改对像
	private OutputThreadMap() {
		map = new HashMap<String, OutputThread>();
	}

	// 单例模式像外面提供该对象
	public synchronized static OutputThreadMap getInstance() {
		if (instance == null) {
			instance = new OutputThreadMap();
		}
		return instance;
	}

	// 添加写线程的方法
	public synchronized void add(String phone, OutputThread out) {
		map.put(phone, out);
	}

	// 移除写线程的方法
	public synchronized void remove(String phone) {
		OutputThread outputThread = map.get(phone);
		outputThread.setStart(false);//终止线程 清理资源
	    map.remove(phone);
	}

	// 取出写线程的方法,群聊的话，可以遍历取出对应写线程
	public synchronized OutputThread getById(String id) {
		return map.get(id);
	}

	// 得到所有写线程方法，用于向所有在线用户发送广播
	public synchronized List<OutputThread> getAll() {
		List<OutputThread> list = new ArrayList<OutputThread>();
		for (Map.Entry<String, OutputThread> entry : map.entrySet()) {
			list.add(entry.getValue());
		}
		return list;
	}

	//获取所有在线的群成员输出线程
    public synchronized HashMap<String,OutputThread> getOnlineGroupMemberThread(int groupId){
        HashMap<String,OutputThread> hashMap = new HashMap<>();
        Set<String> allGroupMemberName = GroupDao.getGroupDao().getGroupMemberName(groupId);
        Set<String> allOnlineMemberName = map.keySet();
        allOnlineMemberName.retainAll(allGroupMemberName);
        for(String phoneNumber : allOnlineMemberName){
            hashMap.put(phoneNumber,map.get(phoneNumber));
        }
        return hashMap;
    }

    public synchronized Set<String> getOfflineGroupMember(int groupId){
        Set<String> allGroupMemberName = GroupDao.getGroupDao().getGroupMemberName(groupId);
        Set<String> allOnlineMemberName = map.keySet();
        allGroupMemberName.removeAll(allOnlineMemberName);
        return allGroupMemberName;
    }

}
