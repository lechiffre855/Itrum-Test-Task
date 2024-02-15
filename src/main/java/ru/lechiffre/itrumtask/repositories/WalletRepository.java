package ru.lechiffre.itrumtask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.lechiffre.itrumtask.model.Wallet;

import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
