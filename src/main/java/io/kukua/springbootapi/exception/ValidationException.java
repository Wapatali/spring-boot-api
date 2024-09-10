package io.kukua.springbootapi.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class ValidationException extends RuntimeException {

    private final Set<String> errors;

}
