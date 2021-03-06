package edu.softserveinc.healthbody;

import static org.testng.Assert.fail;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import edu.softserveinc.healthbody.db.ConnectionManager;
import edu.softserveinc.healthbody.db.DBCreationManager;
import edu.softserveinc.healthbody.db.DataSourceRepository;
import edu.softserveinc.healthbody.exceptions.JDBCDriverException;

public class CreateDatabaseTestOpenShift extends CreateDropTestDatabase {
private static Logger logger = LoggerFactory.getLogger(CreateDatabaseTestOpenShift.class.getName());
 	
 	@BeforeSuite
 	@Parameters("testDatabase")
 	@Override
 	public void createTestDatabase(@Optional("healthbodydbtest") String testDatabase) {
 		logger.info("Setting up database " + testDatabase + ".");
		try (Connection con = ConnectionManager
 				.getInstance(DataSourceRepository.getInstance().getPostgresJenkins()).getConnection();
 				Statement st = con.createStatement()) {
 			if (!DBCreationManager.getInstance().dropDatabase(st, testDatabase)) {
 				String failMessage = "Database " + testDatabase + " does not exist.";
 				logger.info(failMessage);
 
 			}
 			if (!DBCreationManager.getInstance().createDatabase(st, testDatabase)) {
 				String failMessage = "Couldn't create database " + testDatabase + ".";
 				logger.error(failMessage);
 				fail(failMessage);
 			}
 		} catch (SQLException e) {
 			String failMessage = "Problem with deleting/creating database " + testDatabase + ".";
 			logger.error(failMessage, e);
 			fail(failMessage, e);
 		} catch (JDBCDriverException e) {
 			String failMessage = "Couldn't get connection.";
 			logger.error(failMessage, e);
 			fail(failMessage, e);
 		}
 			logger.info("Setting up database ends successfully...");
 	}
 	
 	@AfterSuite
 	@Parameters("testDatabase")
 	@Override
 	public void dropTestDatabase(@Optional("healthbodydbtest") String testDatabase) {
 		Connection con = null;
 		try {
 			con = ConnectionManager.getInstance(DataSourceRepository.getInstance().getPostgresJenkins())
					.getConnection();
 		} catch (JDBCDriverException e) {
 			String failMessage = "Couldn't get connection.";
 			logger.error(failMessage, e);
 			fail(failMessage, e);
 		}
 		try (Statement st = con.createStatement()) {
 			if (!DBCreationManager.getInstance().dropDatabase(st, testDatabase)) {
 				String failMessage = "Couldn't delete database " + testDatabase + ".";
 				logger.error(failMessage);
 				fail(failMessage);
 			} else {
 				logger.info("Database " + testDatabase + " was deleted.");
 			}
 		} catch (SQLException e) {
			String failMessage = "Problem with deleting database " + testDatabase + ".";
 			logger.error(failMessage, e);
 			fail(failMessage, e);
 		}
 	}

}
