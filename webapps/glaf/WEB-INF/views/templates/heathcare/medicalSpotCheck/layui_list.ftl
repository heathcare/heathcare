<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>体格抽样检查</title>
<#include "/inc/init_layui_import.ftl"/>
<script type="text/javascript">
    
	var layer = layui.layer;

    function getLink(){
	    var link_ = "${request.contextPath}/heathcare/medicalSpotCheck/json?q=1&uiType=layui";
		var checkId = jQuery("#checkId").val();

		if(checkId != "" && checkId != undefined){
           link_ = link_+"&checkId="+checkId;
	    }
		
		if(jQuery("#cityLike").val() != "" && jQuery("#cityLike").val() != undefined){
           link_ = link_+"&cityLike="+jQuery("#cityLike").val();
	    }

		if(jQuery("#areaLike").val() != "" && jQuery("#areaLike").val() != undefined){
           link_ = link_+"&areaLike="+jQuery("#areaLike").val();
	    }

		if(jQuery("#nationLike").val() != "" && jQuery("#nationLike").val() != undefined){
           link_ = link_+"&nationLike="+jQuery("#nationLike").val();
	    }

		if(jQuery("#organizationLike").val() != "" && jQuery("#organizationLike").val() != undefined){
           link_ = link_+"&organizationLike="+jQuery("#organizationLike").val();
	    }

		if(jQuery("#sex").val() != "" && jQuery("#sex").val() != undefined){
           link_ = link_+"&sex="+jQuery("#sex").val();
	    }

		if(jQuery("#heightLevel").val() != null && jQuery("#heightLevel").val() != ""
		    && jQuery("#heightLevel").val() != undefined){
           link_ = link_+"&heightLevel="+jQuery("#heightLevel").val();
	    }

		if(jQuery("#weightLevel").val() != null && jQuery("#weightLevel").val() != "" 
		    && jQuery("#weightLevel").val() != undefined){
           link_ = link_+"&weightLevel="+jQuery("#weightLevel").val();
	    }
		//alert(link_);
		return link_;
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
			layer.alert("请选择一个主题！");
			return false;
		}

		if(sex == ""){
			layer.alert("请选择男生或女生！");
			return;
		}

        if(checkType == ""){
			layer.alert("请选择导出类型！");
			return;
		}
		
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotal&useExt=Y&checkType="+checkType;
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike;
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike;
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike;
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike;
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
			layer.alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			layer.alert("请选择男生或女生！");
			return;
		}

        if(checkType == ""){
			layer.alert("请选择导出类型！");
			return;
		}
		
		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotal5&useExt=Y&checkType="+checkType;
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike;
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike;
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike;
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike;
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
			layer.alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			layer.alert("请选择男生或女生！");
			return;
		}

		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotalV35&useExt=Y";
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike;
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike;
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike;
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike;
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
			layer.alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			layer.alert("请选择男生或女生！");
			return;
		}

		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotalV42&useExt=Y";
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike;
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike;
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike;
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike;
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
			layer.alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			layer.alert("请选择男生或女生！");
			return;
		}

		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotalV5&useExt=Y";
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike;
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike;
		}
		if(nationLike != ""){
			link = link  + "&nationLike=" + nationLike;
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
			layer.alert("请选择一个主题！");
			return;
		}

		if(sex == ""){
			layer.alert("请选择男生或女生！");
			return;
		}

		var link="${contextPath}/heathcare/reportMain/exportXls?reportId=MedicalSpotCheckTotalV6&useExt=Y";
		link = link + "&sex="+sex+"&time="+getNowFormatDate()+"&megerFlag=Y&checkId="+checkId;

		if(organizationLike != ""){
			link = link + "&organizationLike=" + organizationLike;
		}
		if(cityLike != ""){
			link = link  + "&cityLike=" + cityLike;
		}
		if(areaLike != ""){
			link = link  + "&areaLike=" + areaLike;
		}

        window.open(link);
	}

    function showImportXy(){
		var checkId = jQuery("#checkId").val();
        var link='${request.contextPath}/heathcare/medicalSpotCheck/showImport?type=spotM&checkId='+checkId;
		/**
		layui.use('layer', function(){
		  //var layer = layui.layer;
		  layer.open({
			  type: 2,
			  maxmin: false,
			  shadeClose: true,
			  title: "导入数据",
			  area: ['680px', (jQuery(window).height() - 50) +'px'],
			  shade: 0.8,
			  fixed: false, //不固定
			  content: [link, 'no']
			});
		});
		**/
		openMiniWin(link, this);
	}

	function doSearch(){
         document.iForm.submit();
	}

</script>
</head>
<body style="margin-top:2px;">
<form id="iForm" name="iForm" method="post" action="">
<table>
 <tr style="margin-top:2px; height:38px; line-height:38px; ">
	<td align="left">
	    &nbsp;<img src="${request.contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">体检信息列表</span>
	</td>
	<td align="left">
	    &nbsp;<select id="checkId" name="checkId" onchange="javascript:doSearch();">
					<option value="" selected>----请选择----</option>
					<#list examDefs as row>
					<option value="${row.checkId}">${row.title}</option>
					</#list>
				</select>
				<script type="text/javascript">
				     document.getElementById("checkId").value="${checkId}";
				</script>
	</td>
	<td align="left">
	    &nbsp;<button class="layui-btn layui-btn-sm" onclick="javascript:showImportXy();">导入</button>
	</td>
	<td align="left">
	    &nbsp;<button class="layui-btn layui-btn-sm" onclick="javascript:execute('deviation');">按离差法统计</button>
	</td>
	<td align="left">
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
	</td>
	<td align="left">
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
	</td>
	<td align="left">
	    &nbsp;
	</td>
	<td align="left">
	    &nbsp;
	</td>
	<td align="left">
	    &nbsp;
	</td>
 </tr>
 </table>
 <table>
 <tr style="margin-top:2px; height:38px; line-height:38px; ">
	<td align="left">
	    &nbsp;市&nbsp;
		<input type="text" id="cityLike" name="cityLike" value="${cityLike}" size="20" 
			class="x-search-text" style="width:80px;"></td>
	<td align="left">
	    &nbsp;区/县&nbsp;
		<input type="text" id="areaLike" name="areaLike" value="${areaLike}" size="20" 
			class="x-search-text" style="width:80px;">
	</td>
	<td>
	    &nbsp;民族&nbsp;
		<input type="text" id="nationLike" name="nationLike" value="${nationLike}" size="20" 
			class="x-search-text" style="width:60px;">
	</td>
	<td align="left">
	   &nbsp;园所&nbsp;
	   <input type="text" id="organizationLike" name="organizationLike" value="${organizationLike}" size="20" 
			  class="x-search-text" style="width:100px;">
	</td>
	<td align="left">
	   &nbsp;等级&nbsp;
	   <input type="text" id="organizationLevelLike" name="organizationLevelLike" value="${organizationLevelLike}" 
			size="20" class="x-search-text" style="width:80px;">
	</td>
	<td align="left">
	    &nbsp;性质&nbsp;
	    <input type="text" id="organizationPropertyLike" name="organizationPropertyLike" 
			value="${organizationPropertyLike}" size="20" class="x-search-text" style="width:80px;">
	</td>
	<td align="left">
	    &nbsp;地域&nbsp;&nbsp;
	    <input type="text" id="organizationTerritoryLike" name="organizationTerritoryLike" 
		    value="${organizationTerritoryLike}" size="20" class="x-search-text" style="width:80px;">
	</td>
	<td align="left">
	   &nbsp;<button class="layui-btn layui-btn-sm" onclick="javascript:doSearch();">查找</button>
	</td>
  </tr>
 </table>
 <table>
 <tr style="margin-top:2px; height:38px; line-height:38px; ">
    <td align="left">
	    &nbsp;性别&nbsp;
		<select id="sex" name="sex" onchange="javascript:doSearch();">
			<option value="">--请选择--</option>
			<option value="0">女生</option>
			<option value="1">男生</option>
		</select>
		<script type="text/javascript">
			document.getElementById("sex").value="${sex}";
		</script>
	</td>
	<td align="left">
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
	</td>
	<td align="left">
	   &nbsp;<button class="layui-btn layui-btn-sm" onclick="javascript:doExport();">导出Excel</button>
	</td>
	<td align="left">
	   &nbsp;<button class="layui-btn layui-btn-sm" onclick="javascript:doExport2();">分析表</button>
	</td>
	<td align="left">
	   &nbsp;<button class="layui-btn layui-btn-sm" onclick="javascript:doExport3();">体格评价表</button>
	</td>
	<td align="left">
	   &nbsp;<button class="layui-btn layui-btn-sm" onclick="javascript:doExport4();">体格评价统计表</button>
	</td>
	<td align="left">
	   &nbsp;<button class="layui-btn layui-btn-sm" onclick="javascript:doExport5();">体格评价统计总表</button>
	</td>
	<td align="left">
	   &nbsp;<button class="layui-btn layui-btn-sm" onclick="javascript:doExport6();">体格评价统计总表（按民族）</button>
	</td>
  </tr>
 </table> 
</form>
<table class="layui-hide" id="mydatagrid" lay-filter="mydatagrid"></table>

<script type="text/html" id="my_toolbar">
  <div class="layui-btn-container">
    <button class="layui-btn layui-btn-sm" lay-event="deleteCheckData">删除选择数据</button>
  </div>
</script>
 
<script type="text/html" id="my_function_bar">
  <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
</script>

<script type="text/html" id="switchSexTpl">
  {{#  if(d.sex === '0'){ }}
    <span style="color:#F581B1; ">女</span>
  {{#  } else { }}
   <span style="color:#0099ff; ">男</span>
  {{#  } }}
</script>

<script>
layui.use('table', function(){
  var table = layui.table;
  
  table.render({
    elem: '#mydatagrid'
	,title: '体检信息列表'
	,toolbar: '#my_toolbar'
    ,url: getLink()
    ,page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
      layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'] //自定义分页布局
      ,curr: 1 //设定初始在第 1 页
      ,groups: 5 //只显示 5 个连续页码
      ,first: false //不显示首页
      ,last: false//不显示尾页
	  ,limits: [10, 15, 20, 25, 50, 100, 200, 500, 1000]
    }
    ,cols: [[
	  {type:'checkbox'},
      {field:'startIndex', width:80, title: '序号', sort: true},
      {field:'name', width:80, title: '姓名', align:"center"},
      {field:'sex', width:80, title: '性别', align:"center", templet: '#switchSexTpl', sort: true},
      {field:'ageOfTheMoonString', width:90, title: '月龄', align:"center", sort:true},
      {title:'民族', field:'nation', width:80, sort:true},
	  {title:'身高', field:'height', width:80, sort:true, align:'right'},
	  {title:'身高评价', field:'heightEvaluateHtml', width:100, align:"center", sort:true},
	  {title:'体重', field:'weight', width:80, sort:true, align:'right'},
	  {title:'体重评价', field:'weightEvaluateHtml', width:100, align:"center", sort:true},
	  {title:'血红蛋白Hb', field:'hemoglobin', width:110, align:"center", sort:true},
	  {title:'市', field:'city', width:120, sort:true},
	  {title:'区/县', field:'area', width:120, sort:true},
	  {title:'园所名称', field:'organization', width:250, sort:true},
	  {title:'园所性质', field:'organizationProperty', width:100, align:"center", sort:true},
	  {title:'功能键', fixed: 'right', toolbar: '#my_function_bar', width:80}
    ]]
    
  });

  //头工具栏事件
  table.on('toolbar(mydatagrid)', function(obj){
    var checkStatus = table.checkStatus(obj.config.id);
    switch(obj.event){
      case 'deleteCheckData':
        var data = checkStatus.data;
		layer.confirm('数据删除后不能恢复，确定删除吗？', function(index){
		   var ids = [];
		   var len = data.length;
           for(i=0; i<len; i++){
			   ids.push(data[i].id);
		   }
		   var str = ids.join(',');
		   jQuery.ajax({
					   type: "POST",
					   url: '${request.contextPath}/heathcare/medicalSpotCheck/delete?ids='+str,
					   dataType: 'json',
					   error: function(data){
						   layer.alert('服务器处理错误！');
					   },
					   success: function(data){
						   if(data != null && data.message != null){
							   layer.alert(data.message);
						   } else {
							   layer.alert('操作成功完成！');
						   }
						   if(data.statusCode == 200){
							    document.iForm.submit();
						   }
					   }
				 });
		  });
      break;
      case 'getCheckLength':
       
      break;
      case 'isAll':
      break;
    };
  });

  //监听行工具事件
  table.on('tool(mydatagrid)', function(obj){
    var data = obj.data;
    //console.log(obj)
    if(obj.event === 'del'){
      layer.confirm('数据删除后不能恢复，确定删除吗？', function(index){
	    var str = obj.data.id;
	    jQuery.ajax({
				   type: "POST",
				   url: '${request.contextPath}/heathcare/medicalSpotCheck/delete?ids='+str,
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   layer.alert(data.message);
					   } else {
						   layer.alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
					       obj.del();
                           layer.close(index);
					   }
				   }
			 });
      });
    } else if(obj.event === 'edit'){
      
    }
  });

});
</script>
</body>
</html>