package com.wmc.AutomateApiDocs.utils.apidocs;

import java.net.URL;

/**
 * 路径工具类
 * 
 * @author 王明昌
 * @date 2017年10月18日
 */
public class PathUtil {
	/**
	 * 获取当前项目Class路径
	 * 
	 * @return
	 */
	public static String getCurrnetClassPath() {
		URL classpath = Thread.currentThread().getContextClassLoader().getResource("");
		String path = classpath.getPath();
		return path;
	}

	/**
	 * 返回当前项目的资源路径
	 * 
	 * @return
	 */
	public static String getResourcePath(String dirPath) {
        URL fileURL =PathUtil.class.getResource(dirPath);   
		return fileURL.getFile();
	}

	/**
	 * 获取项目所在路径
	 * 
	 * @return
	 */
	public static String getRealPath(Class<?> clz) {
		String realPath = clz.getClassLoader().getResource("").getFile();
		java.io.File file = new java.io.File(realPath);
		realPath = file.getAbsolutePath();
		try {
			realPath = java.net.URLDecoder.decode(realPath, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return realPath;
	}
	

	public static void main(String[] args) throws Exception {
	//	System.out.println(getResourcePath());

	}
}
