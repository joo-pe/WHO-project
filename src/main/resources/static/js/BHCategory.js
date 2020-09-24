function BHCategory(elem, opt){
	var menuObject = this;
	this.mouseDownPosition = {'x' : 0, 'y' : 0, 'moveIs' : false, 'downIs' : false, 'obj' : null};
	this.element = '';
	this.elementID = '';
	this.menu = {};
	this.slideSpeed = 200;
	this.IDFirst = 'BHMenu';
	this.addMenuIs = false;

	// Option
	this.optionMaxLevel = typeof opt.maxLevel != 'undefined' ? opt.maxLevel : 3;
	this.optionWidth = typeof opt.width != 'undefined' ? opt.width : 300;
	this.optionHeight = typeof opt.height != 'undefined' ? opt.height : 400;
	this.optionAddMenu = opt.addMenu;
	this.optionMoveMenu = opt.moveMenu;
	this.optionDeleteMenu = opt.delMenu;
	this.optionModifyMenu = opt.modMenu;
	this.optionClickMenu = opt.titleClick;
	this.optionfolderLoadMenu = opt.folderLoad;
	this.optionDefaultTitle = typeof opt.defaultTitle != 'undefined' ? opt.defaultTitle : '이름없음';
	this.optionHTML = typeof opt.maxLevel != 'undefined' ? opt.HTML : false;

	this.ActionOption = null;

	// 초기화
	if(typeof elem == 'string'){
		if(elem[0] == '#') this.element = $(elem);
		else this.element = $('#' + elem);
	}else this.element = $(elem);

	this.elementID = this.element.attr('id');

	this.element.addClass('BHCategory');
	this.element.css({
		width : this.optionWidth,
		height : this.optionHeight
	});
	this.element.append('<ul data-level="0"></ul>');

	this.modifyButtonHtml = '<a href="#" class="modify"><span>Modify</span></a>';
	this.deleteButtonHtml = '<a href="#" class="delete"><span>Del</span></a>';
	this.upButtonHtml = '<a href="#" class="up"><span>Up</span></a>';
	this.downButtonHtml = '<a href="#" class="down"><span>Down</span></a>';
	this.addButtonHtml = '<a href="#" class="add"><span>Add</span></a>';


	this.SetMenu = function(el, nm, id, enabled){
		if(typeof enabled == 'undefined') enabled = false;
		if(typeof el == 'string') el = '#' + this.IDFirst + el;
		if(typeof id == 'undefined') id = '';
		var pr = $(el).children('ul');
		var level = parseInt(pr.attr('data-level'));
		if(!pr.length){
			$(el).append('<ul></ul>');
			pr = $(el).children('ul');
			level = parseInt(pr.parent().closest('ul').attr('data-level')) + 1;
			pr.attr('data-level', level);
		}

		var btnHtml = '';
		this.downButtonHtml + this.upButtonHtml + this.deleteButtonHtml;

		if(!level){
			btnHtml = this.addButtonHtml;
		}
		else{
			btnHtml = this.upButtonHtml + this.downButtonHtml + this.modifyButtonHtml;
			btnHtml += this.deleteButtonHtml;
			if(level < this.optionMaxLevel){
				btnHtml += this.addButtonHtml;
			}else{
				btnHtml += '<span class="add">&nbsp;</span>';
			}
		}
		pr.append('<li id="' + this.IDFirst + id + '" data-id="' + id + '"><a href="#" class="folder' + ( enabled ? '' : ' disabled') + '"><span>Show</span></a><a href="#" class="title"><b>' + nm + '</b></a><div class="btns">' +btnHtml+ '</div></li>');
		var last = pr.children('li').last();
		return last;
	};

	this.AddMenu = function(id, enabled){
		if(this.ActionOption == null || this.ActionOption.command != 'Add') return;
		var li = this.SetMenu(this.ActionOption.parent, this.ActionOption.title, id, enabled);
		this.ActionOption.parent.children('a.folder').addClass('folder-open');
		if(li.parent().is(':visible')){
			li.css('display', 'none');
			li.slideDown(this.slideSpeed);
		}
		else li.parent().slideDown(this.slideSpeed);

		this.ActionOption = null;

		return li;
	};

	this.ModifyMenu = function(){
		if(this.ActionOption == null || this.ActionOption.command != 'Mod') return;
		var title = this.ActionOption.element.children('a.title');
		if(this.optionHTML) title.children('b').html(this.ActionOption.title);
		else title.children('b').text(this.ActionOption.title);
		title.prev().remove();
		title.show();
		this.ActionOption = null;
	};

	this.DeleteMenu = function(){
		if(this.ActionOption == null || this.ActionOption.command != 'Del') return;
		this.ActionOption.element.remove();
		this.ActionOption = null;
	};

	this.MoveMenu = function(){
		if(this.ActionOption == null || this.ActionOption.command != 'Mov') return;
		if(this.ActionOption.position == 'before'){
			this.ActionOption.target.before(this.ActionOption.element);
		}
		else if(this.ActionOption.position == 'after'){
			this.ActionOption.target.after(this.ActionOption.element);
		}
		this.ActionOption = null;
	};


	this.toggleFolder = function(obj){
		if(typeof obj == 'undefined') obj = $('#'+this.IDFirst);
		if($(obj).children('a.folder').hasClass('folder-open')){
			$(obj).children('a.folder').removeClass('folder-open');
			$(obj).children('a.folder').removeClass('folder-empty');
			$(obj).children('ul').slideUp(this.slideSpeed);
		}else{
			if($(obj).children('ul').length){
				$(obj).children('a.folder').addClass('folder-open');
				$(obj).children('a.folder').removeClass('folder-empty');
				$(obj).children('ul').slideDown(this.slideSpeed);
			}
			else{
				$(obj).children('a.folder').addClass('folder-empty');
			}
			this.BeforeAddMenu(obj);
		}
	};

	// 메뉴추가
	this.element.on('click', 'a.add', function(e){
		e.preventDefault();
		var li = $(this).closest('li');

		if(!li[0].hasAttribute('data-load') && !li.children('ul').length && typeof menuObject.optionfolderLoadMenu != 'undefined'){
			if(menuObject.ActionOption != null){
				return;
			}
			menuObject.addMenuIs = true;
			menuObject.optionfolderLoadMenu(li);
			li.attr('data-load','y');
		}
		else{
			menuObject.addMenuIs = true;
			menuObject.BeforeAddMenu(li);
		}


	});

	this.BeforeAddMenu = function(li){
		if(!this.addMenuIs) return;
		this.addMenuIs = false;
		if(typeof menuObject.optionAddMenu != 'undefined'){
			menuObject.ActionOption = {
				command : 'Add',
				title : menuObject.optionDefaultTitle,
				parent : li
			};

			menuObject.optionAddMenu(li.attr('data-id'), menuObject.optionDefaultTitle, li.children('ul').children().length);
		}
	};

	// 메뉴 수정
	this.element.on('click', 'a.modify', function(e){
		e.preventDefault();
		e.stopPropagation();

		var title = $(this).closest('li').children('a.title');
		if(!title.prev().hasClass('modifyInput')){
			title.before('<form class="modifyInput"><input type="text" value="' + title.children('b').html().replace(/"/ig, '&quot;') + '"><button type="submit" class="modifyConfirm"><span>Confirm</span></button></form>');
			$(this).closest('li').children('.modifyInput').children('input').focus();
			title.hide();
		}
		else{
			title.prev().remove();
			title.show();
		}
	});

	// 메뉴 수정 완료
	this.element.on('submit', 'form.modifyInput', function(e){
		e.preventDefault();
		var li = $(this).closest('li');


		var title = li.children('.modifyInput').children('input').val();
		if(typeof menuObject.optionModifyMenu != 'undefined'){
			menuObject.ActionOption = {
				command : 'Mod',
				title : title,
				element : li
			};

			menuObject.optionModifyMenu(li.attr('data-id'), title);
		}
	});

	// 메뉴삭제
	this.element.on('click', 'a.delete', function(e){
		e.preventDefault();
		var thisObj = this;
		CMConfirm('하위폴더까지 모두 삭제됩니다.\n정말 삭제하시겠습니까?', function(){
			var li = $(thisObj).closest('li');

			if(typeof menuObject.optionDeleteMenu != 'undefined'){
				menuObject.ActionOption = {
					command : 'Del',
					element : li
				};

				menuObject.optionDeleteMenu(li.attr('data-id'));
			}
		});
	});

	// 폴더 클릭
	this.element.on('click', 'a.folder', function(e){
		e.preventDefault();

		var li = $(this).closest('li');
		if(!li[0].hasAttribute('data-load') && !li.children('ul').length && typeof menuObject.optionfolderLoadMenu != 'undefined'){
			if(menuObject.ActionOption != null) return;
			menuObject.optionfolderLoadMenu(li);
			li.attr('data-load','y');
		}
		else menuObject.toggleFolder(li);

	});

	// 메뉴위로이동
	this.element.on('click', 'a.up', function(e){
		e.preventDefault();

		var li = $(this).closest('li');
		if(li.prev().length){
			if(typeof menuObject.optionMoveMenu != 'undefined'){
				menuObject.ActionOption = {
					command : 'Mov',
					element : li,
					target : li.prev(),
					position : 'before'
				};
				menuObject.optionMoveMenu(li.attr('data-id'), li.index()-1, li.parent().closest('li').attr('data-id'));
			}
		}
	});

	// 메뉴아래로이동
	this.element.on('click', 'a.down', function(e){
		e.preventDefault();
		var li = $(this).closest('li');
		if(li.next().length){
			if(typeof menuObject.optionMoveMenu != 'undefined'){
				menuObject.ActionOption = {
					command : 'Mov',
					element : li,
					target : li.next(),
					position : 'after'
				};
				menuObject.optionMoveMenu(li.attr('data-id'), li.index()+1, li.parent().closest('li').attr('data-id'));
			}
		}
	});

	// 타이틀 클릭이벤트 제거
	$(document).on('click', 'a.title', function(e){
		e.preventDefault();
		e.stopPropagation();
	});

	$(document).on('click', 'form.modifyInput', function(e){
		e.stopPropagation();
	});

	$(document).on('click', 'body', function(e){
		if($('form.modifyInput').length){
			$('form.modifyInput').submit();
		}
	});

	this.clickTimeout = 0;

	// 타이틀 마우스 업
	this.element.on('mouseup', 'a.title', function(e){
		e.preventDefault();
		var clickLi = $(menuObject.mouseDownPosition.obj).closest('li');
		var thisLi = $(this).closest('li');
		var dataid = thisLi.attr('data-id');
		if(menuObject.clickTimeout){
			clearTimeout(menuObject.clickTimeout);
			menuObject.clickTimeout = 0;
			thisLi.children('.btns').children('a.modify').trigger('click');
			return;
		}

		if(menuObject.element.find('form.modifyInput').length) return;

		if(thisLi.is(clickLi) && !menuObject.mouseDownPosition.moveIs && menuObject.mouseDownPosition.downIs && typeof menuObject.optionClickMenu != 'undefined'){
			menuObject.clickTimeout = setTimeout(function(){
				if(thisLi.closest('ul').attr('data-level') != '0'){
					menuObject.optionClickMenu(dataid);
				}
				menuObject.clickTimeout = 0;
			}, 200);
		}
	});

	// 타이틀 마우스 드래그
	this.element.on('mousedown', 'a.title', function(e){
		e.preventDefault();
		e.stopPropagation();
		menuObject.mouseDownPosition.x = e.pageX;
		menuObject.mouseDownPosition.y = e.pageY;
		menuObject.mouseDownPosition.moveIs = false;
		menuObject.mouseDownPosition.downIs = true;
		menuObject.mouseDownPosition.obj = this;
	});

	// 마우스 업
	$(document).on('mouseup', function(e){
		if($('#dragTitle').length) $('#dragTitle').remove();
		if(menuObject.mouseDownPosition.moveIs && menuObject.mouseDownPosition.downIs){
			var clickLi = $(menuObject.mouseDownPosition.obj).closest('li');
			var target = menuObject.element.find('li.moverTop');
			if(target.length){
				target.removeClass('moverTop');
				e.preventDefault();
				if(typeof menuObject.optionMoveMenu != 'undefined'){
					menuObject.ActionOption = {
						command : 'Mov',
						element : clickLi,
						target : target,
						position : 'before'
					};
					menuObject.optionMoveMenu(clickLi.attr('data-id'), clickLi.index() > target.index() ? target.index() : target.index() - 1, clickLi.parent().closest('li').attr('data-id'));
				}

			}
			target = menuObject.element.find('li.moverBottom');
			if(target.length){
				target.removeClass('moverBottom');
				if(typeof menuObject.optionMoveMenu != 'undefined'){
					menuObject.ActionOption = {
						command : 'Mov',
						element : clickLi,
						target : target,
						position : 'after'
					};
					menuObject.optionMoveMenu(clickLi.attr('data-id'),  clickLi.index() < target.index() ? target.index() : target.index() + 1, clickLi.parent().closest('li').attr('data-id'));
				}
			}
		}
		menuObject.mouseDownPosition.obj = null;
		menuObject.mouseDownPosition.moveIs = false;
		menuObject.mouseDownPosition.downIs = false;
	});

	// 마우스 이동
	$(document).on('mousemove', function(e){
		e.preventDefault();
		if(!menuObject.mouseDownPosition.downIs) return;
		if(!menuObject.mouseDownPosition.moveIs && (Math.abs(e.pageX - menuObject.mouseDownPosition.x) > 5 || Math.abs(e.pageY - menuObject.mouseDownPosition.y) > 5)){
			menuObject.mouseDownPosition.moveIs = true;
			$('body').append('<div id="dragTitle">' + $(menuObject.mouseDownPosition.obj).text() + '</div>');
		}

		if(menuObject.mouseDownPosition.moveIs){
			$('#dragTitle').css({
				'top' : e.pageY + 'px',
				'left' : (e.pageX + 5) + 'px'
			});
		}
	});

	// 메뉴 마우스 벗어남
	$(document).on('mouseleave', '#' + this.elementID + ' li', function(e){
		if(!menuObject.mouseDownPosition.moveIs || !menuObject.mouseDownPosition.downIs) return;
		var thisLi = $(this).closest('li');
		var clickLi = $(menuObject.mouseDownPosition.obj).closest('li');
		if(thisLi.parent().is(clickLi.parent())){
			// 초기화시키기
			thisLi.removeClass('moverTop').removeClass('moverBottom');

		}
	});

	// 메뉴 마우스 이동
	$(document).on('mousemove', '#' + this.elementID + ' li', function(e){
		if(!menuObject.mouseDownPosition.moveIs || !menuObject.mouseDownPosition.downIs) return;
		var thisLi = $(this).closest('li');
		var clickLi = $(menuObject.mouseDownPosition.obj).closest('li');
		if(!thisLi.parent().is(clickLi.parent())){
			return;
		}

		if(thisLi.index() != clickLi.index()){
			var middle = thisLi.offset().top + (thisLi.outerHeight() / 2);
			if(e.pageY > middle) thisLi.addClass('moverBottom').removeClass('moverTop');
			else thisLi.addClass('moverTop').removeClass('moverBottom');
		}
	});

	this.SetMenu(this.element, 'ROOT', '', true);

}