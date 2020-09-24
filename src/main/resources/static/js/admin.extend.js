App.Admin.GoodsSelecting = {
	reloadIs : true,       // 상품 선택 다이얼로그를 닫았을 시 화면을 새로고침합니다.
	formAction : '',        // 상품 검색 경로를 설정
	formMethod : 'get',     // 상품 선택 다이얼로그를 불러올때 요청 방식
	formAddData : {},       // 상품 검색 시 추가로 보낼 데이타
	getListBeforeMethod : null,

	/**
	 * 인자로 버튼의 #아이디를 전달하고,
	 * 버튼의 href 속성의 경로에서 상품 리스트를 불러온다.
	 */
	Init : function(buttonId, selectingCallback, selectAllCallback){
		$(document).on('click', buttonId, App.Admin.GoodsSelecting.SetButton);
		$(document).on('click', '#goodsSelecting .paging a', App.Admin.GoodsSelecting.Paging);
		if(typeof(selectingCallback) === 'function'){
			$(document).on('click', '#goodsSelectingList a', selectingCallback);
		}
		if(typeof(selectAllCallback) === 'function'){
			$(document).on('click', '#selAllBtn', selectAllCallback);
		}

		$(document).on('submit', '#gsForm', function(e){
			e.preventDefault();
			$('#gsPage').val(1);
			App.Admin.GoodsSelecting.GetList();
		});
	},

	SetButton : function(e){
		e.preventDefault();
		App.Admin.GoodsSelecting.GetList(this.href);
	},

	Paging : function(e){
		e.preventDefault();
		$('#gsPage').val($(this).attr('data-page'));
		App.Admin.GoodsSelecting.GetList();
	},

	GetList : function(href){
		if(typeof this.getListBeforeMethod === 'function') this.getListBeforeMethod();

		if($('#goodsSelecting').length){
			if(App.Admin.GoodsSelecting.formAction !== '') $('#gsForm').attr('action', App.Admin.GoodsSelecting.formAction);
			$('#gsForm').attr('method', App.Admin.GoodsSelecting.formMethod);

			$.each(this.formAddData, function(k, v){
				if(!$('#gsForm').find('#goodsSelAddData' + k).length){
					$('#gsForm').append('<input type="hidden" name="selected_goods" value="' + v + '" id="goodsSelAddData'+k+'">');
				}
			});

			JCM.ajaxForm('#gsForm', function(data){
				$('#goodsSelecting').closest('.modal_contents').html(data);
				$('#goodsSelectingModal').show();
			});
		}
		else{
			JCM._ajax(href, this.formAddData, {type : App.Admin.GoodsSelecting.formMethod}, function(data){
				JCM.createModal('상품검색', 'goodsSelectingModal', data, 1000, 800);
				$('#goodsSelectingModal').data('close_method', function(){
					if(App.Admin.GoodsSelecting.reloadIs) location.reload();
				});
			});
		}
	}
};

App.Admin.GroupList = {
	Init : function(url, buttonId){
		App.GoodsCategory.Init(url);
		App.Admin.GoodsSelecting.Init(buttonId, this.SelectedGoods, this.SelectAll);
		$(document).on('change', '#group_name', this.GroupChange);
		$(document).on('submit', '#grAddForm', this.GroupAdd);
		$(document).on('click', '.group_goods_list .delBtn a', this.RemoveGoods);
		$(document).on('click', '#remove_group', this.RemoveGroup);
	},

	SelectedGoods : function(e){
		e.preventDefault();
		var obj = this;
		JCM.post(this.href, {switch : $(this).closest('article').hasClass('switchon') ? 0 : 1}, function(data){
			if(data.switch) $(obj).closest('article').addClass('switchon').removeClass('switchoff');
			else $(obj).closest('article').addClass('switchoff').removeClass('switchon');
		});
	},

	SelectAll : function(e){
		e.preventDefault();
		var article = $('#goodsSelectingList article');
		if(!article.length) return;

		var s = article.filter('.switchon').length < article.length ? 1 : 0;
		var href = article.eq(0).find('a').attr('href');

		var seq = [];
		article.each(function(){
			seq.push($(this).attr('data-id'));
		});

		JCM.post(href, {ids : seq, switch : s}, function(data){
			if(s) article.addClass('switchon').removeClass('switchoff');
			else article.addClass('switchoff').removeClass('switchon');
		});
	},

	GroupChange : function(e){
		location.href = $(this).attr('data-href') + $(this).val();
	},

	GroupAdd : function(e){
		e.preventDefault();
		JCM.ajaxForm(this, function(data){
			location.href = $('#group_name').attr('data-href') + data.id;
		});
	},

	RemoveGoods : function(e){
		e.preventDefault();
		var obj = this;
		JCM.post(this.href, {switch : 0}, function(data){
			$(obj).closest('article').remove();
		});
	},

	RemoveGroup : function(e){
		if($('#group_name').val() === ''){
			CMAlert('삭제할 분류를 선택하여 주세요.');
		}
		var obj = this;
		CMConfirm('정말 삭제하시겠습니까?', function(){
			JCM.post($(obj).attr('data-href'), {id : $('#group_name').val()}, function(data){
				location.href = $('#group_name').attr('data-href');
			});
		});
	},
};

App.Admin.Upload = {
	Init : function(url, buttonId){
		App.GoodsCategory.Init(url);
		App.Admin.GoodsSelecting.Init(buttonId, this.SelectedGoods, this.SelectAll);
	},
};

App.Admin.Coupon = {
	Init : function(GoodsCategoryUrl, buttonId){
		App.GoodsCategory.url = GoodsCategoryUrl;
		App.GoodsWrite.categoryBtnSet();

		this.RadioSet();

		App.Admin.GoodsSelecting.Init(buttonId, this.SelectedGoods);
		App.Admin.GoodsSelecting.formMethod = 'post';
		App.Admin.GoodsSelecting.reloadIs = false;
		App.Admin.GoodsSelecting.getListBeforeMethod = function(){
			var goods = '';
			$('#selectedGoodsArea li').each(function(){
				goods += (goods === '' ? '' : ',') + $(this).attr('data-id');
			});
			App.Admin.GoodsSelecting.formAddData.selected_goods = goods;
		}

		$(document).on('click', '#selectedGoodsArea a.goodsRemoveBtn', this.RemoveGoods);


		$(document).on('submit', '#CouponWriteForm', function(e){
			var res = $(this).validCheck();
			if(!res){
				e.preventDefault();
				return false;
			}

			var ctgr = '';
			$('div.categorySelect').each(function () {
				var find = '';
				$(this).find('select').each(function () {
					if ($.trim($(this).val()) != '') find = $.trim($(this).val());
				});
				if (find != '') ctgr += (ctgr == '' ? '' : ',') + find;
			});

			$('#selected_category').val(ctgr);

			var goods = '';
			$('#selectedGoodsArea li').each(function () {
				goods += (goods == '' ? '' : ',') + $(this).attr('data-id');
			});

			$('#selected_goods').val(goods);

		});

		$(document).on('click', 'button.inpClearBtn', function(e){
			$(this).closest('td').find('input').val('');
		});
	},

	// 라디오 버튼 클릭 시  입력창이 보이거나 감춤
	RadioSet : function(){
		$(document).on('click', '#contents.CouponWrite input[type=radio]', function(){
			radioClick(this);
		});

		$(document).on('click', 'input[name^=create_type]', function(){
			var radio = $('input[name=coupon_type]:visible');
			if(radio.length) radio.eq(0)[0].checked = true;
		});

		function radioClick(obj){
			$('.toggle_' + obj.name).each(function(){
				var attrName = 'data-' + obj.name.replace(/_/ig, '-');
				if(!this.hasAttribute(attrName)) return;
				var data = $(this).attr('data-' + obj.name.replace(/_/ig, '-')).split(',');
				// console.log(data, obj.value);
				if($.inArray(obj.value, data) !== -1) $(this).show();
				else $(this).hide();
			});
		}

		$('#contents.CouponWrite input[type=radio]:checked').each(function(){
			radioClick(this);
		});
	},

	// 상품 선택 시
	SelectedGoods : function(e){
		e.preventDefault();
		var article = $(this).closest('article');
		var id = article.attr('data-id');
		if(!article.hasClass('switchon')){
			article.addClass('switchon').removeClass('switchoff');
			var html = '<li id="selectedGoods' + id + '" data-id="' + id + '">' +
				'<a href="#" class="goodsRemoveBtn"><i></i>삭제</a>' +
				'<span><img src="' + article.find('.img img').attr('src') + '"></span>' +
				'<b>' + article.find('.goods_name').text() + '</b>' +
				'</li>';
			$('#selectedGoodsArea').append(html);
		}
		else{
			article.addClass('switchoff').removeClass('switchon');
			$('#selectedGoods' + id).remove();
		}
	},

	RemoveGoods : function(e){
		e.preventDefault();
		$(this).closest('li').remove();
	},
};

App.Admin.Order = {
	Init : function(GoodsCategoryUrl, buttonId){
		App.GoodsCategory.url = GoodsCategoryUrl;
		App.GoodsWrite.categoryBtnSet();

		App.Admin.GoodsSelecting.Init(buttonId, this.SelectedGoods);
		App.Admin.GoodsSelecting.reloadIs = false;
		App.Admin.GoodsSelecting.formMethod = 'get';

		// SUBMIT
		$(document).on('submit', '#OrderWriteForm', this.SubmitForm);
		$(document).on('click', '.cancelBtn', function(e){
			e.preventDefault();
			var form = document.getElementById('act');
			form.action = this.href;
			form.submit();
		});

		$(document).on('click', '#changeStepBtn', function(){
		});

		$(document).on('submit', '#goodsForm', function(e){
			e.preventDefault();
		});

		$(document).on('click', '#putItemBtn', this.ItemPut);

		$(document).on('click', 'button.itemDeleteBtn', function(e){
			$(this).closest('tr').remove();

			// 합계 계산
			App.Admin.Order.ItemTotalCalculate();
		});

		$(document).on('click', '#selectedOptionArea .removeBtn', App.Goods.removeOption);

		$(document).on('click', '.ea .ea-up', function(e){
			App.EAUp.call(this, e, App.Goods.EAChangeAfter);
			
//			$(this).closest('tr').find('.a_price').text().replace(/,/gi, '').replace('원','')
			
			var price = parseInt($(this).closest('tr').find("input[name=_ori_price]").val());
			var ea = parseInt($(this).closest('tr').find("input[name='ea[]']").val());
			
			$(this).closest('tr').find('.o_price').text(JCM.setComma(price*ea)+" 원");
			$(this).closest('tr').find('.a_price').text(JCM.setComma(price*ea)+" 원");

			if( $("#cartListBody").find("tr.g_"+$(this).attr("value")).length>0 )
			{
				// 합계 계산
				App.Admin.Order.ItemTotalCalculate();
			}
		});
		$(document).on('click', '.ea .ea-down', function(e){
			App.EADown.call(this, e, App.Goods.EAChangeAfter);

			var price = parseInt($(this).closest('tr').find("input[name=_ori_price]").val());
			var ea = parseInt($(this).closest('tr').find("input[name='ea[]']").val());
			
			$(this).closest('tr').find('.o_price').text(JCM.setComma(price*ea)+" 원");
			$(this).closest('tr').find('.a_price').text(JCM.setComma(price*ea)+" 원");
			
			if( $("#cartListBody").find("tr.g_"+$(this).attr("value")).length>0 )
			{
				// 합계 계산
				App.Admin.Order.ItemTotalCalculate();
			}
		});
		$(document).on('keyup', '.ea input', function(e){
			App.Goods.EAChangeAfter.call(this);

			// 합계 계산
			App.Admin.Order.ItemTotalCalculate();
		});
	},

	// 상품 선택 시
	SelectedGoods : function(e){
		e.preventDefault();
		var article = $(this).closest('article');
		JCM.getModal(this.href, {}, '상품 선택', 'optionSelect', 800, 600);
	},

	// SUBMIT
	SubmitForm : function(e){
		var thisObj = this;
		var res = $(this).validCheck();
		if(!res){
			e.preventDefault();
			return false;
		}
		if($(this).attr('data-mode') == 'Modify'){

			if(!this.hasAttribute('data-price-check') || $(this).attr('data-price-check') != 'y'){
				e.preventDefault();
				$(this).attr('action', $(this).attr('data-check-href'));
				JCM.ajaxForm(this, function(){
					$(thisObj).attr('data-price-check', 'y');
					$(thisObj).submit();
				}, function(data){
					if(data === 'PRICE_UP'){
						CMConfirm('주문변경 시 결제금액보다 금액이 상승하게 됩니다. 계속하시겠습니까?', function(){
							$(thisObj).attr('data-price-check', 'y');
							$(thisObj).submit();
						});
					}
				});
			}
			else $(this).attr('action', $(this).attr('data-submit-href'));
		}

		$(this).attr('action', $(this).attr('data-submit-href'));
	},

	ItemHtml : function(data){
		var frozen = data.frozen;
		if(frozen == 1){
			frozen = ' [냉장]';
		} else if(frozen == 2){
			frozen = ' [냉동]';
		} else {
      frozen  = '';
    }
		return '<tr class="listTr g_'+data.goods_seq+'">'+
				'<td>' +
				'	<input type="hidden" name="goods_seq[]" value="' + data.goods_seq + '">' +
				'	<input type="hidden" name="opt_category[]" value="' + data.opt_category + '">' +
				'	<input type="hidden" name="opt_name[]" value="' + data.goods_name +'">' + 
				'	<input type="hidden" name="_ori_price" value="' + data.price + '">' + 
				'	<img src="' + data.image + '">' +
				'</td>' +
				'<td class="left">' +
				'	<p class="goods_name">' + data.goods_name + frozen + '</p>' +
				'	<p class="opt_name">' + data.opt_name + '</p>' +
				'</td>' +
				'<td><div class="ea"><input type="text" name="ea[]" value="' + data.ea + '" readonly><button class="ea-up" type="button" value="'+data.goods_seq+'" data-max-ea="'+data.max_ea+'" data-min-ea="'+data.min_ea+'">수량올리기</button><button class="ea-down" type="button" value="'+data.goods_seq+'" data-max-ea="'+data.max_ea+'" data-min-ea="'+data.min_ea+'">수량내리기</button></div></td>' +
				'<td class="o_price">' + JCM.setComma(data.price*data.ea) + ' 원</td>' +
				'<td>' + data.event_dc + '</td>' +
				'<td>' + data.member_dc + '</td>' +
				'<td></td>' +
				'<td>' + data.d_price + '</td>' +
				'<td class="a_price">' + JCM.setComma(data.price * data.ea - data.event_dc - data.member_dc * data.ea) + ' 원</td>' +
				'<td class="cartAdminOpt"><button type="button" class="sBtn itemDeleteBtn">삭제</button></td>' +
			'</tr>';
	},

	ItemPut : function(e){
		e.preventDefault();


		if(typeof(e) !== 'undefined') e.preventDefault();
		var existsOption = $('#optionSelectArea').length;
		if(existsOption){
			if(!$('.selectedOption').length){
				CMAlert('상품 옵션을 선택하여 주세요.');
				return;
			}
			$('.selectedOption').each(function(e){
				var data = {
					opt_category : $(this).attr('data-key'),
					goods_seq : $('#goodsSeq').val(),
					goods_name : $('#goodsForm header b').text(),
					opt_name : $(this).attr('data-option-name'),
					ea : $(this).find('.ea input').val(),
					image : $('#goodsImages img').attr('src'),
					price : $(this).attr('data-price'),
					event_dc : '',
					member_dc : '',
					d_price : '',
					frozen : $("#frozen").val(),
					max_ea : $("#max_ea").val(),
					min_ea : $("#min_ea").val()
				};
				$('#cartListBody').append(App.Admin.Order.ItemHtml(data));
			});
		}
		else{

			var data = {
				opt_category : '',
				goods_seq : $('#goodsSeq').val(),
				goods_name : $('#goodsForm header b').text(),
				opt_name : '',
				ea : $('#goodsEa').val(),
				image : $('#goodsImages img').attr('src'),
				price : $('#goodsEa').attr('data-price'),
				event_dc : '',
				member_dc : '',
				d_price : '',
				frozen : $("#frozen").val(),
				max_ea : $("#max_ea").val(),
				min_ea : $("#min_ea").val()
			};

			$('#cartListBody').append(App.Admin.Order.ItemHtml(data));

			App.Admin.Order.ItemTotalCalculate();
		}

		JCM.removeModal('#optionSelect');
	},

	// 금액 합계
	ItemTotalCalculate : function(e){		
		if( $("#cartListBody").find(".listTr").length>0 )
		{
			
//			var _sum = $(".cartResult").find("dl.addPrice").find("dd").text().replace(/,/gi, "").replace(/원/gi, "")*1;
			var _sum = 0;
			$.each($("#cartListBody").find(".listTr"), function(){		
				var _price = parseInt($(this).find("input[name=_ori_price]").val());
				var _ea = parseInt($(this).find("input[name='ea[]']").val());

				_sum += (_price*_ea);
			});
			
			var cartDc = $(".cartDc").next().text().replace(/,/gi, "").replace(/원/gi, "")*1;
			var point = $(".usePoint").next().text().replace(/,/gi, "").replace(/원/gi, "")*1;
			
			$(".cartResult").find("dl.addPrice").find("dd").text( JCM.setComma(_sum)+"원" );
			$(".cartResult").find("dl.goodsPrice").find("dd").text( JCM.setComma(_sum)+"원" );
			$(".cartResult").find("dl.sum").find("dd").text( JCM.setComma(_sum-cartDc-point)+"원" );
		}
		else
		{
			$(".cartResult").find("dl.addPrice").find("dd").text( "0원" );
			$(".cartResult").find("dl.goodsPrice").find("dd").text( "0원" );
		}
	},

	ListInit : function(){
		$(document).on('submit', '#prtOrdForm', this.SubmitPrintOrder);
	},

	SubmitPrintOrder : function(e){
		e.preventDefault();
		var chk = $('input[name^=ordSeq]:checked');
		var autoStepChk = $('#autoStepChk').length ? $('#autoStepChk')[0].checked : false;
		if(!chk.length){
			CMAlert('출력할 주문을 선택하여 주세요.');
			return false;
		}

		var id = '';
		chk.each(function(){
			id += (id == '' ? '' : ',') + this.value;
		});

		$('#prtOrdSeq').val(id);

		this.submit();

		//console.log(autoStepChk, id);
		if($('#prtOrdStep').val() === '30'){
			setTimeout(function(){
				if(autoStepChk){
					JCM.post($('#prtOrdForm').attr('data-step-href'), {seq : id}, function(data){
						location.reload();
					});
				}
				else if(0){
					CMConfirm('선택한 주문을 상품 준비중으로 이동하시겠습니까?', function(){
						JCM.post($('#prtOrdForm').attr('data-step-href'), {seq : id}, function(data){
							location.reload();
						});
					});
				}
			}, 1000);
		}

	},

};

App.Admin.AddressSelect = {
	Init : function(){
		$('.bAdrSel select.adrSiGunGu').each(function(){
			App.Admin.AddressSelect.SetSiGunGu(this);
		});

		$('.bAdrSel select.adrBName').each(function(){
			App.Admin.AddressSelect.SetBName(this);
		});

		$(document).on('change', '.bAdrSel select.adrSiDo', function(){
			var area = $(this).closest('.bAdrSel');
			var sigunguObj = area.find('select.adrSiGunGu');
			App.Admin.AddressSelect.SetSiGunGu(sigunguObj, $(this).val());
		});

		$(document).on('change', '.bAdrSel select.adrSiGunGu', function(){
			var area = $(this).closest('.bAdrSel');
			var bnameObj = area.find('select.adrBName');
			App.Admin.AddressSelect.SetBName(bnameObj, $(this).val());
		});
	},

	SetSiGunGu : function(obj, val, selectVal){
		if(typeof(selectVal) === 'undefined') selectVal = '';

		$(obj).html('<option value="">시/군/구</option>');
		if(typeof(val) !== 'undefined' && val !== ''){
			JCM.get('/Common/AjaxAdrSiGunGu', {'code' : val}, function(data){
				var html = '';
				$.each(data, function(idx, val){
					html += '<option value="' + idx + '"' + (selectVal == idx ? ' selected' : '') + '>' + val + '</option>';
				});
				obj.append(html);
			});
		}

		this.SetBName($(obj).closest('.bAdrSel').find('select.adrBName'), selectVal);
	},

	SetBName : function(obj, val, selectVal){
		if(typeof(selectVal) === 'undefined') selectVal = '';

		$(obj).html('<option value="">읍/면/동</option>');
		if(typeof(val) !== 'undefined' && val !== ''){
			JCM.get('/Common/AjaxAdrBName', {'code' : val}, function(data){
				var html = '';
				$.each(data, function(idx, val){
					html += '<option value="' + idx + '"' + (selectVal == idx ? ' selected' : '') + '>' + val + '</option>';
				});
				obj.append(html);
			});
		}
	},
},

App.Admin.StoreArea = {
	Init : function(){

		App.Admin.AddressSelect.Init();

		$(document).on('submit', '#AreaWriteForm', this.WriteSubmit);

		$(document).on('change', '#storeSelect', function(){
			$('#muid1').val($(this).val());
			$('#muid2').val($(this).val());

			$('#schForm').FormReset();

			App.Admin.StoreArea.GetList();
		});

		$(document).on('submit', '#schForm', function (e) {
			e.preventDefault();
			App.Admin.StoreArea.GetList();
		});

		$(document).ready(function(){
			App.Admin.StoreArea.GetList();
		});

		$(document).on('click', 'button.deleteBtn', function(){
			var obj = this;
			CMConfirm('정말 삭제하시겠습니까?', function(){
				JCM.post($(obj).attr('data-href'), {}, function(){
					CMAlert('삭제되었습니다.', function(){
						App.Admin.StoreArea.GetList();
					});
				});
			});
		});
	},

	WriteSubmit : function(e){
		e.preventDefault();
		var res = $(this).validCheck();
		if (!res) return false;

		var addr = $('#address_code1 option:selected').text();
		if($('#address_code2').val() !== '') addr += '|' + $('#address_code2 option:selected').text();
		if($('#address_code3').val() !== '') addr += '|' + $('#address_code3 option:selected').text();
		$('#MD_address').val(addr);

		JCM.ajaxForm(this, function(){
			CMAlert('등록되었습니다.', function(){
				$('#schForm').FormReset();
				App.Admin.StoreArea.GetList();
			});
		});
	},

	GetList : function(){
		JCM.ajaxForm('#schForm', function(data){
			$('#areaList').html(data);
		});
	},
};

App.Admin.SortChange = {
	obj : null,
	url : '',

	Init : function(url){
		this.url = url;
		$(document).on('mousedown', '#contents .group_goods_list .img img', this.MouseDown);

		// 마우스 이동
		$(document).on('mousemove', this.MouseMove);

		$(document).on('mouseup', '#contents .group_goods_list article', this.MouseUp);

		$(document).on('click', '.moveHeadBtn a, .moveTailBtn a', this.UpDownClick);
	},

	MouseDown : function(e){
		e.preventDefault();
		e.stopPropagation();
		App.Admin.SortChange.obj = {
			el : $(this).closest('article'),
			x : e.pageX,
			y : e.pageY,
		};
	},

	MouseMove : function(e){
		e.preventDefault();
		if(App.Admin.SortChange.obj === null) return;

		if(Math.abs(e.pageX - App.Admin.SortChange.obj.x) > 5 || Math.abs(e.pageY - App.Admin.SortChange.obj.y) > 5){
			if(!$('#dragGoodsTemp').length){
				$('body').append('<div id="dragGoodsTemp">' + App.Admin.SortChange.obj.el.find('.info b').text() + '</div>');
				$('#dragGoodsTemp').css({
					'position' : 'absolute',
					'line-height' : '30px',
					'background' : 'white',
					'border' : '1px solid #555',
					'opacity' : '0.8',
					'color' : '#333'
				});
			}

			$('#dragGoodsTemp').css({
				'top' : e.pageY + 'px',
				'left' : (e.pageX + 5) + 'px'
			});
		}
	},

	MouseUp : function(e){
		if($('#dragGoodsTemp').length) $('#dragGoodsTemp').remove();
		if(App.Admin.SortChange.obj === null) return;
		var cpObj = {};
		jQuery.extend(cpObj,App.Admin.SortChange.obj);
		App.Admin.SortChange.obj = null;
		var article = $(this);
		if(cpObj.el.is(article)){
			cpObj = null;
			return;
		}

		JCM.getWithLoading(App.Admin.SortChange.url, {select : cpObj.el.attr('data-id'), target : article.attr('data-id')}, function(){
			if(cpObj.el.index() > article.index()) article.before(cpObj.el);
			else article.after(cpObj.el);
		}, function(){
		});
	},

	UpDownClick : function(e){
		e.preventDefault();
		JCM.getWithLoading(this.href, {}, function(){
			location.reload();
		});
	},
};

/*
 *  최상위 클래스 keyValueArea (attribute : data-id)
 *  카테고리 영역 태그 아이디는 {firstKeyName}CategoryArea
 *  아이템 영역 태그 아이디는 {firstKeyName}_{Category}_Area
 *  button.addCategoryBtn
 *  button.delCategoryBtn
 *
 *  button.addItemBtn(attribute : data-category)
 *  button.delItemBtn
 */
App.Admin.KeyValue = {
	CategoryInit : function(){
		$(document).on('click', 'button.addCategoryBtn', function(){
			App.Admin.KeyValue.AddCategory(App.Admin.KeyValue.GetID(this));
		});
		$(document).on('click', 'button.delCategoryBtn', function(){
			$(this).closest('article').remove();
		});
	},

	ItemInit : function(){
		$(document).on('click', 'button.addItemBtn', function(){
			App.Admin.KeyValue.AddItem(App.Admin.KeyValue.GetID(this), $(this).attr('data-category'));
		});
		$(document).on('click', 'button.delItemBtn', function(){
			$(this).closest('article').remove();
		});
	},

	AddCategory : function(id, k, v){
		var parentObj = null;
		$('.keyValueArea').each(function(){
			if($(this).attr('data-id') === id){
				parentObj = this;
				return;
			}
		});
		if(parentObj === null) return;

		if(typeof(k) === 'undefined') k = '';
		if(typeof(v) === 'undefined') v = '';
		var html = '<article style="padding:2px;">' +
			'<label>분류키 : <input type="text" name="' + id + 'CategoryKey[]" value="' + JCM.replaceTag(k) + '" class="engonly w10p" required></label>' +
			'<label>분류명 : <input type="text" name="' + id + 'CategoryName[]" value="' + JCM.replaceTag(v) + '" class="w30p" required></label>' +
			'<button type="button" class="sBtn delCategoryBtn">삭제</button>' +
			'</article>';
		$(parentObj).find('.CategoryArea').append(html);
	},

	AddItem : function(id, c, k, v){
		var parentObj = null;
		$('.keyValueArea').each(function(){
			if($(this).attr('data-id') === id){
				parentObj = this;
				return;
			}
		});
		if(parentObj === null) return;

		var itemArea = null;
		$(parentObj).find('.ItemArea').each(function(){
			if($(this).attr('data-category') === c){
				itemArea = this;
				return;
			}
		});
		if(itemArea === null) return;

		if(typeof(k) === 'undefined') k = '';
		if(typeof(v) === 'undefined') v = '';
		var html = '<article style="padding:2px;">' +
			'<label>항목키 : <input type="text" name="' + id + '_' + JCM.replaceTag(c) + 'Key[]" value="' + JCM.replaceTag(k) + '" class="engonly w10p" required></label>' +
			'<label>항목명 : <input type="text" name="' + id + '_' + JCM.replaceTag(c) + 'Val[]" value="' + JCM.replaceTag(v) + '" class="w30p" required></label>' +
			'<button type="button" class="sBtn delItemBtn">삭제</button>' +
			'</article>';
		$(itemArea).append(html);
	},

	GetID : function(obj){
		return $(obj).closest('.keyValueArea').attr('data-id');
	},
};