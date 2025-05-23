package org.univ.exception;

// 잘못된 입력값(예: 주소, 이름 등) 검증용 예외
public class InvalidInputException extends RuntimeException {
    public InvalidInputException(String message) {
        super(message);
    }
}