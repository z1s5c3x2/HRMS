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

    @PostMapping("/post") // 급여 등록
    public boolean slryWrite(@RequestBody SalaryDto salaryDto ){
        return salaryService.slryWrite( salaryDto );
    }

    // 2.
    @GetMapping("/getAll") // ( 인사팀 전용 ) 전체 급여지급 목록내역 확인하기
    public List<SalaryDto> slryGetAll(){

        return salaryService.slryGetAll();
    }
    @GetMapping("/get") // ( 인사팀 전용 ) 전체 급여지급 목록에서 선택된 급여 상세보기
    public SalaryDto slryGet(int slryNo){

        return salaryService.slryGet(slryNo);
    }
    @GetMapping("/getMe") // SalaryMain 페이지에서 세션을 통한 본인 사번으로 본인정보 출력
    public List<SalaryDto> slryGetMeAll(String empNo){

        return salaryService.slryGetMeAll(empNo);
    }

    // 3.
    @PutMapping("/put") // 급여 수정
    public boolean slryUpdate(@RequestBody SalaryDto salaryDto ){
        return salaryService.slryUpdate( salaryDto );
    }
    // 4
    @DeleteMapping("/delete") // 급여 삭제
    public boolean slryDelete( @RequestParam int slry_no){
        return salaryService.slryDelete( slry_no );
    }


}
