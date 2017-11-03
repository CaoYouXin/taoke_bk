package com.taoke.miquaner.util;

import com.taoke.miquaner.data.EAdmin;
import org.springframework.data.jpa.repository.JpaRepository;

public class JpaUtil {

    public static Object persistent(JpaRepository repository, Object obj) {
        try {
            Object saved = repository.save(obj);
            return Result.success(saved);
        } catch (Exception e) {
            return Result.fail(new ErrorR(ErrorR.CAN_NOT_SAVE_OBJECT, ErrorR.CAN_NOT_SAVE_OBJECT_MSG));
        }
    }

}
