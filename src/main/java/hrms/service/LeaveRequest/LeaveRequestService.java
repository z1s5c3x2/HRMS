package hrms.service.LeaveRequest;

import hrms.model.dto.LeaveRequestDto;
import hrms.model.entity.LeaveRequestEntity;
import hrms.model.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LeaveRequestService {
    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    @Transactional
    public boolean lrqWrite( LeaveRequestDto leaveRequestDto ){
        LeaveRequestEntity leaveRequestEntity
                = leaveRequestRepository.save( leaveRequestDto.saveToEntity());
        // 2.
        if( leaveRequestEntity.getLrq_no() >= 1){ return true;} return false;
    }

    // 2. 모두 출력
    @Transactional
    public List<LeaveRequestDto> lrqGetAll(){
        // 1. 모두 출력
        List<LeaveRequestEntity> leaveRequestEntities = leaveRequestRepository.findAll();
        List<LeaveRequestDto> leaveRequestDtos = new ArrayList<>();
        leaveRequestEntities.forEach( e ->{ leaveRequestDtos.add( e.allToDto()); });
        return leaveRequestDtos;
    // 3.
    }
    @Transactional
    public boolean lrqUpdate( LeaveRequestDto leaveRequestDto ){
        // 수정할 엔티티 찾기
        Optional<LeaveRequestEntity> optionalLeaveRequestEntity = leaveRequestRepository.findById( leaveRequestDto.getLrq_no() );
        // 수정할 엔티티 존재하면?
        if(optionalLeaveRequestEntity.isPresent()){
            // 엔티티 꺼내기
            LeaveRequestEntity leaveRequestEntity = optionalLeaveRequestEntity.get();
            // 객체 수정하면 테이블 내 레코드 같이 수정
            leaveRequestEntity.setLrq_st( leaveRequestDto.getLrq_st());
            leaveRequestEntity.setLrq_end( leaveRequestDto.getLrq_end());
            leaveRequestEntity.setLrq_srtype( leaveRequestDto.getLrq_srtype());
            leaveRequestEntity.setLrq_type( leaveRequestDto.getLrq_type());
            return true;
        }
        return false;
    }
    // 4
    @Transactional
    public boolean lrqDelete(int lrq_no){
       // 1. 엔티티 호출
        Optional<LeaveRequestEntity> optionalLeaveRequestEntity = leaveRequestRepository.findById( lrq_no );
        // 2. 엔티티가 호출되었는지 확인
        if(optionalLeaveRequestEntity.isPresent()){
            //3. 삭제
            leaveRequestRepository.deleteById(lrq_no);
            return true;
        }
        return false;
    }

}
