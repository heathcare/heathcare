<!DOCTYPE html>
<html>
<head>
<title>儿童信息导入</title>
<#include "/inc/init_easyui_import.ftl"/>
<script language="JavaScript">

	function submitRequest(form){
		 if(document.getElementById("file").value==""){
             alert("请选择您导入的儿童基础信息！");
			 return;
		 }
		 if(document.getElementById("joinDate").value==""){
			alert("入园日期不能为空。");
			document.getElementById("joinDate").focus();
			return;
		}
		 if(document.getElementById("tplType").value==""){
             alert("请选择导入模板类型！");
			 return;
		 }
         if(confirm("您确定导入数据到班级，确认吗？")){
			 var tplType = document.getElementById("tplType").value;
			 document.iForm.action="${request.contextPath}/heathcare/person/"+tplType;
             document.iForm.submit();
		 }
	}

</script>
</head>
<body leftmargin="0" topmargin="0" marginheight="0" marginwidth="0">
<br>
<br>
<br>
<center>
<form method="post" enctype="multipart/form-data" name="iForm" class="x-form">
<input type="hidden" id="gradeId" name="gradeId" value="${gradeId}">
<div class="content-block"  style=" width: 80%;" >
<br>
<div class="x_content_title"><img
	src="${request.contextPath}/static/images/window.png" alt="儿童信息导入">&nbsp;儿童信息导入
</div>
 
<div align="center">
<br>
<label for="file">请选择导入的儿童信息，必须是Excel格式（文件扩展名为xls或xlsx）</label>&nbsp;&nbsp;
<br>您可以下载标准模板1<a href="${request.contextPath}/static/templates/person.xls"><span style="color:red;">person.xls</span></a>用于导入数据。
<br>您也可以下载标准模板2<a href="${request.contextPath}/static/templates/person2.xls"><span style="color:red;">person2.xls</span></a>用于导入数据。
<br>
<br>
入园日期&nbsp;<input id="joinDate" name="joinDate" type="text" class="easyui-datebox x-text" style="width:100px;">
(提示：如果Excel中未录入入园日期，可以在这里统一指定。)
<br><br>
请选择导入模板类型：
<select id="tplType" name="tplType">
	<option value="">----请选择----</option>
	<option value="doImport">按第一套模板导入</option>
	<option value="doImport2">按第二套模板导入</option>
</select>
<br><br>
<input type="file" id="file" name="file" size="50" class="input-file"> 
<br>
<div align="center"><br>
<input type="button" name="bt01" value="确定" class="btn btnGray" onclick="javascript:submitRequest(this.form);" /> 
<br>
<br>
</div>
</div>
<br>
<div align="left" style="margin-left:120px; font-size:14px;">
<br>填表说明：
<br>1、请从本表空白行开始添加数据，每一行代表一个儿童。
<br>2、出生日期和入园日期的格式为2018-01-01这样的格式，其他格式不支持。
<br>3、出生日期要和身份证中的年月日保持一致。
<br>4、电话号码是数值类型，并且是合法的手机号码。
<br>5、性别只能是“男”或“女”，其他不能识别。
</div>
<br>
</form>
</center>
</body>
</html>