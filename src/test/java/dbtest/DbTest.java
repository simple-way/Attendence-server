package dbtest;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.Test;

public class DbTest {

	@Test
	public void test() {
		assertThat(1, is(1));
		//fail("Not yet implemented");
	}

}
