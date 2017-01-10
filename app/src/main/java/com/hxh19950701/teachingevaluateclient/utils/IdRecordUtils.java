package com.hxh19950701.teachingevaluateclient.utils;

import com.hxh19950701.teachingevaluateclient.bean.service.IdRecord;

import java.util.List;

public class IdRecordUtils {

    private IdRecordUtils() {
        throw new UnsupportedOperationException("This class cannot be instantiated, and its methods must be called directly.");
    }

    public static final IdRecord findIdRecord(List<? extends IdRecord> data, int id) {
        if (id < 0) return null;
        for (IdRecord item : data) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

}
