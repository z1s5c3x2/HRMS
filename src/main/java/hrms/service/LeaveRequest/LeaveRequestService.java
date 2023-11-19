package hrms.service.LeaveRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.LeaveRequestDto;
import hrms.model.dto.PageDto;
import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.LeaveRequestEntity;
import hrms.model.repository.EmployeeEntityRepository;
import hrms.model.repository.LeaveRequestEntityRepository;
import hrms.service.approval.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestEntityRepository leaveRequestRepository;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private EmployeeEntityRepository employeeEntityRepository;
    @Autowired
    private LeaveCalcService leaveCalcService;

    @Transactional
    public boolean lrqWrite(ApprovalRequestDto<LeaveRequestDto> approvalRequestDto) {
        // 결재 테이블 등록 메서드
        // => 실행 후 aprv엔티티 객체 반환
        ApprovalEntity approvalEntity = approvalService.postApproval(
                approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                approvalRequestDto.getAprvCont(),   // 결재내용
                approvalRequestDto.getApprovers()   // 검토자
        );
        LeaveRequestDto leaveRequestDto = approvalRequestDto.getData();

        // 1. 사원 번호(empNo)를 사용하여 EmployeeEntity를 찾습니다.
        String empNoString = leaveRequestDto.getEmpNo();
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo(empNoString);

        if (optionalEmployeeEntity.isPresent()) {

            EmployeeEntity employeeEntity = optionalEmployeeEntity.get();
            // 2. LeaveRequestEntity 생성하고 EmployeeEntity를 설정합니다.
            LeaveRequestEntity leaveRequestEntity = LeaveRequestEntity.builder()
                    .lrqSt(leaveRequestDto.getLrqSt().plusDays(1))
                    .lrqEnd(leaveRequestDto.getLrqEnd().plusDays(1))
                    .lrqSrtype(leaveRequestDto.getLrqSrtype())
                    .lrqType(leaveRequestDto.getLrqType())
                    .aprvNo(approvalEntity)
                    .empNo(employeeEntity)
                    .build();

            leaveRequestRepository.save(leaveRequestEntity);
            employeeEntity.setEmpSta(false);
            // 양방향
            approvalEntity.getLeaveRequestEntities().add(leaveRequestEntity);


            if (leaveRequestEntity.getLrqNo() >= 1) {
                return true;
            }
        }
        return false;
    }

    // 2. 모두 출력
    @Transactional
    public PageDto lrqGetAll(int page, String key, String keyword, int view, int empRk, int dptmNo, int lrqType, int lrqSrtype, String DateSt, String DateEnd) {
        // 1. 모두 출력
        // 페이징처리
        Pageable pageable = PageRequest.of(page - 1, view);
        // 1. 모든 게시물 호출한다.
        Page<LeaveRequestEntity> leaveRequestEntities = leaveRequestRepository.findBySearch(key, keyword, empRk, dptmNo, lrqType, lrqSrtype, DateSt, DateEnd, pageable);

        List<LeaveRequestDto> leaveRequestDtos = new ArrayList<>();
        leaveRequestEntities.forEach(e -> {
            leaveRequestDtos.add(e.allToDto());
        });

        // 5. pageDto 구성해서 axios에게 전달
        PageDto<LeaveRequestDto> pageDto = PageDto.<LeaveRequestDto>builder()
                .totalPages(leaveRequestEntities.getTotalPages()) // 총페이지
                .totalCount(leaveRequestEntities.getTotalElements()) // 검색된 row개수
                .someList(leaveRequestEntities.stream().map(lrq -> lrq.allToDto()).collect(Collectors.toList()))
                .build();

        return pageDto;
    }

    // 개별 출력 ( 기본적으로 Main에서 내정보 보기 , 인사팀이 사원정보로 입력시 정보 호출 )
    public PageDto lrqGet(int page, int view, String empNo , int lrqType, int lrqSrtype, String DateSt, String DateEnd) {
        // 페이징처리
        Pageable pageable = PageRequest.of(page - 1, view);

        // 1. 해당 empNo에 맞는 엔티티 호출
        Page<LeaveRequestEntity> leaveRequestEntities = leaveRequestRepository.findByEmpNo_EmpNo(empNo, lrqType ,lrqSrtype , DateSt , DateEnd , pageable);

        List<LeaveRequestDto> leaveRequestDtos = new ArrayList<>();
        leaveRequestEntities.forEach(e -> {
            leaveRequestDtos.add(e.allToDto());
        });

        // 5. pageDto 구성해서 axios에게 전달
        PageDto<LeaveRequestDto> pageDto = PageDto.<LeaveRequestDto>builder()
                .totalPages(leaveRequestEntities.getTotalPages()) // 총페이지
                .totalCount(leaveRequestEntities.getTotalElements()) // 검색된 row개수
                .someList(leaveRequestEntities.stream().map(lrq -> lrq.allToDto()).collect(Collectors.toList()))
                .build();

        return pageDto;
    }

    //3. 수정
    @Transactional
    public boolean lrqUpdate(LeaveRequestDto leaveRequestDto) {
        // 수정할 엔티티 찾기
        Optional<LeaveRequestEntity> optionalLeaveRequestEntity = leaveRequestRepository.findById(leaveRequestDto.getLrqNo());
        // 수정할 엔티티 존재하면?
        if (optionalLeaveRequestEntity.isPresent()) {
            // 엔티티 꺼내기
            LeaveRequestEntity leaveRequestEntity = optionalLeaveRequestEntity.get();
            // 객체 수정하면 테이블 내 레코드 같이 수정
            leaveRequestEntity.setLrqSt(leaveRequestDto.getLrqSt());
            leaveRequestEntity.setLrqEnd(leaveRequestDto.getLrqEnd());
            leaveRequestEntity.setLrqSrtype(leaveRequestDto.getLrqSrtype());
            leaveRequestEntity.setLrqType(leaveRequestDto.getLrqType());
            return true;
        }
        return false;
    }

    // 4 삭제
    @Transactional
    public boolean lrqDelete(int lrqNo) {
        // 1. 엔티티 호출
        Optional<LeaveRequestEntity> optionalLeaveRequestEntity = leaveRequestRepository.findById(lrqNo);
        // 2. 엔티티가 호출되었는지 확인
        if (optionalLeaveRequestEntity.isPresent()) {
            //3. 삭제
            leaveRequestRepository.deleteById(lrqNo);
            return true;
        }
        return false;
    }

    // 하나 찾기
    public LeaveRequestDto findOneLrq(int lrqNo) {
        System.out.println("lrqNo = " + lrqNo);
        System.out.println("LeaveRequestController.findOneLrq");
        Optional<LeaveRequestEntity> optionalLeaveRequestEntity = leaveRequestRepository.findById(lrqNo);
        if (optionalLeaveRequestEntity.isPresent()) {
            LeaveRequestDto leaveRequestDto = optionalLeaveRequestEntity.get().OneToDto();
            leaveRequestDto.setLeaveCnt(leaveCalcService.calcRestCount(leaveRequestDto.getEmpNo()));
            return leaveRequestDto;
        }
        return null;
    }

    // 연차 수정
    public boolean updateYearLrq(@RequestBody ApprovalRequestDto<LeaveRequestDto> approvalRequestDto) {
        try {
            System.out.println("updateYearLrq = " + approvalRequestDto);
            // DTO객체 => json 문자열
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String json = objectMapper.writeValueAsString(approvalRequestDto.getData());
            approvalRequestDto.setAprvJson(json);
            // 날짜 맞추기
            approvalRequestDto.getData().setLrqSt(approvalRequestDto.getData().getLrqSt().plusDays(1));
            approvalRequestDto.getData().setLrqEnd(approvalRequestDto.getData().getLrqEnd().plusDays(1));
            // 결재 테이블 등록 메서드
            // => 실행 후 실행결과 반환
            return approvalService.updateApproval(
                    approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                    approvalRequestDto.getAprvCont(),   // 결재내용
                    approvalRequestDto.getApprovers(),  // 검토자
                    approvalRequestDto.getAprvJson()    // 수정할 JSON 문자열
            );


        } catch (Exception e) {
            System.out.println("updateYearLrq" + e);
        }

        return false;
    }
}
