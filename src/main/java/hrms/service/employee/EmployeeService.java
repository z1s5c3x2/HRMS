package hrms.service.employee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hrms.model.dto.*;
import hrms.model.entity.*;
import hrms.model.repository.*;
import hrms.service.approval.ApprovalService;
import hrms.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    ApprovalLogEntityRepository approvalLogEntityRepository;
    @Autowired
    private SecurityService securityService;
    private final int LEAVE_COUNT = 5;
    // 비밀번호 인코딩
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    // 사원 등록
    @Transactional
    public boolean registerEmp(ApprovalRequestDto<EmployeeDto> employeeDtoApprovalRequestDto)
    {
        EmployeeDto employeeDto = employeeDtoApprovalRequestDto.getData(); // 결제정보를 포함한 dto에서 사원 데이터 추출
        employeeDto.setEmpNo(generateEmpNumber(employeeDto.getEmpSex())); // pk생성
        employeeDto.setEmpPwd(passwordEncoder.encode(employeeDto.getEmpPwd()));
        System.out.println("employeeDto = " + employeeDto.toString());
        // 결제 등록 후 entity반환
        try{
            ApprovalEntity approvalEntity = approvalService.postApprovalJson(
                    employeeDtoApprovalRequestDto.getAprvType()
                    , employeeDtoApprovalRequestDto.getAprvCont()
                    ,employeeDtoApprovalRequestDto.getApprovers()
                    ,new ObjectMapper().writeValueAsString(employeeDto));
            return approvalEntity.getAprvNo() > 0;
        }catch(Exception e) {
            System.out.println("registerEmp" + e);
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
                .someList(result.stream().map(EmployeeEntity::allToDto).collect(Collectors.toList())) // 검색된 Entity 를 dto로 형변환한다
                .build();
        return pageDto;
    }
    @Transactional
    public EmployeeDto getMyInfo()
    {
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(securityService.getEmp().getEmpNo());
        if(optionalEmployeeEntity.isPresent())
        {
            return optionalEmployeeEntity.get().notPwdToDto();
        }
        return null;
    }
    //사원 개별 조회
    @Transactional
    public EmployeeDto getOneEmp(String empNo)
    {
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(empNo);
        if(optionalEmployeeEntity.isPresent())
        {
            return optionalEmployeeEntity.get().notPwdToDto();
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
    public boolean changeInfo(ApprovalRequestDto<EmployeeDto> approvalRequestDto) //사원 개인정보 수정
    {
        EmployeeDto employeeDto = approvalRequestDto.getData();
        System.out.println("employeeDto = " + employeeDto);
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(employeeDto.getEmpNo());
        //변경한 사원의 현재 비밀번호와 일치하면 수정을 완료하고 새로운 비밀번호가 입력 됐으면
        // 새로운 비밀번호를 대입하여 수정 테이블에 삽입
        if(optionalEmployeeEntity.isPresent())
        {

            /*System.out.println(optionalEmployeeEntity.get().getEmpPwd() +"db 비번" );
            System.out.println(employeeDto.getEmpPwd() + " 입력 비번");*/
            if(passwordEncoder.matches(employeeDto.getEmpPwd(),optionalEmployeeEntity.get().getEmpPwd()))
            {
                if(employeeDto.getEmpNewPwd() != null)
                {
                    System.out.println("새로운 비번");
                    employeeDto.setEmpPwd(passwordEncoder.encode(employeeDto.getEmpNewPwd()));
                }else{
                    System.out.println("그냥 비번");
                    employeeDto.setEmpPwd(optionalEmployeeEntity.get().getEmpPwd());
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
        }
        return false;
    }

    @Transactional //사원 퇴사 등록
    public boolean setRetiredEmployee(ApprovalRequestDto<RetiredEmployeeDto> approvalRequestDto)
    {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(approvalRequestDto.getData());
            System.out.println("EmployeeService.setRetiredEmployee");
            System.out.println("json = " + json);
            // 날짜 맞추기
            approvalRequestDto.getData().setRtempDate(approvalRequestDto.getData().getRtempDate().plusDays(1));
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
            System.out.println("setRetiredEmployee" + e);
        }



        return false;
    }

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

    @Transactional //사원 직급 변경
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
    @Transactional //사원 부서 변경
    public boolean changeEmployeeDepartment(ApprovalRequestDto<DepartmentHistoryDto> approvalRequestDto)
    {
        try{
            System.out.println("approvalRequestDto = " + approvalRequestDto);
            // DTO객체 => json 문자열
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(approvalRequestDto.getData());
            // 날짜 맞추기
            approvalRequestDto.getData().setHdptmStart(approvalRequestDto.getData().getHdptmStart().plusDays(1));

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
            System.out.println("changeEmployeeDepartment" + e);
        }
        return false;
    }
    @Transactional // 사원 검색 페이지에서의 페이징 처리
    public PageDto<EmployeeDto> findOneOption(EmployeeSearchOptionDto employeeSearchOptionDto)
    {
        System.out.println("employeeSearchOptionDto = " + employeeSearchOptionDto);
        Pageable pageable = PageRequest.of(employeeSearchOptionDto.getPage()-1,10);  // 현재 페이지 수 설정
        Page<EmployeeEntity> searchResult =  employeeRepository.searchToOption(employeeSearchOptionDto.getSearchNameOrEmpNo(),
                employeeSearchOptionDto.getSearchValue(),pageable);
        System.out.println("searchResult = " + searchResult);
        PageDto<EmployeeDto> result = PageDto.<EmployeeDto>builder()
                .totalCount(searchResult.getTotalElements())
                .totalPages(searchResult.getTotalPages())
                .someList(searchResult.stream().map(EmployeeEntity::searchToDto).collect(Collectors.toList())).build();
        return result;
    }

    @Transactional //검색 페이지에서의 사원 상세 정보 호출
    public EmployeeSearchDto empSearchInfo(String empNo)
    {
        // empno로 사원 정보 db에서 호출
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(empNo);
        if(optionalEmployeeEntity.isPresent())
        {
            //사원 entity를 페이지에 맞는 정보를 dto로 초기화
            EmployeeSearchDto employeeSearchDto = optionalEmployeeEntity.get().searchInfoToDto();
            //조회 사원의 결제리스트 호출
            //사원의 결제 리스트를 호출하여 filter로 반복
            //반복중인 결제건의 로그를 확인,필터 반복문
            //anymatch를 사용하여 올린 결제로그의 타입을 확인하여 결제 진행중(sta = 3)인것만 반환하여 카운트 집계, 설정
            employeeSearchDto.setAprvCount((int) optionalEmployeeEntity.get()
                    .getApprovalEntities()
                    .stream()
                    .filter(a -> a.getApprovalLogEntities().stream().anyMatch(l -> l.getAplogSta() == 3))
                    .count());
            // 사원 양방향 -> 결제 로그 리스트에서 결제타입 3인(검토중) 카운트 집계
            employeeSearchDto.setApLogCount((int)optionalEmployeeEntity.get().getApprovalLogs().stream().filter( l -> l.getAplogSta() == 3).count());
            return employeeSearchDto;
        }
        return null;
    }

    // pm을 제외한 개발팀 기획팀의 사원들을 출력
    @Transactional
    public List<EmployeeDto> getTeamsMebers(){

        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        List<EmployeeDto> employeeDtos = new ArrayList<>();

        for(EmployeeEntity employeeEntity : employeeEntities){
            System.out.println(employeeEntity);

            // 휴직상태인 사원들을 제외
            if(!employeeEntity.isEmpSta()){
                continue;
            }

            // 이미 프로젝트 매니저로 등록된 사원은 제외
            if(!employeeEntity.getProjectEntities().isEmpty()){
                System.out.println("이미등록된 프로젝트 매니저");
                continue;
            }

            // 이미 프로젝트 팀원으로 등록된 사원은 제외
            if(!employeeEntity.getTeamMemberEntities().isEmpty()){
                continue;
            }

            // 개발 or 기획팀이면 리스트에 추가
            if(employeeEntity.getDptmNo().getDptmNo() == 3 || employeeEntity.getDptmNo().getDptmNo() == 2){
                employeeDtos.add(employeeEntity.allToDto());
            }

        }

        return employeeDtos;
    }






}
