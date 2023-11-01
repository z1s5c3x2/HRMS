package hrms.service.Salary;


import hrms.model.dto.SalaryDto;
import hrms.model.entity.SalaryEntity;
import hrms.model.repository.SalaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {
    @Autowired
    private SalaryRepository salaryRepository;

    @Transactional
    public boolean slryWrite( SalaryDto salaryDto ){
        SalaryEntity salaryEntity
                = salaryRepository.save( salaryDto.saveToEntity());
        // 2.
        if( salaryEntity.getSlryNo() >= 1){ return true;} return false;
    }

    @Transactional
    public List<SalaryDto> slryGetAll(){
        // 1. 모두 출력
        List<SalaryEntity> salaryEntities = salaryRepository.findAll();
        List<SalaryDto> salaryDtos = new ArrayList<>();
        salaryEntities.forEach( e ->{ salaryDtos.add( e.allToDto()); });
        return salaryDtos;
    }

    // 3.

    public boolean slryUpdate( SalaryDto salaryDto ){
        // 수정할 엔티티 찾기
        Optional<SalaryEntity> optionalSalaryEntity = salaryRepository.findById( salaryDto.getSlryNo() );
        // 수정할 엔티티 존재하면?
        if(optionalSalaryEntity.isPresent()){
            // 엔티티 꺼내기
            SalaryEntity salaryEntity = optionalSalaryEntity.get();
            // 객체 수정하면 테이블 내 레코드 같이 수정
            salaryEntity.setSlryDate( salaryDto.getSlryDate());
            salaryEntity.setSlryPay( salaryDto.getSlryPay());
            salaryEntity.setSlryType( salaryDto.getSlryType());
            return true;
        }
        return false;
    }
    // 4

    public boolean slryDelete(int slryNo){
        // 1. 엔티티 호출
        Optional<SalaryEntity> optionalSalaryEntity = salaryRepository.findById( slryNo );
        // 2. 엔티티가 호출되었는지 확인
        if(optionalSalaryEntity.isPresent()){
            //3. 삭제
            salaryRepository.deleteById(slryNo);
            return true;
        }
        return false;
    }
}
