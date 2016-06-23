package edu.softserveinc.healthbody.testapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.softserveinc.healthbody.dto.CompetitionDTO;
import edu.softserveinc.healthbody.exceptions.CloseStatementException;
import edu.softserveinc.healthbody.exceptions.DataBaseReadingException;
import edu.softserveinc.healthbody.exceptions.EmptyResultSetException;
import edu.softserveinc.healthbody.exceptions.JDBCDriverException;
import edu.softserveinc.healthbody.exceptions.QueryNotFoundException;
import edu.softserveinc.healthbody.services.impl.CompetitionsServiceImpl;

public class TestAppl {
	// TODO
	private static Logger logger = LogManager.getLogger(TestAppl.class.getName());
	private static Connection con = null;
	private static String username = "postgres";
	private static String password = "root";
	private static String URL = "jdbc:postgresql://localhost:5432/";
	private static String databaseName = "healthbodydb";

	public static void main(String[] args) throws SQLException, ClassNotFoundException, QueryNotFoundException, JDBCDriverException, DataBaseReadingException, EmptyResultSetException, CloseStatementException {

		
		CompetitionsServiceImpl cs = new CompetitionsServiceImpl();

		List<CompetitionDTO> ls1 = cs.getAllActive(1, 20);
		List<CompetitionDTO> ls2 = cs.getAllActiveByUser(1, 20, "Login 7");
		List<CompetitionDTO> ls3 = cs.getAllByUser(1, 20, "Login 7");
		List<CompetitionDTO> ls4 = cs.getAll(1, 20);
		System.out.println("In getAllActive: " + Arrays.toString(ls1.toArray()));
		System.out.println("In getAllActiveByUser: " + Arrays.toString(ls2.toArray()));
		System.out.println("In getAllByUser: " + Arrays.toString(ls3.toArray()));
		System.out.println("In getAll: " + Arrays.toString(ls4.toArray()));

		logger.info("TestAppl starts...");
		DriverManager.registerDriver(new org.postgresql.Driver());

		con = DriverManager.getConnection(URL, username, password);
		if (con != null) {
			logger.info("Connection Successful! \n");
		}
		if (con == null) {
			System.exit(0);
		}
		Statement st = con.createStatement();
//		createDatabase(st, databaseName);

		con = DriverManager.getConnection(URL + databaseName, username, password);
		logger.info("Using database...");
//		createTables(st, databaseName);
		st = con.createStatement();

		
		
		if (st != null) {
			st.close();
		}
		if (con != null) {
			con.close();
		}
	}
}