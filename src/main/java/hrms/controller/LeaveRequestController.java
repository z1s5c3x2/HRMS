package hrms.controller;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.LeaveRequestDto;
import hrms.model.dto.PageDto;
import hrms.service.LeaveRequest.LeaveCalcService;
import hrms.service.LeaveRequest.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/leaveRequest")
public class LeaveRequestController {
    @Autowired
    private LeaveRequestService leaveRequestService;
    @Autowired
    private LeaveCalcService leaveCalcService;

    @PostMapping("/post") // 연차 신청
    public boolean lrqWrite(@RequestBody ApprovalRequestDto<LeaveRequestDto> approvalRequestDto) {
        return leaveRequestService.lrqWrite(approvalRequestDto);
    }

    // 2. 모든 LRQ 리스트 출력
    @GetMapping("/getAll")
    public PageDto lrqGetAll(@RequestParam int page,
                             @RequestParam String key,
                             @RequestParam String keyword,
                             @RequestParam int view,
                             @RequestParam int empRk,
                             @RequestParam int dptmNo,
                             @RequestParam int lrqType,
                             @RequestParam int lrqSrtype,
                             @RequestParam String DateSt,
                             @RequestParam String DateEnd


    ) {

        return leaveRequestService.lrqGetAll(page, key, keyword, view, empRk, dptmNo, lrqType, lrqSrtype, DateSt, DateEnd);
    }

    @GetMapping("/get") // 메인페이지전용 세션에서 들어온 본인 사번으로 본인정보 출력
    public PageDto lrqGet(@RequestParam int page,
                          @RequestParam int view,
                          @RequestParam String empNo,
                          @RequestParam int lrqType,
                          @RequestParam int lrqSrtype,
                          @RequestParam String DateSt,
                          @RequestParam String DateEnd
    ) {

        return leaveRequestService.lrqGet(page, view, empNo , lrqType ,lrqSrtype , DateSt, DateEnd);
    }

    // 3. 보유 연차 출력
    @GetMapping("/getLeave")
    public int lrqGetLeave(@RequestParam String empNo) {
        System.out.println("empNo" + empNo);
        return leaveCalcService.calcRestCount(empNo);
    }

    @GetMapping("/findone")
    public LeaveRequestDto findOneLrq(@RequestParam int lrqNo) {
        System.out.println("lrqNo = " + lrqNo);
        System.out.println("LeaveRequestController.findOneLrq");
        LeaveRequestDto leaveRequestDto = leaveRequestService.findOneLrq(lrqNo);
        System.out.println("leaveRequestDto = " + leaveRequestDto);
        return leaveRequestDto;
    }

    @PostMapping("/updateyearleave")
    public boolean updateYearLrq(@RequestBody ApprovalRequestDto<LeaveRequestDto> approvalRequestDto) {
        return leaveRequestService.updateYearLrq(approvalRequestDto);
    }

    // 3.
    @PutMapping("/put")
    public boolean lrqUpdate(LeaveRequestDto leaveRequestDto) {
        return leaveRequestService.lrqUpdate(leaveRequestDto);
    }

    // 4
    @DeleteMapping("/delete")
    public boolean lrqDelete(@RequestParam int lrqNo) {
        return leaveRequestService.lrqDelete(lrqNo);
    }


}
