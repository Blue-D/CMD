package bean;

import java.time.Year;

public class team extends Bean implements Table{
private String tno;
private Year year;
private String cno;
private String tna;
private int tispassed;
private String captainsno;

public String getTno(){ return tno;}
public void setTno(String x){tno=x;}
public Year getYear(){ return year;}
public void setYear(Year x){year=x;}
public String getCno(){ return cno;}
public void setCno(String x){cno=x;}
public String getTna(){ return tna;}
public void setTna(String x){tna=x;}
public int getTispassed(){ return tispassed;}
public void setTispassed(int x){tispassed=x;}
public String getCaptainsno(){ return captainsno;}
public void setCaptainsno(String x){captainsno=x;}

public team(){}
@Override
public String GetPrivateKey() {
	// TODO Auto-generated method stub
	return "tno";
}

}