<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>物品采购计划单</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/map.js"></script>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

    function getLink(){
	    var link_ = "${contextPath}/heathcare/goodsPurchasePlan/json?startTime=${startTime}&endTime=${endTime}";
		//alert(link_);
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
				url: '${contextPath}/heathcare/goodsPurchasePlan/json?startTime=${startTime}&endTime=${endTime}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				    <#if audit == true>
					{title:'选择', field: 'chk', width: 60, align: 'center', formatter: formatterKey},
					</#if>
				    {title:'序号', field:'startIndex', width:60, sortable:false},
					{title:'物品名称', field:'goodsName', width:220, align:'left', sortable:true, formatter:formatterName},
					{title:'重量(千克)', field:'quantity', width:120, align:'right', sortable:true, formatter:formatterQuantity},
					{title:'库存重量(千克)', field:'stockQuantity', width:120, align:'right', sortable:true},
					{title:'计量单位', field:'unit', width:100, align:'center', formatter:formatterUnit},
					{title:'计划采购日期', field:'purchaseTime', width:100, align:'center', sortable:true},
					{title:'确认人', field:'confirmName', width:100, align:'center'},
					{title:'确认时间', field:'confirmTime', width:100, align:'center'},
					{title:'状态', field:'businessStatus', width:100, align:'center', formatter:formatterStatus},
					{title:'自动生成', field:'sysFlag', width:120, align:"center", formatter:formatterSys},
					{title:'功能键', field:'functionKey',width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 200,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
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

	function formatterName(val, row){
		return "<a href='#' onclick=javascript:showName('"+row.goodsId+"')>"+val+"</a>";
	}

	function formatterQuantity(val, row){
		if(row.businessStatus == 9){
			return val;
		}
        return "<input type='text' id='"+row.ex_secutity_id+"' name='plan_qty' size='5' class='x-small-decimal' value='"+val+"'>";
	}


	function showName(id){
	    var link="${contextPath}/heathcare/foodComposition/view?id="+id;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "查看食物成分",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['480px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function formatterSys(val, row) {
		if(val =="Y"){
			return "系统生成";
		}
		return "";
	}

	function formatterUnit(val, row){
		if(val == 'KG'){
			return "千克";
		} else if(val == 'G'){
			return "克"
		} else if(val == 'L'){
			return "升"
		} else if(val == 'ML'){
			return "毫升"
		}
	}

    function formatterKey(value, row, index) {
		if(row.businessStatus == 0){
		  var s = '<input name="isCheck" type="checkbox" value="'+row.id+'" > ';
		  return s;
		}
		return "";
	}

	function formatterKeys(val, row){
		if(row.businessStatus == 9){
			return "已审核";
		}
		var str = "";
		<#if audit == true>
		  str = "<a href='javascript:auditRow(\""+row.id+"\");'><img src='${contextPath}/static/images/audit.png' border='0'>&nbsp;审核</a>&nbsp;";
		<#else>
		  str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
		</#if>
	    return str;
	}

	function formatterStatus(val, row){
		if(val == 9){
           return "<font color='green'>已确认</font>";   
		}
        return "未确认";   
	}
	

	function addNew(){
	    var link="${contextPath}/heathcare/goodsPurchasePlan/edit";
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/goodsPurchasePlan/edit?audit=${audit}&id='+row.id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

   function editRow(id){
	    var link = '${contextPath}/heathcare/goodsPurchasePlan/edit?id='+id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function auditRow(id){
	    var link = '${contextPath}/heathcare/goodsPurchasePlan/edit?audit=true&id='+id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "审核记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPurchasePlan/delete?id='+id,
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
					       jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  }
	}

	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/goodsPurchasePlan/edit?id='+row.id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','物品采购单查询');
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
		  var link = '${contextPath}/heathcare/goodsPurchasePlan/edit?id='+selected.id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	    }
	}

	function auditSelected(){
	    var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '${contextPath}/heathcare/goodsPurchasePlan/edit?audit=true&id='+selected.id;
		  jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "审核记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['880px', (jQuery(window).height() - 50) +'px'],
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
		    var link='${contextPath}/heathcare/goodsPurchasePlan/edit?readonly=true&id='+selected.id;
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
			area: ['880px', (jQuery(window).height() - 50) +'px'],
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
				   url: '${contextPath}/heathcare/goodsPurchasePlan/delete?ids='+str,
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
			url:  url,
			dataType:  'json',
			error: function(data){
				alert('服务器处理错误！');
			},
			success: function(data){
				jQuery('#mydatagrid').datagrid('loadData', data);
			}
		});
	}

	function auditBatch(){
	  var objectIds = jQuery("input[name='isCheck']:checked").map(function () {
               return jQuery(this).val();
           }).get().join(',');
	  if(objectIds == ""){
           alert("请选择至少一条记录。");
		   return;
	  }
	  if(confirm("确定批量通过审核吗？")){
		if(confirm("审核通过后的数据不能再修改或删除，确定吗？")){
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPurchasePlan/audit?ids='+objectIds,
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
	}

	function stocklist(){
        var link='${contextPath}/heathcare/goodsOutStock/stocklist';
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "查看库存",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['880px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		   });
	}

	function checkAll() {
		//jQuery("input[name='isCheck']").attr("checked", true); 
		jQuery("[name = isCheck]:checkbox").attr("checked", true);
	}

    function exportXls(){
		var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=GoodsPurchasePlan";
		if(startTime != ""){
			link = link + "&startTime=" + startTime ;
		}
		if(endTime != ""){
			link = link  + "&endTime="+endTime+" 23:59:59";
		}
        window.open(link);
    }

	function exportWeeklyXls(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=GoodsPurchasePlanWeekly";
		if(startTime != ""){
			link = link + "&startTime=" + startTime ;
		}
		if(endTime != ""){
			link = link  + "&endTime="+endTime+" 23:59:59";
		}
        window.open(link);
	}

	function doSearch(){
		var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime != ""){
			startTime = startTime + " 00:00:00";
		}
		if(endTime != ""){
			endTime = endTime + " 23:59:59";
			//jQuery("#endTime").val(endTime);
		}
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		document.iForm.submit();
	}

	function parchasePlan(){
        var link = '${contextPath}/heathcare/dietary/searchlist';
        var x=30;
        var y=30;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1280, 580);
	}

	function deletePlan(){
		var link="${contextPath}/heathcare/goodsPurchasePlan/showRemove";
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "删除计划数据",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		   });
	}

	function saveAll(){
	 if(confirm("确定保存数据吗？")){
		  var array = document.getElementsByName("plan_qty");
		  var len = array.length;
		  var str = "[";
		  for(var i=0; i<len; i++){
             str = str +"{'id':'"+array[i].id+"', 'quantity':"+array[i].value+"}";
			 if(i < len-1){
				 str = str+", ";
			 }
		  }
		  str = str+"]";
		  document.getElementById("json").value=str;
		  //alert(str);
		  var params = jQuery("#iForm").formSerialize();
          jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsPurchasePlan/updateAll',
				   data: params,
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
   <div data-options="region:'north', split:false, border:true" style="height:75px" class="toolbar-backgroud"> 
    <div style="margin:2px;">
	<form id="iForm" name="iForm" method="post">
	 <input type="hidden" id="json" name="json">
	 <table valign="middle">
     <tr>
	  <td valign="middle" width="50%">
		<img src="${contextPath}/static/images/window.png">
		&nbsp;<span class="x_content_title">物品采购计划列表</span>
		<#if audit == true>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-checkall'"
	       onclick="javascript:checkAll();">全选</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-audit'"
		   onclick="javascript:auditSelected();">审核</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-audit'"
		   onclick="javascript:auditBatch();">批量审核</a>
		<#else>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
		   onclick="javascript:addNew();">新增</a>  
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
		   onclick="javascript:editSelected();">修改</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'"
		   onclick="javascript:saveAll();">保存</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
		   onclick="javascript:deleteSelections();">删除</a>
		<br>&nbsp;&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_table_row_add'"
		   onclick="javascript:parchasePlan();">生成采购计划</a>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-delete'"
		   onclick="javascript:deletePlan();">删除计划</a> 
		</#if>
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_cubes'"
	       onclick="javascript:stocklist();">查看库存</a>
	</td>
	<td valign="middle" width="38%">
		  &nbsp;日期&nbsp;开始&nbsp;
		  <input id="startTime" name="startTime" type="text" class="easyui-datebox x-text" style="width:120px"
		         <#if startTime?exists> value="${startTime}"</#if>>
		  &nbsp;结束&nbsp;
		  <input id="endTime" name="endTime" type="text" class="easyui-datebox x-text" style="width:120px"
		         <#if endTime?exists> value="${endTime}"</#if>>
		  &nbsp;
		  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	       onclick="javascript:doSearch();">查找</a>
	</td>
	<td valign="middle" width="12%">
		&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
		   onclick="javascript:exportXls();">导出Excel</a><br>
		&nbsp;
		<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
		   onclick="javascript:exportWeeklyXls();">按周导出Excel</a>
	</td>
    </tr>
   </table>
   </form>
   </div> 
  </div> 
  <div data-options="region:'center',border:true">
	 <table id="mydatagrid"></table>
  </div>  
</div>
</div>
</body>
</html>