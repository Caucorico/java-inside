package fr.umlv.java.inside;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JSONProperty {
        String value() default "";
}
