<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>每月实际用量费用</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.table-border { background-color:#3399cc; height: 32px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 32px;font-size: 12px; font-family:"宋体"}

</style>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
 
 	function switchXY(){
        document.iForm.submit();
	}

</script>
</head>
<body style=" margin: 2px">
<form id="iForm" name="iForm" method="post" action="">
 <table>
	<tr>
		<td>&nbsp;&nbsp;年份&nbsp;&nbsp;
            <select id="year" name="year" onchange="javascript:switchXY();">
            	<option value="">--请选择--</option>
            	<#list years as year>
            	<option value="${year}">${year}</option>
            	</#list> 
            </select>
            <script type="text/javascript">
            	document.getElementById("year").value="${year}";
            </script>
		</td>
		<td>&nbsp;&nbsp;月份&nbsp;&nbsp;
            <select id="month" name="month" onchange="javascript:switchXY();">
            	<option value="">--请选择--</option>
            	<#list months as month>
            	<option value="${month}">${month}</option>
            	</#list> 
            </select>
            <script type="text/javascript">
            	document.getElementById("month").value="${month}";
            </script>
		</td>
		<td>&nbsp;&nbsp;
		    <input type="button" value="确定" onclick="javascript:switchXY();" class="btnGray">
		</td>
	</tr>
  </table>
  <br>
  <table class="table-content" cellpadding='1' cellspacing='1' border="0" width="90%">
      <#if total?exists>
      <tr>
	     <td colspan="5" class="table-content" style="height:25px;" align="center">
		    <span class="x_content_title">${year}年${month}月实际用量费用汇总</span>
		 </td>
	  </tr>
	  </#if>
	  <tr>
		<td width="20%" align="center" valign="top">
		 <#if weekTotalList0?exists>
		  <table cellpadding='1' cellspacing='1' class="table-border"  width="100%">
		      <tr>
			   <td colspan="2" align="center" class="table-content" style="height:25px;">${year}年${month}月第1周</td>
			  </tr>
			  <#list weekTotalList0 as fee>
			  <tr>
				<td width="70%" class="table-content">&nbsp;${fee.name}</td>
				<td width="30%" class="table-content" align="right">${fee.cost}</td>
			  </tr>
			  </#list> 
			  <tr>
			   <td colspan="2" class="table-content" style="height:25px;" align="right">小计：${total0}</td>
			  </tr>
		  </table>
		  </#if>
		</td>
		<td width="20%" align="center" valign="top">
		 <#if weekTotalList1?exists>
		  <table cellpadding='1' cellspacing='1' class="table-border"  width="100%">
		      <tr>
			   <td colspan="2" align="center" class="table-content" style="height:25px;">${year}年${month}月第2周</td>
			  </tr>
			  <#list weekTotalList1 as fee>
			  <tr>
				<td width="70%" class="table-content">&nbsp;${fee.name}</td>
				<td width="30%" class="table-content" align="right">${fee.cost}</td>
			  </tr>
			  </#list> 
			  <tr>
			   <td colspan="2" class="table-content" style="height:25px;" align="right">小计：${total1}</td>
			  </tr>
		  </table>
		  </#if>
		</td>
		<td width="20%" align="center" valign="top">
		  <#if weekTotalList2?exists>
		  <table cellpadding='1' cellspacing='1' class="table-border"  width="100%">
		      <tr>
			   <td colspan="2" align="center" class="table-content" style="height:25px;">${year}年${month}月第3周</td>
			  </tr>
			  <#list weekTotalList2 as fee>
			  <tr>
				<td width="70%" class="table-content">&nbsp;${fee.name}</td>
				<td width="30%" class="table-content" align="right">${fee.cost}</td>
			  </tr>
			  </#list> 
			  <tr>
			   <td colspan="2" class="table-content" style="height:25px;" align="right">小计：${total2}</td>
			  </tr>
		  </table>
		  </#if>
		</td>
		<td width="20%" align="center" valign="top">
		 <#if weekTotalList3?exists>
		  <table cellpadding='1' cellspacing='1' class="table-border"  width="100%">
		      <tr>
			   <td colspan="2" align="center" class="table-content" style="height:25px;">${year}年${month}月第4周</td>
			  </tr>
			  <#list weekTotalList3 as fee>
			  <tr>
				<td width="70%" class="table-content">&nbsp;${fee.name}</td>
				<td width="30%" class="table-content" align="right">${fee.cost}</td>
			  </tr>
			  </#list> 
			  <tr>
			   <td colspan="2" class="table-content" style="height:25px;" align="right">小计：${total3}</td>
			  </tr>
		  </table>
		  </#if>
		</td>
		<td width="20%" align="center" valign="top">
		  <#if weekTotalList4?exists>
		  <table cellpadding='1' cellspacing='1' class="table-border"  width="100%">
		      <tr>
			   <td colspan="2" align="center" class="table-content" style="height:25px;">${year}年${month}月第5周</td>
			  </tr>
			  <#list weekTotalList4 as fee>
			  <tr>
				<td width="70%" class="table-content">&nbsp;${fee.name}</td>
				<td width="30%" class="table-content" align="right">${fee.cost}</td>
			  </tr>
			  </#list> 
			  <tr>
			   <td colspan="2" class="table-content" style="height:25px;" align="right">小计：${total4}</td>
			  </tr>
		  </table>
		  </#if>
		</td>
	  </tr>
	  <#if total?exists>
      <tr>
	     <td colspan="5" class="table-content" style="height:25px;" align="right">
		   <span style="font: bold 15px 微软雅黑; color:#ff3333;">本月合计：${total}</span>
		 </td>
	  </tr>
	  </#if>
  </table>
 </form>
</body>
</html>