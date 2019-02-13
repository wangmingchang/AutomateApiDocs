package com.github.wangmingchang.automateapidocs.utils.apidocs;

import com.github.wangmingchang.automateapidocs.annotation.ApiDocsClass.Null;
import com.github.wangmingchang.automateapidocs.annotation.ApiDocsMethod;
import com.github.wangmingchang.automateapidocs.annotation.ApiDocsParam;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.*;
import com.google.gson.Gson;

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

/**
 * 类相关的工具类
 *
 * @author 王明昌
 * @since 2017年9月9日
 */
public class ClassUtil {

    private static List<ClassFiedInfoDto> fieldInfoList = new CopyOnWriteArrayList<ClassFiedInfoDto>();
    /**
     * 属性名和类型集合
     ***/
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
            LoggerUtil.info(clas.getName());
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
     * @param classLocation class全名
     * @param packageName   包名
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
     * @param packageName 包名
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
     * @param packageName 包名
     * @param packagePath 包的路径
     * @param recursive   是否循环
     * @param classes     class
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
     * @param packageName 包名
     * @return 类的完整名称
     */
    public static List<String> getClassName(String packageName) {
        return getClassName(packageName, true);
    }

    /**
     * 获取某包下所有类
     *
     * @param packageName  包名
     * @param childPackage 是否遍历子包
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
     * @param filePath     文件路径
     * @param className    类名集合
     * @param childPackage 是否遍历子包
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
     * @param jarPath      jar文件路径
     * @param childPackage 是否遍历子包
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
     * @param urls         URL集合
     * @param packagePath  包路径
     * @param childPackage 是否遍历子包
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
     * @param currentClass class
     * @param isDelete     是否删除list的缓存
     * @param gradeNum     排序号
     * @return 类的所有字段属性名称（包括父类）
     * @throws Exception 自定异常
     */
    public static List<ClassFiedInfoDto> getClassFieldAndMethod(Class<?> currentClass, boolean isDelete, int gradeNum)
            throws Exception {
        if (isDelete) {
            fieldInfos.clear(); // 清空List
        }
        String className = currentClass.getName();
        Map<String,String> pojoRemarkMap = getPojoFieldRemark(currentClass); // 注释
        Field[] objFields = currentClass.getDeclaredFields(); // 字段
        int fieldNum = 0;// 字段数
        List<Field> fields = new ArrayList<Field>();
        for (int i = 0; i < objFields.length; i++) {
            // 获取get方法的字段
            Field field = objFields[i];
            field.setAccessible(true);
            if (getGetMethod(currentClass, field.getName()) != null) {
                fields.add(field);
                fieldNum++;
            }
        }
        //TODO
        LoggerUtil.info("fields长度：" + fields.size());
        LoggerUtil.info("fields：" + fields);
        LoggerUtil.info("pojoRemarkMap长度：" + pojoRemarkMap.size());
        LoggerUtil.info("pojoRemarkMap：" + new Gson().toJson(pojoRemarkMap));

        /*
         * if (oneWayRemarks.size() != fieldNum) { throw new RuntimeErrorException(null,
         * className + "：类有get方法的字段没有注释"); }
         */
        for (int i = 0; i < fields.size(); i++) {
            ClassFiedInfoDto classFiedInfoDto = new ClassFiedInfoDto();
            Field field = fields.get(i);
            String fieldName = field.getName();
            field.setAccessible(true);
            if (field.isAnnotationPresent(ApiDocsParam.class)) {
                ApiDocsParam apiDocsParam = field.getAnnotation(ApiDocsParam.class);
                if (!apiDocsParam.value()) {
                    // 不是必传字段
                    classFiedInfoDto.setIfPass(false);
                }
            }
            classFiedInfoDto.setName(fieldName);
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
                        classFiedInfoDto.setChildNode(fieldName);
                        // 获取list中对象
                        if (!className.equals(genericClazz.getName())) {
                            getClassFieldAndMethodForChildNode(genericClazz, fieldName, gradeNum + 1);
                        }
                    }
                }
                classFiedInfoDto.setGrade(gradeNum + 1);
            } else if (type.indexOf("class") != -1 || type.indexOf("Serializable") != -1) {
                Type t = field.getGenericType();
                //当前字段的真实类型
                String typeName = t.getTypeName();
                if(typeName.equals("T")){
                    //如果字段是泛型，则不获取它的真实对象字段信息
                    type = "object";
                }else {
                    // 可能字段是一个对象
                    Class<?> forName = Class.forName(type.substring(6, type.length()));
                    // 设置子节点
                    classFiedInfoDto.setChildNode(fieldName);
                    getClassFieldAndMethodForChildNode(forName, fieldName, gradeNum + 1);
                    classFiedInfoDto.setGrade(gradeNum + 1);
                    type = "class";
                }
            } else {
                classFiedInfoDto.setGrade(gradeNum);
            }

            classFiedInfoDto.setType(type);
            classFiedInfoDto.setDescription(pojoRemarkMap.get(fieldName));

            fieldInfos.add(classFiedInfoDto);
        }

        if (currentClass.getSuperclass() != null && currentClass.getSuperclass() != Object.class) {
            //有父类
            getClassFieldAndMethod(currentClass.getSuperclass(), false, gradeNum + 1);
        }
        return fieldInfos;
    }

    /**
     * 获取类的所有字段属性名称（包括父类）-当前的线程
     *
     * @param currentClass class
     * @param isDelete     是否删除list的缓存
     * @param gradeNum     排序号
     * @return 类的所有字段属性名称（包括父类）-当前的线程的list
     * @throws Exception 自定义异常
     */
    public static List<ClassFiedInfoDto> getCurrnetClassFieldAndMethod(Class<?> currentClass, boolean isDelete,
                                                                       int gradeNum) throws Exception {
        if (isDelete) {
            fieldInfoList.clear(); // 清空List
        }
        String className = currentClass.getName();
        Map<String, String> pojoFieldRemarkMap = getPojoFieldRemark(currentClass);// 注释
        Field[] objFields = currentClass.getDeclaredFields(); // 字段
        int fieldNum = 0;// 字段数
        List<Field> fields = new ArrayList<Field>();
        for (int i = 0; i < objFields.length; i++) {
            // 获取get方法的字段
            Field field = objFields[i];
            field.setAccessible(true);
            if (getGetMethod(currentClass, field.getName()) != null) {
                fields.add(field);
                fieldNum++;
            }
        }
        if (pojoFieldRemarkMap.size() != fieldNum) {
            throw new RuntimeErrorException(null, className + "：类有get方法的字段没有注释");
        }
        for (int i = 0; i < fields.size(); i++) {
            ClassFiedInfoDto classFiedInfoDto = new ClassFiedInfoDto();
            Field field = fields.get(i);
            field.setAccessible(true);
            String fieldName = field.getName();
            if (field.isAnnotationPresent(ApiDocsParam.class)) {
                ApiDocsParam apiDocsParam = field.getAnnotation(ApiDocsParam.class);
                if (!apiDocsParam.value()) {
                    // 不是必传字段
                    classFiedInfoDto.setIfPass(false);
                }
            }
            classFiedInfoDto.setName(fieldName);
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
                        getClassFieldAndMethodForChildNode(genericClazz, fieldName, gradeNum + 1);
                    }
                }
                classFiedInfoDto.setGrade(gradeNum + 1);
            } else if (type.indexOf("class") != -1) {
                Type t = field.getGenericType();
                //当前字段的真实类型
                String typeName = t.getTypeName();
                if(typeName.equals("T")){
                    //如果字段是泛型，则不获取它的真实对象字段信息
                    type = "object";
                }else {
                    // 可能字段是一个对象
                    Class<?> forName = Class.forName(type.substring(6, type.length()));
                    // 设置子节点
                    classFiedInfoDto.setChildNode(fieldName);
                    getClassFieldAndMethodForChildNode(forName, fieldName, gradeNum + 1);
                    type = "class";
                    classFiedInfoDto.setGrade(gradeNum + 1);
                }
            } else {
                classFiedInfoDto.setGrade(gradeNum);
            }
            classFiedInfoDto.setType(type);
            classFiedInfoDto.setDescription(pojoFieldRemarkMap.get(fieldName));

            fieldInfoList.add(classFiedInfoDto);
        }

        if (currentClass.getSuperclass() != null && currentClass.getSuperclass() != Object.class) {
            getCurrnetClassFieldAndMethod(currentClass.getSuperclass(), false, gradeNum + 1);
        }
        return fieldInfoList;
    }

    /**
     * 子类节点返回所有字段属性名称（包括父类）
     *
     * @param currentClass class
     * @param parentNode   父节点名称
     * @param gradeNum     排序号
     * @throws Exception 自定义异常
     */
    public static void getClassFieldAndMethodForChildNode(Class<?> currentClass, String parentNode, int gradeNum)
            throws Exception {
        String className = currentClass.getName();
        Map<String, String> pojoFieldRemarkMap = getPojoFieldRemark(currentClass);// 注释
        Field[] objFields = currentClass.getDeclaredFields(); // 字段
        List<Field> fieldList = Arrays.asList(objFields);
        fieldList = new ArrayList<Field>(fieldList);
        Iterator<Field> iterator = fieldList.iterator();
        while (iterator.hasNext()) {
            Field field = (Field) iterator.next();
            if (ConstantsUtil.SERIAL_VERSION_VID.equals(field.getName())) {
                iterator.remove();
                break;
            }
        }
        if (pojoFieldRemarkMap.size() != fieldList.size()) {
            throw new RuntimeErrorException(null, className + "：类有字段没有注释");
        }
        for (int i = 0; i < fieldList.size(); i++) {
            ClassFiedInfoDto classFiedInfoDto = new ClassFiedInfoDto();
            if (parentNode != null) {
                classFiedInfoDto.setParentNode(parentNode);
            }
            Field field = fieldList.get(i);
            String fieldName = field.getName();
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
                        classFiedInfoDto.setChildNode(fieldName);
                        // 获取list中对象
                        getClassFieldAndMethodForChildNode(genericClazz, fieldName, gradeNum + 1);
                    }
                }
                classFiedInfoDto.setGrade(gradeNum + 1);
            } else if (type.indexOf("class") != -1) {
                Type t = field.getGenericType();
                //当前字段的真实类型
                String typeName = t.getTypeName();
                if(typeName.equals("T")){
                    //如果字段是泛型，则不获取它的真实对象字段信息
                    type = "object";
                }else {
                    // 可能字段是一个对象
                    Class<?> forName = Class.forName(type.substring(6, type.length()));
                    // 设置子节点
                    classFiedInfoDto.setChildNode(fieldName);
                    getClassFieldAndMethodForChildNode(forName, fieldName, gradeNum + 1);
                    type = "class";
                    classFiedInfoDto.setGrade(gradeNum + 1);
                }
            } else {
                classFiedInfoDto.setGrade(gradeNum);
            }
            classFiedInfoDto.setType(type);
            classFiedInfoDto.setDescription(pojoFieldRemarkMap.get(fieldName));
            fieldInfos.add(classFiedInfoDto);
        }
        // 有父类
        if (currentClass.getSuperclass() != null && currentClass.getSuperclass() != Object.class) {
            getClassFieldAndMethodForChildNode(currentClass.getSuperclass(), parentNode, gradeNum + 1);
        }

    }

    /**
     * 返回字段的类型
     *
     * @param type class
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
     * 获取java的POJO的字段备注
     *
     * @param className
     * @return java文件的单行注释(key ： 字段名称 ， value : 字段备注)
     */
    public static Map<String, String> getPojoFieldRemark(Class<?> className) {
        String filePath = getClassPath(className);
        Map<String, String> remarkMap = new HashMap<>();
        List<Map<String, String>> remarks = new ArrayList<>();
        try {
            FileReader freader = new FileReader(filePath);
            BufferedReader breader = new BufferedReader(freader);
            StringBuilder sb = new StringBuilder();
            try {
                String temp = "";
                boolean flag = false;
                //开始标记
                boolean startFlag = false;
                //结束标记
                boolean endFlag = false;
                while ((temp = breader.readLine()) != null) {
                    if(StringUtil.isBlank(temp)){
                        continue;
                    }
                    if(startFlag){
                        if(temp.contains(ConstantsUtil.FIELD_SCOPE_PUBLIC) || StringUtil.indexOf(temp, ConstantsUtil.SB_IGNORE_STR_ARR)){
                            //endFlag = true;
                            continue;
                        }
                        if(temp.contains(ConstantsUtil.STAR_FLAG) || StringUtil.indexOf(temp, ConstantsUtil.SPECIAL_FLAG_ARR)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                        if(!endFlag){
                            if(temp.contains(ConstantsUtil.SERIAL_VERSION_VID)){
                                continue;
                            }
                            sb.append(temp);
                            if(!flag){
                                sb.append("\n");
                            }
                        }else {
                            break;
                        }
                    }
                    if (temp.contains(ConstantsUtil.FIELD_SCOPE_CLASS)) {
                        startFlag = true;
                    }
                }
                LoggerUtil.info("sb:" + "\n" +  sb);
                String[] sbArr = sb.toString().split("\n");
                for (String str : sbArr){
                    String remark = "";
                    String fieldName = "";
                    String[] strArr = str.split(ConstantsUtil.FIELD_SCOPE_PRIVATE);
                    if(strArr == null || strArr.length < 2){
                        continue;
                    }
                    if(strArr[0].contains(ConstantsUtil.MORE_START_FLAG)){
                        //有多行注释
                        String arr0 = strArr[0];
                        int index = arr0.lastIndexOf(ConstantsUtil.MORE_START_FLAG);
                        arr0 = arr0.substring(index, arr0.length());

                        remark = StringUtil.replaceSlashBlank(StringUtil.replaceStarBlank(arr0));
                        remark = StringUtil.substringBefore(remark, "@");
                        String[] nameArr = strArr[1].split("\\s+");
                        for (String nameStr : nameArr){
                            if(nameStr.contains(";")){
                                fieldName = StringUtil.replaceBlankAll(nameStr.split(";")[0]);
                                break;
                            }
                        }
                    }else {
                        if(strArr[1].contains(ConstantsUtil.ONE_WAY_FLAG)){
                            String[] oneWayArr = strArr[1].split("//");
                            for (String s : oneWayArr){
                                if(s.contains(";")){
                                    String[] nameArr = s.split("\\s+");
                                    for (String nameStr : nameArr){
                                        if(nameStr.contains(";")){
                                            fieldName = StringUtil.replaceBlankAll(nameStr.split(";")[0]);
                                            break;
                                        }
                                    }
                                }else {
                                    remark = StringUtil.replaceBlankAll(s);
                                }
                            }
                        }
                    }
                    if(StringUtil.isNotBlank(fieldName)){
                        remarkMap.put(fieldName, remark);
                    }

                }


            } catch (IOException e) {
                LoggerUtil.error("类：" + className + "文件读取失败", e);
            } finally {
                breader.close();
                freader.close();
            }
        } catch (FileNotFoundException e) {
            LoggerUtil.error("类：" + className + "文件不存在", e);
        } catch (IOException e) {
            LoggerUtil.error("类：" + className + "文件读取失败", e);
        }
        return remarkMap;
    }

    /**
     * 获取Class的java文件绝对路径
     *
     * @param className class
     * @return Class的java文件绝对路径
     */
    public static String getClassPath(Class<?> className) {
        String path = null;
        try {
            File f1 = new File(className.getResource("").getPath());
            path = Paths.get(f1.getPath() + "\\" + className.getSimpleName() + ".java").toString();
            path = path.replaceAll("target", "src");
            path = path.replaceAll("classes", "main");
            String[] split = path.split("\\\\main\\\\");
            for (int i = 0; i < split.length; i++) {
                if (i == 0) {
                    path = split[i] + "\\main\\java\\";
                } else {
                    path += split[i];
                }
            }
        } catch (Exception e) {
            LoggerUtil.info("类：" + className + "文件不存在");
        }
        return path;
    }

    /**
     * 获取Controoler类的多行注释的内容
     *
     * @param className class
     * @return Controoler类的多行注释的内容
     */
    public static ClassMoreRemarkDto getClassMoreRemark(Class<?> className) {
        String filePath = getClassPath(className);
        ClassMoreRemarkDto classMoreRemarkDto = new ClassMoreRemarkDto();
        //类头部多行注释
        StringBuilder classRemarkSb = new StringBuilder();
        //类的方法多行注释Map(key:方法路径；value:备注信息)
        Map<String, MethodExplainDto> methodExplainDtoMap = new HashMap<>();
        //类的方法多行注释Map(key:方法路径；value:备注信息)
        Map<String, StringBuilder> sbMap = new HashMap<String, StringBuilder>();
        try {
            FileReader freader = new FileReader(filePath);
            BufferedReader breader = new BufferedReader(freader);
            StringBuilder moreSb = new StringBuilder();
            StringBuilder tempSb = new StringBuilder();
            //方法的根路径
            String methodRootPath = "";
            //方法的完整路径
            String url = "";
            //是否第一个requestMapping,且还没有解析到class,interface下,如果是，则为根路径
            boolean isOneRequestMappingFlag = true;
            try {
                String temp = "";
                boolean flag = false;
                boolean startFlag = false;
                //不是/**开头标记
                boolean isNotOneFlag = true;
                //当前方法注释是第几个
                int currentNum = 0;
                boolean isExistApiDocsMethod = false;
                String sbMapKey = "";
                //读取文件内容
                while ((temp = breader.readLine()) != null) {
                    //LoggerUtil.info("temp:" + temp);
                    if(StringUtil.isBlank(temp)){
                        continue;
                    }
                    if (temp.contains(ConstantsUtil.MORE_START_FLAG)) {
                        startFlag = true;
                        isNotOneFlag = false;
                        moreSb.delete(0, moreSb.length());
                    } else {
                        isNotOneFlag = true;
                        if(temp.contains(ConstantsUtil.FIELD_SCOPE_CLASS) || temp.contains(ConstantsUtil.FIELD_SCOPE_INTERFACE)){
                            isOneRequestMappingFlag = false;
                        }
                    }
                    if (temp.contains(ConstantsUtil.MORE_END_FLAG)) {
                        startFlag = false;
                    }
                    if (startFlag) {
                        if (temp.contains(ConstantsUtil.STAR_FLAG)) {
                            flag = true;
                        } else {
                            flag = false;
                        }
                    } else {
                        flag = false;
                    }
                    if (flag && isNotOneFlag) {
                        if (temp.contains(ConstantsUtil.PARAM_STR)) {
                            temp = StringUtil.replaceBlank(temp);
                        } else {
                            temp = StringUtil.replaceBlankAll(temp);
                        }
                        moreSb.append(temp);
                        //moreSb.append('\n');
                    } else {
                        //注：@ApiDocsMethod一定要在@RequestMapping前面
                        if(!temp.contains(ConstantsUtil.MORE_START_FLAG) || !temp.contains(ConstantsUtil.ONE_WAY_FLAG)){
                            if (temp.contains(ConstantsUtil.API_DOCS_CLASS_STR)) {
                                tempSb = moreSb;
                                classRemarkSb = tempSb;
                                LoggerUtil.info("moreSb:" + tempSb);
                                moreSb = new StringBuilder();
                                LoggerUtil.info("tempSb:" + tempSb);
                                isExistApiDocsMethod = false;
                            } else if (temp.contains(ConstantsUtil.API_DOCS_METHOD)) {
                                currentNum++;
                                tempSb = moreSb;
                                LoggerUtil.info("moreSb:" + moreSb);
                                sbMapKey = ConstantsUtil.METHOD_MAP_KEY + currentNum;
                                sbMap.put(sbMapKey, tempSb);
                                moreSb = new StringBuilder();
                                LoggerUtil.info("tempSb:" + tempSb);
                                isExistApiDocsMethod = true;
                            }else if(StringUtil.indexOf(temp, ConstantsUtil.SPRING_REQUEST_MAPPER)){
                                if(isOneRequestMappingFlag){
                                    methodRootPath = getMethodPath(temp);
                                    int lastIndex = methodRootPath.lastIndexOf("/");
                                    if(lastIndex == (methodRootPath.length() - 1)){
                                        methodRootPath = methodRootPath.substring(0, lastIndex);
                                    }
                                }else {
                                    if(isExistApiDocsMethod){
                                        url = methodRootPath + getMethodPath(temp);
                                        LoggerUtil.info("url:" + url);
                                        StringBuilder sbMapValue = sbMap.get(sbMapKey);
                                        sbMap.put(url, sbMapValue);
                                        sbMap.remove(sbMapKey);
                                    }

                                }
                                isExistApiDocsMethod = false;
                            }
                        }
                    }
                }
                //保存class信息
                String[] classRemarkSbArr = classRemarkSb.toString().split("\\*");
                ClassExplainDto classExplainDto = new ClassExplainDto();
                String classExplain = "";
                String classAuthor = "";
                String classCreateDate = "";
                for (String str : classRemarkSbArr){
                    if(StringUtil.indexOf(str, ConstantsUtil.AUTHOR_ARR)){
                        classAuthor = StringUtil.replaceBlankAll(StringUtil.replaceCustomBlank(str, ConstantsUtil.AUTHOR_ARR, ""));
                    }else if(StringUtil.indexOf(str, ConstantsUtil.DATE_ARR)){
                        classCreateDate = StringUtil.replaceBlankAll(StringUtil.replaceCustomBlank(str, ConstantsUtil.DATE_ARR, ""));
                    }else if(StringUtil.indexOf(str, ConstantsUtil.EXPLAIN_ARR)){
                        classExplain += StringUtil.replaceBlankAll(str);
                    }else {
                        if(!StringUtil.indexOf(str, ConstantsUtil.NO_CHECK_ARR)){
                            classExplain += StringUtil.replaceBlankAll(str);
                        }
                    }
                }
                classExplain = StringUtil.substringBefore(classExplain, "@");
                classExplainDto.setExplain(classExplain);
                classExplainDto.setAuthor(classAuthor);
                classExplainDto.setCreateDate(classCreateDate);
                classMoreRemarkDto.setClassExplainDto(classExplainDto);

                //保存方法信息
                Set<Map.Entry<String, StringBuilder>> entries = sbMap.entrySet();
                for (Map.Entry<String, StringBuilder> entry : entries){
                    List<RequestParamDto> requestParamDtos = new ArrayList<>();
                    String mUrl = entry.getKey();
                    StringBuilder sb = entry.getValue();
                    String[] sbArr = sb.toString().split("\\*");
                    String explain = ""; // 方法业务说明
                    boolean isExplainFlag = true;
                    for (int j = 0 ; j < sbArr.length; j++) {
                        String str = sbArr[j];
                        boolean isOneFlag = true;
                        String paramName = ""; //参数名称
                        String paramExplain = "";
                        if (str.indexOf(ConstantsUtil.PARAM_STR) != -1) {
                            isExplainFlag = false;
                            //str有两种情况，第一种是：" @param map";第二种是：" @param map 请求参数"
                            String[] strArr = str.split("\\s+"); //以空格为分割
                            for (String s : strArr) {
                                if (StringUtil.isNotBlank(s) && s.indexOf(ConstantsUtil.PARAM_STR) == -1) {
                                    if (isOneFlag) {
                                        paramName = StringUtil.replaceBlankAll(s);
                                    } else {
                                        paramExplain += StringUtil.replaceBlankAll(s);
                                    }
                                    isOneFlag = false;
                                }
                            }
                            if (StringUtil.isBlank(paramExplain)) {
                                //第二种情况
                                int num = j + 1;
                                if (num < sbArr.length) {
                                    paramExplain = StringUtil.replaceBlankAll(sbArr[num]);
                                }
                            }

                        } else if (str.indexOf(ConstantsUtil.RETURN_STR) != -1) {
                            isExplainFlag = false;
                            //返回参数,暂时不处理
                        } else {
                            if (isExplainFlag && StringUtil.isNotBlank(str)) {
                                explain += StringUtil.replaceBlankAll(str);
                            }
                        }
                        if (StringUtil.isNotBlank(paramName)) {
                            RequestParamDto requestParamDto = new RequestParamDto();
                            requestParamDto.setName(paramName);
                            requestParamDto.setDescription(paramExplain);
                            requestParamDtos.add(requestParamDto);
                        }
                    }
                    explain = StringUtil.substringBefore(explain, "@");
                    if (StringUtil.isNotBlank(explain)) {
                        MethodExplainDto methodExplainDto = new MethodExplainDto();
                        methodExplainDto.setExplain(explain);
                        methodExplainDto.setMethodPath(mUrl);
                        methodExplainDto.setParamDtos(requestParamDtos);
                        LoggerUtil.info("mUrl:" + mUrl);
                        LoggerUtil.info("methodExplainDto:" + new Gson().toJson(methodExplainDto));
                        methodExplainDtoMap.put(mUrl, methodExplainDto);
                    }
                }
                classMoreRemarkDto.setMethodExplainDtoMap(methodExplainDtoMap);


            } catch (IOException e) {
                LoggerUtil.error("类：" + className + "文件读取失败", e);
            } finally {
                breader.close();
                freader.close();
            }
        } catch (FileNotFoundException e) {
            LoggerUtil.error("类：" + className + "文件不存在", e);
        } catch (IOException e) {
            LoggerUtil.error("类：" + className + "文件读取失败", e);
        }
        return classMoreRemarkDto;
    }

    /**
     * 获取类中的路径
     * @author wangmingchang
     * @date 2019/1/25 9:57
     * @param temp
     * @return
     **/
    private static String getMethodPath(String temp) {
        String path = "";
        String[] tempArr = temp.split("\"");
        path = tempArr[1];
        if(!path.startsWith("/")){
            path = "/" + path;
        }
        return path;
    }

    /**
     * java反射bean的get方法
     *
     * @param objectClass class
     * @param fieldName   字段名
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
     * @param objectClass class
     * @param fieldName   字段名
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
     * @param arr         数组
     * @param targetValue 值
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
     * @param cls class
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