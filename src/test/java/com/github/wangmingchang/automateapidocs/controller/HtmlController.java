package com.github.wangmingchang.automateapidocs.controller;

import com.github.wangmingchang.automateapidocs.annotation.ApiDocsClass;
import com.github.wangmingchang.automateapidocs.annotation.ApiDocsMethod;
import com.github.wangmingchang.automateapidocs.apidocs.domain.BizResponseDTO;
import com.github.wangmingchang.automateapidocs.apidocs.vo.*;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

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

    /**
     * hell方法
     *
     * @param map 请求参数
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
     * 获取路径
     *
     * @param path url
     * @return 请求的路径
     */
    @ApiDocsMethod(responseBean = BizResponseDTO.class)
    @GetMapping(value = "/getUrl")
    private String getUrl(String path) {
        return path;
    }

    /**
     * 获取老师信息
     *
     * @param id   id信息
     * @param name name信息
     * @return
     * @author wangmingchang
     * @date 2019/1/24 17:05
     **/
    @ApiDocsMethod(responseBean = BaseResponseVo.class)
    @GetMapping(value = "/getTeacharInfo")
    public String getTeacharInfo(@RequestParam String id, @RequestParam String name) {

        return "/helloHtml";
    }

    public static void main(String[] arg){
        BizResponseVo<DemoVo> demoVoBizResponseVo = new BizResponseVo<>();
        demoVoBizResponseVo.setCode("0000");
        demoVoBizResponseVo.setMsg("成功");
        demoVoBizResponseVo.setSysTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        DemoVo demoVo = new DemoVo();
        demoVo.setId("123");
        demoVo.setName("小狗狗");
        CarVo carVo = new CarVo();
        carVo.setCarName("卡丁车");
        carVo.setColor("红色");
        Teachar teachar = new Teachar();
        teachar.setTeacharName("老赵");
        carVo.setTeachar(teachar);
        demoVo.setCarVo(carVo);
        demoVoBizResponseVo.setData(demoVo);
        System.out.println(new Gson().toJson(demoVoBizResponseVo));
    }

}
