package com.kuddy.common.review.exception;

import com.kuddy.common.exception.custom.NotFoundException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ReviewNotFoundException extends NotFoundException  {
    public ReviewNotFoundException(Long id) {
        super("id=" + id);
    }
}