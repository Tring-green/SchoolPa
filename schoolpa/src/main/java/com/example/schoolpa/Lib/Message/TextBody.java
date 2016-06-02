package com.example.schoolpa.lib.Message;

public class TextBody implements MessageBody {
	private String content;

	public TextBody(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}
}