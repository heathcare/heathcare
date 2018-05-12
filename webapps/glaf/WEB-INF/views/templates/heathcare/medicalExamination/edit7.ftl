<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>体格检查</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

	function saveData(){
		if(jQuery("#personId").val() == ""){
			alert("请选择一个学生。");
			document.getElementById("personId").foucs();
		}
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/medicalExamination/save',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   //alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
						   window.parent.reloadGrid();
						   alert('操作成功完成！');
						   //window.close();
					       //window.parent.location.reload();
						   var index = window.parent.layer.getFrameIndex(window.name);
                           window.parent.layer.close(index);
					   } 
				   }
			 });
	}

	function saveAsData(){
		if(jQuery("#personId").val() == ""){
			alert("请选择一个学生。");
			document.getElementById("personId").foucs();
		}
		document.getElementById("id").value="";
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/medicalExamination/save',
				   data: params,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   
					   }
					   if(data.statusCode == 200){
						   window.parent.reloadGrid();
						   alert('操作成功完成！');
						   //window.close();
					       //window.parent.location.reload();
						   var index = window.parent.layer.getFrameIndex(window.name);
                           window.parent.layer.close(index);
					   }
				   }
			 });
	}

</script>
</head>
<body>
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px"  class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">编辑体格检查</span>
	<#if person?exists && hasWritePermission>
	  <#if person.birthday?exists && person.sex?exists>
	     <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
		    onclick="javascript:saveData();" >保存</a> 
	  <#else>
	    <span style="font:bold 13px 微软雅黑; color:#ff0033;">性别或出生日期未设置，请先设置才能保存体检信息！</span>
	 </#if>
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${medicalExamination.id}"/>
  <input type="hidden" id="checkId" name="checkId" value="${checkId}"/>
  <input type="hidden" id="gradeId" name="gradeId" value="${gradeId}"/>
  <#if person?exists >
  <input type="hidden" id="personId" name="personId" value="${person.id}">
  <#else>
  <input type="hidden" id="personId" name="personId" value="${personId}"/>
  </#if>
  <table style="width:825px" align="center">
    <tbody>
	<#if person?exists >
	<tr>
		<td width="12%" align="left">姓名</td>
		<td width="38%" align="left">${person.name}</td>
		<td width="12%" align="left">类型</td>
		<td width="38%" align="left">
            <input type="hidden" id="type" name="type" value="${type}">
			 专项检查
		</td>
	</tr>
	<#elseif persons?exists >
	<tr>
		<td width="12%" align="left">姓名</td>
		<td width="38%" align="left" > 
            <select id="personId" name="personId">
			    <option value="">----请选择----</option>
				<#list persons as person>
			    <option value="${person.id}">${person.name}</option>
			    </#list> 
			</select>
		</td>
		<td width="12%" align="left">类型</td>
		<td width="38%" align="left">
             <input type="hidden" id="type" name="type" value="${type}">
			 专项检查
		</td>
	</tr>
	</#if>
	<tr>
		<td width="12%" align="left">身高</td>
		<td width="38%" align="left">
			<input id="height" name="height" type="text"
			       class="easyui-numberbox  x-text" precision="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.height}"/>&nbsp;(厘米cm)
		</td>
		<td width="12%" align="left">身高评价</td>
		<td width="38%" align="left">
            <input id="heightEvaluate" name="heightEvaluate" type="text"
			       class="easyui-validatebox  x-readonly" style="width:180px" readonly
				   value="${medicalExamination.heightEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">体重</td>
		<td width="38%" align="left">
			<input id="weight" name="weight" type="text"
			       class="easyui-numberbox x-text" precision="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.weight}"/>&nbsp;(千克kg)
		</td>
		<td width="12%" align="left">体重评价</td>
		<td width="38%" align="left">
            <input id="weightEvaluate" name="weightEvaluate" type="text" 
			       class="easyui-validatebox x-readonly" style="width:180px" readonly
				   value="${medicalExamination.weightEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">BMI</td>
		<td align="left">
			<input id="bmi" name="bmi" type="text"
			       class="easyui-numberbox x-text x-readonly" precision="2" style="width:60px; text-align:right;" readonly
				   value="${medicalExamination.bmi}"/>
		</td>
		<td width="12%" align="left">综合评价</td>
		<td align="left">
			 <input id="bmiEvaluate" name="bmiEvaluate" type="text" 
			        class="easyui-validatebox x-readonly" style="width:180px" readonly
				    value="${medicalExamination.bmiEvaluate}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">左眼</td>
		<td width="38%" align="left">
				<select id="eyeLeft" name="eyeLeft"  onchange="javascript:switchEyeLeft();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="T">沙眼</option>
					<option value="A">弱视</option>
					<option value="O">未检查</option>
				</select>
			    <div id="eyeLeftRemarkDiv" style="display:none; margin-top:2px; ">
				   <input id="eyeLeftRemark" name="eyeLeftRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.eyeLeftRemark}"/>
				</div>
				<script type="text/javascript">
			         document.getElementById("eyeLeft").value="${medicalExamination.eyeLeft}";
					 if(document.getElementById("eyeLeft").value == "N"){
						 jQuery("#eyeLeftRemarkDiv").show();
					 } else {
                         jQuery("#eyeLeftRemarkDiv").hide();
					 }
					 function switchEyeLeft(){
                        if(document.getElementById("eyeLeft").value == "N"){
							jQuery("#eyeLeftRemarkDiv").show();
						} else {
                            jQuery("#eyeLeftRemarkDiv").hide();
						}
					 }
			    </script>
		</td>
		<td width="12%" align="left">右眼</td>
		<td width="38%" align="left">
				<select id="eyeRight" name="eyeRight" onchange="javascript:switchEyeRight();">
					<option value="">----请选择----</option> 
					<option value="Y">未见异常</option>
					<option value="N">异常</option>
					<option value="T">沙眼</option>
					<option value="A">弱视</option>
					<option value="O">未检查</option>
				</select>
			    <div id="eyeRightRemarkDiv" style="display:none; margin-top:2px; ">
				   <input id="eyeRightRemark" name="eyeRightRemark" type="text" 
			              class="easyui-validatebox x-small-text" style="width:102px"
				          value="${medicalExamination.eyeRightRemark}"/>
				</div>
				<script type="text/javascript">
					 document.getElementById("eyeRight").value="${medicalExamination.eyeRight}";
					 if(document.getElementById("eyeRight").value == "N"){
						  jQuery("#eyeRightRemarkDiv").show();
					 } else {
                          jQuery("#eyeRightRemarkDiv").hide();
					 }
					 function switchEyeRight(){
                        if(document.getElementById("eyeRight").value == "N"){
							 jQuery("#eyeRightRemarkDiv").show();
						} else {
                             jQuery("#eyeRightRemarkDiv").hide();
						}
					 }
				</script>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">左视力</td>
		<td width="38%" align="left">
			<input id="eyesightLeft" name="eyesightLeft" type="text"
			       class="easyui-numberbox x-text"  precision="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.eyesightLeft}"/>
		</td>
		<td width="12%" align="left">右视力</td>
		<td width="38%" align="left">
			<input id="eyesightRight" name="eyesightRight" type="text"
			       class="easyui-numberbox x-text"  precision="1" style="width:60px; text-align:right;"
				   value="${medicalExamination.eyesightRight}"/>
		</td>
	</tr>
	<tr>
		<td align="left" colspan="4"><br><br><br><br></td>
	</tr>
    </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>