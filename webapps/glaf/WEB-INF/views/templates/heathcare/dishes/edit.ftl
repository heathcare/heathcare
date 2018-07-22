<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜肴</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
		if(document.getElementById("nodeId").value==""){
			alert("类型不能为空。");
			document.getElementById("nodeId").focus();
			return;
		}

		if(document.getElementById("name").value==""){
			alert("菜肴名称不能为空。");
			document.getElementById("name").focus();
			return;
		}

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dishes/save',
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
					       window.parent.location.reload();
					   } 
				   }
			 });
	}

	function saveAsData(){
		if(document.getElementById("nodeId").value==""){
			alert("类型不能为空。");
			document.getElementById("nodeId").focus();
			return;
		}
		if(document.getElementById("name").value==""){
			alert("菜肴名称不能为空。");
			document.getElementById("name").focus();
			return;
		}
		//document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dishes/save?saveAsFlag=true',
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
					       window.parent.location.reload();
					   }
				   }
			 });
	}

</script>
</head>
<body>
<div style="margin:0px;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:true" style="height:45px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;编辑菜肴</span>
	<#if canSave == true>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a>
	</#if>
	<#if dishes.id?exists>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveAsData();" >另存</a> 
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${dishes.id}"/>
  <input type="hidden" id="dishesId" name="dishesId" value="${dishes.id}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">类别</td>
		<td align="left">
            <select id="nodeId" name="nodeId">
			    <option value="">----请选择----</option>
				<#list categories as category>
				<option value="${category.id}">${category.name}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("nodeId").value="${dishes.nodeId}";
			  </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">名称</td>
		<td align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:385px;"
				   value="${dishes.name}"/>&nbsp;
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">描述</td>
		<td align="left">
		    <textarea  id="description" name="description" rows="6" cols="46" class="x-text" style="height:150px;width:385px;" >${dishes.description}</textarea>
		</td>
	</tr>
 
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>