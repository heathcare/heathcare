<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>体格检查</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	var setting = {
		async: {
				enable: true,
				url:"${contextPath}/heathcare/gradeInfo/treeJson"
			},
			callback: {
				beforeClick: zTreeBeforeClick,
				onClick: zTreeOnClick
			}
	};

	function zTreeBeforeClick(treeId, treeNode, clickFlag) {
           
	}

	function zTreeOnClick(event, treeId, treeNode, clickFlag) {
		if(treeNode.type == "person"){
		    jQuery("#nodeId").val(treeNode.id);
			jQuery("#personId").val(treeNode.id);
			jQuery("#div_person_print").show();
		    loadData('${contextPath}/heathcare/medicalExamination/json?checkId=${checkId}&type=${type}&personId='+treeNode.id);
		} else {
			jQuery("#nodeId").val("");
			jQuery("#personId").val("");
			jQuery("#div_person_print").hide();
			document.getElementById("gradeId").value=treeNode.id;
		    loadData('${contextPath}/heathcare/medicalExamination/json?checkId=${checkId}&type=${type}&gradeId='+treeNode.id);
		}
	}

	function loadData(url){
		$.post(url,{qq:'xx'},function(data){
		      //var text = JSON.stringify(data); 
              //alert(text);
			  $('#mydatagrid').datagrid('loadData', data);
		},'json');
	}


	$(document).ready(function(){
		$.fn.zTree.init($("#myTree"), setting);
		$("#search-bt").click(searchNodes);
	});


	//用按钮查询节点  
	function searchNodes(){  
		var treeObj = $.fn.zTree.getZTreeObj("myTree");  
		var keywords=$("#keyword").val();  
		var nodes = treeObj.getNodesByParamFuzzy("name", keywords, null);  
		if (nodes.length>0) {  
			treeObj.selectNode(nodes[0]);  
		}  
	}  
	
		  
	function searchKeyWords(){  
		var treeObj = $.fn.zTree.getZTreeObj("myTree");  
		var keywords = $("#keyword").val();  
		var nodes = treeObj.getNodesByParamFuzzy("name", keywords, null);  
		if (nodes.length > 0) {  
			treeObj.selectNode(nodes[0]);  
		}  
	}  

	function setCheck() {  
        var zTree = $.fn.zTree.getZTreeObj("myTree");  
        type = { "Y":p + s, "N":p + s};  
        zTree.setting.check.chkboxType = type;  
        showCode('setting.check.chkboxType = { "Y" : "' + type.Y + '", "N" : "' + type.N + '" };');  
    }  

	function getLink(){
       var link_ = "${contextPath}/heathcare/medicalExamination/json?type=${type}&checkId=${checkId}";
	   if(jQuery("#nodeId").val() != ""){
           link_ = link_+"&gradeId="+jQuery("#nodeId").val();
	   } else {
           link_ = link_+"&gradeId="+jQuery("#gradeId").val();
	   }
	   if(jQuery("#personId").val() != ""){
           link_ = link_+"&personId="+jQuery("#personId").val();
	   }
	   if(jQuery("#year").val() != ""){
           link_ = link_+"&year="+jQuery("#year").val();
	   }
	   if(jQuery("#month").val() != ""){
           link_ = link_+"&month="+jQuery("#month").val();
	   }
	   if(jQuery("#sex").val() != ""){
           link_ = link_+"&sex="+jQuery("#sex").val();
	   }
	   return link_;
	}

    jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width: x_width,
				height: x_height,
				fit: false,
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
						{title:'班级', field:'gradeName', width:120, align:"left", sortable:true},
						{title:'姓名', field:'name', width:80, align:"center", sortable:true, formatter:formatterName},
						{title:'性别', field:'sex', width:60, formatter:formatterSex, align:"center", sortable:true},
						{title:'身高(厘米)&nbsp;&nbsp;', field:'height', width:90, align:"right", sortable:true},
						{title:'&nbsp;身高评价', field:'heightEvaluateHtml', width:120, align:"center", sortable:true},
						{title:'体重(千克)&nbsp;&nbsp;', field:'weight', width:90, align:"right", sortable:true},
						{title:'&nbsp;体重评价', field:'weightEvaluateHtml', width:120, align:"center", sortable:true},
						{title:'综合评价', field:'bmiEvaluateHtml', width:120, align:"center", sortable:true},
						{title:'出生日期', field:'birthday', width:90, align:"center", sortable:true},
					    {title:'体检日期', field:'checkDate', width:90, align:"center", sortable:true},
						{title:'月龄&nbsp;', field:'checkAgeOfMonth', width:70, align:"center", sortable:true},
						{title:'功能键', field:'functionKey',width:120, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 50,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000,2000],
				pagePosition: 'bottom',
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
        return "<a href='javascript:showPersonExamList(\""+row.personId+"\");'>"+val+"</a>&nbsp;";
	}

	function showPersonExamList(personId){
		var link='${contextPath}/heathcare/medicalExamination/examList?gradeId=${gradeId}&personId='+personId;
		var x=180;
        var y=80;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 1115, 580);
	}

	function formatterKeys(val, row){
		<#if hasWritePermission>
		var str = "<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
	    return str;
		<#else>
		return "<a href='javascript:editRow(\""+row.id+"\");'>查看</a>&nbsp;";
		</#if>
	}
	

	function addNew(){
        var personId = jQuery("#nodeId").val();
		if(personId == ""){
			alert("请选择学生。");
			return;
		}
	    var link="${contextPath}/heathcare/medicalExamination/edit?personId="+personId+"&checkId=${checkId}&type=${type}";
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "新增记录",
		  area: ['880px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}


	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/medicalExamination/edit?id='+row.id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['880px', (jQuery(window).height() - 50) +'px'],
		  shade: 0.8,
		  fixed: false, //不固定
		  shadeClose: true,
		  content: [link, 'no']
		});
	}

    function editRow(id){
	    var link = '${contextPath}/heathcare/medicalExamination/edit?id='+id;
		layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['880px', (jQuery(window).height() - 50) +'px'],
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
					       reloadGrid();
					   }
				   }
			 });
		  }
	}

	
	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/medicalExamination/edit?id='+row.id;
	    layer.open({
		  type: 2,
          maxmin: true,
		  shadeClose: true,
		  title: "编辑记录",
		  area: ['880px', (jQuery(window).height() - 50) +'px'],
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
			  area: ['880px', (jQuery(window).height() - 50) +'px'],
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
			  area: ['880px', (jQuery(window).height() - 50) +'px'],
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

	function changeCheck(){
		var checkId = jQuery("#checkId").val();
		location.href="${contextPath}/heathcare/medicalExamination?checkId="+checkId;
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
		if(sex != ""){
			link = link  + "&sex=" + sex;
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

	function exportXls55(){
		var gradeId = jQuery("#gradeId").val();
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
		var sex = jQuery("#sex").val();
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalExaminationCount&checkId=${checkId}&ts=${ts}";
		if(gradeId != ""){
			link = link + "&gradeId=" + gradeId;
		}
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
		if(sex != ""){
			link = link  + "&sex=" + sex;
		}
        window.open(link);
    }

	function exportXls60(){
		var gradeId = jQuery("#gradeId").val();
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
		if(gradeId == ""){
			alert("请选择班级！");
			return;
		}
        if(year == ""){
			alert("请选择年份！");
			return;
		}
		if(month == ""){
			alert("请选择月份！");
			return;
		}
		var link="${contextPath}/heathcare/tenantReportMain/exportXls?reportId=TenantMedicalExaminationGrade&ts=${ts}&type=${type}";
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

	function exportXls65(){
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
        if(year == ""){
			alert("请选择年份！");
			return;
		}
		if(month == ""){
			alert("请选择月份！");
			return;
		}
		var link="${contextPath}/heathcare/tenantReportMain/exportXls?reportId=TenantMedicalExaminationGradeCount&ts=${ts}&type=${type}";
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
        window.open(link);
    }

	function exportXls70(){
		var year = jQuery("#year").val();
        var month = jQuery("#month").val();
        if(year == ""){
			alert("请选择年份！");
			return;
		}
		if(month == ""){
			alert("请选择月份！");
			return;
		}
		var link="${contextPath}/heathcare/tenantReportMain/exportXls?reportId=MedicalExaminationPersonExport&ts=${ts}&type=${type}";
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
        window.open(link);
    }

	function showChart(){
        var year = jQuery("#year").val();
        var month = jQuery("#month").val();
		var sex = jQuery("#sex").val();
		var link="${contextPath}/heathcare/medicalExaminationChart/columnLine?type=${type}&ts=${ts}";
		if(year != ""){
			link = link + "&year=" + year;
		}
		if(month != ""){
			link = link  + "&month=" + month;
		}
		if(sex != ""){
			link = link  + "&sex=" + sex;
		}
		window.open(link);
	}
</script>
</head>
<body>  
<input type="hidden" id="nodeId" name="nodeId" value="" > 
<div class="easyui-layout" data-options="fit:true">  
    <div data-options="region:'west', split:true" style="width:180px;">
	  <div class="easyui-layout" data-options="fit:true"> 
		  <div data-options="region:'north', border:false" class="search-bar" style="margin:2px;height:30px">  
            <input id="keyword" name="keyword" type="text" placeholder="请输入..." class="x-small-text" style="width:100px;"> 
			<input id="search-bt" type="button" value="查找" class="btnGrayMini" onclick="javascript:searchKeyWords();">
          </div>  
		  <div data-options="region:'center', border:false">
			  <ul id="myTree" class="ztree"></ul>  
		  </div>		 
        </div>  
	</div> 
	
    <div data-options="region:'center'">  
        <div class="easyui-layout" data-options="fit:true">  
          <div data-options="region:'center', split:true, border:true, fit:true" style="margin-top:-4px;">
		   <div data-options="region:'north', split:false, border:true" style="height:62px" class="toolbar-backgroud"> 
		    <div style="margin-top:4px;">
			<form id="iForm" name="iForm" method="post" action="">
			<input type="hidden" id="personId" name="personId">
			<input type="hidden" id="type" name="type" value="${type}">
			<table>
			  <tr>
				<td width="28%" valign="top">
					<img src="${contextPath}/static/images/window.png">
					&nbsp;<span class="x_content_title">体格检查列表</span>
					<#if hasWritePermission>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
					   onclick="javascript:addNew();">新增</a>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
					   onclick="javascript:editSelected();">修改</a>  
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
					   onclick="javascript:deleteSelections();">删除</a> 
					</#if>
				</td>
				<td width="10%" valign="top">
					&nbsp;
					<select id="gradeId" name="gradeId" onchange="switchXY();">
						<option value="">--请选择--</option>
						<#list gradeInfos as grade>
						 <#if grade.deleteFlag == 0>
						  <option value="${grade.id}">${grade.name}</option>
						 </#if>
						</#list> 
					</select>
					<script type="text/javascript">
						document.getElementById("gradeId").value="${gradeId}";
					</script>
				</td>
				<td width="12%" valign="top">年份&nbsp;&nbsp;
					<select id="year" name="year" onchange="switchXY();">
						<option value="">--请选择--</option>
						<option value="2015">2015</option>
						<option value="2016">2016</option>
						<option value="2017">2017</option>
						<option value="2018">2018</option>
					</select>
					<script type="text/javascript">
						document.getElementById("year").value="${year}";
					</script>
				</td>
				<td width="12%" valign="top">月份&nbsp;&nbsp;
					<select id="month" name="month" onchange="switchXY();">
						<option value="">--请选择--</option>
						<#list months as month>
						<option value="${month}">${month}</option>
						</#list> 
					</select>
					<script type="text/javascript">
						document.getElementById("month").value="${month}";
					</script>
				</td>
				<td width="12%" valign="top">性别&nbsp;&nbsp;
					<select id="sex" name="sex" onchange="switchXY();">
						<option value="">--请选择--</option>
						<option value="0">女生</option>
						<option value="1">男生</option>
					</select>
					<script type="text/javascript">
						document.getElementById("sex").value="${sex}";
					</script>
				</td>
				<td width="26%">
				    &nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls();">统计结果</a>
					&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls55();">体格统计</a>
					&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_chart'" 
					   onclick="javascript:showChart();">图表</a>
                    <br>
					&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls60();">班级</a>
					&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls65();">全园</a>
					&nbsp;
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls70();">超重与肥胖</a>
				    &nbsp;
				    <#if personId?exists>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls2();">打印</a>
				    <#else>
				    <a id="div_person_print" style="display:none" href="#" class="easyui-linkbutton" 
					   data-options="plain:true, iconCls:'icon_export_xls'" 
					   onclick="javascript:exportXls3();">打印</a>
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