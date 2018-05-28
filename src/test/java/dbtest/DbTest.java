package dbtest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import dao.DBUtil;
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


	@After
	public void after() throws SQLException{
		mConnention.rollback();
		mConnention.close();
	}

}
