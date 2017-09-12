package com.wmc.AutomateApiDocs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.wmc.AutomateApiDocs.controller.HtmlController;
import com.wmc.AutomateApiDocs.pojo.apidocs.ClassExplainDto;
import com.wmc.AutomateApiDocs.utils.apidocs.ClassUtil;

public class PrintComments {
	/**
	 * 获取java文件的单行注释
	 * @param filePath 绝对路径
	 * @return
	 */
	private static List<String> getOneWayRemark(String filePath){
		List<String> remarks = new ArrayList<String>();
		try {
			FileReader freader = new FileReader(filePath);
			BufferedReader breader = new BufferedReader(freader);
			StringBuilder sb = new StringBuilder();
			try {
				String temp = "";
				/**
				 * 读取文件内容，并将读取的每一行后都不加\n 其目的是为了在解析双反斜杠（//）注释时做注释中止符
				 */
				while ((temp = breader.readLine()) != null) {
					sb.append(temp);
					sb.append('\n');
				}
				String src = sb.toString();
				int begin = 0;
	
				/**
				 * 2、对//注释进行匹配（渐进匹配法） 匹配方法是 // 总是与 \n 成对出现
				 */
				begin = 0;
				Pattern leftpattern1 = Pattern.compile("//");
				Matcher leftmatcher1 = leftpattern1.matcher(src);
				Pattern rightpattern1 = Pattern.compile("\n");
				Matcher rightmatcher1 = rightpattern1.matcher(src);
				sb = new StringBuilder();
				while (leftmatcher1.find(begin)) {
					rightmatcher1.find(leftmatcher1.start());
					String remarkStr = src.substring(leftmatcher1.start(), rightmatcher1.end());
					System.out.println(remarkStr);
					
					remarks.add(replaceBlank(remarkStr.substring(2)));
					
					
					sb.append(src.substring(leftmatcher1.start(), rightmatcher1.end()));
					begin = rightmatcher1.end();
				}
				System.out.println(sb.toString());
				System.out.println(remarks);
			} catch (IOException e) {
				System.out.println("文件读取失败");
			} finally {
				breader.close();
				freader.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在");
		} catch (IOException e) {
			System.out.println("文件读取失败");
		}
		return remarks;
	}
	/**
	 * 去除空格
	 * @param str
	 * @return
	 */
    public static String replaceBlank(String str) {  
        String dest = "";  
        if (str!=null) {  
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");  
            Matcher m = p.matcher(str);  
            dest = m.replaceAll("");  
        }  
        return dest;  
    } 
    /**
     * 获取Controoler类的多行注释的内容
     * @param filePath 绝对路径（H:\eclipse_4.7_worksapace\AutomateApiDocs\src\test\java\com\wmc\AutomateApiDocs\PrintComments.java）
     * @return
     */
	private static List<ClassExplainDto> getClassMoreRemark(String filePath) {
		List<ClassExplainDto> remarks = new ArrayList<ClassExplainDto>();
		try {
			FileReader freader = new FileReader(filePath);
			BufferedReader breader = new BufferedReader(freader);
			StringBuilder sb = new StringBuilder();
			try {
				String temp = "";
				/**
				 * 读取文件内容，并将读取的每一行后都不加\n 其目的是为了在解析双反斜杠（//）注释时做注释中止符
				 */
				while ((temp = breader.readLine()) != null) {
					sb.append(temp);
					sb.append('\n');
				}
				String src = sb.toString();
				/**
				 * 1、做/* 注释的正则匹配
				 *
				 * 
				 * 通过渐进法做注释的正则匹配，因为/*注释总是成对出现 当匹配到一个/*时总会在接下来的内容中会首先匹配到"*\\/",
				 * 因此在获取对应的"*\\/"注释时只需要从当前匹配的/*开始即可， 下一次匹配时只需要从上一次匹配的结尾开始即可 （这样对于大文本可以节省匹配效率）——
				 * 这就是渐进匹配法
				 *
				 * 
				 */
				Pattern leftpattern = Pattern.compile("/\\*");
				Matcher leftmatcher = leftpattern.matcher(src);
				Pattern rightpattern = Pattern.compile("\\*/");
				Matcher rightmatcher = rightpattern.matcher(src);
				sb = new StringBuilder();
				/**
				 * begin 变量用来做渐进匹配的游标 {@value} 初始值为文件开头
				 **/
				int begin = 0;
				while (leftmatcher.find(begin)) {
					rightmatcher.find(leftmatcher.start());
					String remarkStr = src.substring(leftmatcher.start(), rightmatcher.end());
					remarkStr = remarkStr.substring(2, remarkStr.length()-2);
					remarkStr = StringUtils.replace(remarkStr, "*", "");
					System.out.println(remarkStr);
					remarkStr = replaceBlank(remarkStr);
					System.out.println(replaceBlank(remarkStr));
					String[] split = StringUtils.split(remarkStr,"@");
					ClassExplainDto classExplainDto = new ClassExplainDto();
					for (String string : split) {
						if(string.contains("author")) {
							String author = StringUtils.substringAfter(string, "author");
							classExplainDto.setAuthor(author);
						}else if(string.contains("date")) {
							String createDate =  StringUtils.substringAfter(string, "date");
							classExplainDto.setCreateDate(createDate );
						}else {
							classExplainDto.setExplain(string);
						}
						
					}
					System.out.println(classExplainDto);
					
					remarks.add(classExplainDto);
					sb.append(src.substring(leftmatcher.start(), rightmatcher.end()));
					/** 为输出时格式的美观 **/
					sb.append('\n');
					begin = rightmatcher.end();
				}
				
			} catch (IOException e) {
				System.out.println("文件读取失败");
			} finally {
				breader.close();
				freader.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("文件不存在");
		} catch (IOException e) {
			System.out.println("文件读取失败");
		}
		return remarks;
	}
	
	
	public static void main(String[] args) {
		
		//System.out.println(ClassUtil.getOneWayRemark(Student.class));
		System.out.println(ClassUtil.getClassMoreRemark(HtmlController.class));
		//System.out.println(getClassMoreRemark("H:\\eclipse_4.7_worksapace\\AutomateApiDocs\\src\\main\\java\\com\\wmc\\AutomateApiDocs\\controller\\HtmlController.java"));
		//getOneWayRemark("H:\\\\eclipse_4.7_worksapace\\\\AutomateApiDocs\\\\src\\\\main\\\\java\\\\com\\\\wmc\\\\AutomateApiDocs\\\\pojo\\\\Student.java");
	}
}
