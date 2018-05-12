<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>儿童转园信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personTransferSchool/savePersonTransferSchool',
				   data: params,
				   dataType:  'json',
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
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/personTransferSchool/savePersonTransferSchool',
				   data: params,
				   dataType:  'json',
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

	function switchPerson(selected){
	   var gradeId = document.getElementById("gradeId").value;
	   if(gradeId != ""){
		   var link = "${contextPath}/heathcare/gradeInfo/personJson?gradeId="+gradeId;
		   //alert(link);
		   jQuery.getJSON(link, function(data){
			  var person = document.getElementById("personId");
			  person.options.length=0;
			  var selectedIndex = 0;
			  
			  jQuery.each(data, function(i, item){
				 if(item.id == selected){
					 selectedIndex = i;
				 }
				 person.options.add(new Option(item.name, item.id));
			  });
             person.selectedIndex = selectedIndex;
			 //alert(selectedIndex);
			});
	   }
	}

	function exportData(){
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=PersonTransferSchool&ts=${ts}&pid=${personTransferSchool.id}";
        window.open(link);
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;" > 
	<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;编辑儿童转园信息</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
	<#if person?exists>
	&nbsp;
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" onclick="javascript:exportData();" >打印</a>
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${personTransferSchool.id}"/>
  <input type="hidden" id="type" name="type" value="${type}">
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">班级</td>
		<td align="left">
             <select id="gradeId" name="gradeId" onchange="javascript:switchPerson('${personTransferSchool.personId}');">
				<option value="">----请选择----</option>
				<#list grades as grade>
			    <option value="${grade.id}">${grade.name}</option>
			    </#list> 
             </select>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">姓名</td>
		<td align="left">
             <select id="personId" name="personId">
				<option value="">----请选择----</option>
				<#list persons as p>
			    <option value="${p.id}">${p.name}</option>
			    </#list> 
             </select>
			 <script type="text/javascript">
			      document.getElementById("personId").value="${person.id}";
			 </script>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">离园日期</td>
		<td align="left">
			<input id="leaveDate" name="leaveDate" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if personTransferSchool.leaveDate?exists>
				   value="${personTransferSchool.leaveDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr>
	<!-- <tr>
		<td width="20%" align="left">体检时间</td>
		<td align="left">
			<input id="checkDate" name="checkDate" type="text" 
			       class="easyui-datebox x-text" style="width:100px"
			       <#if personTransferSchool.checkDate?exists>
				   value="${personTransferSchool.checkDate ? string('yyyy-MM-dd')}"
				   </#if>
				   />
		</td>
	</tr> -->
	<tr>
		<td width="20%" align="left">转入前就读幼儿园</td>
		<td align="left">
            <input id="fromSchool" name="fromSchool" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px"  
				   value="${fromSchool}"/>
			<br>（提示：如果是转入，请填写转入前就读幼儿园）
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">转到幼儿园</td>
		<td align="left">
            <input id="toSchool" name="toSchool" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px"  
				   value="${toSchool}"/>
			<br>（提示：如果是转出，请填写转出幼儿园）
		</td>
	</tr>
	<!-- <tr>
		<td width="20%" align="left">体检机构</td>
		<td align="left">
            <input id="checkOrganization" name="checkOrganization" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px"  
				   value="${personTransferSchool.checkOrganization}"/>
		</td>
	</tr> -->
	<tr>
		<td width="20%" align="left">健康状况</td>
		<td align="left">
            <input id="checkResult" name="checkResult" type="text" 
			       class="easyui-validatebox  x-text" style="width:350px"  
				   value="${personTransferSchool.checkResult}"/>
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" 
			          class="easyui-validatebox  x-text"  
			          style="width:350px;height:120px">${personTransferSchool.remark}</textarea>
			<br><br><br><br>
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>