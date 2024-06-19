package io.kukua.springbootapi.validation;

public interface Validator<T> {

    void validateBeforeInsert(T entity) throws ValidationException;

}
