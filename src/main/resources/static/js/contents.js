function parseDate(myDate){
	var parts, date, time, dt, ms;

	parts = myDate.split(/[T ]/); // Split on `T` or a space to get date and time
	date = parts[0];
	time = parts[1];

	dt = new Date();

	parts = date.split(/[-\/]/);  // Split date on - or /
	dt.setFullYear(parseInt(parts[0], 10));
	dt.setMonth(parseInt(parts[1], 10) - 1); // Months start at 0 in JS
	dt.setDate(parseInt(parts[2], 10));

	parts = time.split(/:/);    // Split time on :
	dt.setHours(parseInt(parts[0], 10));
	dt.setMinutes(parseInt(parts[1], 10));
	dt.setSeconds(parseInt(parts[2], 10));

	ms = dt.getTime();
	return ms;
}

// ==============================================================
//
//    App Init
//

var App = {
	URL : '',
	NaverCliendId : '',

	GetURL : function(){
		return this.URL === '' ? window.location.origin : App.URL;
	},

	SetURL : function(url){
		this.URL = url;
	},

	GetOS : function(){
		if(navigator.userAgent.toLowerCase().indexOf('android') > -1) return 'web_android';
		if(navigator.userAgent.toLowerCase().indexOf('iphone') > -1) return 'web_iphone';
		if(navigator.userAgent.toLowerCase().indexOf('ipod') > -1) return 'web_ipod';
		if(navigator.userAgent.toLowerCase().indexOf('ipad') > -1) return 'web_ipad';
		return false;
	},

	Init : function(){
		// _ImageAlign.enabled = true;
		// window.messageModal = new MessageModal(jQuery);
		$(document).ready(function(){
			$('#topBannerHideBtn').on('click', App.TopBannerHide);
			$('#tnbCategoryShowBtn').on('click', function(e){
				e.preventDefault();
				$('#tnbCategoryMap').toggle();
			});
			$('#tnbCategoryHideBtn').on('click', function(e){
				e.preventDefault();
				$('#tnbCategoryMap').hide();
			});

			$(window).scroll(App.ScrollAction);
			$('#quickGoTop').find('a').on('click', App.GoTop);
			$(document).on('click', '.daumAddress .find_address', App.FindDaumAddress);
			$(document).on('click', '.daumAddress .find_address2', App.FindDaumAddress2);

			$('#startPageBtn').on('click', App.StartPage);
			$('#favoriteLinkBtn').on('click', App.Favorite);
			$('#quickContainer .quickContents').each(function(){
				App.QuickGoodsViewByPage($(this));
			});

			$(document).on('click', 'a.addWishBtn', App.addWish);

			var wrap = $('#leftQuickBanner1 ul');

			if(wrap.length){
				wrap.find('li').eq(0).find('img').imgLoad(function(){
					var obj = $(this).closest('li');
					wrap.translate3d({height : wrap.outerHeight()}, {height : obj.outerHeight()}, 400);
				});
			}
		});

		// 퀵바
		$(document).on('click', '#quickGoodsTab a.upBtn, #quickGoodsTab a.dnBtn', this.ClickQuickUpDown);
		$(document).on('click', '#quickGoodsTab a.hd', this.ClickQuickHeader);

        $(document).on('click', '#leftQuickBanner1 .btns button', this.ClickLeftQBannerNav);
		$(document).on('click', '#todayView .todayViewDelBtn', this.ClickTodayViewDelete);


		$(document).on('click', '#printedMatterDiag a.left', function(e){
			e.preventDefault();
			var sections = $('#printedMatterDiag section');
			var section = sections.filter('.on');
			var next = (section.next().length && section.next()[0].tagName == 'SECTION') ? section.next() : sections.eq(0);
			next.addClass('on').siblings('section').removeClass('on');
		});

		$(document).on('click', '#printedMatterDiag a.right', function(e){
			e.preventDefault();
			var sections = $('#printedMatterDiag section');
			var section = sections.filter('.on');
			var next = (section.prev().length && section.prev()[0].tagName == 'SECTION') ? section.prev() : sections.last();
			next.addClass('on').siblings('section').removeClass('on');
		});



		$(document).on('click', '.clickViewPopupGuide .viewPopupBtn button', function(){
			$(this).closest('.clickViewPopupGuide').find('.hiddenPopup').show();
		});
		$(document).on('click', '.clickViewPopupGuide .close', function(){
			$(this).closest('.clickViewPopupGuide').find('.hiddenPopup').hide();
		});
	},

	MobileInit : function(){
		$(document).on('click', '#showBarcodeBtn', function(e){
			e.preventDefault();
			var layout = $('#barcodeLayout');
			if($.trim(layout.text()) != ''){
				layout.show();
				return;
			}
			JCM.get(layout.attr('data-href'), {}, function(data){
				layout.html(data);
				layout.show();
			});
		});

		$(document).on('click', '#barcodeLayout .closeBtn', function(e){
			e.preventDefault();
			var layout = $('#barcodeLayout');
			layout.hide();
		});
	},

	ResetForm : function(){
		var form = $(this).closest('form');
		form.find('select, input').each(function(){
			if(this.tagName === 'SELECT') this.value = '';
			if(this.tagName === 'INPUT'){
				if(this.type === 'text') this.value = '';
				if(this.type === 'radio' || this.type === 'checkbox'){
					this.checked = (this.value === '');
				}
			}
		});

		if(typeof(form.data('reset')) !== 'undefined') form.data('reset')();
		form.submit();
	},


	// https://openapi.map.naver.com/openapi/v3/maps.js?clientId=xxxxxxx&submodules=geocoder
	AddressToLocByNaver : function(address1, address2, callback){
		naver.maps.Service.geocode({
			address: address1 + ' ' + address2
		}, function(status, response) {
			if (status === naver.maps.Service.Status.ERROR) {
				callback(false, false);
			}
			else{
				var item = response.result.items[0];
				callback(item.point.y, item.point.x);
			}
		});
	},

	AddressToLocByGoogle : function(address1, address2, callback){
		JCM.getWithLoading('http://maps.googleapis.com/maps/api/geocode/json?address=' + encodeURIComponent(address1 + ' ' + address2) + '&language=ko&sensor=false', {}, function(res){
			if (res.status == 'OK') {
				if (res.results[0]) callback(res.results[0].geometry.location.lat, res.results[0].geometry.location.lng);
			}
			else callback(false, false);
		});
	},

	StartPage : function(e){
		e.preventDefault();
		document.body.style.behavior='url(#default#homepage)';
		document.body.setHomePage(this.href);
	},

	Favorite : function(e) {
		e.preventDefault();
		var bookmarkURL = this.href;
		var bookmarkTitle = document.title;
		var triggerDefault = false;

		if (window.sidebar && window.sidebar.addPanel) {
			// Firefox version &lt; 23
			window.sidebar.addPanel(bookmarkTitle, bookmarkURL, '');
		} else if ((window.sidebar && (navigator.userAgent.toLowerCase().indexOf('firefox') < -1)) || (window.opera && window.print)) {
			// Firefox version &gt;= 23 and Opera Hotlist
			var $this = $(this);
			$this.attr('href', bookmarkURL);
			$this.attr('title', bookmarkTitle);
			$this.attr('rel', 'sidebar');
			$this.off(e);
			triggerDefault = true;
		} else if (window.external && ('AddFavorite' in window.external)) {
			// IE Favorite
			window.external.AddFavorite(bookmarkURL, bookmarkTitle);
		} else {
			// WebKit - Safari/Chrome
			alert((navigator.userAgent.toLowerCase().indexOf('mac') != -1 ? 'Cmd' : 'Ctrl') + '+D 를 이용해 이 페이지를 즐겨찾기에 추가할 수 있습니다.');
		}

		return triggerDefault;
	},

	TopBannerHide : function(e){
		e.preventDefault();
		$('#wrap').addClass('topBannerClose');
	},

	ScrollAction : function(){
		var qc = $('#quickContainer');

		// fixed
		var stop = $(window).scrollTop();
		if(qc.css('position') === 'fixed'){
			var qtop = parseInt(qc.css('top'));
			var t = parseInt(qc.css('top')) - stop;
			if(stop < qtop) t = 0;
			if(t * (-1) > qtop) t = qtop * -1;

			qc.css({'transform' : 'translate3d(0, ' + t + 'px, 0)'});
		}
		else{
			var t = stop - parseInt(qc.css('top'));
			if(t < 0) t = 0;

			qc.css({'transform' : 'translate3d(0, ' + t + 'px, 0)'});
		}
	},

	GoTop : function(e){
		e.preventDefault();
		$(window).scrollTop(0);
	},

	EAUp : function(e, callback){
		e.preventDefault();
		var inp = $(this).closest('.ea').find('input');
		var num = parseInt(inp.val()) + 1;
		var maxEA = $(this).attr("data-max-ea") ? parseInt($(this).attr("data-max-ea")) : 0;
		if(maxEA == 0 || typeof(maxEA) == undefined ){
			maxEA = inp[0].hasAttribute('data-max-ea') ? (parseInt(inp.attr('data-max-ea')) || 0) : 0;	
		}
		if(maxEA > 0 && maxEA < num){
			CMAlert('최대 주문수량은 ' + maxEA + '개 입니다.');
		}
		else{
			inp[0].value = num;
			if(typeof callback === 'function') callback.call(inp[0]);
		}
	},

	EADown : function(e, callback){
		e.preventDefault();
		var inp = $(this).closest('.ea').find('input');
		var num = parseInt(inp.val()) - 1;

		var minEA = $(this).attr("data-min-ea") ? parseInt($(this).attr("data-min-ea")) : 0;
//		var minEA = inp[0].hasAttribute('data-min-ea') ? (parseInt(inp.attr('data-min-ea')) || 0) : 0;
		if(minEA == 0 || typeof(minEA) == undefined ){
			minEA = inp[0].hasAttribute('data-min-ea') ? (parseInt(inp.attr('data-min-ea')) || 0) : 0;	
		}

		if(minEA > 0 && minEA > num){
			CMAlert('최소 주문수량은 ' + minEA + '개 입니다.');
		}
		else if(num < 1){
			CMAlert('수량은 1 이상을 입력하셔야합니다.');
		}
		else{
			inp[0].value = num;
			if(typeof callback === 'function') callback.call(inp[0]);
		}
	},


	FindDaumAddress2 : function(e){
		e.preventDefault();
		var area = $(this).closest('.daumAddress');
		JCM.popPostCode(function(data) {
			area.find('input.zipcode').val(data.zonecode);
			area.find('input.address1').val(data.address);
			var sido = area.find('input.address_sido');
			var sigungu = area.find('input.address_sigungu');
			var bname = area.find('input.address_bname');
			var code = area.find('input.address_bcode');
			var build = area.find('input.building_name');
			if(sido.length) sido.val(data.sido);
			if(sigungu.length) sigungu.val(data.sigungu);
			if(bname.length) bname.val(data.bname);
			// 2018-02-02 rsu : bcode->zonecode로 변경
			//if(code.length) code.val(data.bcode.substr(0, 8));
			if(code.length) code.val(data.zonecode);
			if(data.buildingName.length>0) bname.val(data.bname+' '+data.buildingName);
		});
	},
	
	FindDaumAddress : function(e){
		e.preventDefault();
		var area = $(this).closest('.daumAddress');
		var flag = false;
		
		JCM.popPostCode(function(data) {
			var dataString = 'zipcode='+ data.zonecode;
			$.ajax({
	               type: "POST",
	               dataType : "json",
	               async : false, 
	               url: "/Mypage/MyInfo/storeList",
	               data: dataString,
	               success: function(data) {
	            	   if(data != null && data.message != null && typeof(data) != undefined && typeof(data.message) != undefined && data.message.length > 0){
	            		   alert(data.message);
	            		   return false;
	            	   } else {
	            		   $("#d_store_code").empty();
	            		   $("#d_store_code").append("<option value=''>선택</option>");
	            		   for(var i=0; i<data.length; i++){
	            			   $("#d_store_code").append("<option value='"+data[i].store_code+"'>"+data[i].nickname+"</option>");   
	            		   }
	            		   flag =true;
	            	   }
	               },
	               fail : function(data){
	            	   alert(data.message);
	               }
	         });
			if(flag){
				area.find('input.zipcode').val(data.zonecode);
				area.find('input.address1').val(data.address);
				var sido = area.find('input.address_sido');
				var sigungu = area.find('input.address_sigungu');
				var bname = area.find('input.address_bname');
				var code = area.find('input.address_bcode');
				var build = area.find('input.building_name');
				if(sido.length) sido.val(data.sido);
				if(sigungu.length) sigungu.val(data.sigungu);
				if(bname.length) bname.val(data.bname);
				// 2018-02-02 rsu : bcode->zonecode로 변경
				//if(code.length) code.val(data.bcode.substr(0, 8));
				if(code.length) code.val(data.zonecode);
				if(data.buildingName.length>0) bname.val(data.bname+' '+data.buildingName);
			}
		});
	},

	// 퀵바
	ClickQuickUpDown : function(e){
		e.preventDefault();
		var contents = $(this).closest('.quickContents');
		var li = contents.find('li');
		var page = parseInt(contents.attr('data-page'));
		var view = parseInt(contents.attr('data-view'));
		var lastPage = Math.ceil(li.length / view) - 1;
		if($(this).hasClass('upBtn')){
			if(page > 0) page -= 1;
			else page = lastPage;
		}
		else{
			if(page < lastPage) page += 1;
			else page = 0;
		}
		contents.attr('data-page', page);
		App.QuickGoodsViewByPage(contents);
	},

	QuickGoodsViewByPage : function(contents){
		var page = parseInt(contents.attr('data-page'));
		var view = parseInt(contents.attr('data-view'));

		contents.find('li').each(function(idx){
			var min = page * view;
			var max = min + view - 1;
			if(idx >= min && idx <= max) $(this).show();
			else $(this).hide();
		});
	},

	ClickQuickHeader : function(e){
		e.preventDefault();
		$(this).closest('.tab').addClass('active').siblings().removeClass('active');
		$(this).blur();
	},



	ClickLeftQBannerNav : function(){
		var wrap = $('#leftQuickBanner1 ul');
		var objs= wrap.find('li');
		var visibleObj = objs.filter(':visible');
		if(visibleObj.length > 1) return;
		if($(this).hasClass('right')){
			var nextObj = visibleObj.next();
			if(!nextObj.length) nextObj = objs.eq(0);
			nextObj.translate3d({x : '100%'}, {x : 0}, 400);
			visibleObj.translate3d({x : 0}, {x : '-100%'}, 400, function(){
				$(this).hide();
			});
			wrap.translate3d({height : visibleObj.outerHeight()}, {height : nextObj.outerHeight()}, 400);
		}
		else{
			var nextObj = visibleObj.prev();
			if(!nextObj.length) nextObj = objs.last();
			nextObj.translate3d({x : '-100%'}, {x : 0}, 400);
			visibleObj.translate3d({x : 0}, {x : '100%'}, 400, function(){
				$(this).hide();
			});
			wrap.translate3d({height : visibleObj.outerHeight()}, {height : nextObj.outerHeight()}, 400);
		}
	},




	ClickTodayViewDelete : function(){
		var id = $(this).attr('data-id');

		//console.log(decodeURIComponent(JCM.getCookie('todayView')));
		var data = JSON.parse(decodeURIComponent(JCM.getCookie('todayView')));
		$.each(data, function(idx, obj){
			if(obj.seq === id) delete(data[idx]);
		});
		JCM.setCookie('todayView', JSON.stringify(data), 60 * 60 * 24);
		var contents = $(this).closest('.quickContents');
		$(this).closest('li').remove();
		contents.closest('.tab').find('span.count').html(contents.find('li').length);
		App.QuickGoodsViewByPage(contents);
	},

	addWish : function(e){
		if(typeof(e) !== 'undefined') e.preventDefault();
		JCM.postWithLoading(this.href, {}, function(data){
			CMAlert('관심상품에 저장되었습니다.');
		});
	},
};

App.Init();

//
//    App Init
//
// ==============================================================
//
//    App Login
//

App.LoginLayer = {
	Init : function(){
		$(document).ready(function(){
			if(!$('#LoginRemember').length) return;
			var mid = JCM.getCookie('mid');
			if(mid != ''){
				$('input[name=mid]').val(mid);
				$('#LoginRemember')[0].checked = true;
			}

			if(jQuery.trim($('input[name=mid]').val()) == '') $('input[name=mid]').focus();
			else $('input[name=pwd]').focus();
		});

		$(document).on('click', '#LoginBtn', App.LoginLayer.Show);
		$(document).on('click', '#closeLoginLayer', App.LoginLayer.Hide);
		$(document).on('submit', '#LoginFrm', App.LoginLayer.FormSubmit);
	},

	Show : function(e){
		e.preventDefault();
		if($('#loginLayer').length) $('#loginLayer').show();

		if(!$('body')[0].hasAttribute('data-ovy')) $('body').attr('data-ovy', $('body').css('overflow-y'));
		$('body').css('overflow-y', 'hidden');
	},

	Hide : function(e){
		e.preventDefault();
		if($('#loginLayer').length) $('#loginLayer').hide();

		$('body').css('overflow-y', $('body')[0].hasAttribute('data-ovy') ? $('body').attr('data-ovy') : 'auto');
	},

	FormSubmit : function(e){
		var res = $(this).validCheck();
		if(!res) e.preventDefault();

		var mid = $(this).find('input[name=mid]');
		if(jQuery.trim(mid.val()) == ''){
			e.preventDefault();
			CMAlert('아이디를 입력하여 주세요.');
			return false;
		}
		var pwd = $(this).find('input[name=pwd]');

		if($('#LoginRemember').length && $('#LoginRemember')[0].checked){
			JCM.setCookie('mid', $('input[name=mid]').val(), 9999);
		}else{
			JCM.setCookie('mid', '1',0);
		}
	},
};

//
//    App Login
//
// ==============================================================
//
//    App MypPage Order
//

App.MypPageOrder = {
	Init : function(){
		$(document).on('click', '#ordSchForm input[name=fix_date]', this.ClickFixDate);
		$(document).on('change', '#ordSchForm select[name=step]', function(){
			$('#ordSchForm').submit();
		});
	},

	ClickFixDate : function(){
		var date_begin = $('#ordSchForm input[name=reg_date_begin]');
		var reg_date_end = $('#ordSchForm input[name=reg_date_end]');
		if(date_begin.length) date_begin.val('');
		if(reg_date_end.length) reg_date_end.val('');
		$('#ordSchForm').submit();
	},
};

//
//    App MypPage Order
//
// ==============================================================
//
//    App Order
//

App.Order = {
	fixIs : false,

	Init : function(){
		$(document).on('click', 'button.couponSelectBtn, #orderCouponBtn, #deliveryCouponBtn', this.ClickCouponSelect);
		$(document).on('click', '#sameFillBtn', this.SameFill);
		$(document).on('click', '#newFillBtn', this.NewFill);
		$(document).on('click', '#defaultFillBtn', this.DefaultFillBtn);
		$(document).on('click', '#lastOrderFillBtn', this.LastOrderFillBtn);
		$(document).on('submit', '#OrderWriteForm', this.Submit);
		$(document).on('submit', '#cpForm', this.CouponSubmit);
		$(document).on('click', '#couponSel', this.CouponSubmit2);
		$(document).on('focusout', '#MD_use_point', this.CheckUsePoint);
		$(document).on('click', '#pointAllBtn', this.UseAllPoint);
		$(document).on('keydown', '#OrderWriteForm select, #OrderWriteForm input', function(e){
			if(e.keyCode === 13){
				e.stopPropagation();
				e.preventDefault();
			}
		});

		$(document).on('click', '#paymentType input[name=settle_kind]', function(){
			if(this.value === 'hiwaypay') $('#paymentType .hipay').show();
			else $('#paymentType .hipay').hide();
			// 2018-02-07 rsu
			if(this.value === 'transfer' || this.value === 'ecm_cash' || this.value === 'hiwaypay' ) $('#paymentType .transfer_bill').show();
			else $('#paymentType .transfer_bill').hide();
			App.Order.GetAjaxPriceData();
		});

		// 2018-02-07 rsu
		$('#paymentType .transfer_bill').show();

		$(document).on('click', '#hiwaypayCheckBtn', function(e){
			App.Hiwaypay.GetRemain($('#hiwaypay_no').val(), $('#hiwaypay_pw').val(), function(point){
				console.log(point);
				$('#hiwaypay_remain_text').text('(사용 가능 금액 : ' + JCM.setComma(point) + ')');
				$('#hiway_remain_point').val(point);
			}, function(msg){
				CMAlert(msg);
			});
		});

		$(document).on('click', '#orderSubmitBtn', function(e){
			e.preventDefault();
			var form = document.getElementById('OrderWriteForm');
			var res = $(form).validCheck();
			if(!res){
				return false;
			}

			var agreeInp = ['buy_agree', 'pg_agree1', 'pg_agree2', 'pg_agree3'];
			for(var i=0; i < agreeInp.length; i++){
				var obj = $('input[name=' + agreeInp[i] + ']');
				if(obj.length){
					if(!obj.is(':checked')){
						CMAlert(obj.closest('.checkbox').children('span').text() + '하셔야 합니다.');
						return false;
					}
				}
			}

			var settleKind = $('#paymentType input[name=settle_kind]:checked').val();

			if(settleKind === 'hiwaypay'){
				form.action = '/Mall/Order/Proc/'+$('#contents').attr('data-ordno');
				form.target = '_self';
				var h_point = $('#hiway_remain_point').val();
				if(h_point == ''){
					CMAlert('하이웨이페이 잔액조회를 하시기 바랍니다.');
					return false;
				}
				else{
					h_point = parseInt(h_point);
					var total_price = parseInt($('#totalLastResult').val());
					if(h_point < total_price){
						CMAlert('하이웨이페이 잔액이 주문금액보다 적습니다. 다른 결제방식을 선택하여 주세요.');
						return false;
					}
					else $('#OrderWriteForm').submit();
				}
				return false;
			}

 
			// 2018-03-22 대면결제도 무통장입금과 같은 방식
			if( settleKind==='ecm_cash' || settleKind==='ecm_card'){
				form.action = '/Mall/Order/Proc/'+$('#contents').attr('data-ordno');
				form.target = '_self';
				$('#OrderWriteForm').submit();
				return false;
			}

			if(settleKind === 'transfer'){
				form.action = '/Mall/Order/Proc/'+$('#contents').attr('data-ordno');
				form.target = '_self';
				$('#OrderWriteForm').submit();
				return false;
			}

			// 이지페이모드
			if($('#EP_pay_type').length){
				form.action = '/Mall/Order/AjaxUpdate/'+$('#contents').attr('data-ordno');
				JCM.ajaxForm(form, function(){
					var kind = $('input[name=settle_kind]:checked').val();
					if(kind === 'transfer') $('#EP_pay_type').val('22');
					if(kind === 'pg_card') $('#EP_pay_type').val('11');
					if(kind === 'pg_transfer') $('#EP_pay_type').val('21');
					f_cert();
				});
			}
			else if($('#sp_pay_type').length){
				form.action = '/Mall/Order/AjaxUpdate/'+$('.contents').attr('data-ordno');
				JCM.ajaxForm(form, function(){
					var kind = $('input[name=settle_kind]:checked').val();
					if(kind === 'transfer') $('#sp_pay_type').val('22');
					if(kind === 'pg_card') $('#sp_pay_type').val('11');
					if(kind === 'pg_transfer') $('#sp_pay_type').val('21');
					f_cert();
				});
			}
		});

		$(document).on('click', '#hwmPointViewBtn', function(e){
			//JCM.showModal('modalHwmPointForm', 400, 250);

			// 2018-06-04 LS
			var jForm = $("form[name=hwmPForm]");			
				jForm.find("input[name=hwm_point_pw]").val('1111');	//	비밀번호 저장				 

			var form = document.getElementById('hwmPForm'),
				id = jForm.find('input[name=hwm_point_id]'),
				pw = jForm.find('input[name=hwm_point_pw]');

			JCM.ajaxForm(form, function(data){
				if($('#hwmPointView').length){
					$('#hwmPointView').html(JCM.setComma(data.point) + 'P');
					$('#hwmPointView').attr('data-point', data.point);
					$('#MD_use_point').attr('data-maxvalue', data.point);

					$('input.hwm_point_id').val(id.val());
					$('input.hwm_point_pw').val(pw.val());
				} 
			});
		});

		$(document).on('click', '#hwmPointUseBtn', function(){
			$('#MD_use_point').val($('#hwmPointView').attr('data-point'));
			App.Order.UpdatePoint();
		});
	},

	ClickCouponSelect : function(){
		JCM.getModal($(this).attr('data-href'), {}, '쿠폰선택', 'selectCouponModal', 300, 300);
	},

	SameFill : function(){
		App.Order.NewFill();
		$('#MD_to_name').val($('#MD_from_name').val());
		$('#MD_to_tel').val($('#MD_from_tel').val());
		$('#MD_to_cellphone').val($('#MD_from_cellphone').val());
		if($('#address1').length) $('#MD_to_address1').val($('#address1').val());
		if($('#address2').length) $('#MD_to_address2').val($('#address2').val());
		if($('#zipcode').length) $('#MD_to_zipcode').val($('#zipcode').val());
	},

	NewFill : function(){
		$('#MD_to_name').val('');
		$('#MD_to_tel').val('');
		$('#MD_to_cellphone').val('');
		$('#MD_to_address1').val('');
		$('#MD_to_address2').val('');
		$('#MD_to_zipcode').val('');
	},

	LastOrderFillBtn : function(){
		App.Order.NewFill();
		if($('#last_mname').length) $('#MD_to_name').val($('#last_mname').val());
		if($('#last_tel').length) $('#MD_to_tel').val($('#last_tel').val());
		if($('#last_cellphone').length) $('#MD_to_cellphone').val($('#last_cellphone').val());
		if($('#last_address1').length) $('#MD_to_address1').val($('#last_address1').val());
		if($('#last_address2').length) $('#MD_to_address2').val($('#last_address2').val());
		if($('#last_zipcode').length) $('#MD_to_zipcode').val($('#last_zipcode').val());
	},

	DefaultFillBtn : function(){
		App.Order.SameFill();
		if($('#d_address1').length) $('#MD_to_address1').val($('#d_address1').val());
		if($('#d_address2').length) $('#MD_to_address2').val($('#d_address2').val());
		if($('#d_zipcode').length) $('#MD_to_zipcode').val($('#d_zipcode').val());
	},

	Submit : function(e){
		var res = $(this).validCheck(this);
		if(!res){
			e.preventDefault();
			return false;
		}
	},

	CouponSubmit : function(e){
		e.preventDefault();
		JCM.ajaxForm(this, function(data){
			App.Order.GetAjaxPriceData();
		});
	},
	
	CouponSubmit2 : function(e){
		e.preventDefault();
		if(JCM.setComma($("#cpform2").find("input[type=radio]:checked").closest(".item").find("input[name=discount]").val()) < 100){
			var goodsAmt = $(".cartResult").find("dl.addPrice").find("dd").text().replace("원","").replace(/,/gi,"");
			var dcAmt = goodsAmt * JCM.setComma($("#cpform2").find("input[type=radio]:checked").closest(".item").find("input[name=discount]").val()) / 100; 
			$('#orderCouponDC').val(dcAmt);
			$(".cartDc").next().text(dcAmt+"원");
			$("#cart_dc").val(dcAmt);
			
			$(".cartResult").find("dl.sum").find("dd").text(JCM.setComma(goodsAmt-dcAmt)+"원"); 
			
		} else {
			var goodsAmt = $(".cartResult").find("dl.addPrice").find("dd").text().replace("원","").replace(/,/gi,"");
			$('#orderCouponDC').val(JCM.setComma($("#cpform2").find("input[type=radio]:checked").closest(".item").find("input[name=discount]").val()));
			$(".cartDc").next().text(JCM.setComma($("#cpform2").find("input[type=radio]:checked").closest(".item").find("input[name=discount]").val())+"원");
			$("#cart_dc").val($("#cpform2").find("input[type=radio]:checked").closest(".item").find("input[name=discount]").val());
			("#coupon_seq").val($("#cpform2").find("input[type=radio]:checked").closest(".item").find("input[name=coupon_seq]").val());
			
			$(".cartResult").find("dl.sum").find("dd").text(JCM.setComma(goodsAmt-$("#cpform2").find("input[type=radio]:checked").closest(".item").find("input[name=coupon_seq]").val())+"원");
			
		}
		JCM.removeModal('#selectCouponModal');
	},

	GetAjaxPriceData : function(){
		var settleKind = $('input[name=settle_kind]:checked');
		var sendData = {'settle_kind' : settleKind.length ? settleKind.val() : ''};
		JCM.getWithLoading($('#orderItemsArea').attr('data-href'), sendData, function(data){
			if(typeof(data.cart) !== 'undefined') $('#orderItemsArea').html(data.cart);
			if(typeof(data.result) !== 'undefined') $('#paymentResult').html(data.result);
			if(typeof(data.cart_dc) !== 'undefined') $('#orderCouponDC').val(JCM.setComma(data.cart_dc));
			if(typeof(data.delivery_dc) !== 'undefined') $('#deliveryCouponDC').val(JCM.setComma(data.delivery_dc));
			if(typeof(data.totalSavePoint) !== 'undefined') $('#totalSavePoint').html(JCM.setComma(data.totalSavePoint));
			if(typeof(data.total_price) !== 'undefined'){
				if($('#EP_product_amt').length) $('#EP_product_amt').val(data.total_price);
				if($('#sp_product_amt').length) $('#sp_product_amt').val(data.total_price);
			}

			JCM.removeModal('#selectCouponModal');
		});
	},

	CheckUsePoint : function(){
		var p = $(this).val();
		var max = $(this).attr('data-maxvalue');
		if(typeof(max) == "undefined"){
			max = '';
		}
		p = parseInt(p.replace(/[^0-9]/, ''));
		max = parseInt(max.replace(/[^0-9]/, ''));
		if($(this).val() == '') $(this).val('0');
		else if(p > max){
			CMAlert('사용가능한 최대 포인트는 ' + max + 'P 입니다.')
			$(this).val(0);
		}
		App.Order.UpdatePoint();
	},

	UseAllPoint : function(){
		$('#MD_use_point').val($('#MD_use_point').attr('data-maxvalue'));
		App.Order.UpdatePoint();
	},

	UpdatePoint : function(){
		var obj = $('#MD_use_point');
		var id = $('input.hwm_point_id').eq(0).val();
		var pw = $('input.hwm_point_pw').eq(0).val();
		var sync_id = $('input[name=hwm_sync_id]').val();
		JCM.post(obj.attr('data-href'), {point : obj.val(), 'hwm_point_id' : id, 'hwm_point_pw' : pw, 'hwm_sync_id' : sync_id}, function(data){
			
			if(data.admin=="y"){
				var goodsAmt = $(".cartResult").find("dl.addPrice").find("dd").text().replace("원","").replace(/,/gi,""); 
				var goodsAmt2 = $(".cartResult").find("dl.sum").find("dd").text().replace("원","").replace(/,/gi,"");
				
				if(goodsAmt == 0){
					goodsAmt = goodsAmt2;
				}
				
				if(parseInt(goodsAmt) < parseInt(data.use_point)){
					CMAlert("사용 포인트가 주문금액을 초과하였습니다.");
					obj.val('0');
					return false;
				}else {
					$(".usePoint").next().text(JCM.setComma(data.use_point)+"원");
					var goodsAmt3 = $(".cartResult").find("dl.addPrice").find("dd").text().replace("원","").replace(/,/gi,"");
          if(goodsAmt3 == 0){
            goodsAmt3 = goodsAmt;
          }
					$(".cartResult").find("dl.sum").find("dd").text(JCM.setComma(goodsAmt3-data.use_point)+"원");
					obj.val(data.use_point);
				}
				
			} else {
				obj.val(data.use_point);
				App.Order.GetAjaxPriceData();	
			}
			
		}, function(){
			obj.val('0');
		});
	},
};

App.Hiwaypay = {
	GetRemain : function(id, pw, successFunc, failFunc){
		if(id === ''){
			if(typeof failFunc === 'function'){
				failFunc('하이웨이페이 회원번호를 입력하여 주세요.');
				return;
			}
		}
		else if(pw === ''){
			if(typeof failFunc === 'function'){
				failFunc('하이웨이페이 회원비밀번호를 입력하여 주세요.');
				return;
			}
		}

		JCM.post('/Mall/Order/HiwaypayRemain', {id:id, pw:pw}, function(data){

			if(data.result){
				if(typeof successFunc === 'function'){
					successFunc(data.data.remain_point);
				}
			}
			else{
				if(typeof failFunc === 'function'){
					failFunc(data.message);
				}
			}

		}, function(data){
			if(typeof failFunc === 'function'){
				failFunc('하이웨이페이 통신오류');
			}
		});
	},
}

//
//    App Order
//
// ==============================================================
//
//    App Cart
//

App.Cart = {
	Init : function(){
		$(document).on('click', '.ea .ea-up', function(e){
			App.EAUp.call(this, e);
		});
		$(document).on('click', '.ea .ea-down', function(e){
			App.EADown.call(this, e);
		});
		$(document).on('click', '.eaArea button.btn', this.ModifyEA);

		$('#deleteItemsBtn').on('click', this.ClickDeleteItems);
		$('button.deleteOptBtn').on('click', this.ClickDeleteOpt);
		$('button.directOrdBtn').on('click', this.ClickDirectOrder);
		$('#selectOrderBtn').on('click', this.ClickSelectOrder);
	},

	ModifyEA : function(){
		JCM.get($(this).attr('data-href'), {'ea' : $(this).closest('.eaArea').find('input').val()}, function(data){
			CMAlert('변경되었습니다.');
			JCM.get($('#orderItemsArea').attr('data-href'), {}, function(data){
				$('#orderItemsArea').html(data);
			});
		});
	},

	ClickDeleteOpt : function(e){
		JCM.post($(this).attr('data-href'), {seq : $(this).attr('data-id')}, function(data){
			CMAlert('삭제되었습니다.', function(){
				location.reload();
			});
		});
	},
	ClickDeleteItems : function(e){
		var gSeq = [];
		var chk = $('.cartList input.checkItem:checked');
		if(!chk.length){
			CMAlert('삭제할 상품을 선택하여 주세요.');
			return;
		}
		chk.each(function(){
			gSeq.push(this.value);
		});

		JCM.post($(this).attr('data-href'), {goods_seq : gSeq}, function(data){
			CMAlert('삭제되었습니다.', function(){
				location.reload();
			});
		});
	},

	ClickDirectOrder : function(){
		JCM.post($(this).attr('data-href'), {}, function(data){
			location.href = '/Mall/Order/Direct';
		});
	},

	ClickSelectOrder : function(){
		var checkbox = $('input.checkItem:checked');
		if(!checkbox.length){
			CMAlert('주문할 상품을 선택하여 주세요.');
			return;
		}
		var gseq = [];
		checkbox.each(function(){
			gseq.push(this.value);
		})

		JCM.post($(this).attr('data-href'), {'goods_seq' : gseq}, function(data){
			location.href = '/Mall/Order/Direct';
		});
	},
};

//
//    App Cart
//
// ==============================================================
//
//    App Event
//

App.Event = {
	Init : function(){
		$('button.downCPBtn').on('click', this.ClickDownloadCP);
	},

	ClickDownloadCP : function(){
		JCM.post($(this).attr('data-href'), {}, function(){
			CMAlert('쿠폰을 다운받았습니다.');
			location.reload();
		}, function(){
			CMAlert('쿠폰 다운 오류');
		});
	}
};

//
//    App Event
//
// ==============================================================
//
//    App Always
//

App.Always = {

	Init : function(){
		$(document).on('click', '#alwaysCategory a', this.ClickCategory);
	},

	Init2 : function(){
		$(document).on('click', '#alwaysCategory a', this.ClickCategory2);
	},

	ClickCategory : function(e){
		e.preventDefault();
		var id = $(this).attr('data-id');

		$(this).closest('li').addClass('active').siblings().removeClass('active');

		var choiceSection = $('#always'+id);
		choiceSection.addClass('active').siblings().removeClass('active');

		$(window).scrollTop(choiceSection.offset().top);
	},

	ClickCategory2 : function(e){
		e.preventDefault();
		JCM.getModal(this.href, {}, $(this).attr('data-title'), 'alwaysModal', 1, 1);
	}
};

//
//    App Always
//
// ==============================================================
// ==============================================================
//
//    SNS Login
//

App.SNSLogin = {
	w : '',
	EventInitIs : false,
	kakaoScriptLoadIs : false,
	kakaoKey : '',
	facebookScriptLoadIs : false,
	facebookKey : '',
	myPageIs : false,

	WebIs : function(){
		var os = App.GetOS();
		if(os === 'android' || os === 'ipad' || os === 'iphone' || os === 'ipod') return false;
		return true;
	},

	Init : function(myPageIs){
		this.myPageIs = (typeof(myPageIs) === 'undefined') ? false : myPageIs;
		App.SNSLogin.KakaoScriptLoad(function(){
			Kakao.init(App.SNSLogin.kakaoKey);
		});

		if(!this.EventInitIs){
			this.EventInitIs = true;
			$(document).on('click', '#naverIdLogin', this.Naver);
			$(document).on('click', '#FacebookLogin', this.WebIs() ? this.FacebookWebLogin : this.Facebook);
			$(document).on('click', '#KakaotalkLogin', this.WebIs() ? this.KakaoWebLogin : this.Kakaotalk);
		}
	},

	Facebook : function(e){
		var _this = App.SNSLogin;
		if(typeof(e) !== 'undefined') e.preventDefault();
		facebookConnectPlugin.getLoginStatus(function(response){
			if(response.status == "connected"){
				_this.GetFacebookInfo(response);
			}else{
				facebookConnectPlugin.login(['email','public_profile'], function(response2){
					if(response2.status == "connected"){
						_this.GetFacebookInfo(response2);
					}
				}, function(error){
				});
			}
		}, function(error){
		});
	},

	GetFacebookInfo : function(data){
		facebookConnectPlugin.api(data.authResponse.userID + "/?fields=id,email,first_name,last_name,gender", [],
			function (result) {
				App.SNSPostLogin('Facebook', result);
			},
			function (error) {
			});
	},

	FacebookScriptLoad : function(callback){
		if(typeof FB === 'undefined' && !App.SNSLogin.facebookScriptLoadIs){
			window.fbAsyncInit = function() {
				FB.init({
					appId      : App.SNSLogin.facebookKey,
					cookie     : true,  // enable cookies to allow the server to access
				                        // the session
					xfbml      : true,  // parse social plugins on this page
					version    : 'v2.8' // use graph api version 2.8
				});
			};

			(function(d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id)) return;
				js = d.createElement(s); js.id = id;
				js.src = "https://connect.facebook.net/en_US/sdk.js";
				fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));

			App.SNSLogin.facebookScriptLoadIs = true;
			setTimeout(function(){
				App.SNSLogin.FacebookScriptLoad(callback);
			}, 500);
		}
		else if(typeof FB === 'undefined'){
			setTimeout(function(){
				App.SNSLogin.FacebookScriptLoad(callback);
			}, 500);
		}
		else if(typeof callback === 'function'){
			callback();
		}
	},

	FacebookWebLogin : function(e){
		if(typeof(e) !== 'undefined') e.preventDefault();

		// window.open($(this).attr('data-href'), 'SnsIdLogin', 'location=no, titlebar=0, resizable=0, scrollbars=yes, width=1000, height=700, status=0');


		App.SNSLogin.FacebookScriptLoad(function(){
			FB.getLoginStatus(function(response) {
				if(response.status == "connected"){
					App.SNSLogin.FacebookStatusChangeCallback(response);
				}else{
					FB.login(function(response) {
						App.SNSLogin.FacebookStatusChangeCallback(response);
					}, {scope: 'public_profile,email'});
				}
			});
		});
	},

	FacebookStatusChangeCallback : function(response){
		if (response.status === 'connected') {
			FB.api(response.authResponse.userID + "/?fields=id,email,first_name,last_name,gender", function(result) {
				result.accessToken = response.authResponse.accessToken;
				App.SNSPostLogin('Facebook', result);
			});
		} else {
		}

	},

	// cordova plugin add https://github.com/lihak/KakaoTalkCordovaPlugin --variable KAKAO_APP_KEY=
	Kakaotalk : function(e){
		var _this = App.SNSLogin;
		if(typeof(e) !== 'undefined') e.preventDefault();
		KakaoTalk.login(
			function (result) {
				KakaoTalk.session(
					function (result) {
						CMAlert('카카오톡으로 로그인되었습니다.', function(){
							App.SNSPostLogin('Kakaotalk', result);
						});
					},
					function (message) {
						// alert('KAKAOTALK LOGIN ERROR#2');
						// ERROR
						CMAlert(message);
					}
				);
			},
			function (message) {
				CMAlert('KAKAOTALK LOGIN ERROR#1');
				// ERROR
				//MessageModal(message);
			}
		);
	},

	KakaoScriptLoad : function(callback){
		if(typeof Kakao === 'undefined' && !App.SNSLogin.kakaoScriptLoadIs){
			$('body').append('<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>');
			App.SNSLogin.kakaoScriptLoadIs = true;
			setTimeout(function(){
				App.SNSLogin.KakaoScriptLoad(callback);
			}, 500);
		}
		else if(typeof Kakao === 'undefined'){
			setTimeout(function(){
				App.SNSLogin.KakaoScriptLoad(callback);
			}, 500);
		}
		else if(typeof callback === 'function'){
			callback();
		}
	},

	KakaoWebLogin : function(e){
		if(typeof(e) !== 'undefined') e.preventDefault();

		App.SNSLogin.KakaoScriptLoad(function(){
			Kakao.Auth.login({
				success: function(authObj) {
					Kakao.API.request({
						url: '/v1/user/me',
						success: function(res) {
							res.accessToken = authObj.access_token;
							res.nickname = res.properties.nickname;
							App.SNSPostLogin('Kakaotalk', res);
						},
						fail: function(error) {
							CMAlert('카카오톡 정보를 가져오지 못했습니다(#2)');
						}
					});

				},
				fail: function(err) {
					console.log(err);
					CMAlert('카카오톡 정보를 가져오지 못했습니다(#1)');
				}
			});
		});
	},

	Naver : function(e){
		if(typeof(e) !== 'undefined') e.preventDefault();

		window.open($(this).attr('data-href'), 'naverIdLogin', 'location=no, titlebar=0, resizable=0, scrollbars=yes, width=400, height=300, status=0');
	},
};


App.SNSPostLogin = function(type, data){
	// facebook : id, email, first_name, last_name, gender
	// google : email, userid, displayName, familyName, givenName
	// kakaotalk : id, nickname
	// naver : id, email, nickname, gender
	var sendData = {};
	sendData.sns = type;
	if(App.SNSLogin.myPageIs) sendData.mode = 'mypage';
	//sendData.uuid = device.uuid;
	switch(type){
		case 'Facebook':
			sendData.id = data.id;
			sendData.email = data.email;
			sendData.name = data.last_name + ' ' + data.first_name;
			sendData.nick = data.last_name + ' ' + data.first_name;
			sendData.sex = data.gender;
			sendData.accessToken = data.accessToken;
			break;
		case 'Google':
			sendData.id = data.userId;
			sendData.email = data.email;
			sendData.name = data.displayName;
			sendData.nick = data.displayName;
			sendData.accessToken = data.idToken;
			//sendData.test = 'test';
			break;
		case 'Kakaotalk':
			sendData.id = data.id;
			sendData.name = data.nickname;
			sendData.nick = data.nickname;
			sendData.accessToken = data.accessToken;
			break;
		case 'Naver':
			sendData.id = data.id;
			sendData.email = data.email;
			sendData.name = data.nickname;
			sendData.nick = data.nickname;
			sendData.sex = data.gender;
			break;
	}

	$.ajax({
		type: 'post'
		, dataType: 'json'
		, url: App.GetURL() + '/SnsLogin/AppCallback/' + type
		, data: sendData
		, async: false
		, success: function (data) {
			if(!data.result){
				CMAlert(data.message != '' ? data.message : '로그인 오류 #2');
				return;
			}
			else{
				if(App.SNSLogin.myPageIs){
					location.reload();
				}
				else{
					CMAlert('로그인되었습니다.', function(){
						location.reload();
					});
				}
			}
		}
		, error: function () {
			alert('로그인 오류 #1');
		}
	});
};


//
//    SNS Login
//
// ==============================================================
