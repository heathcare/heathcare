<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食谱模板类别</title>
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
				width:950,
				height:x_height,
				fit: true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/dietaryCategory/json?nodeId=${nodeId}&nameLike_enc=${nameLike_enc}&sysFlag=${sysFlag}&typeId=${typeId}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'名称', field:'name', width:280, align:"left"},	
						{title:'描述', field:'description', width:320, align:"left"},
						{title:'季节', field:'season', width:120, align:"left", formatter:formatterSeason},
						{title:'地域', field:'region', width:120, align:"left", formatter:formatterRegion},
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

	function formatterSeason(value, row, index) {
		if(row.season == "1"){
           return "春季";
		} else if(row.season == "2"){
           return "夏季";
		} else if(row.season == "3"){
           return "秋季";
		} else if(row.season == "4"){
           return "冬季";
		}
	    return "";
	}

	function formatterRegion(value, row, index) {
		if(row.region == "North"){
           return "北方特色";
		} else if(row.region == "South"){
           return "南方特色";
		} else if(row.region == "Halal"){
           return "清真";
		}
	    return "";
	}


	function formatterKeys(val, row){
		var str = "";
		<#if canEdit == true>
		str = str +"<a href='javascript:editRow(\""+row.id+"\");'>修改</a>";
		if(row.suitNo > 1000){
			str = str+"&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
		}
		</#if>
		str = str +"&nbsp;<a href='javascript:templates(\""+row.sysFlag+"\""+", "+"\""+row.suitNo+"\");'>组成</a>";
	    return str;
	}


    function templates(sysFlag, suitNo){
	    var link = '${contextPath}/heathcare/dietaryTemplateExport/showExport?sysFlag='+sysFlag+'&suitNo='+suitNo;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "食物模板列表",
		  area: ['1280px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

  
	function addNew(){
	    var link="${contextPath}/heathcare/dietaryCategory/edit";
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
	    var link = '${contextPath}/heathcare/dietaryCategory/edit?id='+row.id;
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
	    var link = '${contextPath}/heathcare/dietaryCategory/edit?id='+id;
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
				   url: '${contextPath}/heathcare/dietaryCategory/delete?id='+id,
				   dataType:  'json',
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
							var sysFlag = document.getElementById("sysFlag").value;
							var nameLike = document.getElementById("nameLike").value;
							var link = "${contextPath}/heathcare/dietaryCategory/json?nameLike="+nameLike+"&sysFlag="+sysFlag;
							loadGridData(link);
					   }
				   }
			 });
		  }
	}


	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/dietaryCategory/edit?id='+row.id;
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
		  var link = '${contextPath}/heathcare/dietaryCategory/edit?id='+selected.id;
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
		    var link='${contextPath}/heathcare/dietaryCategory/edit?readonly=true&id='+selected.id;
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
				   url: '${contextPath}/heathcare/dietaryCategory/delete?ids='+ids,
				   dataType:  'json',
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
							var sysFlag = document.getElementById("sysFlag").value;
							var nameLike = document.getElementById("nameLike").value;
							var link = "${contextPath}/heathcare/dietaryCategory/json?nameLike="+nameLike+"&sysFlag="+sysFlag;
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


	function doSearch(){
		var typeId = document.getElementById("typeId").value;
		var sysFlag = document.getElementById("sysFlag").value;
        var nameLike = document.getElementById("nameLike").value;
        var link = "${contextPath}/heathcare/dietaryCategory?nameLike="+nameLike+"&sysFlag="+sysFlag+"&typeId="+typeId;
		window.location.href=link;
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
   <div data-options="region:'north',split:false, border:true" style="height:48px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	  <table width="100%" align="left">
		<tbody>
		 <tr>
		    <td width="35%" align="left">
				&nbsp;<img src="${contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">食谱模板类别列表</span>
				<#if canEdit == true>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
				   onclick="javascript:addNew();">新增</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
				   onclick="javascript:editSelected();">修改</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
				   onclick="javascript:deleteSelections();">删除</a> 
				</#if>
			</td>
			<td width="65%" align="left">
			  &nbsp;餐点&nbsp;
			  <#if typeDict?exists>
				<span style="color:#0066ff;font-weight:bold;">${typeDict.name}</span>
			  </#if>
			  &nbsp;
			  <select id="typeId" name="typeId">
					<option value="">----请选择----</option>
					<#list dictoryList as d>
					<option value="${d.id}">${d.name}</option>
					</#list> 
			  </select>
			  <script type="text/javascript">
				  document.getElementById("typeId").value="${typeId}";
			  </script>
			  &nbsp;类型&nbsp;
			  <select id="sysFlag" name="sysFlag" onchange="javascript:doSearch();">
				  <option value="">----请选择----</option> 
				  <option value="Y">系统内置</option>
				  <option value="N">我自己的</option>
			  </select>
			  <script type="text/javascript">
					document.getElementById("sysFlag").value="${sysFlag}";
			  </script>
			  <script type="text/javascript">
				   document.getElementById("nodeId").value="${nodeId}";
			  </script>
			  &nbsp;
              <input id="nameLike" name="nameLike" type="text" class="x-searchtext"  
					 style="width:125px;" value="${nameLike}">
			  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'" 
	             onclick="javascript:doSearch();" >查找</a>
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