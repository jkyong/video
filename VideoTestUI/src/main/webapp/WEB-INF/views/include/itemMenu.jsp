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
	
<script src="${pageContext.request.contextPath }/resources/bootstrap/js/modal.js"></script>
<link
	href="${pagecontext.request.contextPath }/resources/bootstrap/css/bootstrap-theme.min.css"
	rel="stylesheet">
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
					
					/* if ( $('#alreadyNameRename').hasClass('alert-danger')) {
						$('#alreadyNameRename').addClass('alert-warning');
						$('#alreadyNameRename').removeClass('alert-danger');
					}
					else if ( $('#alreadyNameRename').hasClass('alert-warning')) {
						$('#alreadyNameRename').addClass('alert-info');
						$('#alreadyNameRename').removeClass('alert-warning');
					}
					else if ( $('#alreadyNameRename').hasClass('alert-info')) {
						$('#alreadyNameRename').addClass('alert-success');
						$('#alreadyNameRename').removeClass('alert-info');
					}
					else if ( $('#alreadyNameRename').hasClass('alert-success')) {
						$('#alreadyNameRename').addClass('alert-danger');
						$('#alreadyNameRename').removeClass('alert-success');
					} */
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
	</div>
	
	
</div>

<!-- new folder modal  -->
<div class="modal fade" id="newFolderModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">New Folder</h4>
			</div>
			<div class="modal-body">
				<div id="alreadyNameFolder" class="alert alert-danger" style="display: none;">
  					<strong> folder name already exists. </strong>
				</div>
				<div id="whiteSpaceFolder" class="alert alert-danger" style="display: none;">
  					<strong> please input the folder name. </strong>
				</div>
				<form id="newFolderForm">
					<input type="text" class="form-control newFolderInput" placeholder="input the new folder name.">
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-primary createNewFolder">Create</button>
			</div>
		</div>
	</div>
</div>

<!-- rename modal  -->
<div class="modal fade" id="renameModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">Rename</h4>
			</div>
			<div class="modal-body">
				<div id="alreadyNameRename" class="alert alert-danger" style="display: none;">
  					<strong> file name already exists. </strong>
				</div>
				<div id="whiteSpaceRename" class="alert alert-danger" style="display: none;">
  					<strong> please input the file name. </strong>
				</div>
				<form id="renameForm">
					<input type="text" class="form-control renameInput" placeholder="rename the file name.">
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-primary nameChange">Change</button>
			</div>
		</div>
	</div>
</div>

<!-- move modal -->
<div class="modal fade" id="moveModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">Directory Move</h4>
			</div>
			<div class="modal-body">
				<div id="moveTree"></div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
				<button type="button" class="btn btn-primary moveModalMove">Move</button>
			</div>
		</div>
	</div>
</div>

<!-- upload modal -->
<div class="modal fade" id="uploadModal" role="dialog">
	<div class="modal-dialog modal-md">
		<div class="modal-content">			
			<div class="modal-header">
				<h4 class="modal-title" id="exampleModalLabel">Upload</h4>
			</div>
			<div class="modal-body" > 
				<div id="fileuploader">Select</div>
			</div> 
			<div class="modal-footer">
				<button type="button" class="btn btn-default uploadCancel" data-dismiss="modal">Cancel</button>
				<button id="extrabutton" type="button" class="btn btn-success startUpload">Start Upload</button>
			</div>
		</div>
	</div>
</div>

<!-- remove modal  -->
<div class="modal fade" id="removeModal" tabindex="-1" role="dialog"
	aria-labelledby="exampleModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-sm">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="exampleModalLabel">Remove</h4>
			</div>
			<div class="modal-body"> 
  				Are you sure you want to remove?
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-danger removeRemove">Remove</button>
			</div>
		</div>
	</div>
</div>
