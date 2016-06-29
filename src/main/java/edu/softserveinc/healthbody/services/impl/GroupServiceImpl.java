package edu.softserveinc.healthbody.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.softserveinc.healthbody.dao.GroupDao;
import edu.softserveinc.healthbody.dto.GroupDTO;
import edu.softserveinc.healthbody.entity.Group;
import edu.softserveinc.healthbody.exceptions.CloseStatementException;
import edu.softserveinc.healthbody.exceptions.DataBaseReadingException;
import edu.softserveinc.healthbody.exceptions.EmptyResultSetException;
import edu.softserveinc.healthbody.exceptions.JDBCDriverException;
import edu.softserveinc.healthbody.exceptions.QueryNotFoundException;
import edu.softserveinc.healthbody.services.GroupService;

public class GroupServiceImpl implements GroupService{
	
	private enum GroupServiceKeys {
		NAME("name"),
		COUNT("count"),
		DESCRIPTION("description"),
		SCORE_GROUP("scoreGroup");

		private String keys;

		private GroupServiceKeys(String keys) {
			this.keys = keys;
		}

		@Override
		public String toString() {
			return keys;
		}

	}
	
	private static final String GROUP_NAME = "name";
	
	private static volatile GroupServiceImpl instance = null;
	
	private GroupServiceImpl() {
	}
	
	public static GroupServiceImpl getInstance() {
		if(instance == null) {
			synchronized (GroupServiceImpl.class) {
				if (instance == null) {
					instance = new GroupServiceImpl();
				}
			}
		}
		return instance;
	}
	

	@Override
	public List<GroupDTO> getAll(int partNumber, int partSize, Map<String, String> filters) throws QueryNotFoundException, 
					JDBCDriverException, DataBaseReadingException, EmptyResultSetException, CloseStatementException {
		
		fillFilters(filters);
		List<GroupDTO> groupDTOs = new ArrayList<GroupDTO>();
		for (Group group : GroupDao.get().getFilterRange((partNumber - 1) * partSize, partSize, filters)) {
			groupDTOs.add(new GroupDTO(group.getName(), String.valueOf(group.getCount()), group.getDescription(), group.getScoreGroup()));
		}
		
		return groupDTOs;	
	}
	
	@Override
	public GroupDTO getGroup(String name) throws QueryNotFoundException, JDBCDriverException, DataBaseReadingException, CloseStatementException{
		 Group group = GroupDao.get().getGroupByName(name);
		 return new GroupDTO(group.getName(), String.valueOf(group.getCount()), group.getDescription(), group.getScoreGroup());
	}
	
	
	@Override
	public String getDescriptionOfGroup(GroupDTO groupDTO) {
		return groupDTO.getDescriptions();
	}
	
	@Override
	public void update(GroupDTO groupDTO, String count, String description, String scoreGroup) throws QueryNotFoundException, JDBCDriverException, DataBaseReadingException, EmptyResultSetException, CloseStatementException {
		GroupDao.get().editGroup(GroupDao.get().getByField(GROUP_NAME, groupDTO.getName()).get(0), count, description, scoreGroup);
	}
	

	private void fillFilters(Map<String, String> filters) {
		filters.put(GroupServiceKeys.NAME.toString(), "name");
		filters.put(GroupServiceKeys.COUNT.toString(), "count");
		filters.put(GroupServiceKeys.DESCRIPTION.toString(), "description");
		filters.put(GroupServiceKeys.SCORE_GROUP.toString(), "scoreGroup");
	}


	@Override
	public void update(List<GroupDTO> baseDTOs) {
		// TODO Auto-generated method stub
		
	}




}