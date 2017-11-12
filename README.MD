# 简介
这是一个可以自动生成html和word文档格式api的小工具。对写java接口的同行来说是一个不错的选择，而且使用起来非常简单。
# maven地址
```
<dependency>
    <groupId>com.github.wangmingchang</groupId>
    <artifactId>automateapidocs</artifactId>
     <version>1.0.0</version>
</dependency>

```
# 快速使用
1.新增一个名字为apiDocs.properties的文件，或者拷贝git上相同名字文件</br>
2.在要生成的Controller类上打上相应的注解，如下
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
	@ApiDocsMethod(requestBean = PageDto.class, baseResponseBean = BaseResponseVo.class, responseBean = DemoVo.class, type = "post", methodExplain = "hell方法")
	@RequestMapping("/helloHtml")
	public String helloHtml(@RequestBody Map<String, Object> map) {
		List<PageDto> pageDtos;

		map.put("hello", "from TemplateController.helloHtml");
		return "/helloHtml";
	}


	/**
	 * 返回数据
	 * 
	 * @return 返回数据
	 */
	@RequestMapping("/getData")
	public BaseResponseVo getData() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<CarVo> list = new ArrayList<CarVo>();
		list.add(new CarVo("小咪", 5.0, "橙色", "2.0T", 15.0));

		map.put("pageDto", new PageDto(1, 10));
		map.put("list", list);
		BaseResponseVo baseResponseVo = new BaseResponseVo();
		baseResponseVo.setMsg("成功");
		baseResponseVo.setStatus("000");
		baseResponseVo.setResult(map);
		return baseResponseVo;
	}

	/**
	 * 获取路径
	 * 
	 * @param path
	 * @return 请求的路径
	 */
	private String getUrl(String path) {
		return path;
	}
	/**
	 * 获老师信息
	 * 
	 * @param id
	 *            主键
	 * @param name
	 *            姓名
	 * @return 请的路径
	 */
	@ApiDocsMethod(responseBean = DemoVo.class, methodExplain = "获老师信息")
	@RequestMapping("/getTeacharInfo")
	public String getTeacharInfo(@RequestParam String id, @RequestParam String name) {

		return "/helloHtml";
	}


}



```