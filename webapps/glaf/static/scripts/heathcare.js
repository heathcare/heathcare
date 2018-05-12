   
    function switchFood(){
       var nodeId = document.getElementById("nodeId").value;
	   if(nodeId != ""){
		   var link = contextPath+"/heathcare/foodComposition/jsonFavorites?nodeId="+nodeId+"&rows=1000";
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
		   var link = contextPath+"/heathcare/foodComposition/jsonFavorites?nodeId="+nodeId+"&rows=1000";
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