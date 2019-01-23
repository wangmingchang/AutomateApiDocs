package com.github.wangmingchang.automateapidocs.utils.apidocs;

import com.github.wangmingchang.automateapidocs.annotation.ApiDocsClass.Null;
import com.github.wangmingchang.automateapidocs.annotation.ApiDocsMethod;
import com.github.wangmingchang.automateapidocs.annotation.ApiDocsParam;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.RuntimeErrorException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类相关的工具类
 * 
 * @author 王明昌
 * @since 2017年9月9日
 */
public class ClassUtil {

	private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

	private static List<ClassFiedInfoDto> fieldInfoList = new CopyOnWriteArrayList<ClassFiedInfoDto>();
	/** 属性名和类型集合 ***/
	private static List<ClassFiedInfoDto> fieldInfos = new ArrayList<ClassFiedInfoDto>();
    public static ClassUtil classUtil = new ClassUtil();
    private static int gradeNum = 1; // 级别

    private ClassUtil() {
        super();
        fieldInfos.clear();
        fieldInfos = new ArrayList<ClassFiedInfoDto>();
    }

    public static ClassUtil getInstance() {
        return classUtil;
    }

    public static void main(String[] args) throws Exception {
        List<Class> classes = ClassUtil.getAllClassByInterface(Class.forName("com.threeti.dao.base.IGenericDao"));
        for (Class clas : classes) {
            System.out.println(clas.getName());
        }
    }

    /**
     * 取得某个接口下所有实现这个接口的类
     *
     * @param c class
     * @return 取得某个接口下所有实现这个接口的类list
     */
    public static List<Class> getAllClassByInterface(Class c) {
        List<Class> returnClassList = null;

        if (c.isInterface()) {
            // 获取当前的包名
            String packageName = c.getPackage().getName();
            // 获取当前包下以及子包下所以的类
            List<Class<?>> allClass = getClasses(packageName);
            if (allClass != null) {
                returnClassList = new ArrayList<Class>();
                for (Class classes : allClass) {
                    // 判断是否是同一个接口
                    if (c.isAssignableFrom(classes)) {
                        // 本身不加入进去
                        if (!c.equals(classes)) {
                            returnClassList.add(classes);
                        }
                    }
                }
            }
        }

		return returnClassList;
	}

	/**
	 * 取得某一类所在包的所有类名 不含迭代
	 * 
	 * @param classLocation
	 *            class全名
	 * @param packageName
	 *            包名
	 * @return 取得某一类所在包的所有类名的数组
	 */
	public static String[] getPackageAllClassName(String classLocation, String packageName) {
		// 将packageName分解
		String[] packagePathSplit = packageName.split("[.]");
		String realClassLocation = classLocation;
		int packageLength = packagePathSplit.length;
		for (int i = 0; i < packageLength; i++) {
			realClassLocation = realClassLocation + File.separator + packagePathSplit[i];
		}
		File packeageDir = new File(realClassLocation);
		if (packeageDir.isDirectory()) {
			String[] allClassName = packeageDir.list();
			return allClassName;
		}
		return null;
	}

	/**
	 * 从包package中获取所有的Class
	 * 
	 * @param packageName
	 *            包名
	 * @return class的list
	 */
	public static List<Class<?>> getClasses(String packageName) {

		// 第一个class类的集合
		List<Class<?>> classes = new ArrayList<Class<?>>();
		// 是否循环迭代
		boolean recursive = true;
		// 获取包的名字 并进行替换
		String packageDirName = packageName.replace('.', '/');
		// 定义一个枚举的集合 并进行循环来处理这个目录下的things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			// 循环迭代下去
			while (dirs.hasMoreElements()) {
				// 获取下一个元素
				URL url = dirs.nextElement();
				// 得到协议的名称
				String protocol = url.getProtocol();
				// 如果是以文件的形式保存在服务器上
				if ("file".equals(protocol)) {
					// 获取包的物理路径
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// 以文件的方式扫描整个包下的文件 并添加到集合中
					findAndAddClassesInPackageByFile(packageName, filePath, recursive, classes);
				} else if ("jar".equals(protocol)) {
					// 如果是jar包文件
					// 定义一个JarFile
					JarFile jar;
					try {
						// 获取jar
						jar = ((JarURLConnection) url.openConnection()).getJarFile();
						// 从此jar包 得到一个枚举类
						Enumeration<JarEntry> entries = jar.entries();
						// 同样的进行循环迭代
						while (entries.hasMoreElements()) {
							// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// 如果是以/开头的
							if (name.charAt(0) == '/') {
								// 获取后面的字符串
								name = name.substring(1);
							}
							// 如果前半部分和定义的包名相同
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// 如果以"/"结尾 是一个包
								if (idx != -1) {
									// 获取包名 把"/"替换成"."
									packageName = name.substring(0, idx).replace('/', '.');
								}
								// 如果可以迭代下去 并且是一个包
								if ((idx != -1) || recursive) {
									// 如果是一个.class文件 而且不是目录
									if (name.endsWith(".class") && !entry.isDirectory()) {
										// 去掉后面的".class" 获取真正的类名
										String className = name.substring(packageName.length() + 1, name.length() - 6);
										try {
											// 添加到classes
											classes.add(Class.forName(packageName + '.' + className));
										} catch (ClassNotFoundException e) {
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}

	/**
	 * 以文件的形式来获取包下的所有Class
	 * 
	 * @param packageName
	 *            包名
	 * @param packagePath
	 *            包的路径
	 * @param recursive
	 *            是否循环
	 * @param classes
	 *            class
	 */
	public static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive,
			List<Class<?>> classes) {
		// 获取此包的目录 建立一个File
		File dir = new File(packagePath);
		// 如果不存在或者 也不是目录就直接返回
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 如果存在 就获取包下的所有文件 包括目录
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// 自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});
		// 循环所有文件
		for (File file : dirfiles) {
			// 如果是目录 则继续扫描
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(packageName + "." + file.getName(), file.getAbsolutePath(), recursive,
						classes);
			} else {
				// 如果是java类文件 去掉后面的.class 只留下类名
				String className = file.getName().substring(0, file.getName().length() - 6);
				try {
					// 添加到集合中去
					classes.add(Class.forName(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取某包下（包括该包的所有子包）所有类
	 * 
	 * @param packageName
	 *            包名
	 * @return 类的完整名称
	 */
	public static List<String> getClassName(String packageName) {
		return getClassName(packageName, true);
	}

	/**
	 * 获取某包下所有类
	 * 
	 * @param packageName
	 *            包名
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	public static List<String> getClassName(String packageName, boolean childPackage) {
		List<String> fileNames = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		String packagePath = packageName.replace(".", "/");
		URL url = loader.getResource(packagePath);
		if (url != null) {
			String type = url.getProtocol();
			if (type.equals("file")) {
				fileNames = getClassNameByFile(url.getPath(), null, childPackage);
			} else if (type.equals("jar")) {
				fileNames = getClassNameByJar(url.getPath(), childPackage);
			}
		} else {
			fileNames = getClassNameByJars(((URLClassLoader) loader).getURLs(), packagePath, childPackage);
		}
		return fileNames;
	}

	/**
	 * 从项目文件获取某包下所有类
	 * 
	 * @param filePath
	 *            文件路径
	 * @param className
	 *            类名集合
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<String> getClassNameByFile(String filePath, List<String> className, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		File file = new File(filePath);
		File[] childFiles = file.listFiles();
		for (File childFile : childFiles) {
			if (childFile.isDirectory()) {
				if (childPackage) {
					myClassName.addAll(getClassNameByFile(childFile.getPath(), myClassName, childPackage));
				}
			} else {
				String childFilePath = childFile.getPath();
				if (childFilePath.endsWith(".class")) {
					childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9,
							childFilePath.lastIndexOf("."));
					childFilePath = childFilePath.replace("\\", ".");
					myClassName.add(childFilePath);
				}
			}
		}

		return myClassName;
	}

	/**
	 * 从jar获取某包下所有类
	 * 
	 * @param jarPath
	 *            jar文件路径
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<String> getClassNameByJar(String jarPath, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		String[] jarInfo = jarPath.split("!");
		String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
		String packagePath = jarInfo[1].substring(1);
		try {
			JarFile jarFile = new JarFile(jarFilePath);
			Enumeration<JarEntry> entrys = jarFile.entries();
			while (entrys.hasMoreElements()) {
				JarEntry jarEntry = entrys.nextElement();
				String entryName = jarEntry.getName();
				if (entryName.endsWith(".class")) {
					if (childPackage) {
						if (entryName.startsWith(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					} else {
						int index = entryName.lastIndexOf("/");
						String myPackagePath;
						if (index != -1) {
							myPackagePath = entryName.substring(0, index);
						} else {
							myPackagePath = entryName;
						}
						if (myPackagePath.equals(packagePath)) {
							entryName = entryName.replace("/", ".").substring(0, entryName.lastIndexOf("."));
							myClassName.add(entryName);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return myClassName;
	}

	/**
	 * 从所有jar中搜索该包，并获取该包下所有类
	 * 
	 * @param urls
	 *            URL集合
	 * @param packagePath
	 *            包路径
	 * @param childPackage
	 *            是否遍历子包
	 * @return 类的完整名称
	 */
	private static List<String> getClassNameByJars(URL[] urls, String packagePath, boolean childPackage) {
		List<String> myClassName = new ArrayList<String>();
		if (urls != null) {
			for (int i = 0; i < urls.length; i++) {
				URL url = urls[i];
				String urlPath = url.getPath();
				// 不必搜索classes文件夹
				if (urlPath.endsWith("classes/")) {
					continue;
				}
				String jarPath = urlPath + "!/" + packagePath;
				myClassName.addAll(getClassNameByJar(jarPath, childPackage));
			}
		}
		return myClassName;
	}

	/**
	 * 获取类的所有字段属性名称（包括父类）
	 * 
	 * @param cur_class
	 *            class
	 * @param baseResponseBeanGenericity
	 * @param isDelete
	 *            是否删除list的缓存
	 * @param gradeNum
	 *            排序号
	 * @return 类的所有字段属性名称（包括父类）
	 * @throws Exception
	 *             自定异常
	 */
	public static List<ClassFiedInfoDto> getClassFieldAndMethod(Class<?> cur_class, Class<?> baseResponseBeanGenericity, boolean isDelete, int gradeNum)
			throws Exception {
		if (isDelete) {
			fieldInfos.clear(); // 清空List
		}
		String class_name = cur_class.getName();
		List<String> oneWayRemarks = getOneWayRemark(cur_class); // 注释
		Field[] obj_fields = cur_class.getDeclaredFields(); // 字段
		int fieldNum = 0;// 字段数
		List<Field> fields = new ArrayList<Field>();
		for (int i = 0; i < obj_fields.length; i++) {
			// 获取get方法的字段
			Field field = obj_fields[i];
			field.setAccessible(true);
			if (getGetMethod(cur_class, field.getName()) != null) {
				fields.add(field);
				fieldNum++;
			}
		}
		//TODO
		System.out.println("fields长度："+fields.size());
		System.out.println("fields--->" + fields);
		System.out.println("oneWayRemarks长度："+oneWayRemarks.size());
		System.out.println("oneWayRemarks--->" + oneWayRemarks);
		
		/*
		 * if (oneWayRemarks.size() != fieldNum) { throw new RuntimeErrorException(null,
		 * class_name + "：类有get方法的字段没有注释"); }
		 */
		for (int i = 0; i < fields.size(); i++) {
			ClassFiedInfoDto classFiedInfoDto = new ClassFiedInfoDto();
			Field field = fields.get(i);
			field.setAccessible(true);
			if (field.isAnnotationPresent(ApiDocsParam.class)) {
				ApiDocsParam apiDocsParam = field.getAnnotation(ApiDocsParam.class);
				if (!apiDocsParam.value()) {
					// 不是必传字段
					classFiedInfoDto.setIfPass(false);
				}
			}
			classFiedInfoDto.setName(field.getName());

			String type = getClassFieldType(field.getType());
			if (type.equals("list")) {
				Type listType = field.getGenericType();
				if (listType instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) listType;

					// 获取list中的真实类型
					Class genericClazz = (Class) pt.getActualTypeArguments()[0];
					if (!genericClazz.getName().startsWith("java.lang")
							&& !genericClazz.getName().startsWith("java.util.Date")
							&& !genericClazz.getName().startsWith("javax")
							&& !genericClazz.getName().startsWith("com.sun")
							&& !genericClazz.getName().startsWith("sun")
							&& !genericClazz.getName().startsWith("boolean")
							&& !genericClazz.getName().startsWith("double")
							&& !genericClazz.getName().startsWith("int")) {
						// 设置子节点
						classFiedInfoDto.setChildNode(field.getName());
						// 获取list中对象
						if (!class_name.equals(genericClazz.getName())) {
							getClassFieldAndMethodForChildNode(genericClazz, field.getName(), gradeNum + 1);
						}
					}
				}
				classFiedInfoDto.setGrade(gradeNum + 1);
			} else if (type.indexOf("class") != -1) {
				// 可能字段是一个对象
				Class<?> forName = Class.forName(type.substring(6, type.length()));
				// 设置子节点
				classFiedInfoDto.setChildNode(field.getName());
				getClassFieldAndMethodForChildNode(forName, field.getName(), gradeNum + 1);
				type = "class";
				classFiedInfoDto.setGrade(gradeNum + 1);
			} else {
				classFiedInfoDto.setGrade(gradeNum);
			}

			classFiedInfoDto.setType(type);
			classFiedInfoDto.setDescription(oneWayRemarks.get(i));

			fieldInfos.add(classFiedInfoDto);
		}

		if (cur_class.getSuperclass() != null && cur_class.getSuperclass() != Object.class) {
			getClassFieldAndMethod(cur_class.getSuperclass(), false, gradeNum + 1);
			// .out.println("父类：" + cur_class.getSuperclass());
		}
		return fieldInfos;
	}
	/**
	 * 获取类的所有字段属性名称（包括父类）
	 *
	 * @param cur_class
	 *            class
	 * @param isDelete
	 *            是否删除list的缓存
	 * @param gradeNum
	 *            排序号
	 * @return 类的所有字段属性名称（包括父类）
	 * @throws Exception
	 *             自定异常
	 */
	public static List<ClassFiedInfoDto> getClassFieldAndMethod(Class<?> cur_class, boolean isDelete, int gradeNum)
			throws Exception {
		if (isDelete) {
			fieldInfos.clear(); // 清空List
		}
		String class_name = cur_class.getName();
		List<String> oneWayRemarks = getOneWayRemark(cur_class); // 注释
		Field[] obj_fields = cur_class.getDeclaredFields(); // 字段
		int fieldNum = 0;// 字段数
		List<Field> fields = new ArrayList<Field>();
		for (int i = 0; i < obj_fields.length; i++) {
			// 获取get方法的字段
			Field field = obj_fields[i];
			field.setAccessible(true);
			if (getGetMethod(cur_class, field.getName()) != null) {
				fields.add(field);
				fieldNum++;
			}
		}
		//TODO
		System.out.println("fields长度："+fields.size());
		System.out.println("fields--->" + fields);
		System.out.println("oneWayRemarks长度："+oneWayRemarks.size());
		System.out.println("oneWayRemarks--->" + oneWayRemarks);

		/*
		 * if (oneWayRemarks.size() != fieldNum) { throw new RuntimeErrorException(null,
		 * class_name + "：类有get方法的字段没有注释"); }
		 */
		for (int i = 0; i < fields.size(); i++) {
			ClassFiedInfoDto classFiedInfoDto = new ClassFiedInfoDto();
			Field field = fields.get(i);
			field.setAccessible(true);
			if (field.isAnnotationPresent(ApiDocsParam.class)) {
				ApiDocsParam apiDocsParam = field.getAnnotation(ApiDocsParam.class);
				if (!apiDocsParam.value()) {
					// 不是必传字段
					classFiedInfoDto.setIfPass(false);
				}
			}
			classFiedInfoDto.setName(field.getName());

			String type = getClassFieldType(field.getType());
			if (type.equals("list")) {
				Type listType = field.getGenericType();
				if (listType instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) listType;

					// 获取list中的真实类型
					Class genericClazz = (Class) pt.getActualTypeArguments()[0];
					if (!genericClazz.getName().startsWith("java.lang")
							&& !genericClazz.getName().startsWith("java.util.Date")
							&& !genericClazz.getName().startsWith("javax")
							&& !genericClazz.getName().startsWith("com.sun")
							&& !genericClazz.getName().startsWith("sun")
							&& !genericClazz.getName().startsWith("boolean")
							&& !genericClazz.getName().startsWith("double")
							&& !genericClazz.getName().startsWith("int")) {
						// 设置子节点
						classFiedInfoDto.setChildNode(field.getName());
						// 获取list中对象
						if (!class_name.equals(genericClazz.getName())) {
							getClassFieldAndMethodForChildNode(genericClazz, field.getName(), gradeNum + 1);
						}
					}
				}
				classFiedInfoDto.setGrade(gradeNum + 1);
			} else if (type.indexOf("class") != -1) {
				// 可能字段是一个对象
				Class<?> forName = Class.forName(type.substring(6, type.length()));
				// 设置子节点
				classFiedInfoDto.setChildNode(field.getName());
				getClassFieldAndMethodForChildNode(forName, field.getName(), gradeNum + 1);
				type = "class";
				classFiedInfoDto.setGrade(gradeNum + 1);
			} else {
				classFiedInfoDto.setGrade(gradeNum);
			}

			classFiedInfoDto.setType(type);
			classFiedInfoDto.setDescription(oneWayRemarks.get(i));

			fieldInfos.add(classFiedInfoDto);
		}

		if (cur_class.getSuperclass() != null && cur_class.getSuperclass() != Object.class) {
			getClassFieldAndMethod(cur_class.getSuperclass(), false, gradeNum + 1);
			// .out.println("父类：" + cur_class.getSuperclass());
		}
		return fieldInfos;
	}

	/**
	 * 获取类的所有字段属性名称（包括父类）-当前的线程
	 * 
	 * @param cur_class
	 *            class
	 * @param isDelete
	 *            是否删除list的缓存
	 * @param gradeNum
	 *            排序号
	 * @return 类的所有字段属性名称（包括父类）-当前的线程的list
	 * @throws Exception
	 *             自定义异常
	 */
	public static List<ClassFiedInfoDto> getCurrnetClassFieldAndMethod(Class<?> cur_class, boolean isDelete,
			int gradeNum) throws Exception {
		if (isDelete) {
			fieldInfoList.clear(); // 清空List
		}
		String class_name = cur_class.getName();
		List<String> oneWayRemarks = getOneWayRemark(cur_class); // 注释
		Field[] obj_fields = cur_class.getDeclaredFields(); // 字段
		int fieldNum = 0;// 字段数
		List<Field> fields = new ArrayList<Field>();
		for (int i = 0; i < obj_fields.length; i++) {
			// 获取get方法的字段
			Field field = obj_fields[i];
			field.setAccessible(true);
			if (getGetMethod(cur_class, field.getName()) != null) {
				fields.add(field);
				fieldNum++;
			}
		}
		if (oneWayRemarks.size() != fieldNum) {
			throw new RuntimeErrorException(null, class_name + "：类有get方法的字段没有注释");
		}
		for (int i = 0; i < fields.size(); i++) {
			ClassFiedInfoDto classFiedInfoDto = new ClassFiedInfoDto();
			Field field = fields.get(i);
			field.setAccessible(true);
			if (field.isAnnotationPresent(ApiDocsParam.class)) {
				ApiDocsParam apiDocsParam = field.getAnnotation(ApiDocsParam.class);
				if (!apiDocsParam.value()) {
					// 不是必传字段
					classFiedInfoDto.setIfPass(false);
				}
			}
			classFiedInfoDto.setName(field.getName());
			String type = getClassFieldType(field.getType());

			if (type.equals("list")) {
				Type listType = field.getGenericType();
				if (listType instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) listType;

					// 获取list中的真实类型
					Class genericClazz = (Class) pt.getActualTypeArguments()[0];
					if (!genericClazz.getName().startsWith("java.lang")
							&& !genericClazz.getName().startsWith("java.util.Date")
							&& !genericClazz.getName().startsWith("javax")
							&& !genericClazz.getName().startsWith("com.sun")
							&& !genericClazz.getName().startsWith("sun")
							&& !genericClazz.getName().startsWith("boolean")
							&& !genericClazz.getName().startsWith("double")
							&& !genericClazz.getName().startsWith("int")) {
						// 设置子节点
						classFiedInfoDto.setChildNode(field.getName());
						// 获取list中对象
						getClassFieldAndMethodForChildNode(genericClazz, field.getName(), gradeNum + 1);
					}
				}
				classFiedInfoDto.setGrade(gradeNum + 1);
			} else if (type.indexOf("class") != -1) {
				// 可能字段是一个对象
				Class<?> forName = Class.forName(type.substring(6, type.length()));
				// 设置子节点
				classFiedInfoDto.setChildNode(field.getName());
				getClassFieldAndMethodForChildNode(forName, field.getName(), gradeNum + 1);
				type = "class";
				classFiedInfoDto.setGrade(gradeNum + 1);
			} else {
				classFiedInfoDto.setGrade(gradeNum);
			}
			classFiedInfoDto.setType(type);
			classFiedInfoDto.setDescription(oneWayRemarks.get(i));

			fieldInfoList.add(classFiedInfoDto);
		}

		if (cur_class.getSuperclass() != null && cur_class.getSuperclass() != Object.class) {
			getCurrnetClassFieldAndMethod(cur_class.getSuperclass(), false, gradeNum + 1);
		}
		return fieldInfoList;
	}

	/**
	 * 子类节点返回所有字段属性名称（包括父类）
	 * 
	 * @param cur_class
	 *            class
	 * @param parentNode
	 *            父节点名称
	 * @param gradeNum
	 *            排序号
	 * @throws Exception
	 *             自定义异常
	 */
	public static void getClassFieldAndMethodForChildNode(Class<?> cur_class, String parentNode, int gradeNum)
			throws Exception {
		String class_name = cur_class.getName();
		List<String> oneWayRemarks = getOneWayRemark(cur_class); // 注释
		Field[] obj_fields = cur_class.getDeclaredFields(); // 字段
		List<Field> fieldList = Arrays.asList(obj_fields);
		fieldList = new ArrayList<Field>(fieldList);
		Iterator<Field> iterator = fieldList.iterator();
		while (iterator.hasNext()) {
			Field field = (Field) iterator.next();
			if ("serialVersionUID".equals(field.getName())) {
				iterator.remove();
			}
		}
		if (oneWayRemarks.size() != fieldList.size()) {
			throw new RuntimeErrorException(null, class_name + "：类有字段没有注释");
		}
		for (int i = 0; i < fieldList.size(); i++) {
			ClassFiedInfoDto classFiedInfoDto = new ClassFiedInfoDto();
			if (parentNode != null) {
				classFiedInfoDto.setParentNode(parentNode);
			}
			Field field = fieldList.get(i);
			field.setAccessible(true);
			classFiedInfoDto.setName(field.getName());
			String type = getClassFieldType(field.getType());

			if (type.equals("list")) {
				Type listType = field.getGenericType();
				if (listType instanceof ParameterizedType) {
					ParameterizedType pt = (ParameterizedType) listType;

					// 获取list中的真实类型
					Class genericClazz = (Class) pt.getActualTypeArguments()[0];
					if (!genericClazz.getName().startsWith("java.lang")
							&& !genericClazz.getName().startsWith("java.util.Date")
							&& !genericClazz.getName().startsWith("javax")
							&& !genericClazz.getName().startsWith("com.sun")
							&& !genericClazz.getName().startsWith("sun")
							&& !genericClazz.getName().startsWith("boolean")
							&& !genericClazz.getName().startsWith("double")
							&& !genericClazz.getName().startsWith("int")) {
						// 设置子节点
						classFiedInfoDto.setChildNode(field.getName());
						// 获取list中对象
						getClassFieldAndMethodForChildNode(genericClazz, field.getName(), gradeNum + 1);
					}
				}
				classFiedInfoDto.setGrade(gradeNum + 1);
			} else if (type.indexOf("class") != -1) {
				// 可能字段是一个对象
				Class<?> forName = Class.forName(type.substring(6, type.length()));
				// 设置子节点
				classFiedInfoDto.setChildNode(field.getName());
				getClassFieldAndMethodForChildNode(forName, field.getName(), gradeNum + 1);
				type = "class";
				classFiedInfoDto.setGrade(gradeNum + 1);
			} else {
				classFiedInfoDto.setGrade(gradeNum);
			}
			classFiedInfoDto.setType(type);
			classFiedInfoDto.setDescription(oneWayRemarks.get(i));
			fieldInfos.add(classFiedInfoDto);
		}
		// 有父类
		if (cur_class.getSuperclass() != null && cur_class.getSuperclass() != Object.class) {
			getClassFieldAndMethodForChildNode(cur_class.getSuperclass(), parentNode, gradeNum + 1);
		}

	}

	/**
	 * 返回字段的类型
	 * 
	 * @param type
	 *            class
	 * @return 返回字段的类型
	 */
	private static String getClassFieldType(Class<?> type) {
		String rs = "";
		if (type.equals(Object.class)) {
			rs = "object";
		} else if (type.equals(List.class)) {
			rs = "list";
		} else if (type.equals(Map.class)) {
			rs = "map";
		} else if (type.equals(String.class)) {
			rs = "string";
		} else if (type.equals(Integer.class)) {
			rs = "Integer";
		} else if (type.equals(Long.class)) {
			rs = "Log";
		} else if (type.equals(Boolean.class)) {
			rs = "Boolean";
		} else if (type.equals(Double.class)) {
			rs = "Double";
		} else if (type.equals(Float.class)) {
			rs = "Float";
		} else if (type.equals(Short.class)) {
			rs = "Short";
		} else if (type.equals(Date.class)) {
			rs = "date";
		} else {
			rs = type.toString();
		}

		return rs;
	}

	/**
	 * 获取java文件的单行注释
	 * 
	 * @param className
	 *            class
	 * @return java文件的单行注释
	 */
	public static List<String> getOneWayRemark(Class<?> className) {
		String filePath = getClassPath(className);
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
					remarks.add(StringUtil.replaceBlankAll(remarkStr.substring(2)));
					sb.append(src.substring(leftmatcher1.start(), rightmatcher1.end()));
					begin = rightmatcher1.end();
				}
			} catch (IOException e) {
				System.out.println("类：" + className + "文件读取失败");
			} finally {
				breader.close();
				freader.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("类：" + className + "文件不存在");
		} catch (IOException e) {
			System.out.println("类：" + className + "文件读取失败");
		}
		return remarks;
	}

	/**
	 * 获取Class的java文件绝对路径
	 * 
	 * @param className
	 *            class
	 * @return Class的java文件绝对路径
	 */
	public static String getClassPath(Class<?> className) {
		String path = null;
		try {
			File f1 = new File(className.getResource("").getPath());
			path = Paths.get(f1.getPath() + "\\" + className.getSimpleName() + ".java").toString();
			path = path.replaceAll("target", "src");
			path = path.replaceAll("classes", "main");
			String[] split = path.split("main");
			for (int i = 0; i < split.length; i++) {
				if (i == 0) {
					path = split[i] + "main\\java";
				} else {
					path += split[i];
				}
			}
		} catch (Exception e) {
			System.out.println("类：" + className + "文件不存在");
		}
		return path;
	}

	/**
	 * 获取Controoler类的多行注释的内容
	 * 
	 * @param className
	 *            class
	 * @return Controoler类的多行注释的内容
	 */
	public static ClassMoreRemarkDto getClassMoreRemark(Class<?> className) {
		ArrayList<MethodExplainDto> methodExplainDtos = new ArrayList<MethodExplainDto>(); // 方法注释list
		methodExplainDtos.clear();
		String filePath = getClassPath(className);
		String simpleName = className.getSimpleName();
		ClassMoreRemarkDto classMoreRemarkDto = new ClassMoreRemarkDto();
		//类头部多行注释
        StringBuilder classSb = new StringBuilder();
		Map<String, StringBuilder> classMap = new HashMap<String, StringBuilder>();
		//类的方法多行注释Map
		Map<String, StringBuilder> methodMap = new HashMap<String, StringBuilder>();
		try {
			FileReader freader = new FileReader(filePath);
			BufferedReader breader = new BufferedReader(freader);
			StringBuilder sb = new StringBuilder();
			StringBuilder moreSb = new StringBuilder();
			StringBuilder tempSb = new StringBuilder();
			try {
				String temp = "";
				boolean flag = false;
				boolean startFlag = false;
				boolean isNotOneFlag = true;
				//当前方法注释是第几个
				int currentNum = 1;
				//多行注释的开头标记
				String moreStartFlag = "/**";
				String moreEndFlag = "*/";
				String starFlag = "*";

				/**
				 * 读取文件内容，并将读取的每一行后都不加\n 其目的是为了在解析双反斜杠（//）注释时做注释中止符
				 */
				while ((temp = breader.readLine()) != null) {
					logger.info("temp:" + temp);
					if(temp.contains(moreStartFlag)){
						startFlag = true;
						isNotOneFlag = false;
						moreSb.delete(0, moreSb.length());
					}else {
						isNotOneFlag = true;
					}
					if(temp.contains(moreEndFlag)){
						startFlag = false;
					}
					if(startFlag){
						if (temp.contains(starFlag)){
							flag = true;
						}else {
							flag = false;
						}
					}else {
						flag = false;
					}
					if(flag && isNotOneFlag){
					    if (temp.contains(ConstantsUtil.PARAM_STR)){
                            temp = StringUtil.replaceBlank(temp);
                        }else {
                            temp = StringUtil.replaceBlankAll(temp);
                        }
						moreSb.append(temp);
						//moreSb.append('\n');
					}else {
						if(temp.contains(ConstantsUtil.API_DOCS_CLASS_STR)){
                            tempSb = moreSb;
                            classMap.put(simpleName, tempSb);
							logger.info("moreSb:" +tempSb);
							//moreSb.delete(0, moreSb.length());
                            moreSb = new StringBuilder();
                            logger.info("tempSb:" +tempSb);
                        }else if(temp.contains(ConstantsUtil.API_DOCS_METHOD)){
                            tempSb = moreSb;
							logger.info("moreSb:" +moreSb);
							methodMap.put(ConstantsUtil.METHOD_MAP_KEY +"-"+ currentNum, tempSb);
                            //moreSb.delete(0, moreSb.length());
                            currentNum++;
                            moreSb = new StringBuilder();
                            logger.info("tempSb:" +tempSb);
                        }
					}
					sb.append(temp);
					sb.append('\n');
				}
                StringBuilder stringBuilder = classMap.get(simpleName);
                StringBuilder stringBuilder1 = methodMap.get(ConstantsUtil.METHOD_MAP_KEY + "-" + 1);
                String classStr = classMap.get(simpleName).toString();
                String[] classSbArr = classStr.split("\\*");
                String explain = ""; //说明
                boolean isExplainFlag = true;
                String author = ""; //作者
                String createDate = ""; //创建时间
                for (String str :classSbArr ) {
                    if(StringUtil.indexOf(str, ConstantsUtil.AUTHOR_ARR)){
                        author = StringUtil.replaceCustomBlank(str, ConstantsUtil.AUTHOR_ARR, "");
                        isExplainFlag = false;
                    }else if(StringUtil.indexOf(str, ConstantsUtil.DATE_ARR)){
                        createDate = StringUtil.replaceCustomBlank(str, ConstantsUtil.DATE_ARR, "");
                        isExplainFlag = false;
                    }else {
                        if(isExplainFlag){
                            explain += str;
                        }
                    }
                }
                ClassExplainDto classExplainDto = new ClassExplainDto();
                classExplainDto.setExplain(explain);
                classExplainDto.setAuthor(author);
                classExplainDto.setCreateDate(createDate);
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
				// Pattern rightpattern = Pattern.compile("\\*/");
				Pattern rightpattern = Pattern.compile("@ApiDocsClass");
				Matcher rightmatcher = rightpattern.matcher(src);
				sb = new StringBuilder();
				/**
				 * begin 变量用来做渐进匹配的游标 {@value} 初始值为文件开头
				 **/
				int begin = 0;
				while (leftmatcher.find(begin)) {
					boolean find = rightmatcher.find(leftmatcher.start());
					if (!find) {
						rightpattern = Pattern.compile("@ApiDocsMethod");
						rightmatcher = rightpattern.matcher(src);
						find = rightmatcher.find(leftmatcher.start());
					}
					if (!find) {
						// 没有匹配到，结束循环
						break;
					}

					String remarkStr = src.substring(leftmatcher.start(), rightmatcher.end());
					remarkStr = remarkStr.substring(2, remarkStr.length() - 2);
					remarkStr = StringUtils.replace(remarkStr, "*", "");
					remarkStr = StringUtil.replaceBlank(remarkStr);
					if (remarkStr.contains("author")) {
						// 如果有author,就说明是类的头部说明
						//ClassExplainDto classExplainDto = new ClassExplainDto();
						String[] split = StringUtil.split(remarkStr);
						int legth = split.length;
						for (int i = 0; i < split.length; i++) {
							if (split[i].indexOf("@") == -1) {
								// 说明
								if (classExplainDto.getAuthor() == null) {
									classExplainDto.setExplain(split[i]);
								}
								continue;
							}
							if ("@author".equals(split[i])) {
								if (i + 1 < legth) {
									classExplainDto.setAuthor(split[i + 1]);
									continue;
								}
							}
							if ("@since".equals(split[i])) {
								if (i + 1 < legth) {
									classExplainDto.setCreateDate(split[i + 1]);
									continue;
								}
							}
						}
						classMoreRemarkDto.setClassExplainDto(classExplainDto);
					} else {
						// 方法的说明
						MethodExplainDto methodExplainDto = new MethodExplainDto();
						ArrayList<RequestParamDto> paramDtos = new ArrayList<RequestParamDto>();

						String[] split = StringUtil.split(remarkStr);
						int legth = split.length;
						for (int i = 0; i < split.length; i++) {
							if (split[i].indexOf("@") == -1) {
								// 说明
								if (methodExplainDto.getExplain() == null) {
									methodExplainDto.setExplain(split[i]);
								}
								continue;
							}
							if ("@param".equals(split[i])) {
								// 参数
								RequestParamDto paramDto = null;
								if (i + 1 < legth && !"@return".equals(split[i + 1])) {
									paramDto = new RequestParamDto();
									paramDto.setName(split[i + 1]);
								}
								if (i + 2 < legth && !"@return".equals(split[i + 1])) {
									paramDto.setDescription(split[i + 2]);
								}
								if (paramDto != null) {
									paramDtos.add(paramDto);
								}
							}
						}
						methodExplainDto.setParamDtos(paramDtos);
						methodExplainDtos.add(methodExplainDto);
					}
					sb.append(src.substring(leftmatcher.start(), rightmatcher.end()));
					/** 为输出时格式的美观 **/
					sb.append('\n');
					begin = rightmatcher.end();
				}
				classMoreRemarkDto.setMethodExplainDtos(methodExplainDtos);

			} catch (IOException e) {
				System.out.println("类：" + className + "文件读取失败");
			} finally {
				breader.close();
				freader.close();
			}
		} catch (FileNotFoundException e) {
			System.out.println("类：" + className + "文件不存在");
		} catch (IOException e) {
			System.out.println("类：" + className + "文件读取失败");
		}
		return classMoreRemarkDto;
	}

    /**
     * java反射bean的get方法
     *
     * @param objectClass
     *            class
     * @param fieldName
     *            字段名
     * @return 返回bean的get方法
     */
    @SuppressWarnings("unchecked")
    public static Method getGetMethod(Class objectClass, String fieldName) {
        StringBuffer sb = new StringBuffer();
        sb.append("get");
        sb.append(fieldName.substring(0, 1).toUpperCase());
        sb.append(fieldName.substring(1));
        try {
            return objectClass.getMethod(sb.toString());
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * java反射bean的set方法
     *
     * @param objectClass
     *            class
     * @param fieldName
     *            字段名
     * @return 返回bean的set方法
     */
    @SuppressWarnings("unchecked")
    public static Method getSetMethod(Class objectClass, String fieldName) {
        try {
            Class[] parameterTypes = new Class[1];
            Field field = objectClass.getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            StringBuffer sb = new StringBuffer();
            sb.append("set");
            sb.append(fieldName.substring(0, 1).toUpperCase());
            sb.append(fieldName.substring(1));
            Method method = objectClass.getMethod(sb.toString(), parameterTypes);
            return method;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

	/**
	 * 判断String数组是否存在某个值
	 * 
	 * @param arr
	 *            数组
	 * @param targetValue
	 *            值
	 * @return 判断String数组是否存在某个值
	 */
	public static boolean containsStr(String[] arr, String targetValue) {
		for (String s : arr) {
			if (s.equals(targetValue))
				return true;
		}
		return false;
	}

	/**
	 * 判断class是否可真实的对象
	 * 
	 * @param cls
	 *            class
	 * @return 判断class是否可真实的对象
	 */
	public static boolean isRealClass(Class<?> cls) {
		boolean flag = false;
		if (cls != Null.class && cls != ApiDocsMethod.class && !cls.toString().contains("$Null")) {
			flag = true;
		}
		return flag;
	}

}