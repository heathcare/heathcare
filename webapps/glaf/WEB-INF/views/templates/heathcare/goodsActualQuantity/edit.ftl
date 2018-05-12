<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>实际用量表</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript" src="${contextPath}/static/scripts/heathcare.js"></script>
<script type="text/javascript">
    var contextPath="${contextPath}";

	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		if(document.getElementById("goodsId").value==""){
			document.getElementById("nodeId").focus();
			alert("请选择物品。");
			return;
		}

		if(document.getElementById("unit").value==""){
			document.getElementById("unit").focus();
			alert("请选择计量单位。");
			return;
		}

		if(document.getElementById("quantity").value==""){
			document.getElementById("quantity").focus();
			alert("请填写使用数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("使用数量必须大于0。");
			return;
		}

		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsActualQuantity/saveGoodsActualQuantity',
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
					       window.parent.location.reload();
					   } 
				   }
			 });
	}

	function saveAsData(){
		document.getElementById("id").value="";
		if(document.getElementById("goodsId").value==""){
			document.getElementById("nodeId").focus();
			alert("请选择物品。");
			return;
		}

		if(document.getElementById("unit").value==""){
			document.getElementById("unit").focus();
			alert("请选择计量单位。");
			return;
		}

		if(document.getElementById("quantity").value==""){
			document.getElementById("quantity").focus();
			alert("请填写使用数量。");
			return;
		}

		if(document.getElementById("quantity").value*1.0 <= 0.0){
			document.getElementById("quantity").focus();
			alert("使用数量必须大于0。");
			return;
		}

        if(document.getElementById("price").value != ""){
			if(document.getElementById("price").value*1.0 <= 0.0){
				document.getElementById("price").focus();
				alert("单价必须大于0。");
				return;
			}
		} else {
			alert("单价必须大于0。");
			return;
		}

        if(document.getElementById("totalPrice").value != ""){
			if(document.getElementById("totalPrice").value*1.0 <= 0.0){
				document.getElementById("totalPrice").focus();
				alert("总价必须大于0。");
				return;
			}
		} else {
			alert("总价必须大于0。");
			return;
		}

		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsActualQuantity/saveGoodsActualQuantity',
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
					       window.parent.location.reload();
					   }
				   }
			 });
	}

   function audit(){
	   if(confirm("审核通过后不能再修改数据，确定通过吗？")){
		 var params = jQuery("#iForm").formSerialize();
		 jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsActualQuantity/audit',
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
					       window.parent.location.reload();
					   } 
				   }
			 });
	   }
	}

	<#if canCancelAudit == true>
	function cancelAudit(){
	    if(confirm("确实要撤销已经审核过的数据吗？")){
		 var params = jQuery("#iForm").formSerialize();
		 jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/goodsActualQuantity/cancelAudit',
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
					       window.parent.location.reload();
					   } 
				   }
			 });
	     }
	}
	</#if>

	function calMTotal(){
		//alert("");
        var quantity = document.getElementById("quantity").value*1.0;
		var price = document.getElementById("price").value*1.0;
		if(quantity*1.0 > 0 && price*1.0 > 0){
			var total = quantity * price;
			//alert("total:" + total);
			//alert(document.getElementById("totalPrice").value);
			total = Math.round(total * 100.00) / 100.00;
            document.getElementById("totalPrice").value=total;
			//jQuery("#totalPrice").val(total);
			//document.getElementById("cal_totalPrice").html(total);
			//document.getElementById("totalPrice").text=total;
			//alert(document.getElementById("totalPrice").value);
		}
	}

   function searchGoods() {
	    var link = '${contextPath}/heathcare/foodComposition/goodslist';
        var x=80;
        var y=80;
        if(is_ie) {
        	x=document.body.scrollLeft+event.clientX-event.offsetX-200;
        	y=document.body.scrollTop+event.clientY-event.offsetY-200;
        }
        openWindow(link, self, x, y, 980, 500);
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<img src="${contextPath}/static/images/window.png">&nbsp;<span class="x_content_title">&nbsp;编辑实际用量表</span>
	<#if audit == true>
	  <#if goodsActualQuantity.businessStatus == 0>
	   <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	      onclick="javascript:audit();" >确认通过</a> 
	  <#elseif goodsActualQuantity.businessStatus == 9>
	  &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
	      <#if canCancelAudit == true>
	       <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-audit'" 
	           onclick="javascript:cancelAudit();" >取消审核</a>
		  </#if>
	  </#if>
	<#else>
	  <#if goodsActualQuantity.businessStatus == 9>
	    &nbsp;<span style="font:bold 13px 宋体; color:#ff0000;">已审核</span>&nbsp;
	      <#if canCancelAudit == true>
	       <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-audit'" 
	           onclick="javascript:cancelAudit();" >取消审核</a>
		  </#if>
	  <#else>
	  <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	     onclick="javascript:saveData();" >保存</a> 
	  </#if>
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="id" name="id" value="${goodsActualQuantity.id}">
  <input type="hidden" id="goodsName" name="goodsName" value="${goodsActualQuantity.goodsName}">
  <table class="easyui-form" style="width:850px;" align="center">
    <tbody>
	<tr>
		<td width="15%" align="left">物品名称</td>
		<td align="left">
			<select id="nodeId" name="nodeId" onchange="javascript:switchFood();">
				<option value="0">----请选择----</option>
				<#list foodCategories as tree>
				<option value="${tree.id}">${tree.name}</option>
				</#list> 
			</select>
			<script type="text/javascript">
			   document.getElementById("nodeId").value="${nodeId}";
			</script>
			&nbsp;
			<select id="goodsId" name="goodsId" onchange="chooseGoodsName();" >
				<option value="">----请选择----</option>
				<#list foods as f>
				<option value="${f.id}">${f.name}</option>
				</#list> 
			</select>
			<script type="text/javascript">
			   document.getElementById("goodsId").value="${goodsActualQuantity.goodsId}";
			</script>
			&nbsp;
		    <a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	           onclick="javascript:searchGoods();">查找</a>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">使用日期</td>
		<td align="left">
             <input id="usageTime" name="usageTime" type="text" class="easyui-datebox x-text" value="${usageTime}" style="width:100px">
			 &nbsp;周次&nbsp;
			 <select id="week" name="week">
				<#list weeks as week>
				<option value="${week}">${week}</option>
				</#list>
			  </select>
			  <script type="text/javascript">
				  document.getElementById("week").value="${nowWeek}";
			  </script>
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">计量单位</td>
		<td align="left">
            <select id="unit" name="unit">
			    <option value="KG" selected>千克</option>
				<option value="L">升</option>
				<option value="O">其他</option>
			</select>
			<script type="text/javascript">
				document.getElementById("unit").value="${goodsActualQuantity.unit}";
			</script> 
			(提示：1千克=1000克 1克=0.001千克，计量单位请统一换算成千克或升)
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">数量</td>
		<td align="left" colspan="3">
			<input id="quantity" name="quantity" type="text" style="width:60px;text-align:right;"
			       class=" x-text" precision="2" onkeyup="javascript:calMTotal();"
				   value="${goodsActualQuantity.quantity}"/>
		    &nbsp;单价&nbsp; 
			<input id="price" name="price" type="text" style="width:60px;text-align:right;"
			       class=" x-text" precision="2" onkeyup="javascript:calMTotal();"
				   value="${goodsActualQuantity.price}"/>
	        &nbsp;总价格&nbsp; 
			<input id="totalPrice" name="totalPrice" type="text" style="width:80px;text-align:right;"
			       class=" x-text" precision="2"
				   value="${goodsActualQuantity.totalPrice}"/>
			&nbsp;<span id="cal_totalPrice"></span>
			<input type="button" value="计算" onclick="javascript:calMTotal();" class="btnGrayMini">
			&nbsp;
			<input type="button" value="转换" onclick="mycal();" class="btnGray">
		</td>
	</tr>
	<tr>
		<td width="15%" align="left">备注</td>
		<td align="left">
		    <textarea id="remark" name="remark" type="text" 
			          class="easyui-validatebox  x-text"  
			          style="width:350px;height:120px">${goodsActualQuantity.remark}</textarea>
		</td>
	</tr>
    </tbody>
  </table>
  </form>
</div>
</div>
</body>
</html>