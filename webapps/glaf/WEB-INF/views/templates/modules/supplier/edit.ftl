<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>供应商信息</title>
<#include "/inc/init_easyui_import.ftl"/>
<script type="text/javascript" src="${contextPath}/static/scripts/global.js"></script>
<script type="text/javascript">
    var contextPath = "${contextPath}";

	function saveData(){
        var params = jQuery("#iForm").formSerialize();
		jQuery.ajax({
				   type: "POST",
				   url: '${contextPath}/supplier/saveSupplier',
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

</script>
</head>
<body>
<div style="margin:0;"></div>
<div class="easyui-layout" data-options="fit:true">  
  <div data-options="region:'north',split:false,border:true" style="height:42px" class="toolbar-backgroud"> 
    <div style="margin:4px;"> 
	<span class="x_content_title">&nbsp;<img src="${contextPath}/static/images/window.png">&nbsp;编辑供应商信息</span>
	<a href="#" class="easyui-linkbutton" data-options="plain:true, iconCls:'icon-save'" onclick="javascript:saveData();" >保存</a> 
    </div> 
  </div>

  <div data-options="region:'center', border:false, cache:true">
  <form id="iForm" name="iForm" method="post">
  <input type="hidden" id="supplierId" name="supplierId" value="${supplier.supplierId}"/>
  <table class="easyui-form" style="width:1050px;" align="center">
    <tbody>
	<tr>
		<td width="12%" align="left">供应商名称</td>
		<td width="88%" align="left" colspan="3">
            <input id="name" name="name" type="text" 
			       class="easyui-validatebox  x-text" style="width:650px;"
				   value="${supplier.name}">
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">供应商简称</td>
		<td width="38%" align="left">
            <input id="shortName" name="shortName" type="text" 
			       class="easyui-validatebox  x-text" style="width:250px;"			
				   value="${supplier.shortName}">
		</td>
		<td width="12%" align="left">代码</td>
		<td width="38%" align="left">
            <input id="code" name="code" type="text" 
			       class="easyui-validatebox  x-text" style="width:250px;"			
				   value="${supplier.code}">
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">企业法人</td>
		<td align="left">
            <input id="legalPerson" name="legalPerson" type="text" 
			       class="easyui-validatebox  x-text" style="width:250px;"
				   value="${supplier.legalPerson}">
		</td>
		<td width="12%" align="left">纳税人识别号</td>
		<td align="left">
            <input id="taxpayerID" name="taxpayerID" type="text" 
			       class="easyui-validatebox  x-text" style="width:250px;" 
				   value="${supplier.taxpayerID}">
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">纳税人身份</td>
		<td align="left">
			<select id="taxpayerIdentity" name="taxpayerIdentity">
			    <option value="">----请选择----</option>
				<option value="2">一般纳税人</option>
				<option value="1">小规模纳税人</option>
				<option value="0">其他</option>
			</select>
            <script type="text/javascript">
                document.getElementById("taxpayerIdentity").value="${supplier.taxpayerIdentity}";
            </script>
		</td>
		<td width="12%" align="left">电子邮件地址</td>
		<td align="left">
            <input id="mail" name="mail" type="text" 
			       class="easyui-validatebox  x-text" style="width:250px;" 
				   value="${supplier.mail}">
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">收款开户银行全称</td>
		<td align="left">
            <input id="bankOfDeposit" name="bankOfDeposit" type="text" 
			       class="easyui-validatebox  x-text" style="width:250px;"
				   value="${supplier.bankOfDeposit}">
		</td>
		<td width="12%" align="left">收款银行账号</td>
		<td align="left">
            <input id="bankAccount" name="bankAccount" type="text" 
			       class="easyui-validatebox  x-text" style="width:250px;" 
				   value="${supplier.bankAccount}">
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">负责人</td>
		<td align="left">
            <input id="principal" name="principal" type="text" 
			       class="easyui-validatebox  x-text" style="width:250px;"
				   value="${supplier.principal}">
		</td>
		<td width="12%" align="left">电话</td>
		<td align="left">
            <input id="telephone" name="telephone" type="text" 
			       class="easyui-validatebox  x-text" style="width:250px;" 
				   value="${supplier.telephone}">
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">性质</td>
		<td align="left">
			<select id="property" name="property">
			    <option value="">----请选择----</option>
				<option value="Public">国营企业</option>
				<option value="Private">私营企业</option>
				<option value="Other">其他</option>
			</select>
            <script type="text/javascript">
                document.getElementById("property").value="${supplier.property}";
            </script>
		</td>
		<td width="12%" align="left">认证</td>
		<td align="left">
			<select id="verify" name="verify">
			    <option value="">----请选择----</option>
				<option value="Y">已认证</option>
				<option value="N">未认证</option>
			</select>
            <script type="text/javascript">
                document.getElementById("verify").value="${supplier.verify}";
            </script>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">省份</td>
		<td align="left">
			<select id="provinceId" name="provinceId" onchange="javascript:selectDistrict('provinceId', 'cityId');">
			    <option value="">----请选择----</option>
				<#list provinces as province>
				<option value="${province.id}">${province.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    //selectProvince("provinceId");
                document.getElementById("provinceId").value="${supplier.provinceId}";
            </script>
		</td>
		<td width="12%" align="left">市</td>
		<td align="left">
			<select id="cityId" name="cityId" onchange="javascript:selectDistrict('cityId', 'areaId');">
			    <option value="">----请选择----</option>
				<#list citys as city>
				<option value="${city.id}">${city.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if citys?exists>
				  document.getElementById("cityId").value="${supplier.cityId}";
				<#else>
				  selectDistrict("cityId", document.getElementById("provinceId").value);
				</#if>
            </script>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">区县</td>
		<td align="left">
		    <select id="areaId" name="areaId" onchange="javascript:selectDistrict('areaId', 'townId');">
			    <option value="">----请选择----</option>
				<#list areas as area>
				<option value="${area.id}">${area.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if areas?exists>
				  document.getElementById("areaId").value="${supplier.areaId}";
				<#else>
				  selectDistrict("areaId", document.getElementById("cityId").value);
				</#if>
            </script>
		</td>
		<td width="12%" align="left">镇</td>
		<td align="left">
		    <select id="townId" name="townId">
			    <option value="">----请选择----</option>
				<#list towns as town>
				<option value="${town.id}">${town.name}</option>
			    </#list>
			</select>
            <script type="text/javascript">
			    <#if towns?exists>
				  document.getElementById("townId").value="${supplier.townId}";
				<#else>
				  selectDistrict("townId", document.getElementById("areaId").value);
				</#if>
            </script>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">供应材料</td>
		<td colspan="3" align="left">
		    <input id="material" name="material" type="text" style="height:60px; width:650px;text-align:left;"
				   class=" x-textarea" value="${supplier.material}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">详细地址</td>
		<td colspan="3" align="left">
		    <input id="address" name="address" type="text" style="height:60px; width:650px;text-align:left;"
				   class=" x-textarea" value="${supplier.address}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">备注</td>
		<td colspan="3" align="left">
		    <input id="remark" name="remark" type="text" style="height:60px; width:650px;text-align:left;"
				   class=" x-textarea" value="${supplier.remark}"/>
		</td>
	</tr>
	<tr>
		<td width="12%" align="left">是否有效</td>
		<td align="left">
		  <input type="radio" name="locked" value="0" <#if supplier.locked == 0>checked</#if>>是&nbsp;&nbsp;
	      <input type="radio" name="locked" value="1" <#if supplier.locked == 1>checked</#if>>否&nbsp;&nbsp;
		</td>
	</tr>
   </tbody>
  </table>
 </form>
</div>
</div>
</body>
</html>