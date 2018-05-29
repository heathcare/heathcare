<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>每日实际就餐人数</title>
<#include "/inc/init_easyui_import.ftl"/>
<style>

.subject { font-size: 13px; text-decoration: none; font-weight:normal; font-family:"宋体"}
.table-border { background-color:#eaf2ff; height: 32px; font-family:"宋体"}
.table-head { background-color:#5cb1f8; height: 30px; font-weight:bold; font-size: 13px; font-family:"宋体"}
.table-content { background-color:#ffffff; height: 28px; font-size: 12px; font-family:"宋体"}


</style>
<script type="text/javascript">
  <#if privilege_write == true>
	function saveData(){
		var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
					   type: "POST",
					   url: '${contextPath}/heathcare/actualRepastPerson/saveBatch',
					   data: params,
					   dataType: 'json',
					   error: function(data){
						   alert('服务器处理错误！');
					   },
					   success: function(data){
						   if(data != null && data.message != null){
							   alert(data.message);
						   } else {
							   if(data.statusCode == 200){
							     alert('操作成功完成！');
					             window.parent.location.reload();
						       }
						   }
					   }
				 });
	}
  </#if>

   function myformatter(date){
        var y = date.getFullYear();
        var m = date.getMonth()+1;
        var d = date.getDate();
        return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
    }

    function myparser(s){
            if (!s) return new Date();
            var ss = (s.split('-'));
            var y = parseInt(ss[0],10);
            var m = parseInt(ss[1],10);
            var d = parseInt(ss[2],10);
            if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
                return new Date(y,m-1,d);
            } else {
                return new Date();
            }
        }

    function onSelectXY(date){
		var y = date.getFullYear();
        var m = date.getMonth()+1;
        var d = date.getDate();
        var repastDate = y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
		//alert(repastDate);
		if(repastDate != ""){
			document.getElementById("repastDate").value=repastDate;
            document.getElementById("iForm").submit();
		}
    }

</script>
</head>
<body>
<div style="margin:0px;"></div>  
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:true,border:true" style="height:40px;margin-top:0px;"> 
    <div class="toolbar-backgroud"> 
	<span class="x_content_title">&nbsp;&nbsp;每日实际就餐人数</span>
	<#if privilege_write == true>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" 
	   onclick="javascript:saveData();" > 保 存 
	</a> 
	</#if>
    </div> 
  </div>

  <div data-options="region:'center',border:false,cache:true">
  <form id="iForm" name="iForm" method="post" action="${contextPath}/heathcare/actualRepastPerson/batchEdit">
  <input type="hidden" id="repastDate" name="repastDate" <#if repastDate?exists>value="${repastDate?string('yyyy-MM-dd')}"</#if>>
  <table cellspacing="1" cellpadding="4" width="95%" nowrap align="center">
    <tbody>
	  <tr>
	  <td width="20%" align="left">&nbsp;</td>
	  <td width="20%" align="left">&nbsp;</td>
	  <td width="60%" align="right">
	     日期&nbsp;
		 <input id="datex" name="datex" type="text"  
		        class="easyui-datebox x-text" style="width:100px;"
				data-options="onSelect:onSelectXY"
			    <#if repastDate?exists>
				value="${repastDate?string('yyyy-MM-dd')}"
				</#if>>
	  </td>
	  </tr>
	</tbody>
  </table>
 
  <table class="table-border" cellspacing="1" cellpadding="4" width="95%" nowrap align="center">
    <thead>
	  <tr>
	  <td class="table-head" width="30%" align="left">年龄</td>
	  <td class="table-head" width="30%" align="left">男生人数</td>
	  <td class="table-head" width="30%" align="left">女生人数</td>
	  </tr>
	</thead>
    <tbody>
	<#list rows as person>
	<tr>
		<td  class="table-content" align="left">${person.age}岁</td>
		<td  class="table-content" align="left">
             <input id="male_${person.age}" name="male_${person.age}" type="text" class="easyui-numberbox x-text" 
			        style="width:60px; text-align:right;"
			        value="${person.male}" size="3" maxlength="3"/>
		     &nbsp;(计划：${person.malePlan})
		</td>
		<td  class="table-content" align="left">
		     <input id="female_${person.age}" name="female_${person.age}" type="text" class="easyui-numberbox x-text" 
			        style="width:60px; text-align:right;"
				    value="${person.female}" size="3" maxlength="3"/>
		     &nbsp;(计划：${person.femalePlan})
		</td>
	</tr>
	</#list>
    </tbody>
  </table>
 </form>
</div>
</div>
<br>
<br>
<br>
<br>
</body>
</html>