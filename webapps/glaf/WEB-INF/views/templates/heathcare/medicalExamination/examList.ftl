<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>儿童健康检查</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">


	function getLink(){
       var link_ = "${contextPath}/heathcare/medicalExamination/personJson?type=${type}&personId=${personId}";
	   return link_;
	}

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width: 1200,
				height: x_height,
				fit: false,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/heathcare/medicalExamination/personJson?type=${type}&personId=${personId}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'姓名', field:'name', width:90, align:"center", sortable:true, formatter:formatterName},
						{title:'性别', field:'sex', width:60, formatter:formatterSex, align:"center", sortable:true},
						{title:'身高(厘米)', field:'height', width:120, align:"right", sortable:true},
						{title:'身高评价', field:'heightEvaluateHtml', width:120, align:"center", sortable:true},
						{title:'体重(千克)', field:'weight', width:120, align:"right", sortable:true},
						{title:'体重评价', field:'weightEvaluateHtml', width:120, align:"center", sortable:true},
						{title:'出生日期', field:'birthday', width:90, align:"center", sortable:true},
					    {title:'体检日期', field:'checkDate', width:90, align:"center", sortable:true},
						{title:'月龄', field:'checkAgeOfMonth', width:80, align:"center", sortable:true},
						{title:'功能键', field:'functionKey',width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 50,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'both',
				onDblClickRow: onMyRowClick 
			});

			$('#mydatagrid').datagrid({
				onDblClickRow: function(index, row){
					 editRow(row.id);
				}
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


	function formatterSex(val, row){
		if(val == "1"){
			return "男";
		} else {
            return "女";
		}
	}


	function formatterName(val, row){
        return "<a href='javascript:showPerson(\""+row.personId+"\");'>"+val+"</a>&nbsp;";
	}

	function showPerson(personId){
		var link='${contextPath}/heathcare/person/edit?id='+personId;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "查看儿童信息",
		  area: ['880px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		}); 
	}


	function formatterKeys(val, row){
		<#if hasWritePermission>
		var str = "<a href='javascript:editRow(\""+row.id+"\",\""+row.type+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
	    return str;
		<#else>
		return "<a href='javascript:editRow(\""+row.id+"\",\""+row.type+"\");'>查看</a>&nbsp;";
		</#if>
	}
	

	function addNew(){
	    var link="${contextPath}/heathcare/medicalExamination/edit?personId=${personId}&checkId=${checkId}&type=${type}";
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "新增记录",
		  area: ['980px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}


	function onMyRowClick(rowIndex, row){
		//alert(row.type);
	    var link = '${contextPath}/heathcare/medicalExamination/edit?id='+row.id;
		if(row.type == '1'){
			link = '${contextPath}/heathcare/medicalExamination/enterEdit?id='+row.id;
		} else if(row.type == '7'){
			link = '${contextPath}/heathcare/medicalExamination/edit7?id='+row.id;
		}
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['980px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

    function editRow(id, type){
	    var link = '${contextPath}/heathcare/medicalExamination/edit?id='+id;
		if(type == "1"){
			link = '${contextPath}/heathcare/medicalExamination/enterEdit?id='+id;
		} else if(type == "7"){
			link = '${contextPath}/heathcare/medicalExamination/edit7?id='+id;
		}
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['980px', (jQuery(window).height() - 50) +'px'],
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
				   url: '${contextPath}/heathcare/medicalExamination/delete?id='+id,
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
	    var link = '${contextPath}/heathcare/medicalExamination/edit?id='+row.id;
		if(row.type == '1'){
			link = '${contextPath}/heathcare/medicalExamination/enterEdit?id='+row.id;
		} else if(row.type == '7'){
			link = '${contextPath}/heathcare/medicalExamination/edit7?id='+row.id;
		}
	    layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['980px', (jQuery(window).height() - 50) +'px'],
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
		  var link = '${contextPath}/heathcare/medicalExamination/edit?id='+selected.id;
		  layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['980px', (jQuery(window).height() - 50) +'px'],
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
		    var link='${contextPath}/heathcare/medicalExamination/edit?id='+selected.id;
		    layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "编辑记录",
			  area: ['980px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
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
				   url: '${contextPath}/heathcare/medicalExamination/delete?ids='+str,
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
					       reloadGrid();
					   }
				   }
			 });
		  }
		} else {
			alert("请选择至少一条记录。");
		}
	}

	function reloadGrid(){
	    //jQuery('#mydatagrid').datagrid('reload');
		jQuery('#mydatagrid').datagrid({
			queryParams: {
				nodeId: jQuery("#nodeId").val(),
				gradeId: jQuery("#gradeId").val(),
				checkId: '${checkId}',
				type: '${type}'
			}
		});
		//layer.close();
		//var index=window.parent.layer.getFrameIndex(window.name);
        //window.parent.layer.close(index);
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

	function searchData(){
        var params = jQuery("#searchForm").formSerialize();
        jQuery.ajax({
                    type: "POST",
                    url: '${contextPath}/heathcare/medicalExamination/json',
                    dataType:  'json',
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

	function switchXY(){
        document.iForm.submit();
	}


	function exportXls(){
		var gradeId = jQuery("#gradeId").val();
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalExaminationSicknessPositiveSign&checkId=${checkId}&ts=${ts}";
		if(gradeId != ""){
			link = link + "&gradeId=" + gradeId;
		}
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
        window.open(link);
    }

	function exportXls2(){
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=Growth&personId=${personId}&checkId=${checkId}&ts=${ts}";
        window.open(link);
    }
	
	function exportXls3(){
		var personId = document.getElementById("personId").value;
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=Growth&personId="+personId+"&checkId=${checkId}&ts=${ts}";
        window.open(link);
    }	

	function line2(){
		var standardType = "ISO";
        var type = "2";
		var sex = "${person.sex}";
		var link = "${contextPath}/heathcare/growthSpline/line?personId=${personId}&type="+type+"&standardType="+standardType+"&sex="+sex;
		var x=20;
	    var y=20;
		if(is_ie) {
			x=document.body.scrollLeft+event.clientX-event.offsetX-200;
			y=document.body.scrollTop+event.clientY-event.offsetY-200;
		}
		var x_height2 = Math.floor(window.screen.height * 0.84);
		var x_width2 = Math.floor(window.screen.width * 0.96);
		openWindow(link, "blank_", x, y, x_width2, x_height2);
	}

	function line3(){
		var standardType = "ISO";
        var type = "3";
		var sex = "${person.sex}";
		var link = "${contextPath}/heathcare/growthSpline/line?personId=${personId}&type="+type+"&standardType="+standardType+"&sex="+sex;
		var x=20;
	    var y=20;
		if(is_ie) {
			x=document.body.scrollLeft+event.clientX-event.offsetX-200;
			y=document.body.scrollTop+event.clientY-event.offsetY-200;
		}
		var x_height2 = Math.floor(window.screen.height * 0.84);
		var x_width2 = Math.floor(window.screen.width * 0.96);
		openWindow(link, "blank_", x, y, x_width2, x_height2);
	}

	function line4(){
		var standardType = "ISO";
        var type = "4";
		var sex = "${person.sex}";
		var link = "${contextPath}/heathcare/growthSpline/line?personId=${personId}&type="+type+"&standardType="+standardType+"&sex="+sex;
		var x=20;
	    var y=20;
		if(is_ie) {
			x=document.body.scrollLeft+event.clientX-event.offsetX-200;
			y=document.body.scrollTop+event.clientY-event.offsetY-200;
		}
		var x_height2 = Math.floor(window.screen.height * 0.84);
		var x_width2 = Math.floor(window.screen.width * 0.96);
		openWindow(link, "blank_", x, y, x_width2, x_height2);
	}
</script>
</head>
<body>  
<input type="hidden" id="nodeId" name="nodeId" value="" > 
<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'center'">  
        <div class="easyui-layout" data-options="fit:true">  
          <div data-options="region:'center', split:true, border:true, fit:true" style="margin-top:-4px;">
		   <div data-options="region:'north', split:false, border:true" style="height:45px" class="toolbar-backgroud"> 
		    <div style="margin-top:4px;">
			<form id="iForm" name="iForm" method="post" action="">
			<input type="hidden" id="personId" name="personId">
			<input type="hidden" id="type" name="type" value="${type}">
			<table>
			  <tr>
				<td>
					<img src="${contextPath}/static/images/window.png">
					&nbsp;<span class="x_content_title">健康检查列表</span>
					<#if hasWritePermission>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
					   onclick="javascript:addNew();">新增</a>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
					   onclick="javascript:editSelected();">修改</a>  
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
					   onclick="javascript:deleteSelections();">删除</a> 
					</#if>
				</td>
				<td>年份&nbsp;&nbsp;
					<select id="year" name="year">
						<option value="">--请选择--</option>
						<option value="2015">2015</option>
						<option value="2016">2016</option>
						<option value="2017">2017</option>
						<option value="2018">2018</option>
						<option value="2019">2019</option>
					</select>
					<script type="text/javascript">
						document.getElementById("year").value="${year}";
					</script>
				</td>
				<td>月份&nbsp;&nbsp;
					<select id="month" name="month">
						<option value="">--请选择--</option>
						<#list months as month>
						<option value="${month}">${month}</option>
						</#list> 
					</select>
					<script type="text/javascript">
						document.getElementById("month").value="${month}";
					</script>
				</td>
				<td>&nbsp;
				  <#if personId?exists>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls2();">打印</a>
					&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_chart_line'" 
					   onclick="javascript:line2();">年龄别身高曲线图</a>
					&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_chart_line'" 
					   onclick="javascript:line3();">年龄别体重曲线图</a>
					&nbsp;&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_chart_line'" 
					   onclick="javascript:line4();">身高别体重曲线图</a>
				  <#else>

				  </#if>
				</td>
			 </tr>
			</table>
			</form>
			</div>
		   </div> 
		   <div data-options="region:'center', border:true" data-options="fit:true">
			 <table id="mydatagrid" style="width:100%; height:100%"></table>
		   </div>  
      </div>  
    </div>  
  </div>  
</div>
</body>  
</html>