package com.myflinkjob.flink.etl.operators.filters;

import com.myflinkjob.commons.models.models.User;
import org.apache.flink.api.common.functions.FilterFunction;

import java.util.Objects;

public class ActiveUserFilter implements FilterFunction<User> {
    @Override
    public boolean filter(User user) {
        return Objects.nonNull(user) && user.isActive();
    }
}