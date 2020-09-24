App.Board = {
	EventInit : false,
	boardListElement : null,

	ListInit : function(){
		if(!this.EventInit){
			this.EventInit = true;

			$(document).on('click', '.passwordView', App.Board.ClickPwdView);

			$(document).on('click', '#secretViewForm button[type=reset]', App.Board.ResetPwdView);
		}
	},

	MoreListEventInit : false,

	MoreListInit : function(){
		if(!this.MoreListEventInit){
			this.MoreListEventInit = true;

			$(document).on('submit', '#bbsSchForm', App.Board.SubmitSearchForm);

			$(document).on('click', '#moreViewBtn', App.Board.ClickMoreViewBtn);

			$(document).on('click', '.passwordView', App.Board.ClickPwdView);

			$(document).on('click', '#secretViewForm button[type=reset]', App.Board.ResetPwdView);
		}

		if(this.boardListElement === null){
			this.boardListElement = $('#bhBoardList table.list tbody');
		}

		App.Board.GetMoreList();

	},

	ClickPwdView : function(e){
		e.preventDefault();
		$('#viewForm').attr('action', this.href);
		$('#secretViewForm').show();
	},

	ResetPwdView : function(e){
		document.querySelector('#viewForm').reset();
		$('#secretViewForm').hide();
	},

	GetMoreList : function(){
		JCM.ajaxForm('#bbsSchForm', function(data){
			var boardList = App.Board.boardListElement;
			if(data.lastIs) $('#bhBoardList .moreViewBtn').hide();
			else $('#bhBoardList .moreViewBtn').show();
			if($.trim(data.list) === ''){
				if($.trim(boardList.text()) === '') boardList.html('<p class="nothing">검색된 글이 없습니다.</p>');
				return;
			}
			boardList.append(data.list);
		});
	},

	SubmitSearchForm : function(e){
		e.preventDefault();
		App.Board.boardListElement.html('');
		$(this).find('input[name=searchKeyword]').val($(this).find('input[name=searchInput]').val());
		$(this).find('input[name=lastSeq]').val('');
		App.Board.GetMoreList();
	},

	ClickMoreViewBtn : function(e){
		e.preventDefault();
		$('#bhBoardList .lastSeq').val(App.Board.boardListElement.children().last().attr('data-seq'));
		App.Board.GetMoreList();
	},

	View : {
		EventInit : false,

		Init : function(){
			if(!this.EventInit){
				this.EventInit = true;

				$(document).on('click', '#deleteArticle', App.Board.View.ClickDeleteBtn);
				$(document).on('click', '#deleteForm button[type=reset]', App.Board.View.ResetDeleteForm);

				$(document).on('click', '#modifyBtn', App.Board.View.ClickModifyBtn);
				$(document).on('click', '#modifyForm button[type=reset]', App.Board.View.ResetModifyForm);
			}
		},

		ClickDeleteBtn : function(e){
			e.preventDefault();
			$('#deleteForm').show();
		},

		ResetDeleteForm : function(e){
			document.querySelector('#delForm').reset();
			$('#deleteForm').hide();
		},

		ClickModifyBtn : function(e){
			if($('#modForm').length){
				e.preventDefault();
				$('#modifyForm').show();
			}
		},

		ResetModifyForm : function(e){
			document.querySelector('#modifyForm').reset();
			$('#modifyForm').hide();
		}
	},

	Write : {
		useSE2Is : false,
		EventInit : false,

		Init : function(useSE2){
			this.useSE2Is = useSE2;
			if(useSE2) SE2_paste('MD_content','');
			if(!this.EventInit){
				this.EventInit = true;

				$(document).on('submit', '#BoardWriteForm', App.Board.Write.SubmitWrite);
			}
		},

		SubmitWrite : function(e){
			if(App.Board.Write.useSE2Is) SE2_update('MD_content');

			var res = $(this).validCheck();
			if(!res){
				e.preventDefault();
				return false;
			}

		}
	}
};
