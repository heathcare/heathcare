<!DOCTYPE html>
<html>
<title>执行处理器</title>
<#include "/inc/init_easyui_import.ftl"/> 
<script language="javascript">

  var contextPath="${request.contextPath}";

  function addElement() {
        var list = document.iForm.noselected1;
        for (i = 0; i < list.length; i++) {
            if (list.options[i].selected) {
                var value = list.options[i].value;
                var text = list.options[i].text;
                addToList(value, text);
				list.remove(i);
				i=i-1;
            }
        }
    }

  function addElement2() {
        var list = document.iForm.noselected2;
        for (i = 0; i < list.length; i++) {
            if (list.options[i].selected) {
                var value = list.options[i].value;
                var text = list.options[i].text;
                addToList2(value, text);
				list.remove(i);
				i=i-1;
            }
        }
    }

  function addToList(value, text) {
        var list = document.iForm.selected1;
        if (list.length > 0) {
            for (k = 0; k < list.length; k++) {
                if (list.options[k].value == value) {
                    return;
                }
            }
        }

        var len = list.options.length;
        list.length = len + 1;
        list.options[len].value = value;
        list.options[len].text = text;
    }

  function addToList2(value, text) {
        var list = document.iForm.selected2;
        if (list.length > 0) {
            for (k = 0; k < list.length; k++) {
                if (list.options[k].value == value) {
                    return;
                }
            }
        }

        var len = list.options.length;
        list.length = len + 1;
        list.options[len].value = value;
        list.options[len].text = text;
    }


  function removeElement() {
        var list = document.iForm.selected1;
		var slist = document.iForm.noselected1;
        if (list.length == 0 || list.selectedIndex < 0 || list.selectedIndex >= list.options.length)
            return;

        for (i = 0; i < list.length; i++) {
            if (list.options[i].selected) {
			    var value = list.options[i].value;
                var text = list.options[i].text;
                list.options[i] = null;
                i--;
				var len = slist.options.length;
				slist.length = len+1;
                slist.options[len].value = value;
                slist.options[len].text = text;				
            }
        }
    }

  function removeElement2() {
        var list = document.iForm.selected2;
		var slist = document.iForm.noselected2;
        if (list.length == 0 || list.selectedIndex < 0 || list.selectedIndex >= list.options.length)
            return;

        for (i = 0; i < list.length; i++) {
            if (list.options[i].selected) {
			    var value = list.options[i].value;
                var text = list.options[i].text;
                list.options[i] = null;
                i--;
				var len = slist.options.length;
				slist.length = len+1;
                slist.options[len].value = value;
                slist.options[len].text = text;				
            }
        }
    }
 

  function doSelection() {

    var len= document.iForm.selected1.length;
	var result = "";
	for (var i=0;i<len;i++) {
      result = result + document.iForm.selected1.options[i].value;
	  if(i < (len - 1)){
		  result = result + ",";
	   }
    }


	if(confirm("确定选择这些处理器吗？")){
		  document.getElementById("previousObjectIds").value=result;
		  //document.getElementById("nextObjectIds").value=result2;
		  var link = "${request.contextPath}/sys/exportPreprocessor/save?expId=${expId}&previousType=${item.type}&nextType=${item.type}";
	      var params = jQuery("#iForm").formSerialize();
		  jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType: 'json',
				   data: params,
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
					       //window.parent.location.reload();
					   }
				   }
			 });
		}

  }

  function switchXY(){
	   var code = document.getElementById("code").value;
       location.href="${request.contextPath}/sys/exportPreprocessor/edit?expId=${expId}&currentStep=${currentStep}&code="+code;
  }
</script>
</head>
<body>
	<center>
		<form id="iForm" name="iForm" class="x-form" method="post" 
		      action="${request.contextPath}/sys/exportPreprocessor/edit?expId=${expId}&currentStep=${currentStep}">
			<input type="hidden" id="expId" name="expId" value="${expId}">
			<input type="hidden" id="currentStep" name="currentStep" value="${currentStep}">
			<input type="hidden" id="previousObjectIds" name="previousObjectIds" >
			<input type="hidden" id="nextObjectIds" name="nextObjectIds">
			<div class="content-block" style="width: 100%;">
				<br>
				<div class="x_content_title">
					<img src="${request.contextPath}/static/images/window.png"
						alt="数据预处理器">&nbsp;数据预处理器
				</div>
				<fieldset class="x-fieldset" style="width: 98%;">
				    <br>
					<select id="code" name="code" onchange="javascript:switchXY();">
						<option value="">----请选择----</option>
						<#list definitions as item>
							<option value="${item.code}">${item.title}</option>
						</#list>
					</select>
					<script type="text/javascript">
						document.getElementById("code").value="${code}";
					</script>
					<table class="table-border" align="center" cellpadding="4" cellspacing="1" width="98%">
						<tbody>
							<tr>
								<td class="beta" colspan="2">
									<div align="center">可选数据处理器</div>
								</td>
								<td class="beta"></td>
								<td class="beta" colspan="3">
									<div align="center">已选数据处理器</div>
								</td>
							</tr>
							<tr>
								<td class="beta" width="18">&nbsp;</td>
								<td class="table-content" height="26" valign="top" width="690">
									<div align="center">
										<select class="list" style="width: 380px; height: 280px;"
											multiple="multiple" size="12" name="noselected1"
											ondblclick="javascript:addElement()">${bufferx}
										</select>
									</div>
								</td>
								<td class="beta" width="124">
									<div align="center" style="margin-right:30px;">
										<button name="add" value="添加->" onclick="javascript:addElement()" class="btn btnGray" type="button">添加</button>
										<br> <br> 
										<button name="remove" value="<-删除" onclick="javascript:removeElement()" class="btn btnGray" type="button">删除</button>
									</div>
								</td>
								<td class="table-content" height="26" valign="top" width="359">
									<div align="center">
										<select class="list" style="width: 380px; height: 280px;"
											multiple="multiple" size="12" name="selected1"
											ondblclick="javascript:removeElement()">${bufferx2}
										</select>
									</div>
								</td>
								<td class="beta" width="23">&nbsp;</td>
							</tr>
						</tbody>
					</table>
					 
				</fieldset>

				<div align="center">
				    <br />
						<button name="remove" value="确 定" style="margin-left: 20px;" onclick="javascript:doSelection()" 
						       class="btn btnGray" type="button">确定</button>
					<br />
					<br />
					<br />
					<br />
					<br />
					<br />
				</div>
			</div>
		</form>
	</center>
 </body>
</html>