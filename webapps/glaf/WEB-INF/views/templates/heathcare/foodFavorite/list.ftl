<!DOCTYPE html>
<html>
<head>
<title>常用食品设置</title>
<#include "/inc/init_easyui_import.ftl"/>

<script language="javascript">

    var contextPath = "${contextPath}";

    function addElement() {
        var list = document.iForm.noselected;
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

	function addAllElement() {
        var list = document.iForm.noselected;
        for (i = 0; i < list.length; i++) {
            var value = list.options[i].value;
            var text = list.options[i].text;
            addToList(value, text);
		    list.remove(i);
		    i=i-1;
        }
    }

    function addToList(value, text) {
        var list = document.iForm.selected;
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
        var list = document.iForm.selected;
		var slist = document.iForm.noselected;
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

	function moveUp() {
        var list = document.iForm.selected;
        if (list.length > 0) {
			var selectedIndex = list.selectedIndex;
			if( selectedIndex > 0 ) {
				var tmpValue = list.options[selectedIndex - 1].value;
				var tmpText = list.options[selectedIndex - 1].text;
				list.options[selectedIndex - 1].value = list.options[selectedIndex].value;
				list.options[selectedIndex - 1].text = list.options[selectedIndex].text;
				list.options[selectedIndex].value = tmpValue;
				list.options[selectedIndex].text = tmpText;
				list.options[selectedIndex - 1].selected = true;
			}
        }
   }

   function moveDown() {
       var list = document.iForm.selected;     
        if (list.length > 0) {
			var selectedIndex = list.selectedIndex;
			if(selectedIndex < (list.length - 1) ) {
				var tmpValue = list.options[selectedIndex].value;
				var tmpText = list.options[selectedIndex].text;
				list.options[selectedIndex].value = list.options[selectedIndex  + 1].value;
				list.options[selectedIndex].text = list.options[selectedIndex  + 1].text;
				list.options[selectedIndex + 1].value = tmpValue;
				list.options[selectedIndex + 1].text = tmpText;
				list.selectedIndex = selectedIndex + 1;
			}
        }
   }

	 function saveData() {
	 
		var len= document.iForm.selected.length;
		var result = "";
		var names = "";
		for (var i=0;i<len;i++) {
		  result = result + document.iForm.selected.options[i].value;
		  names = names + document.iForm.selected.options[i].text;
		  if(i < (len - 1)){
			  result = result + ",";
			  names = names + ",";
		   }
		}
 

		document.getElementById("items").value=result;

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/foodFavorite/saveAll',
				   data: params,
				   dataType: 'json',
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
						  
					   }  
				   }
			 });
	}

    function switchFood(){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   document.iForm.submit();
	   }
   }

</script>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">
<center> <br>
<form id="iForm" name="iForm" class="x-form" action="${contextPath}/heathcare/foodFavorite">
<input type="hidden" id="items" name="items"> 
<div class="content-block" style="width: 785px;"> 
<div class="x_content_title"><img
	src="${contextPath}/static/images/window.png"
	alt="常用食品设置">&nbsp;常用食品设置</div>

 <table class="table-border" align="center" cellpadding="4" cellspacing="1" width="90%">
 <tr>
	<td width="30%">&nbsp;</td>
	<td width="40%">
	  请选择类别&nbsp;<select id="nodeId" name="nodeId" onchange="javascript:switchFood();">
					<option value="0">----请选择----</option>
					<#list foodCategories as cat>
					<option value="${cat.id}">${cat.name}</option>
					</#list> 
				</select>
				<script type="text/javascript">
					document.getElementById("nodeId").value="${nodeId}";
				</script>
	</td>
	<td width="30%">&nbsp;</td>
 </tr>
 </table>
<table class="table-border" align="center" cellpadding="4" cellspacing="1" width="90%">
	 <tbody>		 
		<tr>
			<td colspan="2" width="212">
			 <div align="center">
			   <div align="center">可选食品</div>
			 </div>
			</td>
			<td></td>
			<td colspan="2" width="212">
			  <div align="center">已选食品</div>
			</td>
		</tr>
		<tr>
			<td width="2">&nbsp;</td>
			<td height="26" valign="top" width="182">
			<div align="center">
			    <select class="list" id="noselected" name="noselected" 
				        style="width:350px; height:480px;" multiple="multiple" size="12"
				        ondblclick="javascript:addElement();">${bufferx}
			    </select>
			</div>
			</td>
			<td width="120">
			  <div align="center">
			    <br>
				<input name="add" value="全选>>" onclick="javascript:addAllElement();" 
				       class="btnGray" type="button" style="width:90px;"> 
				<br><br>
				<input name="add" value="添加->" onclick="javascript:addElement();" 
				       class="btnGray" type="button" style="width:90px;"> 
				<br><br>
				<input name="remove" value="<-删除" onclick="javascript:removeElement();"
					   class="btnGray" type="button" style="width:90px;">
				<br><br>
				<input name="up" value="向上移动" onclick="javascript:moveUp();"
					   class="btnGray" type="button" style="width:90px;">
				<br><br>
				<input name="down" value="向下移动" onclick="javascript:moveDown();"
					   class="btnGray" type="button" style="width:90px;">
			  </div>
			</td>
			<td height="26" valign="top" width="212">
			  <div align="center">
			      <select id="selected" name="selected" class="list"
				          style="width:350px; height:480px;" multiple="multiple" size="12"
				          ondblclick="javascript:removeElement();">${buffery}
			      </select>
			  </div>
			</td>
			<td width="23">&nbsp;</td>
		</tr>
	</tbody>
</table>
 
<div align="center">
<br/>
 <c:if save_permission == "true">
   <input value=" 保存 " class=" btnGray " name="button" type="button" onclick="javacsript:saveData();">
 </c:if>
<br/>
</div>

</div>
</form>
</center>

 