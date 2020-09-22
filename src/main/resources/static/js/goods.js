App.Goods = new function(){
	var _this = this;
	this.url = '';

	this.goods_seq = null;

	this.Init = function(u){
		this.url = u;
		if(!$('#goodsSeq').length){
			CMAlert('상품번호 오류.', function(){
				history.back();
			});
		}

		$(document).on('click', '#addCartBtn', this.addCart);
		$(document).on('click', '#directBuyBtn', this.directBuy);
		$(document).on('click', '#goodsImages a', this.ThumbClick);
		if($('#goodsImages li a').length){
			$('#goodsImages li a').eq(0).trigger('click');
		}
		$(document).on('click', '#goodsReviewList .paging a, #goodsQAList .paging a', this.BoardPageClick);
		$(document).on('change', '#goodsReviewList .opt select', this.SelectChange);

		$(document).on('click', '.goodsWriteViewBtn', this.WriteView);

		$(document).on('submit', '.goodsBoardWrite form', this.WriteSubmit);

		$(document).on('click', '.goodsBoardWrite form button[type=reset]', this.WriteReset);

		$(document).on('click', '.goodsTab a', function(e){
			e.preventDefault();
			location.replace(this.href);
		});

		$(document).on('click', '#selectedOptionArea .removeBtn', this.removeOption);

		$(document).on('click', '.ea .ea-up', function(e){
			App.EAUp.call(this, e, App.Goods.EAChangeAfter);
		});
		$(document).on('click', '.ea .ea-down', function(e){
			App.EADown.call(this, e, App.Goods.EAChangeAfter);
		});
		$(document).on('keyup', '.ea input', function(e){
			App.Goods.EAChangeAfter.call(this);
		});

		this.goods_seq = $('#goodsSeq').val();
	};

	this.removeOption = function(e){
		e.preventDefault();
		var obj = $(this).closest('.selectedOption');
		CMConfirm('해당 상품을 삭제하시겠습니까?', function(){
			obj.remove();
			if($('#opt3sel').length) $('#opt3sel').selectVal('');
			else if($('#opt2sel').length) $('#opt2sel').selectVal('');
			else if($('#opt1sel').length) $('#opt1sel').selectVal('');
		});
	};

	this.ThumbClick = function(e){
		e.preventDefault();
		$(this).closest('li').addClass('active').siblings().removeClass('active');
		$('#goodsImages header').html('<img src="' + $(this).find('img')[0].src + '">');
	};

	this.EAChangeAfter = function(){
		var box = $(this).closest('.selectedOption');
		var price = parseInt(box.attr('data-price'));
		var n = parseInt(this.value);
		box.find('.price').html(JCM.setComma(n * price) + '원');
	};

	// =================================================================
	//
	//      상품 관련 게시물
	//
	this.WriteView = function(e){
		e.preventDefault();
		$(this).closest('.goodsBoardList').find('.goodsBoardWrite').toggle();
	};

	this.BoardPageClick = function(e){
		e.preventDefault();
		var parent = $(this).closest('.goodsBoardList');
		JCM.get(this.href, {}, function(data){
			parent.html(data);
		});
	};

	this.SelectChange = function(){
		var parent = $(this).closest('.goodsBoardList');
		JCM.get($(this).attr('data-url'), {'sort' : $(this).val()}, function(data){
			parent.html(data);
		});
	};

	this.WriteSubmit = function(e){
		e.preventDefault();
		var rd = $(this).attr('data-redirect');
		var parent = $(this).closest('.goodsBoardList');
		JCM.ajaxForm(this, function(){
			CMAlert('작성완료되었습니다.', function(){
				JCM.getWithLoading(rd, {}, function(data){
					parent.html(data);
				});
			});
		});
	};

	this.WriteReset = function(){
		$(this).closest('.goodsBoardWrite').hide();
	};

	/*
	* 장바구니 담기 버튼
	*/
	this.addCart = function(e){
		if(typeof(e) !== 'undefined') e.preventDefault();
		var existsOption = $('#optionSelectArea').length;
		if(existsOption){
			if(!$('.selectedOption').length){
				CMAlert('상품 옵션을 선택하여 주세요.');
				return;
			}
			var options = [];
			$('.selectedOption').each(function(e){
				options.push({
					category : $(this).attr('data-key'),
					name : $(this).attr('data-option-name'),
					ea : $(this).find('.ea input').val()
				});
			});

			JCM.post(this.href, {options : options}, function(data){
				CMConfirm('장바구니에 담았습니다. 장바구니로 이동하시겠습니까?', function(){
					location.href = '/Mall/Cart';
				});
			});
		}
		else{
			JCM.post(this.href, {ea : $('#goodsEa').val()}, function(data){
				CMConfirm('장바구니에 담았습니다. 장바구니로 이동하시겠습니까?', function(){
					location.href = '/Mall/Cart';
				});
			});
		}
	};

	this.directBuy = function(e){
		if(typeof(e) !== 'undefined') e.preventDefault();
		var existsOption = $('#optionSelectArea').length;
		if(existsOption){
			if(!$('.selectedOption').length){
				CMAlert('상품 옵션을 선택하여 주세요.');
				return;
			}
			var options = [];
			$('.selectedOption').each(function(e){
				options.push({
					category : $(this).attr('data-key'),
					name : $(this).attr('data-option-name'),
					ea : $(this).find('.ea input').val()
				});
			});

			JCM.post(this.href, {options : options}, function(data){
				location.href = '/Mall/Order/Direct';
			});
		}
		else{
			JCM.post(this.href, {ea : $('#goodsEa').val()}, function(data){
				location.href = '/Mall/Order/Direct';
			});
		}
	};

	this.Option = {

		initIs : false,

		CategoryLength : 0,

		Init : function(){
			App.Goods.Option.GetOption('');
			if(this.initIs) return;
			this.initIs = true;
			$(document).on('change', '#optionSelectArea select.option', App.Goods.Option.OptionChange);
		},

		GetOption : function(category){
			JCM.get($('#optionSelectArea').attr('data-href'), {category : category}, function(data){
				var html = App.Goods.Option.DataToHtml(data);
				$('#optionSelectArea').append(html);
			});
		},

		DataToHtml : function(data){
			var opt_type = $('#optionSelectArea').attr('data-opt-type').split(',');
			var idx = $('#optionSelectArea').children('.selectBox').length;

			var html = '<div class="selectBox">';
			html += '<select class="option" name="opt">';
			
			if(!opt_type[idx] || opt_type[idx] == null || opt_type[idx] == undefined ) {
				opt_type[idx] = '';
			}
			html += '<option value="">' + opt_type[idx] + ' 선택</option>';
			for(var i = 0, m = data.length; i < m; i++){
				html += '<option value="' + data[i].category + '" data-price="' + data[i].price + '" data-id="' + data[i].category + '" data-has-child="' + data[i].has_child + '">' + data[i].title + '</option>';
			}
			html += '</select>';
			html += '</div>';
			return html;
		},

		OptionChange : function(){
			var sbox = $(this).closest('.selectBox');
			while(sbox.next('.selectBox').length){
				sbox.next('.selectBox').remove();
			}
			if($(this).val() !== ''){
				if($(this).children('option:selected').attr('data-has-child') == 'y')
					App.Goods.Option.GetOption($(this).val());
				else App.Goods.Option.AddOption();
			}
		},

		AddOption: function(){
			var select = $('#optionSelectArea select.option');
			var hasChild = select.last().attr('data-has-child') === 'y';
			if(select.last().val() == '' || hasChild){
				CMAlert('상품 옵션을 선택하여 주세요.');
				return;
			}

			var opt = select.last().children('option:selected');

			var price = opt.attr('data-price');
			var id = opt.attr('data-id');
			if($('#opt' + id).length){
				CMAlert('이미 추가한 상품입니다.');
				return;
			}

			var minEA = parseInt($('#goodsOption').attr('data-min-ea'));
			var maxEA = parseInt($('#goodsOption').attr('data-max-ea'));

			var opt = [];
			for(var i = 0, m = select.length; i < m; i++){
				opt.push(JCM.html2txt(select.eq(i).children('option:selected').text()));
			}

			var html = '<div class="selectedOption" id="opt' + id + '" data-price="' + price + '" data-option-name="' + opt.join('|') + '" data-key="' + id + '">';
			html += '<b>';
			for(var i = 0, m = opt.length; i < m; i++){
				if(i) html += '<span class="gt">&gt;</span> ';
				html += opt[i];
			}
			html += '</b>';
			html += '<span class="ea">' +
				'<input type="text" class="numberonly" value="' + (minEA ? minEA : 1) + '" data-min-ea="' + minEA + '" data-max-ea="' + maxEA + '" data-price="' + price + '">' +
				'<button type="button" class="ea-up">수량 올림</button>' +
				'<button type="button" class="ea-down">수량 내림</button>' +
				' 개</span>';
			html += '<span class="price">' + JCM.setComma(price) + '원</span>';
			html += '<span class="optBtns"><button type="button" class="removeBtn">삭제</span>';
			html += '</div>';
			$('#selectedOptionArea').append(html);
		},

	};



	this.GoodsCalcInit = function(){
		var n = 0;
		$('.ea input').each(function(){
			n += parseInt($(this).attr('data-price')) * parseInt($(this).val());
		});
		$('#totalPrice span').html(JCM.setComma(n));
		setTimeout(function(){
			App.Goods.GoodsCalcInit();
		}, 500);
	};
};

// =================================================================
//
//      카테고리
//

App.GoodsCategory = new function(){
	var _this = this;
	this.url = '';

	this.Init = function(url){
		$(document).on('change', '.categorySelect select', function(){
			_this.removeNext(this);
			_this.getSubCategory(this);
		});
		if(typeof url !== 'undefined') this.url = url;
	};

	this.SearchInit = function(){
		$(document).on('click', '.detailSearchBtn', function(){
			var extend = $(this).closest('form').find('.extend');
			var slide = $(this).closest('form').find('input[name=slide]');
			if(extend.is(':visible')){
				extend.stop().slideUp();
				slide.val('up');
			}
			else{
				extend.stop().slideDown();
				slide.val('down');
			}
		});

		$(document).on('click', '.resetSearchBtn', function(e){
			e.preventDefault();
			App.ResetForm.call(this);
			/*var form = $(this).closest('form');
			form.find('select, input').each(function(){
				if(this.tagName === 'SELECT') this.value = '';
				if(this.tagName === 'INPUT'){
					if(this.type === 'text') this.value = '';
					if(this.type === 'radio' || this.type === 'checkbox'){
						this.checked = (this.value === '');
					}
				}
			});

			if(typeof(form.data('reset')) !== 'undefined') form.data('reset')();*/

		});
	};

	this.removeNext = function(obj){
		if($(obj)[0].tagName === 'SELECT') obj = $(obj).parent();
		if($(obj).next().next().length) this.removeNext($(obj).next());
		if($(obj).next().length && $(obj).next().hasClass('selectBox')) $(obj).next().remove();
	};

	this.getSubCategory = function(obj, nowCategory){
		if($(obj)[0].tagName === 'SELECT') obj = $(obj).parent();
		var select = $(obj).find('select');
		if(select.val() === '') return;

		if(typeof nowCategory == 'undefined') nowCategory = '';

		this.ajaxCategory(this.url + '/' + select.val(), function(html){
			$(obj).closest('.categorySelect').find('.selectBox').last().after(html);
			if($(obj).next().children('select').val() !== '') _this.getSubCategory($(obj).next(), nowCategory);
		}, nowCategory);

	};

	this.ajaxCategory = function(url, callback, nowCategory){
		if(typeof nowCategory == 'undefined') nowCategory = '';

		JCM.get(url, {}, function(data){
			if(data){
				var html = '<span class="selectBox"><select name="selectcategory[]">' +
					'<option value="">선택</option>';

				$.each(data, function(category, title){
						var selected = (nowCategory != '' && category == nowCategory.substr(0, category.length)) ? ' selected' : '';
						html += '<option' + selected + ' value="' + category + '">' + title + '</option>';

				});
				/*for(var i=0; i < data.length; i++){
					var selected = (nowCategory != '' && data[i].category == nowCategory.substr(0, data[i].category.length)) ? ' selected' : '';
					html += '<option' + selected + ' value="' + data[i].category + '">' + data[i].title + '</option>';
				}*/
				html += '</select></span>';
				if(typeof callback === 'function') callback(html);
			}
		});
	};

	this.getRoot = function(obj, nowCategory){
		_this.ajaxCategory(this.url + '/', function(html){
			$(obj).prepend(html);
			if(typeof nowCategory !== 'undefined' && nowCategory.length) _this.getSubCategory($(obj).children('.selectBox').eq(0), nowCategory);
		}, nowCategory);
	}
};
