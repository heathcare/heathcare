<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>菜肴</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

    var x_height = Math.floor(window.screen.height * 0.60);
	var x_width = Math.floor(window.screen.width * 0.80);

	if(window.screen.height <= 768){
        x_height = Math.floor(window.screen.height * 0.54);
	}

	if(window.screen.width < 1200){
        x_width = Math.floor(window.screen.width * 0.82);
	} else if(window.screen.width > 1280){
        x_width = Math.floor(window.screen.width * 0.72);
	}  


    jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1250,
				height:x_height,
				fit: true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/dishes/json?nodeId=${nodeId}&nameLike_enc=${nameLike_enc}&sysFlag=${sysFlag}&namePinyinLike=${namePinyinLike}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号',field:'startIndex', width:60, sortable:false},
				        {title:'选择',field: 'chk', width: 60, align: 'center', formatter: formatterKey},
						{title:'名称',field:'name', width:180, align:"left"},	
						{title:'热能(千卡)',field:'heatEnergy', width:90, align:"right", sortable:true},
						{title:'蛋白质(克)',field:'protein', width:90, align:"right", sortable:true},
						{title:'脂肪(克)',field:'fat', width:90, align:"right", sortable:true},
						{title:'碳水化合物(克)',field:'carbohydrate', width:140, align:"right", sortable:true},
						{title:'微生素A(μgRE)',field:'vitaminA', width:140, align:"right", sortable:true},
						{title:'微生素B1(毫克)',field:'vitaminB1', width:140, align:"right", sortable:true},
						{title:'微生素B2(毫克)',field:'vitaminB2', width:140, align:"right", sortable:true},
						{title:'微生素C(毫克)',field:'vitaminC', width:140, align:"right", sortable:true},
						{title:'胡萝卜素(微克)',field:'carotene', width:140, align:"right", sortable:true},
						{title:'视黄醇(微克)',field:'retinol', width:140, align:"right", sortable:true},
						{title:'尼克酸(毫克)',field:'nicotinicCid', width:140, align:"right", sortable:true},
						{title:'钙(毫克)',field:'calcium', width:90, align:"right", sortable:true},
						{title:'铁(毫克)',field:'iron', width:90, align:"right", sortable:true},
						{title:'锌(毫克)',field:'zinc', width:90, align:"right", sortable:true},
						{title:'功能键', field:'functionKey',width:130, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200,500],
				pagePosition: 'bottom',
				onDblClickRow: onMyRowClick 
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});

	function formatterKey(value, row, index) {
		if(row.purchaseFlag == "Y"){
           return "";
		}
		var s = '<input name="check" type="checkbox" value=\"'+row.id+'\" "/> ';
		return s;
	}


	function formatterKeys(val, row){
		var str = "";
		<#if canEdit == true>
		str = str +"<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>&nbsp;<a href='javascript:calRow(\""+row.id+"\");'>计算</a>";
		</#if>
		str = str="&nbsp;<a href='javascript:details(\""+row.id+"\");'><img src='${contextPath}/static/images/statistics.png' border='0'>组成</a>";
	    return str;
	}

 
   function details(dishesId){
	    var link = '${contextPath}/heathcare/dishesItem?dishesId='+dishesId;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "食物构成列表",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	function addNew(){
	    var link="${contextPath}/heathcare/dishes/edit";
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "新增记录",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/dishes/edit?id='+row.id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

   function editRow(id){
	    var link = '${contextPath}/heathcare/dishes/edit?id='+id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dishes/delete?id='+id,
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
					        var nodeId = document.getElementById("nodeId").value;
							var sysFlag = document.getElementById("sysFlag").value;
							var nameLike = document.getElementById("nameLike").value;
							var link = "${contextPath}/heathcare/dishes/json?nodeId="+nodeId+"&nameLike="+nameLike+"&sysFlag="+sysFlag;
							loadGridData(link);
					   }
				   }
			 });
		  }
	}

	function calRow(id){
		if(confirm("确定重新计算菜肴的成分吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dishes/calculate?dishesId='+id,
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
					       jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  }
	}

	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/dishes/edit?id='+row.id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['820px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}


	function editSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '${contextPath}/heathcare/dishes/edit?id='+selected.id;
		  layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['820px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	    }
	}

	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    var link='${contextPath}/heathcare/dishes/edit?readonly=true&id='+selected.id;
			layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['820px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
		}
	}

	function deleteSelections(){
		var ids = $("input[name='check']:checked").map(function () {
               return $(this).val();
           }).get().join(',');
		if(ids.length > 0 ){
		  if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dishes/delete?ids='+ids,
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
					        var nodeId = document.getElementById("nodeId").value;
							var sysFlag = document.getElementById("sysFlag").value;
							var nameLike = document.getElementById("nameLike").value;
							var link = "${contextPath}/heathcare/dishes/json?nodeId="+nodeId+"&nameLike="+nameLike+"&sysFlag="+sysFlag;
							loadGridData(link);
					   }
				   }
			 });
		  }
		} else {
			alert("请选择至少一条记录。");
		}
	}

	function reloadGrid(){
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


	function calSelected() {
        var link = '${contextPath}/heathcare/dishesCount';
        var x=30;
        var y=30;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1280, 580);
	}


	function calAll() {
		if(confirm("确定重新计算该菜肴的各个成分含量吗？")){
            var link = "${contextPath}/heathcare/dishes/calculateAll";
		    jQuery.ajax({
				   type: "POST",
				   url: link,
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
					       jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  } 
	}

 
	function doSearch(){
		var nodeId = document.getElementById("nodeId").value;
		var sysFlag = document.getElementById("sysFlag").value;
        var nameLike = document.getElementById("nameLike").value;
        var link = "${contextPath}/heathcare/dishes/json?nodeId="+nodeId+"&nameLike="+nameLike+"&sysFlag="+sysFlag;
		//window.location.href=link;
		loadGridData(link);
	}

	function searchXY(namePinyinLike){
        var nodeId = document.getElementById("nodeId").value;
		var sysFlag = document.getElementById("sysFlag").value;
        var nameLike = document.getElementById("nameLike").value;
        var link = "${contextPath}/heathcare/dishes/json?nodeId="+nodeId+"&nameLike="+nameLike+"&sysFlag="+sysFlag+"&namePinyinLike="+namePinyinLike;
		//window.location.href=link;
		loadGridData(link);
	}

	function loadGridData(url){
	    jQuery.ajax({
			type: "POST",
			url:  url,
			dataType: 'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:false, border:true" style="height:68px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	  <table width="100%" align="left">
		<tbody>
		 <tr>
		    <td width="35%" align="left">
				&nbsp;<img src="${contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">菜肴列表</span>
				<#if canEdit == true>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
				   onclick="javascript:addNew();">新增</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
				   onclick="javascript:editSelected();">修改</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
				   onclick="javascript:deleteSelections();">删除</a> 
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-formula'"
				   onclick="javascript:calAll();">计算</a>
				</#if>
			</td>
			<td width="65%" align="left">
			  &nbsp;类型&nbsp;
			  <select id="sysFlag" name="sysFlag" onchange="javascript:doSearch();">
				  <option value="">----请选择----</option> 
				  <option value="Y">系统内置</option>
				  <option value="N">我自己的</option>
			  </select>
			  <script type="text/javascript">
					document.getElementById("sysFlag").value="${sysFlag}";
			  </script>
			  &nbsp;品类&nbsp;
			  <select id="nodeId" name="nodeId" onchange="javascript:doSearch();">
			    <option value="">----请选择----</option> 
				<#list categories as category>
				<option value="${category.id}">${category.name}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				   document.getElementById("nodeId").value="${nodeId}";
			  </script>
			  &nbsp;
              <input id="nameLike" name="nameLike" type="text" class="x-searchtext"  
					 style="width:185px;" value="${nameLike}">
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'" 
	             onclick="javascript:doSearch();" >查找</a>
			</td>
		</tr>
		<tr>
			<td colspan="2">
			    &nbsp;&nbsp;&nbsp;&nbsp;
				<#list charList as item>
				&nbsp;<span style="cursor: pointer;" onclick="javascript:searchXY('${item}');">${item}</span>&nbsp;
				</#list>
			</td>
		</tr>
	   </tbody>
	  </table>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>