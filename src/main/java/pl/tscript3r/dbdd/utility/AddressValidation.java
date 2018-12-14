package pl.tscript3r.dbdd.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddressValidation {
	private static final String OLD_HOSTNAME_REGEX = "P\\d{11}";
	private static final String NEW_HOSTNAME_REGEX = "PE\\d{7}";
	private static final String IP_REGEX = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private static final Pattern oldHostnamePattern = Pattern.compile(OLD_HOSTNAME_REGEX);
	private static final Pattern newHostnamePattern = Pattern.compile(NEW_HOSTNAME_REGEX);
	private static final Pattern ipPattern = Pattern.compile(IP_REGEX);
	
	public static Boolean hostnameValide(String hostname) {		
		Matcher oldMatcher = oldHostnamePattern.matcher(hostname);
		Matcher newMatcher = newHostnamePattern.matcher(hostname);

		if (oldMatcher.matches() || newMatcher.matches())
			return true;
		else
			return false;
	}

	public static boolean ipAdressValidate(final String ip) {
		return ipPattern.matcher(ip).matches();
	}
}
