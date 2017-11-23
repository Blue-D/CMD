package bean;
import java.sql.*;

public class teammember extends Bean implements Table{
private String sreason;
private String tno;
private String sno;
private int sispassed;

public String getSreason(){ return sreason;}
public void setSreason(String x){sreason=x;}
public String getTno(){ return tno;}
public void setTno(String x){tno=x;}
public String getSno(){ return sno;}
public void setSno(String x){sno=x;}
public int getSispassed(){ return sispassed;}
public void setSispassed(int x){sispassed=x;}

public teammember(){}
@Override
public String GetPrivateKey() {
	// TODO Auto-generated method stub
	return "tno;sno";
}

}