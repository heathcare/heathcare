<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应商信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
   var contextPath = "${contextPath}";

   jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:1000,
				height:480,
				fit:true,
				fitColumns: true,
				nowrap: false,
				striped: true,
				collapsible: true,
				url: '${contextPath}/supplier/json?property=${property}&keywordLike_enc=${keywordLike_enc}',
				remoteSort: false,
				singleSelect: true,
				idField: 'id',
				columns:[[
				        {title:'选择',field: 'chk', width: 60, align: 'center', formatter: formatterKey},
				        {title:'序号', field:'startIndex', width:60, sortable:false},
						{title:'供应商名称', field:'name', width:220, sortable:true},
						{title:'供应商简称', field:'shortName', width:120, sortable:true},
						{title:'供应商代码', field:'code', width:120, sortable:true},
						{title:'性质', field:'property', width:100, sortable:true, formatter:formatterProperty},
						{title:'负责人', field:'principal', width:90, sortable:true},
						{title:'电话', field:'telephone', width:90, sortable:true},
						{title:'创建人', field:'createBy', width:90, sortable:true},
						{title:'创建日期', field:'createTime', width:90, sortable:true},
						{title:'是否有效', field:'locked', width:90, formatter:formatterStatus}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200,500,1000],
				pagePosition: 'bottom'
			});

			var p = jQuery('#mydatagrid').datagrid('getPager');
			jQuery(p).pagination({
				onBeforeRefresh:function(){
					//alert('before refresh');
				}
		    });
	});

 
	function formatterProperty(val, row){
        if(val == "Public"){
			return "国营企业";
		} else if(val == "Private"){
			return "私营企业";
		}  
		return "其他";
	}

	function formatterStatus(val, row){
        if(val == 0){
			return "<font color='green'>有效</font>";
		}
		return "<font color='red'>无效</font>";
	}


	function formatterKey(value, row, index) {
		var s = '<input name="isCheck" type="radio" onclick="javascript:selectedRx(\''+row.supplierId+'\',\''+row.name+'\')"/> ';
		return s;
	}

    function selectedRx(supplierId, name){
		document.getElementById("supplierId").value = supplierId;
		document.getElementById("supplier").value = name;
		//choose();
	}


	function choose(){
		var parent_window = getOpener();
        var supplierId = parent_window.document.getElementById("supplierId");
		//var name = parent_window.document.getElementById("name");
		supplierId.value = document.getElementById("supplierId").value;
		//setTimeout(parent_window.switchFood(), 3000 );
        //parent_window.switchFood();
		var supplier = parent_window.document.getElementById("supplier");
		supplier.value = document.getElementById("supplier").value;
	 
		window.close();
	}


	function searchWin(){
	    jQuery('#dlg').dialog('open').dialog('setTitle','供应商信息查询');
	    //jQuery('#searchForm').form('clear');
	}

	function resize(){
		jQuery('#mydatagrid').datagrid('resize', {
			width:800,
			height:400
		});
	}

 
	function viewSelected(){
		var rows = jQuery('#mydatagrid').datagrid('getSelections');
		if(rows == null || rows.length !=1){
			alert("请选择其中一条记录。");
			return;
		}
		var selected = jQuery('#mydatagrid').datagrid('getSelected');
		if (selected ){
		    var link='${contextPath}/supplier/edit?supplierId='+selected.id;
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
			area: ['1080px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		   });
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
        var params = jQuery("#searchForm").formSerialize();
        jQuery.ajax({
                    type: "POST",
                    url: '${contextPath}/supplier/json',
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
	
	function searchData(){
        document.iForm.submit();
	}	 
</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:48px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<form id="iForm" name="iForm" method="post" action="">
	<input type="hidden" id="supplierId" name="supplierId" value="">
	<input type="hidden" id="supplier" name="supplier" value="">
    <table>
      <tr>
	    <td>
			<img src="${contextPath}/static/images/window.png">
			&nbsp;<span class="x_content_title">供应商信息列表</span>
	    </td>
		<td>
		  名称&nbsp;<input id="keywordLike" name="keywordLike" type="text" 
			       class="easyui-validatebox  x-searchtext" style="width:200px;"
				   value="${keywordLike}">
		</td>
		<td>
		  性质&nbsp;
		  <select id="property" name="property">
			    <option value="">----请选择----</option>
				<option value="Public">国营企业</option>
				<option value="Private">私营企业</option>
				<option value="Other">其他</option>
			</select>
            <script type="text/javascript">
                document.getElementById("property").value="${property}";
            </script>
		</td>
		<td>
		    <button type="button" id="searchButton" class="btn btnGrayMini" style="width:60px" 
	                onclick="javascript:searchData();">查找</button>
			<button type="button" id="okButton" class="btn btnGrayMini" style="width: 90px" 
	                onclick="javascript:choose();">确定</button>
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