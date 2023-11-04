package hrms.service.employee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    /* 환희 ====================================================================== */
    // 결재 후 사원 기본정보(전화번호/비밀번호/계좌번호) / 부서 / 직급 변경
    @Transactional
    public boolean setEmployeeInfo( int aprvNo ) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalEntityRepository.findById( aprvNo );
        if( !optionalApprovalEntity.isPresent() ) return false;

        // JSON문자열 => DTO객체로 변환
        EmployeeDto employeeDto
                = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), EmployeeDto.class);
        // DTO => Entity 객체로 변환
        EmployeeEntity employeeEntity = employeeDto.saveToEntity();

        // 수정 전 해당 결재수정안에 대한 data 호출
        if( optionalApprovalEntity.isPresent() ) {

            // 변경 대상사원 객체 호출
            Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo( employeeEntity.getEmpNo() );

            if( optionalEmployeeEntity.isPresent() ){

                // 결재 타입에 따른 DB변경 실행
                switch ( optionalApprovalEntity.get().getAprvType() ) {

                    // 사원 기본정보 변경(전화번호/비밀번호/계좌번호)
                    case 2 :
                        optionalEmployeeEntity.get().setEmpPhone(employeeEntity.getEmpPhone());
                        optionalEmployeeEntity.get().setEmpPwd(employeeEntity.getEmpPwd());
                        optionalEmployeeEntity.get().setEmpAcn(employeeEntity.getEmpAcn());
                        break;

                    // 사원 부서변경
                    case 4 :

                        Optional<DepartmentEntity> optionalDepartmentEntity = departmentEntityRepository.findById( employeeDto.getDtpmNo() );
                        if( !optionalDepartmentEntity.isPresent() ) return false;

                        optionalEmployeeEntity.get().setDptmNo(optionalDepartmentEntity.get());
                        employeeRepository.save(optionalEmployeeEntity.get());

                        break;

                    // 사원 직급변경
                    case 5 :
                        optionalEmployeeEntity.get().setEmpRk(employeeEntity.getEmpRk());
                        break;

                }
                return true;
            }
            return false;
        }
        return false;
    }










}
