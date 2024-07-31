package Iteration.Excel.Service;

import Iteration.Excel.Entity.person;
import Iteration.Excel.Repo.personRepo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


@Service
public class personService {

    @Autowired
    private personRepo pr;

    public void savePersonData(MultipartFile file) throws IOException, IOException {
        try (InputStream inputStream = file.getInputStream(); Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row
                try {
                    String name = row.getCell(0).getStringCellValue();
                    int age = (int) row.getCell(1).getNumericCellValue();

                    LocalDate dob = LocalDate.parse(row.getCell(2).getStringCellValue(), formatter);

                    // Check if the record already exists
                    if (!pr.existsByNameAndAgeAndDob(name, age, dob)) {

                    person per = new person();
                    per.setName(name);
                    per.setAge(age);
                    per.setDob(dob);

                    // Save each person individually
                    pr.save(per);
                    } else {
                        System.out.println("Duplicate record found: " + name + ", " + age + ", " + dob);
                    }
                } catch (Exception e) {
                    // Handle the exception for the current row and continue with the next
                    System.err.println("Failed to process row " + row.getRowNum() + ": " + e.getMessage());
                }
            }
        }
    }
}
