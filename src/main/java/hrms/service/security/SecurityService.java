package hrms.service.security;

import hrms.model.dto.EmployeeDto;
import hrms.model.entity.EmployeeEntity;
import hrms.model.repository.EmployeeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SecurityService implements UserDetailsService {

    @Autowired
    private EmployeeEntityRepository employeeEntityRepository;

    // --------------------------------------------------
    // 1. UserDetailsService 구현체
    // 2. 인증처리 해주는 메소드 구현
    // 3. loadUserByUsername 메소드는 무조건 UserDetails 객체를 반환해야한다
    // 4. UserDetails 객체를 이용한 패스워드 검증과 사용자 권한을 확인하는 메소드

    // @Autowired  // PasswordEncoder는 직접 컨테이너에 등록해야한다(객체 생성).
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // 9
    @Transactional
    public EmployeeDto getEmp(){
        // 시큐리티 사용할때는 일반 서블릿 세션이 아니고 시큐리티 저장소를 이용한다
        System.out.println( "시큐리티에 저장된 세션 정보 저장소 : " + SecurityContextHolder.getContext() );
        System.out.println( "시큐리티에 저장된 세션 정보 저장소에 저장된 인증 : " +
                SecurityContextHolder.getContext().getAuthentication() );
        System.out.println( "시큐리티에 저장된 세션 정보 저장소에 저장된 인증 호출 : " +
                SecurityContextHolder.getContext().getAuthentication().getPrincipal() );

        // 인증에 성공한 정보 호출[ 세션 호출 ]
        Object o = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 1. 만약에 인증결과가 실패이거나 없을때
        if(o.equals("anonymousUser")){return null;} // 로그인하지 않은 상태
        // 2. 인증결과에 저장된 UserDetails로 타입변환
        UserDetails userDetails = (UserDetails)o;   // 로그인 결과를 가지고 있는 객체

        Optional<EmployeeEntity> employeeEntityOptional = employeeEntityRepository.findByEmpNo(userDetails.getUsername());
        EmployeeEntity employeeEntity = employeeEntityOptional.get();

        return EmployeeDto.builder().empNo( userDetails.getUsername() ).empName(employeeEntity.getEmpName()).dptmNo(employeeEntity.getDptmNo().getDptmNo()).build();
    }


    @Override
    public UserDetails loadUserByUsername(String empNo) throws UsernameNotFoundException {
        // login 페이지에서 form을 통해 전송된 아이디를 받음(패스워드 없음)
        System.out.println("empNo = " + empNo);

        // 1. 사용자의 아이디만으로 사용자 정보를 로딩[불러오기]
        Optional<EmployeeEntity> employeeEntityOptional = employeeEntityRepository.findByEmpNo(empNo);
        EmployeeEntity employeeEntity = null;
        if(employeeEntityOptional.isPresent()){
            employeeEntity = employeeEntityOptional.get();
        }
        // 2. 없는 아이디 이면 (throw : 예외처리 던지기) - UsernameNotFoundException : username이 없을때 사용하는 예외 클래스
        if(employeeEntity == null){ throw new UsernameNotFoundException("없는 아이디입니다."); }

        // 2-1 권한 목록 추가
        List<GrantedAuthority> authList = new ArrayList<>();
        authList.add(new SimpleGrantedAuthority((employeeEntity.getDptmNo().getDptmName() ) ) );
        System.out.println("설정된 권한 = " + employeeEntity.getDptmNo().getDptmName());

        // 2-2 DTO 만들기
        EmployeeDto employeeDto = EmployeeDto.builder()
                .empNo(employeeEntity.getEmpNo())
                .empPwd(employeeEntity.getEmpPwd())
                .empName(employeeEntity.getEmpName())
                .authList(authList)
                .build();

        return employeeDto;
    }

}
