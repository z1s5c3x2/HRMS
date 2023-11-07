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

    @PostMapping("/post")
    public boolean slryWrite(@RequestBody SalaryDto salaryDto ){
        // 입력받은 값이 담긴 Dto를 -> 결재 서비스로 보내서 결제 엔티티를 먼저 생성한다.
        // 그 후 해당 생성된 엔티티로부터 결재PK를 리턴받아서
        // 입력받아서 가져온 Dto에 .set 으로 결재번호 삽입 후 급여 엔티티 생성

        return salaryService.slryWrite( salaryDto );
    }

    // 2.
    @GetMapping("/getAll") // 인사팀전용 전체 급여지급 목록 확인하기
    public List<SalaryDto> slryGetAll(){

        return salaryService.slryGetAll();
    }
    @GetMapping("/get") // 인사팀전용 전체 급여지급 목록에서 클릭된 특정 급여 상세보기 정보
    public SalaryDto slryGet(int slryNo){

        return salaryService.slryGet(slryNo);
    }
    @GetMapping("/getMe") // 메인페이지전용 세션에서 들어온 본인 사번으로 본인정보 출력
    public List<SalaryDto> slryGetMeAll(String empNo){

        return salaryService.slryGetMeAll(empNo);
    }

    // 3.
    @PutMapping("/put")
    public boolean slryUpdate(@RequestBody SalaryDto salaryDto ){
        return salaryService.slryUpdate( salaryDto );
    }
    // 4
    @DeleteMapping("/delete")
    public boolean slryDelete( @RequestParam int slry_no){
        return salaryService.slryDelete( slry_no );
    }


}
