<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>循环SQL到表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">


    function getLink(){
	    var link_ = "${contextPath}/sys/loopSqlToTable/json?q=1";
		var namePinyinLike = jQuery("#namePinyinLike").val();
		if(namePinyinLike != "" && namePinyinLike != "undefined" ){
		    link_ = link_ + "&namePinyinLike="+namePinyinLike;
		}
		return link_;
    }

    jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: getLink(),
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
					    {title:'标题', field:'title', width:250},
					    {title:'目标表', field:'targetTableName', width:150},
					    {title:'同步状态', field:'syncStatus', width:90, formatter:formatterSyncStatus},
					    {title:'同步时间', field:'syncTime_datetime', width:145},
						{title:'创建人', field:'createBy', width:90},
						{title:'创建时间', field:'createTime', width:90},
					    {title:'是否有效', field:'locked', width:90, formatter:formatterActive},
					    {title:'功能键', field:'functionKey', width:150, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick 
			});

			var pgx = $("#mydatagrid").datagrid("getPager");
			if(pgx){
			   $(pgx).pagination({
				   onBeforeRefresh:function(){
					   //alert('before refresh');
				   },
				   onRefresh:function(pageNumber,pageSize){
					   //alert(pageNumber);
					   //alert(pageSize);
					   loadGridData(getLink()+"&page="+pageNumber+"&rows="+pageSize);
					},
				   onChangePageSize:function(){
					   //alert('pagesize changed');
					   loadGridData(getLink());
					},
				   onSelectPage:function(pageNumber, pageSize){
					   //alert(pageNumber);
					   //alert(pageSize);
					   loadGridData(getLink()+"&page="+pageNumber+"&rows="+pageSize);
					}
			   });
			}
	});


    function formatterActive(val, row){
        var str = "";
		if(val == 0){
             str = "<font color='green'>有效</font>";
		} else {
             str = "<font color='red'>无效</font>";
		}
	    return str;
	}

	function formatterSyncStatus(val, row){
        var str = "";
		if(val == 9){
             str = "<font color='green'>成功</font>";
		} else if(val == -1) {
             str = "<font color='red'>失败</font>";
		}
	    return str;
	}

	function formatterKeys(val, row){
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>&nbsp;<a href='javascript:dropTable(\""+row.id+"\");'>重建表</a>&nbsp;<a href='javascript:historyList(\""+row.id+"\");'>日志</a>";
	    return str;
	}


	function historyList(syncId){
	    var link = '${request.contextPath}/sys/executionLog?type=loop_sql_table&businessKey=loop_sql_table_'+syncId;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "执行历史",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['1080px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}
	
	function addNew(){
	    var link="${contextPath}/sys/loopSqlToTable/edit";
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "新增记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}

	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/sys/loopSqlToTable/edit?id='+row.id;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}

    function editRow(id){
	    var link = '${contextPath}/sys/loopSqlToTable/edit?id='+id;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}

	
	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/loopSqlToTable/delete?id='+id,
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
	    var link = '${contextPath}/sys/loopSqlToTable/edit?id='+row.id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		}); 
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','循环SQL到表查询');
	    //jQuery('#searchForm').form('clear');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
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
		  var link = '${contextPath}/sys/loopSqlToTable/edit?id='+selected.id;
		  jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "编辑记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
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
		    var link='${contextPath}/sys/loopSqlToTable/edit?readonly=true&id='+selected.id;
		    jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "编辑记录",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['980px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			}); 
		}
	}

	function deleteSelections(){
		var ids = [];
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		for(var i=0;i<rows.length;i++){
			ids.push(rows[i].id);
		}
		if(ids.length > 0 ){
		  if(confirm("数据删除后不能恢复，确定删除吗？")){
		    var str = ids.join(',');
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/sys/loopSqlToTable/delete?ids='+str,
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

	function loadGridData(url){
	    jQuery.ajax({
			type: "POST",
			url: url,
			dataType: 'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

	function searchData(){
        var params = jQuery("#searchForm").formSerialize();
        jQuery.ajax({
                    type: "POST",
                    url: '${contextPath}/sys/loopSqlToTable/json',
                    dataType: 'json',
                    data: params,
                    error: function(data){
                              alert('服务器处理错误！');
                    },
                    success: function(data){
                              jQuery('#mydatagrid').datagrid('loadData', data);
                    }
                  });

	    jQuery('#dlg').dialog('close');
	}

	function dropTable(id){
	  if(confirm("数据删除后不能恢复,确定重建目标表吗?")){
		var link = "${request.contextPath}/sys/loopSqlToTable/dropTable?syncId="+id;
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
	                          //window.location.reload();
	                       }
                       }
		 });
	  }
	}

	function execute(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		    alert("请选择其中一条记录。");
		    return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected && confirm("确定要在目标库上执行同步操作吗？")){
			var syncId = selected.id;
			//alert("syncId="+syncId+"  "+selected.title);
		    jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/sys/loopSqlToTable/execute?syncId='+syncId,
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
				   }
			});
	    }
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
		    <td width="55%" align="left">
				<img src="${contextPath}/static/images/window.png">
				&nbsp;<span class="x_content_title">循环SQL到表列表</span>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
				   onclick="javascript:addNew();">新增</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
				   onclick="javascript:editSelected();">修改</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
				   onclick="javascript:deleteSelections();">删除</a> 
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sys'"
	               onclick="javascript:execute();">执行</a>
			</td>
			<td width="45%" align="left">&nbsp;</td>
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