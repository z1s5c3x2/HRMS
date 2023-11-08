package hrms.controller.employee;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.DepartmentDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.dto.RetiredEmployeeDto;
import hrms.model.entity.EmployeeEntity;
import hrms.service.LeaveRequest.LeaveCalcService;
import hrms.service.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    LeaveCalcService leaveCalcService;

    @PostMapping("/register") //사원 등록 결제 정보 받아오기
    public boolean registerEmp(@RequestBody ApprovalRequestDto<EmployeeDto> employeeDtoApprovalRequestDto)
    {
//        System.out.println("employeeDto = " + employeeDtoApprovalRequestDto);
         return employeeService.registerEmp(employeeDtoApprovalRequestDto);
    }

    @PutMapping("/leave")
    public boolean leaveEmpStatus(@RequestBody ApprovalRequestDto<RetiredEmployeeDto> retiredEmployeeDtoApprovalRequestDto)
    {
        //System.out.println("EmployeeController.setEmpStatus");
        //return employeeService.leaveEmpStatus(retiredEmployeeDtoApprovalRequestDto);
        return false;
    }

    @GetMapping("getaprvlist") // 결제 받을 인사팀 가져오기
    public List<EmployeeDto> getAprvList()
    {
        return employeeService.getAprvList();
    }
    @GetMapping("/restlist") // 휴직 사원 불러오기
    public List<EmployeeDto> getRestList()
    {
        return employeeService.getRestList();
    }
    @GetMapping("/list") //사원 전체 조회
    public List<EmployeeDto> getEmpList()
    {
        //System.out.println("EmployeeController.getEmpList");
        return employeeService.getEmpList();
    }
    @PutMapping("/changernk") // 사원 직급 변경
    public boolean changeRank(@RequestBody ApprovalRequestDto<EmployeeDto> employeeDtoApprovalRequestDto)
    {
        System.out.println("employeeDtoApprovalRequestDto = " + employeeDtoApprovalRequestDto);
        return false;
    }
    @PutMapping("/changeinfo") //  사원 정보 변경
    public boolean changeInfo(@RequestBody ApprovalRequestDto<EmployeeDto> employeeDtoApprovalRequestDto)
    {
        System.out.println("employeeDtoApprovalRequestDto = " + employeeDtoApprovalRequestDto);
        return false;
    }
    @GetMapping("/findemp") // 개별 사원 조회
    public EmployeeDto findByEmployee(@RequestParam String empNo)
    {
        return employeeService.getOneEmp(empNo);
    }


}
