package com.wmc.AutomateApiDocs.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wmc.AutomateApiDocs.annotation.ApiDocsClass;
import com.wmc.AutomateApiDocs.annotation.ApiDocsMethod;
import com.wmc.AutomateApiDocs.pojo.vo.BaseResponseVo;
import com.wmc.AutomateApiDocs.pojo.vo.CarVo;
import com.wmc.AutomateApiDocs.pojo.vo.CatVo;
import com.wmc.AutomateApiDocs.pojo.vo.DemoVo;
import com.wmc.AutomateApiDocs.pojo.vo.Fish;
import com.wmc.AutomateApiDocs.pojo.vo.PageDto;

/**
 * HTML-controller
 * 
 * @author 王明昌
 * @date 2017年9月3日
 */
@RestController
@RequestMapping("/api/index/v1")
@ApiDocsClass()
public class HtmlController {
	
	private PageDto PageDto;

	/**
	 * index方法
	 * 
	 * @return
	 */
	@RequestMapping("/")
	@ApiDocsMethod(baseResponseBean=BaseResponseVo.class,methodExplain="index方法",responseBean = DemoVo.class,
	responseBeans= {Fish.class,CatVo.class})
	public String index() {
		return "index";
	}

	/**
	 * hell方法
	 * 
	 * @param map
	 *            请求参数
	 * @return
	 */
	@RequestMapping("/helloHtml")
	@ApiDocsMethod(requestBean = PageDto.class, baseResponseBean=BaseResponseVo.class ,responseBean = DemoVo.class, type = "post",methodExplain="hell方法")
	public String helloHtml(@RequestBody Map<String, Object> map) {
		List<PageDto> pageDtos;
		
		map.put("hello", "from TemplateController.helloHtml");
		return "/helloHtml";
	}
	/**
	 * 获老师信息
	 * 
	 * @param id 主键
	 * @param name 姓名
	 * @return
	 */
	@RequestMapping("/getTeacharInfo")
	@ApiDocsMethod(responseBean = DemoVo.class,methodExplain="获老师信息")
	public String getTeacharInfo(@RequestParam String id, @RequestParam String name) {

		return "/helloHtml";
	}

	
	/**
	 * 返回数据
	 * @return
	 */
	@RequestMapping("/getData")
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
	 * 获取路径
	 * @param path
	 * @return
	 */
	private String getUrl(String path) {
		return path;
	}

}
