package com.wmc.AutomateApiDocs.utils.apidocs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

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
import com.wmc.AutomateApiDocs.pojo.apidocs.WordContentDto;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 自动生成API工具
 * 
 * @author OP-T-PC-0036
 *
 */
public class ApiDocsUtil {
	
	private static boolean isWord; //是否生成word格式api;
	private static boolean isHTML; //是否生成HTML格式api;
	private static String htmlpackageName = "/api-html/";//生成html保存的包名
	private static String wordPackageName = "/api-doc/";//生成word保存的包名
	
	
	/**
	 * 执行自动生成api方法
	 */
	public static void init() {
		URL classpath = Thread.currentThread().getContextClassLoader().getResource("");    
        String rootPate = classpath.getPath(); 
        System.out.println("**********路径**********"+rootPate);
		//String rootPate = path + "/resources/";
		Properties properties = PropertiesUtil.loadProps(rootPate+"apiDocs.properties");
		String packageName = properties.getProperty("packageName");
		String savePath = properties.getProperty("savePath");
		String isWordStr = properties.getProperty("isWord");
		String isHTMLStr = properties.getProperty("isHTML");
		
		isWord = isWordStr == "" || isWordStr == null ? true : Boolean.valueOf(isWordStr);
		isHTML = isHTMLStr == "" || isHTMLStr == null ? true : Boolean.valueOf(isHTMLStr);
		if(savePath == null || savePath == "") {
			savePath = rootPate +"apiDocs";
		}
		
		generateApi(packageName, savePath);
	}
	
	private static int sequence = 0; //顺序号
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
			List<WordContentDto> wordContentDtos = new ArrayList<WordContentDto>(); //word文档返回list
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
					sequence = 0;
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
						Class<?>[] responseBeans = apiDocs.responseBeans(); //多个响应数据Bean
						String type = apiDocs.type(); // 请求方式
						String url = apiDocs.url(); // 请求方法路径
						String methodDescription = apiDocs.methodExplain(); // 方法说明
						url = StringUtils.isBlank(url) ? methodPath : path + url;
						List<ResponseDataDto> baseResponseDataDtos = new ArrayList<ResponseDataDto>(); // 响应字段信息(基础类)
						List<ResponseDataDto> responseDataDtos = new ArrayList<ResponseDataDto>(); // 响应字段信息
						if (requestBean != Null.class) {
							System.out.println("请求字段信息CLASS：" + requestBean);
							List<ClassFiedInfoDto> requestFieldInfos = ClassUtil.getClassFieldAndMethod(requestBean,true,1);
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
							List<ClassFiedInfoDto> responseFieldInfos = ClassUtil.getClassFieldAndMethod(responseBean,true,1);
							saveFiledInfo(responseBean, responseFieldInfos, responseDataDtos);
						}
						
						if(responseBeans != null && responseBeans.length > 0) {
							for (int j = 0; j < responseBeans.length; j++) {
								Class<?> responseBeanClass = responseBeans[j];
								List<ClassFiedInfoDto> responseFieldInfos = ClassUtil.getClassFieldAndMethod(responseBeanClass,true,1);
								saveFiledInfo(responseBeanClass, responseFieldInfos, responseDataDtos);
							}
						}
						
						if (baseResponseBean != Null.class) {
							// 有基础类返回
							List<ClassFiedInfoDto> responseFieldInfos = ClassUtil
									.getClassFieldAndMethod(baseResponseBean, true,1);
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

						ArrayList<String> groupKeyList = new ArrayList<String>(); //来确定分组key集合
						List<ResponseDataDto> list = null;
						List<ResponseClassDto> responseClassDtoList = new ArrayList<ResponseClassDto>();
						int responseClassDtoIndex = -1;
						for (ResponseDataDto responseDataDto : responseDataDtos) {
							String csName = responseDataDto.getClassName();
							int grade = responseDataDto.getGrade();
							String fieldType = responseDataDto.getType();
							String groupKey  = csName; //分组的key
							boolean containsKey = groupKeyList.contains(groupKey);
							if(containsKey) {
								list.add(responseDataDto);
							}else {
								responseClassDtoIndex ++;
								System.out.println("responseClassDtoIndex:"+responseClassDtoIndex);
								list = new ArrayList<ResponseDataDto>();
								list.clear();
								list.add(responseDataDto);
								groupKeyList.add(groupKey);
							}
							String[] fieldTypeArr = {"list","class"};
							if(ClassUtil.containsStr(fieldTypeArr,fieldType)) {
								//如果字段是以上数组，级别就减1
								grade--;
							}
							//classNameMap.put(groupKey, list);
							ResponseClassDto responseClassDto = new ResponseClassDto();
							responseClassDto.setGrade(grade);
							responseClassDto.setClassName(csName);
							responseClassDto.setFieldType(fieldType);
							responseClassDto.setResponseDataDtos(list);
							if(responseClassDtoList.size()-1 == responseClassDtoIndex) {
								responseClassDtoList.remove(responseClassDtoIndex);
							}
							responseClassDtoList.add(responseClassDtoIndex, responseClassDto);
						}
						System.out.println("groupKeyList:"+ groupKeyList);
						for (ResponseClassDto responseClassDto : responseClassDtoList) {
							responseClassDtos.add(responseClassDto);
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
						System.out.println("类的说明 ：" + classExplainDto.getExplain());
						System.out.println("方法业务说明：" + methodDescription);
						System.out.println("方法请求路径：" + url);
						System.out.println("请求方法方式：" + type);
						System.out.println("请求字段信息：" + requestParamDtos);
						System.out.println("响应字段信息：" + responseClassDtos);
						System.out.println("响应字段basRespons信息：" + baseResponseDataDtos);

					}
				}
				if(isHTML) {
					setMethodApiTemplate(savePath, classExplainDto, methodDescriptions, methodInfoDtos);
				}
				if(isWord) {
					WordContentDto wordContentDto = new WordContentDto();
					wordContentDto.setMethodInfoDtos(methodInfoDtos);
					wordContentDto.setClassExplainDto(classExplainDto);
					wordContentDtos.add(wordContentDto);
				}
			}
			if (classExplains.size() > 0) {
				if(isHTML) {
					setIndexTemplate(savePath, classExplains);
					//添加样式
					addCss(savePath);
				}
				if(isWord) {
					setWordTemplate(savePath,wordContentDtos);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 保存类的字段相关信息
	 * @param obj 类
	 * @param responseFieldInfos 类的字段信息
	 * @param responseDataDtos 返回的总字段信息的集合
	 * @throws Exception
	 * @throws Exception
	 */
	private static void saveFiledInfo(Class<?> obj,List<ClassFiedInfoDto> responseFieldInfos,List<ResponseDataDto> responseDataDtos) throws Exception, Exception {
		List<ClassFiedInfoDto> classFiedInfoDtoList = new CopyOnWriteArrayList<ClassFiedInfoDto>();
		classFiedInfoDtoList = responseFieldInfos;
		if (classFiedInfoDtoList != null && classFiedInfoDtoList.size() > 0) {
			System.out.println("List的数据："+classFiedInfoDtoList);
			Map<String, List<ResponseDataDto>> map = new HashMap<String, List<ResponseDataDto>>();
			for (ClassFiedInfoDto classFiedInfoDto : classFiedInfoDtoList) {
				ResponseDataDto responseDataDto = new ResponseDataDto();
				responseDataDto.setGrade(classFiedInfoDto.getGrade());
				responseDataDto.setClassName(obj.getSimpleName());
				responseDataDto.setName(classFiedInfoDto.getName());
				
				sequence++;
				responseDataDto.setSequence(sequence);
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
	
	/**
	 * 添加Css样式
	 * @param savePath
	 */
	private static void addCss(String savePath) {
		try {
			URL classpath = Thread.currentThread().getContextClassLoader().getResource("");    
	        String rootPath = classpath.getPath(); 
	        System.out.println("**********路径**********"+rootPath);
			File file = new File(rootPath+"/templates/apiDocs/css/style.css");
			if(file.exists()) {
				String fileSavePath = savePath + htmlpackageName + file.getName(); //保存路径
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
	 * 方法api的HTML模版输出
	 * @param savePath 输出路径 
	 * @param classExplainDto 类的头部相关信息
	 * @param methodDescriptions 方法业务名称
	 * @param methodInfoDtos 方法信息
	 */
	private static void setMethodApiTemplate(String savePath, ClassExplainDto classExplainDto, List<String> methodDescriptions,List<MethodInfoDto> methodInfoDtos ) {
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
			savePath += htmlpackageName;
			if (StringUtils.isNotBlank(savePath)) {
				ClassUtil.createFolder(savePath);
			}
			String filePath = savePath + fileName;
			// 渲染模板
			FileWriter write = new FileWriter(Paths.get(filePath).toFile());
			templateEngine.process("methodApi", context, write);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * indx的HTML模版输出
	 * @param savePath 输出路径
	 * @param classExplains 类的头部信息
	 * @throws IOException
	 */
	private static void setIndexTemplate(String savePath,List<ClassExplainDto> classExplains){
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
			savePath += htmlpackageName;
			if (StringUtils.isNotBlank(savePath)) {
				ClassUtil.createFolder(savePath);
			}
			String filePath = savePath + fileName;
			// 渲染模板
			FileWriter write;
				write = new FileWriter(Paths.get(filePath).toFile());
			templateEngine.process("index", context, write);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 输出word模版
	 * @param savePath
	 * @param wordContentDtos
	 */
	private static void setWordTemplate(String savePath, List<WordContentDto> wordContentDtos) {
		URL classpath = Thread.currentThread().getContextClassLoader().getResource("");    
        String path = classpath.getPath(); 
        System.out.println("**********路径**********"+path);
        System.out.println("数据："+wordContentDtos);
        savePath += wordPackageName;
        if (StringUtils.isNotBlank(savePath)) {
			ClassUtil.createFolder(savePath);
		}
		File file = new File(savePath + "/api.doc");
		//创建一个freemarker.template.Configuration实例，它是存储 FreeMarker 应用级设置的核心部分
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_25);
		try {
			File dir = new File(path + "/templates/word");
			 //设置模板目录
			configuration.setDirectoryForTemplateLoading(dir);
			configuration.setDefaultEncoding("UTF-8");
			//从设置的目录中获得模板
			Template template = configuration.getTemplate("api.ftl");
			//将数据与模板渲染的结果写入文件中
			Writer writer = new OutputStreamWriter(new FileOutputStream(file),"UTF-8");
			HashMap<String, Object> map = new HashMap<String,Object>();
			map.put("wordContentDtos", wordContentDtos);
			template.process(map, writer);
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
