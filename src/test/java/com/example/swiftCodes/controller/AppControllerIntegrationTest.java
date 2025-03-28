package com.example.swiftCodes.controller;

import com.example.swiftCodes.model.BankEntity;
import com.example.swiftCodes.model.Branch;
import com.example.swiftCodes.service.BankService;
import com.example.swiftCodes.repository.BankEntityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AppControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BankService bankService;

    @Autowired
    private BankEntityRepository bankEntityRepository;

    BankEntity bankTest, bankDifferentCountryTest;
    Branch branchTest;
    List<BankEntity> bankTestList;

    @BeforeEach
    void setUp() {
        bankTest = new BankEntity("BANKTEST", "BANKTESTXXX", "TESTSTREET", "PL", "Poland", true);
        branchTest = new Branch("BRANCHTEST", "BANKTEST123", "TESTSTREET1", "PL", "Poland", false);
        bankDifferentCountryTest = new BankEntity("BANKTEST2", "BANKTST1XXX", "TESTSTREET2", "DE", "Germany", true);
        bankTestList = List.of(bankTest, bankDifferentCountryTest);
        bankEntityRepository.deleteAll();
    }

    @Test
    void getAllBanks_shouldReturnAllBanks() throws Exception {
        bankService.saveBank(bankTest);
        bankService.saveBank(bankDifferentCountryTest);

        mockMvc.perform(get("/v1/swift-codes/banks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(bankTestList)));
    }

    @Test
    void getBankBySwiftCode_shouldReturnBank() throws Exception {
        bankService.saveBank(bankTest);

        mockMvc.perform(get("/v1/swift-codes/BANKTESTXXX"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(bankTest)));
    }

    @Test
    void getBankBySwiftCode_shouldReturnBranch() throws Exception {
        bankTest.getBranches().add(branchTest);
        bankService.saveBank(bankTest);

        mockMvc.perform(get("/v1/swift-codes/BANKTEST123"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(branchTest)));
    }

    @Test
    void getBankBySwiftCode_shouldThrowExceptionWhenBankNotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/BANKTESTXXX"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBranchBySwiftCode_shouldThrowExceptionWhenBranchNotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/BANKTEST123"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getBanksByCountry_shouldReturnBanksByCountry() throws Exception {
        bankService.saveBank(bankTest);
        bankService.saveBank(bankDifferentCountryTest);

        mockMvc.perform(get("/v1/swift-codes/country/PL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(bankTest))));
    }

    @Test
    void getBanksByCountry_shouldReturnNotFound() throws Exception {
        mockMvc.perform(get("/v1/swift-codes/country/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void addBank_shouldAddBank() throws Exception {
        mockMvc.perform(post("/v1/swift-codes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bankTest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Bank added successfully"));
    }

    @Test
    void addBank_shouldThrowExceptionWhenBankExists() throws Exception {
        bankService.saveBank(bankTest);

        mockMvc.perform(post("/v1/swift-codes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(bankTest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Bank " + bankTest.getSwiftCode() + " already exists"));
    }

    @Test
    void addBank_shouldAddBranch() throws Exception {
        bankService.saveBank(bankTest);

        mockMvc.perform(post("/v1/swift-codes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(branchTest)))
                .andExpect(status().isOk())
                .andExpect(content().string("Branch added successfully"));
    }

    @Test
    void addBank_shouldReturnNotFoundIfNoHeadquarter() throws Exception {
        mockMvc.perform(post("/v1/swift-codes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(branchTest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void addBank_shouldThrowExceptionIfBranchExists() throws Exception {
        bankTest.getBranches().add(branchTest);
        bankService.saveBank(bankTest);

        mockMvc.perform(post("/v1/swift-codes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(branchTest)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Branch already exists"));
    }

    @Test
    void deleteBank_shouldDeleteBank() throws Exception {
        bankService.saveBank(bankTest);

        mockMvc.perform(delete("/v1/swift-codes/BANKTESTXXX"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bank deleted successfully"));
    }

    @Test
    void deleteBank_shouldReturnNotFound() throws Exception {
        mockMvc.perform(delete("/v1/swift-codes/"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBank_shouldDeleteBranch() throws Exception {
        bankTest.getBranches().add(branchTest);
        bankService.saveBank(bankTest);

        mockMvc.perform(delete("/v1/swift-codes/BANKTEST123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Bank deleted successfully"));
    }

    @Test
    void deleteBank_shouldReturnNotFoundWhenHeadquarterNotFound() throws Exception {
        mockMvc.perform(delete("/v1/swift-codes/BANKTEST123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Bank with "+ bankTest.getSwiftCode()+ " not found"));
    }

    @Test
    void deleteBank_shouldReturnNotFoundWhenBranchNotFound() throws Exception {
        bankService.saveBank(bankTest);

        mockMvc.perform(delete("/v1/swift-codes/BANKTEST123"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Branch with BANKTEST123 not found in BANKTEST"));
    }
}
