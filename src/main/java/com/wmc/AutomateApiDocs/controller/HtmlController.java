package com.wmc.AutomateApiDocs.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wmc.AutomateApiDocs.annotation.ApiDocs;
import com.wmc.AutomateApiDocs.pojo.dto.PageDto;
import com.wmc.AutomateApiDocs.pojo.vo.BaseResponseVo;
import com.wmc.AutomateApiDocs.pojo.vo.DemoVo;

/**
 * HTML-controller
 * 
 * @author 王明昌
 * @date 2017年9月3日
 */
@Controller
@RequestMapping("/api/index/v1")
public class HtmlController {

	private PageDto PageDto;

	/**
	 * index方法
	 * 
	 * @return
	 */
	@RequestMapping("/")
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
	@ApiDocs(requestBean = PageDto.class, responseBean = BaseResponseVo.class, type = "post")
	public String helloHtml(@RequestBody Map<String, Object> map) {

		map.put("hello", "from TemplateController.helloHtml");
		return "/helloHtml";
	}


	/**
	 * 获取路径
	 * @param path
	 * @return
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
	 * @return
	 */
	@RequestMapping("/getTeacharInfo")
	@ApiDocs(responseBean = DemoVo.class)
	public String getTeacharInfo(@RequestParam String id, @RequestParam String name) {

		return "/helloHtml";
	}
}
