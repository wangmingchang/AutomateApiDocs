package com.github.wangmingchang.automateapidocs.utils.apidocs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.github.wangmingchang.automateapidocs.config.FreemarkerConfig;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.ClassExplainDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.MethodInfoDto;

import freemarker.template.Template;

/**
 * HTML生成模板工具
 * 
 * @author 王明昌
 * @date 2017年10月18日
 */
public class HtmlTemlateUtil {
	private static String htmlpackageName = "/apiDocs/api-html/";// 生成html保存的包名

	/**
	 * 方法api的HTML模版输出
	 * 
	 * @param savePath
	 *            输出路径
	 * @param classExplainDto
	 *            类的头部相关信息
	 * @param methodDescriptions
	 *            方法业务名称
	 * @param methodInfoDtos
	 *            方法信息
	 */
	public static void setMethodApiTemplate(String savePath, ClassExplainDto classExplainDto,
			List<String> methodDescriptions, List<MethodInfoDto> methodInfoDtos) {
		String saveFileName = classExplainDto.getExplain() + ".html";
		String templateName = "methodApi.ftl";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("classExplainDto", classExplainDto); // 类的头部相关信息
		dataMap.put("methodDescriptions", methodDescriptions); // 方法业务名称
		dataMap.put("methodInfoDtos", methodInfoDtos); // 方法信息
		generateTemlate(savePath, saveFileName, templateName, dataMap);
	}

	/**
	 * indx的HTML模版输出
	 * 
	 * @param savePath
	 *            输出路径
	 * @param classExplains
	 *            类的头部信息
	 */
	public static void setIndexTemplate(String savePath, List<ClassExplainDto> classExplains) {
		String saveFileName = "index.html";
		String templateName = "index.ftl";
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("classExplains", classExplains); // 类的头部相关信息
		generateTemlate(savePath, saveFileName, templateName, dataMap);
	}

	/**
	 * 添加样式
	 * 
	 * @param savePath
	 *            保存路径
	 */
	public static void addCss(String savePath) {
		FileOutputStream fileOutputStream = null;
		InputStream inputStream = null;
		try {
			inputStream = HtmlTemlateUtil.class.getResource("/templates/apiDocs/css/style.css").openStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String fileSavePath = savePath + htmlpackageName + "style.css";
			fileOutputStream = new FileOutputStream(Paths.get(fileSavePath).toFile());
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				fileOutputStream.write(line.getBytes());
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 生成模板
	 * 
	 * @param savePath
	 *            保存路径
	 * @param saveFileName
	 *            保存的文件名
	 * @param templateName
	 *            模板的文件名
	 * @param dataMap
	 *            数据
	 */
	private static void generateTemlate(String savePath, String saveFileName, String templateName,
			Map<String, Object> dataMap) {
		try {
			String dirPath = "/templates/apiDocs/html";
			savePath += htmlpackageName;
			if (StringUtils.isNotBlank(savePath)) {
				PathUtil.createFolder(savePath);
			}
			File file = new File(savePath + saveFileName);
			Template template = FreemarkerConfig.getTemplateFactory(dirPath, templateName);
			// 将数据与模板渲染的结果写入文件中
			Writer writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			template.process(dataMap, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
