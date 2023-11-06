package hrms.controller.employee;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.DepartmentDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.dto.RetiredEmployeeDto;
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
        //System.out.println("employeeDto = " + employeeDto);
        return employeeService.registerEmp(employeeDtoApprovalRequestDto);
    }

    @PutMapping("/leave")
    public boolean leaveEmpStatus(@RequestBody ApprovalRequestDto<RetiredEmployeeDto> retiredEmployeeDtoApprovalRequestDto)
    {
        //System.out.println("EmployeeController.setEmpStatus");
        //return employeeService.leaveEmpStatus(retiredEmployeeDtoApprovalRequestDto);
        return false;
    }


    @GetMapping("/restlist")
    public List<EmployeeDto> getRestList()
    {
        return employeeService.getRestList();
    }
    @GetMapping("/list")
    public List<EmployeeDto> getEmpList()
    {
        //System.out.println("EmployeeController.getEmpList");
        return employeeService.getEmpList();
    }
    @GetMapping("/restcount") // 테스트용
    public int getRestCount(@RequestParam String empNo)
    {
        return leaveCalcService.calcRestCount(empNo);
    }
    @PutMapping("/changernk")
    public boolean changeRank(@RequestBody ApprovalRequestDto<EmployeeDto> employeeDtoApprovalRequestDto)
    {
        System.out.println("employeeDtoApprovalRequestDto = " + employeeDtoApprovalRequestDto);
        return false;
    }
    @PutMapping("/changeinfo")
    public boolean changeInfo(@RequestBody ApprovalRequestDto<EmployeeDto> employeeDtoApprovalRequestDto)
    {
        System.out.println("employeeDtoApprovalRequestDto = " + employeeDtoApprovalRequestDto);
        return false;
    }
    @GetMapping("/findemp")
    public EmployeeDto findByEmployee(@RequestParam String empNo)
    {
        return employeeService.getOneEmp(empNo);
    }


}
