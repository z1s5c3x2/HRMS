package hrms.controller;

import hrms.model.dto.LeaveRequestDto;
import hrms.service.LeaveRequest.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaveRequest")
public class LeaveRequestController {
    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping("")
    public boolean lrqWrite( LeaveRequestDto leaveRequestDto ){

        return leaveRequestService.lrqWrite( leaveRequestDto );
    }

    // 2.
    @GetMapping("")
    public List<LeaveRequestDto> lrqGetAll(){

        return leaveRequestService.lrqGetAll();
    }

    // 3.
    @PutMapping("")
    public boolean lrqUpdate( LeaveRequestDto leaveRequestDto ){
        return leaveRequestService.lrqUpdate( leaveRequestDto );
    }
    // 4
    @DeleteMapping("")
    public boolean lrqDelete( @RequestParam int lrq_no){
        return leaveRequestService.lrqDelete( lrq_no );
    }

}
