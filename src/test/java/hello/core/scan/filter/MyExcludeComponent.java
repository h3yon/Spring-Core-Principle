package hello.core.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // class
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
