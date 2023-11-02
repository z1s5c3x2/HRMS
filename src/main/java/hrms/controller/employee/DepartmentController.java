package hrms.controller.employee;


import hrms.model.dto.DepartmentDto;
import hrms.service.department.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;

    @PostMapping("/chageemp")
    public boolean chagneEmp(@RequestBody DepartmentDto departmentDto)
    {
        return false;
    }

    @PostMapping("/insertdpm")
    public boolean insertDpm(@RequestBody DepartmentDto departmentDto)
    {
        System.out.println("departmentDto = " + departmentDto.toString());
        return departmentService.insertDpm(departmentDto);
    }
}
