package org.zeppelin.p3.common;

public class RegExp {
	// regular expression for a string with first letter as capital followed by
	// any sequence of characters
	public static final String REGEX_FIRST_CAPS = "^[A-Z](.*)";
	// regular expression that denotes all letters of a word as capital letters
	public static final String REGEX_ALL_CAPS = "[^a-z]+$";
	// regular expression that denotes that a word ends with ! or . or ?
	public static final String REGEX_SENT_ENDS = "(.*)[.!?]+$";
	// regular expression that denotes that punctuations ! or . or ?
	public static final String REGEX_PUNCTUATION = "[.!?]+$";
	// regular expression that denotes that a word contains punctuation ! or .
	// or ?
	public static final String REGEX_CONTAINS_PUNCTUATION = "(.*)[.!?]+(.*)";
	// regular expression that denotes that a string is in caps
	public static final String REGEX_ANY_CAPS = "(.*)[A-Z+](.*)";
	// regular expression that denotes that a formatted date i.e., yyyymmdd
	public static final String REGEX_FORMATTED_DATE = "[-]{0,1}([\\d]{8})";
	// regular expression that denotes that a formatted time i.e., HH:mm:ss
	public static final String REGEX_FORMATTED_TIME = "([\\d]{2}[:][\\d]{2}:[\\d]{2})";
	// regular expression that denotes a number e.g. 12 , 98.22
	public static final String REGEX_REAL_NUM = "^([+-]?[.]?[\\d]+[,]?)*[\\.]?([\\d]*)$";
	// regular expression that denotes a number or a number which is prior to
	// its derived form like 98/100, 1*2,98.22%
	public static final String REGEX_COMPOSITE_NUM = "^[\\d]+[/|*|.]?[\\d]+[%]?$";

	// regular expression that denotes a number sequence followed by an optional
	// period
	public static final String REGEX_NUM_PERIOD = "[\\d]+[\\.]?";

	// Will match tokens containing any number of instances of just hyphen. Eg
	// -, --- or --
	public static final String REGEX_FOR_JUST_HYPHEN = "^(\\s)*[-]+(\\s)*$";

	public static final String REGEX_FOR_ALPHABETS_HYPHEN = "([aA-zZ]+[-][aA-zZ]+$)";

	// This regex should match for various alphanumeric combinations with
	// hyphens like 6-6, BB3-A,
	// BB3B-A, BB3-A9 etc. //NOTE Currently not being used
	public static final String REGEX_FOR_HYPHEN_ALPHANUMERIC = "((\\d)+[-](\\d)+)|(([a-zA-Z]*)(\\d)+([a-zA-Z]*)[-](\\d)*[aA-zZ]+(\\d)*)|((\\d)*([a-zA-Z]+)(\\d)*[-][aA-zZ]*(\\d)+[aA-zZ]*)";

	// Eg. a-- , ++b
	public static final String REGEX_FOR_HYPHEN_AT_END_OR_START = "([-]{1,}[^0-9]*|[^0-9]*[-]{1,})"; /* "([aA-zZ]+[-]+$)|(^[-]+[aA-zZ]+$)" */

	public static final String REGEX_FOR_SPECIAL_CHARS_EXCLUDE_HYPHENS_PUNCTUATION = "[^ a-zA-Z0-9!?.-]";

	public static final String REGEX_FOR_SPECIAL_CHARS = "[^a-zA-Z0-9]";

	// Extended Punctuations for Date Handling - May or may not be there
	public static final String REGEX_EXT_PUNCTUATION = "([,;]{0,}[?.!]{0,}$)";

	// Match a valid date from 1-31 or 01-31. It might be followed by a
	// punctuation.
	public static final String REGEX_DATE = "([0]{0,1}[1-9]{1}|[12][0-9]|[3][0-1])";

	public static final String REGEX_YEAR_BC_AD = "(\\d{1,4})(BC|AD)";

	public static final String REGEX_BC_AD = "(BC|AD)";

	public static final String REGEX_YEAR = "(\\d{1,4})";

	public static final String REGEX_FULL_YEAR = "(\\d{4})";

	public static final String REGEX_FULL_YEAR_BC_AD = "(\\d{4})(BC|AD)";

	public static final String REGEX_COMPOSITE_YEAR = "(\\d{4}[-/]\\d{2})";

	public static final String REGEX_MONTHS = "([a-zA-Z]{3,9})";

	// Regular Expression for time
	public static final String REGEX_HOURS = "([01]?[0-9]|2[0-3])";

	public static final String REGEX_MINUTES_SECONDS = "(([0-5][0-9]){0,1})";

	public static final String REGEX_TIME_SEPARATOR = "([:]{0,1})";

	public static final String REGEX_TIME_AM_PM = "((am|pm|AM|PM|hrs|HRS|hours|HOURS){0,1})";

	// Regex For Place and Date Separations
	// Assuming it will be always in the format
	// Month DD. For month no word can be less than 3 (jan, feb) chars or more
	// than 9 i.e., for September
	public static final String REGEX_FOR_PLACE_DATE = "(.*)(\\s+([A-Za-z]{3,9}[\\s]+[0-9]{1,2}))";

}
