<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>记账法</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
</head>
<body style="margin:0px;">
<div class="easyui-tabs" data-options="tools:'#tab-tools'" style="overflow:hidden; width:100%; height:auto">
  <div title="食物用量登记" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/goodsActualQuantity" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="平均每日各种实得热量及营养素" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/dietaryAnalyze/dailyAvg?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="平均推荐摄入量" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/tenantReportMain/exportXls?tenantId=${tenantId}&reportId=TenantDietaryNutritionAvgQuantityV1&ts=${ts}&outputFormat=html" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="分析与评价" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/dietaryAnalyze/report?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="各种营养素占比" data-options="closable:false" style="overflow:hidden;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/dietaryAnalyze/percent?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="食物营养成分统计" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe scrolling="yes" frameborder="0"  
	        src="${contextPath}/heathcare/dietaryAnalyze?tenantId=${tenantId}" 
	        style="width:100%;height:680px;"></iframe>
  </div>
</div>
</body>
</html>