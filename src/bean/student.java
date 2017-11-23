package bean;
import java.sql.*;

public class student extends Bean implements Table{
private String major;
private String sno;
private String sna;
private String id;
private String tele;
private String email;

public String getMajor(){ return major;}
public void setMajor(String x){major=x;}
public String getSno(){ return sno;}
public void setSno(String x){sno=x;}
public String getSna(){ return sna;}
public void setSna(String x){sna=x;}
public String getId(){ return id;}
public void setId(String x){id=x;}
public String getTele(){ return tele;}
public void setTele(String x){tele=x;}
public String getEmail(){ return email;}
public void setEmail(String x){email=x;}

public student(){}
@Override
public String GetPrivateKey() {
	// TODO Auto-generated method stub
	return "sno";
}

}