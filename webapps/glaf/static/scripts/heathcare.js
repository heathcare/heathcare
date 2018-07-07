   
    function getNowFormatDate() {
		var date = new Date();
		var seperator1 = "-";
		var seperator2 = ":";
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate+ " " + date.getHours() + seperator2 + date.getMinutes()+ seperator2 + date.getSeconds();
		return currentdate;
    }

    function switchFood(){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = contextPath+"/heathcare/foodComposition/jsonFavorites?nodeId="+nodeId+"&rows=1000&tt="+getNowFormatDate();
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("goodsId");
			  food.options.length=0;
			  jQuery.each(data.rows, function(i, item){
				 food.options.add(new Option(item.name, item.id));
			  });
			});
	   }
   }


   function switchFood2(selected, selectedName){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = contextPath+"/heathcare/foodComposition/jsonFavorites?nodeId="+nodeId+"&rows=1000&tt="+getNowFormatDate();
		   //alert(link);
		   jQuery.getJSON(link, function(data){
			  var food = document.getElementById("goodsId");
			  food.options.length = 0;
			  var selectedIndex = 0;
			  var selectedX = "";
			  jQuery.each(data.rows, function(i, item){
				 if(item.id == selected){
					 selectedIndex = i;
					 selectedX = item.name;
				 }
				 food.options.add(new Option(item.name, item.id));
			  });
             if(selectedX == ""){
				 //alert(selectedName);
                 food.options.add(new Option(selectedName, selected));
				 selectedIndex = food.options.length -1;
			 }
             food.selectedIndex = selectedIndex;
			 //alert(selectedIndex);
			});
	   }
   }