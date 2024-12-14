package org.acme.amqp.processor;

import java.util.Random;

import org.acme.amqp.model.Quote;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * A bean consuming data from the "request" AMQP queue and giving out a random
 * quote.
 * The result is pushed to the "quotes" AMQP queue.
 */
@ApplicationScoped
public class QuoteProcessor {

    private Random random = new Random();

    @Incoming("quote-requests") // <1>
    @Outgoing("quotes") // <2>
    @Blocking // <3>
    public Quote process(String quoteRequest) throws InterruptedException {
        // simulate some hard working task
        Thread.sleep(200);
        Log.info("---- Request is being answered. ----");
        return new Quote(quoteRequest, random.nextInt(100));
    }
}