package hrms.service.LeaveRequest;

import hrms.model.dto.LeaveRequestDto;
import hrms.model.dto.SalaryDto;
import hrms.model.entity.LeaveRequestEntity;
import hrms.model.entity.SalaryEntity;
import hrms.model.repository.LeaveRequestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {

    @Autowired
    private LeaveRequestEntityRepository leaveRequestRepository;

    @Transactional
    public boolean lrqWrite( LeaveRequestDto leaveRequestDto ){

        //
        leaveRequestDto.setLrqSt(leaveRequestDto.getLrqSt().plusDays(1));
        leaveRequestDto.setLrqEnd(leaveRequestDto.getLrqEnd().plusDays(1));

        LeaveRequestEntity leaveRequestEntity
                = leaveRequestRepository.save( leaveRequestDto.saveToEntity());
        // 2.
        System.out.println("service");
        System.out.println( leaveRequestEntity.getLrqSt() );
        System.out.println( leaveRequestEntity.getLrqEnd() );

        if( leaveRequestEntity.getLrqNo() >= 1){ return true;} return false;
    }

    // 2. 모두 출력
    @Transactional
    public List<LeaveRequestDto> lrqGetAll()  {
        // 1. 모두 출력
        List<LeaveRequestEntity> leaveRequestEntities = leaveRequestRepository.findAll();
        List<LeaveRequestDto> leaveRequestDtos = new ArrayList<>();
        leaveRequestEntities.forEach( e ->{ leaveRequestDtos.add( e.allToDto()); });
        return leaveRequestDtos;
    }
    // 2-2. 개별 출력
    @Transactional
    public LeaveRequestDto lrqGet( int lrqNo ){
        Optional<LeaveRequestEntity> optionalLeaveRequestEntity = leaveRequestRepository.findById( lrqNo ) ;

        if(optionalLeaveRequestEntity.isPresent()){

            LeaveRequestDto leaveRequestDto = new LeaveRequestDto();

            leaveRequestDto.setLrqNo( lrqNo );
            leaveRequestDto.setLrqSt( optionalLeaveRequestEntity.get().getLrqSt());
            leaveRequestDto.setLrqEnd( optionalLeaveRequestEntity.get().getLrqEnd());
            leaveRequestDto.setLrqType( optionalLeaveRequestEntity.get().getLrqType());
            leaveRequestDto.setLrqSrtype( optionalLeaveRequestEntity.get().getLrqSrtype());
            leaveRequestDto.setAprvNo( optionalLeaveRequestEntity.get().getAprvNo().getAprvNo());
            leaveRequestDto.setEmpNo( optionalLeaveRequestEntity.get().getEmpNo().getEmpNo() );
            leaveRequestDto.setCdate( optionalLeaveRequestEntity.get().getCdate());
            leaveRequestDto.setUdate( optionalLeaveRequestEntity.get().getUdate());
            return leaveRequestDto;
        }
        return null;
    }
    public List<LeaveRequestDto> lrqGetMeAll(String empNo) {
        // 1. 해당 empNo에 맞는 엔티티 호출
        List<LeaveRequestEntity> leaveRequestEntities = leaveRequestRepository.findByEmpNoEmpNo(empNo);

        List<LeaveRequestDto> leaveRequestDtos = new ArrayList<>();
        leaveRequestEntities.forEach(e -> {
            leaveRequestDtos.add(e.allToDto());
        });

        return leaveRequestDtos;
    }
    //3. 수정
    @Transactional
    public boolean lrqUpdate( LeaveRequestDto leaveRequestDto ){
        // 수정할 엔티티 찾기
        Optional<LeaveRequestEntity> optionalLeaveRequestEntity = leaveRequestRepository.findById( leaveRequestDto.getLrqNo() );
        // 수정할 엔티티 존재하면?
        if(optionalLeaveRequestEntity.isPresent()){
            // 엔티티 꺼내기
            LeaveRequestEntity leaveRequestEntity = optionalLeaveRequestEntity.get();
            // 객체 수정하면 테이블 내 레코드 같이 수정
            leaveRequestEntity.setLrqSt( leaveRequestDto.getLrqSt());
            leaveRequestEntity.setLrqEnd( leaveRequestDto.getLrqEnd());
            leaveRequestEntity.setLrqSrtype( leaveRequestDto.getLrqSrtype());
            leaveRequestEntity.setLrqType( leaveRequestDto.getLrqType());
            return true;
        }
        return false;
    }
    // 4 삭제
    @Transactional
    public boolean lrqDelete(int lrqNo){
       // 1. 엔티티 호출
        Optional<LeaveRequestEntity> optionalLeaveRequestEntity = leaveRequestRepository.findById( lrqNo );
        // 2. 엔티티가 호출되었는지 확인
        if(optionalLeaveRequestEntity.isPresent()){
            //3. 삭제
            leaveRequestRepository.deleteById(lrqNo);
            return true;
        }
        return false;
    }

}
