package com.myflinkjob.flink.etl.operators.filters;

import com.myflinkjob.commons.models.models.ContactBy;
import com.myflinkjob.commons.models.models.User;
import org.apache.flink.api.common.functions.FilterFunction;

import java.util.Objects;

public class EmailUserFilter implements FilterFunction<User> {
    @Override
    public boolean filter(User user) {
        return Objects.nonNull(user) && (user.getContactBy() == ContactBy.ALL
                || user.getContactBy() == ContactBy.EMAIL);
    }
}
