package hrms.service.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.DepartmentHistoryDto;
import hrms.model.dto.EmployeeDto;
import hrms.model.dto.PageDto;
import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.DepartmentEntity;
import hrms.model.entity.DepartmentHistoryEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.repository.*;
import hrms.service.approval.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    DepartmentHistoryEntityRepository departmentHistoryEntityRepository;
    @Autowired
    ApprovalService approvalService;
    private final int LEAVE_COUNT = 5;
    // 사원 등록
    @Transactional
    public boolean registerEmp(ApprovalRequestDto<EmployeeDto> employeeDtoApprovalRequestDto)
    {
        EmployeeDto employeeDto = employeeDtoApprovalRequestDto.getData(); // 결제정보를 포함한 dto에서 사원 데이터 추출
        System.out.println("employeeDto = " + employeeDto.toString());
        employeeDto.setEmpNo(generateEmpNumber(employeeDto.getEmpSex())); // pk생성
        //EmployeeEntity employeeEntity = employeeDto.saveToEntity(); // 사원 dto entity로 변경

        // 결제 등록 후 entity반환
        ApprovalEntity approvalEntity = approvalService.postApproval(
                employeeDtoApprovalRequestDto.getAprvType()
                , employeeDtoApprovalRequestDto.getAprvCont()
                ,employeeDtoApprovalRequestDto.getApprovers());
        System.out.println(approvalEntity.toString());
        // 입력한 부서의 fk 호출
        Optional<DepartmentEntity> optionalDepartmentEntity =  departmentEntityRepository.findById(employeeDto.getDtpmNo());
        System.out.println("optionalDepartmentEntity = " + optionalDepartmentEntity);
        // 결제 메소드 추가

        // 부서 유효성 검사 및 fk 매핑
        if(optionalDepartmentEntity.isPresent())
        {
            //사원 저장
            EmployeeEntity employeeEntity = employeeRepository.save(employeeDto.saveToEntity());
            employeeEntity.setDptmNo(optionalDepartmentEntity.get()); // 사원 부서 fk
            optionalDepartmentEntity.get().getEmployeeEntities().add(employeeEntity); //부서 pk
            employeeEntity.getApprovalEntities().add(approvalEntity); // 결제 pk

            System.out.println("optionalDepartmentEntity = " + optionalDepartmentEntity.get().getEmployeeEntities());
            System.out.println("employeeEntity.getApprovalEntities() = " + employeeEntity.getApprovalEntities());

            return !employeeEntity.getEmpNo().isEmpty();

        }
        return false;
    }
    // 사원 pk 생성
    @Transactional
    public String generateEmpNumber(String _sex)
    {
        // 사원 pk 생성 현재 날짜를 기준으로    YY-MM-성별-해당 현재 연도에 입사한 사원수
        LocalDate now = LocalDate.now();
        String _str = String.valueOf(now.getYear()).substring(2); //년
        _str += String.valueOf(now.getMonthValue());   //월
        _str += _sex.equals("남자") ? "1" : "2"; // 성별
        _str += String.valueOf(employeeRepository.countNowEmployee(String.valueOf(now.getYear()))) ;
        System.out.println("_str = " + _str);
        return _str;
    }

    //사원 조회 ( 페이징 처리 예정)
    @Transactional
    public PageDto<EmployeeDto> getEmpList(int page,int sta,int dptmNo)
    {
        //List<EmployeeDto> result = new ArrayList<>();
        System.out.println("page = " + page + ", Sta = " + sta + ", dptmNo = " + dptmNo);
        Pageable pageable = PageRequest.of(page-1,10); //현재 페이지와 한 페이지에 보여줄 데이터 수 설정
        Page<EmployeeEntity> result = employeeRepository.findByEmpPage(sta,dptmNo,pageable); //pageable을 인자로 넘겨서 findAll 페이징 처리
        PageDto<EmployeeDto> pageDto = PageDto.<EmployeeDto>builder()
                .totalCount(result.getTotalElements()) // 검색된 row 개수
                .totalPages(result.getTotalPages())   // 총 페이지 수
                .someList(result.stream().map(emp -> emp.allToDto()).collect(Collectors.toList())) // 검색된 Entity 를 dto로 형변환한다
                .build();
        return pageDto;
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
    
    @Transactional
    public List<EmployeeDto> getAprvList()
    {
        //List<EmployeeEntity> result = employeeRepository.findByDptmNoAndEmpRkGreaterThan(1,0);
        List<EmployeeEntity> result = employeeRepository.findByDptmNoOrderByEmpRkDesc(departmentEntityRepository.findById(1).get());
        List<EmployeeDto> response = new ArrayList<>();
        result.forEach( e ->{
            response.add(e.allToDto());
        });

        return response;
    }
    public boolean changeInfo(ApprovalRequestDto<EmployeeDto> approvalRequestDto)
    {
        EmployeeDto employeeDto = approvalRequestDto.getData();
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(employeeDto.getEmpNo());
        if(optionalEmployeeEntity.isPresent())
        {
            System.out.println("step1");
            System.out.println(optionalEmployeeEntity.get().getEmpPwd() +"db 비번" );
            System.out.println(employeeDto.getEmpPwd() + " 입력 비번");
            if(optionalEmployeeEntity.get().getEmpPwd().equals(employeeDto.getEmpPwd()))
            {
                System.out.println("step2");
                if(!employeeDto.getEmpNewPwd().isEmpty())
                {
                    System.out.println("step3");
                    employeeDto.setEmpPwd(employeeDto.getEmpNewPwd());
                }
                try{
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = objectMapper.writeValueAsString(employeeDto);
                    approvalRequestDto.setAprvJson(json);

                    // 결재 테이블 등록 메서드
                    // => 실행 후 실행결과 반환

                    return approvalService.updateApproval(
                            approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                            approvalRequestDto.getAprvCont(),   // 결재내용
                            approvalRequestDto.getApprovers(),  // 검토자
                            approvalRequestDto.getAprvJson()    // 수정할 JSON 문자열
                    );

                }catch(Exception e) {
                    System.out.println("changeInfo" + e);
                }
            }
            System.out.println("step4");
        }
        return false;
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

    @Transactional
    public boolean changeEmployeeRank(ApprovalRequestDto<EmployeeDto> approvalRequestDto)
    {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(approvalRequestDto.getData());
            approvalRequestDto.setAprvJson(json);

            // 결재 테이블 등록 메서드
            // => 실행 후 실행결과 반환

            return approvalService.updateApproval(
                    approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                    approvalRequestDto.getAprvCont(),   // 결재내용
                    approvalRequestDto.getApprovers(),  // 검토자
                    approvalRequestDto.getAprvJson()    // 수정할 JSON 문자열
            );
        }catch(Exception e) {
            System.out.println("changeEmployeeRank" + e);
        }

        return false;
    }
    @Transactional
    public boolean changeEmployeeDepartment(ApprovalRequestDto<DepartmentHistoryDto> approvalRequestDto)
    {
        try{
            System.out.println("approvalRequestDto = " + approvalRequestDto);
            // DTO객체 => json 문자열
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(approvalRequestDto.getData());
            approvalRequestDto.setAprvJson(json);

            // 결재 테이블 등록 메서드
            // => 실행 후 실행결과 반환
            ApprovalEntity approvalEntity = approvalService.updateLogApproval(
                    approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                    approvalRequestDto.getAprvCont(),   // 결재내용
                    approvalRequestDto.getApprovers(),  // 검토자
                    approvalRequestDto.getAprvJson()    // 수정할 JSON 문자열
            );
            // 날짜 맞추기
            approvalRequestDto.getData().setHdtpmStart(approvalRequestDto.getData().getHdtpmStart().plusDays(1));

            //부서, 사원 id로 가져오기
            Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(approvalRequestDto.getEmpNo());
            Optional<DepartmentEntity> optionalDepartmentEntity = departmentEntityRepository.findById(approvalRequestDto.getData().getDtpmNo());

            // 부서,사원을 성공적으로 가져오면 실행
            if(optionalEmployeeEntity.isPresent() && optionalDepartmentEntity.isPresent())
            {
                //사원이 현재 일하고 있는 부서의 마지막 날 설정
                departmentHistoryEntityRepository.findTop1ByEmpNoAndHdptmEndIsNullOrderByHdptmEndDesc(optionalEmployeeEntity.get()).ifPresent( d ->{
                    d.setHdptmEnd(approvalRequestDto.getData().getHdtpmStart());
                });
                //부서 저장
                DepartmentHistoryEntity departmentHistoryEntity = DepartmentHistoryEntity.builder()
                        .htrdpRk(optionalEmployeeEntity.get().getEmpRk())
                        .dptmNo(optionalDepartmentEntity.get())
                        .empNo(optionalEmployeeEntity.get())
                        .hdptmStart(approvalRequestDto.getData().getHdtpmStart())
                        .aprvNo(approvalEntity).build();

                /* 단방향 */
                departmentHistoryEntityRepository.save(departmentHistoryEntity);
                /* 양방향 */
                optionalDepartmentEntity.get().getDepartmentHistory().add(departmentHistoryEntity);
                optionalEmployeeEntity.get().getDepartmentHistoryEntities().add(departmentHistoryEntity);
                approvalEntity.getDepartmentHistoryEntities().add(departmentHistoryEntity);
                return true;
            }
        }catch(Exception e) {
            System.out.println("changeEmployeeDepartment" + e);
        }
        return false;
    }











}
