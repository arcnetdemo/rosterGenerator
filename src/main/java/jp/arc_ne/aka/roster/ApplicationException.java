package jp.arc_ne.aka.roster;

public class ApplicationException extends Exception {

	/** SerialVersionUID */
	private static final long serialVersionUID = 2501800010113079523L;

	public ApplicationException(String string) {
		super(string);
	}

	public ApplicationException(String string, Throwable throwable) {
		super(string, throwable);
	}

}
