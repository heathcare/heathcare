<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>数据字典</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

    var prevTreeNode;

    var setting = {
			async: {
				enable: true,
				url:"${contextPath}/mytreenode/treeJson?nodeCode=DICTORY",
				dataFilter: filter
			},
			callback: {
				onExpand: zTreeOnExpand,
				onClick: zTreeOnClick
			}
		};
  
  	function filter(treeId, parentNode, childNodes) {
		if (!childNodes) return null;
		for (var i=0, l=childNodes.length; i<l; i++) {
			childNodes[i].name = childNodes[i].name.replace(/\.n/g, '.');
			childNodes[i].icon="${contextPath}/static/images/basic.gif";
		}
		return childNodes;
	}

	function zTreeOnExpand(treeId, treeNode){
		var zTree1 = $.fn.zTree.getZTreeObj("myTree");
        treeNode.icon="${contextPath}/static/scripts/ztree/css/zTreeStyle/img/diy8.png";
		if(prevTreeNode){
			prevTreeNode.icon="${contextPath}/static/scripts/ztree/css/zTreeStyle/img/diy/2.png";
			zTree1.updateNode(prevTreeNode);
		}
		
		zTree1.updateNode(treeNode);
		prevTreeNode = treeNode; 
	}

    function zTreeOnClick(event, treeId, treeNode, clickFlag) {
		jQuery("#nodeId").val(treeNode.id);
		reloadGrid();
		//loadData('${contextPath}/mydictory/json?nodeId='+treeNode.id);
	}

	function loadData(url){
		  jQuery.get(url,{qq:'xx'},function(data){
		      //var text = JSON.stringify(data); 
              //alert(text);
			  jQuery('#mydatagrid').datagrid('loadData', data);
		  },'json');
	  }

    jQuery(document).ready(function(){
			jQuery.fn.zTree.init(jQuery("#myTree"), setting);
	});

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns:true,
				nowrap: false,
				striped: true,
				collapsible:true,
				url:'${contextPath}/mydictory/json',
				remoteSort: false,
				singleSelect:true,
				idField:'id',
				columns:[[
				    {title:'选择',field:'chk', width:60, align:'center', formatter:formatterKey},
	                {title:'序号',field:'startIndex', width:60, sortable:true},
					{title:'名称',field:'name', width:150, sortable:true},
					{title:'属性值',field:'value', width:150, sortable:true},
					{title:'描述',field:'desc', width:180}
				]],
				rownumbers:false,
				pagination:true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100,200,500]
			});
	});

 
    function formatterKey(value, row, index) {
		var s='<input name="isCheck" type="radio" onclick="javascript:selectedRx(\''+row.name+'\',\''+row.code+'\',\''+row.value+'\')"/> ';
		return s;
	}

	function selectedRx(name, code, value){
		document.getElementById("name").value = name;
		document.getElementById("code").value = code
	    document.getElementById("value").value = value;
	}

	function choose(){
		var parent_window = getOpener();
		var name = parent_window.document.getElementById("${elementId}");
		name.value = document.getElementById("name").value;
		window.close();
	}
		 
	function formatterStatus(val, row){
       if(val == 0){
			return '<span style="color:green; font: bold 13px 宋体;">是</span>';
	   } else  {
			return '<span style="color:red; font: bold 13px 宋体;">否</span>';
	   }  
	}

	function reloadGrid(){
		jQuery('#mydatagrid').datagrid({
			queryParams: {
				nodeId: jQuery("#nodeId").val(),
				typeId: jQuery("#nodeId").val()
			}
		});
	}


	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','数据字典查询');
	    //jQuery('#searchForm').form('clear');
	}


	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}


	function reloadGrid2(){
	    jQuery('#mydatagrid').datagrid('reload');
	}

	function getSelected(){
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected){
		alert(selected.code+":"+selected.name+":"+selected.addr+":"+selected.col4);
	    }
	}

	function getSelections(){
	    var ids = [];
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    for(var i=0;i<rows.length;i++){
		ids.push(rows[i].code);
	    }
	    alert(ids.join(':'));
	}

	function clearSelections(){
	    jQuery('#mydatagrid').datagrid('clearSelections');
	}

	function searchData(){
	    var params = jQuery("#searchForm").formSerialize();
	    var queryParams = jQuery('#mydatagrid').datagrid('options').queryParams;
	    jQuery('#mydatagrid').datagrid('reload');	
	    jQuery('#dlg').dialog('close');
	}
		 
</script>
</head>
<body style="margin:1px;">  
<input type="hidden" id="nodeId" name="nodeId" value="" >
<input type="hidden" id="name" name="name" value="" >
<input type="hidden" id="code" name="code" value="" >
<input type="hidden" id="value" name="value" value="" >
<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'west',split:true" style="width:195px;">
	  <div class="easyui-layout" data-options="fit:true">  
           
			 <div data-options="region:'center',border:false">
			    <ul id="myTree" class="ztree"></ul>  
			 </div> 
			 
        </div>  
	</div> 
   <div data-options="region:'center'">   
		<div class="easyui-layout" data-options="fit:true">  
		   <div data-options="region:'north', split:false, border:true" style="height:42px" class="toolbar-backgroud"> 
			<div style="margin:4px;"> 
				<img src="${contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">数据字典列表</span>
				<button type="button" id="okButton" class="btn btnGrayMini" style="width: 90px" 
	                    onclick="javascript:choose();">确定</button>
		   </div> 
		  </div> 
		  <div data-options="region:'center',border:true">
			 <table id="mydatagrid"></table>
		  </div>  
      </div>
	</div>
</div>
</body>
</html>
