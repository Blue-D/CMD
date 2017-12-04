package bean;
import java.sql.*;

public class competition extends Bean implements Table{
private String sog;
private String level;
private String cno;
private String cna;
private int reginpro;
private int maxteammember;

public String getSog(){ return sog;}
public void setSog(String x){sog=x;}
public String getLevel(){ return level;}
public void setLevel(String x){level=x;}
public String getCno(){ return cno;}
public void setCno(String x){cno=x;}
public String getCna(){ return cna;}
public void setCna(String x){cna=x;}
public int getReginpro() {	return reginpro;}
public void setReginpro(int reginpro) {	this.reginpro = reginpro;}
public int getMaxteammember() {
	return maxteammember;
}
public void setMaxteammember(int maxteammember) {
	this.maxteammember = maxteammember;
}

public competition(){}
@Override
public String GetPrivateKey() {
	return "cno";
}

}