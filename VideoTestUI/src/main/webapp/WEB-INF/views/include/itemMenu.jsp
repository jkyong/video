<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<script
	src="${pageContext.request.contextPath}/resources/fancytree/js/treeJs/itemMenu.js"></script>
<script
	src="${pageContext.request.contextPath}/resources/fancytree/js/treeJs/fileManager.js"></script>
<%-- <script
	src="${pagecontext.request.contextPath }/resources/upload/js/jquery.uploadfile.min.js"></script>
<link
	href="${pagecontext.request.contextPath }/resources/upload/css/uploadfile.css"
	rel="stylesheet">  --%>
<script
	src="${pageContext.request.contextPath }/resources/fancytree/js/treeJs/selectFile.js"></script>
	
<script
	src="${pageContext.request.contextPath }/resources/fancytree/js/treeJs/external.js"></script>
	
<script src="${pageContext.request.contextPath }/resources/bootstrap/js/modal.js"></script>
<link
	href="${pagecontext.request.contextPath }/resources/bootstrap/css/bootstrap-theme.min.css"
	rel="stylesheet">
<jsp:include page="../modal/itemMenuModal.jsp"></jsp:include>
<script>
	$(document).ready(function() {
		$('#moveTree').fancytree({
			source : {
				url : "db"
			}
		});
		
		$('input').on('keydown', function(e) {
			var ctrl = 17;
			var v = 86;
			
			if ( e.keyCode == 17 ) {
				
			}
		});
		
		$('.new-folder').click(function() {
			if ( treeObject.treeData.selectedId === null) {
				
			}
			else {
				$('.newFolderInput').val('');	
				$('#whiteSpaceFolder').hide();
				$('#alreadyNameFolder').hide();
				$('#newFolderModal').modal({backdrop: "static"});
			}
		});
		
		$('.newFolderInput').bind('keypress', function(event) {
			var regex = new RegExp("^[a-zA-Z0-9_]+$");
		    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
		    if (!regex.test(key)) {
		       event.preventDefault();
		       return false;
		    }
		});
		
		$('.createNewFolder').click(function() {
			var newFolderName = $('.newFolderInput').val();
			
			var createValid = fileManager.createFolderValid(newFolderName);
			
				if ( createValid == true && newFolderName != '') {
					var t = $("#treeItem").fancytree("getActiveNode");
					var tempData = { title : newFolderName, folder : true };
					
					fileManager.renameInput = newFolderName;
					
					t.addChildren(tempData);
					fileManager.createSave();
					fileManager.itemsAppend(treeObject.treeData.selectedId);
					
					$('#newFolderModal').modal('hide');
				}
				else if ( newFolderName == '' && createValid == true) {
					$('#alreadyNameFolder').slideUp('fast');
					$('#whiteSpaceFolder').slideDown('fast');
				}
				else {
					$('#whiteSpaceFolder').slideUp('fast');
					$('#alreadyNameFolder').slideDown('fast');
				}
		});
		
		$(document).keydown(function(e) {
			var f2key = 113;
			if ( e.which == f2key) {
				var checkedItem = $('input[name=chkbox]:checked').map(function() {
					return $(this).data('id');
				}).get();	
				
				if ( checkedItem.length == 1) {
					$('.renameInput').val('');
					$('#alreadyNameRename').hide();
					$('#whiteSpaceRename').hide();
					$('#renameModal').modal({backdrop : "static"});
				}
			}
		});
		
		$('.renameInput').bind('keypress', function(event) {
			var regex = new RegExp("^[a-zA-Z0-9_]+$");
		    var key = String.fromCharCode(!event.charCode ? event.which : event.charCode);
		    if (!regex.test(key)) {
		       event.preventDefault();
		       return false;
		    }
		});
		
		$('.nameChange').bind('click', function() {
			
			var checkedItem = $('input[name=chkbox]:checked').map(function() {
				return $(this).data('id');
			}).get();
			
			var renameInput = $('.renameInput').val();
			
			if ( checkedItem.length == 1) {
				var result = fileManager.renameValid(checkedItem, renameInput);
				console.log('result : ' + result);
				console.log('rename input : ' + renameInput);
				
				if ( renameInput === '') {
					$('#alreadyNameRename').slideUp('fast');
					$('#whiteSpaceRename').slideDown('fast');
				}
				else if ( result == renameInput) {
					$('#renameModal').modal('hide');					
				}
				else if ( result != renameInput){
					$('#whiteSpaceRename').slideUp('fast');
					$('#alreadyNameRename').slideDown('fast');
				}
				
			}
		});

		$('.download').click(function(e) {
			fileManager.download();
		});

		$('.upload').click(function() {
			
			if ( treeObject.treeData.selectedId != null) {
				fileManager.uploader();
				fileManager.uploadedId = treeObject.treeData.selectedId;
				$('#uploadModal').modal({backdrop: "static"});
				$('.ajax-file-upload-statusbar').hide();
			}
		});
		
		$('.move').click(function() {
			var checkedItem = $('input[name=chkbox]:checked').map(function() {
				return $(this).data('id');
			}).get();
			
			if ( checkedItem != '') {
				$('#moveModal').modal({backdrop: "static"});
				
				$('#moveTree').fancytree( $('#treeItem').fancytree('getTree') );
				
				var setHeight = $('#moveTree').find('ul.ui-fancytree');
				setHeight.css('height', '300px');
			}
			else {
				alert('file not selected');
			}
		});
		
		$('.moveModalMove').click(function() {
			
			var moveId = $('#moveTree').fancytree("getActiveNode");
			
			if ( moveId != null) {
			
				var checkedItemId = $('input[name=chkbox]:checked').map(function() {
					return $(this).data('id');
				}).get();
				
				var checkedItemName = $('input[name=chkbox]:checked').map(function() {
					return $(this).closest('li').find('a._link_list_item_filename').text()
				}).get();
				
		//		alert('check id : ' + checkedItemId + ', name : ' + checkedItemName);
				$('#moveModal').modal('hide');
				fileManager.move(checkedItemId, checkedItemName, moveId.key);
			}
			else {
 				alert('please select a folder');
				
			}
			
		});
		
		$('.delete').click(function() {
			var checkedItem = $('input[name=chkbox]:checked').map(function() {
				return $(this).data('id');
			}).get();
			
			if ( checkedItem.length > 0) {
				$('#removeModal').modal({backdrop : "static"});
			}
		});
		
		$('.removeRemove').click(function() {			
			fileManager.remove();
			$('#removeModal').modal('hide');
		});
		
		$('.testtable').click(function() {
			var checkedItem = $('input[name=chkbox]:checked').map(function() {
				return $(this).data('id');
			}).get();
			
			if ( checkedItem.length == 1) {
				var split = checkedItem[0].split('_');
				
				if ( split[0] == "fileId") {
					$.ajax({
						url : "validExtension",
						data : {
							id : split[1]
						},
						success : function(bool) {
							if ( bool == true) {
								
								if ( $('.externalCreate').hasClass('disabled')) {
									$('.externalCreate').removeClass('disabled');
									$('.externalCreate').prop('disabled', false);
								}
								
								$('.defaultResults').hide();
								$('.securityResult').hide();
								
								$('.dropdownBtn').html('private' + ' <span class="caret"></span>');
								$('.memo').val(''); 
								
								$('#externalModal').modal({backdrop : "static"});
								
								external.access = $.trim($('.dropdownBtn').text());
							}
						},
						error : function(x) {
							alert('fail' + x.status);
						}
					});
				}
			}
		});
		
		$('.externalCreate').click(function() {
			if ( external.access === 'private' || external.access === 'public') {
				var checkedItem = $('input[name=chkbox]:checked').map(function() {
					return $(this).data('id');
				}).get();
				
				var split = checkedItem[0].split('_');
				var fileId = split[1];
			
				$('.externalCreate').addClass('disabled');
				$('.externalCreate').prop('disabled', true);
				
				var access = external.access;
				var startDate = external.getStartDate();
				var endDate = new Date(startDate.getTime());
	
				endDate.setDate(startDate.getDate() + 1);
				endDate.setMinutes(0);
				
				var memo = $('.memo').val();
				
				$('#startDate').text(external.getDateString(startDate));
				$('#endDate').text(external.getDateString(endDate));
				
				external.save(access, startDate, endDate, memo, fileId);
			}
		});
		
		$('.dropdown-menu li a').click(function() {
			$(".dropdownBtn:first-child").html($(this).text() + ' <span class="caret"></span>');
			external.access = $(this).text();
		});
		
	});
</script>


<form class="navbar-form navbar-right" role="search">
	<div class="form-group">
		<input type="text" class="form-control" placeholder="Search">
	</div>
	<button type="submit" class="btn btn-default">
		<span class="glyphicon glyphicon-search"></span> Search
	</button>
</form>

<div class="navbar-form btn-group btn-group-md pull-left">
	<div class="dropdown">
		<button type="button" class="btn btn-primary new-folder">
			<span class="glyphicon glyphicon-folder-close"></span> new folder
		</button>
		
		<button type="button" class="btn btn-primary download">
			<span class="glyphicon glyphicon-download"> </span> download
		</button>
		
		<button type="button" class="btn btn-primary btn-md upload">
			<span class="glyphicon glyphicon-upload"> </span> upload
		</button>

		<button type="button" class="btn btn-primary move" >
			<span class="glyphicon glyphicon-share-alt"></span> move
		</button>

		<button type="button" class="btn btn-primary delete">
			<span class="glyphicon glyphicon-remove"></span> remove
		</button>
		
		<button type="button" class="btn btn-primary testtable">
			<span class="glyphicon glyphicon-play-circle"></span> table
		</button>
	</div>
</div>
