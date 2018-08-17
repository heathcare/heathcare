<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食物成分表</title>
<#include "/inc/init_easyui_import.ftl"/>
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

    function getLink(){
	    var link_ = "${contextPath}/heathcare/foodComposition/json?wordLike_enc=${wordLike_enc}";
		var typeId = jQuery("#typeId").val();
		if(typeId != ""){
		    link_ = link_ + "&typeId="+typeId;
		}
		var namePinyinLike = jQuery("#namePinyinLike").val();
		if(namePinyinLike != ""){
		    link_ = link_ + "&namePinyinLike="+namePinyinLike;
		}
		return link_;
    }

    jQuery(function(){
		jQuery('#mydatagrid').datagrid({
				width:x_width,
				height:x_height,
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
						{title:'选择',field: 'chk', width:60, align: 'center', formatter: formatterKey},
				        {title:'序号', field:'startIndex', width: 60, sortable:false},
						{title:'名称',field:'name', width:180, formatter:formatterName, align:"left", sortable:true},
						{title:'别名',field:'alias', width:180, sortable:true},
						{title:'热能(千卡)',field:'heatEnergy', width:90, align:"right", sortable:true},
						{title:'蛋白质(克)',field:'protein', width:90, align:"right", sortable:true},
						{title:'脂肪(克)',field:'fat', width:90, align:"right", sortable:true},
						{title:'碳水化合物(克)',field:'carbohydrate', width:120, align:"right", sortable:true},
						{title:'钙(毫克)',field:'calcium', width:90, align:"right", sortable:true},
						{title:'铁(毫克)',field:'iron', width:90, align:"right", sortable:true},
						{title:'锌(毫克)',field:'zinc', width:90, align:"right", sortable:true},
						{title:'碘(毫克)',field:'iodine', width:90, align:"right", sortable:true},
						{title:'磷(毫克)',field:'phosphorus', width:90, align:"right", sortable:true}
				]],
				rownumbers: false,
				pagination: true,
				pageSize: 100,
				pageList: [10,15,20,25,30,40,50,100,200],
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
        var str = "<a href='javascript:viewRow(\""+row.id+"\");' title='"+row.description+"'>"+val+"</a>";
	    return str;
	}

	function formatterKey(value, row, index) {
		var s='<input name="isCheck" type="radio" onclick="javascript:selectedRx(\''+row.id+'\',\''+row.nodeId+'\',\''+row.name+'\')"/> ';
		return s;
	}

    function choose(){
		var parent_window = getOpener();
        var nodeId = parent_window.document.getElementById("nodeId");
		var name = parent_window.document.getElementById("name");
		nodeId.value = document.getElementById("nodeId").value;
		//setTimeout(parent_window.switchFood(), 3000 );
        //parent_window.switchFood();
		var foodId = parent_window.document.getElementById("foodId");
		foodId.value = document.getElementById("foodId").value;
		//alert(document.getElementById("foodId").value);
		parent_window.switchFood2(document.getElementById("foodId").value, document.getElementById("name").value);
		if(name != null){
		  name.value = document.getElementById("name").value;
		  //alert(name.value);
		}
		window.close();
	}

    function selectedRx(x_id, x_nodeId, x_name){
		document.getElementById("foodId").value = x_id;
		document.getElementById("nodeId").value = x_nodeId
	    document.getElementById("name").value = x_name;
	}

	function onMyRowClick(rowIndex, row){
	    var link = '${contextPath}/heathcare/foodComposition/edit?id='+row.id;
	    jQuery.layer({
			type: 2,
			maxmin: true,
			shadeClose: true,
			title: "查看记录",
			closeBtn: [0, true],
			shade: [0.8, '#000'],
			border: [10, 0.3, '#000'],
			offset: ['20px',''],
			fadeIn: 100,
			area: ['698px', (jQuery(window).height() - 50) +'px'],
            iframe: {src: link}
		});
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
       document.iForm.submit();
	}

	function searchXY(nameLike){
		jQuery("#namePinyinLike").val(nameLike);
		var typeId = document.getElementById("typeId").value;
        var link = "${contextPath}/heathcare/foodComposition/jsonAll?typeId="+typeId+"&namePinyinLike="+nameLike;
		loadGridData(link);
	}

</script>
</head>
<body style="margin:1px;">  
<div style="margin:0;"></div>  
<div class="easyui-layout" data-options="fit:true">  
   <div data-options="region:'north', split:false, border:true" style="height:68px" class="toolbar-backgroud"  > 
    <div style="margin:4px;"> 
	<form id="iForm" name="iForm" method="post" action="">
	<input type="hidden" id="nodeId" name="nodeId" value="">
	<input type="hidden" id="foodId" name="foodId" value="">
	<input type="hidden" id="name" name="name" value="">
    <table>
    <tr>
	    <td>
		  <img src="${contextPath}/static/images/window.png"> &nbsp;<span class="x_content_title">食物名称列表</span>
		</td>

		<td>
		    &nbsp;大类&nbsp;
			<select id="typeId" name="typeId">
				<option value="">----请选择----</option>
				<#list foodCategories as cat>
				<option value="${cat.id}">${cat.name}</option>
				</#list> 
		    </select>
		    <script type="text/javascript">
			   document.getElementById("typeId").value="${typeId}";
		    </script>
		</td>

		<td>
		   名称&nbsp;
		   <input id="wordLike" name="wordLike" type="text" class="x-searchtext"  
	              style="width:125px;" value="${wordLike}">
		</td>

		<td>
		    <button type="button" id="searchButton" class="btn btnGrayMini" style="width: 90px" 
	                onclick="javascript:searchData();">查找</button>
		    <button type="button" id="okButton" class="btn btnGrayMini" style="width: 90px" 
	                onclick="javascript:choose();">确定</button>
		</td>
     </tr>
	 <tr>
		<td colspan="5">
			<input type="hidden" id="namePinyinLike" name="namePinyinLike" value="${namePinyinLike}">
			&nbsp;&nbsp;&nbsp;&nbsp;
			<#list charList as item>
			&nbsp;<span class="x_char_name" onclick="javascript:searchXY('${item}');">${item}</span>&nbsp;
			</#list>
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