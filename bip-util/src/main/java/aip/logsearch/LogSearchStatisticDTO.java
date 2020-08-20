package aip.logsearch;

import aip.orm.AIPBaseEntity;

public class LogSearchStatisticDTO extends AIPBaseEntity {

	private java.lang.String word;
	private java.lang.Integer wcount;
	private java.lang.Integer id;
	
	public java.lang.String getWord() {
		return word;
	}
	public void setWord(java.lang.String word) {
		this.word = word;
	}
	public java.lang.Integer getWcount() {
		return wcount;
	}
	public void setWcount(java.lang.Integer wcount) {
		this.wcount = wcount;
	}
	public java.lang.Integer getId() {
		return id;
	}
	public void setId(java.lang.Integer id) {
		this.id = id;
	}
	

	
}
