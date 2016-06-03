selectFile = {
	checkedFile : function() {
		$('input[name=chkbox]').change(function() {
			//$(this).is(':checked') ? $(this).prop('checked', false) : $(this).prop('checked', true);
			$(this).closest('li._item').toggleClass('focus');
		});
		
		$('td._item_thumb_box').on('click', function() {
			var $chkbox = $(this).closest('li._item').find('.input_check');			
			$chkbox.is(':checked') ? $chkbox.prop('checked', false) : $chkbox.prop('checked', true);
			$(this).closest('li._item').toggleClass('focus');
		});
		
		$('td._item_thumb_box').on('dblclick', function() {
						
			var $chkbox = $(this).closest('li._item').find('.input_check');
			var selectedDataId = $chkbox.data('id');

			var split = selectedDataId.split("_");
			
			if ( split[0] == "folderId") {
				var key = split[1];
				
				if ( isNaN(split[1]) != true) {
					if ($('#treeItem').fancytree('getTree').getNodeByKey(key) != null)
						$('#treeItem').fancytree('getTree').getNodeByKey(key).setActive();
					else {
						alert( ' nu l  l');
					}
				}
				else {
					alert('NaN');
				}
			}
			else if ( split[0] == "fileId") {
				//파일이면 실행
				var key = split[1];
				
				$.ajax({
					url : "play/internal/view",
					data : {
						id : key
					},
					dataType : "html",
					success : function(data) {
						$('.viewSpace').html(data);
						$('#viewModal').modal({backdrop : "static", keyboard: false});
					},
					error : function() {
						alert();
					},
				});
			}
		});
		
		$('td._up').off('dblclick').on('dblclick', function() {
			var dataId = $(this).closest('li').find('._up').data('id');
			var activeNodeParent = $('#treeItem').fancytree("getActiveNode").parent;

			if ( activeNodeParent != null) {
				if ( dataId == activeNodeParent.key) {
					// key가 root_1 일수도 있으므로 숫자일 경우만 if
					if ( isNaN(activeNodeParent.key) != true)
						$('#treeItem').fancytree('getTree').getNodeByKey(activeNodeParent.key).setActive();
				}
				
			}
		});		
	}
}

