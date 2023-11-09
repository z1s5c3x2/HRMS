package hrms.controller.employee;


import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.DepartmentDto;
import hrms.service.department.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired DepartmentService departmentService;

    @PutMapping("/chageemp")
    public boolean chagneEmp(@RequestBody ApprovalRequestDto<DepartmentDto> approvalRequestDto)
    {
        System.out.println("approvalRequestDto = " + approvalRequestDto);
        return false;
    }

    @PostMapping("/insertdpm")
    public boolean insertDpm(@RequestBody DepartmentDto departmentDto)
    {
        System.out.println("departmentDto = " + departmentDto.toString());
        return departmentService.insertDpm(departmentDto);
    }

    @GetMapping("/findAll")
    public List<DepartmentDto> findAllDptm() {
        return departmentService.findAllDptm();
    }
}
