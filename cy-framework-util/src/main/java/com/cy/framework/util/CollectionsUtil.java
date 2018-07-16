package com.cy.framework.util;

import java.util.Comparator;
import java.util.List;

public class CollectionsUtil {
    /**
     * @param intStr
     */
    public static void sort(List<String> intStr) {
        if (intStr == null || intStr.size() <= 0) {
            return;
        }
        intStr.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (Integer.parseInt(o1) > Integer.parseInt(o2)) {
                    return 1;
                }
                return -1;
            }
        });
    }

    public static void sortChar(List<String> intStr) {
        if (intStr == null || intStr.size() <= 0) {
            return;
        }
        intStr.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (Integer.parseInt(o1) > Integer.parseInt(o2)) {
                    return 1;
                }
                return -1;
            }
        });
    }

    public static void shuffleLength(List<String> intStr) {
        if (intStr == null || intStr.size() <= 0) {
            return;
        }
        intStr.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if ((o1 != null && o1.length() > 0) && (o2 != null && o2.length() > 0)) {
                    if (o1.length() < o2.length()) {
                        return 1;
                    }
                }
                return -1;
            }
        });
    }

    public static void sortLength(List<String> intStr) {
        if (intStr == null || intStr.size() <= 0) {
            return;
        }
        intStr.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if ((o1 != null && o1.length() > 0) && (o2 != null && o2.length() > 0)) {
                    if (o1.length() > o2.length()) {
                        return 1;
                    }
                }
                return -1;
            }
        });
    }

    public static void shuffle(List<String> intStr) {
        if (intStr == null || intStr.size() <= 0) {
            return;
        }
        intStr.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (Integer.parseInt(o1) < Integer.parseInt(o2)) {
                    return 1;
                }
                return -1;
            }
        });
    }

    public static String toString(List<String> intStr) {
        if (intStr != null && intStr.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String str : intStr) {
                if (StringUtil.isEmpty(str)) {
                    continue;
                }
                stringBuilder.append(str).append(",");
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        return null;
    }

    public static String toStringInt(List<Integer> intStr) {
        if (intStr != null && intStr.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (Integer str : intStr) {
                if (str == null) {
                    continue;
                }
                stringBuilder.append(str).append(",");
            }
            return stringBuilder.substring(0, stringBuilder.length() - 1);
        }
        return null;
    }
}
