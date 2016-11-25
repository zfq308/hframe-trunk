require(['layer','ajax','js/hframework/errormsg'], function () {
    var layer = require('layer');
    var ajax = require('ajax');
    //var flist = require('js/hframework/list');

    //$('form').submit(function(){
    //    return false;
    //});

    //动态刷新的form注解需要捆绑改submit属性，否则就直接提交走了
    $('form').live("submit", function(){
        if(!$(this).attr("action")) {
            return false;
        }
    });

    $("a").click(function(){
        var href = $(this).attr("href");
        if(href.endsWith(".json") || href.endsWith(".html")) {
            $(this).attr("orig-href", $(this).attr("href"));
            $(this).attr("href", $(this).attr("orig-href") + "?" +　getPageContextInfo());
        }

    });

    $(".hfhref").live("click", function(){
        var action =JSON.parse($(this).attr("action"));
        var param  = formatContent($(this).attr("params"), $(this));
        var contextValues = getPageContextInfo();
        if(contextValues) {
            param = contextValues + "&" + param;
        }
        doEvent(action, param, $(this));
    });

    $(".hfselect").live("change", function(){
        if($(this).hasClass("hfselect-init")) {
            $(this).removeClass("hfselect-init");
            return;
        }
        var $action =JSON.parse($(this).attr("action"));
        var $param  = formatContent($(this).attr("params"), $(this));
        var $contextValues = getPageContextInfo();
        if($contextValues) {
            $param = $contextValues + "&" + $param;
        }
        doEvent($action, $param, $(this));
    });

    $('.tree').bind('selected',function (event,data){
        //treeClick(event,data.info[0],$(this));
    });
    $('.tree').bind('closed',function (event,data){
        //treeClick(event,data,$(this));
    });
    $('.tree').bind('opened',function (event,data){
        //treeClick(event,data,$(this));
    });
    $('.tree').bind('clickBtn',function (event,_$btn,data){
        var id = data.additionalParameters.id;
        var $param = _$btn.attr("params");
        var $action = JSON.parse(_$btn.attr("action"));
        if($param.indexOf("{") > 0 && $param.indexOf("}") > 0) {
            $param = $param.replace($param.substring($param.indexOf("{") , $param.indexOf("}") + 1), id);
        }
        //给被刷新容器直接赋值
        if($("div[path][component]").size() > 0) {
            $("div[path][component]").attr("path",id);
        }

        var $contextValues = getPageContextInfo();
        if($contextValues) {
            if($param && "null" != $param) {
                $param = $contextValues + "&" + $param;
            }else {
                $param = $contextValues;
            }
        }
        doEvent($action, $param, $(this))
    });

    //function treeClick(event,selectItemData, $this){
    //    var id = selectItemData.additionalParameters.id;
    //
    //    var $param = $($(".dyn-tree-ele span")[0]).attr("params");
    //    var $action = JSON.parse($($(".dyn-tree-ele span")[0]).attr("action"));
    //    if($param.indexOf("{") > 0 && $param.indexOf("}") > 0) {
    //        $param = $param.replace($param.substring($param.indexOf("{") , $param.indexOf("}") + 1), id);
    //    }
    //
    //    //给被刷新容器直接赋值
    //    if($("div[path][component]").size() > 0) {
    //        $("div[path][component]").attr("path",id);
    //    }
    //
    //    var $contextValues = getPageContextInfo();
    //    if($contextValues) {
    //        $param = $contextValues + "&" + $param;
    //    }
    //    //alert($param);
    //    doEvent($action, $param, $this)
    //
    //}


    function doEvent($action, $param,  $this){
        $type = null;
        for(type in $action) {
            $type = type;
            break;
        }

        var url = $action[$type].action;
        if($type == "confirm") {
            var content = formatContent($action[$type].content,$this);
            showConfirmDialog(content,function(){
                delete $action[$type];
                doEvent($action,$param,$this);
            });
        }else if($type == "alert") {
            var content = formatContent($action[$type].content,$this);
            showConfirmDialog(content,function(){
                delete $action[$type];
                doEvent($action,$param,$this);
            });
        }else if($type == "pageFwd") {
            var isStack =$action[$type].isStack;
            var $componentParam  = formatContent($($this).attr("params"), $($this));
            if($componentParam != null && $componentParam.endsWith("thisForm")) {
                $thisForm = $this.parents("form")[0];
                //参数检查
                if(!$.checkSubmit($thisForm)) {
                    //alert("字段不能为空！");
                    return;
                }
                $($thisForm).attr("action", url + "?" + $param);
                $($thisForm).attr("method", "post");
                $($thisForm).submit();
            }else {
                location.href = url + "?" + $param;
            }




        }else if($type == "ajaxSubmitByJson") {
            var _data;
            var targetId =$action[$type].targetId;

            if(targetId != null) {
                var json = {};
                var targetIds = targetId.split(",");
                for(var tarId in targetIds) {
                    $component = $("[component= " + targetIds[tarId] +"]");
                    if($component.find("form").length > 0) {
                        //参数检查
                        if(!$.checkSubmit($($component.find("form")[0]))) {
                            //alert("字段不能为空！");
                            return;
                        }
                        json[targetIds[tarId]] = JSON.parse($($component.find("form")[0]).serializeJson());
                    }else {
                        var hierarchy = $component.orgchart('getHierarchy');
                        json[targetIds[tarId]]  = JSON.stringify(hierarchy, null, 2);
                    }
                }
                //console.log(JSON.stringify(json));
                _data = JSON.stringify(json);
            }else if($param.endsWith("thisForm")) {
                if($this.parents("form").length == 0) {
                    var $rootNodes = $this.parents("div .hfspan").children(".hfcontainer").children("div").children("div .box");
                    var filePath = $this.parents("div .hfspan").find("div[path]").attr("path");
                    $param = $param + "&path=" + filePath;
                    //alert(filePath);
                    var json = getNodesJson($rootNodes);
                    _data = JSON.stringify(json);
                    //console.log(JSON.stringify(json));
                }else {
                    var $thisForm = $this.parents("form")[0];
                    //参数检查
                    if(!$.checkSubmit($($thisForm))) {
                        //alert("字段不能为空！");
                        return;
                    }
                    _data = $($thisForm).serializeJson();
                }

            }else {
                _data = parseUrlParamToObject($param);
            }
            //_data ='[{"hfpmProgramId":"123","hfpmProgramName":"test","hfpmProgramCode":"234","hfpmProgramDesc":"234","opId":"234","createTime":"2015-10-31 00:20:58","modifyOpId":"","modifyTime":"2015-10-31 00:20:58","delFlag":""},{"hfpmProgramId":"151031375370","hfpmProgramName":"框架","hfpmProgramCode":"hframe","hfpmProgramDesc":"框架","opId":"999","createTime":"2015-10-31 00:20:58","modifyOpId":"999","modifyTime":"2015-10-31 00:20:58","delFlag":"0"}]';
            //console.log(_data);

            $.ajax({
                url: "/" + url  + "?" + $param,
                data: _data,
                type: 'post',
                contentType : 'application/json;charset=utf-8',
                dataType: 'json',
                success: function(data){
                    if(data.resultCode != '0') {
                        alert(data.resultMessage);
                        return;
                    }

                    delete $action[$type];
                    doEvent($action,$param,$this);
                }
            });

        }else if($type == "ajaxSubmit") {
            var _data = {};
            var $componentParam  = formatContent($($this).attr("params"), $($this));

            if(url.endsWith("deleteByAjax.json") && $componentParam != null && $componentParam.endsWith("=")){
                if($($this.parents("tr")[0]).siblings().size()> 0){
                    $this.parents("tr")[0].remove();
                }
                return;
            }


            if($componentParam != null && $componentParam.endsWith("thisForm")) {
                $thisForm = $this.parents("form")[0];
                //参数检查
                if(!$.checkSubmit($thisForm)) {
                    //alert("字段不能为空！");
                    return;
                }
                _data = parseUrlParamToObject(decodeURIComponent($($thisForm).serialize().replace(/\+/g," ")));
            }else {
                if($($this).attr("params") == "checkIds") {
                    var checkIds = new Array();
                    var $thisList = $this.parents(".hflist")[0];
                    var $allChecked = $($thisList).find("input[type=checkbox][name=checkIds]:checked");
                    $allChecked.each(function(){
                        var columnName = $(this).attr("value-key");
                        var columnValue  = formatContent("{" + columnName + "}", $(this));
                        checkIds.push(columnValue);
                    });

                    _data["checkIds"] = checkIds;

                }else {
                    _data = parseUrlParamToObject($param);
                    //版本冲突，不知道为什么加这部分内容， 注掉改部分内容
                    //$thisForm = $this.parents("form")[0];
                    //tmpArray = parseUrlParamToObject($($thisForm).serialize());
                    //for(var $index in tmpArray) {
                    //    _data[$index] = decodeURIComponent(tmpArray[$index]);
                    //}
                }



            }
            console.log(_data);
            //_data = {"hfmdEntityAttrId":"","hfmdEntityAttrName":"1231232132","hfmdEntityAttrCode":"","hfmdEntityAttrDesc":"","attrType":"","size":"","ispk":"","nullable":"","isBusiAttr":"","isRedundantAttr":"","relHfmdEntityAttrId":"","hfmdEnumClassId":"","pri":"","hfpmProgramId":"","hfpmModuleId":"","hfmdEntityId":"","opId":"","createTime":"2015-02-13 12:12:12","modifyOpId":"","modifyTime":"2015-02-13 12:12:12","delFlag":""};
            ajax.Post(url  + "?" + $param,_data,function(data){
                if(data.resultCode != '0') {
                    alert(data.resultMessage);
                    return;
                }

                delete $action[$type];
                if(url.endsWith("deleteByAjax.json") && $this.parents("tr").length> 0){
                    if($($this.parents("tr")[0]).siblings().size()> 0){
                        $this.parents("tr")[0].remove();
                    }
                    return;
                }
                doEvent($action,$param,$this);
            });
        }else if($type == "component.reload") {

            var $curComponent = $this.parents("[component]")[0];

            var targetId = $action[$type].targetId;
            $targetComponent = $this.parents("[component]")[0];
            if(targetId) {
                $targetComponent = $("[component=" + targetId + "]");
            }

            if($($curComponent).hasClass("hftree")) {
                $($targetComponent).attr("param",$param);
            }else {
                if($this.parents("form").size() > 0) {
                    $thisForm = $this.parents("form")[0];
                    $($targetComponent).attr("param",$($thisForm).serialize());
                }
            }
            delete $action[$type];

            if($($targetComponent).hasClass("hflist")) {
                refreshList(1,$targetComponent);
            }else {
                refreshComponent($targetComponent);
            }


        }else if($type == "page.reload") {
            var paramObj = {};
            var $curComponent = $this.parents("[component]")[0];
            if(!$($curComponent).hasClass("hftree")) {
                paramObj = parseUrlParamToObject($param, true);
            }

            var url = location.href;
            //alert($param + " | " + url);
            for(var key in paramObj) {
                if(key != "") {
                    url = changeURLParameterValue(url,key, paramObj[key]);
                }
            }

            delete $action[$type];
            location.href = url;
        }else if($type == "page.reload.static") {
            delete $action[$type];
            location.reload();
        }else if($type == "component.row.add") {
            $curRow = $this.parents("tr")[0];
            $newRow = $($curRow).clone();
            $($newRow).find("input").val("");
            $($newRow).find("[readonly=readonly]").removeAttr("readonly");
            $($curRow).after($newRow);
            $($newRow).find(".hfselectx").each(function(i){
                $(this).next().remove();
                $(this).show();
                $(this).chosen();//设置为selectx
            });
            $($curRow).find(".hfselect").each(function(i){
                var $target = $($newRow).find(".hfselect").eq(i);
                $target.removeClass("city-picker-input");
                $target.next().remove();
                $target.next().remove();
                //$target.citypicker.Constructor

                $.selectPanelLoad($target);;
            });


        }else if($type == "component.row.copy") {
            $curRow = $this.parents("tr")[0];
            $newRow = $($curRow).clone();
            $($newRow).find("input[type=hidden]").val("");
            $($curRow).find("select").each(function(i){
                $($newRow).find("select").eq(i).val($(this).val());
            });
            $($curRow).after($newRow);
            $($newRow).find(".hfselectx").each(function(i){
                $(this).next().remove();
                $(this).show();
                $(this).chosen();//设置为selectx
            });

            $($curRow).find(".hfselect").each(function(i){
                var $target = $($newRow).find(".hfselect").eq(i);
                $target.removeClass("city-picker-input");
                $target.next().remove();
                $target.next().remove();
                //$target.citypicker.Constructor

                $.selectPanelLoad($target);;
            });

        }else if($type == "component.row.up") {
            $curRow = $this.parents("tr")[0];
            $targetRow = $($curRow).prev();
            $newRow = $($targetRow).clone();
            $($targetRow).find("select").each(function(i){
                $($newRow).find("select").eq(i).val($(this).val());
            });
            $($curRow).after($newRow);
            $targetRow.remove();

        }else if($type == "component.row.down") {
            $curRow = $this.parents("tr")[0];
            $targetRow = $($curRow).next();
            $newRow = $($targetRow).clone();
            $($targetRow).find("select").each(function(i){
                $($newRow).find("select").eq(i).val($(this).val());
            });
            $($curRow).before($newRow);
            $targetRow.remove();
        }else if($type == "component.row.remove") {
            $curRow = $this.parents("tr")[0];
        }else if($type == "dialog") {
            showDialog(url + "?" + "isPop=true&" +$param,function(){
                delete $action[$type];
                doEvent($action,$param,$this);
            });
        }
        //delete $action[$type];
    }

    function getNodesJson($rootNodes){
        var json = {};
        $($rootNodes).each(function(){
            var componentId = $(this).attr("component");
            var id = $(this).parent("div[dc]").attr("dc");
            if(componentId != null) {
                if(componentId =="mutexContainer") {
                    var data = [];
                    var $subInst = $(this).children(".box-content").children(".tab-content").children(".tab-pane").children("div .hfcontainer");
                    $($subInst).each(function(){
                        var $subNodes = $(this).children("div").children("div .box");
                        var subJson = getNodesJson($subNodes);
                        data.push(subJson);
                    });
                    json[id] =data;
                }else {
                    json[id] = $(this).find("form").serializeJson();
                    //console.info($(this).find("form").serializeJson());
                }
            }
        });

        return json;
    }

    function changeURLParameterValue(destiny, par, par_value)
    {
        var pattern = par+'=([^&]*)';
        var replaceText = par+'='+par_value;
        if (destiny.match(pattern))
        {
            var tmp = '/\\'+par+'=[^&]*/';
            tmp = destiny.replace(eval(tmp), replaceText);
            return (tmp);
        }
        else
        {
            if (destiny.match('[\?]'))
            {
                return destiny+'&'+ replaceText;
            }
            else
            {
                return destiny+'?'+replaceText;
            }
        }
        return destiny+'\n'+par+'\n'+par_value;
    }

    var refreshList = function(pageNo, compoContainer){
        var module = $(compoContainer).attr("module");
        var page =$(compoContainer).attr("page");
        var component  =$(compoContainer).attr("component");
        var param  =$(compoContainer).attr("param");
        var _url =  "/" + module + "/" + page + ".html";
        var _data = {"pageNo":pageNo,"component" : component};
        if(param) {
            console.log("{\"" + (param + "&1=1").replace(new RegExp("=","gm"),"\":").replace(new RegExp("&","gm"),",\"").replace(new RegExp(":,","gm"),":null,")  + "}");
            var params =JSON.parse("{\"" + (param + "&1=1").replace(new RegExp("=","gm"),"\":\"").replace(new RegExp("&","gm"),"\",\"").replace(new RegExp(":,","gm"),"\":null,")  + "\"}")
            //var params =JSON.parse("{\"" + (param + "&1=1").replace(new RegExp("=","gm"),"\":").replace(new RegExp("&","gm"),",\"").replace(new RegExp(":,","gm"),":null,")  + "}");
            for (var key in params) {
                _data[key]=decodeURI(params[key]).trim();
            }
        }
        console.log(_data);
        //alert(_data);
        ajax.Post(_url,_data,function(data){
            var $newHfList = $(data);
            $(compoContainer).find(".hflist-pager").html($newHfList.find(".hflist-pager").html());
            $(compoContainer).find(".hflist-data").html($newHfList.find(".hflist-data").html());
            componentinit();
            $.reloadListDisplay();
        },'html');
    }

    var refreshComponent = function(compoContainer){
        var module = $(compoContainer).attr("module");
        var page =$(compoContainer).attr("page");
        var component  =$(compoContainer).attr("component");
        var param  =$(compoContainer).attr("param");
        var _url =  "/" + module + "/" + page + ".html";
        var _data = {"component" : component};
        if(param) {
            console.log("{\"" + (param + "&1=1").replace(new RegExp("=","gm"),"\":").replace(new RegExp("&","gm"),",\"").replace(new RegExp(":,","gm"),":null,")  + "}");
            var params =JSON.parse("{\"" + (param + "&1=1").replace(new RegExp("=","gm"),"\":\"").replace(new RegExp("&","gm"),"\",\"").replace(new RegExp(":,","gm"),"\":null,")  + "\"}")
            //var params =JSON.parse("{\"" + (param + "&1=1").replace(new RegExp("=","gm"),"\":").replace(new RegExp("&","gm"),",\"").replace(new RegExp(":,","gm"),":null,")  + "}");
            for (var key in params) {
                _data[key]=decodeURI(params[key]).trim();
            }
        }
        console.log(_data);
        //alert(_data);
        ajax.Post(_url,_data,function(data){
            var $newComponent = $(data);

            var $targetCoponent = $newComponent.find(".hfcontainer[component=container]");
            if($targetCoponent != null && $targetCoponent.size() > 0) {//表明为容器
                $(compoContainer).html($targetCoponent.html());
                componentinit();
                $.reloadDisplay(compoContainer);
            }else {//表明为普通组件
                $(compoContainer).find(".box-content").html($newComponent.find(".box-content").html());
                $.reloadDisplay(compoContainer.find(".box-content"));
                var $groupElement = compoContainer.parents("[group][group !='']:first");
                if($groupElement) {
                    var groupName = $groupElement.attr("group");
                    $("[group][group='" + groupName + "']").each(function(index, element){
                       if($(element) != $groupElement) {
                           $(element).hide();
                       }
                    });
                    $groupElement.show();
                }
            }

        },'html');
    }



    function parseUrlParamToJson($paramStr){
        return JSON.stringify(parseUrlParamToObject($paramStr));
    }

    function parseUrlParamToObject($paramStr, $containBlank){
        console.info($paramStr);
        var result = {};
        if(!$paramStr) {
            return result;
        }
        $params = $paramStr.split("&");
        for($index in $params) {
            var key = $params[$index].substring(0, $params[$index].indexOf("="));
            var value = $params[$index].substring($params[$index].indexOf("=") + 1);
            if(value != '' || $containBlank) {
                //if(result[key] != null) {
                //    if(result[key] instanceof Array) {
                //        result[key].push(value);
                //    }else {
                //        result[key] = [result[key],value];
                //    }
                //}else {
                if(key == "createTime" || key == "modifyTime") {
                    continue;
                }
                result[key] = value;
                //}
            }
        }
        return result;
    }

    function formatContent($param, $this){
        if($param) {
            var value;
            var position = $param.substring($param.indexOf("{") + 1, $param.indexOf("}"));
            if($this.parents("tr").find("span[code="+ position +"]").size() > 0 ) {
                value = $this.parents("tr").find("span[code="+ position +"]").text();
            }else {
                value = $this.parents("form").find("[name="+ position +"]").val();
            }
            return $param.replace("{" + position +"}",value);
        }
        return null;
    }

    function getPageContextInfo(){
        $pageContextValues = $($("#breadcrumb").find("form")[0]).serialize();
        return $pageContextValues;
    }


    function showDialog(url, ok){
        layer.open({
            area: ['766px', '510px'],
            type: 2,
            fix: false, //不固定
            maxmin: true,
            //closeBtn: 1,
            content: url,
            success: function (l, i){
                l.find(".btn").on('click', function(){
                    layer.closeAll();
                    ok();
                    return true;
                });
                l.find('.hfconfirm-btn-cancel').on('click', function(){
                    layer.closeAll();
                    return false;
                    //location.reload();
                });
            }
        });
    }

    function showConfirmDialog(msg, ok){
        var _tpl = $('#myModal').html();
        layer.open({
            area: ['510px', '170px'],
            type: 1,
            title: false,
            closeBtn: 0,
            content: _tpl,
            success: function (l, i){
                $('.hfconfirm-content').html(msg);
                $('.hfconfirm-btn-ok').on('click', function(){
                    layer.closeAll();
                    ok();
                    return true;
                });
                $('.hfconfirm-btn-cancel').on('click', function(){
                    layer.closeAll();
                    return false;
                    //location.reload();
                });
            }
        });
    }

    (function($){
        $.fn.serializeJson = function(){
            var jsonData1 = {};
            var serializeArray = this.serializeArray();
            // 先转换成{"id": ["12","14"], "name": ["aaa","bbb"], "pwd":["pwd1","pwd2"]}这种形式
            $(serializeArray).each(function () {
                if (jsonData1[this.name] != null) {
                    if ($.isArray(jsonData1[this.name])) {
                        jsonData1[this.name].push(this.value);
                    } else {
                        jsonData1[this.name] = [jsonData1[this.name], this.value];
                    }
                } else {
                    jsonData1[this.name] = this.value;
                }
            });
            // 再转成[{"id": "12", "name": "aaa", "pwd":"pwd1"},{"id": "14", "name": "bb", "pwd":"pwd2"}]的形式
            var vCount = 0;
            // 计算json内部的数组最大长度
            for(var item in jsonData1){
                var tmp = $.isArray(jsonData1[item]) ? jsonData1[item].length : 1;
                vCount = (tmp > vCount) ? tmp : vCount;
            }

            if(vCount > 1) {
                var jsonData2 = new Array();
                for(var i = 0; i < vCount; i++){
                    var jsonObj = {};
                    for(var item in jsonData1) {
                        jsonObj[item] = jsonData1[item][i];
                    }
                    jsonData2.push(jsonObj);
                }
                return JSON.stringify(jsonData2);
            }else{
                return "[" + JSON.stringify(jsonData1) + "]";
            }
        };
    })(jQuery);
});
