<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>班级信息列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
</head>
<body style="margin:0px;">
<div class="easyui-tabs" data-options="tools:'#tab-tools'" style="overflow:hidden; width:100%; height:auto">
  <div title="入园体检" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe id="frm1" name="frm1" scrolling="yes" frameborder="0"  
	        src="" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="定期体检" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe id="frm2" name="frm2" scrolling="yes" frameborder="0"  
	        src="" 
	        style="width:100%;height:680px;"></iframe>
  </div>
  <div title="专项体检" data-options="closable:false" style="overflow:auto;width:100%;height:680px;">
    <iframe id="frm3" name="frm3" scrolling="yes" frameborder="0"  
	        src="" 
	        style="width:100%;height:680px;"></iframe>
  </div>
</div>
</body>
</html>