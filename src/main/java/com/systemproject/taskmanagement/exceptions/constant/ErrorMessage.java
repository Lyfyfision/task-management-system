package aston.lab.javacardservice.exception.constant;

public enum ErrorMessage {

    ACCOUNT_NOT_FOUND("Страница не найдена"),

    SERVER_EXCEPTION("Непредвиденная ошибка. Попробуйте позже"),

    AUTHORIZED_EXCEPTION("Доступ запрещён"),

    PRODUCTS_NOT_FOUND("Не найдено ни одного продукта");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
