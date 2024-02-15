package ru.lechiffre.itrumtask.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.lechiffre.itrumtask.model.Wallet;
import ru.lechiffre.itrumtask.model.WalletRequest;
import ru.lechiffre.itrumtask.services.WalletService;
import ru.lechiffre.itrumtask.util.BadRequestException;
import ru.lechiffre.itrumtask.util.WalletErrorResponse;
import ru.lechiffre.itrumtask.util.WalletNotFoundException;
import ru.lechiffre.itrumtask.util.WalletNotWithdrawException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/wallet")
    public ResponseEntity<String> postWallet(@RequestBody @Valid WalletRequest walletRequest, BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            StringBuilder errorMsg = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error: errors){
                errorMsg.append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append("; ");
            }
            throw new BadRequestException(errorMsg.toString());
        }

        walletService.postWallet(walletRequest);

        return new ResponseEntity<>(walletService.getResponseMessage(), HttpStatus.OK);
    }

    @GetMapping("/wallets/{WALLET_UUID}")
    public Wallet getWallet(@PathVariable("WALLET_UUID")UUID uuid) {
        return walletService.getWallet(uuid);
    }

    @ExceptionHandler
    private ResponseEntity<WalletErrorResponse> handleException(WalletNotFoundException e){
        WalletErrorResponse response = new WalletErrorResponse(
                "The wallet with this ID wasn't found. Try another one!",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    private ResponseEntity<WalletErrorResponse> handleException(BadRequestException e){
        WalletErrorResponse response = new WalletErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<WalletErrorResponse> handleException(WalletNotWithdrawException e) {
        WalletErrorResponse response = new WalletErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler
    private ResponseEntity<WalletErrorResponse> handleException (HttpMessageNotReadableException e) {
        WalletErrorResponse response = new WalletErrorResponse(
                ("You posted a invalid JSON-request. " + e.getMessage()),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
