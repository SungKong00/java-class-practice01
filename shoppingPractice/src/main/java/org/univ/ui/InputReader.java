package org.univ.ui;

import java.util.Scanner;
import java.util.function.Predicate;

public class InputReader {
    private final Scanner sc = new Scanner(System.in);

    /**
     * 프롬프트를 출력하고, 검증이 통과할 때까지 반복해서 한 줄을 읽어 반환
     */
    public String readLine(String prompt, Predicate<String> validator, String errorMsg) {
        String line;
        while (true) {
            System.out.print(prompt);
            line = sc.nextLine().trim();
            if (validator.test(line)) return line;
            System.out.println(">> 오류: " + errorMsg);
        }
    }

    public void close() {
        sc.close();
    }
}
