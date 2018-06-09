<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>采购参数设置</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">

	function saveData(){
		 if(confirm("确定将选中日期的食谱中需要的物品加入采购单中吗？")){
			 if(confirm("物品加入采购单中后将结单且不能再修改及删除，确实要加入采购计划并结单吗？")){
               var params = jQuery("#iForm").formSerialize();
		       jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/genDailyParchasePlan',
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
	}

</script>
</head>

<body>
<div style="margin:0;"></div>  

<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">&nbsp;采购参数设置</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >确定</a>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="fullDay" name="fullDay" value="${fullDay}"/>
  <table class="easyui-form" style="width:600px;" align="center">
    <tbody>
	<tr>
		<td width="20%" align="left">计划日期</td>
		<td align="left">
             ${fullDay}
		</td>
	</tr>
	<tr>
		<td width="20%" align="left">食部系数</td>
		<td align="left">
          <select id="radicalFlag" name="radicalFlag">
			 <option value="">----请选择----</option>
			 <option value="1">直接按已经去皮量采购</option>
			 <option value="2">按可食部换算后采购</option>
		  </select>
		  <script type="text/javascript">
		      document.getElementById("radicalFlag").value="${radicalFlag}";
		  </script>
		</td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>