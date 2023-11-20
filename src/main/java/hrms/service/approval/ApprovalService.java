package hrms.service.approval;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hrms.model.dto.*;
import hrms.model.entity.*;
import hrms.model.repository.*;
import hrms.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalEntityRepository approvalRepository;
    @Autowired
    private ApprovalLogEntityRepository approvalLogRepository;
    @Autowired
    private EmployeeEntityRepository employeeRepository;
    @Autowired
    private DepartmentEntityRepository departmentEntityRepository;
    @Autowired
    private ProjectEntityRepository projectRepository;
    @Autowired
    private TeamMemberEntityRepository teamMemberRepository;
    @Autowired
    private LeaveRequestEntityRepository leaveRequestEntityRepository;
    @Autowired
    private DepartmentHistoryEntityRepository departmentHistoryEntityRepository;
    @Autowired
    private RetiredEmployeeEntityRepository retiredEmployeeEntityRepository;
    @Autowired
    private SecurityService securityService;

    // 최초등록 : 결재 테이블 등록 [등록 기능에 관한 테이블]
    @Transactional
    public ApprovalEntity postApproval(int aprvType, String aprvCont, ArrayList<String> approvers) {

        /*
        타입 정리가 확실히 될 시 유효성 검사 추가 예정

        if( aprvType == 0 ){
            return null;
        }
        */

        // 상신자
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo( securityService.getEmp().getEmpNo() );

        if (optionalEmployeeEntity.isPresent()) {

            ApprovalEntity approvalEntity = ApprovalEntity
                    .builder()
                    .aprvType(aprvType)
                    .aprvCont(aprvCont)
                    .empNo(optionalEmployeeEntity.get())
                    .build();

            // DB 저장
            ApprovalEntity result = approvalRepository.save(approvalEntity);
            /* 단방향 */
            // 검토자에 대한 사원테이블 JPA 단방향 관계 정립
            result.setEmpNo(optionalEmployeeEntity.get());
            /* 양방향 */
            // 사원테이블 JPA 단방향 관계 정립
            optionalEmployeeEntity.get().getApprovalEntities().add(result);

            // 검토자 DB 저장을 위한 메서드 실행
            postApprovalLog(approvers, result.getAprvNo());

            if (result.getAprvNo() >= 1) return result;

        }
        return null;
    }

    //post시 json 저장이 필요한 경우
    @Transactional
    public ApprovalEntity postApprovalJson(int aprvType, String aprvCont, ArrayList<String> approvers,String aprvJson) {
        /*System.out.println("aprvType = " + aprvType + ", aprvCont = " + aprvCont + ", approvers = " + approvers + ", aprvJson = " + aprvJson);*/
        try{
            // 상신자
            Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo( securityService.getEmp().getEmpNo() );

            // 결재 내용이 없을 시 기본값 null 을 공백으로 변경하여 DB저장
            if( aprvCont == null ) aprvCont = "";

            if (optionalEmployeeEntity.isPresent()) {
                /*System.out.println("step1");*/
                ApprovalEntity approvalEntity = ApprovalEntity
                        .builder()
                        .aprvType(aprvType)
                        .aprvCont(aprvCont)
                        .aprvJson(aprvJson)
                        .empNo(optionalEmployeeEntity.get())
                        .build();
                // DB 저장
                System.out.println("approvalEntity = " + approvalEntity);
                ApprovalEntity result = approvalRepository.save(approvalEntity);
                /* 단방향 */
                // 검토자에 대한 사원테이블 JPA 단방향 관계 정립
                result.setEmpNo(optionalEmployeeEntity.get());
                /* 양방향 */
                // 사원테이블 JPA 단방향 관계 정립
                optionalEmployeeEntity.get().getApprovalEntities().add(result);
                // 검토자 DB 저장을 위한 메서드 실행
                postApprovalLog(approvers, result.getAprvNo());
                if (result.getAprvNo() >= 1) return result;

            }
        }catch(Exception e) {
            System.out.println("postApprovalJson" + e);
        }

        return null;
    }

    // 수정 : 결재 테이블 JSON문자열 저장 [수정 기능에 관한 테이블]
    @Transactional
    public boolean updateApproval(int aprvType, String aprvCont, ArrayList<String> approvers, String aprvJson) {

        /*
        타입 정리가 확실히 될 시 유효성 검사 추가 예정

        if( aprvType == 0 ){
            return null;
        }
        */

        // 상신자
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo( securityService.getEmp().getEmpNo() );

        if (optionalEmployeeEntity.isPresent()) {

            ApprovalEntity approvalEntity = ApprovalEntity
                    .builder()
                    .aprvType(aprvType)
                    .aprvCont(aprvCont)
                    .aprvJson(aprvJson)
                    .empNo(optionalEmployeeEntity.get())
                    .build();

            // DB 저장
            ApprovalEntity result = approvalRepository.save(approvalEntity);
            /* 단방향 */
            // 검토자에 대한 사원테이블 JPA 단방향 관계 정립
            result.setEmpNo(optionalEmployeeEntity.get());

            /* 양방향 */
            // 사원테이블 JPA 단방향 관계 정립
            optionalEmployeeEntity.get().getApprovalEntities().add(result);

            // 검토자 DB 저장을 위한 메서드 실행
            postApprovalLog(approvers, result.getAprvNo());

            if (result.getAprvNo() >= 1) return true;

        }

        return false;
    }


    // 결재 검토자 테이블에 검토자 등록
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean postApprovalLog(ArrayList<String> approvers, int aprvNo) {

        // 해당 결재건을 조회
        Optional<ApprovalEntity> optionalApproval = approvalRepository.findById(aprvNo);
        Optional<EmployeeEntity> optionalEmployee;

        // 검토자 목록을 순회하며 각 검토자에 대한 ApprovalLogEntity를 생성하고 설정
        for (String approver : approvers) {

            // 검토자 1명에 대한 optional EMP 객체 생성
            optionalEmployee = employeeRepository.findByEmpNo(approver);

            if (optionalEmployee.isPresent() && optionalApproval.isPresent()) {

                /* 단방향 */
                // 결재 검토자에 대한 객체 생성
                ApprovalLogEntity logEntity = ApprovalLogEntity
                        .builder()
                        .aplogSta(3)
                        .empNo(optionalEmployee.get())
                        .aprvNo(optionalApproval.get())
                        .build();
                // DB 저장
                ApprovalLogEntity result = approvalLogRepository.save(logEntity);
                // 결재테이블 JPA 단방향 관계 정립
                result.setAprvNo(optionalApproval.get());
                // 사원테이블 JPA 단방향 관계 정립
                result.setEmpNo(optionalEmployee.get());

                /* 양방향 */
                // 결재테이블 JPA 양방향 관계 정립
                optionalApproval.get().getApprovalLogEntities().add(result);
                // 사원테이블 JPA 양방향 관계 정립
                optionalEmployee.get().getApprovalLogs().add(result);

            } else {
                return false;
            }
        }

        return true;

    }

    // 검토자 1명 승인
    @Transactional
    public boolean approbate(int aprvNo, int aplogSta) throws JsonProcessingException {

        // 검토자
        Optional<EmployeeEntity> optionalEmployee = employeeRepository.findByEmpNo( securityService.getEmp().getEmpNo() );
        Optional<ApprovalEntity> optionalApproval = approvalRepository.findById(aprvNo);


        if (!optionalApproval.isPresent() || !optionalEmployee.isPresent()) return false;

        Optional<ApprovalLogEntity> optionalApprovallogEntity = approvalLogRepository.findByAprvNoAndEmpNo(
                optionalApproval.get(), optionalEmployee.get());

        switch (aplogSta) {
            // 결재
            case 1:
                optionalApprovallogEntity.get().setAplogSta(1);
                break;
            // 반려
            case 2:
                optionalApprovallogEntity.get().setAplogSta(2);
                break;
            default:
                return false;
        }

        // 해당 검토자가 최종 검토자일 경우
        if (approvalLogRepository.findMinAplogNoByAprvNo(optionalApproval.get()) == optionalApprovallogEntity.get().getAplogNo()) {
            // 결재의 종류가 수정(put)일 경우
            // 기존 DB에 저장된 문자열JSON 이용하여 DB UPDATE 실행
            switch (optionalApproval.get().getAprvType()) {
                case 1:
                    commitEmployeeRegister(aprvNo);
                    break;
                // 사원정보 수정
                case 2:
                case 3:
                case 4:
                case 5:
                    return updateMemberInfoAproval(optionalApproval.get().getAprvNo());

                // (휴직/연차/병가) 기간 변경 수정
                case 7:
                case 9:
                case 11:
                    return updateLeaveRequestInfoAproval(optionalApproval.get().getAprvNo());

                // 프로젝트 수정
                case 13:
                    return updateProjectInfoAproval(optionalApproval.get().getAprvNo());

                // 프로젝트 / 프로젝트 팀원 삭제
                case 14:
                case 17:
                    return deleteProjectApproval(optionalApproval.get().getAprvNo());

                // 프로젝트 팀 사원 수정
                case 16:
                    return updateTeamMemberApproval(optionalApproval.get().getAprvNo());

            }
        }

        return true;
    }

    // 결재건에 대한 결재 진행상태 확인 ( 1:결재완료  2:반려  3:검토중 )
    public int checkApprovalState(ApprovalEntity approvalEntity) {

        // 1개의 결재건에 대한 검토자리스트 호출
        for (int i = 0; i < approvalEntity.getApprovalLogEntities().size(); i++) {

            // 1명의 검토자 비교
            switch (approvalEntity.getApprovalLogEntities().get(i).getAplogSta()) {

                case 2: return 2;   // 결재상태 : 반려

                case 3: return 3;   // 결재상태 : 검토중

                case 1:             // 결재상태 : 완료
                    if( i == approvalEntity.getApprovalLogEntities().size()-1 )  return 1;

            }
        }

        return -1;
    }

    @Transactional // 결제 완료된 사원 퇴사
    public boolean commitRetiredEmployee(int aprvNo)
    {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,true);
            Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
            System.out.println("step 1");
            if(optionalApprovalEntity.isPresent())
            {
                //저장된 퇴사 정보 가져오기
                RetiredEmployeeDto retiredEmployeeDto = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(),RetiredEmployeeDto.class);
                //퇴사원 정보 호출
                Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(retiredEmployeeDto.getEmpNo());
                System.out.println("step 2");
                if(optionalEmployeeEntity.isPresent()){
                    RetiredEmployeeEntity retiredEmployeeEntity = retiredEmployeeEntityRepository.save(retiredEmployeeDto.saveToEntity()); // 퇴사 기록 저장
                    //단방향
                    retiredEmployeeEntity.setAprvNo(optionalApprovalEntity.get());
                    retiredEmployeeEntity.setEmpNo(optionalEmployeeEntity.get());
                    //양방향
                    optionalEmployeeEntity.get().getRetiredEmployeeEntities().add(retiredEmployeeEntity);
                    optionalApprovalEntity.get().getRetiredEmployees().add(retiredEmployeeEntity);
                    optionalEmployeeEntity.get().setEmpSta(false ); // 퇴사
                    //이후 처리
                    Optional<DepartmentHistoryEntity> optionalDepartmentHistoryEntity = departmentHistoryEntityRepository.findTop1ByEmpNoAndHdptmEndIsNullOrderByHdptmEndDesc(optionalEmployeeEntity.get());
                    Optional<TeamMemberEntity> optionalTeamMemberEntity = teamMemberRepository.findTop1ByEmpNoAndTmEndIsNullOrderByTmNoDesc(optionalEmployeeEntity.get());
                    if(optionalDepartmentHistoryEntity.isPresent() && optionalTeamMemberEntity.isPresent())
                    {
                        optionalDepartmentHistoryEntity.get().setHdptmEnd(retiredEmployeeDto.getRtempDate()); //부서 마지막날 설정
                        optionalTeamMemberEntity.get().setTmEnd(retiredEmployeeDto.getRtempDate()); // 팀 멤버 마지막 날 설정
                    }

                    return true;
                }
            }

        }catch(Exception e) {
            System.out.println("commitRetiredEmployee" + e);
        }

        return false;
    }
    // 결재 완료된 사원 '수정' 기능 메서드
    @Transactional
    public boolean updateMemberInfoAproval(int aprvNo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();



        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
        if (!optionalApprovalEntity.isPresent()) return false;
        if(optionalApprovalEntity.get().getAprvType() == 2 || optionalApprovalEntity.get().getAprvType() == 5) // 기본 정보 수정
        {
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }else if(optionalApprovalEntity.get().getAprvType() == 3) //사원 퇴사
        {
            System.out.println("타입 3 들어옴");
            return commitRetiredEmployee(aprvNo);
        }else if(optionalApprovalEntity.get().getAprvType() == 4) //부서 변경
        {
            return commitChangeDepartment(aprvNo);
        }else{
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        }
        // JSON문자열 => DTO객체로 변환
        EmployeeDto employeeDto
                = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), EmployeeDto.class);
        // DTO => Entity 객체로 변환
        EmployeeEntity employeeEntity = employeeDto.saveToEntity();

        // 수정 전 해당 결재수정안에 대한 data 호출
        // 변경 대상사원 객체 호출
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(employeeEntity.getEmpNo());

        if (optionalEmployeeEntity.isPresent()) {

            // 결재 타입에 따른 DB변경 실행
            switch (optionalApprovalEntity.get().getAprvType()) {

                // 사원 기본정보 변경(전화번호/비밀번호/계좌번호)
                case 2:
                    optionalEmployeeEntity.get().setEmpPhone(employeeEntity.getEmpPhone());
                    optionalEmployeeEntity.get().setEmpPwd(employeeEntity.getEmpPwd());
                    optionalEmployeeEntity.get().setEmpAcn(employeeEntity.getEmpAcn());
                    break;



                // 사원 직급변경
                case 5:
                    optionalEmployeeEntity.get().setEmpRk(employeeEntity.getEmpRk());
                    break;

            }
            return true;
        }
        return false;

    }
    @Transactional //결제 완료시 부서 변경
    public boolean commitChangeDepartment(int aprvNo)
    {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

            Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
            if (!optionalApprovalEntity.isPresent()) return false;

            // JSON문자열 => DTO객체로 변환
            DepartmentHistoryDto departmentHistoryDto
                    = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), DepartmentHistoryDto.class);
            //부서, 사원 id로 가져오기
            Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo(departmentHistoryDto.getEmpNo());
            Optional<DepartmentEntity> optionalDepartmentEntity = departmentEntityRepository.findById(departmentHistoryDto.getDptmNo());
            System.out.println("오나?");
            System.out.println("optionalDepartmentEntity = " + optionalDepartmentEntity);
            System.out.println("optionalEmployeeEntity = " + optionalEmployeeEntity);
            // 부서,사원을 성공적으로 가져오면 실행
            if(optionalEmployeeEntity.isPresent() && optionalDepartmentEntity.isPresent())
            {
                //사원이 현재 일하고 있는 부서의 마지막 날 설정
                departmentHistoryEntityRepository.findTop1ByEmpNoAndHdptmEndIsNullOrderByHdptmEndDesc(optionalEmployeeEntity.get()).ifPresent( d ->{
                    d.setHdptmEnd(departmentHistoryDto.getHdptmStart());
                });
                //부서 저장
                DepartmentHistoryEntity departmentHistoryEntity = DepartmentHistoryEntity.builder()
                        .htrdpRk(optionalEmployeeEntity.get().getEmpRk())
                        .dptmNo(optionalDepartmentEntity.get())
                        .empNo(optionalEmployeeEntity.get())
                        .hdptmStart(departmentHistoryDto.getHdptmStart())
                        .aprvNo(optionalApprovalEntity.get()).build();

                /* 단방향 */
                System.out.println("여기");
                optionalEmployeeEntity.get().setDptmNo(departmentHistoryEntity.getDptmNo());
                System.out.println("optionalEmployeeEntity = " + optionalEmployeeEntity);
                departmentHistoryEntityRepository.save(departmentHistoryEntity);
                /* 양방향 */
                optionalDepartmentEntity.get().getDepartmentHistory().add(departmentHistoryEntity);
                optionalEmployeeEntity.get().getDepartmentHistoryEntities().add(departmentHistoryEntity);
                optionalApprovalEntity.get().getDepartmentHistoryEntities().add(departmentHistoryEntity);

                return true;
            }
        }catch(Exception e) {
            System.out.println("commitChangeDepartment" + e);
        }



        return false;
    }

    // 결재 완료된 (휴직/연차/병가) 기간/급여지급여부 '수정' 기능 메서드
    @Transactional
    public boolean updateLeaveRequestInfoAproval(int aprvNo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
        if (!optionalApprovalEntity.isPresent()) return false;
        LeaveRequestEntity leaveRequestEntity = leaveRequestEntityRepository.findByAprvNo(optionalApprovalEntity.get()); // 휴가 신청 테이블 조인

        // JSON문자열 => DTO객체로 변환
        LeaveRequestDto leaveRequestDto  = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), LeaveRequestDto.class);

        leaveRequestEntity.setLrqSrtype(leaveRequestDto.getLrqSrtype()); // 급여 지급 수정
        leaveRequestEntity.setLrqSt(leaveRequestDto.getLrqSt()); // 휴가 시작날 수정
        leaveRequestEntity.setLrqEnd(leaveRequestDto.getLrqEnd()); // 휴가 복귀날 수정

        return true;
    }

    // 결재 완료된 프로젝트 '수정' 기능 메서드
    @Transactional
    public boolean updateProjectInfoAproval(int aprvNo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // datetime 모듈 추가
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
        if (!optionalApprovalEntity.isPresent()) return false;

        // JSON문자열 => DTO객체로 변환
        ProjectDto projectDto
                = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), ProjectDto.class);

        // DTO => Entity 객체로 변환
        ProjectEntity projectEntity = projectDto.saveToEntity();

        // 수정 전 해당 결재수정안에 대한 data 호출
        // 변경대상 프로젝트 호출
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(projectDto.getPjtNo());

        if (optionalProjectEntity.isPresent()) {

            // 수정 PM ENTITY 호출
            Optional<EmployeeEntity> pmEntity = employeeRepository.findByEmpNo(projectDto.getEmpNo());
            // 프로젝트 변경
            optionalProjectEntity.get().setEmpNo(pmEntity.get());                 // 프로젝트PM
            optionalProjectEntity.get().setPjtName(projectEntity.getPjtName());   // 프로젝트명
            optionalProjectEntity.get().setPjtSt(projectEntity.getPjtSt());       // 시작일자
            optionalProjectEntity.get().setPjtEND(projectEntity.getPjtEND());     // 마감일자
            optionalProjectEntity.get().setPjtSta(projectEntity.getPjtSta());     // 프로젝트 진행상태 ( 0:진행x 1:진행중 )

            projectRepository.save(optionalProjectEntity.get());

        }

        return false;
    }

    // 결재 완료된 사원 '등록' 기능 메서드
    @Transactional
    public EmployeeDto commitEmployeeRegister(int aprvNo)
    {
        try{
            Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            if(optionalApprovalEntity.isPresent())
            {
                EmployeeDto employeeDto = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), EmployeeDto.class);
                // 입력한 부서의 fk 호출
                Optional<DepartmentEntity> optionalDepartmentEntity =  departmentEntityRepository.findById(employeeDto.getDptmNo());
                // 부서 유효성 검사 및 fk 매핑
                if(optionalDepartmentEntity.isPresent())
                {
                    //사원 저장
                    EmployeeEntity employeeEntity = employeeRepository.save(employeeDto.saveToEntity());
                    employeeEntity.setDptmNo(optionalDepartmentEntity.get()); // 사원 부서 fk

                    DepartmentHistoryEntity departmentHistoryEntity = DepartmentHistoryEntity.builder()
                            .hdptmStart(LocalDate.now())
                            .htrdpRk(employeeEntity.getEmpRk())
                            .aprvNo(optionalApprovalEntity.get())
                            .dptmNo(employeeEntity.getDptmNo())
                            .empNo(employeeEntity).build();
                    optionalDepartmentEntity.get().getEmployeeEntities().add(employeeEntity); //부서 pk
                    optionalDepartmentEntity.get().getDepartmentHistory().add(departmentHistoryEntity);
                    employeeEntity.getDepartmentHistoryEntities().add(departmentHistoryEntity);
                    System.out.println("optionalDepartmentEntity = " + optionalDepartmentEntity.get().getEmployeeEntities());
                    System.out.println("employeeEntity.getApprovalEntities() = " + employeeEntity.getApprovalEntities());

                }
            }
            return null;
        }catch(Exception e) {
            System.out.println("commitEmployeeRegister" + e);
        }

        return null;
    }

    // 결재 완료된 팀 멤버 '수정' 기능 메서드
    @Transactional
    public boolean updateTeamMemberApproval(int aprvNo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // datetime 모듈 추가
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
        if (!optionalApprovalEntity.isPresent()) return false;

        // JSON문자열 => DTO객체로 변환
        TeamMemberDto teamMemberDto
                = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), TeamMemberDto.class);

        // ※ DTO => Entity 객체로 변환할 생성자가 없으므로 생략

        // 수정 전 해당 결재수정안에 대한 data 호출
        // 변경대상 프로젝트 팀원 호출
        Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(teamMemberDto.getTmNo());

        if (optionalProjectEntity.isPresent()) {

            // 수정 PM ENTITY 호출
            Optional<TeamMemberEntity> optionalTeamMemberEntity = teamMemberRepository.findById(teamMemberDto.getTmNo());
            // 프로젝트 변경
            optionalTeamMemberEntity.get().setTmNo(teamMemberDto.getTmNo());
            optionalTeamMemberEntity.get().setTmSt(teamMemberDto.getTmSt());
            optionalTeamMemberEntity.get().setTmEnd(teamMemberDto.getTmEnd());

            projectRepository.save(optionalProjectEntity.get());

        }

        return false;
    }

    // 결재 완료된 프로젝트 '삭제' 기능 메서드
    @Transactional
    public boolean deleteProjectApproval(int aprvNo) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById(aprvNo);
        if (!optionalApprovalEntity.isPresent()) return false;

        // JSON문자열 => 객체로 변환
        int typeNo = optionalApprovalEntity.get().getAprvType();
        int idNo = objectMapper.readValue(optionalApprovalEntity.get().getAprvJson(), Integer.class);

        switch (typeNo) {

            // 프로젝트 삭제
            case 14:
                Optional<ProjectEntity> optionalProjectEntity = projectRepository.findById(idNo);
                if (!optionalProjectEntity.isPresent()) return false;
                projectRepository.deleteById(idNo);
                return true;

            // 프로젝트 팀원 삭제
            case 17:
                Optional<TeamMemberEntity> optionalTeamMemberEntity = teamMemberRepository.findById(idNo);
                if (!optionalTeamMemberEntity.isPresent()) return false;
                teamMemberRepository.deleteById(idNo);
                return true;
        }

        return false;
    }


    // 개별 상신목록 조회
    @Transactional
    public PageDto<ApprovalDto> getReconsiderHistory(
            int page, String key, String keyword,
            int apState, String strDate, String endDate ) {

        
        // 상신자
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpNo( securityService.getEmp().getEmpNo() );

        /* 자체 페이지 네이션 ============================================================= */

        // 페이지별 출력 결재 건수는 15건 고정
        int maxSize = 15;
        // 페이지 레코드 시작번호 ( 0, 13, 26 ... )
        int startRow = (page-1) * maxSize;
        // 총 출력 결재 건수
        int totalApprovalCount = approvalRepository.reconsiderMaxCheck( key, keyword, apState, strDate, endDate, securityService.getEmp().getEmpNo() ).size();

        /* ============================================================================= */

        // DB의 개별 상신목록 저장
        List<ApprovalEntity> approvalEntities = approvalRepository.reconsiderViewSearch( key, keyword, apState, strDate, endDate, securityService.getEmp().getEmpNo(), startRow, maxSize );

        // 변환할 DTO 리스트
        List<ApprovalDto> approvalDtos = new ArrayList<>();

        approvalEntities.forEach(e -> {

            // 변환된 DTO 리스트 삽입
            approvalDtos.add(e.toApprovalDto());
            // 마지막 추가된 DTO
            // => 현재 결재 진행상태를 확인하여 저장
            approvalDtos.get(approvalDtos.size() - 1).setApState( checkApprovalState(e) );
         // approvalDtos.get(approvalDtos.size() - 1).setApState( checkApprovalState(e) );
            // 상신자명 저장
            approvalDtos.get(approvalDtos.size()-1).setEmpName( e.getEmpNo().getEmpName() );

        });

        return PageDto.<ApprovalDto> builder()
                .someList(approvalDtos)
                .totalPages(totalApprovalCount%maxSize == 0
                            ? totalApprovalCount/maxSize
                            : totalApprovalCount/maxSize+1)
                .build();
    }

    
    // 개별 결재목록 조회
    @Transactional
    public PageDto<ApprovalDto> getApprovalHistory(
            int page, String key, String keyword,
            int apState, String strDate, String endDate ) {


        /* 자체 페이지 네이션 ============================================================= */

        // 페이지별 출력 결재 건수는 15건 고정
        int maxSize = 15;
        // 페이지 레코드 시작번호 ( 0, 13, 26 ... )
        int startRow = (page-1) * maxSize;
        // 총 출력 결재 건수
        int totalApprovalCount = approvalRepository.approvalViewMaxCheck( key, keyword, apState, strDate, endDate, securityService.getEmp().getEmpNo() ).size();

        /* ============================================================================= */

        // DB의 결재목록 저장
        List<ApprovalEntity> approvalEntities = approvalRepository.approvalViewSearch( key, keyword, apState, strDate, endDate, securityService.getEmp().getEmpNo(), startRow, maxSize );

        // 반환할 LIST 선언
        List<ApprovalDto> approvalDtos = new ArrayList<>();

        approvalEntities.forEach( e-> {

            // DTO로 변환하여 list 저장
            approvalDtos.add( e.toApprovalDto() );
            // checkApprovalState(e) : 추가된 결재 건에 대해 검토진행 현황 확인 후 저장
            // 1:완료  2:반려  3:검토중
            approvalDtos.get(approvalDtos.size()-1).setApState( checkApprovalState( e ) );
            // 상신자명 저장
            approvalDtos.get(approvalDtos.size()-1).setEmpName( e.getEmpNo().getEmpName() );

        });


        return PageDto.<ApprovalDto>builder()
                .someList(approvalDtos)
                .totalPages(totalApprovalCount%maxSize == 0
                            ? totalApprovalCount/maxSize
                            : totalApprovalCount/maxSize+1 )
                .build();
    }
       


    // 전사원 상신목록 조회
    @Transactional
    public PageDto<ApprovalDto> getAllEmployeesApproval(
            int page, String key, String keyword,
            int apState, String strDate, String endDate ) throws JsonProcessingException {


        /* 자체 페이지 네이션 ============================================================= */

        // 페이지별 출력 결재 건수는 15건 고정
        int maxSize = 15;
        // 페이지 레코드 시작번호 ( 0, 13, 26 ... )
        int startRow = (page-1) * maxSize;
        // 총 출력 결재 건수
        int totalApprovalCount = approvalRepository.findByMaxCheck( key, keyword, apState, strDate, endDate ).size();

        /* ============================================================================= */


        // DB의 전사원 결재목록 저장
        List<ApprovalEntity> approvalEntities = approvalRepository.findBySearch( key, keyword, apState, strDate, endDate,startRow ,maxSize );

        // 반환할 LIST 선언
        List<ApprovalDto> approvalDtos = new ArrayList<>();

        approvalEntities.forEach( e-> {

            // DTO로 변환하여 list 저장
            approvalDtos.add( e.toApprovalDto() );
            // checkApprovalState(e) : 추가된 결재 건에 대해 검토진행 현황 확인 후 저장
                // 1:완료  2:반려  3:검토중
            approvalDtos.get(approvalDtos.size()-1).setApState( checkApprovalState( e ) );
            // 상신자명 저장
            approvalDtos.get(approvalDtos.size()-1).setEmpName( e.getEmpNo().getEmpName() );

        });

        return PageDto.<ApprovalDto>builder()
                .someList(approvalDtos)
                .totalPages( totalApprovalCount % maxSize == 0
                            ? totalApprovalCount / maxSize
                            : totalApprovalCount / maxSize+1  )
                .build();

    }

    // 결재 상세내역 조회
    @Transactional
    public ApprovalEntity getDetailedApproval( int aprvNo ){

        Optional<ApprovalEntity> optionalApprovalEntity = approvalRepository.findById( aprvNo );

        if( optionalApprovalEntity.isPresent() ){
            return optionalApprovalEntity.get();
        }

        return null;
    }


}





















