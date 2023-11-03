package hrms.service.employee;

import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.dto.RetiredEmployeeDto;
import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.DepartmentEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.RetiredEmployeeEntity;
import hrms.model.repository.ApprovalEntityRepository;
import hrms.model.repository.DepartmentEntityRepository;
import hrms.model.repository.EmployeeEntityRepository;
import hrms.model.repository.RetiredEmployeeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    EmployeeEntityRepository employeeRepository;
    @Autowired
    RetiredEmployeeEntityRepository retiredEmployeeEntityRepository;
    @Autowired
    DepartmentEntityRepository departmentEntityRepository;
    @Autowired
    ApprovalEntityRepository approvalEntityRepository;
    private final int LEAVE_COUNT = 5;

    // 사원 등록
    @Transactional
    public boolean registerEmp(ApprovalRequestDto<EmployeeDto> employeeDtoApprovalRequestDto)
    {
        EmployeeDto employeeDto = employeeDtoApprovalRequestDto.getData();
        System.out.println("employeeDto = " + employeeDto.toString());
        employeeDto.setEmpNo(generateEmpNumber(employeeDto.getEmpSex())); // pk
        EmployeeEntity employeeEntity = employeeDto.saveToEntity();

        Optional<DepartmentEntity> optionalDepartmentEntity =  departmentEntityRepository.findById(employeeDto.getDtpmNo());

        // 결제 메소드 추가


        if(optionalDepartmentEntity.isPresent())
        {
            employeeEntity.setDptmNo(optionalDepartmentEntity.get());
            employeeEntity.setAprvNo(new ApprovalEntity()); // 테스트
            employeeRepository.save(employeeEntity);

            //System.out.println("optionalDepartmentEntity = " + optionalDepartmentEntity);
            optionalDepartmentEntity.get().getEmployeeEntities().add(employeeEntity);

        }
        return employeeEntity.getEmpNo().length() > 0;
    }
    // 사원 pk 생성
    @Transactional
    public String generateEmpNumber(String _sex)
    {
        LocalDate now = LocalDate.now();
        String _str = String.valueOf(now.getYear()).substring(2); //년
        _str += String.valueOf(now.getMonthValue());   //월
        _str += _sex.equals("male") ? "1" : "2"; // 성별
        _str += String.valueOf(employeeRepository.countNowEmployee(String.valueOf(now.getYear()))) ;
        System.out.println("_str = " + _str);
        return _str;
    }

    //사원 조회 ( 페이징 처리 예정)
    @Transactional
    public List<EmployeeDto> getEmpList()
    {
        List<EmployeeDto> result = new ArrayList<>();
        employeeRepository.findAll().forEach(e ->{
            result.add(e.allToDto());
        });
        return result;
    }
    //사원 개별 조회
    @Transactional
    public EmployeeDto getOneEmp(String empNo)
    {
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(empNo);
        if(optionalEmployeeEntity.isPresent())
        {
            return optionalEmployeeEntity.get().allToDto();
        }
        return null;
    }
  /*  @Transactional
    public boolean leaveEmpStatus(RetiredEmployeeDto retiredEmployeeDto)
    {
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(retiredEmployeeDto.getEmpNo());
        System.out.println("retiredEmployeeDto = " + retiredEmployeeDto);
        System.out.println("optionalEmployeeEntity = " + optionalEmployeeEntity);
        if(optionalEmployeeEntity.isPresent())
        {
            EmployeeEntity employeeEntity = optionalEmployeeEntity.get();
            employeeEntity.setEmpSta(!employeeEntity.isEmpSta());

            setRetiredEmployee(retiredEmployeeDto);
            return true;
        }else{
            return false;
        }

    }
    @Transactional
    public void setRetiredEmployee(ApprovalRequestDto<RetiredEmployeeDto> approvalRequestDto)
    {
        RetiredEmployeeDto retiredEmployeeDto = approvalRequestDto.getData();
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(retiredEmployeeDto.getEmpNo());
        if(optionalEmployeeEntity.isPresent())
        {
            RetiredEmployeeEntity retiredEmployeeEntity = retiredEmployeeDto.saveToEntity();
            retiredEmployeeEntity.setEmpNo(optionalEmployeeEntity.get());
            retiredEmployeeEntityRepository.save(retiredEmployeeEntity);

            optionalEmployeeEntity.get().getRetiredEmployeeEntities().add(retiredEmployeeEntity);
            System.out.println("optionalEmployeeEntity.to = " + optionalEmployeeEntity.get());
            System.out.println("retiredEmployeeEntity = " + retiredEmployeeEntity);

        }



    }*/

    // 휴직 사원 조회
    @Transactional
    public List<EmployeeDto> getRestList()
    {
        List<EmployeeDto> result = new ArrayList<>();
        employeeRepository.findAllByEmpStaIsFalse().forEach(e ->{
            result.add(e.allToDto());
        });

        return result;

    }
}
