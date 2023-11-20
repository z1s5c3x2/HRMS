package hrms.config;

import hrms.controller.security.AuthLoginController;
import hrms.service.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    // 시큐리티 관련 메소드 커스텀
    // 1. 해당 클래스에 상속받기 extens WebSecurityConfigurerAdapter
    // 2. 커스텀 할 메소드 오버라이딩 하기 configure(HttpSecurity http)


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // super.configure(http);

        // 0. 인증(로그인)된 권환(허가) 통해 페이지 접근 제한  UserDetails 내 유저네임과 일치시켜야함
        http.authorizeHttpRequests()    // 1. 인증된
                //.antMatchers("/info").hasRole("인사")   // 인증된 권한중에 인사팀이면 HTTP 허용
                .antMatchers("/teamproject/teammember/print",
                        "/teamProject/teammember/write",
                        "/teamProject/teammember/update",
                        "/teamProject",
                        "/teamProject/update",
                        "/attendance",
                        "/attendance/dall",
                        "/attendance/leaveRequestlist",
                        "/employee/list",
                        "/employee/register",
                        "/employee/update",
                        "/employee/searchemp",
                        "/salary/list",
                        "/salary/write",
                        "/salary/view",
                        "/approval").hasAuthority("인사")
                .antMatchers("/teamproject/teammember/print").hasAuthority("개발")
                .antMatchers("/teamproject/teammember/print").hasAuthority("기획")
                /*.antMatchers("/teamproject/teammember/write").hasAuthority("인사")
                .antMatchers("/teamproject/teammember/update").hasAuthority("인사")
                .antMatchers("/teamproject").hasAuthority("인사")
                .antMatchers("/teamproject/listAll").hasAuthority("인사")
                .antMatchers("/teamproject/update").hasAuthority("인사")
                .antMatchers("/attendance").hasAuthority("인사")
                .antMatchers("/attendance/dall").hasAuthority("인사")
                .antMatchers("/attendance/leaveRequestlist").hasAuthority("인사")
                .antMatchers("/employee/list").hasAuthority("인사")
                .antMatchers("/employee/register").hasAuthority("인사")
                .antMatchers("/employee/update").hasAuthority("인사")
                .antMatchers("/employee/searchemp").hasAuthority("인사")
                .antMatchers("/salary/list").hasAuthority("인사")
                .antMatchers("/salary/write").hasAuthority("인사")
                .antMatchers("/salary/view").hasAuthority("인사")*/
                .antMatchers("/**").permitAll()     // 모든 페이지는 권한 모두 허용
                //.anyRequest().authenticated()     // 모든 페이지는 인증 필요
                .and()
                .exceptionHandling().accessDeniedHandler((httpServletRequest, httpServletResponse, e) -> {
                    //httpServletResponse.sendRedirect("/accessdenied");
                    httpServletResponse.setHeader("Refresh", "0; URL=/accessdenied");
                });
        // 1. 인증(로그인) 커스텀
        http.formLogin()                                    // 1. 시큐리티 로그인 사용[form전송]
                .loginPage("/member/Login")                        // 2. 시큐리티 로그인으로 사용할 VIEW 페이지 HTTP주소
                .loginProcessingUrl("/member/login")        // 3. 시큐리티 로그인(인증)처리 요청시 사용할 HTTP주소
                // HTTP '/member/login' POST 요청시 ---> MemberService의 loadUserByUsername으로 이동
                //.defaultSuccessUrl("/")                     // 4. 만약 로그인 성공하면 이동할 HTTP 주소
                //.failureUrl("/login")    // 5. 만약에 로그인 실패하면 이동할 HTTP 주소
                .usernameParameter("empNo")                // 6. 로그인시 입력받은 아이디의 변수명 정의
                .passwordParameter("empPwd")            // 7. 로그인시 입력받은 비밀번호의 변수명 정의
                .successHandler(authLoginController)       // 로그인 성공했을때 해당 클래스 매핑
                .failureHandler(authLoginController);      // 로그인 실패했을때 해당 클래스 매핑
        // 2. 로그아웃 커스텀[시큐리티 사용전에 컨트롤러와 서비스에 구현한 logout 관련 메소드 제거]
        http.logout()           // 1. 로그인(인증) 로그아웃 처림
                .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))         // 2. 로그아웃 처리할 HTTP 주소 정의
                .logoutSuccessUrl("/member/Login")             // 3. 로그아웃 성공했을때 이동할 HTTP주소
                .invalidateHttpSession(true);        // 4. 로그아웃 할때 http 세션 모두 초기화[ true : 초기화/ false : 초기화x]

        // 3. csrf 커스텀
        http.csrf().disable();  // ---- 모든 HTTP post,get에서 csrf 사용안함
        //http.csrf().ignoringAntMatchers("/member/post");   // 특정 HTTP에서만 csrf를 사용하지 않음

    }

    @Autowired
    private SecurityService securityService;

    @Autowired
    private AuthLoginController authLoginController;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // super.configure(auth);
        auth.userDetailsService(securityService).passwordEncoder( new BCryptPasswordEncoder() );
        // auth.userDetailsService( userDetailService 구현체 ).passwordEncoder( 사용할 암호화 객체 );
    }
}

