package test;

import io.micrometer.context.ContextRegistry;
import io.micrometer.context.ThreadLocalAccessor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

public class MdcTest {
    @Test
    public void testMDC() {
        Logger log = LoggerFactory.getLogger("reactor");

        Hooks.enableAutomaticContextPropagation();

        // To deal with the entire MDC (if we ensured no third-party code modifies it):
        // ContextRegistry.getInstance().registerThreadLocalAccessor(new MdcAccessor());

        // To deal with an individual key in the MDC:
        ContextRegistry.getInstance().registerThreadLocalAccessor(
            "key",
            () -> MDC.get("key"),
            value -> MDC.put("key", value),
            () -> MDC.remove("key"));

        MDC.put("key", "Hello");

        Mono.just("Delayed")
            .delayElement(Duration.ofMillis(10))
            .doOnNext(log::info)
            // It is vital to capture MDC contents into Reactor's context
            // as it is the source of truth for propagating ThreadLocals
            //.contextCapture() // Can be skipped since reactor-core:3.5.7
            .block();
    }

    static class MdcAccessor implements ThreadLocalAccessor<Map<String, String>> {

        static final String KEY = "mdc";

        @Override
        public Object key() {
            return KEY;
        }

        @Override
        public Map<String, String> getValue() {
            return MDC.getCopyOfContextMap();
        }

        @Override
        public void setValue(Map<String, String> value) {
            MDC.setContextMap(value);
        }

        @Override
        public void reset() {
            MDC.clear();
        }
    }
}
