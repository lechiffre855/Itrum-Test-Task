package ru.lechiffre.itrumtask.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.lechiffre.itrumtask.model.Wallet;
import ru.lechiffre.itrumtask.model.WalletRequest;
import ru.lechiffre.itrumtask.repositories.WalletRepository;
import ru.lechiffre.itrumtask.util.BadRequestException;
import ru.lechiffre.itrumtask.util.WalletNotFoundException;
import ru.lechiffre.itrumtask.util.WalletNotWithdrawException;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
        startQueueProcessor();
    }

//    private String responseMessage = "";
//    public String getResponseMessage() {
//        return responseMessage;
//    }

    @Transactional
    public Wallet getWallet(UUID uuid){
        Optional<Wallet> foundWallet = walletRepository.findByWalletId(uuid);

        return foundWallet.orElseThrow(WalletNotFoundException::new);
    }


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
                    postWalletInternal(request);
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

    @Transactional
    public void postWalletInternal(WalletRequest walletRequest){
        Optional<Wallet> foundWallet = walletRepository.findByWalletId(walletRequest.getWalletId());

        if (walletRequest.getOperationType().equals("DEPOSIT")) {
            if (foundWallet.isEmpty()){
                walletRepository.save(new Wallet(walletRequest.getWalletId(), walletRequest.getAmount()));
//                responseMessage = "The account with specified requisites has been successfully created and replenished.";
            } else {
                Double tempAmount = foundWallet.get().getAmount() + walletRequest.getAmount();
                Wallet foundWallet1 = foundWallet.get();
                foundWallet1.setAmount(tempAmount);
                walletRepository.save(foundWallet1);
//                responseMessage = tempAmount + "  " + foundWallet.get().getAmount().toString() + " The account with specified requisites has been successfully replenished.";
            }

        } else if (walletRequest.getOperationType().equals("WITHDRAW")) {
            if (foundWallet.isPresent()) {
                if (foundWallet.get().getAmount() > walletRequest.getAmount()) {
                    Double tempAmount = foundWallet.get().getAmount() - walletRequest.getAmount();
                    Wallet foundWallet1 = foundWallet.get();
                    foundWallet1.setAmount(tempAmount);
                    walletRepository.save(foundWallet1);
//                    responseMessage = "The given amount has been withdrawn from the specified account.";
                } else if (foundWallet.get().getAmount().equals(walletRequest.getAmount())) {
                    walletRepository.delete(foundWallet.get());
//                    responseMessage = "The given amount had been withdrawn from the specified account and then the account was closed.";
                } else
                    throw new WalletNotWithdrawException("You do not have enough funds in your account. " +
                            "The amount in your request is more than the amount on your account (" + foundWallet.get().getAmount() + "). Try again!");
            } else
                throw new WalletNotFoundException();
        }
    }

}

