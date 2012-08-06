package asakichy.object_relational.structural.association_table_mapping;

import static asakichy.object_relational.structural.DB.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeMapperTest {
	@Test
	public void データ操作() throws Exception {
		// スキルマスタ登録
		SkillMapper.instance.insert(new Skill(0, "Java"));
		SkillMapper.instance.insert(new Skill(1, "JavaScript"));
		SkillMapper.instance.insert(new Skill(2, "Ruby"));
		SkillMapper.instance.insert(new Skill(3, "C#"));

		// 従業員マスタ登録・スキル付き
		List<Skill> skills = Arrays.asList(new Skill[] { new Skill(0), new Skill(2) });
		EmployeeMapper.instance.insert(new Employee(0, "山田", "太郎", skills));

		// 従業員マスタ検索
		Employee taro = EmployeeMapper.instance.find(0);

		// 従業員情報の確認
		assertThat(taro.getFirstName(), is("山田"));
		assertThat(taro.getLastName(), is("太郎"));

		// 従業員スキルの確認
		List<Skill> foundSkills = taro.getSkills();
		Collections.sort(foundSkills, new Comparator<Skill>() {
			@Override
			public int compare(Skill s1, Skill s2) {
				return (int) (s1.getId() - s2.getId());
			}
		});
		Skill skill = foundSkills.get(0);
		assertThat(skill.getId(), is(0L));
		assertThat(skill.getName(), is("Java"));
		skill = foundSkills.get(1);
		assertThat(skill.getId(), is(2L));
		assertThat(skill.getName(), is("Ruby"));
	}

	@Before
	public void 準備() throws SQLException {
		Statement stmt = createStatement();
		stmt.execute("DROP TABLE IF EXISTS employees");
		stmt.execute("CREATE TABLE employees ( id BIGINT PRIMARY KEY, firstname VARCHAR(32), lastname VARCHAR(32) )");

		stmt.execute("DROP TABLE IF EXISTS skills");
		stmt.execute("CREATE TABLE skills ( id BIGINT PRIMARY KEY, name VARCHAR(32) )");

		stmt.execute("DROP TABLE IF EXISTS employee_skills");
		stmt.execute("CREATE TABLE employee_skills ( employee_id BIGINT, skill_id BIGINT, PRIMARY KEY(employee_id, skill_id))");
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
