package hrms.service.department;

import hrms.model.dto.DepartmentDto;
import hrms.model.repository.DepartmentEntityRepository;
import hrms.model.repository.DepartmentHistoryEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;

@Service
public class DepartmentService {
    @Autowired
    DepartmentEntityRepository departmentEntityRepository;


    @Transactional
    public boolean insertDpm(DepartmentDto departmentDto)
    {
        return departmentEntityRepository.save(departmentDto.saveToAll()).getDptmNo()>=0 ;
    }

}
