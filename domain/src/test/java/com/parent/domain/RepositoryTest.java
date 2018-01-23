package com.parent.domain;


import android.support.annotation.NonNull;

import com.actors.Actor;
import com.actors.ActorSystem;
import com.actors.Message;

import org.javatuples.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.TestScheduler;

import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST;
import static com.parent.domain.Domain.MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS;
import static org.junit.Assert.assertEquals;

public class RepositoryTest {

    private final TestScheduler scheduler = new TestScheduler();
    private final Requester requester = new Requester(scheduler);
    private final MockFiveDayForecastRequester api = new MockFiveDayForecastRequester();
    private final Repository repository = new Repository(api, new MockCitySearcher(),scheduler);

    @Before
    public void registerToActorSystem() {
        ActorSystem.register(requester);
        ActorSystem.register(repository);
    }

    @After
    public void unregisterToActorSystem() {
        ActorSystem.unregister(requester);
        ActorSystem.unregister(repository);
        api.setFailure(null);
    }

    @Test
    public void onRequestFiveDayForecastWithSuccessfulResponseThenReplyWithSuccessMessage() {
        Message message = new Message(MSG_REQUEST_FIVE_DAY_FORECAST, Pair.with(0L,Requester.class));

        ActorSystem.send(message, Repository.class);
        scheduler.triggerActions();

        assertEquals(MSG_REQUEST_FIVE_DAY_FORECAST_SUCCESS, requester.message.getId());
    }

    @Test
    public void onRequestFiveDayForecastWithFailingResponseThenReplyWithFailureMessage() {
        Message message = new Message(MSG_REQUEST_FIVE_DAY_FORECAST, Pair.with(0L,Requester.class));
        RuntimeException failure = new UnsupportedOperationException("!!");
        api.setFailure(failure);

        ActorSystem.send(message, Repository.class);
        scheduler.triggerActions();

        assertEquals(failure, requester.message.getContent());
    }


    private static class Requester implements Actor {

        final Scheduler scheduler;
        Message message;

        Requester(Scheduler scheduler) {
            this.scheduler = scheduler;
        }

        @Override
        public void onMessageReceived(Message message) {
            this.message = message;
        }

        @NonNull
        @Override
        public Scheduler observeOnScheduler() {
            return scheduler;
        }
    }

}


