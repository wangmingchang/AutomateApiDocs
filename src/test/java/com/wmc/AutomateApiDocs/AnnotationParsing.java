package com.wmc.AutomateApiDocs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.wmc.AutomateApiDocs.utils.apidocs.ApiDocsUtil;

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
		ApiDocsUtil.generateApi("com.wmc.AutomateApiDocs.controller", "H:\\eclipse_4.7_worksapace\\AutomateApiDocs\\resources\\static\\apiDocs");
		/*try {
			List<String> classNames = ClassUtil.getClassName("com.wmc.AutomateApiDocs.controller");
			String savePath = "F:\\eclipse-jee-oxyen-workspace\\AutomateApiDocs\\resources\\static\\apiDocs";
			
			List<ClassExplainDto> classExplains = new ArrayList<ClassExplainDto>(); //类的业务说明
			for (String classNameStr : classNames) {
				ClassExplainDto classExplainDto = new ClassExplainDto(); //类的头部相关信息
				List<MethodExplainDto> methodExplainDtos = new ArrayList<MethodExplainDto>(); //类中的方法多行注释的信息
				List<String> methodDescriptions = new ArrayList<String>(); //方法业务说明
				List<MethodInfoDto> methodInfoDtos = new ArrayList<MethodInfoDto>(); //方法信息
				
				
				Class<?> className = Class.forName(classNameStr);
				System.out.println(className);
				if(!className.isAnnotationPresent(ApiDocsClass.class)) {
					System.out.println(className+"没有在类上注解ApiDocsClass");
					//不是生成api文档类
					continue;
				}
				
				ClassMoreRemarkDto classMoreRemark = ClassUtil.getClassMoreRemark(className);
				classExplainDto = classMoreRemark.getClassExplainDto(); // 类的头部相关信息
				methodExplainDtos = classMoreRemark.getMethodExplainDtos(); // 类中的方法多行注释的信息
				System.out.println("类的头部相关信息：" + classExplainDto);
				System.out.println("类中的方法多行注释的信息："+ methodExplainDtos);
				if(classExplainDto == null || methodExplainDtos.size() == 0) {
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
				System.out.println(methodExplainDtos +"*******"+methodExplainDtos.size() +"*****"+className.getDeclaredMethods().length+"*****"+ className.getMethods().length);
				if(methodExplainDtos.size() != className.getDeclaredMethods().length) {
					throw new RuntimeErrorException(null, className + ":类的方法和方法上的多行注释不一致");
				}
				
				for (int i = 0; i < className.getDeclaredMethods().length; i++) {
					Method method = className.getDeclaredMethods()[i];
					
					String methodPath = ""; //方法请求路径
					if (method.isAnnotationPresent(ApiDocs.class)) {
						MethodExplainDto methodExplainDto = methodExplainDtos.get(i);
						List<RequestParamDto> requestParamDtos = methodExplainDto.getParamDtos(); //请求的参数
						
						RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
						String[] value = requestMapping.value(); //获取方法上的路径 
						for (String string : value) {
							methodPath = path + string;
						}
						
						ApiDocs apiDocs = method.getAnnotation(ApiDocs.class);
						Class<?> requestBean = apiDocs.requestBean(); // 请求参数Bean
						Class<?> baseResponseBean = apiDocs.baseResponseBean(); // 响应数据的基础返回Bean
						Class<?> responseBean = apiDocs.responseBean(); // 响应数据Bean
						String type = apiDocs.type(); // 请求方式
						String url = apiDocs.url(); //请求方法路径
						String methodDescription = apiDocs.methodExplain(); //方法说明
						url = StringUtils.isBlank(url) ? methodPath : path + url;
						List<ResponseDataDto> baseResponseDataDtos = new ArrayList<ResponseDataDto>(); //响应字段信息(基础类)
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
								Map<String,List<ResponseDataDto>> map = new HashMap<String,List<ResponseDataDto>>();
								for (ClassFiedInfoDto classFiedInfoDto : responseFieldInfos) {
									ResponseDataDto responseDataDto = new ResponseDataDto();
									responseDataDto.setName(classFiedInfoDto.getName());
									responseDataDto.setType(classFiedInfoDto.getType());
									responseDataDto.setDescription(classFiedInfoDto.getDescription());
									String childNode = classFiedInfoDto.getChildNode();
									if(childNode != null) {
										responseDataDto.setChildNode(childNode);
										boolean containsKey = map.containsKey(childNode);
										if(containsKey) {
											//有子节点
											responseDataDto.setResponseDataDtos(map.get(childNode));
										}
										System.out.println("当前父节点数据为:"+responseDataDto);
									}
									String parentNode = classFiedInfoDto.getParentNode();
									if(parentNode != null) {
										boolean containsKey = map.containsKey(parentNode);
										List<ResponseDataDto> list = new ArrayList<ResponseDataDto>();
										if(containsKey) {
											list = map.get(parentNode);
											list.add(responseDataDto);
										}else {
											list.add(responseDataDto);
										}
										//将父节点名的为key,子节点的对象为value
										map.put(parentNode, list);
										System.out.println("将子节点保存在父节点中:"+map.toString());
										continue;
									}
									responseDataDtos.add(responseDataDto);
								}
							}
						} 
						
						if(baseResponseBean != Null.class) {
							//有基础类返回
							List<ClassFiedInfoDto>  responseFieldInfos = ClassUtil.getClassFieldAndMethod(baseResponseBean,true);
							if(responseFieldInfos != null && responseFieldInfos.size() > 0) {
								for (ClassFiedInfoDto classFiedInfoDto : responseFieldInfos) {
									ResponseDataDto responseDataDto = new ResponseDataDto();
									responseDataDto.setName(classFiedInfoDto.getName());
									responseDataDto.setType(classFiedInfoDto.getType());
									responseDataDto.setDescription(classFiedInfoDto.getDescription());
									String[] types = {"list","object","map"};
									List<String> typesList = Arrays.asList(types);
									if(typesList.contains(classFiedInfoDto.getType())) {
										//存在
										responseDataDto.setResponseDataDtos(responseDataDtos);
									}
									baseResponseDataDtos.add(responseDataDto);
								}
							}
							System.out.println("baseResponseBean:"+baseResponseBean);
						}
						
						methodDescriptions.add(methodDescription);
						MethodInfoDto methodInfoDto = new MethodInfoDto();
						methodInfoDto.setMethodDescription(methodDescription);
						methodInfoDto.setType(type);
						methodInfoDto.setUrl(url);
						methodInfoDto.setRequestParamDtos(requestParamDtos);
						methodInfoDto.setResponseDataDtos(responseDataDtos);
						methodInfoDto.setBaseResponseDataDtos(baseResponseDataDtos);
						methodInfoDtos.add(methodInfoDto );
						System.out.println("方法业务说明：" + methodDescription);
						System.out.println("方法请求路径：" + url);
						System.out.println("请求方法方式：" + type);
						System.out.println("请求字段信息：" + requestParamDtos);
						System.out.println("响应字段信息：" + responseDataDtos);
						System.out.println("响应字段basRespons信息：" + baseResponseDataDtos);
						
						
					}
				}
				
				//构造模板引擎
				ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		        resolver.setPrefix("templates/apiDocs/");//模板所在目录，相对于当前classloader的classpath。
		        resolver.setSuffix(".html");//模板文件后缀
		        resolver.setTemplateMode("HTML");
		        resolver.setCharacterEncoding(CharEncoding.UTF_8);
		        TemplateEngine templateEngine = new TemplateEngine();
			    templateEngine.setTemplateResolver(resolver);
			        

		        //构造上下文(Model)
		        Context context = new Context();
		        context.setVariable("classExplainDto", classExplainDto); //类的头部相关信息
		        context.setVariable("methodDescriptions", methodDescriptions); //方法业务名称
		        context.setVariable("methodInfoDtos", methodInfoDtos); //方法信息
		        String fileName = classExplainDto.getExplain()+".html";
		        if(StringUtils.isNotBlank(savePath)) {
		        	ClassUtil.createFolder(savePath);
		        }
		        String filePath = savePath+"\\"+fileName;
		        //渲染模板
		        FileWriter write = new FileWriter(Paths.get(filePath).toFile());
		        templateEngine.process("methodApi", context, write);

			}
			
			if(classExplains.size() > 0) {
				//构造模板引擎
				ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
				resolver.setPrefix("templates/apiDocs/");//模板所在目录，相对于当前classloader的classpath。
				resolver.setSuffix(".html");//模板文件后缀
				resolver.setTemplateMode("HTML");
				resolver.setCharacterEncoding(CharEncoding.UTF_8);
				TemplateEngine templateEngine = new TemplateEngine();
				templateEngine.setTemplateResolver(resolver);
				
				
				//构造上下文(Model)
				Context context = new Context();
				context.setVariable("classExplains", classExplains); //类的头部相关信息
				String fileName = "index.html";
				if(StringUtils.isNotBlank(savePath)) {
					ClassUtil.createFolder(savePath);
				}
				String filePath = savePath+"\\"+fileName;
				//渲染模板
				FileWriter write = new FileWriter(Paths.get(filePath).toFile());
				templateEngine.process("index", context, write);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}*/

	}
	
	

}
