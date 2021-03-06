package org.infinispan.factories.annotations;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Method level annotation that indicates a (no-param) method to be called on a component after the
 * {@link org.infinispan.factories.ComponentRegistry} has been fully initialized
 * <p/>
 *
 * @author Tristan Tarrant
 * @since 9.2
 */
@Target(METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostStart {
   /**
    * Optional parameter which defines the order in which this method will be called.  Defaults to 10.
    *
    * @return execution priority
    */
   int priority() default 10;
}
