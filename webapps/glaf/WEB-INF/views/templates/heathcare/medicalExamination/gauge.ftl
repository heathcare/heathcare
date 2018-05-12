<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${chartTitle}</title>
<#include "/inc/init_easyui_import.ftl"/>
<#include "/inc/init_highcharts_import.ftl"/>
<script type="text/javascript">

	function switchXY(){
        document.iForm.submit();
	}
</script>
</head>
<body>
<form id="iForm" name="iForm" method="post" action="">
 <input type="hidden" id="type" name="type" value="${type}">
 <table>
	<tr>
		<td>年份&nbsp;&nbsp;
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
		<td>月份&nbsp;&nbsp;
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
		<td>统计指标&nbsp;&nbsp;
            <select id="index" name="index" onchange="switchXY();">
            	<option value="A">体重正常人数</option>
            	<option value="B">身高正常人数</option>
				<option value="C">体重低于2SD人数</option>
				<option value="D">身高低于2SD人数</option>
				<option value="E">消瘦人数</option>
				<option value="F">超重人数</option>
				<option value="G">肥胖人数</option>
            </select>
            <script type="text/javascript">
            	document.getElementById("index").value="${index}";
            </script>
		</td>
	</tr>
  </table>
 </form>
 <div id="container" style="min-width:80px; max-width:1480px; max-height:720px; margin: 0 auto; height:708px;"></div>
</body>
</html>