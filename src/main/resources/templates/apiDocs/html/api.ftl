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
        .nav-tabs li a, .nav-tabs li span:hover {border-color: #eee #eee #ddd;}
        .panel-child{margin-bottom: 0px; font-size: 12px;}
        .table-child{margin-bottom: 0px;}
        #table-responseClassDtos div{text-align: left;}
        #table-responseClassDtos div table{text-align: left;}
        #request-example{word-wrap: break-word;}
        #response-example{word-wrap: break-word;}
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
<div id="main" style="overflow: hidden;position: relative;">
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
                        <div class="panel-heading" style="height: 55px;">
                            <span style="float: left;line-height: 30px;">请求参数样例</span>
                            <button id="btn-request-example" class="btn btn-default" type="text" style="float: right;" onclick="formatExample(this)">格式化</button>
                        </div>
                        <div id="request-example" class="panel-body">
                            无！
                        </div>
                    </div>
                    <h3>响应结果</h3>
                    <table id="table-responseClassDtos" class="table table-bordered">
                        <thead>
                        <tr>
                            <th width="20%">参数名称</th>
                            <th width="15%">类型</th>
                            <th>描述</th>
                        </tr>
                        </thead>
                        <tbody class="table-responseClassDtos">
                        </tbody>
                    </table>
                    <div class="panel panel-default">
                        <div class="panel-heading" style="height: 55px;">
                            <span style="float: left;line-height: 30px;">响应结果样例</span>
                            <button id="btn-response-example" class="btn btn-default" type="text" style="float: right;" onclick="formatExample(this)">格式化</button>
                        </div>
                        <div id="response-example" class="panel-body">
                            无！
                        </div>
                    </div>

                </div>
            </div>

        </div>
    </div>
    <!--临时数据区-->
    <div id="panel-data-div"></div>
    <div id="child-data-div"></div>
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
                var methodInfoDtoArr = htmlMethonContentDto.methodInfoDtos;
                for (var i = 0; i< methodInfoDtoArr.length; i++){
                    methodInfoDtos.push(methodInfoDtoArr[i]);
                }
            });
        });

        init();
    });


    function init() {
        $(".nav-tabs li").hover(function () {
            $(this).find(".glyphicon-remove").show();
        },function () {
            $(this).find(".glyphicon-remove").hide();
        });
        //点击左侧菜单
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
        //删除标签
        $(".glyphicon-remove").on("click", function () {
            var $parent = $(this).parent()[0];
            var $nextParent = $($parent).next();
            var $prevParent = $($parent).prev();
            var liId = '';
            if($parent.className.indexOf('active') != -1){
                if($nextParent.length > 0){
                    $($nextParent).addClass('active');
                    $($nextParent).data('leftliid');
                    setData(liId);
                    chooseLi(liId);
                }else {
                    if($prevParent.length > 0){
                        $($prevParent).addClass('active');
                        liId = $($prevParent).data('leftliid');
                        setData(liId);
                        chooseLi(liId);
                    }else {
                        removeActiveLi();
                    }
                }
            }
            $($parent).remove();
            if($(".nav-tabs li").length == 0){
                $(".main-panel").hide();
            }else {
                $(".main-panel").show();
            }
        });

    }
    var javaTypes = ["class", "list"];
    //子菜单点击事件
    function listGroupItemChildClick(obj) {
        var liId = obj.id;
        chooseLi(liId);
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
        setData(liId);
    }
    /**
     * 选择左侧子菜单
     * */
    function chooseLi(id) {
        $(".list-group-item-child").each(function () {
            $(this).removeClass("active");
        });

        $("#"+ id).addClass('active');
    }
    function removeActiveLi() {
        $(".list-group-item-child").each(function () {
            $(this).removeClass("active");
        });
    }
    var chidIndex = 0;
    /**
     * 设置数据
     * */
    async function setData(liId) {
        removeData();
        var noExecuteNum = 0;
        for(var i = 0; i < methodInfoDtos.length; i ++){
            var methodInfoDto = methodInfoDtos[i];
            var methodKey = methodInfoDto.methodKey;
            if(methodKey == liId){
                var requestParamDtos = methodInfoDto.requestParamDtos;
                var responseClassDtos = methodInfoDto.baseResponseDataDtos;
                var requestBeanJsonKey = methodInfoDto.requestBeanJsonKey;
                var responseBeanJsonKey = methodInfoDto.responseBeanJsonKey;
                if(responseClassDtos == null || responseClassDtos.length <= 0){
                    responseClassDtos = methodInfoDto.responseClassDtos;
                }
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
                        var required = requestParamDtos[j].required;
                        var description = requestParamDtos[j].description;
                        if(!isNotBank(description)){
                            description = '';
                        }
                        if(required){
                            required = 'Y';
                        }else {
                            required = 'N';
                        }
                        var html_str = '<tr class="'+ className +'"><td >'+requestParamDtos[j].name+'</td><td>'+requestParamDtos[j].type+'</td><td>'+required+'</td><td>'+description+'</td></tr>';
                        $("#table-requestParamDtos tbody").append(html_str);
                    }
                    if(isNotBank(requestBeanJsonKey)){
                        var requestExampleData = await getExampleData(requestBeanJsonKey);
                        if(isNotBank(requestExampleData)){
                            $("#request-example").text(requestExampleData);
                            $("#btn-request-example").show();
                        }
                    }
                }else {
                    $("#table-requestParamDtos tbody").append('<tr><td colspan="4" style="text-align:center">无请求参数！</td></tr>');
                }
                if(responseClassDtos.length > 0){
                    chidIndex = 0;
                    for(var j = 0; j < responseClassDtos.length; j++){
                        var className = 'active';
                        var responseDataDtos = responseClassDtos[j].responseDataDtos;
                        for(var h = 0; h < javaTypes.length; h++){
                            if(javaTypes[h] == responseClassDtos[j].type){
                                executeFlag = false;
                            }
                        }
                        if(j % 2 == 0){
                            className = 'active';
                        }else {
                            className = '';
                        }
                        var name = responseClassDtos[j].name;
                        var description = responseClassDtos[j].description;
                        if(!isNotBank(description)){
                            description = '';
                        }
                        var tableId = "table-responseClassDtos";
                        if(isNotBank(name)){
                            var html_str = '<tr class="'+ className +'"><td >'+name+'</td><td>'+responseClassDtos[j].type+'</td><td>'+description+'</td></tr>';
                            $("#"+tableId+" ." + tableId).append(html_str);
                            if(null != responseDataDtos && responseDataDtos.length > 0){
                                //有子类
                                var childTableId = 'table-child-' + name + "-" + uuid(8, 10);
                                var childDivId = 'div-child-' + name + "-" + uuid(8, 10);
                                var childFlag = "childFlag-" + j;
                                $("#child-data-div").data(childFlag, childTableId);
                                $("#panel-data-div").data(childFlag, childDivId);
                                setResponseDataDtos(responseDataDtos, name, tableId, childTableId,childFlag);
                            }
                        }else {
                            if(null != responseDataDtos && responseDataDtos.length > 0){
                                for (var i = 0; i < responseDataDtos.length; i ++){
                                    var name = responseDataDtos[i].name;
                                    var description = responseDataDtos[i].description;
                                    if(!isNotBank(description)){
                                        description = '';
                                    }
                                    if(i % 2 == 0){
                                        className = 'active';
                                    }else {
                                        className = '';
                                    }
                                    var html_str = '<tr class="'+ className +'"><td >'+name+'</td><td>'+responseDataDtos[i].type+'</td><td>'+description+'</td></tr>';
                                    $("#"+tableId+" ." + tableId).append(html_str);
                                    var childResponseDataDtos = responseDataDtos[i].responseDataDtos;
                                    if(null != childResponseDataDtos && childResponseDataDtos.length > 0){
                                        //有子类
                                        var childTableId = 'table-child-' + name + "-" + uuid(8, 10);
                                        var childDivId = 'div-child-' + name + "-" + uuid(8, 10);
                                        var childFlag = "childFlag-" + j + "-" + i;
                                        $("#child-data-div").data(childFlag, childTableId);
                                        $("#panel-data-div").data(childFlag, childDivId);
                                        setResponseDataDtos(childResponseDataDtos, name, tableId, childTableId,childFlag);
                                    }
                                }
                            }
                        }
                    }
                    if(isNotBank(responseBeanJsonKey)){
                        var responseExampleData = await getExampleData(responseBeanJsonKey);
                        if(isNotBank(responseExampleData)){
                            $("#response-example").text(responseExampleData);
                            $("#btn-response-example").show();
                        }
                    }
                }else {
                    $("#table-responseClassDtos tbody").append('<tr><td colspan="3" style="text-align:center">无响应结果！</td></tr>');
                }
                break;
            }
        }

    }

    /**
     * 清空数据
     */
    async function removeData() {
        $("#methodDescription").text('');
        $("#request-type").text('');
        $("#request-url").text('');
        $("#table-requestParamDtos tbody tr").remove();
        $("#table-responseClassDtos tbody tr").remove();
        $("#child-data-div").remove();
        $("#panel-data-div").remove();
        var html_str = '<div id="panel-data-div"></div> <div id="child-data-div"></div>';
        $("#main").append(html_str);
        $("#request-example").text("无！");
        $("#response-example").text("无！");
        $("#btn-request-example").hide();
        $("#btn-response-example").hide();

    }

    /**
     * 设置子类响应结果
     * @param responseDataDtos
     */
    async function setResponseDataDtos(dataArr, fieldName, parentTableId, childTableId, childFlag) {
        if(dataArr.length > 0){
            var panel_id;
            var panelFlagValue = $("#panel-data-div").data(childFlag);
            var $panelDiv = null;
            if(isNotBank(panelFlagValue)){
                $panelDiv = $("#" + panelFlagValue);
            }
            if(null == $panelDiv || $panelDiv.length <= 0){
                panel_id = 'div-child-' + fieldName + "-" + uuid(8,10);
                $("#panel-data-div").data(childFlag,panel_id);
                var html_str = '<tr><td colspan="3" style="text-align:center"><div id="'+ panel_id +'" class="panel panel-info panel-child">\n' +
                    '                        <div class="panel-heading">'+ fieldName +' 对象的信息</div>\n' +
                    '                        <div class="panel-body">\n' +
                    '                            <table id="'+ childTableId +'" class="table table-bordered table-child">\n' +
                    '                                <thead>\n' +
                    '                                <tr>\n' +
                    '                                    <th width="20%">参数名称</th>\n' +
                    '                                    <th width="15%">类型</th>\n' +
                    '                                    <th>描述</th>\n' +
                    '                                </tr>\n' +
                    '                                </thead>\n' +
                    '                                <tbody class="'+ childTableId+'">\n' +
                    '                                </tbody>\n' +
                    '                            </table>\n' +
                    '                        </div>\n' +
                    '                    </div></td></tr>';
                $("#"+ parentTableId +" ." + parentTableId).append(html_str);
            }
        }
        for(var i = 0; i < dataArr.length; i++){
            var childFlagValue =  $("#child-data-div").data(childFlag);
            if(isNotBank(childFlagValue)){
                childTableId = childFlagValue;
            }
            var className = 'active';
            var responseDataDtos = dataArr[i].responseDataDtos;
            for(var h = 0; h < javaTypes.length; h++){
                if(javaTypes[h] == dataArr[i].type){
                    executeFlag = false;
                }
            }
            if( i % 2 == 0){
                className = 'active';
            }else {
                className = '';
            }
            var html_str = '<tr class="'+ className +'"><td >'+dataArr[i].name+'</td><td>'+dataArr[i].type+'</td><td>'+dataArr[i].description+'</td></tr>';
            $("#"+childTableId+" ." + childTableId).append(html_str);
            if(null != responseDataDtos && responseDataDtos.length > 0){
                var childFlag = childFlag + "-" + i;
                //有子类
                var nextChildId = 'table-child-' + fieldName + "-" + uuid(8, 10);
                var childDivId = 'div-child-' + fieldName + "-" + uuid(8,10);
                $("#child-data-div").data(childFlag, nextChildId);
                $("#panel-data-div").data(childFlag, childDivId);
                setResponseDataDtos(responseDataDtos, dataArr[i].name, childTableId, nextChildId, childFlag);
            }
        }
    }

    //判断值是否不为空
    function isNotBank(nameVal) {
        if(nameVal == null || nameVal == "" || nameVal == undefined){
            return false;
        }
        return true;
    }
    function uuid(len, radix) {
        var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
        var uuid = [], i;
        radix = radix || chars.length;
        if (len) {
            for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
        } else {
            var r;
            uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
            uuid[14] = '4';
            for (i = 0; i < 36; i++) {
                if (!uuid[i]) {
                    r = 0 | Math.random()*16;
                    uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
                }
            }
        }
        return uuid.join('');
    }

    //获取样例数据
    function getExampleData(key) {
        var resultData = '';
        return new Promise(function(resolve) {
            //异步操作
            $.getJSON("apiExampleData.json", function (data) {
                $.each(data, function (i, jsonData) {
                    if(key == jsonData.key){
                        resultData = JSON.stringify(jsonData.data);
                        resolve(resultData);

                    }
                });
            });

        });
    }
    //格式化json数据
    function formatExample(obj) {
        if(obj.id == 'btn-request-example'){
            var $example = $("#request-example pre");
            if(isNotBank($example) && $example.length > 0){
                var jsonStr = $("#request-example pre").text();
                jsonStr = JSON.parse(jsonStr);
                jsonStr = JSON.stringify(jsonStr);
                $("#request-example pre").remove();
                $("#request-example").text(jsonStr);
                $(obj).text("格式化");
            }else {
                var jsonStr = $("#request-example").text();
                jsonStr = JSON.stringify(JSON.parse(jsonStr), null, 4);
                $("#request-example").html("<pre>"+jsonStr+"</pre>");
                $(obj).text("压缩");
            }
        }else {
            var $example = $("#response-example pre");
            if(isNotBank($example) && $example.length > 0){
                var jsonStr = $("#response-example pre").text();
                jsonStr = JSON.parse(jsonStr);
                jsonStr = JSON.stringify(jsonStr);
                $("#response-example pre").remove();
                $("#response-example").text(jsonStr);
                $(obj).text("格式化");
            }else {
                var jsonStr = $("#response-example").text();
                jsonStr = JSON.stringify(JSON.parse(jsonStr), null, 4);
                $("#response-example").html("<pre>"+jsonStr+"</pre>");
                $(obj).text("压缩");
            }
        }
    }
</script>
</html>