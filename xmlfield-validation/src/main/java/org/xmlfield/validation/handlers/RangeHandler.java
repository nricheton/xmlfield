package org.xmlfield.validation.handlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.xmlfield.validation.annotations.Range;

public class RangeHandler implements IHandler {

    @Override
    public boolean handles(Annotation a) {
        return a instanceof Range;
    }

    @Override
    public Set<ConstraintViolation<Object>> validate(Annotation a, Method m, Object o, Class<?> group)
            throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

        Range as = (Range) a;

        if ((group == null && as.groups().length == 0) || ArrayUtils.contains(as.groups(), group)) {

            double currentValue = 0;

            Object result = m.invoke(o, new Object[] {});
            if (result == null)
                return null;

            if (result instanceof Number) {
                currentValue = ((Number) result).doubleValue();
            }

            if (currentValue > as.max() || currentValue < as.min())
                return createResultFromViolation(new ConstraintViolation<Object>(m.getName(), "min/max",
                        String.valueOf(currentValue)));
        }
        return null;
    }

    private <T> Set<ConstraintViolation<T>> createResultFromViolation(ConstraintViolation<T> c) {

        Set<ConstraintViolation<T>> result = new HashSet<ConstraintViolation<T>>();

        result.add(c);
        return result;

    }

}
