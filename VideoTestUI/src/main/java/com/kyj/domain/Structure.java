package com.kyj.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@Table(name = "structure1")
public class Structure {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long key;
	
	private String title;
	
	private long pid;
	
	@Transient
	private List<Structure> children = new ArrayList<>();
	
	@Transient
	private boolean folder;
	
	@JsonIgnore
	@OneToMany(mappedBy = "structure", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<FileInfo> fileInfo;

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

	public List<Structure> getChildren() {
		return children;
	}

	public void setChildren(List<Structure> children) {
		this.children = children;
	}

	public boolean getFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	public List<FileInfo> getFileInfo() {
		return fileInfo;
	}

	public void setFileInfo(List<FileInfo> fileInfo) {
		this.fileInfo = fileInfo;
	}
	
}
