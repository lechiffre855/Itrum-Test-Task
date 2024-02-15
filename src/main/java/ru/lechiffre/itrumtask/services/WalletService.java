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
            } else {
                Double tempAmount = foundWallet.get().getAmount();
                walletRepository.save(new Wallet(walletRequest.getWalletId(), (walletRequest.getAmount()) + tempAmount));
            }

        } else if (walletRequest.getOperationType().equals("WITHDRAW")) {
            if (foundWallet.isPresent()) {
                if (foundWallet.get().getAmount() > walletRequest.getAmount()) {
                    Double tempAmount = foundWallet.get().getAmount();
                    walletRepository.save(new Wallet(walletRequest.getWalletId(), (walletRequest.getAmount()) + tempAmount));
                } else if (foundWallet.get().getAmount() == walletRequest.getAmount())
                    walletRepository.delete(new Wallet(walletRequest.getWalletId(), walletRequest.getAmount()));
                else
                    throw new WalletNotWithdrawException("You do not have enough funds in your account. " +
                            "The amount in your request is more than the amount in your account (" + foundWallet.get().getAmount() + ").");
            } else
                throw new WalletNotFoundException();
        }
    }
}
