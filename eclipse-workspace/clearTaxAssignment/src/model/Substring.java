package model;

public class Substring {
	private int startIndex;
	private int endIndex;
	private String partialContent;

	public Substring(int startIndex, int endIndex, StringBuilder content) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.partialContent = content.substring(startIndex, endIndex + 1);
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public String getContent() {
		return partialContent;
	}

	public void setPartialContent(String partialContent) {
		this.partialContent = partialContent;
	}
}
