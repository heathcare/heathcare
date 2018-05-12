<html>
<head>
<meta http-equiv=Content-Type content="text/html; charset=UTF-8">
<#include "/inc/init_easyui_import.ftl"/>
<link rel=Stylesheet href="${contextPath}/static/pages/DietaryAnalyze/stylesheet.css">
<style>
<!--table
	{mso-displayed-decimal-separator:"\.";
	mso-displayed-thousand-separator:"\,";}
@page
	{margin:.75in .25in .75in .25in;
	mso-header-margin:.3in;
	mso-footer-margin:.3in;
	mso-page-orientation:landscape;
	mso-horizontal-page-align:center;}
ruby
	{ruby-align:left;}
rt
	{color:windowtext;
	font-size:9.0pt;
	font-weight:400;
	font-style:normal;
	text-decoration:none;
	font-family:微软雅黑;
	mso-generic-font-family:auto;
	mso-font-charset:134;
	mso-char-type:none;
	display:none;}
-->
</style>

<script language="JavaScript"><!--

function msoCommentShow(com_id,anchor_id) {
	if(msoBrowserCheck()) {
	   c = document.all(com_id);
	   a = document.all(anchor_id);
	   if (null != c) {
		var cw = c.offsetWidth;
		var ch = c.offsetHeight;
		var aw = a.offsetWidth;
		var ah = a.offsetHeight;
		var x = a.offsetLeft;
		var y = a.offsetTop;
		var el = a;
		while (el.tagName != "BODY") {
		   el = el.offsetParent;
		   x = x + el.offsetLeft;
		   y = y + el.offsetTop;
		   }		
		var bw = document.body.clientWidth;
		var bh = document.body.clientHeight;
		var bsl = document.body.scrollLeft;
		var bst = document.body.scrollTop;
		if (x + cw + ah/2 > bw + bsl && x + aw - ah/2 - cw >= bsl ) {
		   c.style.left = x + aw - ah / 2 - cw; 
		}
		else {
		   c.style.left = x + ah/2; 
		}
		if (y + ch + ah/2 > bh + bst && y + ah/2 - ch >= bst ) {
	 	   c.style.top = y + ah/2 - ch;
		} 
		else {
		   c.style.top = y + ah/2;
		}
		c.style.visibility = "visible";
	   }
	}
}

function msoCommentHide(com_id) {
	if(msoBrowserCheck()) {
	  c = document.all(com_id)
	  if (null != c) {
	    c.style.visibility = "hidden";
	    c.style.left = "-10000";
	    c.style.top = "-10000";
	  }
	}
}

function msoBrowserCheck() {
 ms=navigator.appVersion.indexOf("MSIE");
 vers = navigator.appVersion.substring(ms+5, ms+6);
 ie4 = (ms>0) && (parseInt(vers) >=4);
 return ie4
}

if (msoBrowserCheck()) {
document.styleSheets.dynCom.addRule(".msocomspan1","position:absolute");
document.styleSheets.dynCom.addRule(".msocomspan2","position:absolute");
document.styleSheets.dynCom.addRule(".msocomspan2","left:-1.5ex");
document.styleSheets.dynCom.addRule(".msocomspan2","width:2ex");
document.styleSheets.dynCom.addRule(".msocomspan2","height:0.5em");
document.styleSheets.dynCom.addRule(".msocomanch","font-size:0.5em");
document.styleSheets.dynCom.addRule(".msocomanch","color:red");
document.styleSheets.dynCom.addRule(".msocomhide","display: none");
document.styleSheets.dynCom.addRule(".msocomtxt","visibility: hidden");
document.styleSheets.dynCom.addRule(".msocomtxt","position: absolute");        
document.styleSheets.dynCom.addRule(".msocomtxt","top:-10000");         
document.styleSheets.dynCom.addRule(".msocomtxt","left:-10000");         
document.styleSheets.dynCom.addRule(".msocomtxt","width: 33%");                 
document.styleSheets.dynCom.addRule(".msocomtxt","background: infobackground");
document.styleSheets.dynCom.addRule(".msocomtxt","color: infotext");
document.styleSheets.dynCom.addRule(".msocomtxt","border-top: 1pt solid threedlightshadow");
document.styleSheets.dynCom.addRule(".msocomtxt","border-right: 2pt solid threedshadow");
document.styleSheets.dynCom.addRule(".msocomtxt","border-bottom: 2pt solid threedshadow");
document.styleSheets.dynCom.addRule(".msocomtxt","border-left: 1pt solid threedlightshadow");
document.styleSheets.dynCom.addRule(".msocomtxt","padding: 3pt 3pt 3pt 3pt");
document.styleSheets.dynCom.addRule(".msocomtxt","z-index: 100");
}

// -->
</script>
<![endif]>
<script type="text/javascript">

	function doSearch(){
		var startDate = jQuery("#startDate").val();
        var endDate = jQuery("#endDate").val();
		if(startDate != ""){
			startDate = startDate + " 00:00:00";
		}
		if(endDate != ""){
			endDate = endDate + " 23:59:59";
			//jQuery("#endDate").val(endDate);
		}
		if(startDate > endDate){
			alert("开始时间不能大于结束时间。");
			return;
		}
		document.iForm.submit();
	}
</script>
</head>

<body link="#0563C1" vlink="#954F72" class=xl65>
<form id="iForm" name="iForm" method="post">
<table border=0 cellpadding=0 cellspacing=0 width=1231 style='border-collapse:
 collapse;table-layout:fixed;width:920pt'>
 <col class=xl65 width=108 style='mso-width-source:userset;mso-width-alt:3840;
 width:81pt'>
 <col class=xl65 width=63 span=3 style='mso-width-source:userset;mso-width-alt:
 2247;width:47pt'>
 <col class=xl65 width=81 style='mso-width-source:userset;mso-width-alt:2872;
 width:61pt'>
 <col class=xl65 width=80 style='mso-width-source:userset;mso-width-alt:2844;
 width:60pt'>
 <col class=xl65 width=63 span=4 style='mso-width-source:userset;mso-width-alt:
 2247;width:47pt'>
 <col class=xl65 width=80 style='mso-width-source:userset;mso-width-alt:2844;
 width:60pt'>
 <col class=xl65 width=63 span=7 style='mso-width-source:userset;mso-width-alt:
 2247;width:47pt'>
 <tr height=42 style='mso-height-source:userset;height:31.2pt'>
  <td colspan=18 height=42 class=xl106 width=1231 style='height:31.2pt;
  width:920pt'>膳食营养分析统计表</td>
 </tr>
 <tr height=34 style='mso-height-source:userset;height:25.2pt'>
  <td colspan=18 height=34 class=xl101 style='height:25.2pt'>
    &nbsp;日期&nbsp;开始&nbsp;
	<input id="startDate" name="startDate" type="text" class="easyui-datebox x-text" style="width:100px; height:25px;"
		   <#if startDate?exists> value="${startDate_}"</#if>>
	&nbsp;结束&nbsp;
	<input id="endDate" name="endDate" type="text" class="easyui-datebox x-text" style="width:100px; height:25px;"
		   <#if endDate?exists> value="${endDate_}"</#if>>
	&nbsp;
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-search'"
	   onclick="javascript:doSearch();">查找</a>
  </td>
 </tr>
 <tr height=34 style='mso-height-source:userset;height:25.2pt'>
  <td colspan=18 height=34 class=xl101 style='height:25.2pt'>${orgName}</td>
 </tr>
 <tr height=34 style='mso-height-source:userset;height:25.2pt'>
  <td colspan=7 height=34 class=xl117 width=521 style='height:25.2pt;
  width:390pt'>一、平均每人进食量</td>
  <td colspan=3 class=xl121 width=189 style='width:141pt'>(记帐法)</td>
  <td colspan=8 class=xl100>
  <#if startDate?exists && endDate?exists>
  ${startDate}<span style='mso-spacerun:yes'>&nbsp;</span>至
  <span style='mso-spacerun:yes'>&nbsp; </span>
  ${endDate}<span style='mso-spacerun:yes'>&nbsp;&nbsp;</span>
  </#if>
  </td>
 </tr>
 <tr class=xl66 height=40 style='mso-height-source:userset;height:30.0pt'>
  <td height=40 class=xl79 width=108 style='height:30.0pt;border-top:none;
  width:81pt'>食物类别</td>
  <td class=xl79 width=63 style='border-top:none;border-left:none;width:47pt'>细粮</td>
  <td class=xl80 style='border-top:none;border-left:none'>杂粮</td>
  <td class=xl79 width=63 style='border-top:none;border-left:none;width:47pt'>糕点</td>
  <td class=xl80 style='border-top:none;border-left:none'>干豆类</td>
  <td class=xl79 width=80 style='border-top:none;border-left:none;width:60pt'>豆制品</td>
  <td class=xl79 width=63 style='border-top:none;border-left:none;width:47pt'>蔬菜总量</td>
  <td class=xl79 width=63 style='border-top:none;border-left:none;width:47pt'>绿橙红等深色蔬菜</td>
  <td class=xl80 style='border-top:none;border-left:none'>水果</td>
  <td class=xl79 width=63 style='border-top:none;border-left:none;width:47pt'>乳类</td>
  <td class=xl80 style='border-top:none;border-left:none'>蛋类</td>
  <td class=xl81 style='border-top:none;border-left:none'>肉类</td>
  <td class=xl81 style='border-top:none;border-left:none'>肝类</td>
  <td class=xl81 style='border-top:none;border-left:none'>鱼</td>
  <td class=xl81 style='border-top:none;border-left:none'>食油</td>
  <td class=xl81 style='border-top:none;border-left:none'>糖</td>
  <td class=xl88 style='border-top:none;border-left:none'>　</td>
  <td class=xl88 style='border-top:none;border-left:none'>　</td>
 </tr>
 <tr height=42 style='mso-height-source:userset;height:31.2pt'>
  <td height=42 class=xl75 width=108 style='height:31.2pt;border-top:none;
  width:81pt'>数量（克）</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item1}&nbsp;</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item2}&nbsp;</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item3}&nbsp;</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item4}&nbsp;</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item5}&nbsp;</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item6}&nbsp;</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item7}&nbsp;</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item8}&nbsp;</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item9}&nbsp;</td>
  <td class=xl77 style='border-top:none;border-left:none'>${item10}&nbsp;</td>
  <td class=xl76 style='border-top:none;border-left:none'>${item11}&nbsp;</td>
  <td class=xl76 style='border-top:none;border-left:none'>${item12}&nbsp;</td>
  <td class=xl76 style='border-top:none;border-left:none'>${item13}&nbsp;</td>
  <td class=xl76 style='border-top:none;border-left:none'>${item14}&nbsp;</td>
  <td class=xl76 style='border-top:none;border-left:none'>${item15}&nbsp;</td>
  <td class=xl90 style='border-top:none;border-left:none'>　</td>
  <td class=xl90 style='border-top:none;border-left:none'>　</td>
 </tr>
 <tr height=26 style='mso-height-source:userset;height:19.35pt'>
  <td height=26 class=xl67 width=108 style='height:19.35pt;border-top:none;
  width:81pt'>　</td>
  <td class=xl68 width=63 style='border-top:none;width:47pt'>　</td>
  <td class=xl68 width=63 style='border-top:none;width:47pt'>　</td>
  <td class=xl68 width=63 style='border-top:none;width:47pt'>　</td>
  <td class=xl68 width=81 style='border-top:none;width:61pt'>　</td>
  <td class=xl68 width=80 style='border-top:none;width:60pt'>　</td>
  <td class=xl68 width=63 style='border-top:none;width:47pt'>　</td>
  <td class=xl68 width=63 style='border-top:none;width:47pt'>　</td>
  <td class=xl68 width=63 style='border-top:none;width:47pt'>　</td>
  <td class=xl68 width=63 style='border-top:none;width:47pt'>　</td>
  <td class=xl69 width=80 style='border-top:none;width:60pt'>　</td>
  <td class=xl70 style='border-top:none'>　</td>
  <td class=xl70 style='border-top:none'>　</td>
  <td class=xl70 style='border-top:none'>　</td>
  <td class=xl70 style='border-top:none'>　</td>
  <td class=xl70 style='border-top:none'>　</td>
  <td colspan=2 class=xl65 style='mso-ignore:colspan'></td>
 </tr>
 <tr height=27 style='mso-height-source:userset;height:20.85pt'>
  <td colspan=18 height=27 class=xl118 width=1231 style='height:20.85pt;
  width:920pt'>二、营养素摄入量</td>
 </tr>
 <tr height=40 style='mso-height-source:userset;height:30.0pt'>
  <td height=40 class=xl82 style='height:30.0pt'>　</td>
  <td class=xl79 width=63 style='border-left:none;width:47pt'>热量<br>
    （千卡）</td>
  <td class=xl79 width=63 style='border-left:none;width:47pt'>蛋白质<br>
    （g）</td>
  <td class=xl79 width=63 style='border-left:none;width:47pt'>脂肪<br>
    （g）</td>
  <td class=xl79 width=81 style='border-left:none;width:61pt'>碳水化合物<br>
    （g）</td>
  <td class=xl79 width=80 style='border-left:none;width:60pt'>视黄醇当量<br>
    (ug）</td>
  <td class=xl79 width=63 style='border-left:none;width:47pt'>维生素A<br>
    （ug）</td>
  <td class=xl79 width=63 style='border-left:none;width:47pt'>胡萝卜素<br>
    （ug）</td>
  <td class=xl79 width=63 style='border-left:none;width:47pt'>维生素B<font
  class="font6"><sub>1<br>
    </sub></font><font class="font7">（ug）</font></td>
  <td class=xl79 width=63 style='border-left:none;width:47pt'>维生素B<font
  class="font6"><sub>2<br>
    </sub></font><font class="font7">（ug）</font></td>
  <td class=xl79 width=80 style='border-left:none;width:60pt'>维生素C<br>
    （ug）</td>
  <td class=xl93 width=63 style='border-left:none;width:47pt'>钙<br>
    （ug）</td>
  <td class=xl93 width=63 style='border-left:none;width:47pt'>铁<br>
    （ug）</td>
  <td class=xl93 width=63 style='border-left:none;width:47pt'>锌<br>
    （ug）</td>
  <td class=xl93 width=63 style='border-left:none;width:47pt'>碘<br>
    （ug）</td>
  <td class=xl93 width=63 style='border-left:none;width:47pt'>磷<br>
    （ug）</td>
  <td class=xl94 width=63 style='border-left:none;width:47pt'>　</td>
  <td class=xl89 style='border-left:none'>　</td>
 </tr>
 <tr height=42 style='mso-height-source:userset;height:31.2pt'>
  <td height=42 class=xl83 width=108 style='height:31.2pt;border-top:none;
  width:81pt'>平均每人每日</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.heatEnergy}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.protein}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.fat}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.carbohydrate}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.retinol}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.vitaminA}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.carotene}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.vitaminB1}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.vitaminB2}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${avg.vitaminC}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${avg.calcium}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${avg.iron}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${avg.zinc}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${avg.iodine}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${avg.phosphorus}&nbsp;</td>
  <td class=xl91 style='border-top:none;border-left:none'>　</td>
  <td class=xl91 style='border-top:none;border-left:none'>　</td>
 </tr>
 <tr height=42 style='mso-height-source:userset;height:31.2pt'>
  <td height=42 class=xl83 width=108 style='height:31.2pt;border-top:none;
  width:81pt'>DRIs</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.heatEnergy}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.protein}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.fat}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.carbohydrate}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.retinol}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.vitaminA}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.carotene}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.vitaminB1}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.vitaminB2}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${foodDRI.vitaminC}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${foodDRI.calcium}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${foodDRI.iron}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${foodDRI.zinc}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${foodDRI.iodine}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${foodDRI.phosphorus}&nbsp;</td>
  <td class=xl91 style='border-top:none;border-left:none'>　</td>
  <td class=xl91 style='border-top:none;border-left:none'>　</td>
 </tr>
 <tr height=42 style='mso-height-source:userset;height:31.2pt'>
  <td height=42 class=xl83 width=108 style='height:31.2pt;border-top:none;
  width:81pt'>比较（%）</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.heatEnergy}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.protein}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.fat}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.carbohydrate}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.retinol}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.vitaminA}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.carotene}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.vitaminB1}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.vitaminB2}&nbsp;</td>
  <td class=xl95 style='border-top:none;border-left:none'>${p.vitaminC}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${p.calcium}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${p.iron}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${p.zinc}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${p.iodine}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${p.phosphorus}&nbsp;</td>
  <td class=xl91 style='border-top:none;border-left:none'>　</td>
  <td class=xl91 style='border-top:none;border-left:none'>　</td>
 </tr>
 <tr height=26 style='mso-height-source:userset;height:19.65pt'>
  <td height=26 colspan=18 class=xl65 style='height:19.65pt;mso-ignore:colspan'></td>
 </tr>
 <tr height=42 style='mso-height-source:userset;height:31.5pt'>
  <td colspan=7 height=42 class=xl110 style='height:31.5pt'>三、热能来源分布</td>
  <td class=xl65></td>
  <td colspan=4 class=xl115>四、蛋白质来源</td>
  <td class=xl65></td>
  <td colspan=5 class=xl103>五、膳食费使用</td>
 </tr>
 <tr height=32 style='mso-height-source:userset;height:24.0pt'>
  <td rowspan=2 height=64 class=xl111 width=108 style='border-bottom:.5pt solid black;
  height:48.0pt;border-top:none;width:81pt'>　</td>
  <td colspan=2 class=xl119 width=126 style='border-right:.5pt solid black;
  border-left:none;width:94pt'>脂肪</td>
  <td colspan=2 class=xl119 width=144 style='border-right:.5pt solid black;
  border-left:none;width:108pt'>蛋白质</td>
  <td colspan=2 class=xl119 width=143 style='border-right:.5pt solid black;
  border-left:none;width:107pt'>碳水化合物</td>
  <td class=xl65></td>
  <td rowspan=2 class=xl111 width=63 style='border-bottom:.5pt solid black;
  border-top:none;width:47pt'>　</td>
  <td colspan=3 class=xl119 width=206 style='border-right:.5pt solid black;
  border-left:none;width:154pt'>优质蛋白质</td>
  <td class=xl65></td>
  <td colspan=2 class=xl107 style='border-right:.5pt solid black'>本月总收入</td>
  <td colspan=2 class=xl104 style='border-right:.5pt solid black;border-left:
  none'>${totalIncome}</td>
  <td class=xl86 style='border-top:none;border-left:none'>元</td>
 </tr>
 <tr height=32 style='mso-height-source:userset;height:24.0pt'>
  <td height=32 class=xl84 width=63 style='height:24.0pt;border-top:none;
  border-left:none;width:47pt'>要求</td>
  <td class=xl84 width=63 style='border-top:none;border-left:none;width:47pt'>现状</td>
  <td class=xl84 width=63 style='border-top:none;border-left:none;width:47pt'>要求</td>
  <td class=xl84 width=81 style='border-top:none;border-left:none;width:61pt'>现状</td>
  <td class=xl84 width=80 style='border-top:none;border-left:none;width:60pt'>要求</td>
  <td class=xl84 width=63 style='border-top:none;border-left:none;width:47pt'>现状</td>
  <td class=xl65></td>
  <td class=xl84 width=63 style='border-top:none;border-left:none;width:47pt'>要求</td>
  <td class=xl84 width=80 style='border-top:none;border-left:none;width:60pt'>动物性食物</td>
  <td class=xl85 style='border-top:none;border-left:none'>豆类</td>
  <td class=xl65></td>
  <td colspan=2 class=xl107 style='border-right:.5pt solid black'>本月支出</td>
  <td colspan=2 class=xl109 style='border-left:none'>${totalPayOut}&nbsp;</td>
  <td class=xl86 style='border-top:none;border-left:none'>元</td>
 </tr>
 <tr height=50 style='mso-height-source:userset;height:37.2pt'>
  <td height=50 class=xl92 width=108 style='height:37.2pt;border-top:none;
  width:81pt'>摄入量（克）</td>
  <td class=xl99 width=63 style='border-top:none;border-left:none;width:47pt'>${fatStd}&nbsp;</td>
  <td class=xl97 style='border-top:none;border-left:none'>${fat}&nbsp;</td>
  <td class=xl99 width=63 style='border-top:none;border-left:none;width:47pt'>${proteinStd}&nbsp;</td>
  <td class=xl97 style='border-top:none;border-left:none'>${protein}&nbsp;</td>
  <td class=xl99 width=80 style='border-top:none;border-left:none;width:60pt'>${carbohydrateStd}&nbsp;</td>
  <td class=xl97 style='border-top:none;border-left:none'>${carbohydrate}&nbsp;</td>
  <td class=xl65></td>
  <td class=xl92 width=63 style='border-top:none;width:47pt'>摄入量（克）</td>
  <td class=xl99 width=63 style='border-top:none;border-left:none;width:47pt'>${proteinHQ}&nbsp;</td>
  <td class=xl97 style='border-top:none;border-left:none'>${animalProtein}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${beansProtein}&nbsp;</td>
  <td class=xl65></td>
  <td colspan=2 class=xl113 style='border-right:.5pt solid black'>盈（或亏）</td>
  <td colspan=2 class=xl76 style='border-left:none'>${profitOrLoss}&nbsp;</td>
  <td class=xl87 style='border-top:none;border-left:none'>元</td>
 </tr>
 <tr height=55 style='mso-height-source:userset;height:41.55pt'>
  <td height=55 class=xl92 width=108 style='height:41.55pt;border-top:none;
  width:81pt'>占总热量（%）</td>
  <td class=xl78 style='border-top:none;border-left:none'><font class="font9">2</font><font
  class="font8">0</font><font class="font6">~</font><font
  class="font7">35</font></td>
  <td class=xl98 width=63 style='border-top:none;border-left:none;width:47pt'>${fatPercent}&nbsp;</td>
  <td class=xl78 style='border-top:none;border-left:none'>12<font class="font6">~</font><font
  class="font7">15</font></td>
  <td class=xl98 width=81 style='border-top:none;border-left:none;width:61pt'>${proteinPercent}&nbsp;</td>
  <td class=xl78 style='border-top:none;border-left:none'>50<font class="font6">~</font><font
  class="font7">55</font></td>
  <td class=xl98 width=63 style='border-top:none;border-left:none;width:47pt'>${carbohydratePercent}&nbsp;</td>
  <td class=xl65></td>
  <td class=xl92 width=63 style='border-top:none;width:47pt'>占总热量（%）</td>
  <td class=xl78 style='border-top:none;border-left:none'>≥50&nbsp;</td>
  <td class=xl97 style='border-top:none;border-left:none'>${animalProteinPercent}&nbsp;</td>
  <td class=xl96 style='border-top:none;border-left:none'>${beansProteinPercent}&nbsp;</td>
  <td class=xl65></td>
  <td colspan=2 class=xl113 style='border-right:.5pt solid black'>占总收入</td>
  <td colspan=2 class=xl122 style='border-left:none'>${incomePercent}&nbsp;</td>
  <td class=xl87 style='border-top:none'>%</td>
 </tr>
 <tr height=29 style='mso-height-source:userset;height:21.9pt'>
  <td colspan=18 height=29 class=xl116 width=1231 style='height:21.9pt;
  width:920pt'></td>
 </tr>
 <tr height=21 style='height:15.6pt'>
  <td height=21 class=xl71 style='height:15.6pt'></td>
  <td class=xl72></td>
  <td colspan=3 class=xl73 style='mso-ignore:colspan'></td>
  <td class=xl72></td>
  <td class=xl73></td>
  <td colspan=3 class=xl72 style='mso-ignore:colspan'></td>
  <td colspan=8 class=xl65 style='mso-ignore:colspan'></td>
 </tr>
 <tr height=21 style='height:15.6pt'>
  <td height=21 colspan=5 class=xl73 style='height:15.6pt;mso-ignore:colspan'></td>
  <td class=xl72></td>
  <td class=xl73></td>
  <td colspan=3 class=xl72 style='mso-ignore:colspan'></td>
  <td colspan=8 class=xl65 style='mso-ignore:colspan'></td>
 </tr>
 <tr height=23 style='mso-height-source:userset;height:17.4pt'>
  <td height=23 colspan=10 class=xl74 style='height:17.4pt;mso-ignore:colspan'></td>
  <td colspan=8 class=xl65 style='mso-ignore:colspan'></td>
 </tr>
</table>
</form>
</body>
</html>