$(function(){
	const backBtn = $("#back");
	backBtn.on("click",function(){
		//alert("view : "+referer);
		if(referer !== undefined && "" !== referer && "null" !== referer){
			window.location.href = referer;
		}else{
			history.back(-1);
		}
	});
});