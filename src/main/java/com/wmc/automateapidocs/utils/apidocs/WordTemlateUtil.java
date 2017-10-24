package com.wmc.automateapidocs.utils.apidocs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.wmc.automateapidocs.pojo.apidocs.WordContentDto;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * @author 王明昌
 * @date 2017年10月20日
 */
public class WordTemlateUtil {
	private static String wordPackageName = "/apiDocs/api-doc/";// 生成word保存的包名
	
	/**
	 * 输出word模版
	 * 
	 * @param savePath
	 * @param wordContentDtos
	 */
	public static void setWordTemplate(String savePath,  List<WordContentDto> wordContentDtos) {
		String dirPath = "/templates/word";
		URL classpath = Thread.currentThread().getContextClassLoader().getResource("");
		String path = classpath.getPath();
		savePath += wordPackageName;
		if (StringUtils.isNotBlank(savePath)) {
			ClassUtil.createFolder(savePath);
		}
		File file = new File(savePath + "/api.doc");
		// 创建一个freemarker.template.Configuration实例，它是存储 FreeMarker 应用级设置的核心部分
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
		try {
			File dir = new File(path + "/templates/word");
			// 设置模板目录
			//configuration.setDirectoryForTemplateLoading(dir);
			configuration.setClassForTemplateLoading(WordTemlateUtil.class, dirPath);
			configuration.setDefaultEncoding("UTF-8");
			// 从设置的目录中获得模板
			Template template = configuration.getTemplate("api.ftl");
			// 将数据与模板渲染的结果写入文件中
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("wordContentDtos", wordContentDtos);
			template.process(map, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
