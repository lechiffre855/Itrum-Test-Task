package ru.lechiffre.itrumtask.util;

public class WalletNotWithdrawException extends RuntimeException{
    public WalletNotWithdrawException(String msg){
        super(msg);
    }
}
