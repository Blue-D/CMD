package util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class Util {
	
	/**
	 * 使用javaCompiler编译目标文件并存放于Bin中
	 * @param newfile
	 * @return
	 */
	public static boolean Comliper(File newfile){
		JavaCompiler jc=ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = jc.getStandardFileManager(null, null, null);
		File[] files=new File[1];
		files[0]=newfile;
		Iterable<? extends JavaFileObject> JCtask=fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
		List<String> options=new ArrayList();
		options.add("-d");
		options.add("build/classes");
		boolean result=jc.getTask(null, fileManager, null, options, null, JCtask).call();
		try {
			fileManager.close();
		} catch (IOException e) {
		}
		return result;
	}
}
