package hrms.controller;


import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.PageDto;
import hrms.model.dto.SalaryDto;
import hrms.service.Salary.SalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/salary")

public class SalaryController {
    @Autowired
    private SalaryService salaryService;

    @PostMapping("/post") // 급여 등록
    public boolean slryWrite(@RequestBody ApprovalRequestDto<SalaryDto> approvalRequestDto) {


        return salaryService.slryWrite(approvalRequestDto);
    }

    // 2.
    @GetMapping("/getAll") // ( 인사팀 전용 ) 전체 급여지급 목록내역 확인하기
    public PageDto slryGetAll(@RequestParam int page,
                              @RequestParam String key,
                              @RequestParam String keyword,
                              @RequestParam int view,
                              @RequestParam int empRk,
                              @RequestParam int dptmNo,
                              @RequestParam int slryType,
                              @RequestParam String DateSt,
                              @RequestParam String DateEnd
    ) {

        return salaryService.slryGetAll(page, key, keyword, view, empRk, dptmNo, slryType, DateSt, DateEnd);
    }

    @GetMapping("/get") // ( 인사팀 전용 ) 전체 급여지급 목록에서 선택된 급여 상세보기
    public SalaryDto slryGet(int slryNo) {

        return salaryService.slryGet(slryNo);
    }

    @GetMapping("/getMe") // SalaryMain 페이지에서 세션을 통한 본인 사번으로 본인정보 출력
    public PageDto slryGetMeAll(@RequestParam int page,
                                @RequestParam int view,
                                @RequestParam String empNo,
                                @RequestParam int slryType,
                                @RequestParam String DateSt,
                                @RequestParam String DateEnd

    ) {

        return salaryService.slryGetMeAll(page, view, empNo, slryType, DateSt, DateEnd);
    }

    // 3.
    @PutMapping("/put") // 급여 수정
    public boolean slryUpdate(@RequestBody SalaryDto salaryDto) {
        return salaryService.slryUpdate(salaryDto);
    }

    // 4
    @DeleteMapping("/delete") // 급여 삭제
    public boolean slryDelete(@RequestParam int slry_no) {
        return salaryService.slryDelete(slry_no);
    }


}
