function openYesOrNoDLG(){
	$('.zhezhao').css('display', 'block');
	$('#removeUse').fadeIn();
}

function cancleBtn(){
	$('.zhezhao').css('display', 'none');
	$('#removeUse').fadeOut();
}

function changeDLGContent(contentStr){
	const p = $(".removeMain").find("p");
	p.html(contentStr);
}

//用户点击 确定删除 按钮后调用本方法
function deleteUser(obj){
	$.ajax({
		type:"GET",
		url:path+"/admin/user.do?method=delete&uid="+obj.attr("userid"),
		dataType:"json",
		success:function(data){
			if(data.delResult === "success"){  // 删除成功, 刷新页面
				location.reload();
			}else if(data.delResult === "failed"){  //删除失败
				changeDLGContent("对不起，删除用户【"+obj.attr("username")+"】失败");
			}else if(data.delResult === "notExist"){
				changeDLGContent("对不起，用户【"+obj.attr("username")+"】不存在");
			}
		},
		error:function(){
			changeDLGContent("对不起, 请求失败, 请点击取消按钮稍后重试");
		}
	});
}

$(function(){
	//通过jquery的class选择器（数组）
	//对每个class为viewUser的元素进行动作绑定（click）
	/**
	 * bind、live、delegate
	 * on
	 */
	$(".viewUser").on("click",function(){
		//将被绑定的元素（a标签）转换成jquery对象，可以使用jquery方法
		const obj = $(this);
		window.location.href=path+"/admin/user.do?method=view&uid="+ obj.attr("userid");
	});
	
	$(".modifyUser").on("click",function(){
		const obj = $(this);
		window.location.href=path+"/admin/user.do?method=modify&uid="+ obj.attr("userid");
	});

	$(".deleteUser").on("click",function(){
		const obj = $(this);
		// 借助#yes按钮传递欲删除用户的id
		$("#yes").attr("userid", obj.attr("userid"));
		changeDLGContent("你确定要删除用户【"+obj.attr("username")+"】吗？");
		openYesOrNoDLG();
	});

	$('#no').click(function () {
		cancleBtn();
	});

	$('#yes').click(function () {
		const obj = $('.deleteUser[userid="'+$(this).attr("userid")+'"]');
		deleteUser(obj);
	});

});
