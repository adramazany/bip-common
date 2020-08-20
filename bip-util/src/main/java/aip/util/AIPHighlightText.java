package aip.util;

import java.util.ArrayList;

public class AIPHighlightText {
	String searchtext;
	String synonyms;
	
	ArrayList<String> searchtext_words; 
	ArrayList<String> synonyms_words;
	
	String styleprefix="<span style='color:red;'>";
	String stylesuffix="</span>";
	
	String style2prefix="<span style='color:maroon;'>";
	String style2suffix="</span>";
	
	
	
	
	public AIPHighlightText(String searchtext){
		this(searchtext,null);
	}

	public AIPHighlightText(String searchtext,String synonyms){
		this.searchtext = searchtext;
		this.synonyms = synonyms;

		searchtext_words = AIPUtil.splitSearchText(searchtext);
		
		if(!NVL.isEmpty(synonyms)){
			ArrayList<ArrayList<String>> splitSearchSynonymsWords = AIPUtil.splitSearchSynonymsWords(searchtext, synonyms);
			
			synonyms_words=new ArrayList<String>();
			for (int i = 0; i < splitSearchSynonymsWords.size(); i++) {
				for (int j = 1; j < splitSearchSynonymsWords.get(i).size(); j++) {
					if(!NVL.isEmpty(splitSearchSynonymsWords.get(i).get(j))){
						synonyms_words.add(splitSearchSynonymsWords.get(i).get(j));//.replaceAll("%","")
					}
				}
			}
		}
	}
	
	public String highlight(String text){
		StringBuffer sb = new StringBuffer(text);
		highlight(sb);
		return sb.toString();
	}
	
	public void highlight(StringBuffer buf){
		highlight(buf,searchtext_words,styleprefix,stylesuffix);
		if(synonyms_words!=null && synonyms_words.size()>0){
			highlight(buf,synonyms_words,style2prefix,style2suffix);
		}
	}
	private void highlight(StringBuffer buf,ArrayList<String> words,String prefix,String suffix){
		if(buf!=null && buf.length()>0){
			for (int i = 0; i < words.size(); i++) {
				String replacement=prefix+words.get(i)+suffix;
				AIPUtil.replaceAllString(buf, words.get(i), replacement);
			}
		}
	}

	
	
	
	public String getSearchtext() {
		return searchtext;
	}

	public void setSearchtext(String searchtext) {
		this.searchtext = searchtext;
	}

	public String getSynonyms() {
		return synonyms;
	}

	public void setSynonyms(String synonyms) {
		this.synonyms = synonyms;
	}

	public String getStyleprefix() {
		return styleprefix;
	}

	public void setStyleprefix(String styleprefix) {
		this.styleprefix = styleprefix;
	}

	public String getStylesuffix() {
		return stylesuffix;
	}

	public void setStylesuffix(String stylesuffix) {
		this.stylesuffix = stylesuffix;
	}

	public String getStyle2prefix() {
		return style2prefix;
	}

	public void setStyle2prefix(String style2prefix) {
		this.style2prefix = style2prefix;
	}

	public String getStyle2suffix() {
		return style2suffix;
	}

	public void setStyle2suffix(String style2suffix) {
		this.style2suffix = style2suffix;
	}

	public ArrayList<String> getSearchtext_words() {
		return searchtext_words;
	}

	public ArrayList<String> getSynonyms_words() {
		return synonyms_words;
	}

	
	
	
	/*
	 * main
	 */
	public static void main(String[] args) {
		String searchtext="بودجه 1390";
		String synonyms=",بودجه,طرح,;";
		String text = "ث ـ وزارت نفت ا ز طريق شركت‌هاي دولتي تابعه ذي‌ربط موظف است در راستاي بودجه عملياتي، طرح‌هاي سرمايه‌اي از محل سهام 1390 خود از درصد پيش گفته و ساير منابع را مطابق با موافقتنامه‌هاي متبادله با معاونت برنامه‌ريزي و نظارت راهبردي رييس‌جمهور اجرا و گزارش عملكرد توليد نفت و گاز را به تفكيك هر ميدان و جداول طرح‌ها و پروژه‌هاي سرمايه‌گذاري و منابع تخصيص يافته و هزينه شده به همراه ميزان پيشرفت فيزيكي هر يك از طرح‌ها و پروژه‌ها را در مقاطع سه ماهه به مراجع ياد شده در تبصره (۳) جز (ب) اين بند ارائه نمايد. وزارت نفت موظف است به گونه‌اي عمل نمايد كه حداقل هشتاد و پنج هزار ميليارد(۰۰۰/۰۰۰/۰۰۰/۰۰۰/۸۵) ريال از منابع حاصل سهم وزرات نفت از طريق شركت‌هاي دولتي تابعه ذي‌ربط، صرف سرمايه‌گذاري در طرح‌هاي مذكور با اولويت ميادين مشترك نفتي و گازي گردد. ";

		AIPHighlightText highlight = new AIPHighlightText(searchtext, synonyms);
		
		System.out.println("AIPHighlightText.main():");
		System.out.println("text="+text);
		System.out.println("highlight.highlight(text);="+highlight.highlight(text));
		
	}
	
}
