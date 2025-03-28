package com.example.swiftCodes.service;

import com.example.swiftCodes.exception.BankAlreadyExistsException;
import com.example.swiftCodes.exception.BankNotFoundException;
import com.example.swiftCodes.model.BankEntity;
import com.example.swiftCodes.model.Branch;
import com.example.swiftCodes.repository.BankEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
public class BankServiceIntegrationTest {
    @Autowired
    private BankService bankService;

    @Autowired
    private BankEntityRepository bankEntityRepository;

    private BankEntity bankTest;
    private Branch branchTest;

    @BeforeEach
    void setUp() {
        bankTest = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        branchTest = new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false);

        bankEntityRepository.deleteAll();
    }

    @Test
    void getAllBanks_shouldReturnAllBanks() {
        bankService.saveBank(bankTest);

        List<BankEntity> banks = bankService.findAll();

        assertEquals(1, banks.size());
        assertTrue(banks.contains(bankTest));
    }

    @Test
    void getBankBySwiftCode_shouldReturnBank() {
        bankService.saveBank(bankTest);

        BankEntity bank = bankService.findBySwiftCode("BANKTESTXXX");
        assertNotNull(bank);
        assertEquals(bankTest, bank);
    }

    @Test
    void getBranchBySwiftCode_shouldReturnBranch() {
        bankTest.getBranches().add(branchTest);
        bankService.saveBank(bankTest);

        Branch branch = bankService.findBranchBySwiftCode("BANKTEST123");
        assertNotNull(branch);
        assertEquals(branchTest, branch);
    }

    @Test
    void getBankBySwiftCode_shouldThrowExceptionWhenBankNotFound() {
        assertThrows(BankNotFoundException.class, () -> bankService.findBySwiftCode("BANKTESTXXX"));
    }

    @Test
    void getBranchBySwiftCode_shouldThrowExceptionWhenBranchNotFound() {
        assertThrows(BankNotFoundException.class, () -> bankService.findBranchBySwiftCode("BANKTEST123"));
    }

    @Test
    void saveBank_shouldSaveBank() {
        BankEntity bank = bankService.saveBank(bankTest);
        assertNotNull(bank);
        assertEquals(bankTest, bank);
    }

    @Test
    void saveBank_shouldThrowExceptionWhenBankAlreadyExists() {
        bankService.saveBank(bankTest);
        assertThrows(BankAlreadyExistsException.class, () -> bankService.saveBank(bankTest));
    }

    @Test
    void saveBranch_shouldSaveBranch() {
        bankService.saveBank(bankTest);
        Branch branch = bankService.saveBranch(branchTest);
        assertNotNull(branch);
        assertEquals(branchTest, branch);
    }

    @Test
    void saveBranch_shouldThrowExceptionWhenBranchAlreadyExists() {
        bankTest.getBranches().add(branchTest);
        bankService.saveBank(bankTest);
        assertThrows(BankAlreadyExistsException.class, () -> bankService.saveBranch(branchTest));
    }

    @Test
    void deleteBySwiftCode_shouldDeleteBank() {
        bankService.saveBank(bankTest);
        bankService.deleteBySwiftCode("BANKTESTXXX");
        assertThrows(BankNotFoundException.class, () -> bankService.findBySwiftCode("BANKTESTXXX"));
    }

    @Test
    void deleteBySwiftCode_shouldDeleteBranch() {
        bankTest.getBranches().add(branchTest);
        bankService.saveBank(bankTest);
        assertDoesNotThrow(() -> bankService.findBranchBySwiftCode("BANKTEST123"));
        bankService.deleteBySwiftCode("BANKTEST123");
        assertThrows(BankNotFoundException.class, () -> bankService.findBranchBySwiftCode("BANKTEST123"));
    }

    @Test
    void deleteBySwiftCode_shouldThrowExceptionWhenBankNotFound() {
        assertThrows(BankNotFoundException.class, () -> bankService.deleteBySwiftCode("BANKTESTXXX"));
    }

    @Test
    void deleteBySwiftCode_shouldThrowExceptionWhenBranchNotFound() {
        bankService.saveBank(bankTest);
        assertThrows(BankNotFoundException.class, () -> bankService.deleteBySwiftCode("BANKTEST123"));
    }
}
