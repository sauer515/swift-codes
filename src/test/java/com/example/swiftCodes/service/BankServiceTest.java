package com.example.swiftCodes.service;

import com.example.swiftCodes.model.BankEntity;
import com.example.swiftCodes.model.Branch;
import com.example.swiftCodes.repository.BankEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {
    @InjectMocks
    private BankService bankService;

    @Mock
    private BankEntityRepository bankEntityRepository;

    @Test
    public void shouldReturnBankBySwiftCode() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(bank);
        BankEntity result = bankService.findBySwiftCode("BANKTESTXXX");

        assertNotNull(result);
        assertEquals(bank, result);
    }

    @Test
    public void shouldReturnNullWhenBankNotFound() {
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(null);
        BankEntity result = bankService.findBySwiftCode("BANKTESTXXX");

        assertNull(result);
    }

    @Test
    public void shouldReturnBranchBySwiftCode() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        bank.getBranches().add(new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false));
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(bank);
        Branch result = bankService.findBranchBySwiftCode("BANKTEST123");

        assertNotNull(result);
        assertEquals(bank.getBranches().get(0), result);
    }

    @Test
    public void shouldReturnNullWhenBranchNotFound() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        bank.getBranches().add(new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false));
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(bank);
        Branch result = bankService.findBranchBySwiftCode("BANKTEST124");

        assertNull(result);
    }
}
