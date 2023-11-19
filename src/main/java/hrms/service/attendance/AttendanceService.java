package hrms.service.attendance;

import hrms.model.dto.AttendanceDto;
import hrms.model.dto.PageDto;
import hrms.model.entity.AttendanceEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.repository.AttendanceEntityRepository;
import hrms.model.repository.EmployeeEntityRepository;
import hrms.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired private AttendanceEntityRepository attendanceRepository;
    @Autowired private EmployeeEntityRepository employeeEntityRepository;
    @Autowired private SecurityService securityService;
    @Transactional
    public boolean setAttendanceGoWork(AttendanceDto attendDto) {
        Optional<EmployeeEntity> employeeEntity = employeeEntityRepository.findByEmpNo(attendDto.getEmpNo());
        AttendanceEntity attendanceEntity = attendanceRepository.save(attendDto.toEntity());
        attendanceEntity.setEmpNo(employeeEntity.get());
        employeeEntity.get().getAttendanceEntities().add(attendanceEntity);
        if(attendanceEntity.getAttdNo() > 0) {return true;}
        return false;
    }
    @Transactional
    public boolean setAttendanceLeaveWork(@RequestBody AttendanceDto attendDto) {
        // pk(empNo)와  오늘날짜의 출근정보 검색
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo(securityService.getEmp().getEmpNo());


        return false;
        
    }

    @Transactional
    public PageDto<AttendanceDto> allEmpAttdList(String start, String end, int page, int dptmNo, int empRk)
    {
        try{
            System.out.println("start = " + start + ", end = " + end + ", page = " + page + ", dptmNo = " + dptmNo + ", empRk = " + empRk);
            Pageable pageable = PageRequest.of(page-1,10); //현재 페이지와 한 페이지에 보여줄 데이터 수 설정
            Page<AttendanceEntity> _page = attendanceRepository.findAllByAttdWrstBetween(start,end,pageable);
            PageDto<AttendanceDto> result = PageDto.<AttendanceDto>builder()
                    .totalCount(_page.getTotalElements())
                    .totalPages(_page.getTotalPages())
                    .someList(_page.stream()
                            .filter(att -> (dptmNo == 0 || att.getEmpNo().getDptmNo().getDptmNo() == dptmNo)
                                    && (empRk == 0 || att.getEmpNo().getEmpRk() == empRk))
                            .map(AttendanceEntity::allEmpListDto)
                            .collect(Collectors.toList()))
                    .build();
            return result;
        }catch(Exception e) {
            System.out.println("allEmpAttdList" + e);
        }
        return null;
    }

}
