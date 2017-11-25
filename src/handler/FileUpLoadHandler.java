package handler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.jasper.tagplugins.jstl.core.Out;

public class FileUpLoadHandler {

	public static int GetTableType(String f_tn) {
		String[] file_tableName = { "budget", "reimbursement", "precompetitiondata" };
		System.out.println(f_tn);
		for (int i = 0; i <= 1; i++) {
			if (file_tableName[i].equals(f_tn)) {
				return 1;
			}
		}
		if (file_tableName[2].equals(f_tn))
			return 2;
		return -1;
	}

	public static String UpLoad(String path, String f_tn, List<FileItem> fileitems) throws IOException {
		// 1.如果文件路径不存在则创建
		File file = new File(path);
		String pathname = "";
		if (!file.exists() && !file.isDirectory()) {
			System.out.println("目录不存在，需要创建");
			file.mkdirs();
		}
		for (FileItem item : fileitems) {
			// 2.判断当前item是否是文件数据
			if (item.isFormField() == false) {
				String filename = item.getName();
				if (filename == null && filename.equals("")) {
					continue;
				}
				InputStream in = item.getInputStream();
				filename = filename.substring(filename.lastIndexOf("\\") + 1);
				String fixStr = filename.substring(filename.lastIndexOf(".") + 1);
				pathname = path + f_tn + "." + fixStr;
				File outfile = new File(pathname);
				byte buffer[] = new byte[1024];
				if (outfile.exists() == false) {
					outfile.createNewFile();
				}
				int len = 0;
				FileOutputStream fout = new FileOutputStream(outfile);
				while ((len = in.read(buffer)) > 0) {
					fout.write(buffer, 0, len);
				}
				in.close();
				fout.close();
				item.delete();
			}
		}
		return pathname;
	}
}
