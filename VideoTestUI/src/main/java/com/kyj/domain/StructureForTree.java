package com.kyj.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "structure")
public class StructureForTree {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long key;
	
	private String title;
	
	private long pid;
	
	@Transient
	private List<StructureForTree> children = new ArrayList<>();
	
	@Transient
	private boolean folder;
	
	public long getKey() {
		return key;
	}

	public void setKey(long key) {
		this.key = key;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public List<StructureForTree> getChildren() {
		return children;
	}

	public void setChildren(List<StructureForTree> children) {
		this.children = children;
	}

	public boolean getFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}
	
}
