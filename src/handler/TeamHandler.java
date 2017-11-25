package handler;


import java.sql.Date;
import java.sql.ResultSet;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import bean.budget;
import bean.logininf;
import bean.reimbursement;
import bean.student;
import bean.team;
import bean.teammember;
import dao.BaseDao;
import exception.TeamExistedException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TeamHandler {
	
	
	
	/**
	 * 创建队伍编号
	 * @param TeamName
	 * @param CompetionNo
	 * @return
	 */
	public static String CreateTeamNo(String TeamName,String CompetionNo) {
		//用户名创建规则，年份+比赛id+三位自增数
		Calendar c=Calendar.getInstance();
		c.setTime(new java.util.Date());
		String condition="CNO='"+CompetionNo+"'and Year="+c.get(c.YEAR);
		List<Map> listmap=dao.BaseDao.getDataWithCondition("team", new String[]{"TNO"},new String[]{condition});
		String tno=c.get(c.YEAR)+CompetionNo+String.format("%03d", listmap.size()+1);
		return tno;
	}
	
	public static boolean IsTeamExist(String TeamName,String Competion){
		Calendar c=Calendar.getInstance();
		c.setTime(new java.util.Date());
		String condition="TNA='"+TeamName+"'and CNO='"+Competion+"'and Year="+c.get(c.YEAR);
		List<Map> listmap=dao.BaseDao.getDataWithCondition("team", new String[]{"TNO"},new String[]{condition});
		if(listmap.size()>0) return true;
		return false;
	}
	
	/**
	 * 创建队伍
	 * @param t
	 * @param stu
	 * @return
	 * @throws TeamExistedException
	 */
	public static logininf CreateTeam(team t,student stu) throws TeamExistedException{
		//1.判断队伍石佛存在
		if(IsTeamExist(t.getTna(),t.getCno())){
			throw new TeamExistedException();
		}
		//2.创建队名
		String tno=CreateTeamNo(t.getTna(), t.getCno());
		t.setTno(tno);
		t.setYear(Year.now());
		t.setTispassed(0);
		//3.创建账号
		logininf lgif=new logininf();
		lgif.setJur(1);
		lgif.setPass(CreatePass());
		lgif.setUsernum(tno);
		//4.创建队员信息
		teammember tm=new teammember();
		tm.setIspassed(0);
		tm.setSno(stu.getSno());
		tm.setTno(tno);
		//5.创建预算与报销信息以及对应的文件路径
		budget bug=new budget();
		bug.setIspassed(0);
		bug.setTno(tno);
		reimbursement reim=new reimbursement();
		reim.setIspassed(0);
		reim.setTno(tno);
		//6.以事务的方式插入数据库
		List<Object> list=new ArrayList<>();
		list.add(t);
		list.add(lgif);
		list.add(stu);
		list.add(tm);
		list.add(bug);
		list.add(reim);
		//7.判断是否插入成功{
		if(dao.BaseDao.Insertcommit(list)) return lgif;
		else{
			return null;
		}
	}
	
	
	/**
	 * 根据队伍编号获取队伍信息 
	 * @param TNA
	 * @return
	 */
	public static JSONObject GetTeamInf(String TNO){
		List<Map> teamlist;
		try {
			teamlist = BaseDao.Select("team",null,new String[]{"TNO='"+TNO+"'"});
			JSONObject jo=JSONObject.fromObject(teamlist.get(0));
			jo.putAll(TeamExam(TNO));
			return	jo ;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static JSONObject TeamExam(String TNO){
		List<Map> stulist=BaseDao.getDataWithCondition("teammember", new String[]{"SNO","IsPassed"}, new String[]{"TNO='"+TNO+"'"});
		List<Map> buglist=BaseDao.getDataWithCondition("budget", new String[]{"IsPassed"}, new String[]{"TNO='"+TNO+"'"});
		JSONObject jo=new JSONObject();
		JSONArray stureason=new JSONArray();
		int flag=1;
		for(Map map:stulist){
			if(map.get("ispassed").equals("0")){
				flag=0;
				stureason.add(map.get("sno"));
			}
		}
		int bud=0;
		for(Map map:buglist){
			if(map.get("ispassed").equals("0")) {
				flag=0;
				bud=1;
			}
		}
		if(flag==1){
			jo.put("ispassed", flag);
		}else{
			if(stureason.size()>0 && bud==1){
				jo.put("reason", "存在未通过的队员,且预算未通过");
			}else if(stureason.size()>0){
				jo.put("reason", "存在未通过的队员");
			}else{
				jo.put("reason", "预算未通过");
			}
		}
		return jo;
	}
	
	
	/**
	 * 创建密码
	 * @return
	 */
	public static String CreatePass(){
		int pass=(int) (Math.random()*100000000+1);
		return String.format("%09d", pass);
	}
	
	public static void main(String[] args) {
		System.out.println(GetTeamInf("201701001"));
	}

}
