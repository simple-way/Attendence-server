package dbtest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import com.example.mrc.attendencesystem.entity.Group;
import com.example.mrc.attendencesystem.entity.GroupMessage;
import com.example.mrc.attendencesystem.entity.GroupSignInMessage;
import dao.DBUtil;
import dao.GroupDao;
import dao.UserDao;
import dbtest.dbtestutil.UserTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbTest {
	private Connection mConnention;
	@Before
	public void setup() throws SQLException{
		mConnention = DBUtil.getDBUtil().getConnection();
		mConnention.setAutoCommit(false);
	}

	@Test
	public void userOffLineTest() throws SQLException{
		//判断用户离线成功
		UserTestUtil.userOffLine(mConnention,"123");
		ResultSet rs = UserTestUtil.getUser(mConnention,"123");
		rs.first();
		int isOnline = rs.getInt("isonline");
		assertThat(isOnline, is(0));
	}

	@Test
	public void changeStateTest() throws SQLException{
		//判断用户上线更改成功
		UserTestUtil.changeStateOnline(mConnention,"123",1);
		ResultSet rs = UserTestUtil.getUser(mConnention,"123");
		rs.first();
		int isOnline = rs.getInt("isonline");
		assertThat(isOnline,is(1));
	}

	@Test
    public void addOrdinaryMessage() throws SQLException{
        GroupMessage groupMessage = new GroupMessage(2,"12",1,"abc");
        UserTestUtil.addOrdinaryMessage(mConnention,groupMessage);
        mConnention.commit();
    }
    @Test
    public void insertConfirmSignInRecord() throws SQLException{
        GroupSignInMessage signInMessage = new GroupSignInMessage();
        signInMessage.setEndTime(1529322712000L);
        signInMessage.setGroupId(2);
        signInMessage.setLatitude(34.824499);
        signInMessage.setLongitude(114.316553);
        signInMessage.setOriginatorId("12");
        signInMessage.setReceiverId("12");
        signInMessage.setRecordId(80);
        signInMessage.setRegion(200);
        signInMessage.setResult(1);
        signInMessage.setRlatitude(34.824507);
        signInMessage.setRlongitude(114.31658);
        signInMessage.setStartTime(1529324800249L);
        signInMessage.setState(false);
        signInMessage.setType(2);
        GroupDao.getGroupDao().insertConfirmSignInRecord(signInMessage);
    }
    @Test
    public void getGroups(){
        System.out.println(GroupDao.getGroupDao().getGroups("r").size());
    }

    @Test
    public void joinGroup(){
        Group group = new Group(null,null,0);
        group.setGroupId(2);
        System.out.println(UserDao.getUserDao().joinGroup("18738997273",group));
    }

	@After
	public void after() throws SQLException{
		//mConnention.rollback();
		mConnention.close();
	}

}
