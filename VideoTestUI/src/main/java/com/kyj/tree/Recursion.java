package com.kyj.tree;

import java.util.List;

import com.kyj.domain.StructureForTree;


public class Recursion {
	
	public void addItem(List<StructureForTree> result, List<StructureForTree> dataList, int index) {
		for (int i = 0; i < result.size(); i++) {
			try {
				long resultId = result.get(i).getKey();

				long dataListId = dataList.get(index).getKey();
				String dataListTitle = dataList.get(index).getTitle();
				long dataListPid = dataList.get(index).getPid();
//				boolean dataListFolder = dataList.get(index).getFolder();

				if (resultId == dataListPid) {
					StructureForTree data = new StructureForTree();
					data.setKey(dataListId);
					data.setTitle(dataListTitle);
					data.setPid(dataListPid);
//					data.setFolder(dataListFolder);
					data.setFolder(true);

					result.get(i).getChildren().add(data);
				} else {
					if (result.get(i).getChildren().size() != 0)
						addItem(result.get(i).getChildren(), dataList, index);
					else {

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public List<StructureForTree> addRootTree(List<StructureForTree> result, List<StructureForTree> dataList) {

		for (int i = 0; i < dataList.size(); i++) {
			long id = dataList.get(i).getKey();			
			String title = dataList.get(i).getTitle();
			long pid = dataList.get(i).getPid();
			
//			boolean folder = dataList.get(i).getFolder();
			
			// root
			if (pid == 0) {
				StructureForTree data = new StructureForTree();
				data.setKey(id);
				data.setTitle(title);
				data.setPid(pid);
//				data.setFolder(folder);
				data.setFolder(true);
				result.add(data);
			} else {
//				for (int j = 0; j < result.size(); j++) {
					addItem(result, dataList, i);
//				}
			}
		}
		return result;
		
	}
}
	
