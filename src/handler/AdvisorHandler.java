package handler;

import java.util.ArrayList;
import java.util.List;

import bean.advisor;
import dao.BaseDao;

public class AdvisorHandler {
	
	public static boolean InsertAdcisor(advisor ad){
		List<Object> list=new ArrayList<>();
		list.add(ad);
		if(BaseDao.Insertcommit(list)) return true;
		return false;
	}
	
	
}
