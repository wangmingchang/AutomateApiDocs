package com.github.wangmingchang.automateapidocs.config;

import java.io.IOException;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;
import freemarker.template.Version;

/**
 * freemarker的配置
 * 
 * @author 王明昌
 * @since 2017年11月19日
 */
public class FreemarkerConfig {
    public static final Version VERSION_2_3_0 = new Version(2, 3, 0);
    
    /** FreeMarker version 2.3.19 (an {@link #Configuration(Version) incompatible improvements break-point}) */
    public static final Version VERSION_2_3_19 = new Version(2, 3, 19);
    
    /** FreeMarker version 2.3.20 (an {@link #Configuration(Version) incompatible improvements break-point}) */
    public static final Version VERSION_2_3_20 = new Version(2, 3, 20);
    
    /** FreeMarker version 2.3.21 (an {@link #Configuration(Version) incompatible improvements break-point}) */
    public static final Version VERSION_2_3_21 = new Version(2, 3, 21);

    /** FreeMarker version 2.3.22 (an {@link #Configuration(Version) incompatible improvements break-point}) */
    public static final Version VERSION_2_3_22 = new Version(2, 3, 22);

    /** FreeMarker version 2.3.23 (an {@link #Configuration(Version) incompatible improvements break-point}) */
    public static final Version VERSION_2_3_23 = new Version(2, 3, 23);

    /** FreeMarker version 2.3.24 (an {@link #Configuration(Version) incompatible improvements break-point}) */
    public static final Version VERSION_2_3_24 = new Version(2, 3, 24);

    /** FreeMarker version 2.3.25 (an {@link #Configuration(Version) incompatible improvements break-point}) */
    public static final Version VERSION_2_3_25 = new Version(2, 3, 25);

    /** FreeMarker version 2.3.26 (an {@link #Configuration(Version) incompatible improvements break-point}) */
    public static final Version VERSION_2_3_26 = new Version(2, 3, 26);
	/**
	 * 创建Template对象
	 * @param dirPath 模版相对路径
	 * @param templateName 模版名字
	 * @return Template对象
	 */
	public static Template getTemplateFactory(String dirPath, String templateName) {
		Configuration configuration = getConfiguration();
		configuration.setClassForTemplateLoading(FreemarkerConfig.class, dirPath);
		configuration.setDefaultEncoding("UTF-8");
		Template template  = null;
		try {
			template = configuration.getTemplate(templateName);
		} catch (TemplateNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTemplateNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return template;
	}
	/**
	 * 获取freemarker.template.Configuration实例
	 * @return freemarker.template.Configuration实例
	 */
	private static Configuration getConfiguration() {
		// 创建一个freemarker.template.Configuration实例，它是存储 FreeMarker 应用级设置的核心部分
		Configuration configuration = null;
		try {
			configuration = new Configuration(VERSION_2_3_26);
		} catch (Exception e) {
			try {
				configuration = new Configuration(VERSION_2_3_25);
			} catch (Exception e1) {
				try {
					configuration = new Configuration(VERSION_2_3_24);
				} catch (Exception e2) {
					try {
						configuration = new Configuration(VERSION_2_3_23);
					} catch (Exception e3) {
						try {
							configuration = new Configuration(VERSION_2_3_22);
						} catch (Exception e4) {
							try {
								configuration = new Configuration(VERSION_2_3_21);
							} catch (Exception e5) {
								try {
									configuration = new Configuration(VERSION_2_3_20);
								} catch (Exception e6) {
									try {
										configuration = new Configuration(VERSION_2_3_19);
									} catch (Exception e7) {
										try {
											configuration = new Configuration(VERSION_2_3_0);
										} catch (Exception e8) {
										}
									}
								}
							}
						}
					}
				}
			}
		}finally {
			if(configuration == null) {
				throw new RuntimeException("找不到freemarker相应的版本，请下载2.3.23以上版本");
			}
		}
		return configuration;
	}
	
}
