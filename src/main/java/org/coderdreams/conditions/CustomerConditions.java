package org.coderdreams.conditions;

import org.coderdreams.util.Utils;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class CustomerConditions {

    public static final class FbCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return isMatch(CustomerType.FB, context);
        }
    };

    public static final class GoogleCondition implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            return isMatch(CustomerType.GOOGLE, context);
        }
    };


    private static boolean isMatch(CustomerType customerType, ConditionContext context) {
        Environment env = context.getEnvironment();
        if(env == null) {
            return false;
        }
        String customer = Utils.getCustomer();
        return customerType.name().equals(customer);
    }
}
