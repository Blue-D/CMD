package bean;
import java.sql.*;

public class budget extends Bean implements Table{
private Double budget;
private String reason;
private String tno;
private Integer ispassed;
private String bfileurl;

public Double getBudget(){ return budget;}
public void setBudget(Double x){budget=x;}
public String getReason(){ return reason;}
public void setReason(String x){reason=x;}
public String getTno(){ return tno;}
public void setTno(String x){tno=x;}
public Integer getIspassed(){ return ispassed;}
public void setIspassed(Integer x){ispassed=x;}
public String getBfileurl(){ return bfileurl;}
public void setBfileurl(String x){bfileurl=x;}

public budget(){}
@Override
public String GetPrivateKey() {
	return "tno";
}

}