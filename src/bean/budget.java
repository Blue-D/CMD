package bean;
import java.sql.*;

public class budget extends Bean implements Table{
private Double budget;
private String breason;
private String tno;
private Integer bispassed;
private String bfileurl;

public Double getBudget(){ return budget;}
public void setBudget(Double x){budget=x;}
public String getBreason(){ return breason;}
public void setBreason(String x){breason=x;}
public String getTno(){ return tno;}
public void setTno(String x){tno=x;}
public Integer getBispassed(){ return bispassed;}
public void setBispassed(Integer x){bispassed=x;}
public String getBfileurl(){ return bfileurl;}
public void setBfileurl(String x){bfileurl=x;}

public budget(){}
@Override
public String GetPrivateKey() {
	return "tno";
}

}