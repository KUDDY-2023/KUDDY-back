package com.kuddy.common.spot.exception;

import com.kuddy.common.exception.custom.NotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class SpotNotFoundException extends NotFoundException {
    public SpotNotFoundException(Long id) {
        super("id=" + id);
    }
}
