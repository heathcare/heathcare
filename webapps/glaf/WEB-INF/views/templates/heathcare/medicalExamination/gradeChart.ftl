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
</script>
</head>
<body style=" margin: 2px">
<form id="iForm" name="iForm" method="post" action="${contextPath}/heathcare/medicalExaminationChart/gradeChart?tenantId=${tenantId}">
 <table>
	<tr>
	    <td>&nbsp;&nbsp;图表类型&nbsp;&nbsp;
            <select id="chartType" name="chartType" onchange="switchXY();">
			    <option value="">--请选择--</option>
				<option value="column">柱状图</option>
				<option value="bar">条形图</option>
            </select>
            <script type="text/javascript">
            	document.getElementById("chartType").value="${chartType}";
            </script>
		</td>
		 <td>&nbsp;&nbsp;体检类型&nbsp;&nbsp;
            <select id="type" name="type" onchange="switchXY();">
			    <option value="">--请选择--</option>
				<!-- <option value="1">入园体检</option> -->
				<option value="5">定期体检</option>
				<option value="7">专项体检</option>
            </select>
            <script type="text/javascript">
            	document.getElementById("type").value="${type}";
            </script>
		</td>
		<td>&nbsp;&nbsp;年级&nbsp;&nbsp;
            <select id="gradeYear" name="gradeYear" onchange="switchXY();">
            	<option value="">--请选择--</option>
            	<#list years as year>
            	<option value="${year}">${year}</option>
            	</#list> 
            </select>
            <script type="text/javascript">
            	document.getElementById("gradeYear").value="${gradeYear}";
            </script>
		</td>
		<td>&nbsp;&nbsp;班级&nbsp;&nbsp;
            <select id="gradeId" name="gradeId" onchange="switchXY();">
            	<option value="">--请选择--</option>
            	<#list grades as grade>
            	<option value="${grade.id}">${grade.name}</option>
            	</#list> 
            </select>
            <script type="text/javascript">
            	document.getElementById("gradeId").value="${gradeId}";
            </script>
		</td>
		<td>&nbsp;&nbsp;统计指标&nbsp;&nbsp;
            <select id="index" name="index" onchange="switchXY();">
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
		<td>&nbsp;&nbsp;
		    <input type="button" value="确定" onclick="javascript:switchXY();" class="btnGray">
		</td>
	</tr>
  </table>
 </form>
 <div id="container" style="min-width:80px; max-width:1480px; max-height:720px; margin: 10px auto; height:708px;"></div>
</body>
</html>