package hrms.service.employee;

import hrms.model.dto.EmployeeDto;
import hrms.model.dto.RetiredEmployeeDto;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.RetiredEmployeeEntity;
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

    @Transactional
    public boolean registerEmp(@RequestBody EmployeeDto employeeDto)
    {
        //System.out.println("employeeDto = " + employeeDto);
        employeeDto.setEmpNo(generateEmpNumber(employeeDto.getEmpSex()));

        return employeeRepository.save(employeeDto.saveToEntity()).getEmpNo() > 0;
    }

    @Transactional
    public int generateEmpNumber(String _sex)
    {
        LocalDate now = LocalDate.now();
        String _str = String.valueOf(now.getYear()).substring(1); //년
        _str += String.valueOf(now.getMonthValue());   //월
        _str += _sex.equals("man") ? "1" : "2"; // 성별
        _str += String.format("%04d",employeeRepository.countNowEmployee(String.valueOf(now.getYear()))) ;
        //System.out.println("_str = " + _str);
        return Integer.parseInt(_str);
    }

    @Transactional
    public List<EmployeeDto> getEmpList()
    {
        List<EmployeeDto> result = new ArrayList<>();
        employeeRepository.findAll().forEach(e ->{
            result.add(e.allToDto());
        });
        return result;
    }

    @Transactional
    public boolean leaveEmpStatus(RetiredEmployeeDto retiredEmployeeDto)
    {
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findById(retiredEmployeeDto.getEmpNo());
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
    public void setRetiredEmployee(RetiredEmployeeDto retiredEmployeeDto)
    {
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findById(retiredEmployeeDto.getEmpNo());
        if(optionalEmployeeEntity.isPresent())
        {
            RetiredEmployeeEntity retiredEmployeeEntity = retiredEmployeeDto.saveToEntity();
            retiredEmployeeEntity.setEmpNo(optionalEmployeeEntity.get());
            retiredEmployeeEntityRepository.save(retiredEmployeeEntity);

            optionalEmployeeEntity.get().getRetiredEmployeeEntities().add(retiredEmployeeEntity);
            System.out.println("optionalEmployeeEntity.to = " + optionalEmployeeEntity.get().toString());
            System.out.println("retiredEmployeeEntity = " + retiredEmployeeEntity.toString());
        }



    }
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
