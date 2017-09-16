package com.wmc.AutomateApiDocs.utils.apidocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.wmc.AutomateApiDocs.annotation.ApiDocsClass;
import com.wmc.AutomateApiDocs.annotation.ApiDocsMethod;
import com.wmc.AutomateApiDocs.annotation.ApiDocsMethod.Null;
import com.wmc.AutomateApiDocs.pojo.apidocs.ClassExplainDto;
import com.wmc.AutomateApiDocs.pojo.apidocs.ClassFiedInfoDto;
import com.wmc.AutomateApiDocs.pojo.apidocs.ClassMoreRemarkDto;
import com.wmc.AutomateApiDocs.pojo.apidocs.MethodExplainDto;
import com.wmc.AutomateApiDocs.pojo.apidocs.MethodInfoDto;
import com.wmc.AutomateApiDocs.pojo.apidocs.RequestParamDto;
import com.wmc.AutomateApiDocs.pojo.apidocs.ResponseClassDto;
import com.wmc.AutomateApiDocs.pojo.apidocs.ResponseDataDto;

/**
 * 自动生成API工具
 * 
 * @author OP-T-PC-0036
 *
 */
public class ApiDocsUtil {
	
	/**
	 * 执行自动生成api方法
	 */
	public static void init() {
		String rootPate = System.getProperty("user.dir")+"/resources/";
		Properties properties = PropertiesUtil.loadProps(rootPate+"apiDocs.properties");
		String packageName = properties.getProperty("packageName");
		String savePath = properties.getProperty("savePath");
		if(savePath == null || savePath == "") {
			savePath = rootPate +"apiDocs";
		}
		generateApi(packageName, savePath);
	}
	
	/**
	 * 生成api
	 * 
	 * @param packageName
	 *            包名
	 * @param savePath
	 *            保存的路径
	 *            （绝对路径，如：‘F:\eclipse-jee-oxyen-workspace\AutomateApiDocs\resources\templates\apiDocs’）
	 */
	@SuppressWarnings("resource")
	public static void generateApi(String packageName, String savePath) {
		try {
			List<String> classNames = ClassUtil.getClassName(packageName);
			List<ClassExplainDto> classExplains = new ArrayList<ClassExplainDto>(); // 类的业务说明
			for (String classNameStr : classNames) {
				ClassExplainDto classExplainDto = new ClassExplainDto(); // 类的头部相关信息
				List<MethodExplainDto> methodExplainDtos = new ArrayList<MethodExplainDto>(); // 类中的方法多行注释的信息
				List<String> methodDescriptions = new ArrayList<String>(); // 方法业务说明
				List<MethodInfoDto> methodInfoDtos = new ArrayList<MethodInfoDto>(); // 方法信息

				Class<?> className = Class.forName(classNameStr);
				System.out.println(className);
				if (!className.isAnnotationPresent(ApiDocsClass.class)) {
					System.out.println(className + "没有在类上注解ApiDocsClass");
					// 不是生成api文档类
					continue;
				}

				ClassMoreRemarkDto classMoreRemark = ClassUtil.getClassMoreRemark(className);
				classExplainDto = classMoreRemark.getClassExplainDto(); // 类的头部相关信息
				methodExplainDtos = classMoreRemark.getMethodExplainDtos(); // 类中的方法多行注释的信息
				System.out.println("类的头部相关信息：" + classExplainDto);
				System.out.println("类中的方法多行注释的信息：" + methodExplainDtos);
				if (classExplainDto == null || methodExplainDtos.size() == 0) {
					continue;
				}

				classExplains.add(classExplainDto);
				StringBuilder path = new StringBuilder();// 请求类路径
				if (className.isAnnotationPresent(RequestMapping.class)) {
					// 获取类请求路径
					RequestMapping requestMapping = className.getAnnotation(RequestMapping.class);
					String[] value = requestMapping.value();
					for (String string : value) {
						path.append(string);
					}
				}
				System.out.println(methodExplainDtos + "*******" + methodExplainDtos.size() + "*****"
						+ className.getDeclaredMethods().length + "*****" + className.getMethods().length);
				if (methodExplainDtos.size() != className.getDeclaredMethods().length) {
					throw new RuntimeErrorException(null, className + ":类的方法和方法上的多行注释不一致");
				}

				for (int i = 0; i < className.getDeclaredMethods().length; i++) {
					Method method = className.getDeclaredMethods()[i];

					String methodPath = ""; // 方法请求路径
					if (method.isAnnotationPresent(ApiDocsMethod.class)) {
						MethodExplainDto methodExplainDto = methodExplainDtos.get(i);
						List<RequestParamDto> requestParamDtos = methodExplainDto.getParamDtos(); // 请求的参数
						List<ResponseClassDto> responseClassDtos = new ArrayList<ResponseClassDto>(); // 返回数据类信息
						
						RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
						String[] value = requestMapping.value(); // 获取方法上的路径
						for (String string : value) {
							methodPath = path + string;
						}

						ApiDocsMethod apiDocs = method.getAnnotation(ApiDocsMethod.class);
						Class<?> requestBean = apiDocs.requestBean(); // 请求参数Bean
						Class<?> baseResponseBean = apiDocs.baseResponseBean(); // 响应数据的基础返回Bean
						Class<?> responseBean = apiDocs.responseBean(); // 响应数据Bean
						String[] responseBeans = apiDocs.responseBeans(); //多个响应数据Bean
						String type = apiDocs.type(); // 请求方式
						String url = apiDocs.url(); // 请求方法路径
						String methodDescription = apiDocs.methodExplain(); // 方法说明
						url = StringUtils.isBlank(url) ? methodPath : path + url;
						List<ResponseDataDto> baseResponseDataDtos = new ArrayList<ResponseDataDto>(); // 响应字段信息(基础类)
						List<ResponseDataDto> responseDataDtos = new ArrayList<ResponseDataDto>(); // 响应字段信息
						if (requestBean != Null.class) {
							System.out.println("请求字段信息CLASS：" + requestBean);
							List<ClassFiedInfoDto> requestFieldInfos = ClassUtil.getClassFieldAndMethod(requestBean,
									true);
							if (requestFieldInfos != null && requestFieldInfos.size() > 0) {
								requestParamDtos.clear(); // 清空多行注释中的参数，以ApiDocs中的为主
								for (ClassFiedInfoDto classFiedInfoDto : requestFieldInfos) {
									RequestParamDto requestParamDto = new RequestParamDto();
									requestParamDto.setName(classFiedInfoDto.getName());
									requestParamDto.setType(classFiedInfoDto.getType());
									requestParamDto.setDescription(classFiedInfoDto.getDescription());
									if(classFiedInfoDto.getIfPass() != null) {
										requestParamDto.setRequired(classFiedInfoDto.getIfPass());
									}
									requestParamDtos.add(requestParamDto);
								}
							}
						}
						if (responseBean != Null.class) {
							System.out.println("响应字段信息CLASS：" + responseBean);
							List<ClassFiedInfoDto> responseFieldInfos = ClassUtil.getClassFieldAndMethod(responseBean,true);
							if (responseFieldInfos != null && responseFieldInfos.size() > 0) {
								Map<String, List<ResponseDataDto>> map = new HashMap<String, List<ResponseDataDto>>();
								for (ClassFiedInfoDto classFiedInfoDto : responseFieldInfos) {
									ResponseDataDto responseDataDto = new ResponseDataDto();
									responseDataDto.setClassName(responseBean.getSimpleName());
									responseDataDto.setName(classFiedInfoDto.getName());
									responseDataDto.setType(classFiedInfoDto.getType());
									responseDataDto.setDescription(classFiedInfoDto.getDescription());
									String childNode = classFiedInfoDto.getChildNode();
									if (childNode != null) {
										responseDataDto.setChildNode(childNode);
										boolean containsKey = map.containsKey(childNode);
										if (containsKey) {
											// 有子节点
											responseDataDto.setResponseDataDtos(map.get(childNode));
										}
										System.out.println("当前父节点数据为:" + responseDataDto);
									}
									String parentNode = classFiedInfoDto.getParentNode();
									if (parentNode != null) {
										boolean containsKey = map.containsKey(parentNode);
										List<ResponseDataDto> list = new ArrayList<ResponseDataDto>();
										if (containsKey) {
											list = map.get(parentNode);
											list.add(responseDataDto);
										} else {
											list.add(responseDataDto);
										}
										// 将父节点名的为key,子节点的对象为value
										map.put(parentNode, list);
										System.out.println("将子节点保存在父节点中:" + map.toString());
										continue;
									}
									responseDataDtos.add(responseDataDto);
								}
							}
						}
						
						if(responseBeans != null && responseBeans.length > 0) {
							for (int j = 0; j < responseBeans.length; j++) {
								Class<?> responseBeanClass = Class.forName(responseBeans[j]);
								List<ClassFiedInfoDto> responseFieldInfos = ClassUtil.getClassFieldAndMethod(responseBeanClass,true);
								if (responseFieldInfos != null && responseFieldInfos.size() > 0) {
									System.out.println("List的数据："+responseDataDtos);
									Map<String, List<ResponseDataDto>> map = new HashMap<String, List<ResponseDataDto>>();
									for (ClassFiedInfoDto classFiedInfoDto : responseFieldInfos) {
										ResponseDataDto responseDataDto = new ResponseDataDto();
										responseDataDto.setClassName(responseBeanClass.getSimpleName());
										responseDataDto.setName(classFiedInfoDto.getName());
										responseDataDto.setType(classFiedInfoDto.getType());
										responseDataDto.setDescription(classFiedInfoDto.getDescription());
										String childNode = classFiedInfoDto.getChildNode();
										if (childNode != null) {
											responseDataDto.setChildNode(childNode);
											boolean containsKey = map.containsKey(childNode);
											if (containsKey) {
												// 有子节点
												responseDataDto.setResponseDataDtos(map.get(childNode));
											}
											System.out.println("当前父节点数据为:" + responseDataDto);
										}
										String parentNode = classFiedInfoDto.getParentNode();
										if (parentNode != null) {
											boolean containsKey = map.containsKey(parentNode);
											List<ResponseDataDto> list = new ArrayList<ResponseDataDto>();
											if (containsKey) {
												list = map.get(parentNode);
												list.add(responseDataDto);
											} else {
												list.add(responseDataDto);
											}
											// 将父节点名的为key,子节点的对象为value
											map.put(parentNode, list);
											System.out.println("将子节点保存在父节点中:" + map.toString());
											continue;
										}
										responseDataDtos.add(responseDataDto);
									}
									
								}
								
							}
						}

						if (baseResponseBean != Null.class) {
							// 有基础类返回
							List<ClassFiedInfoDto> responseFieldInfos = ClassUtil
									.getClassFieldAndMethod(baseResponseBean, true);
							if (responseFieldInfos != null && responseFieldInfos.size() > 0) {
								for (ClassFiedInfoDto classFiedInfoDto : responseFieldInfos) {
									ResponseDataDto responseDataDto = new ResponseDataDto();
									responseDataDto.setName(classFiedInfoDto.getName());
									responseDataDto.setType(classFiedInfoDto.getType());
									responseDataDto.setDescription(classFiedInfoDto.getDescription());
									String[] types = { "list", "object", "map" };
									List<String> typesList = Arrays.asList(types);
									if (typesList.contains(classFiedInfoDto.getType())) {
										// 存在
										responseDataDto.setResponseDataDtos(responseDataDtos);
									}
									baseResponseDataDtos.add(responseDataDto);
								}
							}
							System.out.println("baseResponseBean:" + baseResponseBean);
						}
						Map<String,List<ResponseDataDto>> classNameMap = new HashMap<String,List<ResponseDataDto>>();
						for (ResponseDataDto responseDataDto : responseDataDtos) {
							String csName = responseDataDto.getClassName();
							boolean containsKey = classNameMap.containsKey(csName);
							List<ResponseDataDto> list = new ArrayList<ResponseDataDto>();
							if(containsKey) {
								list = classNameMap.get(csName);
								list.add(responseDataDto);
							}else {
								list.add(responseDataDto);
							}
							classNameMap.put(csName, list);
						}
						for (Map.Entry<String, List<ResponseDataDto>> entry : classNameMap.entrySet()) {  
							ResponseClassDto responseClassDto = new ResponseClassDto();
							responseClassDto.setClassName(entry.getKey());
							responseClassDto.setResponseDataDtos(entry.getValue());
							responseClassDtos.add(responseClassDto);
							
						    System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());  
						  
						}   

						methodDescriptions.add(methodDescription);
						MethodInfoDto methodInfoDto = new MethodInfoDto();
						methodInfoDto.setMethodDescription(methodDescription);
						methodInfoDto.setType(type);
						methodInfoDto.setUrl(url);
						methodInfoDto.setRequestParamDtos(requestParamDtos);
						methodInfoDto.setResponseClassDtos(responseClassDtos);
						methodInfoDto.setBaseResponseDataDtos(baseResponseDataDtos);
						methodInfoDtos.add(methodInfoDto);
						System.out.println("方法业务说明：" + methodDescription);
						System.out.println("方法请求路径：" + url);
						System.out.println("请求方法方式：" + type);
						System.out.println("请求字段信息：" + requestParamDtos);
						System.out.println("响应字段信息：" + responseClassDtos);
						System.out.println("响应字段basRespons信息：" + baseResponseDataDtos);

					}
				}
				getMethodApiTemplate(savePath, classExplainDto, methodDescriptions, methodInfoDtos);
			}
			if (classExplains.size() > 0) {
				getIndexTemplate(savePath, classExplains);
				//添加样式
				addCss(savePath);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 添加Css样式
	 * @param savePath
	 */
	private static void addCss(String savePath) {
		try {
			String rootPath = System.getProperty("user.dir");
			File file = new File(rootPath+"/resources/templates/apiDocs/css/style.css");
			if(file.exists()) {
				String fileSavePath = savePath + "/" + file.getName(); //保存路径
				FileInputStream fileInputStream = new FileInputStream(file);
				FileOutputStream fileOutputStream = new FileOutputStream(Paths.get(fileSavePath).toFile());
				byte[] bytearray = new byte[1024];
				while (fileInputStream.read() > 0) {
					fileInputStream.read(bytearray, 0, 1024);
					fileOutputStream.write(bytearray);
					
				}
				fileInputStream.close();
				fileOutputStream.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	/**
	 * 方法api模版输出
	 * @param savePath 输出路径 
	 * @param classExplainDto 类的头部相关信息
	 * @param methodDescriptions 方法业务名称
	 * @param methodInfoDtos 方法信息
	 */
	private static void getMethodApiTemplate(String savePath, ClassExplainDto classExplainDto, List<String> methodDescriptions,List<MethodInfoDto> methodInfoDtos ) {
		try {
			// 构造模板引擎
			ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
			resolver.setPrefix("templates/apiDocs/");// 模板所在目录，相对于当前classloader的classpath。
			resolver.setSuffix(".html");// 模板文件后缀
			resolver.setTemplateMode("HTML");
			resolver.setCharacterEncoding(CharEncoding.UTF_8);
			TemplateEngine templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(resolver);
			
			// 构造上下文(Model)
			Context context = new Context();
			context.setVariable("classExplainDto", classExplainDto); // 类的头部相关信息
			context.setVariable("methodDescriptions", methodDescriptions); // 方法业务名称
			context.setVariable("methodInfoDtos", methodInfoDtos); // 方法信息
			String fileName = classExplainDto.getExplain() + ".html";
			if (StringUtils.isNotBlank(savePath)) {
				ClassUtil.createFolder(savePath);
			}
			String filePath = savePath + "\\" + fileName;
			// 渲染模板
			FileWriter write = new FileWriter(Paths.get(filePath).toFile());
			templateEngine.process("methodApi", context, write);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * indx模版输出
	 * @param savePath 输出路径
	 * @param classExplains 类的头部信息
	 * @throws IOException
	 */
	private static void getIndexTemplate(String savePath,List<ClassExplainDto> classExplains){
		try {
		// 构造模板引擎
			ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
			resolver.setPrefix("templates/apiDocs/");// 模板所在目录，相对于当前classloader的classpath。
			resolver.setSuffix(".html");// 模板文件后缀
			resolver.setTemplateMode("HTML");
			resolver.setCharacterEncoding(CharEncoding.UTF_8);
			TemplateEngine templateEngine = new TemplateEngine();
			templateEngine.setTemplateResolver(resolver);
	
			// 构造上下文(Model)
			Context context = new Context();
			context.setVariable("classExplains", classExplains); // 类的头部相关信息
			String fileName = "index.html";
			if (StringUtils.isNotBlank(savePath)) {
				ClassUtil.createFolder(savePath);
			}
			String filePath = savePath + "\\" + fileName;
			// 渲染模板
			FileWriter write;
				write = new FileWriter(Paths.get(filePath).toFile());
			templateEngine.process("index", context, write);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
