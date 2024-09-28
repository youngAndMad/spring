package kz.danekerscode.asynchttpresponses;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/greeting")
public class GreetingController {

    private final GreetingService greetingService;

    @GetMapping("/sync")
    Greeting syncGreeting(@RequestParam String name) {
        log.info("Start processing sync greeting");
        Greeting greeting = greetingService.getGreeting(name);
        log.info("End processing sync greeting");
        return greeting;
    }

    @GetMapping("/async/deferred")
    DeferredResult<Greeting> asyncDeferredGreeting(
            @RequestParam String name
    ) {
        log.info("Start processing async request");
        DeferredResult<Greeting> deferredGreeting = new DeferredResult<>();
        ForkJoinPool.commonPool()
                .submit(() -> {
                    log.info("Start processing async deferred greeting");
                    Greeting greeting = greetingService.getGreeting(name);
                    log.info("End processing async deferred greeting");
                    deferredGreeting.setResult(greeting);
                });
        log.info("Http thread released");
        return deferredGreeting;
    }

    @GetMapping("/async/completable")
    CompletableFuture<Greeting> asyncCompletableGreeting(
            @RequestParam String name
    ) {
        log.info("Start processing async request with completable future");

        Greeting greeting = greetingService.getGreeting(name);
        CompletableFuture<Greeting> completableFuture = CompletableFuture.supplyAsync(() -> {
            log.info("Start processing async completable greeting");
            log.info("End processing async completable greeting");
            return greeting;
        });

        log.info("Http thread released with completable future");

        return completableFuture;
    }

    @GetMapping("/async/completable/deferred")
    DeferredResult<Greeting> asyncCompletableDeferredGreeting(
            @RequestParam String name
    ) {
        log.info("Start processing async request with completable future and deferred result");

        DeferredResult<Greeting> deferredGreeting = new DeferredResult<>();
        CompletableFuture.supplyAsync(() -> {
            log.info("Start processing async completable deferred greeting");
            Greeting greeting = greetingService.getGreeting(name);
            log.info("End processing async completable deferred greeting");
            deferredGreeting.setResult(greeting);
            return greeting;
        });

        log.info("Http thread released with completable future and deferred result");

        return deferredGreeting;
    }

}

record Greeting(
        String name
) {
    public Greeting {
        name = "Hello, " + name;
    }
}

@Service
@Slf4j
class GreetingService {
    Greeting getGreeting(String name) {
        try {
            log.info("Start processing greeting for {}", name);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return new Greeting(name);
    }
}
