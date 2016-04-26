<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
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
			},
			extensions: ["edit"],
		
			edit: {
				triggerCancel: ["esc"],
				triggerStart: ["f2", "shift+click", "mac+enter"],				
				allowEmpty: false,
				beforeEdit: function(event, data){
					console.log("beforeEdit");	// 1
				},
				edit: function(event, data){
					console.log("edit");	// 3
				},
				beforeClose: function(event, data){
					console.log("beforeClose");	// 2
					
				},
				save: function(event, data){
					var renameInput = data.input.val();
					var validTitle = fileManager.renameValid(renameInput);
					
					var createValid = fileManager.createFolderValid(renameInput);
					
					console.log("save");
					
										
					if ( fileManager.mode == "create") {
						console.log("fileManager.createMode true");
						
						if ( createValid == true) {
							
							console.log("   create vaild title true");
							fileManager.renameInput = renameInput;
							
							console.log("   rename input renameinput");
							console.log("   new item append");
							
							fileManager.createSave();
							fileManager.itemsAppend(treeObject.treeData.selectedId);
							fileManager.mode = "idle";
							console.log("create mode createValid file manager mode : " + fileManager.mode);
							return true;
						}
						else {
							console.log("   create valid title false");
							return false;
						}
					}
					else {
						console.log("fileManager.createMode false");
					
						if (validTitle == true) {
							fileManager.renameInput = renameInput;
						
							fileManager.rename();
							
							return true;
						}
						else {
							return false;
						}
					}
					return;
					console.log("save...", this, data);
					
					
				},
				close: function(event, data){
					console.log("close");
					console.log(data.save);
					if( data.save ) {
						$(data.node.span).addClass("pending");
						
					}
				}
			},
			select : function(event, data) {
				var v = data.tree.getSelectedNodes().join(", ");
					alert("v : " + v);
			},
			activate : function(e, data) {
			//	fileManager.itemsAppend(data.node.key);
				
			//	treeObject.treeData.selectedId = data.node.key;
				
			//	var t = $('#treeItem').fancytree("getActiveNode");
			},
			dblclick : function(e, data) {
				
			}
		});
		

		
		$('.new-folder').click(function() {
			fileManager.mode = "create";

			fileManager.folderCreate();
		});

		$('.download').click(function(e) {
			 
			fileManager.download();
		});

		$('.upload').click(function() {
			fileManager.uploader();
			fileManager.uploadedId = treeObject.treeData.selectedId;
			$('#uploadModal').modal({backdrop: true});
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
			fileManager.remove();
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
				<h4 class="modal-title" id="exampleModalLabel">파일 이동</h4>
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
			<div id="extrabutton" class="ajax-file-upload-green"
				style="padding-left: 2%;">Start Upload</div>

			<div id="fileuploader">Upload</div>
		</div>
	</div>
</div>





