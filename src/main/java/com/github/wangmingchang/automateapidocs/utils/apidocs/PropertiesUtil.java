package com.github.wangmingchang.automateapidocs.utils.apidocs;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * properties配置文件工具类
 * 
 * @author 王明昌
 * @since 2017年9月16日
 */
public class PropertiesUtil {
	/**
	 * 加载属性文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 加载属性文件
	 */
	public static Properties loadProps(String filePath) {
		Properties properties = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(filePath));
			properties.load(in);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 读取配置文件
	 * 
	 * @param properties
	 *            properties文件
	 * @param key
	 *            key值
	 * @return 取配置文件
	 */
	public static String getString(Properties properties, String key) {
		return properties.getProperty(key);
	}

	/**
	 * * 更新properties文件的键值对 如果该主键已经存在，更新该主键的值； 如果该主键不存在，则插入一对键值。
	 * 
	 * @param properties
	 *            properties文件
	 * @param filePath
	 *            路径
	 * @param keyname
	 *            key值
	 * @param keyvalue
	 *            value值
	 */
	public static void updateProperty(Properties properties, String filePath, String keyname, String keyvalue) {
		try {

			// 从输入流中读取属性列表（键和元素对）
			properties.setProperty(keyname, keyvalue);
			FileOutputStream outputFile = new FileOutputStream(filePath);
			properties.store(outputFile, null);
			outputFile.flush();
			outputFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
