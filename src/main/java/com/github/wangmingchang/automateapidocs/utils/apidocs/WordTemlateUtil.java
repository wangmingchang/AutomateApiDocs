package com.github.wangmingchang.automateapidocs.utils.apidocs;

import com.github.wangmingchang.automateapidocs.config.FreemarkerConfig;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.WordContentDto;
import freemarker.template.Template;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;

/**
 * word文档模板工具
 * 
 * @author 王明昌
 * @since 2017年10月20日
 */
public class WordTemlateUtil {
	private static String wordPackageName = "/apiDocs/api-doc/";// 生成word保存的包名

	/**
	 * 输出word模版
	 * @param savePath 保存路径
	 * @param wordContentDtos 输出数据
	 * @param isShowResponseClassName 是否开户在word文档的响应字段中显示类名
	 */
	public static void setWordTemplate(String savePath, List<WordContentDto> wordContentDtos, boolean isShowResponseClassName) {
		String dirPath = "/templates/word";
		savePath += wordPackageName;
		if (StringUtils.isNotBlank(savePath)) {
			PathUtil.createFolder(savePath);
		}
		File file = new File(savePath + "/api.doc");
		try {
			String templateName = "api.ftl";
			Template template = FreemarkerConfig.getTemplateFactory(dirPath, templateName );
			// 将数据与模板渲染的结果写入文件中
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("wordContentDtos", wordContentDtos);
			map.put("isShowResponseClassName", isShowResponseClassName);
			template.process(map, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
