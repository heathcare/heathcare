<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>学生信息</title>
<#include "/inc/init_easyui_import.ftl"/>
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
		    editRow(treeNode.id);
		} else {
			jQuery("#gradeId").val(treeNode.id);
			loadData('${contextPath}/heathcare/person/json?gradeId='+treeNode.id);
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


	function getLink(){
		var link_ = "${contextPath}/heathcare/person/json?nameLike_enc=${nameLike_enc}&namePinyinLike=${namePinyinLike}";
        if(jQuery("#nodeId").val() != ""){
            link_ = link_ + "&gradeId="+jQuery("#nodeId").val();
	    } else {
            link_ = link_ + "&gradeId=${gradeId}";
		}
		var namePinyinLike = jQuery("#namePinyinLike").val();
		if(namePinyinLike != ""){
		    link_ = link_ + "&namePinyinLike="+namePinyinLike;
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
				    {title:'序号', field:'startIndex', width:50, sortable:false},
					{title:'姓名', field:'name', width:80, sortable:true},
					{title:'性别', field:'sex', width:60, sortable:true, formatter:formatterSex, align:"center"},
					{title:'籍贯', field:'birthPlace', width:100, sortable:true},
					{title:'家庭住址', field:'homeAddress', width:280, sortable:true},
					{title:'出生日期', field:'birthday', width:90, align:"center", sortable:true},
					{title:'入园日期', field:'joinDate', width:90, align:"center", sortable:true},
					{title:'功能键', field:'functionKey', width:190, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 50,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'bottom',
				onDblClickRow: onRowClick 
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

	function formatterKeys(val, row){
		var str = "";
		<#if privilege_write == true>
		str = str+"<a href='javascript:editRow(\""+row.id+"\");'>修改</a>&nbsp;<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>&nbsp;<a href='javascript:editLinkman(\""+row.id+"\");'>联系人</a>&nbsp;<a href='javascript:examList(\""+row.id+"\");'>体检信息</a>";
	    </#if>
		return str;
	}

	function addNew(){
        var gradeId = jQuery("#gradeId").val();
	    var link="${contextPath}/heathcare/person/edit?gradeId="+gradeId;
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

	function editLinkman(personId){
	    var link="${contextPath}/heathcare/personLinkman?personId="+personId;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "联系人信息",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['980px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	}

	function examList(personId){
	    var link="${contextPath}/heathcare/medicalExamination/examList?personId="+personId;
		jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "体检信息",
			closeBtn: [0, true],
			shade: [0.82, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['1150px', (jQuery(window).height() - 30) +'px'],
            iframe: {src: link}
		});
	}

	function onRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/person/edit?id='+row.id;
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
	    var link = '${contextPath}/heathcare/person/edit?id='+id;
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

	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/person/delete?id='+id,
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

   function sicknessList(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    var link = '${contextPath}/heathcare/personSickness?gradeId=${gradeId}&personId='+selected.id;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "疾病信息列表",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['1080px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
		}
	}

	function triphopathiaList(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    var link = '${contextPath}/heathcare/triphopathia?gradeId=${gradeId}&personId='+selected.id;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "营养性疾病列表",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['1080px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
		}
	}

	function checkList(){
        var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    var link = '${contextPath}/heathcare/medicalExamination?gradeId=${gradeId}&personId='+selected.id;
			jQuery.layer({
				type: 2,
				maxmin: true,
				shadeClose: true,
				title: "体检信息列表",
				closeBtn: [0, true],
				shade: [0.8, '#000'],
				border: [10, 0.3, '#000'],
				offset: ['20px',''],
				fadeIn: 100,
				area: ['1080px', (jQuery(window).height() - 50) +'px'],
				iframe: {src: link}
			});
		}
	}

	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','学生信息查询');
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
		  var link = '${contextPath}/heathcare/person/edit?id='+selected.id;
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
			area: ['680px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
	    }
	}

	function payList(){
        var rows = jQuery('#mydatagrid').datagrid('getSelections');
	    if(rows == null || rows.length !=1){
		  alert("请选择其中一条记录。");
		  return;
	    }
	    var selected = jQuery('#mydatagrid').datagrid('getSelected');
	    if (selected ){
		  var link = '${contextPath}/heathcare/personPayment?personId='+selected.id;
		  jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "缴费记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['1080px', (jQuery(window).height() - 50) +'px'],
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
		    var link='${contextPath}/heathcare/person/edit?readonly=true&id='+selected.id;
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
			area: ['680px', (jQuery(window).height() - 50) +'px'],
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
				   url: '${contextPath}/heathcare/person/delete?ids='+str,
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

	function searchData(){
        var params = jQuery("#iForm").formSerialize();
        jQuery.ajax({
                    type: "POST",
                    url: '${contextPath}/heathcare/person/json?gradeId=${gradeId}',
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

 <#if privilege_export == true>
	function doExport1(){
		var gradeId = jQuery("#gradeId").val();
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=PersonExport&gradeId="+gradeId;
        window.open(link);
	}
</#if>

	function switchXY(){
       document.iForm.submit();
	}
	
	function searchXY(namePinyinLike){
		document.getElementById("namePinyinLike").value=namePinyinLike;
        var nameLike = document.getElementById("nameLike").value;
        var link = "${contextPath}/heathcare/person/json?nameLike="+nameLike+"&namePinyinLike="+namePinyinLike;
		//window.location.href=link;
		loadGridData(link);
	}

</script>
</head>
<body>  
<input type="hidden" id="nodeId" name="nodeId" value="" >
<input type="hidden" id="gradeId" name="gradeId" value="" >
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true, border:true">  
    <div data-options="region:'west', split:true" style="width:188px;">
	  <div class="easyui-layout" data-options="fit:true">  
		  <div data-options="region:'north', border:false" class="search-bar" style="margin:2px;height:28px">  
            <input id="keyword" name="keyword" type="text" placeholder="请输入..." class="x-small-text" style="width:110px;"> 
			<input id="search-bt" type="button" value="查找" class="btnGrayMini" onclick="javascript:searchKeyWords();">
          </div>  
		  <div data-options="region:'center', border:false">
			  <ul id="myTree" class="ztree"></ul>  
		  </div>				 
        </div>  
	</div> 
	
    <div data-options="region:'center'">  
        <div class="easyui-layout" data-options="fit:true, border:true">  
          <div data-options="region:'center', split:true, border:true, fit:true">
		   <div data-options="region:'north', split:true, border:true" style="height:68px" class="toolbar-backgroud"> 
		    <div>
			<form id="iForm" name="iForm" method="post" action="">
			<table>
			  <tr>
				<td>
					&nbsp;<img src="${contextPath}/static/images/window.png">
					&nbsp;<span class="x_content_title">学生信息列表</span>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-add'" 
					   onclick="javascript:addNew();">新增</a>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-edit'"
					   onclick="javascript:editSelected();">修改</a>  
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
					   onclick="javascript:deleteSelections();">删除</a>
					<#if privilege_export == true>
					<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'"
	                   onclick="javascript:doExport1();">导出</a>
					</#if>
				</td>
				<td> 
				    <input id="nameLike" name="nameLike" type="text" class="x-searchtext"
				           style="width:125px;" value="${nameLike}">
				</td>
				<td> 
				    <button type="button" id="searchButton" class="btn btnGrayMini" style="width:60px" 
	                        onclick="javascript:searchData();">查找</button>
				</td>
			 </tr>
			 <tr>
				<td colspan="3">
				    <input type="hidden" id="namePinyinLike" name="namePinyinLike">
					&nbsp;&nbsp;&nbsp;&nbsp;
					<#list charList as item>
					<span class="x_char_name" onclick="javascript:searchXY('${item}');">${item}</span>&nbsp;&nbsp;
					</#list>
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