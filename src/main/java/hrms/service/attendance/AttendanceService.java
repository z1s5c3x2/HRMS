package hrms.service.attendance;

import hrms.model.dto.AttendanceDto;
import hrms.model.dto.PageDto;
import hrms.model.entity.AttendanceEntity;
import hrms.model.entity.EmployeeEntity;
import hrms.model.entity.LeaveRequestEntity;
import hrms.model.repository.AttendanceEntityRepository;
import hrms.model.repository.EmployeeEntityRepository;
import hrms.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Callable;
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
    //전사원 근무 현황
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
                                _res.setAttdRes(convertAttdToRes(_at.getAttdWrst(), _at.getAttdWrend(),formatter,_at.getEmpNo().getLeaveRequestEntities()));
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
        List<LeaveRequestEntity> leaveRequestEntities = optionalEmployeeEntity.get().getLeaveRequestEntities();
        if (optionalEmployeeEntity.isPresent()) {
            List<AttendanceEntity> attendanceEntities = attendanceRepository.findAllByEmpNoAndAttdWrstBetween(optionalEmployeeEntity.get(), startDateTime.format(formatter), endDateTime.format(formatter));
            System.out.println(attendanceEntities);

            return attendanceEntities.stream().map((attd) -> {
                return AttendanceDto.builder()
                        .attdWrst(attd.getAttdWrst())
                        .attdWrend(attd.getAttdWrend())
                        .attdRes(convertAttdToRes(attd.getAttdWrst(), attd.getAttdWrend(), formatter,leaveRequestEntities)).build();
            }).collect(Collectors.toList());
        }
        return null;

    }
    @Transactional
    public List<AttendanceDto> getlrqChart(String empNo, int year,int month) {
        System.out.println("empNo = " + empNo + ", year = " + year + ", month = " + month);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo(empNo);
        System.out.println("endDateTime = " + endDateTime);

        if (optionalEmployeeEntity.isPresent()) {
            LocalDateTime currentDate = yearMonth.atDay(1).atStartOfDay();
            while (!currentDate.isAfter(endDateTime)) {

                //System.out.println(currentDate.format(formatter));

                currentDate = currentDate.plusDays(1);
            }
        }


        return null;
    }
    private final static String[] ATTD_RES_STRING = new String[]{"X","휴직","연차","병가"};
    @Transactional
    public String convertAttdToRes(String start,String end,DateTimeFormatter ft,List<LeaveRequestEntity> leaveRequestEntities)
    {
        if(start == null || end == null){
            return "결석";
        }
        LocalDateTime _empAttdStart = LocalDateTime.parse(start,ft);
        LocalDateTime _empAttdEnd = LocalDateTime.parse(end,ft);
        LocalDateTime _nowStart =  _empAttdStart.withHour(9).withMinute(0).withSecond(0);
        LocalDateTime _nowEnd =  _empAttdStart.withHour(18).withMinute(0).withSecond(0);
        for(int i=0;i<leaveRequestEntities.size();i++){
            if( (_empAttdStart.isEqual(leaveRequestEntities.get(i).getLrqSt().atStartOfDay() ) ||
                    _empAttdStart.isAfter(leaveRequestEntities.get(i).getLrqSt().atStartOfDay() )) &&
                    (_empAttdEnd.isEqual(leaveRequestEntities.get(i).getLrqEnd().atTime(LocalTime.MAX)) ||
                    _empAttdEnd.isBefore(leaveRequestEntities.get(i).getLrqEnd().atTime(LocalTime.MAX)))) {
                return ATTD_RES_STRING[leaveRequestEntities.get(i).getLrqType()];
            }
        }
        if(_empAttdStart.isAfter(_nowStart))
        {
            return "지각";
        } else if (_empAttdEnd.isBefore(_nowEnd)) {
            return "조퇴";
        }
        return "출근";

    }

}
