package handler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.competition;
import bean.teammember;
import dao.BaseDao;
import exception.CompetitionExistedException;
import exception.CompetitionNotExistedException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CompetitionHander {
	
	/**
	 * 获取正在报名中的比赛
	 * @return
	 */
	public static JSONObject GetCanSignUpCompetition(){
		JSONArray CNA=new JSONArray();
		JSONArray CNO=new JSONArray();
		List<competition> competitions=new ArrayList<>();
		try {
			competitions = BaseDao.Select("competition",new String[]{"CNA,CNO"},new String[]{"reginpro=1"});
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		for(competition c:competitions){
			CNA.add(c.getCna());
			CNO.add(c.getCno());
		}
		JSONObject jo=new JSONObject();
		jo.put("CNA", CNA);
		jo.put("CNO", CNO);
		return jo;
	}
	/**
	 * 获取所有比赛信息
	 * @return
	 */
	public static JSONObject GetALLCompetition(){
		List<competition> competitions=new ArrayList<>();
		try {
			competitions = BaseDao.Select("competition",null,null);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		JSONObject jo=new JSONObject();
		jo.put("CNA", JSONArray.fromObject((competitions)));
		return jo;
	}
	/**
	 * 创建竞赛
	 * @param CNA
	 * @param SOG
	 * @param level
	 * @return
	 * @throws CompetitionExistedException
	 */
	public static boolean CreateCompetion(String CNA,String SOG,String level,int MaxTeamMember) throws CompetitionExistedException{
		if(CompetionExist(CNA)==true){
			throw new CompetitionExistedException();
		}
		competition com=new competition();
		com.setCna(CNA);
		com.setSog(SOG);
		com.setLevel(level);
		com.setReginpro(0);
		com.setMaxteammember(MaxTeamMember);
		if(BaseDao.Insert(com)==1) return true;
		else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param CNA
	 * @return
	 */
	public static boolean CompetionExist(String CNA){
		String condtion = "CNA='"+CNA+"'";
		return BaseDao.getDataWithCondition("competition", new String[]{"CNO"}, new String[]{condtion}).size()>0;
	}
	/**
	 * 开启比赛
	 * @param TNA
	 * @return
	 * @throws CompetitionNotExistedException 
	 */
	public static boolean OpenCompetition(String CNA) throws CompetitionNotExistedException {
		if(CompetionExist(CNA)==false){
			throw new CompetitionNotExistedException();
		}
		competition com=new competition();
		com.setReginpro(1);
		if(BaseDao.Update(com, new String[]{"CNA='"+CNA+"'"})==0) return false;
		return true;
	}
	
	/**
	 * 关闭比赛
	 * @param TNA
	 * @return
	 * @throws CompetitionNotExistedException 
	 */
	public static boolean CloseCompetition(String CNA) throws CompetitionNotExistedException {
		if(CompetionExist(CNA)==false){
			throw new CompetitionNotExistedException();
		}
		competition com=new competition();
		com.setReginpro(0);
		if(BaseDao.Update(com, new String[]{"CNA='"+CNA+"'"})==0) return false;
		return true;
	}
	
}
