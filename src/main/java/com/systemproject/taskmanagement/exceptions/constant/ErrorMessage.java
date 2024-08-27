package com.systemproject.taskmanagement.exceptions.constant;

public enum ErrorMessage {

  //  ACCOUNT_NOT_FOUND("Страница не найдена"),

    AUTHORIZED_EXCEPTION("Доступ запрещён"),

    AUTHENTICATE_EXCEPTION("Пользователь с указанной эл. почтой не найден"),

    TASKS_NOT_FOUND("Не найдено ни одной таски");

    private String message;

    ErrorMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
