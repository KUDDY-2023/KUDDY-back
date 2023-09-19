package com.kuddy.common.notification.exception;

import com.kuddy.common.exception.custom.ApplicationException;
import com.kuddy.common.exception.custom.NotFoundException;

public class NotificationNotFoundException extends NotFoundException {
    public NotificationNotFoundException(Long id) {
        super("id=" + id);
    };
}
