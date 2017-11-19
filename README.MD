# 简介
这是一个可以自动生成html和word文档格式api的小工具。对写java接口的同行来说是一个不错的选择，而且使用起来非常简单。
# maven地址
```
<dependency>
    <groupId>com.github.wangmingchang</groupId>
    <artifactId>automateapidocs</artifactId>
     <version>1.1.0</version>
</dependency>

```
# 快速使用
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