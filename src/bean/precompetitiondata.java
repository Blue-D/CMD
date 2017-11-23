package bean;
import java.sql.*;

public class precompetitiondata extends Bean implements Table{
private String docurl;
private String tno;
private int issigningup;
private String cerurl;
private String phourl;
private int cerispassed;
private String reason;
private String videourl;

public String getDocurl(){ return docurl;}
public void setDocurl(String x){docurl=x;}
public String getTno(){ return tno;}
public void setTno(String x){tno=x;}
public int getIssigningup(){ return issigningup;}
public void setIssigningup(int x){issigningup=x;}
public String getCerurl(){ return cerurl;}
public void setCerurl(String x){cerurl=x;}
public String getPhourl(){ return phourl;}
public void setPhourl(String x){phourl=x;}
public int getCerispassed(){ return cerispassed;}
public void setCerispassed(int x){cerispassed=x;}
public String getReason(){ return reason;}
public void setReason(String x){reason=x;}
public String getVideourl(){ return videourl;}
public void setVideourl(String x){videourl=x;}

public precompetitiondata(){}
@Override
public String GetPrivateKey() {
	// TODO Auto-generated method stub
	return "tno";
}

}