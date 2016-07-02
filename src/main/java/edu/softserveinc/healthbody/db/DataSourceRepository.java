package edu.softserveinc.healthbody.db;

import edu.softserveinc.healthbody.exceptions.JDBCDriverException;

public final class DataSourceRepository {
		
	private static volatile DataSourceRepository instance = null;
	

	private DataSourceRepository() {

	}
	
	public static DataSourceRepository getInstance() {
		if(instance == null) {
			synchronized (DataSourceRepository.class) {
				if(instance == null) {
					instance = new DataSourceRepository();
				}
			}
		}
		return instance;
	}
	
	public DataSource getPostgresLocalHost() throws JDBCDriverException {
		return new DataSource(DriverRepository.getInstance().getPostgresDriver(),
				"jdbc:postgresql://localhost:5432/healthbodydb", "postgres", "root");
	}
}