<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱模板</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.table-border { background-color:#0099CC; height: 32px; }
.table-content { background-color:#ffffff; height: 32px; font-size: 16px; font-family:"微软雅黑"}
.table-content2 { background-color:#ffffff; height: 32px; font-size: 14px; font-family:"微软雅黑"}

.x_y_title {
	text-transform: uppercase;
	background-color: inherit;
	color: #000033;
	font-size: 16px;
	font-weight: bold;
	text-align: center;
}

.dietary_title {
	height: 20px;
	line-height: 20px;
	text-align: center;
	font: bold 16px 微软雅黑;
	color: #FF6600;
	cursor: pointer;
}

.dietary_item {
	height: 20px;
	line-height: 20px;
	text-align: center;
	font: bold 15px 微软雅黑;
	color: #0099CC;
}

.xz_input {
    background-color: #fff;
	border: 1px solid #fff;
	font: bold 15px 微软雅黑;
	color: #FF6600;
	padding: 2px 2px;
	line-height: 22px;
	width: 35px;
	height: 22px;
	font-size: 15px;
	text-align: right;
}

.xz_input:hover {
	color: rgb(255, 0, 0);
	font-weight: bold;
	box-shadow: 1px 1px 1px 1px #aaa;
	background-color: #ffff99;
	font-size: 15px;
	-moz-box-shadow: 0 1px 1px #aaa;
	-webkit-box-shadow: 0 1px 1px #aaa;
}

</style>
<script type="text/javascript">

	function doExport(){
		if(document.getElementById("suitNo").value==""){
			alert("请选择模板。");
			document.getElementById("suitNo").focus();
			return;
		}
		var sysFlag = document.getElementById("sysFlag").value;
		//var year = document.getElementById("year").value;
		var suitNo = document.getElementById("suitNo").value;
		var link = '${contextPath}/heathcare/dietaryTemplateExport/export?exportType=xls&suitNo='+suitNo;
		    link = link+'&sysFlag='+sysFlag;
		window.open(link); 
	}

	function doSubmit(){
		document.iForm.action="${contextPath}/heathcare/dietaryTemplateExport/showExport";
        document.iForm.submit();
	}

	function doCopyX(){
		var suitNo = document.getElementById("suitNo").value;
		if(confirm("模板复制后不能删除,确定要复制第"+suitNo+"作为新模板吗?")){
			var sysFlag = document.getElementById("sysFlag").value;
            var link = '${contextPath}/heathcare/dietaryTemplateExport/copyTemplates?suitNo='+suitNo;
		    link = link+'&sysFlag='+sysFlag;
		    location.href=link; 
		}
	}

	function batchAdd(){
		var suitNo = document.getElementById("suitNo").value;
		var sysFlag = document.getElementById("sysFlag").value;
		var link="${contextPath}/heathcare/dietaryTemplate/batchAdd?suitNo="+suitNo+"&sysFlag="+sysFlag;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "批量增加",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px', ''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function execute(){
		var form = document.getElementById("iForm");
	    var link = "${contextPath}/heathcare/dietaryStatistics/execute";
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
					   window.location.reload();
				   }
			 });
	}

	function modifyItem(id){
		var quantity = jQuery("#item_"+id).val();
		//alert(id + "=" + quantity);
		var link="${contextPath}/heathcare/dietaryItem/updateTemplateQuantity?id="+id+"&quantity="+quantity;
        jQuery.ajax({
				   type: "POST",
				   url: link,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					  
				   }
			 });
	}

    function editItems(id) {
		var link = "${contextPath}/heathcare/dietaryItem?templateId="+id;
		jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "食物构成",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['880px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
		});
	}

  
 	function exportNutrition(){
        var sysFlag = jQuery("#sysFlag").val();
        var suitNo = jQuery("#suitNo").val();
		if(suitNo == ""){
            alert('请选择模板序号！');
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=WeeklyDietaryTemplateNutritionCount";
		if(sysFlag != ""){
			link = link + "&sysFlag=" + sysFlag;
		}
		if(suitNo != ""){
			link = link  + "&suitNo="+suitNo;
		}
        window.open(link);
	}

	function showList(){
		var sysFlag = jQuery("#sysFlag").val();
        var suitNo = jQuery("#suitNo").val();
		if(suitNo == ""){
            alert('请选择模板序号！');
			return;
		}
        var link="${contextPath}/heathcare/dietaryTemplate?q=1";
		if(sysFlag != ""){
			link = link + "&sysFlag=" + sysFlag;
		}
		if(suitNo != ""){
			link = link  + "&suitNo="+suitNo;
		}
		window.open(link);
	}

</script>
</head>
<body style="margin:5px;"> 
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north', split:false, border:false" style="height:48px"> 
    <div class="toolbar-backgroud">
	  <form id="iForm" name="iForm" method="post">
	   <table width="99%" align="left">
		<tbody>
		 <tr>
		    <td width="8%" align="left">
			<img src="${contextPath}/static/images/window.png"><span class="x_content_title">&nbsp;食谱模板</span>
			</td>
			<td width="25%" align="left">
			  &nbsp;类型&nbsp;
			  <select id="sysFlag" name="sysFlag" onchange="javascript:doSubmit();">
				<option value="">----请选择----</option> 
				<option value="N">我自己的</option>
				<option value="Y">系统内置</option>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("sysFlag").value="${sysFlag}";
			  </script>
			  &nbsp;序号&nbsp;
			  <select id="suitNo" name="suitNo" onchange="javascript:doSubmit();">
				<#list categories as category>
				<option value="${category.suitNo}">${category.name}</option>
				</#list> 
			  </select>
			  <script type="text/javascript">
				   document.getElementById("suitNo").value="${suitNo}";
			  </script>
			</td>
			<td width="10%"><span style="font-size: 13px;font-weight: bold;color: #ff0033">${copy_msg}</span></td>
			<td width="45%" >
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-ok'" 
	             onclick="javascript:doSubmit();" >确定</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'" 
	             onclick="javascript:showList();" >调整</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
	             onclick="javascript:doExport();" >导出</a>
			   &nbsp;
			  <#if dietary_copy_add_perm == true>
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	             onclick="javascript:doCopyX();" >复制</a>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
	             onclick="javascript:batchAdd();" >批量加入</a>
			  </#if>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
	             onclick="javascript:exportNutrition();" >营养成分统计表</a>
			  <#if dietary_execute_perm == true>
			  &nbsp;
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-formula'" 
	             onclick="javascript:execute();" >统计成分</a>
			  </#if>
			</td>
		</tr>
	   </tbody>
	  </table>
	 </form>
    </div> 
  </div>
  <div data-options="region:'center',border:false,cache:true">
  <#if weekList?exists>
    <table width="99%" height="98%" cellpadding='1' cellspacing='2' class="table-border" nowrap>
	  <tr>
	    <td colspan="8" align="center"  class="table-content">
		   <table border='0' cellpadding='0' cellspacing='0'  width="99%">
		    <tr>
		     <td width="70%" align="center">
		       <span class="x_y_title">  第 ${suitNo} 套  帯  量  食  谱  模  板 </span>
		     </td>
		     <td width="30%" align="right">
		       <span>&nbsp;配餐均龄：4岁 &nbsp;一人可食均量：克&nbsp;</span>
		     </td>
		     </tr>
		   </table>
		</td>
	  </tr>
	  <tr>
		  <td width="4%" class="table-content"  align="center" >
		     餐<br>别
		  </td>
		  <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border">
			  <tr>
				<td colspan="3" align="center" class="table-content">
				${wkdata.weekName} <#if wkdata.dateName?exists></#if>
				</td>
			  </tr>
			  <tr>
				<td width="40%" align="center" class="table-content">食谱</td>
				<td width="45%" align="center" class="table-content">食物</td>
				<td width="15%" align="center" class="table-content">重量</td>
			  </tr>
			  </table>
		  </td>
		  </#list>
        </tr>
	    <tr>
		  <td width="4%" class="table-content" align="center">
		  早<br>餐
		  </td>
		  <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.breakfastList as r1>
			  <tr>
				<td valign="top" width="40%" height="18">
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
                </td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15"><span class="dietary_item">${item.name}</span></td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="3" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
        </tr>

	    <tr>
		  <td class="table-content" align="center">
		  早<br>点
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.breakfastMidList as r1>
			  <tr>
				<td valign="top" width="40%" height="18">
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15"><span class="dietary_item">${item.name}</span></td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="3" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
        </tr>

	    <tr>
		  <td class="table-content" align="center">
		   午<br>餐
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.lunchList as r1>
			  <tr>
				<td valign="top" width="40%" height="18">
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15"><span class="dietary_item">${item.name}</span></td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="3" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
        </tr>

	    <tr>
		  <td class="table-content" align="center">
		   午<br>点
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.snackList as r1>
			  <tr>
				<td valign="top" width="40%" height="18"> 
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15"><span class="dietary_item">${item.name}</span></td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="3" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
        </tr>

	    <tr>
		  <td class="table-content" align="center">
		   晚<br>餐
		  </td>
          <#list weekList as wkdata>
		  <td width="18%" class="table-content">
			  <table border='0' cellpadding='0' cellspacing='0' width="98%" height="98%"  >
			  <#list wkdata.dinnerList as r1>
			  <tr>
				<td valign="top" width="40%" height="18">
				     <span class="dietary_title" 
					       onclick="javascript:editItems('${r1.dietaryTemplate.id}');">${r1.name}</span>&nbsp;
				</td>
				<td valign="top" width="60%">
					<table>
					<#list r1.items as item>
					<tr>
					  <td align="left" width="90%" height="15"><span class="dietary_item">${item.name}</span></td>
					  <td align="right" width="10%">
					   <#if item.name?exists>
					    <input type="text" id="item_${item.id}" name="myInput" value="${item.quantity2}"
						       onchange="javascript:modifyItem('${item.id}');" size="3" class="xz_input">
					   </#if>
					  </td>
					</tr>
					</#list>
					</table>
				</td>
			  </tr>
			  </#list>
			  </table>
		  </td>
		  </#list>
        </tr>
	 </#if>

     <#if dietary_nutrient == true>
	    <tr>
		  <td class="table-content2">
		   <table border='0' cellpadding='0' cellspacing='0' width="100%" height="100%" class="table-border">
		   <tr>
			<td rowspan="5" class="table-content2" height="60" style="width:60px;" align="center">
			一<br>周<br>日<br>均<br>营<br>养<br>分<br>析<br>
			</td>
			<td height="60">
             <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border">
             <tr>
				<td class="table-content2" height="15" style="width:60px;" valign="top">营养</td>
             </tr>
             <tr>
				<td class="table-content2" height="15" valign="top">标准</td>
             </tr>
             <tr>
				<td class="table-content2" height="15" valign="top">实际</td>
             </tr>
             <tr>
				<td class="table-content2" height="15" valign="top">实给</td>
             </tr>
			 <tr>
				<td class="table-content2" height="15" valign="top">评价</td>
             </tr>
             </table>
			</td>
		   </tr>
		   </table>
		  </td>
		  <td width="18%" class="table-content2">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">总热能(kcal)</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">碳水热</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">脂肪热</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_foodDRI.heatEnergy}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">50-65%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">20-35%</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekAgv.heatEnergy}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.heatEnergyCarbohydrate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.heatEnergyFat}</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.heatEnergyPercent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.heatEnergyCarbohydratePercent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.heatEnergyFatPercent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.heatEnergyEvaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.heatEnergyCarbohydrateEvaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.heatEnergyFatEvaluate}</td>
			  </tr>
			 </table>
		  </td>
		  <td width="18%" class="table-content2">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">蛋白质总量(g)</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">动物类</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">动豆类</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_foodDRI.protein}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">30%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">50%</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekAgv.protein}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.proteinAnimal}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.proteinAnimalBeans}</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.proteinPercent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.proteinAnimalPercent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.proteinAnimalBeansPercent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.proteinEvaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.proteinAnimalEvaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.proteinAnimalBeansEvaluate}</td>
			  </tr>
			 </table>
		  </td>
		  <td width="18%" class="table-content2">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">V-A(ug)</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">V-B1(mg)</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">V-B2(mg)</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_foodDRI.vitaminA}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_foodDRI.vitaminB1}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_foodDRI.vitaminB2}</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekAgv.vitaminA}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekAgv.vitaminB1}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekAgv.vitaminB2}</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.vitaminAPercent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.vitaminB1Percent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.vitaminB2Percent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.vitaminAEvaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.vitaminB1Evaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.vitaminB2Evaluate}</td>
			  </tr>
			 </table>
		  </td>
		  <td width="18%" class="table-content2">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">V-C(mg)</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">尼克酸(mg)</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">钙(mg)</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_foodDRI.vitaminC}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_foodDRI.nicotinicCid}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_foodDRI.calcium}</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekAgv.vitaminC}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekAgv.nicotinicCid}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekAgv.calcium}</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.vitaminCPercent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.nicotinicCidPercent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.calciumPercent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.vitaminCEvaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.nicotinicCidEvaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.calciumEvaluate}</td>
			  </tr>
			 </table>
		  </td>
		  <td width="18%" class="table-content2">
			 <table border='0' cellpadding='0' cellspacing='1' width="100%" height="100%" class="table-border" align="center">
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">铁(mg)</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">锌(mg)</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">磷(mg)</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_foodDRI.iron}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_foodDRI.zinc}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_foodDRI.phosphorus}</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekAgv.iron}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekAgv.zinc}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekAgv.phosphorus}</td>
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.ironPercent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.zincPercent}%</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.phosphorusPercent}%</td>	
			  </tr>
			  <tr>
				<td valign="top" height="15" width="40%" class="table-content2" align="center">${cnt_weekRptModel.ironEvaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.zincEvaluate}</td>
				<td valign="top" height="15" width="30%" class="table-content2" align="center">${cnt_weekRptModel.phosphorusEvaluate}</td>
			  </tr>
			 </table>
		  </td>
        </tr>
        </#if>
     <!-- <tr>
	    <td colspan="6" align="right"  class="table-content" height="15">
		  &nbsp;
		</td>
	  </tr> -->
    </table>
   <br>&nbsp;
   <br>&nbsp;
   <table width="98%" height="98%" border="0">
   <tr>
	<td width="50%">
	    <iframe id="x1" name="x1" style="width:380px; height:380px" frameborder="0" ></iframe>
	</td>
	<td width="50%">
	    <iframe id="x2" name="x2" style="width:380px; height:380px" frameborder="0"></iframe>
	</td>
   </tr>
   <tr>
	<td width="50%">
	    <iframe id="x3" name="x3" style="width:380px; height:380px" frameborder="0" ></iframe>
	</td>
	<td width="50%">
	    <iframe id="x4" name="x4" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
   </tr>
   <tr>
	<td width="50%">
	    <iframe id="x5" name="x5" style="width:380px; height:380px" frameborder="0" ></iframe>
	</td>
	<td width="50%">
	    <iframe id="x6" name="x6" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
   </tr>
   <tr>
	<td width="50%">
	    <iframe id="x7" name="x7" style="width:380px; height:380px" frameborder="0" ></iframe>
	</td>
	<td width="50%">
	    <iframe id="x8" name="x8" style="width:420px; height:480px" frameborder="0" ></iframe>
	</td>
   </tr>
   </table>
  </div>
</div>
<script type="text/javascript">
  <#if suitNo?exists>
	jQuery(document).ready(function() { 

		$('#x1').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135903&suitNo=${suitNo}&type=heatEnergyX1PerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x2').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135902&suitNo=${suitNo}&type=heatEnergyX2PerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x4').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135302&suitNo=${suitNo}&type=proteinPercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x3').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135907&suitNo=${suitNo}&type=heatEnergyX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x5').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135904&suitNo=${suitNo}&type=vitaminAAnimalsX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

        $('#x6').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135905&suitNo=${suitNo}&type=ironAnimalsX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

        $('#x7').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135906&suitNo=${suitNo}&type=fatAnimalsX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

		$('#x8').attr('src', '${contextPath}/chart/highcharts/showChart?chartId=135908&suitNo=${suitNo}&type=calciumPhosphorusX3PercentPerDietaryOfWeek&tenantCorrelation=${tenantCorrelation}&sysFlag=${sysFlag}');

        });
  </#if>
</script>	
</body>
</html>