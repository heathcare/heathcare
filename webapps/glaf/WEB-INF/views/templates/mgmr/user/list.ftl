 <!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">
   var contextPath="${contextPath}";

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/mgmr/tenant/user/json?tenantId=${tenantId}',
				remoteSort: false,
				singleSelect: true,
				idField: 'userId',
				columns:[[
				        {title:'序号', field:'startIndex', width:80, sortable:false},
						{title:'登录账号',field:'userId', width:120},
						{title:'用户姓名',field:'name', width:120},
						{title:'邮箱',field:'email', width:120},
						{title:'最后登录时间',field:'lastLoginTime', width:120},
						{title:'登录IP',field:'loginIP', width:120},
						{title:'最后更新时间',field:'updateDate', width:120}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'bottom' 
			});
	});
 
</script>
</head>
<body style="margin:1px;"> 

<div style="margin:2;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">用户列表</span>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</body>
</html>
