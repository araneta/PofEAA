package asakichy.object_relational.structural.embedded_value;

import static asakichy.object_relational.structural.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Locale;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import asakichy.object_relational.structural.AppRuntimeException;

public class EmployeeMapperTest {

	@Test
	public void データ検索() throws Exception {
		Employee taro = EmployeeMapper.instance.find(0);
		assertThat(taro.getName(), is("山田太郎"));
		assertThat(taro.getPeriod().getStart(), is(getDate(2000, Calendar.APRIL, 1)));
		assertThat(taro.getPeriod().getEnd(), is(getDate(2012, Calendar.MARCH, 31)));
		assertThat(taro.getSalary().getAmount(), is(2000L));
		assertThat(taro.getSalary().getCurrency(), is("$"));
	}

	@Test
	public void データ登録() throws Exception {
		DateRange period = new DateRange(getDate(1990, Calendar.APRIL, 1), getDate(2000, Calendar.MARCH, 31));
		Money salary = new Money(200000, "\\");
		Employee hanako = new Employee(1, "山田花子", period, salary);
		EmployeeMapper.instance.insert(hanako);

		Employee insertedHanako = EmployeeMapper.instance.find(1);
		assertThat(insertedHanako, sameInstance(hanako));

		EmployeeMapper.instance.refresh();
		Employee foundHanako = EmployeeMapper.instance.find(1);
		assertThat(foundHanako.getName(), is("山田花子"));
		assertThat(foundHanako.getPeriod().getStart(), is(getDate(1990, Calendar.APRIL, 1)));
		assertThat(foundHanako.getPeriod().getEnd(), is(getDate(2000, Calendar.MARCH, 31)));
		assertThat(foundHanako.getSalary().getAmount(), is(200000L));
		assertThat(foundHanako.getSalary().getCurrency(), is("\\"));
	}

	@Test
	public void データ更新() throws Exception {
		DateRange period = new DateRange(getDate(1990, Calendar.APRIL, 1), getDate(2000, Calendar.MARCH, 31));
		Money salary = new Money(200000, "\\");
		Employee hanako = new Employee(0, "山田花子", period, salary);
		EmployeeMapper.instance.update(hanako);

		Employee updatedHanako = EmployeeMapper.instance.find(0);
		assertThat(updatedHanako, sameInstance(hanako));

		EmployeeMapper.instance.refresh();
		Employee foundHanako = EmployeeMapper.instance.find(0);
		assertThat(foundHanako.getName(), is("山田花子"));
		assertThat(foundHanako.getPeriod().getStart(), is(getDate(1990, Calendar.APRIL, 1)));
		assertThat(foundHanako.getPeriod().getEnd(), is(getDate(2000, Calendar.MARCH, 31)));
		assertThat(foundHanako.getSalary().getAmount(), is(200000L));
		assertThat(foundHanako.getSalary().getCurrency(), is("\\"));
	}

	@Test(expected = AppRuntimeException.class)
	public void データ削除() throws Exception {
		EmployeeMapper.instance.delete(new Employee(0));
		EmployeeMapper.instance.find(0);
	}

	static public Date getDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance(Locale.JAPAN);
		cal.clear();
		cal.set(year, month, day);
		return new Date(cal.getTimeInMillis());
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS employees");
		stmt.execute("CREATE TABLE employees ( id BIGINT PRIMARY KEY, name VARCHAR(32), "
				+ "start DATE, end DATE, salary_amount BIGINT, salary_currency VARCHAR(1) )");

		stmt.execute("INSERT INTO employees VALUES(0,'山田太郎','2000-04-01','2012-03-31',2000,'$')");
	}

	@BeforeClass
	public static void DB前処理() throws Exception {
		init();
	}

	@AfterClass
	public static void DB後処理() throws Exception {
		terminate();
	}

}
