package com.example.swiftCodes.csv;

import com.example.swiftCodes.model.BankEntity;
import com.example.swiftCodes.model.Branch;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CsvParser {
    public List<BankEntity> parseCsvFile(String fileName) {
        List<BankEntity> headquarters = new ArrayList<>();

        CsvMapper csvMapper = new CsvMapper();
        CsvSchema schema = CsvSchema.emptySchema().withHeader().withColumnSeparator(',');

        try {
            File csvFile = new File(fileName);

            if (!csvFile.exists()) {
                throw new IllegalArgumentException("File .csv not found");
            }

            MappingIterator<Map<String, String>> mi = csvMapper.readerFor(Map.class)
                                                                .with(schema)
                                                                .readValues(csvFile);
            Map<String, BankEntity> headquarterMap = new HashMap<>();

            while (mi.hasNext()) {
                Map<String, String> row = mi.next();

                String bankName = row.get("NAME");
                String swiftCode = row.get("SWIFT CODE");
                String address = row.get("ADDRESS");
                String countryISO2 = row.get("COUNTRY ISO2 CODE");
                String countryName = row.get("COUNTRY NAME");

                boolean isHeadquarter = swiftCode.contains("XXX");

                if (isHeadquarter) {
                    BankEntity headquarterBank = new BankEntity();
                    headquarterBank.setBankName(bankName);
                    headquarterBank.setSwiftCode(swiftCode);
                    headquarterBank.setAddress(address);
                    headquarterBank.setCountryISO2(countryISO2);
                    headquarterBank.setCountryName(countryName);
                    headquarterBank.setHeadquarter(true);

                    headquarters.add(headquarterBank);
                    headquarterMap.put(swiftCode, headquarterBank);
                }
                else {
                    Branch branch = new Branch();
                    branch.setBankName(bankName);
                    branch.setSwiftCode(swiftCode);
                    branch.setAddress(address);
                    branch.setCountryISO2(countryISO2);
                    branch.setCountryName(countryName);
                    branch.setHeadquarter(false);

                    String headquarterSwiftCode = swiftCode.substring(0, 8) + "XXX";
                    if (headquarterMap.containsKey(headquarterSwiftCode)) {
                        headquarterMap.get(headquarterSwiftCode).getBranches().add(branch);
                    }
                }
            }
            return headquarters;
        }
        catch (IOException e) {
            throw new IllegalArgumentException("File .csv not found");
        }
    }
}
