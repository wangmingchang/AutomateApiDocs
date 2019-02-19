# 简介
这是一个可以自动生成html和word文档格式api的小工具。对写java接口的同行来说是一个不错的选择，而且使用起来非常简单。
# maven地址
```
<dependency>
    <groupId>com.github.wangmingchang</groupId>
    <artifactId>automateapidocs</artifactId>
     <version>2.0</version>
</dependency>

```
# 快速使用
## 【2.0版本】使用配置（推荐）
### 第一步
1.新增一个名字为apiDocs.properties的文件，或者拷贝git上相同名字文件</br>
2.在要生成的Controller类上打上相应的注解</br>
(1)@ApiDocsClass:表示这一个Controller类是要生成api文档的；</br>
(2)@ApiDocsMethod:表示这个方法是要生成api文档的；</br>
如下
``` java
/**
 * HTML-controller
 *
 * @author 王明昌
 * @date 2019年1月9日
 */
@ApiDocsClass
@RestController
@RequestMapping("/api/index/v1/")
public class HtmlController {

    private PageDto PageDto;

    /**
     * index方法
     * @Author wangmingchang
     * @Date 2019/1/9 14:21
     * @return
     */
    @ApiDocsMethod
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public String index() {
        try {
            BizResponseVo<DemoVo> demoVoBizResponseVo = new BizResponseVo<>();
            Class<? extends Object> aClass = demoVoBizResponseVo.getClass();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "index";
    }
    ...
```
### 第二步
**请求或者响应的封装类一定要记得每一个字段都要写备注哦！！！**
``` java
public class DemoVo {

	private String id; // 主键
	private String name; // 姓名
	private Double socre; // 分数
	private CarVo carVo; // 车
	private List<String> urls; // 路径集合

	...

}

```
### 第三步
**新增 apiDocs.properties配置、apiDocsData.properties配置**

### 配置文件
#### 1. apiDocs.properties配置
``` properties
#扫瞄的包,多个包以英文逗号（“,”）隔开
apiDocs.sys.packageName=com.github.wangmingchang.automateapidocs.controller
#生成api保存的路径(注：路径要是linux下的路径方式“/”)
apiDocs.sys.savePath=D:/idea-repository/wangmingchang/AutomateApiDocs/src/main/resources
#是否开启生成word格式api(默认开启)
#apiDocs.sys.isWord=true
#是否开启生成HTML格式api(默认开启)
#apiDocs.sys.isHTML=false
#apiDocsData.properties的编码格式(默认UTF-8)
apiDocs.sys.charsetCode=UTF-8
#是否开户在word文档的响应字段中显示类名(默认开启)
#apiDocs.sys.word.isShowResponseClassName=false
```
#### 2. apiDocsData.properties配置
``` properties
#apid生成的数据配置

#监听controller的根路径
apiDocs.key.reference.request=com.github.wangmingchang.automateapidocs.pojo.apidocs.vo
apiDocs.key.reference.response=com.github.wangmingchang.automateapidocs.pojo.apidocs.vo
apiDocs.key.reference.controller=com.github.wangmingchang.automateapidocs.controller

#请求和响应的实体
apiDocs.key.entry.Fish=${reference.request}.Fish
apiDocs.key.entry.BizResponseVo=${reference.response}.BizResponseVo
apiDocs.key.entry.DemoVo=${reference.response}.DemoVo
apiDocs.key.entry.PageDto=com.github.wangmingchang.automateapidocs.pojo.apidocs.vo.PageDto
apiDocs.key.entry.CarVo=com.github.wangmingchang.automateapidocs.pojo.apidocs.vo.CarVo

#Controller的配置
apiDocs.key.controller.HtmlController=${reference.controller}.HtmlController

#Controller的类的描述
${controller.HtmlController}.classExplain=测试类-1

#method的配置
#方法的路径
${controller.HtmlController}.index().ApiDocsMethod.url=/api/index/v1/index
#方法的请求类型
${controller.HtmlController}.index().ApiDocsMethod.type=post
#方法的说明
${controller.HtmlController}.index().ApiDocsMethod.methodExplain=index方法-2
#方法的请求参数
${controller.HtmlController}.index().ApiDocsMethod.requestBean=${entry.Fish}
#方法的响应基本类
${controller.HtmlController}.index().ApiDocsMethod.baseResponseBean=${entry.BizResponseVo}
#方法的响应的泛型真实类型
${controller.HtmlController}.index().ApiDocsMethod.baseResponseBeanGenericity=${entry.DemoVo}
#方法的响应类
${controller.HtmlController}.index().ApiDocsMethod.responseBean=
#方法的多个响应类
${controller.HtmlController}.index().ApiDocsMethod.responseBeans=
#方法的请求参数的字段不是必传的
${controller.HtmlController}.index().ApiDocsParam.request.false=${entry.Fish.type}
#方法的请求参数的字段不需要
${controller.HtmlController}.index().ApiDocsParam.request.isShow.false=${entry.Fish.pageDto}
#方法的响应类的字段不需要
${controller.HtmlController}.index().ApiDocsParam.response.isShow.false=${entry.DemoVo.carVo.teachar},${entry.DemoVo.carVo.displacement},${entry.DemoVo.carVo}
#方法的请求参数样例
${controller.HtmlController}.index().ApiDocsMethod.requestBean.json.key=request-index-BizResponseVo
#方法的响应结果样例
${controller.HtmlController}.index().ApiDocsMethod.responseBean.json.key=response-index-BizResponseVo

```


### 第四步
**执行调用**
``` java
public class DocContext {

    
	public static void main(String[] args) {
		
		ApiDocsUtil.init();

	}
}

```
**最终输出结果**
![image](https://github.com/wangmingchang/AutomateApiDocs/blob/master/src/main/resources/static/word-2.png)
![image](https://github.com/wangmingchang/AutomateApiDocs/blob/master/src/main/resources/static/html-2-1.png)
![image](https://github.com/wangmingchang/AutomateApiDocs/blob/master/src/main/resources/static/html-2-2.png)

## 【1.0版本】不使用配置
1.新增一个名字为apiDocs.properties的文件，或者拷贝git上相同名字文件</br>
2.在要生成的Controller类上打上相应的注解</br>
(1)@ApiDocsClass:表示这一个Controller类是要生成api文档的；</br>
(2)@ApiDocsMethod:表示这个方法是要生成api文档的；</br>
如下
``` java
/**
 * HTML-controller
 * 
 * @author 王明昌
 * @since 2017年9月3日
 */
@ApiDocsClass
@RestController
@RequestMapping("/api/index/v1")
public class HtmlController {

	private PageDto PageDto;

	/**
	 * index方法
	 * 
	 * @return 请的路径
	 */
	@ApiDocsMethod(baseResponseBean = BaseResponseVo.class, methodExplain = "index方法", responseBean = DemoVo.class, responseBeans = {
			Fish.class, CatVo.class })
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * hell方法
	 * 
	 * @param map
	 *            请求参数
	 * @return 请的路径
	 */
	@ApiDocsMethod(requestBean = PageDto.class, baseResponseBean = BaseResponseVo.class, 
	responseBean = DemoVo.class, type = "post", methodExplain = "hell方法")
	@RequestMapping("/helloHtml")
	public String helloHtml(@RequestBody Map<String, Object> map) {
		List<PageDto> pageDtos;

		map.put("hello", "from TemplateController.helloHtml");
		return "/helloHtml";
	}


	...

}

```
**请求或者响应的封装类一定要记得每一个字段都要写备注哦！！！**
``` java
public class DemoVo {

	private String id; // 主键
	private String name; // 姓名
	private Double socre; // 分数
	private CarVo carVo; // 车
	private List<String> urls; // 路径集合

	...

}

```
**执行调用**
``` java
public class DocContext {

    
	public static void main(String[] args) {
		
		ApiDocsUtil.init();

	}
}

```
3.最终输出结果</br>
![image](https://github.com/wangmingchang/AutomateApiDocs/blob/master/src/main/resources/static/word.png)
![image](https://github.com/wangmingchang/AutomateApiDocs/blob/master/src/main/resources/static/html.png)