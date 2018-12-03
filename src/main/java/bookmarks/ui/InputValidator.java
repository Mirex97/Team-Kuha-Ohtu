package bookmarks.ui;

public class InputValidator {
	
	public static boolean validateIsbn(String isbn) {
        if (isbn == null) {
            return false;
        }

        isbn = isbn.replaceAll("-", "");
		isbn = isbn.replaceAll(" ", "");

        if (isbn.length() != 13) {
            return false;
        }
		
		if (!isbn.substring(0, 3).equals("978") && !isbn.substring(0, 3).equals("979")) {
			return false;
		}

        try {
            int total = 0;
            for (int i = 0; i < 12; i++) {
                int digit = Integer.parseInt("" + isbn.charAt(i));
				if (i % 2 == 0) {
					total += digit;
				} else {
					total += digit * 3;
				}
            }

            int checksum = 10 - (total % 10);
            if (checksum == 10) {
                checksum = 0;
            }

            return checksum == Integer.parseInt("" + isbn.charAt(12));
        } catch (Exception e) {
            return false;
        }
    }
	
	public static boolean validateLink(String input) {
		String urlPattern = "^(http(s?)://)?[a-zA-Z0-9_\\-]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
		return input.matches(urlPattern);
	}
}
