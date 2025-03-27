package com.example.swiftCodes.controller;

import com.example.swiftCodes.exception.BankAlreadyExistsException;
import com.example.swiftCodes.exception.BankNotFoundException;
import com.example.swiftCodes.model.BankEntity;
import com.example.swiftCodes.model.Branch;
import com.example.swiftCodes.service.BankService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AppControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BankService bankService;

    @Autowired
    ObjectMapper objectMapper;

    private BankEntity bankTest, bankDifferentCountryTest;
    private Branch branchTest;
    private List<BankEntity> bankTestList;

    @BeforeEach
    void setUp() {
        bankTest = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        branchTest = new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false);
        bankDifferentCountryTest = new BankEntity("BANKTEST2", "BANKTST1XXX", "TESTSTREET2", "DE", "Germany", true);
        bankTestList = List.of(bankTest, bankDifferentCountryTest);
    }

    @Test
    void getAllBanks_shouldReturnAllBanks() throws Exception {
        when(bankService.findAll()).thenReturn(bankTestList);

        mockMvc.perform(get("/v1/swift-codes/banks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(bankTestList)));

        verify(bankService, times(1)).findAll();
    }

    @Test
    void getBankBySwiftCode_shouldReturnBankBySwiftCode() throws Exception {
        when(bankService.findBySwiftCode("BANKTESTXXX")).thenReturn(bankTest);

        mockMvc.perform(get("/v1/swift-codes/BANKTESTXXX"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(bankTest)));

        verify(bankService).findBySwiftCode("BANKTESTXXX");
    }

    @Test
    void getBankBySwiftCode_shouldReturnBankNotFound() throws Exception {
        when(bankService.findBySwiftCode("BANKTESTXXX")).thenReturn(null);

        mockMvc.perform(get("/v1/swift-codes/BANKTESTXXX"))
                .andExpect(status().isNotFound());

        verify(bankService).findBySwiftCode("BANKTESTXXX");
    }

    @Test
    void getBankBySwiftCode_shouldReturnBranchBySwiftCode() throws Exception {
        when(bankService.findBranchBySwiftCode("BANKTEST123")).thenReturn(branchTest);

        mockMvc.perform(get("/v1/swift-codes/BANKTEST123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(branchTest)));

        verify(bankService).findBranchBySwiftCode("BANKTEST123");
    }

    @Test
    void getBankBySwiftCode_shouldReturnBranchNotFound() throws Exception {
        when(bankService.findBranchBySwiftCode("BANKTEST123")).thenReturn(null);

        mockMvc.perform(get("/v1/swift-codes/BANKTEST123"))
                .andExpect(status().isNotFound());

        verify(bankService).findBranchBySwiftCode("BANKTEST123");
    }

    @Test
    void getBanksByCountry_shouldReturnBanksByCountry() throws Exception {
        when(bankService.findByCountryISO2("PL")).thenReturn(List.of(bankTest));

        mockMvc.perform(get("/v1/swift-codes/country/PL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bankTest))));
    }

    @Test
    void getBanksByCountry_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addBank_shouldAddBank() throws Exception {
        when(bankService.saveBank(any(BankEntity.class))).thenReturn(bankTest);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankTest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Bank added successfully"));

        verify(bankService).saveBank(bankTest);
    }

    @Test
    void addBank_shouldAddBranch() throws Exception {
        when(bankService.saveBranch(any(Branch.class))).thenReturn(branchTest);

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(branchTest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Branch added successfully"));

        verify(bankService).saveBranch(branchTest);
    }

    @Test
    void addBank_shouldReturnConflict() throws Exception {
        when(bankService.saveBank(any(BankEntity.class)))
                .thenThrow(new BankAlreadyExistsException("Bank " + bankTest.getSwiftCode() +" exists"));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bankTest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Bank " + bankTest.getSwiftCode() +" exists"));

        verify(bankService).saveBank(bankTest);
    }

    @Test
    void addBank_shouldReturnConflictBranch() throws Exception {
        when(bankService.saveBranch(any(Branch.class)))
                .thenThrow(new BankAlreadyExistsException("Branch already exists"));

        mockMvc.perform(post("/v1/swift-codes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(branchTest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Branch already exists"));

        verify(bankService).saveBranch(branchTest);
    }

    @Test
    void deleteBank_shouldDeleteBank() throws Exception {
        doNothing().when(bankService).deleteBySwiftCode("BANKTESTXXX");

        mockMvc.perform(delete("/v1/swift-codes/BANKTESTXXX"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bank deleted successfully"));

        verify(bankService).deleteBySwiftCode("BANKTESTXXX");
    }

    @Test
    void deleteBank_shouldReturnNotFound() throws Exception {
        doThrow(new BankNotFoundException("Bank with BADSWIFTXXX not found")).when(bankService).deleteBySwiftCode("BADSWIFTXXX");

        mockMvc.perform(delete("/v1/swift-codes/BADSWIFTXXX"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bank with BADSWIFTXXX not found"));
    }
}
