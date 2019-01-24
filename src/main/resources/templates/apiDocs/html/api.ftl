<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
    <!-- 最新版本的 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/css/bootstrap.min.css"
          integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="http://libs.baidu.com/jquery/2.1.4/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@3.3.7/dist/js/bootstrap.min.js"
            integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
            crossorigin="anonymous"></script>
    <style type="text/css">
        body{overflow:hidden;}
        .navbar {border-radius: 0px;background-color: #2a2a2f;
            border-color: #2a2a2f;}
        .main-left {margin: 0px;left: 0px;right: auto;top: 0px;bottom: 0px;height: 800px;z-index: 0;width: 318px;display: block;visibility: visible;background: #333;overflow: auto;}

        .main-right {
            position: absolute;
            margin: 0px;
            left: 318px;
            right: 0px;
            top: 0px;
            bottom: 0px;
            height: 890px;
            z-index: 0;
            display: block;
            visibility: visible;
            overflow: auto;
        }

        .list-group-item {
            position: relative;
            display: block;
            padding: 10px 15px;
            margin-bottom: -1px;
            background-color: #333;
            border: 0;
            color: #fff;
        }
        a.list-group-item, button.list-group-item {
            color: #fff;
        }
        .list-group-item.active, .list-group-item.active:focus, .list-group-item.active:hover {background-color: #65606087;border-color: #65606087;}
        .navbar{margin-bottom: 0px;}
        .list-bottom{height: 200px;width: 300px;}
        .list-group-ul{padding: 0px;border: 0px;margin: 0px;}
        .list-group-ul ul{padding: 0; display: none;}
        .list-group-ul ul li{padding: 5px 0px 5px 0px; font-size: 12px;cursor: pointer;}
        .list-group-ul ul li a{color: #fff;text-decoration: none;padding-left: 40px;}
        .list-group-ul ul li:hover{background-color: #afacac1f;border-color: #afacac1f;}
        .list-group-ul ul li.active{background-color: orange;}
        .main-left::-webkit-scrollbar {/*滚动条整体样式*/
            width: 10px;     /*高宽分别对应横竖滚动条的尺寸*/
            height: 1px;
        }
        .main-left::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
            border-radius: 10px;
            -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
            background: #535353;
        }
        .main-left::-webkit-scrollbar-track {/*滚动条里面轨道*/
            -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
            border-radius: 10px;
            background: #EDEDED;
        }
        .main-right::-webkit-scrollbar {/*滚动条整体样式*/
            width: 10px;     /*高宽分别对应横竖滚动条的尺寸*/
            height: 1px;
        }
        .main-right::-webkit-scrollbar-thumb {/*滚动条里面小方块*/
            border-radius: 10px;
            -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
            background: #535353;
        }
        .main-right::-webkit-scrollbar-track {/*滚动条里面轨道*/
            -webkit-box-shadow: inset 0 0 5px rgba(0,0,0,0.2);
            border-radius: 10px;
            background: #EDEDED;
        }
        .list-group-item.active{background-color: #333;border-color: #65606087;}
        a.list-group-item:focus, a.list-group-item:hover, button.list-group-item:focus, button.list-group-item:hover {
            color: #eee;
            text-decoration: none;
            background-color: #a9a5a573;
        }
        .conten-head{height: 10px;width: 100%;}
        .conten-main{padding-left: 10px;}
        .glyphicon-remove{position: relative;float: right;margin-top: -40px;margin-right: 5px;font-size: 10px;cursor: pointer;display: none;}
        .glyphicon-remove:hover{color: orange;}
        .main-panel{margin: 10px;display: none;}
        .data-head{color: #333;background-color: #f5f5f5;    padding: 10px 15px;border: 1px solid #ddd;border-radius: 4px;}
        .nav-tabs li{max-width: 150px;}
        .nav-tabs>li>a{overflow: hidden; color: #333;}
        .nav-tabs li a, .nav-tabs li span:hover {
            border-color: #eee #eee #ddd;
        }

    </style>
</head>
<body>
<!--头部-->
<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="javascript:void(0);">
                <span>ApiDocs</span>
            </a>
        </div>
    </div>
</nav>
<div style="overflow: hidden;position: relative;">
    <!--左边菜单-->
    <div class="main-left" style="">
        <div class="list-group">
            <ul class="list-group-ul">
                <#list htmlMethonContentDtos as  htmlMethonContentDto>
                    <li>
                        <a href="javascript:void(0);" class="list-group-item active">
                            <span class="glyphicon glyphicon-folder-close" aria-hidden="true"></span>
                            <span> ${htmlMethonContentDto.classExplainDto.explain}</span>
                        </a>
                    <ul>
                        <#list htmlMethonContentDto.methodDescriptions as methodDescription>
                            <li id="${methodDescription['methodKey']!''}" onclick="listGroupItemChildClick(this)" class="list-group-item-child"><a href="javascript:void(0);">${methodDescription["methodDescriptionValue"]!''}</a></li>
                        </#list>
                        </ul>
                    </li>

                </#list>
            </ul>
        </div>
        <!--空白内容-->
        <div class="list-bottom"></div>
    </div>
    <!--内容区-->
    <div class="main-right" style="">
        <div class="conten-head"></div>
        <div class="conten-main">
            <ul class="nav nav-tabs">
            </ul>
        </div>
        <div class="main-panel">
            <div class="panel panel-default">
                <div class="panel-body">
                    <h2 id="methodDescription"></h2>
                    <div class="data-head">
                        <div><label>请求方式：</label><span id="request-type"></span></div>
                        <div><label>请求路径：</label><span id="request-url"></span></div>
                    </div>
                    <h3>请求参数</h3>
                    <table id="table-requestParamDtos" class="table table-bordered">
                        <thead>
                        <tr>
                            <th width="20%">参数名称</th>
                            <th width="15%">类型</th>
                            <th width="10%">是否必传</th>
                            <th>描述</th>
                        </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                    <div class="panel panel-default">
                        <div class="panel-heading">请求参数样例</div>
                        <div class="panel-body">
                            无！
                        </div>
                    </div>
                    <h3>响应结果</h3>
                    <table class="table table-bordered">
                        <thead>
                        <tr>
                            <th width="20%">参数名称</th>
                            <th width="15%">类型</th>
                            <th width="10%">是否必传</th>
                            <th>描述</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td colspan="4" style="text-align:center">无响应结果！</td>
                        </tr>
                        </tbody>
                    </table>
                    <div class="panel panel-default">
                        <div class="panel-heading">响应结果样例</div>
                        <div class="panel-body">
                            无！
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</div>

</body>

<script type="text/javascript">
    var methodInfoDtos = []; //方法的数组
    $(function () {
        $(".main-left").height($(document).height()-50);
        //设置左边菜单
        $(".list-group-item").click(function () {
            var $this = this;
            var $glyhicon = $($this).find(".glyphicon")[0];
            if($glyhicon.className.indexOf('glyphicon-folder-close') != -1 ){
                $($glyhicon).removeClass('glyphicon-folder-close');
                $($glyhicon).addClass('glyphicon-folder-open');
                $($this).next().show();
                $($this).addClass('active');
            }else {
                $($glyhicon).removeClass('glyphicon-folder-open');
                $($glyhicon).addClass('glyphicon-folder-close');
                $($this).next().hide();
                $($this).removeClass('active');
            }
        });
        $.getJSON("apiData.json", function (data) {
            $.each(data, function (i, htmlMethonContentDto) {
                console.log(htmlMethonContentDto);
                var methodInfoDtoArr = htmlMethonContentDto.methodInfoDtos;
                console.log(methodInfoDtoArr);
                for (var i = 0; i< methodInfoDtoArr.length; i++){
                    methodInfoDtos.push(methodInfoDtoArr[i]);
                }
                console.log(methodInfoDtos);
            })
        })
        init();
    });

    function init() {
        $(".nav-tabs li").hover(function () {
            $(this).find(".glyphicon-remove").show();
        },function () {
            $(this).find(".glyphicon-remove").hide();
        });
        $(".nav-tabs li").on("click",function () {
            $(".nav-tabs li").each(function () {
                $(this).removeClass('active');
            });
            $(this).addClass('active');
            if($(".nav-tabs li").length == 0){
                $(".main-panel").hide();
            }else {
                $(".main-panel").show();
                var leftLiId = $(this).data('leftliid');
                if(leftLiId != null && leftLiId != ''){
                    listGroupItemChildClick($("#" + leftLiId)[0])
                }
            }
        });

        $(".glyphicon-remove").on("click", function () {
            var $parent = $(this).parent()[0];
            var $nextParent = $($parent).next();
            var $prevParent = $($parent).prev();
            if($parent.className.indexOf('active') != -1){
                if($nextParent.length > 0){
                    $($nextParent).addClass('active');
                }else {
                    if($prevParent.length > 0){
                        $($prevParent).addClass('active');
                    }
                }
            }
            if($(".nav-tabs li").length == 0){
                $(".main-panel").hide();
            }else {
                $(".main-panel").show();
            }
            $($parent).remove();
        });

    }
    var javaTypes = ["class", "list"];
    //子菜单点击事件
    function listGroupItemChildClick(obj) {
        removeData();
        var liId = obj.id;
        $(".list-group-item-child").each(function () {
            $(this).removeClass("active");
        });
        $(obj).addClass('active');
        var existLi = null;
        var textContent = $(obj).find('a').text();
        $(".nav-tabs li").each(function () {
            $(this).removeClass('active');
            var leftLiId = $(this).data('leftliid');
            if(leftLiId ==liId){
                existLi = this;
            }
        });
        if(existLi != null){
            $(existLi).addClass('active');
        }else {
            var htmlStr = '<li data-leftliid="'+ liId +'" role="presentation" class="active" ><a href="javascript:void(0);"><nobr>'+textContent+'</nobr></a><span  class="glyphicon glyphicon-remove" aria-hidden="true"></span></li>';
            $(".nav-tabs").append(htmlStr);
            init();
            $(".main-panel").show();
        }
        //设置数据
        var noExecuteNum = 0;
        for(var i = 0; i < methodInfoDtos.length; i ++){
            var methodInfoDto = methodInfoDtos[i];
            var methodKey = methodInfoDto.methodKey;
            var requestParamDtos = methodInfoDto.requestParamDtos;
            if(methodKey == liId){
                $("#methodDescription").text(methodInfoDto.methodDescription);
                $("#request-type").text(methodInfoDto.type);
                $("#request-url").text(methodInfoDto.url);
                if(requestParamDtos.length > 0){
                    for(var j = 0; j < requestParamDtos.length; j++){
                        var className = 'active';
                        var executeFlag = true;
                        for(var h = 0; h < javaTypes.length; h++){
                            if(javaTypes[h] == requestParamDtos[j].type){
                                executeFlag = false;
                            }
                        }
                        if(!executeFlag){
                            noExecuteNum++;
                            continue;
                        }
                        if((j + noExecuteNum) % 2 == 0){
                            className = 'active';
                        }else {
                            className = '';
                        }
                        var html_str = '<tr class="'+ className +'"><td >'+requestParamDtos[j].name+'</td><td>'+requestParamDtos[j].type+'</td><td>'+requestParamDtos[j].required+'</td><td>'+requestParamDtos[j].description+'</td></tr>';
                        $("#table-requestParamDtos tbody").append(html_str);
                    }

                }else {
                    $("#table-requestParamDtos tbody").append('<tr><td colspan="4" style="text-align:center">无请求参数！</td></tr>');
                }
                break;
            }
        }
    }

    /**
     * 清空数据
     */
    function removeData() {
        $("#methodDescription").text('');
        $("#request-type").text('');
        $("#request-url").text('');
        $("#table-requestParamDtos tbody").remove();
    }

</script>
</html>