<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>职工信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

  var contextPath = "${contextPath}";

  <#if privilege_write == true>
	function saveData(){
		if(document.getElementById("name").value==""){
			alert("姓名不能为空。");
			document.getElementById("name").focus();
			return;
		}
		if(document.getElementById("joinDate").value==""){
			alert("入职日期不能为空。");
			document.getElementById("joinDate").focus();
			return;
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/employee/saveEmployee',
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
		if(document.getElementById("name").value==""){
			alert("姓名不能为空。");
			document.getElementById("name").focus();
			return;
		}
		if(document.getElementById("joinDate").value==""){
			alert("入职日期不能为空。");
			document.getElementById("joinDate").focus();
			return;
		}
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/employee/saveEmployee',
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
  </#if>

</script>
</head>
<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑职工信息</span>
	<#if privilege_write == true>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存
	</a> 
	</#if>
    </div> 
  </div>
  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${employee.id}"/>
  <table class="easyui-form" style="width:950px;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">姓名</td>
		<td width="35%" align="left">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:185px;" 
				   value="${employee.name}"/>
		</td>
		<td width="15%" align="left">关联账户</td>
		<td width="35%" align="left">
             <select id="userId" name="userId">
				<option value="">----请选择----</option>
				<#list users as user>
				<option value="${user.actorId}">${user.name}[${user.actorId}]</option>
				</#list>
             </select>
			 <script type="text/javascript">
			      document.getElementById("userId").value="${employee.userId}";
			 </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">身份证编号</td>
		<td width="35%" align="left">
            <input id="idCardNo" name="idCardNo" type="text" precision="0" maxlength="20"
			       class="easyui-validatebox  x-text" style="width:185px;" 
				   value="${employee.idCardNo}"/>
		</td>
		<td width="15%" align="left">工号</td>
		<td width="35%" align="left">
            <input id="employeeID" name="employeeID" type="text" precision="0" maxlength="20"
			       class="easyui-validatebox  x-text" style="width:185px;" 
				   value="${employee.employeeID}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">手机号码</td>
		<td width="35%" align="left">
            <input id="mobile" name="mobile" type="text" 
			       class="easyui-validatebox  x-text" style="width:185px;" 
				   value="${employee.mobile}"/>
		</td>
		<td width="15%" align="left">&nbsp;</td>
		<td width="35%" align="left">&nbsp;
             
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">出生日期</td>
		<td align="left">
			<input id="birthday" name="birthday" type="text" 
			       class="easyui-datebox x-text" style="width:120px;"
				   <#if employee.birthday?if_exists>
				   value="${employee.birthday?string('yyyy-MM-dd')}"
				   </#if>>
   
		</td>
		<td width="15%" align="left">入职日期</td>
		<td width="35%" align="left">
            <input id="joinDate" name="joinDate" type="text" 
			       class="easyui-datebox x-text" style="width:120px;"
				   <#if employee.joinDate?if_exists>
				   value="${employee.joinDate?string('yyyy-MM-dd')}"
				   </#if>>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">性别</td>
		<td align="left">
		  <input type="radio" name="sex" value="0" <#if employee.sex == "0">checked</#if>>女&nbsp;&nbsp;
	      <input type="radio" name="sex" value="1" <#if employee.sex == "1">checked</#if>>男&nbsp;&nbsp;
		</td>
		<td width="15%" align="left">婚否</td>
		<td align="left">
		  <input type="radio" name="marryStatus" value="N" <#if employee.marryStatus == "N">checked</#if>>未婚&nbsp;&nbsp;
	      <input type="radio" name="marryStatus" value="Y" <#if employee.marryStatus == "Y">checked</#if>>已婚&nbsp;&nbsp;
		</td>
	</tr>
    <tr>
		<td width="15%" align="left">民族</td>
		<td align="left">
            <input class="easyui-combobox" id="nation" name="nation" style="width:185px;" value="${employee.nation}"
                   data-options="
                        url: '${contextPath}/dictory/jsonArray?nodeCode=employee_nation',
                        method: 'get',
                        valueField:'value',
                        textField:'value'">
		</td>
		<td width="15%" align="left">职务</td>
		<td align="left">
            <input class="easyui-combobox" id="position" name="position" style="width:185px;" value="${employee.position}"
                   data-options="
                        url: '${contextPath}/dictory/jsonArray?nodeCode=employee_position',
                        method: 'get',
                        valueField:'value',
                        textField:'value'">
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">学历</td>
		<td align="left">
            <input class="easyui-combobox" id="education" name="education" style="width:185px;" value="${employee.education}"
                   data-options="
                        url: '${contextPath}/dictory/jsonArray?nodeCode=employee_education',
                        method: 'get',
                        valueField:'value',
                        textField:'value'">
		</td>
		<td width="15%" align="left">资历</td>
		<td align="left">
             <input class="easyui-combobox" id="seniority" name="seniority" style="width:185px;" value="${employee.seniority}"
                   data-options="
                        url: '${contextPath}/dictory/jsonArray?nodeCode=employee_seniority',
                        method: 'get',
                        valueField:'value',
                        textField:'value'">
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">类型</td>
		<td align="left">
            <input class="easyui-combobox" id="type" name="type" style="width:185px;" value="${employee.type}"
                   data-options="
                        url: '${contextPath}/dictory/jsonArray?nodeCode=employee_type',
                        method: 'get',
                        valueField:'value',
                        textField:'value'">
		</td>
		<td width="15%" align="left">工作性质</td>
		<td align="left">
            <input class="easyui-combobox" id="category" name="category" style="width:185px;" value="${employee.category}"
                   data-options="
                        url: '${contextPath}/dictory/jsonArray?nodeCode=employee_category',
                        method: 'get',
                        valueField:'value',
                        textField:'value'">
		</td>
	</tr>
    <tr>
		<td width="15%" align="left">家庭住址</td>
		<td align="left" colspan="3">
		    <input id="homeAddress" name="homeAddress" type="text" 
			       class="easyui-validatebox  x-text"  style="width:600px;"
				   value="${employee.homeAddress}"/>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">备注</td>
		<td align="left" colspan="3">
		    <textarea id="remark" name="remark" rows="8" cols="50" style="width:600px;height:120px;"  class="easyui-validatebox  x-text" >${employee.remark}</textarea>
		</td>
	</tr>
   </tbody>  
  </table>
 </form>
</div>
</div>
</body>
</html>