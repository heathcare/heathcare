<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>删除食谱</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

* {
	margin: 0;
	padding: 0;
}

body {
	font-family: "微软雅黑";
	font-size: 12px;
}


/* add bottom margin between tables */

#table1,
#table2 {
	margin-bottom: 20px;
}


/* drag container (contains two tables) */

#drag {
	float: left;
}


/* drag objects (DIV inside table cells) */

.drag {
	position: relative;
	cursor: move;
	z-index: 10;
	background-color: #fff;
	text-align: center;
	width: 170px;
	height: 33px;
	line-height: 33px;
	-moz-border-radius: 10px;
	-moz-box-shadow: 4px 4px 8px #444;
	border-radius: 5px;
	box-sizing: border-box;
}


/* drag objects border for the first table */

.t2 {
	border: 1px solid #9B9EA2;
}


/* tables */

div#drag table {
	background-color: #f6f6f6;
	border-collapse: collapse;
	width: 300px;
}


/* table cells */

div#drag td {
	height: 45px;
	border: 1px solid #3399ff;
	font-size: 12px;
	padding-left: 10px;
}


/* message cell (marked as forbidden) */

.mark {
	color: white;
	background-color: #9B9EA2;
}

.before_wk {
	list-style-type: none;
	margin: 0;
	padding: 0;
	float: left;
	width: 195px;
	box-sizing: border-box;
}

.before_wkday {
	border-left: 1px #3399ff solid;
	padding-left: 55px;
	border-bottom: 1px #3399ff solid;
	height: 46px;
	box-sizing: border-box;
	background-color: #f6f6f6;
	line-height: 45px;
}

.week_title {
	display: inline-block;
	margin-right: 0;
	box-sizing: border-box;
	padding-left: 10px;
	height: 45px;
	line-height: 45px;
}

.title_div {
	border: 1px solid #3399ff;
	width: 493px;
	background-color: #f6f6f6;
	height: 45px;
}


</style>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript" src="${contextPath}/static/scripts/drag.js"></script>
<script type="text/javascript" src="${contextPath}/static/scripts/map.js"></script>
<script type="text/javascript">

	function adjust(){
        var list = jQuery("li");
        var list2 = jQuery(" td > div ");
		var str = "";
		for (var i = 0; i < list.length; i++) {
          str= str + "&date_"+jQuery(list[i]).attr("key") + "="+ jQuery(list2[i]).attr("value");
		}
		//alert(str);
        if(confirm("确定调整选择日期的食谱数据吗？")){
		  var params = jQuery("#iForm").formSerialize();
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/dietary/adjust?year=${year}&week=${week}'+str,
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
					       //window.parent.location.reload();
					   } 
				   }
			 });
		}
	}

    function intDrag(){
		// initialization
		glaf.drag.init();
		// set hover color
		glaf.drag.hover_color = '##3366ff';
		// set drop option to 'switching'
		glaf.drag.drop_option = 'switching';
	}  
</script>
</head>
<body onload="intDrag();">
       <br>
       &nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="btnGray" value="确定" onclick="javascript:adjust();">
	   <br><br>
        <div class="title_div">
			<span class="week_title" style="border-right: 1px solid #3399ff;width: 195px;">调整前日期</span>
			<span class="week_title" style="width: 250px;">调整后日期</span>
		</div>
		<ul class="before_wk" id="before_wk">
		   <#list items as item>
			<li class="before_wkday" key="${item.name}" value="${item.value}">${item.text}</li>
		   </#list>
		</ul>

		<div id="drag">
			<table id="table1">
			    <#list items as item>
				<tr>
					<td style="border-top: 0px;">
						<div id="kd" class="drag t2" key="${item.name}" value="${item.value}">原${item.text}</div>
					</td>
				</tr>
				</#list>
			</table>
		</div>
</body>
</html>