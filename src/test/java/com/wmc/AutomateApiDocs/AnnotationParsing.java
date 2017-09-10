package com.wmc.AutomateApiDocs;

import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.wmc.AutomateApiDocs.annotation.ApiDocs;
import com.wmc.AutomateApiDocs.annotation.ApiDocs.Null;
import com.wmc.AutomateApiDocs.pojo.dto.ClassExplainDto;
import com.wmc.AutomateApiDocs.pojo.dto.ClassFiedInfoDto;
import com.wmc.AutomateApiDocs.pojo.dto.ClassMoreRemarkDto;
import com.wmc.AutomateApiDocs.pojo.dto.MethodExplainDto;
import com.wmc.AutomateApiDocs.pojo.dto.RequestParamDto;
import com.wmc.AutomateApiDocs.pojo.dto.ResponseDataDto;
import com.wmc.AutomateApiDocs.utils.ClassUtil;

public class AnnotationParsing {

	private static final String ResponseDataDto = null;

	public static void getFruitInfo(String className) {
		try {
			Class<?> forName = Class.forName(className);
			forName.getDeclaredFields();

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void getClassFieldAndMethod(Class cur_class) {
		String class_name = cur_class.getName();
		Field[] obj_fields = cur_class.getDeclaredFields();
		for (Field field : obj_fields) {
			field.setAccessible(true);
			System.out.println(class_name + ":" + field.getName());
		}
		Method[] methods = cur_class.getDeclaredMethods();
		for (Method method : methods) {
			method.setAccessible(true);
			System.out.println(class_name + ":" + method.getName());
		}
		if (cur_class.getSuperclass() != null) {
			getClassFieldAndMethod(cur_class.getSuperclass());
		}
	}

	public static void main(String[] args) {

		try {
			List<String> classNames = ClassUtil.getClassName("com.wmc.AutomateApiDocs.controller");
			String savePath = "H:\\eclipse_4.7_worksapace\\AutomateApiDocs\\resources\\static\\apiDocs";
			for (String classNameStr : classNames) {
				Class<?> className = Class.forName(classNameStr);
				System.out.println(className);
				ClassMoreRemarkDto classMoreRemark = ClassUtil.getClassMoreRemark(className);
				ClassExplainDto classExplainDto = classMoreRemark.getClassExplainDto(); // 类的头部相关信息
				List<MethodExplainDto> methodExplainDtos = classMoreRemark.getMethodExplainDtos(); // 类中的方法多行注释的信息
				System.out.println("类的头部相关信息：" + classExplainDto);
				System.out.println("类中的方法多行注释的信息："+ methodExplainDtos);

				
				StringBuilder path = new StringBuilder();// 请求类路径
				if (className.isAnnotationPresent(RequestMapping.class)) {
					// 获取类请求路径
					RequestMapping requestMapping = className.getAnnotation(RequestMapping.class);
					String[] value = requestMapping.value();
					for (String string : value) {
						path.append(string);
					}
				}
				System.out.println(methodExplainDtos.size() +"*****"+className.getDeclaredMethods().length+"*****"+ className.getMethods().length);
				if(methodExplainDtos.size() != className.getDeclaredMethods().length) {
					throw new RuntimeErrorException(null, className + ":类的方法和方法上的多行注释不一致");
				}
				
				for (int i = 0; i < className.getDeclaredMethods().length; i++) {
					Method method = className.getDeclaredMethods()[i];
					System.out.println(method.getName());
					MethodExplainDto methodExplainDto = methodExplainDtos.get(i);
					String methodDescription = methodExplainDto.getExplain(); //方法说明
					List<RequestParamDto> requestParamDtos = methodExplainDto.getParamDtos(); //请求的参数
					
					String methodPath = ""; //方法请求路径
					if (method.isAnnotationPresent(RequestMapping.class)) {
						RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
						String[] value = requestMapping.value();
						for (String string : value) {
							methodPath = path + string;
						}
					}
					
					if (method.isAnnotationPresent(ApiDocs.class)) {
						ApiDocs apiDocs = method.getAnnotation(ApiDocs.class);
						Class<?> requestBean = apiDocs.requestBean(); // 请求参数Bean
						Class<?> responseBean = apiDocs.responseBean(); // 响应数据Bean
						String type = apiDocs.type(); // 请求方式
						String url = apiDocs.url(); //请求方法路径
						url = StringUtils.isBlank(url) ? methodPath : path + url;
						List<ResponseDataDto> responseDataDtos = new ArrayList<ResponseDataDto>(); //响应字段信息
						if(requestBean != Null.class) {
							System.out.println("请求字段信息CLASS："+requestBean);
							List<ClassFiedInfoDto>  requestFieldInfos = ClassUtil.getClassFieldAndMethod(requestBean,true);
							if(requestFieldInfos != null && requestFieldInfos.size() > 0) {
								requestParamDtos.clear(); //清空多行注释中的参数，以ApiDocs中的为主
								for (ClassFiedInfoDto classFiedInfoDto : requestFieldInfos) {
									RequestParamDto RequestParamDto = new RequestParamDto();
									RequestParamDto.setName(classFiedInfoDto.getName());
									RequestParamDto.setType(classFiedInfoDto.getType());
									RequestParamDto.setDescription(classFiedInfoDto.getDescription());
									requestParamDtos.add(RequestParamDto);
								}
							}
						}
						if(responseBean != Null.class) {
							System.out.println("响应字段信息CLASS："+responseBean);
							List<ClassFiedInfoDto>  responseFieldInfos = ClassUtil.getClassFieldAndMethod(responseBean,true);
							if(responseFieldInfos != null && responseFieldInfos.size() > 0) {
								for (ClassFiedInfoDto classFiedInfoDto : responseFieldInfos) {
									ResponseDataDto responseDataDto = new ResponseDataDto();
									responseDataDto.setName(classFiedInfoDto.getName());
									responseDataDto.setType(classFiedInfoDto.getType());
									responseDataDto.setDescription(classFiedInfoDto.getDescription());
									if(classFiedInfoDto.getChildNode()  != null) {
										responseDataDto.setChildNode(classFiedInfoDto.getChildNode());
									}
									if(classFiedInfoDto.getParentNode() != null) {
										responseDataDto.setParentNode(classFiedInfoDto.getParentNode());
									}
									responseDataDtos.add(responseDataDto);
								}
							}
							
						} 
						
						System.out.println("方法业务说明：" + methodDescription);
						System.out.println("方法请求路径：" + url);
						System.out.println("请求方法方式：" + type);
						System.out.println("请求字段信息：" + requestParamDtos);
						System.out.println("响应字段信息：" + responseDataDtos);
						
						//构造模板引擎
						ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
				        resolver.setPrefix("templates/apiDocs/");//模板所在目录，相对于当前classloader的classpath。
				        resolver.setSuffix(".html");//模板文件后缀
				        resolver.setTemplateMode("HTML5");
				        resolver.setCharacterEncoding(CharEncoding.UTF_8);
				        TemplateEngine templateEngine = new TemplateEngine();
 				        templateEngine.setTemplateResolver(resolver);

				        //构造上下文(Model)
				        Context context = new Context();
				        //context.setVariable("name", "蔬菜列表");
				        //context.setVariable("array", new String[]{"土豆", "番茄", "白菜", "芹菜"});
				        context.setVariable("methodDescription", methodDescription); //方法业务名称
				        context.setVariable("className", classExplainDto.getExplain()); //类的说明
				        String fileName = classExplainDto.getExplain()+".html";
				        if(StringUtils.isNotBlank(savePath)) {
				        	ClassUtil.createFolder(savePath);
				        }
				        String filePath = savePath+"\\"+fileName;
				        //渲染模板
				        FileWriter write = new FileWriter(Paths.get(filePath).toFile());
				        templateEngine.process("methodApi", context, write);

				
					}
				}

			}
			// System.out.println(com.wmc.AutomateApiDocs.controller.HtmlController.class.getAnnotations());
			/*
			 * List<String> classNames2 =
			 * ClassUtil.getClassName("com.wmc.AutomateApiDocs.pojo"); for (String
			 * classNameStr : classNames2) { Class<?> className =
			 * Class.forName(classNameStr); List<String> oneWayRemark =
			 * ClassUtil.getOneWayRemark(ClassUtil.getClassPath(className)); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
