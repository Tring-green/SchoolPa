package com.example.schoolpa.domain;

public class Invitation {

	private long id;
	private String owner;
	private String userId;
	private String name;
	private String icon;
	private String content;
	private boolean agree;

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAgree() {
		return agree;
	}

	public void setAgree(boolean agree) {
		this.agree = agree;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Invitation [id=" + id + ", owner=" + owner + ", userId="
				+ userId + ", name=" + name + ", icon=" + icon + ", content="
				+ content + ", agree=" + agree + "]";
	}

}
