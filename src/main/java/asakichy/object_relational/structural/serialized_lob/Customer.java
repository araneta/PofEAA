package asakichy.object_relational.structural.serialized_lob;

import static asakichy.object_relational.structural.DB.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import asakichy.architecture.datasource_architecture.AppRuntimeException;

/**
 * テーブル「customers」のアクティブレコード.
 */
public class Customer {
	private static final String STATEMENT_FIND = "SELECT * FROM customers WHERE id = ?";
	private static final String STATEMENT_INSERT = "INSERT INTO customers VALUES ( ?, ?, ? )";

	private long id;
	private String name;
	private Department department;

	public Customer(long id, String name, Department department) {
		this.id = id;
		this.name = name;
		this.department = department;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Department getDepartment() {
		return department;
	}

	public void insert() {
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_INSERT);
			stmt.setLong(1, id);
			stmt.setString(2, name);

			String departmentXML = serializeDepartment(department);
			StringReader sr = new StringReader(departmentXML);
			stmt.setClob(3, sr, departmentXML.length());

			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		} catch (JAXBException e) {
			throw new AppRuntimeException(e);
		}
	}

	private static String serializeDepartment(Department department) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Department.class);
		Marshaller marshaller = context.createMarshaller();
		StringWriter sw = new StringWriter();
		marshaller.marshal(department, sw);
		return sw.toString();
	}

	public static Customer find(long id) {
		try {
			PreparedStatement stmt = prepareStatement(STATEMENT_FIND);
			stmt.setLong(1, id);
			ResultSet rs = stmt.executeQuery();
			rs.next();

			String name = rs.getString(2);
			Clob departmentClob = rs.getClob(3);
			String departmentXML = departmentClob.getSubString(1, (int) departmentClob.length());
			Department department = deserializeDepartment(departmentXML);
			return new Customer(id, name, department);
		} catch (SQLException e) {
			throw new AppRuntimeException(e);
		} catch (JAXBException e) {
			throw new AppRuntimeException(e);
		}
	}

	private static Department deserializeDepartment(String departmentXML) throws JAXBException {
		JAXBContext context = JAXBContext.newInstance(Department.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		StringReader sr = new StringReader(departmentXML);
		return (Department) unmarshaller.unmarshal(sr);
	}

}
