var fileManager = {
	renameInput : { },
//	mode : { },
	uploadedId : {},
	uploadedName : [],
	duplicatedFile : [],
	//uploadFileName : { },
	
	// 웹 상 폴더 생성 표시
	/*folderCreate : function() {
		var t = $("#treeItem").fancytree("getActiveNode");
		var tempData = { title : "new folder", folder : true };
		t.editCreateNode("child", tempData);
	},*/
	
	// 생성한 폴더 db 저장
	createSave : function() {
		var parentId = treeObject.treeData.selectedId;
		var lastChild = $('#treeItem').fancytree("getActiveNode").getLastChild();
		
		$.ajax({
			url : "folderCreate",
			async : false,
			data : {
				title : fileManager.renameInput,
				pid : parentId
			},
			dataType : "json",
			success : function(key) {
				console.log("create save js parent id : " + parentId + ", last child : " + lastChild);
				var nextKey = key.id;
				
				lastChild.key = key.id.toString();
				
				console.log("next key : " + nextKey);

			},
			error : function(w) {
				alert("db save error" + w.status);
			}
		})
	},
	
	// 폴더 클릭시 하위 파일 불러오며 file list에 붙이기
	itemsAppend : function(selectedId) {
		
		if ( isNaN(selectedId) != true ) {
			$.ajax({
				url : "fileList",
				type : "GET",
				data : {
					id : selectedId
				},
				dataType : "json",
				success : function(data) {
					var selectedParent = $("#treeItem").fancytree('getTree').getNodeByKey(selectedId);
					if ( selectedParent == null) {
						if ( selectedParent.getParent().title == 'root') {
							console.log('ddff');
						}
					}
					else {
						if ( selectedParent.getParent().title != 'root') {
							$("td._up").data('id', selectedParent.parent.key);
						}
					}
					
					if ( data.length != 0) {
						$.each(data, function(i, item) {
							var itemTitle = data[i].title;
							var itemKey = data[i].key;
							var appendDataId = 'folderId' + '_' + itemKey;
							$.ajax({
								url : "fileItem",
								type : "GET",
								dataType : "html",
								async : false,
								success : function(htmlData) {
									var $li = $('' + htmlData + '');
									
									$li.find("._link_list_item_filename").text(itemTitle);
									$li.find(".input_check").data("id", appendDataId);
									$li.find(".rate").text("");
									
									$li.find("._file_icon").addClass('bu_folder_b');
									
									if (i == 0) {
										$("._item").remove();
										$(".type_thumb").append($li);
									} else
										$(".type_thumb").append($li);
								}
							});
						});
					}
					else {
						$("._item").remove();
					}
	
				},
				
				complete : function() {
					$.ajax({
				    	url: "uploadFileAppend",
				    	data: {
				    		selectedId : treeObject.treeData.selectedId
			    		},
				    	dataType: "json",
				    	success: function(data) {
				    		if ( data.length != 0) {
								$.each(data, function(i, item) {
									var itemTitle = data[i].name;
									var itemExtension = data[i].extension;
									var itemSize = data[i].size;
									
									var itemKey = data[i].id;
									var appendDataId = 'fileId' + '_' + itemKey;
									
									var bytes = ' B';
																	
									// max i = 3, GB 
									for ( var i = 0; i < 3; i++) {
										
										if (i == 0 && itemSize < 0.1){
											itemSize = 0.1 + bytes;
											break;
										}
										else if ( itemSize < 1024 ) {
											itemSize = itemSize.toFixed(2);
											itemSize = itemSize + bytes;
											break;
										}
										else {
											itemSize = itemSize / 1024;
											
											if ( bytes == ' B')
												bytes = ' KB';
											else if ( bytes == ' KB')
												bytes = ' MB';
											else if ( bytes == ' MB')
												bytes = ' GB';
										}
										
										if ( i == 2) {
											itemSize = itemSize.toFixed(2);
											itemSize = itemSize + bytes;
										}
									}
									
									$.ajax({
										url : "fileItem",
										type : "GET",
										dataType : "html",
										async : false,
										success : function(htmlData) {
											var $li = $('' + htmlData + '');
											var upperExtension = itemExtension.toUpperCase();
											
											$li.find("._link_list_item_filename").text(itemTitle);
											$li.find(".input_check").data("id", appendDataId);
											$li.find(".rate").text(itemSize);
											$li.find("._file_icon").addClass('bu_mov_b');
											$li.find("span._file_icon_span").html(upperExtension);
	
					//						if (i == 0) {
										//		$("._item").remove();
												$(".type_thumb").append($li);
								//			} else
								//				$(".type_thumb").append($li);
										},
									});
								});
							}
							else {
							//	$("._item").remove();
							}
				    	},
				    	error: function() {
				    		
				    	},
				    	complete: function() {
				    		selectFile.checkedFile();										
				    	}
				    	
				    });
				},
	
				error : function(x, h, r) {
					alert("item append error : " + x.status);
				}
	
			});
		}
	},
	
	// 부모 폴더 선택하여 하위 폴더 불러온 상태에서 하위 폴더의 이름 바꿀 시 부모 폴더 갱신
/*	updataParentFolder : function() {
		var selectedNode = $("#treeItem").fancytree("getActiveNode");
		var parentNode = selectedNode.getParent();
		fileManager.itemsAppend(selectedNode.key);
	},*/

	// 선택한 파일 삭제
	remove : function() {
	/*	var node = $("#treeItem").fancytree("getActiveNode");
		node.remove(); 
		
		var test = $("#treeItem").fancytree("getTree").getNodeByKey("4");
		
		test.remove();*/
		var checkedItem = $('input[name=chkbox]:checked').map(function() {
			return $(this).data('id');
		}).get();
		
		if ( checkedItem.length != 0) {
			var folderId = [];
			var fileId = [];
			
			for ( var i =0; i < checkedItem.length; i++) {
				var split = checkedItem[i].split("_");
				
				if ( split[0] == 'folderId') {
					var folderKey = split[1];
					folderId.push(folderKey);
					
					var removeItem = $("#treeItem").fancytree("getTree").getNodeByKey(folderKey);
					
					if ( removeItem != null)
						removeItem.remove();
				
				}
				else if ( split[0] == 'fileId') {
					var fileKey = split[1];
					
					fileId.push(fileKey);
				}
				else {
					alert('etc ids');
				}			
			}
			// db remove
			$.ajax({
				url : "remove",
				async : false,
				data : {
					folderIds : folderId,
					fileIds : fileId
				},
				success : function() {
					fileManager.itemsAppend(treeObject.treeData.selectedId);
				},
				error : function() {
					alert('remove error ');
				}
			})
		}
	},

	uploader : function() {
		var extraObj = $('#fileuploader').uploadFile({
			url : "uploadForm",
			fileName : "file",
			showFileCounter : false,
			multiple : true,
			autoSubmit : false,
			sequential : true,
			sequentialCount : 1,
			showProgress : true,
			showDone: true,
			showCancel: true,
			showDelete: false,
			showAbort: false,
			allowDuplicates: false,
			uploadStr: "Select Files",
			showStatusAfterSuccess: false,
			errorClass: "ajax-file-upload-error alert alert-danger",
			statusBarWidth: 558,
			dragdropWidth: 568,
//			allowedTypes: "mp4,wmv,mkv,avi",
//			formData: { "selectedId": treeObject.treeData.selectedId },
			dynamicFormData: function() {
				var data = { "selectedId": fileManager.uploadedId};
				return data;
			},
			onLoad:function(obj) {
				
			},
			onSelect:function(files) {
				// selected file add array
				
				// files 와 fileManager.uploadedName 비교해서 같은게 있으면 alert 없으면 push
				for ( var i = 0; i < files.length; i++) {
					if ( fileManager.uploadedName == files[i].name) {
						alert(files[i] );
						break;
					}
					else {
						if ( i == files.length - 1) {
							for ( var i = 0; i < files.length; i++) {
								fileManager.uploadedName.push(files[i].name);
							}
						}
					}
					
				}
				
			/*	if ( files.length != 0) {
					for ( var i = 0; i < files.length; i++) {
						fileManager.uploadedName.push(files[i].name);
					}
					console.log(fileManager.uploadedName);
				}*/
			},
			onSuccess:function(files,data,xhr,pd)
			{
			    //files: list of files
			    //data: response from server
			    //xhr : jquer xhr object
				fileManager.uploadedName = $.grep(fileManager.uploadedName, function(value) {
					return value != files;
				});
				console.log('success : ' + fileManager.uploadedName);
//				fileManager.itemsAppend(treeObject.treeData.selectedId);
			},
			onError: function(files,status,errMsg,pd)
			{
			    //files: list of files
			    //status: error status
			    //errMsg: error message
			    alert('upload fail');			 
			},
			onCancel: function(files,pd)
			{
			    //files: list of files
			    //pd:  progress div
				var cancelName = files;
				
			    fileManager.uploadedName = $.grep(fileManager.uploadedName, function(value) {
			    	return value != cancelName;
			    });
			},
			afterUploadAll:function(obj)
			{
				//You can get data of the plugin using obj
//				fileManager.itemsAppend(treeObject.treeData.selectedId);
				console.log(JSON.stringify(obj));
				console.log(obj.existingFileNames[0]);
			
//				fileManager.uploadedName.shift(); // first element removed
//				fileManager.uploadedName = fileManager.uploadedName.slice(1); // first element removed
//				fileManager.uploadedName.splice(0,1); // first element removed
//				fileManager.uploadedName.pop(); // last element removed
				
				console.log('after upload : ' + fileManager.uploadedName);
				$('.uploadCancel').removeClass('disabled');
				$('.startUpload').removeClass('disabled');
				$('.uploadCancel').prop('disabled', false);
				$('.startUpload').prop('disabled', false);
				
				fileManager.itemsAppend(treeObject.treeData.selectedId);
			},
		});
		
		$("#extrabutton").on('click', function() {		
			
			if ( fileManager.uploadedName.length > 0) {
				$('.uploadCancel').addClass('disabled');
				$('.startUpload').addClass('disabled');
				$('.uploadCancel').prop('disabled', true);
				$('.startUpload').prop('disabled', true);
				
				$.ajax({
					url : "uploadFileValid",
					async : false,
					type : "GET",
					data : {
						selectedFiles : fileManager.uploadedName,
						uploadedId : fileManager.uploadedId
					},
					dataType : "json",
					success : function(duplicatedFile) {
						if ( duplicatedFile.length == 0 ) {
							extraObj.startUpload();
						}
						else {
							alert(duplicatedFile[0] + ' already exist.');
							
							$('.uploadCancel').removeClass('disabled');
							$('.startUpload').removeClass('disabled');
							$('.uploadCancel').prop('disabled', false);
							$('.startUpload').prop('disabled', false);
						}
					},
					error : function() {
						alert('error ?');
					},
				});
			}
			else {
				alert('file is empty');
			}
			
		});
	},
	
	// 폴더명 바꾸기 
	renameFolder : function(folderId, renameFolder) {
		$.ajax({
			url : "renameFolder",
			type : "GET",
			async : false,
			data : {
				folderId : folderId,
				title : renameFolder
			},
			success : function() {
				var $tree = $('#treeItem').fancytree('getTree');
				$tree.reload().done(function(){
					$tree.getNodeByKey(treeObject.treeData.selectedId).setActive();
		        });
			},
			error : function(x) {
				console.log(x.status + "rename error");
			}
		})	
	},
	
	// 파일명 바꾸기
	renameFile : function(fileId, renameFile) {
		$.ajax({
			url : "renameFile",
			type : "GET",
			async : false,
			data : {
				fileId : fileId,
				name : renameFile
			},
			success : function() {
			
			},
			error : function(x) {
				console.log(x.status + "rename error");
			}
		})	
	},

	renameValid : function(renameObj, renameInput) {
		var split = renameObj[0].split("_");
		
		var objType = split[0];
		var key = split[1];
		
		console.log(objType);
		console.log(key);
		if ( objType == 'folderId') {
			$.ajax({
				url : "renameFolderValid",
				async : false,
				data : {
					pid : treeObject.treeData.selectedId,
					folderId : key,
					renameInput : renameInput
				},
				success : function(rename) {					
					if ( rename == '') {
						fileManager.renameInput = rename;
					}
					else {
						fileManager.renameInput = rename;
						fileManager.renameFolder(key, fileManager.renameInput);
						fileManager.itemsAppend(treeObject.treeData.selectedId);
						
					}
				},
				error : function(e) {
					alert(e.status);
				}
				
			});
		}
		else if ( objType == 'fileId') {
			$.ajax({
				url : "renameFileValid",
				async : false,
				data : {
					pid : treeObject.treeData.selectedId,
					fileId : key,
					renameInput : renameInput
				},
				success : function(rename) {
					if ( rename == '') {
						fileManager.renameInput = rename;
					}						
					else {
						fileManager.renameInput = rename;
						fileManager.renameFile(key, fileManager.renameInput);
						fileManager.itemsAppend(treeObject.treeData.selectedId);
					}
				},
				error : function() {
					
				}
				
			});
		}
		
		return fileManager.renameInput;
	},
	
	// 폴더 생성시 유효성 검사
	createFolderValid : function(title) {
		var selectedNode = $('#treeItem').fancytree("getActiveNode");
		var selectedNodeFirstChild = selectedNode.getFirstChild();
		
		// created node
		var selectedNodeLastChild = selectedNode.getLastChild();
		/*console.log("first child : " + selectedNodeFirstChild);
		console.log("last child : " + selectedNodeLastChild);*/
		
		while(true) {
			if ( selectedNodeFirstChild != null ) {
				
				// 폴더 하나밖에 없으므로 바로 true
				if ( selectedNodeLastChild.getIndex() == 0 ) {
					return true;
				}
				
				if ( title == selectedNodeFirstChild.title) {
					return false;
				}
				else {
					selectedNodeFirstChild = selectedNodeFirstChild.getNextSibling();
					if ( selectedNodeLastChild == selectedNodeFirstChild && selectedNodeLastChild.title != title) {
						return true;
					}
				}
			}
			
			else {
				return true;
			}
		}
	},
	
	move : function(checkedItemId, checkedItemName, moveId) {
		
		// move folder id
		var folderId = [];
		// folder id folder name
		var folderName = [];
		
		// move file id
		var fileId = [];		
		// file id file name
		var fileName = [];
		
		// duplicate name array
		var dupFolderName = [];
		var dupFileName = [];
		
		for ( var i = 0; i < checkedItemId.length; i++) {
			var split = checkedItemId[i].split("_");
			
			if ( split[0] == "folderId" ) {
				folderId.push(split[1]);
				folderName.push(checkedItemName[i]);
			}
			else if ( split[0] == "fileId") {
				fileId.push(split[1]);
				fileName.push(checkedItemName[i]);
			}
		}
		
		if ( folderName.length != 0) {
			$.ajax({
				url : "moveFolderNameValid",
				async : false,
				data : {
					moveId : moveId
				},
				success : function(folders) {
					if ( folders.length != 0) {
						for ( var i = 0; i < folders.length; i++) {
							for ( var k = 0; k < folderName.length; k++) {
								if ( folderName[k] == folders[i].title ) {
									dupFolderName.push(folderName[k]);
									
									folderName.splice(k, 1);
									folderId.splice(k, 1);
								}
							}
						}
					}
				},
				error : function() {
					
				}
			});
		}
		
		if ( fileName.length != 0) {
			$.ajax({
				url : "moveFileNameValid",
				async : false,
				data : {
					moveId : moveId
				},
				success : function(files) {
					if ( files.length != 0) {
						for ( var i = 0; i < files.length; i++) {
							for ( var k = 0; k < fileName.length; k++) {
								if ( fileName[k] == files[i].name ) {
									dupFileName.push(fileName[k]);
									
									fileName.splice(k, 1);
									fileId.splice(k, 1);
								}
							}
						}
					}
				},
				error : function() {
					
				}
			});
		}
		
		if ( dupFileName.length == 0 && dupFolderName.length == 0) {
			$.ajax({
				url : "move",			
				data : {
					folderId : folderId,
					fileId : fileId,
					moveId : moveId
				},
				success : function() {
					var $tree = $('#treeItem').fancytree('getTree');
					$tree.reload().done(function(){
						$tree.getNodeByKey(treeObject.treeData.selectedId).setActive();
			        });
					
				},
				error : function(a) {
					alert(a.status);
				},
			});
		}
		else {
			var v = '';
			
			if (dupFileName.length != 0) {
				if ( dupFolderName.length != 0) 
					v = dupFileName + ',' + dupFolderName;
				else
					v = dupFileName;
			}
			else if ( dupFolderName.length != 0) {
				if (dupFileName.length != 0) 
					v = dupFileName + ',' + dupFolderName;
				else
					v = dupFolderName;				
			}
		}
	},
	
	download : function() {
		var checkedItem = $('input[name=chkbox]:checked').map(function() {
			return $(this).data('id');
		}).get();
			
		var folderId = [];
		var fileId = [];
	
		if ( checkedItem.length != 0) {
			
			for ( var i =0; i < checkedItem.length; i++) {
				var split = checkedItem[i].split("_");
				
				if ( split[0] == 'folderId') {
					var folderKey = split[1];
					
					folderId.push(folderKey);
				}
				else if ( split[0] == 'fileId') {
					var fileKey = split[1];
					
					fileId.push(fileKey);
				}
				else {
					alert('etc ids');
				}
			}
		}


		if ( checkedItem.length == 0 && fileId.length == 0) {
			
		}
		// 파일 하나
		else if ( checkedItem.length == 1 && fileId.length == 1) {		
			location.href = "downloadOnlyOneFile" + "/" + fileId[0];
		}
		
		// 폴더 하나
		else if ( checkedItem.length == 1 && folderId.length == 1) {
			$.ajax({
				url : "downloadOnlyOneFolder" + "/" + folderId[0],				
				success : function(map) {
					var path = "startDownloadOneFolder?zipPath=" + map.zipPath + "&zipName=" + map.zipName
					location.href = path;
				},
				error : function() {
					alert('e');
				},
				beforeSend : function() {
					console.log('loading');
				}
			});
		}
		
		// 폴더, 파일 여러개
		else {
			var parentFolderTitle = $("#treeItem").fancytree("getActiveNode").title;
			var parentFolderId = $("#treeItem").fancytree("getActiveNode").key;
			
			$.ajax({
				url : "downloadMultiple",
				data : {
					parentFolderId : parentFolderId,
					parentFolderTitle : parentFolderTitle,
					folderId : folderId,
					fileId : fileId
				},
				beforeSend : function() {
					
				},
				success : function(map) {
					var path = "startDownloadMultiple?zipPath=" + map.zipPath + "&zipName=" + map.zipName
					location.href = path;
				},
				error : function(e) {
					alert(e.status);
				}
			});
		}
	
	}


}