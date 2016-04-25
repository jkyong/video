var treeObject = {
	treeData : {
		selectedId : {},

		title : {},

		pid : {},	 
	},
	Init : function() {
		$('#treeItem').fancytree({
		
			source : {
				url : "db"
			},
			extensions: ["edit"],
		
			edit: {
				triggerCancel: ["esc"],
				triggerStart: ["f2", "shift+click", "mac+enter"],				
				allowEmpty: false,
				beforeEdit: function(event, data){
					// Return false to prevent edit mode
					console.log("beforeEdit");	// 1
				},
				edit: function(event, data){
					// Editor was opened (available as data.input)
					console.log("edit");	// 3
				},
				beforeClose: function(event, data){
					// Return false to prevent cancel/save (data.input is available)
					console.log("beforeClose");	// 2
//					fileManager.renameValid(data.input.val());
					
				},
				save: function(event, data){
				//	alert("fileManager.createMode : " + fileManager.createMode);
					var renameInput = data.input.val();
					var validTitle = fileManager.renameValid(renameInput);
					
					var createValid = fileManager.createFolderValid(renameInput);
					
					console.log("save");
					
				/*	console.log("rename input : " + renameInput);
					console.log("rename valid title : " + validTitle);
					console.log("create valid : " + createValid);*/
										
					if ( fileManager.mode == "create") {
						console.log("fileManager.createMode true");
						
						if ( createValid == true) {
							
							console.log("   create vaild title true");
							fileManager.renameInput = renameInput;
							
							console.log("   rename input renameinput");
//							fileManager.newItemAppend();
							console.log("   new item append");
							
							fileManager.createSave();
							fileManager.itemsAppend(treeObject.treeData.selectedId);
							/*alert("create save end");*/
							fileManager.mode = "idle";
							console.log("create mode createValid file manager mode : " + fileManager.mode);
							return true;
						}
						else {
//							fileManager.mode = "create";
							console.log("   create valid title false");
							return false;
						}
					}
//					else if ( fileManager.mode == "rename"){
					else {
						console.log("fileManager.createMode false");
					
						if (validTitle == true) {
							fileManager.renameInput = renameInput;
						
							fileManager.rename();
							
						/*	alert("rename true return");*/
//							fileManager.mode = "idle";
							return true;
						}
						else {
							return false;
						}
					}
//					else {
						// jquery.fancytree.edit.js nodeKeydown function custom
						//   --> fileManager.mode ="rename";
//					}
					return;
					// Save data.input.val() or return false to keep editor open
					console.log("save...", this, data);
					
					// Simulate to start a slow ajax request...
					
				/*	setTimeout(function(){
						$(data.node.span).removeClass("pending");
						// Let's pretend the server returned a slightly modified
						// title:
						data.node.setTitle(data.node.title + "!");
					}, 20000);*/
					
					// We return true, so ext-edit will set the current user input
					// as title
					
				},
				close: function(event, data){
					// Editor was removed
					console.log("close");
					console.log(data.save);
					if( data.save ) {
						// Since we started an async request, mark the node as preliminary
						$(data.node.span).addClass("pending");
						
					}
				}
			},
			select : function(event, data) {
				var v = data.tree.getSelectedNodes().join(", ");
					alert("v : " + v);
			},
			activate : function(e, data) {
				fileManager.itemsAppend(data.node.key);
				
				treeObject.treeData.selectedId = data.node.key;
				
				var t = $('#treeItem').fancytree("getActiveNode");
				/*			console.log("selected prev node : " + t.getPrevSibling());
				console.log("selected get index : " + t.getIndex());
				console.log("selected get first child : " + t.getFirstChild());
				console.log("selected node id : " + treeObject.treeData.selectedId);
				console.log("selected node parent : " + t.getParent().key);
				console.log("selected node children : " + t.getChildren());*/
			},
			dblclick : function(e, data) {
				
			}
		});
	}
}