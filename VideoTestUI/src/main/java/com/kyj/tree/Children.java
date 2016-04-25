package com.kyj.tree;

import java.util.ArrayList;
import java.util.List;

import com.kyj.domain.Structure;

public class Children {

	public List<Structure> getChildrenObj(List<Structure> dataList, long selectedId) {

		List<Structure> childrenList = new ArrayList<>();

		for (int i = 0; i < dataList.size(); i++) {
			long childrenPid = dataList.get(i).getPid();

			// parent id �� pid�� ���� ������ �߰�
			if (childrenPid == selectedId) {
				childrenList.add(dataList.get(i));
			}

			// �˻��� ��������
			if (i == dataList.size() - 1)
				return childrenList;
		}
		return null;
	}

	public List<Structure> getChildrenTitle(List<Structure> dataList, long selectedId) {
		List<Structure> childrenList = new ArrayList<>();
		
		for ( int i =0; i < dataList.size(); i++) {
			long children = dataList.get(i).getPid();
			if ( children == selectedId)
				childrenList.add(dataList.get(i));
			if ( i == dataList.size() - 1)
				return childrenList;
		}
		
		return null;
	}
}
