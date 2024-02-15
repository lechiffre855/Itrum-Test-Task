package ru.lechiffre.itrumtask.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.lechiffre.itrumtask.model.Wallet;
import ru.lechiffre.itrumtask.model.WalletRequest;
import ru.lechiffre.itrumtask.repositories.WalletRepository;
import ru.lechiffre.itrumtask.util.WalletNotFoundException;
import ru.lechiffre.itrumtask.util.WalletNotWithdrawException;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    private String responseMessage = "";
    public String getResponseMessage() {
        return responseMessage;
    }

    public Wallet getWallet(UUID uuid){
        Optional<Wallet> foundWallet = walletRepository.findById(uuid);

        return foundWallet.orElseThrow(WalletNotFoundException::new);
    }

    @Transactional
    public void postWallet(WalletRequest walletRequest){
        Optional<Wallet> foundWallet = walletRepository.findById(walletRequest.getWalletId());

        if (walletRequest.getOperationType().equals("DEPOSIT")) {
            if (foundWallet.isEmpty()){
                walletRepository.save(new Wallet(walletRequest.getWalletId(), walletRequest.getAmount()));
                responseMessage = "The account with specified requisites has been successfully created and replenished.";
            } else {
                Double tempAmount = foundWallet.get().getAmount();
                walletRepository.save(new Wallet(walletRequest.getWalletId(), (walletRequest.getAmount()) + tempAmount));
                responseMessage = "The account with specified requisites has been successfully replenished.";
            }

        } else if (walletRequest.getOperationType().equals("WITHDRAW")) {
            if (foundWallet.isPresent()) {
                if (foundWallet.get().getAmount() > walletRequest.getAmount()) {
                    Double tempAmount = foundWallet.get().getAmount();
                    walletRepository.save(new Wallet(walletRequest.getWalletId(), (tempAmount - walletRequest.getAmount())));
                    responseMessage = "The given amount has been withdrawn from the specified account.";
                } else if (foundWallet.get().getAmount().equals(walletRequest.getAmount())) {
                    walletRepository.delete(new Wallet(walletRequest.getWalletId(), walletRequest.getAmount()));
                    responseMessage = "The given amount had been withdrawn from the specified account and then the account was closed.";
                } else
                    throw new WalletNotWithdrawException("You do not have enough funds in your account. " +
                            "The amount in your request is more than the amount on your account (" + foundWallet.get().getAmount() + "). Try again!");
            } else
                throw new WalletNotFoundException();
        }
    }
}
