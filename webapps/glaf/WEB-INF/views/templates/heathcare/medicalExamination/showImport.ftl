<!DOCTYPE html>
<html>
<head>
<title>儿童体检信息导入</title>
<#include "/inc/init_easyui_import.ftl"/>
<script language="javascript">

	function submitRequest(form){
		if(document.getElementById("file").value==""){
             alert("请选择您导入的儿童体检信息！");
			 return;
		}
		  
		if(document.getElementById("checkDate").value==""){
			alert("体检日期不能为空。");
			document.getElementById("checkDate").focus();
			return;
		}

		if(document.getElementById("tplType").value==""){
             alert("请选择导入模板类型！");
			 return;
		}

        if(confirm("您确定导入体检数据到班级，确认吗？")){
			 var gradeId = document.getElementById("gradeId").value;
			 var tplType = document.getElementById("tplType").value;
			 document.iForm.action="${request.contextPath}/heathcare/medicalExamination/"+tplType+"?type=${type}&gradeId="+gradeId;
             document.iForm.submit();
		}
	}

</script>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">
<center>
<form method="post" enctype="multipart/form-data" name="iForm" class="x-form">
<input type="hidden" id="type" name="type" value="${type}">
<div class="content-block" style=" width: 80%;" >
<br>
<div class="x_content_title"><img
	src="${request.contextPath}/static/images/window.png" alt="儿童体检信息导入">&nbsp;儿童体检信息导入
</div>
 
<div align="center">

<table>
<tr>
	<td colspan="2" height="40">
	  <label for="file">请选择导入的儿童体检信息，必须是Excel格式（文件扩展名为xls）</label>&nbsp;&nbsp;
      <br>您可以下载标准模板1<a href="${request.contextPath}/static/templates/examination1.xls"><span style="color:red;">examination.xls</span></a>用于导入数据。
	</td>
</tr>
<tr>
	<td width="20%" height="60">体检日期</td>
	<td>
	<input id="checkDate" name="checkDate" type="text" class="easyui-datebox x-text" style="width:100px;">
	&nbsp;(提示：如果Excel中未录入体检日期，可以在这里统一指定。)
	</td>
</tr>
<tr>
	<td height="30">导入模板</td>
	<td>
		<select id="tplType" name="tplType">
		  <option value="">----请选择----</option>
		  <option value="doImport">按第一套模板导入</option>
	    </select>
    </td>
</tr>
<tr>
	<td height="30">班级</td>
	<td>
	  <select id="gradeId" name="gradeId">
		<option value="">----请选择----</option>
		<#list gradeInfos as grade>
		<#if grade.deleteFlag == 0>
		<option value="${grade.id}">${grade.name}</option>
		</#if>
		</#list> 
      </select>
	  &nbsp;(提示：如果有重名的情况，可以选择指定班级导入。)
	</td>
</tr>
<tr>
	<td height="30">导入文件</td>
	<td>
	   <input type="file" id="file" name="file" size="50" class="input-file"> 
	</td>
</tr>
<tr>
	<td height="30">&nbsp;</td>
	<td height="30" align="left">
	   <br><input type="button" name="bt01" value="确定" class="btn btnGray" onclick="javascript:submitRequest(this.form);" /> 
    </td>
</tr>
</table>
 
</div>
</div>
<br>
</form>
</center>
</body>
</html>