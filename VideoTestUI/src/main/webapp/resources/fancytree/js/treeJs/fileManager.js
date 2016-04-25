var fileManager = {
	renameInput : { },
	mode : { },
	uploadedId : {},
	uploadedName : [],
	duplicatedFile : [],
	//uploadFileName : { },
	
	// 웹 상 폴더 생성 표시
	folderCreate : function() {
		// alert("selected id : " +
		// JSON.stringify(treeObject.treeData.selectedId));
		var t = $("#treeItem").fancytree("getActiveNode");
		var tempData = { title : "new folder", folder : true };
		t.editCreateNode("child", tempData);
	},
	
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
					var selectedParent = $("#treeItem").fancytree('getTree').getNodeByKey(selectedId).parent;
					
					if ( selectedParent != null) { 
						$("td._up").data('id', selectedParent.key);
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
						})
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
	
											if (i == 0) {
										//		$("._item").remove();
												$(".type_thumb").append($li);
											} else
												$(".type_thumb").append($li);
										},
									});
								})
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
	updataParentFolder : function() {
		var selectedNode = $("#treeItem").fancytree("getActiveNode");
		var parentNode = selectedNode.getParent();
		/*console.log("parent node : " + parentNode.key);*/
		fileManager.itemsAppend(selectedNode.key);
	/*	if ( parentNode.key != "root_1") {
			$.ajax({
				url : "fileList",
				type : "GET",
				data : {
					id : parentNode.key
				},
				dataType : "json",
				success : function(data) {
					console.log(JSON.stringify(data));
					if ( data.length != 0) {
						$.each(data, function(i, item) {
							var itemTitle = data[i].title;
							
							$.ajax({
								url : "fileItem",
								type : "GET",
								dataType : "html",
								success : function(htmlData) {
									var $li = $('' + htmlData + '');
									$li.find("._link_list_item_filename").text(itemTitle);
									$li.find(".rate").text(i);
	
									if (i == 0) {
										$("._item").remove();
										$(".type_thumb").append($li);
									} else
										$(".type_thumb").append($li);
								}
							})
						})
					}
					else {
						$("._item").remove();
					}
	
				},
	
				error : function(x, h, r) {
					alert("update parent folder error " + x.status);
				}
	
			})
		}
		else {
			$.ajax({
				url : "fileList",
				type : "GET",
				data : {
					id : selectedNode.key
				},
				dataType : "json",
				success : function(data) {
					console.log(JSON.stringify(data));
					if ( data.length != 0) {
						$.each(data, function(i, item) {
							var itemTitle = data[i].title;
							
							$.ajax({
								url : "fileItem",
								type : "GET",
								dataType : "html",
								success : function(htmlData) {
									var $li = $('' + htmlData + '');
									$li.find("._link_list_item_filename").text(itemTitle);
									$li.find(".rate").text(i);
	
									if (i == 0) {
										$("._item").remove();
										$(".type_thumb").append($li);
									} else
										$(".type_thumb").append($li);
								}
							})
						})
					}
					else {
						$("._item").remove();
					}
	
				},
	
				error : function(x, h, r) {
					alert("update parent folder error " + x.status);
				}
	
			})
		}*/
	},

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
			showAbort: true,
			allowDuplicates: false,
//			showStatusAfterSuccess: false,
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
				if ( files.length != 0) {
					for ( var i = 0; i < files.length; i++) {
						fileManager.uploadedName.push(files[i].name);
					}
				}
			},
			onSuccess:function(files,data,xhr,pd)
			{
			    //files: list of files
			    //data: response from server
			    //xhr : jquer xhr object
				
				fileManager.itemsAppend(treeObject.treeData.selectedId);
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
			
				fileManager.uploadedName.shift(); // first element removed
				fileManager.uploadedName = fileManager.uploadedName.slice(1); // first element removed
				fileManager.uploadedName.splice(0,1); // first element removed
				fileManager.uploadedName.pop(); // last element removed
			/*	for ( var i = 0; i < obj.existingFileNames.length; i++) {
					var completeName = files;
					
				    fileManager.uploadedName = $.grep(fileManager.uploadedName, function(value) {
				    	return value != completeName;
				    });
				}*/
			},
		});
		
		$("#extrabutton").off('click').on('click', function() {
			if ( fileManager.uploadedName.length != 0) {
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
							
							// delete all array
//							fileManager.uploadedName.shift(); // first element removed
//							fileManager.uploadedName = fileManager.uploadedName.slice(1); // first element removed
//							fileManager.uploadedName.splice(0,1); // first element removed
//							fileManager.uploadedName.pop(); // last element removed
							
						}
						else {
							alert(duplicatedFile[0] + ' already exist.');
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
	
	// 이름 바꾸기
	rename : function() {
		$.ajax({
			url : "rename",
			type : "GET",
			data : {
				id : treeObject.treeData.selectedId,
				title : fileManager.renameInput
			},
			success : function() {
				console.log("rename get selected id " + treeObject.treeData.selectedId);
							
		//		fileManager.updataParentFolder();
			
			},
			error : function(x) {
				console.log(x.status + "rename error");
			}
		})	
	},

	// 이름 바꿀 때 유효성 검사
	renameValid : function(title) {
		var node = $('#treeItem').fancytree("getActiveNode");
		var nextNode = node.getNextSibling();
		var prevNode = node.getPrevSibling();
		

		while (true) {

			if (nextNode != null) {
				/*console.log("nextNode not null : " + nextNode.title);*/
				if (title == nextNode.title) {
					// 동일 이름 존재
					/*console.log("next node alreay exist");*/
//					alert("동일 이름 존재");
					return false;
				}
				else {
					/*console.log("title : " + title + ", next node : " + nextNode.title);*/
		//			alert("title : " + title + ", next node : " + nextNode.title);
					nextNode = nextNode.getNextSibling();					
				}
			}

			if (prevNode != null) {
				/*console.log("prevNode not null");*/
				if (title == prevNode.title) {
					// 동일 이름 존재
					/*console.log("prev node alreay exist");*/
					return false;
				}
				else {
				/*	console.log("title : " + title + ", prev node : " + prevNode.title);*/
	//				alert("title : " + title + ", prev node : " + prevNode.title);
					prevNode = prevNode.getPrevSibling();					
				}				
			}
			

			if (nextNode == null && prevNode == null) {
//				alert("both null");
				return true;
			}
			
		}
	},
	
	// 폴더 생성시 유효성 검사
	createFolderValid : function(title) {
		var selectedNode = $('#treeItem').fancytree("getActiveNode");
		var selectedNodeFirstChild = selectedNode.getFirstChild();
		
		// created node
		var selectedNodeLastChild = selectedNode.getLastChild();
		/*console.log("last child : " + selectedNodeLastChild);*/
		
		while(true) {
			if ( selectedNodeFirstChild != null ) {
				
				// 폴더 하나밖에 없으므로 바로 true
				if ( selectedNodeLastChild.getIndex() == 0 )
					return true;
				
				if ( title == selectedNodeFirstChild.title) {
					return false;
				}
				else {
					/*console.log("++");*/
					 
					selectedNodeFirstChild = selectedNodeFirstChild.getNextSibling();
					if ( selectedNodeLastChild == selectedNodeFirstChild)
						return true;
				}
			}
			
			else
				return true;
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
			alert(v + ' is duplicated');
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
		
		// 파일 하나
		if ( checkedItem.length == 1 && fileId.length == 1) {		
			location.href = "downloadOnlyOneFile" + "/" + fileId[0];
		}
		
		// 폴더 하나
		else if ( checkedItem.length == 1 && folderId.length == 1) {
			location.href = "downloadOnlyOneFolder" + "/" + folderId[0];
		}
		
		// 폴더, 파일 여러개
		else {
			
		}
	
	}


}