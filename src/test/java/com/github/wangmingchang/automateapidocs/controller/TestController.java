package com.github.wangmingchang.automateapidocs.controller;

import com.github.wangmingchang.automateapidocs.annotation.ApiDocsMethod;
import com.github.wangmingchang.automateapidocs.apidocs.vo.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试Controller
 * @author 王明昌
 * @since 2017年10月11日
 */
@Controller
@RequestMapping("/test")
//@ApiDocsClass
public class TestController {
	private com.github.wangmingchang.automateapidocs.apidocs.vo.PageDto PageDto;

	/**
	 * index方法test
	 *
	 * @return 请求的路径
	 */
	@ApiDocsMethod(requestBean = Fish.class,baseResponseBean= BaseResponseVo.class,responseBean = DemoVo.class)
	@RequestMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * 获取路径test
	 * @param path
	 * @return 请求的路径
	 */
	private String getUrl(String path) {
		return path;
	}

	/**
	 * hell方法test
	 *
	 * @param map
	 *            请求参数
	 * @return 请求的路径
	 */
	@ApiDocsMethod(requestBean = PageDto.class, baseResponseBean=BaseResponseVo.class ,responseBean = DemoVo.class)
	@RequestMapping("/ht")
	public String helloHtml(@RequestBody Map<String, Object> map) {
		List<PageDto> pageDtos;

		map.put("hello", "from TemplateController.helloHtml");
		return "/helloHtml";
	}

	/**
	 * 返回数据test
	 * @return 返回数据
	 */
	@RequestMapping("/getData3")
	public BaseResponseVo getData(){
		Map<String,Object> map = new HashMap<String,Object>();
		List<CarVo> list = new ArrayList<CarVo>();
		list.add(new CarVo("小咪", 5.0, "橙色", "2.0T", 15.0));

		map.put("pageDto", new PageDto(1,10));
		map.put("list", list);
		BaseResponseVo baseResponseVo = new BaseResponseVo();
		baseResponseVo.setMsg("成功");
		baseResponseVo.setStatus("000");
		baseResponseVo.setResult(map);
		return baseResponseVo;
	}

	/**
	 * 获老师信息test
	 *
	 * @param id 主键
	 * @param name 姓名
	 * @return 请求的路径
	 */
	@ApiDocsMethod(responseBean = DemoVo.class)
	@RequestMapping("/getTeacharInfo2")
	public String getTeacharInfo(@RequestParam String id, @RequestParam String name) {

		return "/helloHtml";
	}



	
	
}
