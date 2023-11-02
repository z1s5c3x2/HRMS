package hrms.controller;

import hrms.model.dto.LeaveRequestDto;
import hrms.service.LeaveRequest.LeaveRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leaveRequest")
@CrossOrigin("http://localhost:3000")
public class LeaveRequestController {
    @Autowired
    private LeaveRequestService leaveRequestService;

    @PostMapping("/post")
    public boolean lrqWrite( LeaveRequestDto leaveRequestDto ){

        return leaveRequestService.lrqWrite( leaveRequestDto );
    }

    // 2. 모든 LRQ 리스트 출력
    @GetMapping("/getAll")
    public List<LeaveRequestDto> lrqGetAll(){

        return leaveRequestService.lrqGetAll();
    }
    // 개별 LRQ 출력
    @GetMapping("/get")
    public LeaveRequestDto lrqGet( String empNo){

        return leaveRequestService.lrqGet( empNo );
    }

    // 3.
    @PutMapping("/put")
    public boolean lrqUpdate( LeaveRequestDto leaveRequestDto ){
        return leaveRequestService.lrqUpdate( leaveRequestDto );
    }
    // 4
    @DeleteMapping("/delete")
    public boolean lrqDelete( @RequestParam int lrqNo){
        return leaveRequestService.lrqDelete( lrqNo );
    }


}
