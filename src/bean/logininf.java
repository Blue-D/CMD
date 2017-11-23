package bean;
import java.sql.*;

public class logininf extends Bean implements Table{
private String usernum;
private String pass;
private int jur;

public String getUsernum(){ return usernum;}
public void setUsernum(String x){usernum=x;}
public String getPass(){ return pass;}
public void setPass(String x){pass=x;}
public int getJur(){ return jur;}
public void setJur(int x){jur=x;}

public logininf(){}

@Override
public String GetPrivateKey() {
	return "usernum";
}

}