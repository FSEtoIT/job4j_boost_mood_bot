package ru.job4j.bmb.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public final class FakeBotCondition implements Condition {

    @Override
    public boolean matches(
            final ConditionContext context,
            final AnnotatedTypeMetadata metadata) {
        String mode = context.getEnvironment().getProperty("telegram.mode");
        return "fake".equalsIgnoreCase(mode);
    }
}
