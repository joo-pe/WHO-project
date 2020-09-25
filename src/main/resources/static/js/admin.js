App.Admin = {
	BoardViewInit : function(){
		App.Board.View.AdminInit = function(){
			App.Board.View.Init();

			$(document).on('click', '#removeBtn', App.Board.View.ClickRemoveBtn);
			$(document).on('click', '#removeForm button[type=reset]', App.Board.View.ResetRemoveForm);
		};

		App.Board.View.ClickRemoveBtn = function(e){
			e.preventDefault();
			$('#removeForm').show();
		};

		App.Board.View.ResetRemoveForm = function(e){
			document.querySelector('#removeForm').reset();
			$('#removeForm').hide();
		};

		App.Board.View.AdminInit();
	},
	BoardListInit : function(){
		App.Board.AdminList = {
			Init : function(){
				$('a.deleteArticle').on('click', App.Board.AdminList.ClickDeleteBtn);

				$('#deleteForm button[type=reset]').on('click', App.Board.AdminList.ResetDeleteForm);

				$('a.removeArticle').on('click', App.Board.AdminList.ClickRemoveBtn);

				$('#removeForm button[type=reset]').on('click', App.Board.AdminList.ResetRemoveForm);

				$('.passwordView').on('click', App.Board.AdminList.ClickPwdBtn);

				$('#secretViewForm button[type=reset]').on('click', App.Board.AdminList.ResetPwdForm);
			},

			ClickDeleteBtn : function(e){
				e.preventDefault();
				$('#delForm').attr('action', this.href);
				$('#deleteForm').show();
			},

			ResetDeleteForm : function(e){
				document.querySelector('#deleteForm').reset();
				$('#deleteForm').hide();
			},

			ClickRemoveBtn : function(e){
				e.preventDefault();
				$('#remForm').attr('action', this.href);
				$('#removeForm').show();
			},

			ResetRemoveForm : function(e){
				document.querySelector('#removeForm').reset();
				$('#removeForm').hide();
			},

			ClickPwdBtn : function(e){
				e.preventDefault();
				$('#viewForm').attr('action', this.href);
				$('#secretViewForm').show();
			},

			ResetPwdForm : function(e){
				document.querySelector('#secretViewForm').reset();
				$('#secretViewForm').hide();
			}
		};

		App.Board.AdminList.Init();
	}
};
