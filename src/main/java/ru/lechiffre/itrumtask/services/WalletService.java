package ru.lechiffre.itrumtask.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.lechiffre.itrumtask.model.Wallet;
import ru.lechiffre.itrumtask.model.WalletRequest;
import ru.lechiffre.itrumtask.util.BadRequestException;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WalletService {

    private final WalletServiceTransactional walletServiceTransactional;

    @Autowired
    public WalletService(WalletServiceTransactional walletServiceTransactional) {
        startQueueProcessor();
        this.walletServiceTransactional = walletServiceTransactional;
    }

    public Wallet getWallet(UUID uuid){ return walletServiceTransactional.getWalletTrans(uuid); }

    public void postWallet(WalletRequest walletRequest) {
        queue.offer(walletRequest);
    }

    private final ConcurrentLinkedQueue<WalletRequest> queue = new ConcurrentLinkedQueue<>();

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    private void startQueueProcessor() {
        executorService.scheduleWithFixedDelay(this::processQueue, 0, 1, TimeUnit.SECONDS);
    }

    private void processQueue() {
        while (!queue.isEmpty()) {
            WalletRequest request = queue.poll();
            if (request != null) {
                try {
                    walletServiceTransactional.postWalletTrans(request);
                } catch (BadRequestException e) {
                    e.printStackTrace();
                }
            }
        }
    }

//    public void shutdown() {
//        executorService.shutdown();
//        try {
//            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
//                executorService.shutdownNow();
//            }
//        } catch (InterruptedException e) {
//            executorService.shutdownNow();
//        }
//    }


}

