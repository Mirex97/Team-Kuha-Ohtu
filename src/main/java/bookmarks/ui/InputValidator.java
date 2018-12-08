package bookmarks.ui;

public class InputValidator {

    public static boolean validateISBN(String isbn) {
        if (isbn == null) {
            return false;
        }

        isbn = isbn.replaceAll("[- ]", "");

        switch (isbn.length()) {
            case 10: {
                try {
                    int checksum = calculateISBN10Checksum(isbn);

                    if (checksum == 10) {
                        return ("" + isbn.charAt(9)).toLowerCase().equals("x");
                    }

                    return checksum == Integer.parseInt("" + isbn.charAt(9));
                } catch (Exception e) {
                    return false;
                }
            }

            case 13: {
                if (!isbn.substring(0, 3).equals("978") && !isbn.substring(0, 3).equals("979")) {
                    return false;
                }

                try {
                    int checksum = calculateISBN13Checksum(isbn);

                    return checksum == Integer.parseInt("" + isbn.charAt(12));
                } catch (Exception e) {
                    return false;
                }
            }

            default: {
                return false;
            }
        }

    }

    private static int calculateISBN10Checksum(String isbn) throws NumberFormatException {
        int total = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Integer.parseInt("" + isbn.charAt(i));
            total += (10 - i) * digit;
        }

        return (11 - (total % 11)) % 11;
    }

    private static int calculateISBN13Checksum(String isbn) throws NumberFormatException {
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

        return checksum;
    }

    public static boolean validateLink(String input) {
        String urlPattern = "^(http(s?)://)?[a-zA-Z0-9_\\-]+\\.([A-Za-z/]{2,5})[a-zA-Z0-9_/\\&\\?\\=\\-\\.\\~\\%]*";
        return input.matches(urlPattern);
    }
}
