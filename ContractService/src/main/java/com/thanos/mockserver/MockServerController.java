package com.thanos.mockserver;

import com.thanos.contract.domain.Contract;
import com.thanos.contract.domain.SimpleCache;
import com.thanos.contract.infrastructure.eventbus.NewMockEvent;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class MockServerController implements Job {

    ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private List<String> existingIndex;

    public void init() {
        ScheduleHelper.addScheduler(
                MockServerController.class, "DetectNewMock", "0 0/5 * ? * * *", null);
        startNewMocks();
    }

    /**
     * Group mock server by provider-consumer
     */
    synchronized void startNewMocks() {
        existingIndex = SimpleCache.getMockInfoList().stream()
                .map(NewMockEvent::getIndex).collect(Collectors.toList());

        final Set<String> indexs = SimpleCache.getContracts().stream()
                .collect(Collectors.groupingBy(Contract::getIndex)).keySet();

        indexs.stream().filter(index -> !existingIndex.contains(index))
                .forEach(this::createNewMockForIndex);
    }

    private void createNewMockForIndex(String index) {
        log.info("Creating new mock server {}", index);
        executor.execute(new MockServerHandler(index));
    }

//    public void shutdown() {
//        try {
//            log.info("Going to shutdown all MockServerHandler");
//            final List<Runnable> threads = executor.shutdownNow();
//            executor.awaitTermination(5, TimeUnit.SECONDS);
//        } catch (InterruptedException e) {
//            log.warn("Task interrupted");
//        } finally {
//            executor.shutdownNow();
//            log.info("MockServerHandler shutdown completed");
//        }
//    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("Scheduler to check new mock every 5 mins");
        startNewMocks();
    }

}
