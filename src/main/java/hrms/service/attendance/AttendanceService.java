package hrms.service.attendance;

import hrms.model.dto.AttendanceDto;
import hrms.model.dto.PageDto;
import hrms.model.entity.AttendanceEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.repository.AttendanceEntityRepository;
import hrms.model.repository.EmployeeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired private AttendanceEntityRepository attendanceRepository;
    @Autowired private EmployeeEntityRepository employeeEntityRepository;
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

        // Optional<AttendanceEntity> attendEntity = AttendanceEntityRepository.findById(attendDto.getEmpNo());
        //결과가 있으면 퇴근정보 update

        
        //결과 없으면  false반환
        
        // 해당 attpNo에
        return false;
        
    }

    @Transactional
    public PageDto<AttendanceDto> allEmpAttdList(String start, String end, int page, int dptmNo, int empRk,int keywordType,String keyword)
    {
        System.out.println("start = " + start + ", end = " + end + ", page = " + page + ", dptmNo = " + dptmNo + ", empRk = " + empRk + ", keywordType = " + keywordType + ", keyword = " + keyword);
        try{
            
            Pageable pageable = PageRequest.of(page-1,10); //현재 페이지와 한 페이지에 보여줄 데이터 수 설정
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            Page<AttendanceEntity> _page = attendanceRepository.findAllByAttdWrstBetween(start,end,pageable);
            PageDto<AttendanceDto> result = PageDto.<AttendanceDto>builder()
                    .totalCount(_page.getTotalElements())
                    .totalPages(_page.getTotalPages())
                    .someList(_page.stream()
                            .filter(att -> (dptmNo == 0 || att.getEmpNo().getDptmNo().getDptmNo() == dptmNo)
                                    && (empRk == 0 || att.getEmpNo().getEmpRk() == empRk)
                                &&(keywordType == 0 || (keywordType == 1 ? keyword.equals(att.getEmpNo().getEmpName()) : keyword.equals(att.getEmpNo().getEmpNo()))))
                            .map( _at -> {
                                AttendanceDto _res = _at.allEmpListDto();
                                _res.setAttdRes(convertAttdToRes(_at.getAttdWrst(), _at.getAttdWrend(),formatter));
                                return _res;
                            })
                            .collect(Collectors.toList()))
                    .build();
            System.out.println("result = " + result);
            return result;
        }catch(Exception e) {
            System.out.println("allEmpAttdList" + e);
        }
        return null;
    }

    //달력 근무현황,출결현황 작업
    @Transactional
    public List<AttendanceDto> getMonthChart(String empNo, int year,int month) {

        System.out.println("empNo = " + empNo + ", year = " + year + ", month = " + month);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(startDateTime.format(formatter));
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo(empNo);
        if (optionalEmployeeEntity.isPresent()) {
            List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByEmpNoAndAttdWrstBetween(optionalEmployeeEntity.get(), startDateTime.format(formatter), endDateTime.format(formatter));
            System.out.println(attendanceEntities);

            return attendanceEntities.stream().map((attd) -> {
                return AttendanceDto.builder()
                        .attdWrst(attd.getAttdWrst())
                        .attdWrend(attd.getAttdWrend())
                        .attdRes(convertAttdToRes(attd.getAttdWrst(), attd.getAttdWrend(), formatter)).build();
            }).collect(Collectors.toList());
        }
        return null;

    }

    @Transactional
    public String convertAttdToRes(String start,String end,DateTimeFormatter ft)
    {
        if(start == null || end == null){
            return "결석";
        }
        LocalDateTime _empAttdStart = LocalDateTime.parse(start,ft);
        LocalDateTime _empAttdEnd = LocalDateTime.parse(end,ft);
        LocalDateTime _nowStart =  _empAttdStart.withHour(9).withMinute(0).withSecond(0);
        LocalDateTime _nowEnd =  _empAttdStart.withHour(18).withMinute(0).withSecond(0);
        if(_empAttdStart.isAfter(_nowStart))
        {
            return "지각";
        } else if (_empAttdEnd.isBefore(_nowEnd)) {
            return "조퇴";
        }
        return "출근";

    }

}
