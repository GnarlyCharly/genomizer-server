package database;

import java.util.ArrayList;

/**
 * Class to parse a PubMed String
 *
 * @author dv11ann
 * R�r f�r tusan inte din hj�kel, detta �r min klass :P get your own!
 */
public class PubMedParser {

	private ArrayList<String> values;
	private String whereString;
	private ArrayList<String> fileAnno;

	/**
	 * Constructor
	 *
	 */
	public PubMedParser() {
		values = new ArrayList<String>();
		whereString = new String();
		makeFileAnno();
	}

	/**
	 * Public method to get the parsed PubMed string and its values
	 *
	 * @param pubMed
	 * @return
	 */
	public ParsedPubMed parsePubMed(String pubMed) {
		parse(pubMed);

		return new ParsedPubMed(whereString, values);
	}

	/**
	 * Private method to parse a PubMed string.
	 *
	 * @param pubMed
	 */
	private void parse(String pubMed) {

		StringBuffer totStr = new StringBuffer();
		boolean foundCol = false;

		ArrayList<String> valueList = new ArrayList<String>();

		int startklam = 0;
		int endklam = 0;

		totStr.append(pubMed);
		System.out.println(pubMed);
		for(int i = 0; i < totStr.length(); i++) {
			char c = totStr.charAt(i);
			foundCol = false;
			if(c == '[') {
				startklam = i +1;
			}

			if (c == ']') {
				endklam = i;
				foundCol = true;
			}

			if(foundCol) {
				String s = totStr.substring(startklam, endklam);
				int k = startklam;

				while((k <= totStr.length()) && (k > (-1)) && (totStr.charAt(k) != ' ') && totStr.charAt(k) != '(') {
					k--;
				}
				k++;

				
				boolean isFileAnno = false;

				for(int j = 0; j < fileAnno.size(); j ++) {
					if(s.equals(fileAnno.get(j))) {
						isFileAnno = true;
					}
				}

				String appendString = null;
				if(isFileAnno) {
					appendString = s + " = ?";
				} else {
					appendString = "(Label = ? AND Value = ?)";
					valueList.add(s);
				}
				
				valueList.add(totStr.substring(k, startklam -1));

				totStr.delete(k, endklam +1);

				totStr.insert(k, appendString);

				i = k + appendString.length();
			}
		}
		values = valueList;
		whereString = totStr.toString();
	}

	private void makeFileAnno() {
		fileAnno = new ArrayList<String>();
		fileAnno.add("FileID");
		fileAnno.add("Path");
		fileAnno.add("Type");
		fileAnno.add("Date");
		fileAnno.add("MetaData");
		fileAnno.add("Author");
		fileAnno.add("Uploader");
		fileAnno.add("IsPrivate");
		fileAnno.add("ExpID");
		fileAnno.add("GRVersion");
	}
}