package handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bean.student;
import bean.teammember;
import dao.BaseDao;
import exception.TeamMenberMaxException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TeamMemberHandler {
	
	private static int MaxTeamMember=3;
	
	protected static int GetNowTeamMemberNum(String TNO){
		List<teammember> teammember=new ArrayList<>();
		try {
			teammember = BaseDao.Select("teammember",new String[]{"TNO"},new String[]{"TNO='"+TNO+"'"});
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return teammember.size();
	}
	
	/**
	 * 
	 * @param TNO
	 * @param stu
	 * @throws TeamMenberMaxException
	 */
	public static boolean InsertMenber(String TNO ,student stu) throws TeamMenberMaxException{
		if(GetNowTeamMemberNum(TNO)>=3){
			throw new TeamMenberMaxException();
		}
		teammember teammember=new teammember();
		teammember.setSno(stu.getSno());
		teammember.setTno(TNO);
		List<Object> prelist=new ArrayList<>();
		prelist.add(stu);
		prelist.add(teammember);
		if(BaseDao.Insertcommit(prelist)) return true;
		return false;
	}
	
	/**
	 * 
	 * @param TNA
	 * @return
	 */
	public static int GetTeamMember(String TNA){
		
		return 0;
	}
	
	/**
	 * 获取所有队员
	 * @param TNO
	 * @return
	 */
	public static List<Map> GetAllTeamMember(String TNO){
		List<Map> list=BaseDao.getDataWithCondition("teammember", new String[]{"SNO","IsPassed"}, new String[]{"TNO='"+TNO+"'"});
		return list;
	}
	
	public static JSONArray GetAllTeamMemberInf(String TNO){
		List<Map> SNOlist=GetAllTeamMember(TNO);
		JSONArray result=new JSONArray();
		for(Map SNO:SNOlist){
			JSONObject jo=new JSONObject();
			try {
				List<student> stu=BaseDao.Select("student", null, new String[]{"SNO='"+SNO.get("sno")+"'"});
				jo=JSONObject.fromObject(stu.get(0));
				jo.put("IsPassed", SNO.get("ispassed"));
				result.add(jo);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(GetAllTeamMemberInf("201701001"));
	}
}
