var treeObject = {
	treeData : {
		selectedId : null,

		title : null,

		pid : null,
	},
	Init : function() {
		$('#treeItem').fancytree({
			
			source : {
				url : "db",
				complete: function() {
					if($('#treeItem').fancytree('getTree').getNodeByKey("1").setActive()!=null)
						$('#treeItem').fancytree('getTree').getNodeByKey("1").setActive();
				}
			},
			select : function(event, data) {
				var v = data.tree.getSelectedNodes().join(", ");
					alert("v : " + v);
			},
			activate : function(e, data) {
				fileManager.itemsAppend(data.node.key);
				
				treeObject.treeData.selectedId = data.node.key;
				console.log(data.node.key);
			/*	var t = $('#treeItem').fancytree("getActiveNode");
							console.log("selected prev node : " + t.getPrevSibling());
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