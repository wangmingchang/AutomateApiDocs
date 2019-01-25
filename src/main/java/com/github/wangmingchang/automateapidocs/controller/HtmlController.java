package com.github.wangmingchang.automateapidocs.controller;

import com.github.wangmingchang.automateapidocs.annotation.ApiDocsMethod;
import com.github.wangmingchang.automateapidocs.pojo.apidocs.vo.*;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HTML-controller
 * 
 * @author 王明昌
 * @since 2017年9月3日
 */
//@ApiDocsClass
@RestController
@RequestMapping("/api/index/v1")
public class HtmlController {

	private PageDto PageDto;

	/**
	 *  index方法
	 * 
	 * @return 请的路径
	 */
	@ApiDocsMethod(requestBean = Fish.class, baseResponseBean = BizResponseVo.class, methodExplain = "index方法", baseResponseBeanGenericity = DemoVo.class)
	@RequestMapping(value = "/index", method = RequestMethod.POST )
	public String index() {
		try {
			BizResponseVo<DemoVo> demoVoBizResponseVo = new BizResponseVo<>();
			Class<? extends Object> aClass = demoVoBizResponseVo.getClass();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "index";
	}

	/**
	 *  hell方法
	 * 
	 * @param map
	 *            请求参数
	 * @return 请的路径
	 */
	@ApiDocsMethod(requestBean = PageDto.class, baseResponseBean = BaseResponseVo.class, responseBean = DemoVo.class, methodExplain = "hell方法")
	@PostMapping(value = "/helloHtml")
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
	 *  获取路径
	 * 
	 * @param path
	 * @return 请求的路径
	 */
	private String getUrl(String path) {
		return path;
	}

	/**
	 * 获取老师信息
	 * @author wangmingchang
	 * @date 2019/1/24 17:05
	 * @param id id信息
	 * @param name name信息
	 * @return
	 **/
	@ApiDocsMethod(responseBean = BaseResponseVo.class)
	@GetMapping(value = "/getTeacharInfo")
	public String getTeacharInfo(@RequestParam String id, @RequestParam String name) {

		return "/helloHtml";
	}


}
