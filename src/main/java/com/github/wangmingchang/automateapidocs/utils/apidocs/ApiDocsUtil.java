package com.github.wangmingchang.automateapidocs.utils.apidocs;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.management.RuntimeErrorException;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.wangmingchang.automateapidocs.annotation.ApiDocsClass;
import com.github.wangmingchang.automateapidocs.annotation.ApiDocsMethod;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.ClassExplainDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.ClassFiedInfoDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.ClassMoreRemarkDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.HtmlMethonContentDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.MethodExplainDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.MethodInfoDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.RequestParamDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.ResponseClassDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.ResponseDataDto;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.WordContentDto;

/**
 * 自动生成API工具
 * 
 * @author 王明昌
 *
 */
public class ApiDocsUtil {

	private static boolean isWord; // 是否生成word格式api;
	private static boolean isHTML; // 是否生成HTML格式api;

	private static int sequence = 0; // 顺序号
	private static List<WordContentDto> wordContentDtos = new ArrayList<WordContentDto>();// word文档返回list
	private static List<ClassExplainDto> classExplains = new ArrayList<ClassExplainDto>(); // 类的业务说明
	private static List<HtmlMethonContentDto> htmlMethonContentDtos = new ArrayList<HtmlMethonContentDto>();// 方法的html页面返回list

	/**
	 * 执行自动生成api方法
	 */
	public static void init() {
		System.out.println("**************************执行自动生成api***************************");
		URL classpath = Thread.currentThread().getContextClassLoader().getResource("");
		String rootPate = classpath.getPath();
		// String rootPate = path + "/resources/";
		Properties properties = PropertiesUtil.loadProps(rootPate + "apiDocs.properties");
		String packageNameStr = properties.getProperty("packageName");
		String savePath = properties.getProperty("savePath");
		String isWordStr = properties.getProperty("isWord");
		String isHTMLStr = properties.getProperty("isHTML");

		isWord = isWordStr == "" || isWordStr == null ? true : Boolean.valueOf(isWordStr);
		isHTML = isHTMLStr == "" || isHTMLStr == null ? true : Boolean.valueOf(isHTMLStr);
		if (savePath == null || savePath == "") {
			savePath = rootPate + "apiDocs";
		}
		String[] packageNames = packageNameStr.split(",");
		for (String packageName : packageNames) {
			generateApi(packageName, savePath);
		}
		if (classExplains.size() > 0) {
			if (isHTML) {
				HtmlTemlateUtil.setIndexTemplate(savePath, classExplains);
				for (HtmlMethonContentDto htmlMethonContentDto : htmlMethonContentDtos) {
					HtmlTemlateUtil.setMethodApiTemplate(savePath, htmlMethonContentDto.getClassExplainDto(),
							htmlMethonContentDto.getMethodDescriptions(), htmlMethonContentDto.getMethodInfoDtos());
				}
				// 添加样式
				HtmlTemlateUtil.addCss(savePath);
			}
			if (isWord) {
				WordTemlateUtil.setWordTemplate(savePath, wordContentDtos);
			}
		}
		System.out.println("**************************生成完成***************************");
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
	public static void generateApi(String packageName, String savePath) {
		try {
			List<String> classNames = ClassUtil.getClassName(packageName);
			for (String classNameStr : classNames) {
				ClassExplainDto classExplainDto = new ClassExplainDto(); // 类的头部相关信息
				List<MethodExplainDto> methodExplainDtos = new ArrayList<MethodExplainDto>(); // 类中的方法多行注释的信息
				List<String> methodDescriptions = new ArrayList<String>(); // 方法业务说明
				List<MethodInfoDto> methodInfoDtos = new ArrayList<MethodInfoDto>(); // 方法信息

				Class<?> className = Class.forName(classNameStr);
				if (!className.isAnnotationPresent(ApiDocsClass.class)) {
					System.out.println(className + "没有在类上注解ApiDocsClass");
					// 不是生成api文档类
					continue;
				}

				ClassMoreRemarkDto classMoreRemark = ClassUtil.getClassMoreRemark(className);
				classExplainDto = classMoreRemark.getClassExplainDto(); // 类的头部相关信息
				methodExplainDtos = classMoreRemark.getMethodExplainDtos(); // 类中的方法多行注释的信息
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
						Class<?>[] responseBeans = apiDocs.responseBeans(); // 多个响应数据Bean
						String type = apiDocs.type(); // 请求方式
						String url = apiDocs.url(); // 请求方法路径
						String methodDescription = apiDocs.methodExplain(); // 方法说明
						url = StringUtils.isBlank(url) ? methodPath : path + url;
						List<ResponseDataDto> baseResponseDataDtos = new ArrayList<ResponseDataDto>(); // 响应字段信息(基础类)
						List<ResponseDataDto> responseDataDtos = new ArrayList<ResponseDataDto>(); // 响应字段信息
						if (ClassUtil.isRealClass(requestBean)) {
							List<ClassFiedInfoDto> requestFieldInfos = ClassUtil.getClassFieldAndMethod(requestBean,
									true, 1);
							if (requestFieldInfos != null && requestFieldInfos.size() > 0) {
								requestParamDtos.clear(); // 清空多行注释中的参数，以ApiDocs中的为主
								for (ClassFiedInfoDto classFiedInfoDto : requestFieldInfos) {
									RequestParamDto requestParamDto = new RequestParamDto();
									requestParamDto.setName(classFiedInfoDto.getName());
									requestParamDto.setType(classFiedInfoDto.getType());
									requestParamDto.setDescription(classFiedInfoDto.getDescription());
									if (classFiedInfoDto.getIfPass() != null) {
										requestParamDto.setRequired(classFiedInfoDto.getIfPass());
									}
									requestParamDtos.add(requestParamDto);
								}
							}
						}
						if (ClassUtil.isRealClass(responseBean)) {
							List<ClassFiedInfoDto> responseFieldInfos = ClassUtil.getClassFieldAndMethod(responseBean,
									true, 1);
							saveFiledInfo(responseBean, responseFieldInfos, responseDataDtos);
						}

						if (responseBeans != null && responseBeans.length > 0) {
							for (int j = 0; j < responseBeans.length; j++) {
								Class<?> responseBeanClass = responseBeans[j];
								if(ClassUtil.isRealClass(responseBeanClass)) {
									List<ClassFiedInfoDto> responseFieldInfos = ClassUtil
											.getClassFieldAndMethod(responseBeanClass, true, 1);
									saveFiledInfo(responseBeanClass, responseFieldInfos, responseDataDtos);
								}
							}
						}

						if (ClassUtil.isRealClass(baseResponseBean)) {
							// 有基础类返回
							List<ClassFiedInfoDto> responseFieldInfos = ClassUtil
									.getClassFieldAndMethod(baseResponseBean, true, 1);
							if (responseFieldInfos != null && responseFieldInfos.size() > 0) {
								for (ClassFiedInfoDto classFiedInfoDto : responseFieldInfos) {
									ResponseDataDto responseDataDto = new ResponseDataDto();
									responseDataDto.setName(classFiedInfoDto.getName());
									responseDataDto.setType(classFiedInfoDto.getType());
									responseDataDto.setDescription(classFiedInfoDto.getDescription());
									String[] types = { "list", "set", "object", "map" };
									List<String> typesList = Arrays.asList(types);
									if (typesList.contains(classFiedInfoDto.getType())) {
										// 存在
										responseDataDto.setResponseDataDtos(responseDataDtos);
									}
									baseResponseDataDtos.add(responseDataDto);
								}
							}
						}

						ArrayList<String> groupKeyList = new ArrayList<String>(); // 来确定分组key集合
						List<ResponseDataDto> list = null;
						List<ResponseClassDto> responseClassDtoList = new ArrayList<ResponseClassDto>();
						int responseClassDtoIndex = -1;
						for (ResponseDataDto responseDataDto : responseDataDtos) {
							String csName = responseDataDto.getClassName();
							int grade = responseDataDto.getGrade();
							String fieldType = responseDataDto.getType();
							String groupKey = csName; // 分组的key
							boolean containsKey = groupKeyList.contains(groupKey);
							if (containsKey) {
								list.add(responseDataDto);
							} else {
								responseClassDtoIndex++;
								list = new ArrayList<ResponseDataDto>();
								list.clear();
								list.add(responseDataDto);
								groupKeyList.add(groupKey);
							}
							String[] fieldTypeArr = { "list", "class" };
							if (ClassUtil.containsStr(fieldTypeArr, fieldType)) {
								// 如果字段是以上数组，级别就减1
								grade--;
							}
							// classNameMap.put(groupKey, list);
							ResponseClassDto responseClassDto = new ResponseClassDto();
							responseClassDto.setGrade(grade);
							responseClassDto.setClassName(csName);
							responseClassDto.setFieldType(fieldType);
							responseClassDto.setResponseDataDtos(list);
							if (responseClassDtoList.size() - 1 == responseClassDtoIndex) {
								responseClassDtoList.remove(responseClassDtoIndex);
							}
							responseClassDtoList.add(responseClassDtoIndex, responseClassDto);
						}
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
						System.out.println("*****************************************");
						System.out.println("类的说明 ：" + classExplainDto.getExplain());
						System.out.println("方法业务说明：" + methodDescription);
						System.out.println("方法请求路径：" + url);
						System.out.println("请求方法方式：" + type);
						System.out.println("请求字段信息：" + requestParamDtos);
						System.out.println("响应字段信息：" + responseClassDtos);
						System.out.println("响应字段basRespons信息：" + baseResponseDataDtos);

					}
				}
				if (isHTML) {
					HtmlMethonContentDto htmlMethonContentDto = new HtmlMethonContentDto(classExplainDto,
							methodDescriptions, methodInfoDtos);
					htmlMethonContentDtos.add(htmlMethonContentDto);
				}
				if (isWord) {
					WordContentDto wordContentDto = new WordContentDto();
					wordContentDto.setMethodInfoDtos(methodInfoDtos);
					wordContentDto.setClassExplainDto(classExplainDto);
					wordContentDtos.add(wordContentDto);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 保存类的字段相关信息
	 * 
	 * @param obj
	 *            类
	 * @param responseFieldInfos
	 *            类的字段信息
	 * @param responseDataDtos
	 *            返回的总字段信息的集合
	 * @throws Exception
	 * @throws Exception
	 */
	private static void saveFiledInfo(Class<?> obj, List<ClassFiedInfoDto> responseFieldInfos,
			List<ResponseDataDto> responseDataDtos) throws Exception, Exception {
		List<ClassFiedInfoDto> classFiedInfoDtoList = new CopyOnWriteArrayList<ClassFiedInfoDto>();
		classFiedInfoDtoList = responseFieldInfos;
		if (classFiedInfoDtoList != null && classFiedInfoDtoList.size() > 0) {
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
					continue;
				}
				responseDataDtos.add(responseDataDto);
			}

		}
	}

}
