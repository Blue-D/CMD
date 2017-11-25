package bean;
import java.sql.*;

public class teammember extends Bean implements Table{
private String reason;
private String tno;
private String sno;
private int ispassed;

public String getReason(){ return reason;}
public void setReason(String x){reason=x;}
public String getTno(){ return tno;}
public void setTno(String x){tno=x;}
public String getSno(){ return sno;}
public void setSno(String x){sno=x;}
public int getIspassed(){ return ispassed;}
public void setIspassed(int x){ispassed=x;}

public teammember(){}
@Override
public String GetPrivateKey() {
	// TODO Auto-generated method stub
	return "tno;sno";
}

}