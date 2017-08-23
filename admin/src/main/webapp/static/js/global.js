// JavaScript Document
$(function(){
	var ww = $(window).width();
	var hh = $(window).height();
	$(window).resize(function() {
		ww = $(window).width();
		if(320<ww<768){
			$("html").css("font-size",ww/640*20+'px');
            
		}else if(769<ww){
			$("html").css("font-size",ww/1920*40+'px');
		}
		$(".nav_left").height(hh);
	});
	if(320<ww<768){
		$("html").css("font-size",ww/640*20+'px');
	}else if(769<ww){
		$("html").css("font-size",ww/1920*40+'px');
	}
	$(".row").height(hh);
	$(".nav_left").height(hh);
	$(".box_right").height(hh);
})