<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>食物成分构成</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript">

    jQuery(function(){
		jQuery("#startTime").val(jQuery.cookie("startTime"));
        jQuery("#endTime").val(jQuery.cookie("endTime"));
	});

	function showExport(){
        var startTime = jQuery("#startTime").val();
        var endTime = jQuery("#endTime").val();
		if(startTime > endTime){
			alert("开始时间不能大于结束时间。");
			return;
		}
		var link="${contextPath}/heathcare/reportMain/exportXls?tenantId=${tenantId}&reportId=WeeklyFoodNutritionCount&ts=${ts}&outputFormat=html";
		if(startTime != ""){
			link = link + "&startDate=" + startTime;
		}
		if(endTime != ""){
			link = link  + "&endDate="+endTime;
		}
		var x_height = $(window).height() -5;
        var x_width = $(window).width() - 5 ;
        x_width = '100%';
		//location.href=link;	
		var link2='<iframe id="newFrame2" name="newFrame2" scrolling="yes" frameborder="0" width="'+x_width+'" height="'+x_height+'" src="'+link+'"></iframe>';
		jQuery("#mainContent").html(link2);
	}
</script>
</head>
<body style="margin-left:5px;overflow:hidden;">
  <table height="100%" width="100%" style="height:60px;">
	<tbody>
	   <tr>
		 <td align="top" height="35"> 
	      &nbsp;时间段&nbsp;开始&nbsp;
		  <input id="startTime" name="startTime" type="text" class="easyui-datebox x-text" style="width:120px; height:28px;"
		         <#if startTime?exists> value="${startTime}"</#if>>
		  &nbsp;结束&nbsp;
		  <input id="endTime" name="endTime" type="text" class="easyui-datebox x-text" style="width:120px; height:28px;"
		         <#if endTime?exists> value="${endTime}"</#if>>
		  &nbsp;<input type="button" value="确定" class="btnGray" onclick="javascript:showExport();">
	  </td>
	  </tr>
	</tbody>
  </table>
  <div id="mainContent">
    
  </div>
  <script type="text/javascript">
       jQuery(function(){
		    jQuery("#startTime").val(jQuery.cookie("startTime"));
            jQuery("#endTime").val(jQuery.cookie("endTime"));
	   });
  </script>
</body>
</html>