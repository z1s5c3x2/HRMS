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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AttendanceService {

    @Autowired private AttendanceEntityRepository attendanceRepository;
    @Autowired private EmployeeEntityRepository employeeEntityRepository;
    @Autowired private SecurityService securityService;
    @Transactional
    public boolean setAttendanceGoWork(AttendanceDto attendDto) {
        Optional<EmployeeEntity> employeeEntity = employeeEntityRepository.findByEmpNo(securityService.getEmp().getEmpNo());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String workNow = LocalDateTime.now().format(formatter);
        AttendanceEntity attendanceEntity = AttendanceEntity.builder().empNo(employeeEntity.get()).attdWrst(workNow)
                .attdWrend(workNow).build();
         attendanceRepository.save(attendanceEntity);
        attendanceEntity.setEmpNo(employeeEntity.get());
        employeeEntity.get().getAttendanceEntities().add(attendanceEntity);
        if(attendanceEntity.getAttdNo() > 0) {return true;}
        return false;
    }
    @Transactional
    public boolean setAttendanceLeaveWork(@RequestBody AttendanceDto attendDto) {
        // pk(empNo)와  오늘날짜의 출근정보 검색
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo(securityService.getEmp().getEmpNo());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        attendDto.setAttdWrst(LocalDateTime.now().format(formatter));
        optionalEmployeeEntity.get().getAttendanceEntities().get(optionalEmployeeEntity.get().getAttendanceEntities().size()-1).setAttdWrend(LocalDateTime.now().format(formatter));
        return false;
        
    }
    //전사원 근무 현황
    @Transactional
    public PageDto<AttendanceDto> allEmpAttdList(String start, String end, int page, int dptmNo, int empRk,int keywordType,String keyword)
    {
        System.out.println("start = " + start + ", end = " + end + ", page = " + page + ", dptmNo = " + dptmNo + ", empRk = " + empRk + ", keywordType = " + keywordType + ", keyword = " + keyword);
        try{

            Pageable pageable = PageRequest.of(page-1,10, Sort.by(Sort.Order.desc("attdWrst"))); //현재 페이지와 한 페이지에 보여줄 데이터 수 설정
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
    public List<AttendanceDto> getMonthChart( int year,int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        System.out.println(startDateTime.format(formatter));
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo(securityService.getEmp().getEmpNo());
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
    public List<AttendanceDto> getlrqChart(int year,int month) {

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(23, 59, 59);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeEntityRepository.findByEmpNo(securityService.getEmp().getEmpNo());
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
    /*
    *  출퇴 시간 null이 있으면 임의 시간으로 전처리,
    *  날짜 기준으로 역정렬 
    *  개인 근무 현황 캘린더 삭제
    * */
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
        if(start.equals(end)){
            return "출근중";
        }
        else if(_empAttdStart.isAfter(_nowStart))
        {
            return "지각";
        } else if (_empAttdEnd.isBefore(_nowEnd)) {
            return "조퇴";
        }
        return "출근";

    }
    @Transactional
    public boolean checkMyWrok()
    {
        Optional<EmployeeEntity> optionalEmployeeEntity =  employeeEntityRepository.findByEmpNo(securityService.getEmp().getEmpNo());

        if(optionalEmployeeEntity.isPresent() && optionalEmployeeEntity.get().getAttendanceEntities().size() != 0)
        {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            AttendanceEntity attendanceEntity  = optionalEmployeeEntity.get().getAttendanceEntities().get(optionalEmployeeEntity.get().getAttendanceEntities().size()-1);
            System.out.println(LocalDate.parse(attendanceEntity.getAttdWrst().split(" ")[0],formatter) );
            if(attendanceEntity.getAttdWrend().equals(attendanceEntity.getAttdWrst()) && LocalDate.now().isEqual(LocalDate.parse(attendanceEntity.getAttdWrst().split(" ")[0],formatter) ) )
            {
                return true;
            }

        }
        return false;
    }
}
