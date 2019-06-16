<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>体格抽样检查</title>
<#include "/inc/init_easyui_layer3_import.ftl"/>
<script type="text/javascript">

    function getLink(){
	    var link_ = "${request.contextPath}/heathcare/medicalSpotCheck/json?q=1";
		var checkId = jQuery("#checkId").val();
		link_ = link_+"&checkId="+checkId;

		if(jQuery("#cityLike").val() != ""){
           link_ = link_+"&cityLike="+jQuery("#cityLike").val().trim();
	    }
		if(jQuery("#areaLike").val() != ""){
           link_ = link_+"&areaLike="+jQuery("#areaLike").val().trim();
	    }
		if(jQuery("#nationLike").val() != ""){
           link_ = link_+"&nationLike="+jQuery("#nationLike").val().trim();
	    }
		if(jQuery("#organizationLike").val() != ""){
           link_ = link_+"&organizationLike="+jQuery("#organizationLike").val().trim();
	    }
		if(jQuery("#sex").val() != ""){
           link_ = link_+"&sex="+jQuery("#sex").val();
	    }
		if(jQuery("#heightLevel").val() != null && jQuery("#heightLevel").val() != ""){
           link_ = link_+"&heightLevel="+jQuery("#heightLevel").val();
	    }
		if(jQuery("#weightLevel").val() != null && jQuery("#weightLevel").val() != ""){
           link_ = link_+"&weightLevel="+jQuery("#weightLevel").val();
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
				        {title:'序号', field:'startIndex', width:50, sortable:false},
						{title:'姓名', field:'name', width:60, sortable:true},
						{title:'性别', field:'sex', width:50, sortable:true, formatter:formatterSex},
						{title:'月龄', field:'ageOfTheMoonString', width:70, sortable:true, align:'right'},
						{title:'民族', field:'nation', width:60, sortable:true},
						{title:'身高', field:'height', width:60, sortable:true, align:'right'},
						{title:'身高评价', field:'heightEvaluateHtml', width:100, sortable:true},
						{title:'体重', field:'weight', width:60, sortable:true, align:'right'},
						{title:'体重评价', field:'weightEvaluateHtml', width:100, sortable:true},
						{title:'血红蛋白Hb', field:'hemoglobin', width:100, align:"center", sortable:true},
						{title:'市', field:'city', width:120, sortable:true},
						{title:'区/县', field:'area', width:120, sortable:true},
						{title:'园所名称', field:'organization', width:180, sortable:true},
						{title:'园所性质', field:'organizationProperty', width:90, sortable:true},
						{title:'功能键', field:'functionKey',width:90, formatter:formatterKeys}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 10,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000,2000],
				pagePosition: 'both'
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
		var str = "<a href='javascript:deleteRow(\""+row.id+"\");'>删除</a>";
	    return str;
	}
	 
	
	function deleteRow(id){
		if(confirm("数据删除后不能恢复，确定删除吗？")){
			jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/heathcare/medicalSpotCheck/delete?ids='+id,
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

 
	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','体格抽样检查查询');
	    //jQuery('#searchForm').form('clear');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
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
				   url: '${request.contextPath}/heathcare/medicalSpotCheck/delete?ids='+str,
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
                    url: '${request.contextPath}/heathcare/medicalSpotCheck/json',
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

	function showImport(){
		var checkId = jQuery("#checkId").val();
        var link='${request.contextPath}/heathcare/medicalSpotCheck/showImport?type=spotM&checkId='+checkId;
		    layer.open({
			  type: 2,
			  maxmin: true,
			  shadeClose: true,
			  title: "导入数据",
			  area: ['880px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  shadeClose: true,
			  content: [link, 'no']
			});
	}

	function deleteSelection(){
      var checkId = jQuery("#checkId").val();
      if(checkId == ""){
		  alert("请选择一个主题！");
		  return;
	  }
      if(confirm("数据删除后无法恢复，确定执行删除操作吗？")){
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/heathcare/medicalSpotCheck/deleteSubject?checkId='+checkId,
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
						   doSearch();
					       //jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  }
	}

	function execute(useMethod){
	  var checkId = jQuery("#checkId").val();
      if(checkId == ""){
		  alert("请选择一个主题！");
		  return;
	  }
	  if(confirm("确定执行统计操作吗？")){
		jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/heathcare/medicalSpotCheck/execute?useMethod='+useMethod+'&checkId='+checkId,
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
						   doSearch();
					       //jQuery('#mydatagrid').datagrid('reload');
					   }
				   }
			 });
		  }
	}

	function doSearch(){
        //document.iForm.submit();
		var link = getLink();
		//alert(link);
		loadGridData(link);
	}

    function getNowFormatDate() {
		var date = new Date();
		var seperator1 = "";
		var seperator2 = "";
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate+ "" + date.getHours() + seperator2 + date.getMinutes()+ seperator2 + date.getSeconds();
		return currentdate;
    }

	function doExport(){
		var sex = jQuery("#sex").val();
		var checkId = jQuery("#checkId").val();
		var nationLike = jQuery("#nationLike").val();
		var areaLike = jQuery("#areaLike").val();
		var cityLike = jQuery("#cityLike").val();
		var checkType = jQuery("#checkType").val();
		var organizationLike = jQuery("#organizationLike").val();
        
		if(checkId == ""){
			alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			alert("请选择男生或女生！");
			return;
		}

        if(checkType == ""){
			alert("请选择导出类型！");
			return;
		}
		
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotal&useExt=Y&checkType="+checkType;
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike.trim();
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike.trim();
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike.trim();
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike.trim();
		}

        window.open(link);
	}

	function doExport2(){
		var sex = jQuery("#sex").val();
		var checkId = jQuery("#checkId").val();
		var nationLike = jQuery("#nationLike").val();
		var areaLike = jQuery("#areaLike").val();
		var cityLike = jQuery("#cityLike").val();
		var checkType = jQuery("#checkType").val();
		var organizationLike = jQuery("#organizationLike").val();
        
		if(checkId == ""){
			alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			alert("请选择男生或女生！");
			return;
		}

        if(checkType == ""){
			alert("请选择导出类型！");
			return;
		}
		
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotal5&useExt=Y&checkType="+checkType;
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike.trim();
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike.trim();
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike.trim();
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike.trim();
		}

        window.open(link);
	}

	function doExport3(){
		var sex = jQuery("#sex").val();
		var checkId = jQuery("#checkId").val();
		var nationLike = jQuery("#nationLike").val();
		var areaLike = jQuery("#areaLike").val();
		var cityLike = jQuery("#cityLike").val();
		var organizationLike = jQuery("#organizationLike").val();
        
		if(checkId == ""){
			alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			alert("请选择男生或女生！");
			return;
		}

		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotalV35&useExt=Y";
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike.trim();
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike.trim();
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike.trim();
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike.trim();
		}

        window.open(link);
	}

	function doExport4(){
		var sex = jQuery("#sex").val();
		var checkId = jQuery("#checkId").val();
		var nationLike = jQuery("#nationLike").val();
		var areaLike = jQuery("#areaLike").val();
		var cityLike = jQuery("#cityLike").val();
		var organizationLike = jQuery("#organizationLike").val();
        
		if(checkId == ""){
			alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			alert("请选择男生或女生！");
			return;
		}

		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotalV42&useExt=Y";
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike.trim();
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike.trim();
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike.trim();
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike.trim();
		}

        window.open(link);
	}

	function doExport5(){
		var sex = jQuery("#sex").val();
		var checkId = jQuery("#checkId").val();
		var nationLike = jQuery("#nationLike").val();
		var areaLike = jQuery("#areaLike").val();
		var cityLike = jQuery("#cityLike").val();
		var organizationLike = jQuery("#organizationLike").val();
        
		if(checkId == ""){
			alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			alert("请选择男生或女生！");
			return;
		}

		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotalV5&useExt=Y";
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike.trim();
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike.trim();
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike.trim();
		}

        window.open(link);
	}

	function doExport6(){
		var sex = jQuery("#sex").val();
		var checkId = jQuery("#checkId").val();
		var nationLike = jQuery("#nationLike").val();
		var areaLike = jQuery("#areaLike").val();
		var cityLike = jQuery("#cityLike").val();
		var organizationLike = jQuery("#organizationLike").val();
        
		if(checkId == ""){
			alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			alert("请选择男生或女生！");
			return;
		}

		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotalV6&useExt=Y";
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike.trim();
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike.trim();
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike.trim();
		}

        window.open(link);
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north',split:false, border:true" style="height:125px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	 <form id="iForm" name="iForm" method="post" action="">
	  <table width="100%" align="left">
		<tbody>
		 <tr>
		    <td width="30%" align="left">
				&nbsp;<img src="${request.contextPath}/static/images/window.png">&nbsp;
				<span class="x_content_title">体检列表</span>
			    <br>&nbsp;&nbsp;&nbsp;&nbsp;
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_upload'"
				   onclick="javascript:showImport();">导入</a>  
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-sys'" 
				   onclick="javascript:execute('deviation');">按离差法统计</a>
				<br>&nbsp;&nbsp;&nbsp;&nbsp;
				<select id="checkId" name="checkId" onchange="javascript:doSearch();">
					<option value="" selected>----请选择----</option>
					<#list examDefs as row>
					<option value="${row.checkId}">${row.title}</option>
					</#list>
				</select>
				<script type="text/javascript">
				     document.getElementById("checkId").value="${checkId}";
				</script>
				&nbsp;
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-remove'"
		           onclick="javascript:deleteSelection();">删除</a> 
			</td>
			<td width="70%" align="left">
				&nbsp;市&nbsp;&nbsp;&nbsp;
				<input type="text" id="cityLike" name="cityLike" value="${cityLike}" size="20" 
					   class="x-search-text" style="width:80px;">
			    &nbsp;区/县&nbsp;
				<input type="text" id="areaLike" name="areaLike" value="${areaLike}" size="20" 
					   class="x-search-text" style="width:80px;">
			    &nbsp;民族&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="text" id="nationLike" name="nationLike" value="${nationLike}" size="20" 
					   class="x-search-text" style="width:60px;">
				&nbsp;园所&nbsp;
				<input type="text" id="organizationLike" name="organizationLike" value="${organizationLike}" size="20" 
					   class="x-search-text" style="width:100px;">
				&nbsp;等级&nbsp;
				<input type="text" id="organizationLevelLike" name="organizationLevelLike" value="${organizationLevelLike}" 
				       size="20" class="x-search-text" style="width:80px;">
			    <br>
				&nbsp;性质&nbsp;
				<input type="text" id="organizationPropertyLike" name="organizationPropertyLike" 
				       value="${organizationPropertyLike}" size="20" class="x-search-text" style="width:80px;">
				&nbsp;地域&nbsp;&nbsp;
				<input type="text" id="organizationTerritoryLike" name="organizationTerritoryLike" 
				       value="${organizationTerritoryLike}" size="20" class="x-search-text" style="width:80px;">
				&nbsp;身高评价&nbsp;
				<select id="heightLevel" name="heightLevel" onchange="javascript:doSearch();">
					<option value="-9">--请选择--</option>
					<option value="0">正常</option>
					<option value="-1">生长迟缓</option>
					<option value="-2">中度生长迟缓</option>
				</select>
				<script type="text/javascript">
					document.getElementById("heightLevel").value="${heightLevel}";
				</script>
				&nbsp;体重评价&nbsp;
				<select id="weightLevel" name="weightLevel" onchange="javascript:doSearch();">
					<option value="-9">--请选择--</option>
					<option value="0">正常</option>
					<option value="1">超重</option>
					<option value="2">肥胖</option>
					<option value="3">重度肥胖</option>
					<option value="-1">偏瘦</option>
					<option value="-2">低体重</option>
					<option value="-3">消瘦</option>
				</select>
				<script type="text/javascript">
					document.getElementById("weightLevel").value="${weightLevel}";
				</script>
				&nbsp;&nbsp;
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
				   onclick="javascript:doSearch();">查找</a>
				<br>
				&nbsp;性别&nbsp;
				<select id="sex" name="sex" onchange="javascript:doSearch();">
					<option value="">--请选择--</option>
					<option value="0">女生</option>
					<option value="1">男生</option>
				</select>
				<script type="text/javascript">
					document.getElementById("sex").value="${sex}";
				</script>
				&nbsp;导出类型&nbsp;
				<select id="checkType" name="checkType">
					<option value="">--请选择--</option>
					<option value="H/A">H/A年龄别身高</option>
					<option value="W/A">W/A年龄别体重</option>
					<!-- <option value="W/H">W/H身高别体重</option> -->
				</select>
				<script type="text/javascript">
					document.getElementById("checkType").value="${checkType}";
				</script>
				<!-- <select id="useMethod" name="useMethod" onchange="javascript:doSearch();">
					<option value="">--请选择--</option>
					<option value="deviation">离差法</option>
					<option value="prctile">百分位法</option>
				</select>
				<script type="text/javascript">
					document.getElementById("useMethod").value="${useMethod}";
				</script> -->
				&nbsp;&nbsp;
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'"
				   onclick="javascript:doExport();">导出</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'"
				   onclick="javascript:doExport2();">分析表</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'"
				   onclick="javascript:doExport3();">体格评价表</a>
				<br>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'"
				   onclick="javascript:doExport4();">体格评价统计表</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'"
				   onclick="javascript:doExport5();">体格评价统计总表</a>
				<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon_export_xls'"
				   onclick="javascript:doExport6();">体格评价统计总表（按民族）</a>
			</td>
		</tr>
	   </tbody>
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