package servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import bean.budget;
import bean.precompetitiondata;
import bean.reimbursement;
import dao.BaseDao;
import dao.DaoUtil;
import handler.FileUpLoadHandler;

public class FileUpLoadServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String TNO=null;		//队伍编号
		String f_tn="";		//表名
		String Se_n="";		//二级目录名
		String Finalpath="";		//文件最终路径
		//1.创建工厂
		DiskFileItemFactory sff=new DiskFileItemFactory();
		//2.创建文件上传解析器
		ServletFileUpload upload=new ServletFileUpload(sff);
		//3.设置编码格式
		upload.setHeaderEncoding("UTF-8");
		//4.如果是以传统方式发送的数据，直接return
		if(!ServletFileUpload.isMultipartContent(req)){
			return ;
		}
		//5.狗仔文件路劲
		String basics=this.getServletContext().getRealPath("/WEB-INF/upload");
		//6.获取upload解析后的表单数据
		try {
			List<FileItem> fileitem=upload.parseRequest(req);
			//7.构筑二级目录
			for(FileItem item:fileitem){
				if(item.isFormField()){
					String name=item.getFieldName();
					if (name==null) {
						continue;
					}
					if(name.equals("TNO")){
						TNO=item.getString("UTF-8");
					}else if(name.equals("file_tableName")){
						f_tn=item.getString("UTF-8");
					}else if(name.equals("Second")){
						Se_n=item.getString("UTF-8");
					}
				}
			}
			if(TNO==null || f_tn==null){
				System.out.println("有数值为空请重试");
				return ;
			}
			//8.判断文件是否属于这三个数据库的子路径，并进行对应的处理
			if(f_tn.equals("budget")){
				String path=basics+"\\"+TNO+"\\"+f_tn+"\\";
				Finalpath=FileUpLoadHandler.UpLoad(path,f_tn,fileitem);
				budget b=new budget();
				System.out.println(TNO);
				b.setTno(TNO);
				b.setBfileurl(Finalpath);
				List<Object> list=new ArrayList<>();
				list.add(b);
				BaseDao.Insertcommit(list);
			}else if(f_tn.equals("reimbursement")){
				String path=basics+"\\"+TNO+"\\"+f_tn+"\\";
				Finalpath=FileUpLoadHandler.UpLoad(path,f_tn,fileitem);
				reimbursement r=new reimbursement();
				r.setTno(TNO);
				r.setRfileurl(Finalpath);
				List<Object> list=new ArrayList<>();
				list.add(r);
				BaseDao.Insertcommit(list);
			}else if(f_tn.equals("reimbursement")){
				String path=basics+"\\"+TNO+"\\"+f_tn+"\\";
				Finalpath=FileUpLoadHandler.UpLoad(path,f_tn,fileitem);
				precompetitiondata p=new precompetitiondata();
				p.setTno(TNO);
				Se_n=DaoUtil.BigToSmalle(Se_n);
				try {
					Method m=p.getClass().getMethod(DaoUtil.CreateGetOSet("set", Se_n), String.class);
					m.invoke(p, Finalpath);
					List<Object> list=new ArrayList<>();
					list.add(p);
					BaseDao.Insertcommit(list);
				} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}else{
				System.out.println("类型错误");
			}
		} catch (FileUploadException e) {
			System.out.println("UploadFailed");
			e.printStackTrace();
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	
	
}
