package leetcode819;

import java.util.*;

/**
 * @author yyw
 * @date 2019/8/1
 * <p>
 * 执行用时 :26 ms, 在所有 Java 提交中击败了78.78%的用户
 * 内存消耗 :37.5 MB, 在所有 Java 提交中击败了82.89%的用户
 * <p>
 * 解题思路：一个字符一个字符判断，然后切割成需要判断的各个字符串，然后利用map记录每个字符串的出现次数，用一个标志位和标志字符串来记录出现次数最多的。
 **/
public class SolutionOne {
    private static String mostCommonWord(String paragraph, String[] banned) {
        int max = 1;
        String result = null;
        Map<String, Integer> map = new HashMap<>();
        Set<String> collect = new HashSet<>(Arrays.asList(banned));
        String s = paragraph.toLowerCase();
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (char ch : chars) {
            if (Character.isLowerCase(ch)) {
                sb.append(ch);
            } else {
                sb.append(",");
            }
        }
        String[] str = sb.toString().split(",");
        for (String s1 : str) {
            //判断是不是禁用
            if (!collect.contains(s1) && !"".equals(s1)) {
                if (map.containsKey(s1)) {
                    Integer count = map.get(s1) + 1;
                    map.put(s1, count);
                    if (count > max) {
                        max = count;
                        result = s1;
                    }
                } else {
                    map.put(s1, 1);
                    if (result == null) {
                        result = s1;
                    }
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        String paragraph = "a, a, a, a, b,b,b,c, c";
        String[] banned = new String[]{"a"};
        mostCommonWord(paragraph, banned);
        System.out.println(mostCommonWord(paragraph, banned));
    }
}
