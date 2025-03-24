package com.example.swiftCodes.service;

import com.example.swiftCodes.csv.CsvParser;
import com.example.swiftCodes.exception.BankAlreadyExistsException;
import com.example.swiftCodes.exception.BankNotFoundException;
import com.example.swiftCodes.model.BankEntity;
import com.example.swiftCodes.model.Branch;
import com.example.swiftCodes.repository.BankEntityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class BankServiceTest {
    @InjectMocks
    private BankService bankService;

    @Mock
    private BankEntityRepository bankEntityRepository;

    @BeforeEach
    public void setUp() {
        reset(bankEntityRepository);
        bankService = new BankService(bankEntityRepository, null);

        lenient().when(bankEntityRepository.findAll()).thenReturn(new ArrayList<>());
        lenient().when(bankEntityRepository.saveAll(List.of())).thenReturn(List.of());
    }

    @Test
    public void shouldReturnBankBySwiftCode() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.of(bank));
        BankEntity result = bankService.findBySwiftCode("BANKTESTXXX");

        assertNotNull(result);
        assertEquals(bank, result);
    }

    @Test
    public void shouldThrowExceptionWhenBankNotFound() {
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.empty());
        assertThrows(BankNotFoundException.class, () -> bankService.findBySwiftCode("BANKTESTXXX"));
    }

    @Test
    public void shouldReturnBranchBySwiftCode() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        bank.getBranches().add(new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false));
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.of(bank));
        Branch result = bankService.findBySwiftCode("BANKTESTXXX").getBranches().get(0);

        assertNotNull(result);
        assertEquals(bank.getBranches().get(0), result);
    }

    @Test
    public void shouldThrowExceptionWhenBranchNotFound() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        bank.getBranches().add(new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false));
        when(bankEntityRepository.findBySwiftCode("BANKTEST124")).thenReturn(Optional.empty());

        assertThrows(BankNotFoundException.class, () -> bankService.findBranchBySwiftCode("BANKTEST124"));
    }

    @Test
    public void saveBank() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        when(bankEntityRepository.save(bank)).thenReturn(bank);
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.empty());

        BankEntity result = bankService.save(bank);

        assertNotNull(result);
        assertEquals(bank, result);
    }

    @Test
    public void shouldSaveBank() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.of(bank));

        assertThrows(BankAlreadyExistsException.class, () -> bankService.save(bank));
    }

    @Test
    public void shouldThrowExceptionWhenBankAlreadyExists() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        Branch newBranch = new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false);

        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.of(bank));

        when(bankEntityRepository.save(any(BankEntity.class))).thenAnswer(invocation -> {
            BankEntity savedBank = invocation.getArgument(0);
            return savedBank;
        });

        Branch result = bankService.saveBranch(newBranch);

        assertNotNull(result);
        assertEquals(newBranch, result);
    }

    @Test
    public void saveBranchThrowsException() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        Branch branch = new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false);

        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.of(bank));

        bank.getBranches().add(branch);

        assertThrows(BankAlreadyExistsException.class, () -> bankService.saveBranch(branch));
    }

    @Test
    public void shouldDeleteBankBySwiftCode() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.of(bank));

        bankService.deleteBySwiftCode("BANKTESTXXX");

        verify(bankEntityRepository, times(1)).deleteBySwiftCode("BANKTESTXXX");
    }

    @Test
    public void shouldThrowExceptionWhenBankToDeleteNotFound() {
        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.empty());

        assertThrows(BankNotFoundException.class, () -> bankService.deleteBySwiftCode("BANKTESTXXX"));
    }

    @Test
    public void shouldDeleteBranchBySwiftCode() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        Branch branch = new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false);
        bank.getBranches().add(branch);

        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.of(bank));
        when(bankEntityRepository.save(any(BankEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        bankService.deleteBySwiftCode("BANKTEST123");

        assertTrue(bank.getBranches().isEmpty());
    }

    @Test
    public void shouldThrowExceptionWhenBranchToDeleteNotFound() {
        BankEntity bank = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        Branch branch = new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false);
        bank.getBranches().add(branch);

        when(bankEntityRepository.findBySwiftCode("BANKTESTXXX")).thenReturn(Optional.of(bank));

        assertThrows(BankNotFoundException.class, () -> bankService.deleteBySwiftCode("BANKTEST124"));
    }

    @Test
    public void shouldParseCsv() {
        CsvParser csvParser = mock(CsvParser.class);
        bankService = new BankService(bankEntityRepository, csvParser);

        String filePath = "src/main/java/com/example/swiftCodes/csv/test.csv";
        List<BankEntity> banks = List.of(
                new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true),
                new BankEntity("BANKTEST1", "BANKTST1XXX", "TESTSTREET1", "PL", "Poland", true)
        );

        when(csvParser.parseCsvFile(filePath)).thenReturn(banks);

        bankService.importFromCsv(filePath);

        verify(csvParser).parseCsvFile(filePath);
        verify(bankEntityRepository).saveAll(banks);
    }

    @Test
    public void shouldThrowExceptionWhenFilePathIsNull() {
        String filePath = "nonexistent.csv";
        assertThrows(NullPointerException.class, () -> bankService.importFromCsv(filePath));
    }
}
