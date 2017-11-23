package bean;
import java.sql.*;
import java.time.Year;

public class prizer extends Bean implements Table{
private String tno;
private Year year;
private String cno;
private String prize;

public String getTno(){ return tno;}
public void setTno(String x){tno=x;}
public Year getYear(){ return year;}
public void setYear(Year x){year=x;}
public String getCno(){ return cno;}
public void setCno(String x){cno=x;}
public String getPrize(){ return prize;}
public void setPrize(String x){prize=x;}

public prizer(){}
@Override
public String GetPrivateKey() {
	// TODO Auto-generated method stub
	return "tno";
}

}