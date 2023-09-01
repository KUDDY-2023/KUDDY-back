package com.kuddy.common.pick.exception;


import com.kuddy.common.exception.custom.NotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PickNotFoundException extends NotFoundException {
    public PickNotFoundException(Long id) {
        super("id=" + id);
    }
}