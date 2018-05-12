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

<#if categories?exists>
 $(function () {

	Highcharts.getOptions().colors = Highcharts.map(Highcharts.getOptions().colors, function(color) {
		return {
			radialGradient: { cx: 0.5, cy: 0.3, r: 0.7 },
			stops: [
				[0, color],
				[1, Highcharts.Color(color).brighten(-0.3).get('rgb')] // darken
			]
		};
	});

    $('#container').highcharts({
        chart: {
            type: '${chartType}'
			<#if chartType == "column">
			,margin: 75
			,options3d: {
                enabled: true,
                alpha: 15,
                beta: 15,
                depth: 40,
                viewDistance: 25
            }
			</#if>
        },
		<#if chartType == "bar">
		colors:[ 
		          <#if index == "A" || index == "B" >
				  '#33ff66',
				  <#else>
				  '#ff0033',
				  </#if>
		          '#ffff00',
				  '#3399CC',
				  '#3399CC',
                  '#ff00ff',  
                  '#ff9933',  
                  '#00cc33'
                ],
	    <#else>
           colors:[ 
                  '#3399CC',
				  '#FFFF00',
				  <#if index == "A" || index == "B" >
				  '#33ff66',
				  <#else>
				  '#ff0033',
				  </#if>
                  '#FF6666', 
                  '#ff00ff',  
                  '#ff9933',  
                  '#00cc33'
                ],
		</#if>
        title: {
            text: '${chartTitle}'
        },
        <#if chart.chartSubTitle?exists>
        subtitle: {
            text: '${chartSubTitle}'
        },
		</#if>
        xAxis: {
            categories: ${categories},
            title: {
                text: '${chart.coordinateX}'
            }
        },
        yAxis: {
            min: 0,
            title: {
                text: '${chart.coordinateY}',
                align: 'high'
            },
            labels: {
                overflow: 'justify'
            }
        },
        tooltip: {
            valueSuffix: ' '
        },
        plotOptions: {
            bar: {
                dataLabels: {
                    enabled: true
                }
            },
			column: {
                dataLabels: {
                    enabled: true
                }
            }
        },
        legend: {
            align: 'center',
            verticalAlign: 'bottom',
            floating: false,
            borderWidth: 1,
            backgroundColor: ((Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'),
            shadow: true
        },
        credits: {
            enabled: false
        },
        series: ${seriesData}
    });
  });

</#if>

	function switchXY(){
        document.iForm.submit();
	}

	function showGradeChart(){
		location.href="${contextPath}/heathcare/medicalExaminationChart/gradeChart?tenantId=${tenantId}";
	}

	function execute(){
		if(confirm("数据统计时间需要等待一段时间，确定重新统计吗？")){
		  jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/heathcare/medicalExaminationStatistics/execute',
				   dataType: 'json',
				   error: function(data){
					   alert('服务器处理错误！');
				   },
				   success: function(data){
					   if(data != null && data.message != null){
						   alert(data.message);
					   } else {
						   //alert('操作成功完成！');
					   }
					   if(data.statusCode == 200){
					       window.location.reload();
						   alert('操作成功完成！');
					   } 
				   }
			 });
		}
	}
</script>
</head>
<body style=" margin: 2px">
<form id="iForm" name="iForm" method="post" action="">
 <input type="hidden" id="type" name="type" value="${type}">
 <table>
	<tr>
	    <td>&nbsp;&nbsp;图表类型&nbsp;&nbsp;
            <select id="chartType" name="chartType" onchange="javascript:switchXY();">
			    <option value="">--请选择--</option>
				<option value="column">柱状图</option>
				<option value="bar">条形图</option>
            	<!-- <option value="line">线形图</option>
				<option value="spline">曲线图</option> -->
            </select>
            <script type="text/javascript">
            	document.getElementById("chartType").value="${chartType}";
            </script>
		</td>
	    <td>&nbsp;&nbsp;全园&nbsp;&nbsp;
            <select id="all" name="all" onchange="javascript:switchXY();">
			    <option value="">--请选择--</option>
            	<option value="Y">是</option>
            	<option value="N">否</option>
            </select>
            <script type="text/javascript">
            	document.getElementById("all").value="${all}";
            </script>
		</td>
		<td>&nbsp;&nbsp;年份&nbsp;&nbsp;
            <select id="year" name="year" onchange="javascript:switchXY();">
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
		<td>&nbsp;&nbsp;月份&nbsp;&nbsp;
            <select id="month" name="month" onchange="javascript:switchXY();">
            	<option value="">--请选择--</option>
            	<#list months as month>
            	<option value="${month}">${month}</option>
            	</#list> 
            </select>
            <script type="text/javascript">
            	document.getElementById("month").value="${month}";
            </script>
		</td>
		<td>&nbsp;&nbsp;统计指标&nbsp;&nbsp;
            <select id="index" name="index" onchange="javascript:switchXY();">
			    <option value="B">身高正常人数</option>
            	<option value="A">体重正常人数</option>
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
		<td>
		    &nbsp;&nbsp;
		    <input type="button" value="确定" onclick="javascript:switchXY();" class="btnGray">
			&nbsp;&nbsp;
		    <input type="button" value="按班级查看" onclick="javascript:showGradeChart();" class="btnGray">
			 &nbsp;&nbsp;
		    <input type="button" value="统计数据" onclick="javascript:execute();" class="btnGray">
		</td>
	</tr>
  </table>
 </form>
 <div id="container" style="min-width:80px; max-width:1480px; max-height:720px; margin: 10px auto; height:708px;"></div>
</body>
</html>