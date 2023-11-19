package hrms.service.Salary;


import hrms.model.dto.ApprovalRequestDto;
import hrms.model.dto.PageDto;
import hrms.model.dto.SalaryDto;
import hrms.model.entity.ApprovalEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.SalaryEntity;
import hrms.model.repository.EmployeeEntityRepository;
import hrms.model.repository.SalaryEntityRepository;
import hrms.service.approval.ApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SalaryService {
    @Autowired
    private SalaryEntityRepository salaryRepository;
    @Autowired
    private EmployeeEntityRepository employeeEntityRepository;
    @Autowired
    private ApprovalService approvalService;


    @Transactional
    public boolean slryWrite(ApprovalRequestDto<SalaryDto> approvalRequestDto) {

        // 결재 테이블 등록 메서드
        // => 실행 후 aprv엔티티 객체 반환
        ApprovalEntity approvalEntity = approvalService.postApproval(
                approvalRequestDto.getAprvType(),   // 결재타입 [메모장 참고]
                approvalRequestDto.getAprvCont(),   // 결재내용
                approvalRequestDto.getApprovers()   // 검토자
        );

        SalaryDto salaryDto = approvalRequestDto.getData();

        // 1. 사원 번호(empNo)를 사용하여 EmployeeEntity를 찾습니다.
        String empNoString = salaryDto.getEmpNo();
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo(empNoString);

        if (optionalEmployeeEntity.isPresent()) {

            EmployeeEntity employeeEntity = optionalEmployeeEntity.get();
            // 2. SalaryEntity를 생성하고 EmployeeEntity를 설정합니다.
            SalaryEntity salaryEntity = SalaryEntity.builder()
                    .slryDate(salaryDto.getSlryDate().plusDays(1))
                    .slryPay(salaryDto.getSlryPay())
                    .aprvNo(approvalEntity)
                    .slryType(salaryDto.getSlryType())
                    .empNo(employeeEntity) // Optional을 사용하여 EmployeeEntity를 가져옵니다.
                    .build();

            salaryRepository.save(salaryEntity);

            // 양방향
            approvalEntity.getSalaryEntities().add(salaryEntity);

            if (salaryEntity.getSlryNo() >= 1) {
                return true;
            }
        }

        return false;
    }


    @Transactional
    public PageDto slryGetAll(int page, String key,
                              String keyword, int view
            , int empRk, int dptmNo, int slryType, String DateSt, String DateEnd
    ) {
        // 1. 모두 출력
        System.out.println("page = " + page + ", key = " + key + ", keyword = " + keyword + ", view = " + view + ", empRk = " + empRk + ", dptmNo = " + dptmNo + ", slryType = " + slryType + ", DateSt = " + DateSt + ", DateEnd = " + DateEnd);
        // 페이징처리
        Pageable pageable = PageRequest.of(page - 1, view);
        // 1. 모든 게시물 호출한다.
        Page<SalaryEntity> salaryEntities = salaryRepository.findBySearch(key, keyword, empRk, dptmNo, slryType, DateSt, DateEnd, pageable);

        List<SalaryDto> salaryDtos = new ArrayList<>();
        salaryEntities.forEach(e -> {
            salaryDtos.add(e.allToDto());
        });

        // 5. pageDto 구성해서 axios에게 전달
        PageDto<SalaryDto> pageDto = PageDto.<SalaryDto>builder()
                .totalPages(salaryEntities.getTotalPages()) // 총페이지
                .totalCount(salaryEntities.getTotalElements()) // 검색된 row개수
                .someList(salaryEntities.stream().map(slry -> slry.allToDto()).collect(Collectors.toList()))
                .build();
        return pageDto;
    }

    @Transactional
    public SalaryDto slryGet(int slryNo) {
        Optional<SalaryEntity> optionalSalaryEntity = salaryRepository.findBySlryNo(slryNo);

        if (optionalSalaryEntity.isPresent()) {

            SalaryDto salaryDto = new SalaryDto();

            salaryDto.setSlryNo(slryNo);
            salaryDto.setSlryDate(optionalSalaryEntity.get().getSlryDate());
            salaryDto.setSlryPay(optionalSalaryEntity.get().getSlryPay());
            salaryDto.setSlryType(optionalSalaryEntity.get().getSlryType());
            salaryDto.setAprvNo(optionalSalaryEntity.get().getAprvNo().getAprvNo());
            salaryDto.setEmpNo(optionalSalaryEntity.get().getEmpNo().getEmpNo());
            salaryDto.setCdate(optionalSalaryEntity.get().getCdate());
            salaryDto.setUdate(optionalSalaryEntity.get().getUdate());
            return salaryDto;
        }
        return null;
    }

    public PageDto slryGetMeAll(int page, int view, String empNo, int slryType, String DateSt, String DateEnd) {
        // 페이징처리
        Pageable pageable = PageRequest.of(page - 1, view);

        // 1. 해당 empNo에 맞는 엔티티 호출
        Page<SalaryEntity> salaryEntities = salaryRepository.findByEmpNo_EmpNo(empNo, slryType, DateSt, DateEnd, pageable);

        List<SalaryDto> salaryDtos = new ArrayList<>();
        salaryEntities.forEach(e -> {
            salaryDtos.add(e.allToDto());
        });
        // 5. pageDto 구성해서 axios에게 전달
        PageDto<SalaryDto> pageDto = PageDto.<SalaryDto>builder()
                .totalPages(salaryEntities.getTotalPages()) // 총페이지
                .totalCount(salaryEntities.getTotalElements()) // 검색된 row개수
                .someList(salaryEntities.stream().map(slry -> slry.allToDto()).collect(Collectors.toList()))
                .build();

        return pageDto;
    }

    // 3.
    public boolean slryUpdate(SalaryDto salaryDto) {
        // 수정할 엔티티 찾기
        Optional<SalaryEntity> optionalSalaryEntity = salaryRepository.findById(salaryDto.getSlryNo());
        // 수정할 엔티티 존재하면?
        if (optionalSalaryEntity.isPresent()) {
            // 엔티티 꺼내기
            SalaryEntity salaryEntity = optionalSalaryEntity.get();
            // 객체 수정하면 테이블 내 레코드 같이 수정
            salaryEntity.setSlryDate(salaryDto.getSlryDate());
            salaryEntity.setSlryPay(salaryDto.getSlryPay());
            salaryEntity.setSlryType(salaryDto.getSlryType());
            return true;
        }
        return false;
    }
    // 4

    public boolean slryDelete(int slryNo) {
        // 1. 엔티티 호출
        Optional<SalaryEntity> optionalSalaryEntity = salaryRepository.findById(slryNo);
        // 2. 엔티티가 호출되었는지 확인
        if (optionalSalaryEntity.isPresent()) {
            //3. 삭제
            salaryRepository.deleteById(slryNo);
            return true;
        }
        return false;
    }
}
