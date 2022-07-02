
package uk.ac.aston.components.security.dataflow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PrivacyFunction {
    public String function() default "read";
    public String id() default "field";
}

