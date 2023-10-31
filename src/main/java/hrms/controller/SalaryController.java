package hrms.controller;



import hrms.model.dto.SalaryDto;
import hrms.service.Salary.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salary")
public class SalaryController {
    @Autowired
    private SalaryService salaryService;

    @PostMapping("")
    public boolean slryWrite(@RequestBody SalaryDto salaryDto ){

        return salaryService.slryWrite( salaryDto );
    }

    // 2.
    @GetMapping("")
    public List<SalaryDto> slryGetAll(){

        return salaryService.slryGetAll();
    }

    // 3.
    @PutMapping("")
    public boolean slryUpdate(@RequestBody SalaryDto salaryDto ){
        return salaryService.slryUpdate( salaryDto );
    }
    // 4
    @DeleteMapping("")
    public boolean slryDelete( @RequestParam int slry_no){
        return salaryService.slryDelete( slry_no );
    }


}
