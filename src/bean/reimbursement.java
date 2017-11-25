package bean;
import java.sql.*;

public class reimbursement extends Bean implements Table{
private Double rmoney;
private String rfileurl;
private String tno;
private Double smoney;
private int ispassed;

public Double getRmoney(){ return rmoney;}
public void setRmoney(Double x){rmoney=x;}
public String getRfileurl(){ return rfileurl;}
public void setRfileurl(String x){rfileurl=x;}
public String getTno(){ return tno;}
public void setTno(String x){tno=x;}
public Double getSmoney(){ return smoney;}
public void setSmoney(Double x){smoney=x;}
public int getIspassed(){ return ispassed;}
public void setIspassed(int x){ispassed=x;}

public reimbursement(){}
@Override
public String GetPrivateKey() {
	// TODO Auto-generated method stub
	return "tno";
}

}