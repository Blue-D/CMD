package bean;
import java.sql.*;

public class advisor extends Bean implements Table{
private String atele;
private String tno;
private String ana;
private String aemail;

public String getAtele(){ return atele;}
public void setAtele(String x){atele=x;}
public String getTno(){ return tno;}
public void setTno(String x){tno=x;}
public String getAna(){ return ana;}
public void setAna(String x){ana=x;}
public String getAemail(){ return aemail;}
public void setAemail(String x){aemail=x;}

public advisor(){}

@Override
public String GetPrivateKey() {
	return "tno;ana";
}

}