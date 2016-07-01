package com.softserve.edu.hb.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.softserve.edu.hb.dao.RoleDao;
import com.softserve.edu.hb.dao.UserDao;
import com.softserve.edu.hb.dao.UserRoleViewDao;
import com.softserve.edu.hb.db.ConnectionManager;
import com.softserve.edu.hb.dto.UserDTO;
import com.softserve.edu.hb.entity.RoleDB;
import com.softserve.edu.hb.entity.UserDB;
import com.softserve.edu.hb.entity.UserRoleView;
import com.softserve.edu.hb.entity.UserRoleView.UserRoleViewFields;

public class UserIUService {
	private final static String ROLE_FIELDNAME = "name";
	protected final static String TRANSACTION_ERROR = "Transaction Error, Rollback";
	//
    private static volatile UserIUService instance = null;
    // Singleton testing is Very Difficult !!!

    private UserIUService() {
    }

    public static UserIUService get() {
        if (instance == null) {
            synchronized (UserIUService.class) {
                if (instance == null) {
                    instance = new UserIUService();
                }
            }
        }
        return instance;
    }

	// Create
	public boolean insertUser(UserDTO userDTO) {
		boolean result = false;
		ConnectionManager.getInstance().beginTransaction();
		List<RoleDB> roles = RoleDao.get().getByFieldName(ROLE_FIELDNAME, userDTO.getRoleName());
		// TODO Password Default
		try {
			result = UserDao.get().insert(new UserDB(0, roles.get(0).getId(),
					userDTO.getLogin(), userDTO.getLogin()));
		} catch (Exception e) {
			ConnectionManager.getInstance().rollbackTransaction();
			throw new RuntimeException(TRANSACTION_ERROR, e);
		}
		ConnectionManager.getInstance().commitTransaction();
        //return UserDao.get().insert(ServiceUtils.get().user2UserDB(userDTO));
		return result;
	}
    
    // Read
    public List<UserDTO> getAllUsers() {
    	List<UserDTO> result = new ArrayList<UserDTO>();
    	ConnectionManager.getInstance().beginTransaction();
    	for (UserRoleView userRoleView : UserRoleViewDao.get().getAll()) {
    		result.add(new UserDTO("", "", userRoleView.getLogin(),
    				"", "", "", "", "", "",
    				userRoleView.getRoleName(), "", "", null));
    	}
		ConnectionManager.getInstance().commitTransaction();
    	return result;
    }

    public List<UserDTO> getConcreteUsers(int partNumber, int partSize,
    			String partialLogin, String partialPassword) {
    	List<UserDTO> result = new ArrayList<UserDTO>();
    	ConnectionManager.getInstance().beginTransaction();
    	Map<String,String> filters = new HashMap<String,String>();
    	filters.put(UserDB.UserDBFields.LOGIN.toString(), partialLogin);
    	filters.put(UserDB.UserDBFields.PASSWD.toString(), partialPassword);
    	for (UserDB userDB : UserDao.get()
    			.getFilterRange((partNumber-1)*partSize, partSize, filters) ) {
    		result.add(new UserDTO("", userDB.getId().toString(), userDB.getLogin(),
    				"", "", "", "", "", "",
    				userDB.getPasswd(), "", "", null));
    	}
		ConnectionManager.getInstance().commitTransaction();
    	return result;
    }

    public List<UserDTO> getUsersByRole(String roleName) {
    	List<UserDTO> result = new ArrayList<UserDTO>();
    	for (UserRoleView userRoleView : UserRoleViewDao.get()
    			.getByFieldName(UserRoleViewFields.ROLES_NAME.toString(), roleName)  ) {
    		result.add(new UserDTO("", "", userRoleView.getLogin(),
    				"", "", "", "", "", "",
    				userRoleView.getRoleName(), "", "", null));
    	}
    	return result;
    }

//    public UserDTO getUserByLogin(String login) {
        //UserDB userDB = UserDao.get().getUserDBByLogin(login);
//        UserDB userDB = UserDao.get().getUserDBByLogin(login);
//        return ServiceUtils.get().userDB2IUser(userDB);
//        return ServiceUtils.get().userDB2IUser(userDB,
//        		RoleService.get().getRoleNameById(userDB.getRoleRef()));
//    }

//    public String getUserFirstnameByLogin(String login) {
//        //return UserDao.get().getUserDBByLogin(login).getFirstname();
//        return UserDao.get().getUserDBByLogin(login).getFirstname();
//    }

//    public Long getUserIdByLogin(String login) {
//        return UserDao.get().getUserDBByLogin(login).getUserId();
//    }

    // Delete
//    public boolean deleteUsersByLogin(String login) {
//        //return UserDao.get().delete(UserDao.get().getUserDBByLogin(login));  
//        return UserDao.get().delete(UserDao.get().getUserDBByLogin(login));  
//    }

}