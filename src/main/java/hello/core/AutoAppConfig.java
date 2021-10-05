package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "hello.core.member", // 탐색 시작 위치! 안 붙이면 현재 위치 HEllo.core에서 시작
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class AutoAppConfig {

    // 수동, 자동 빈 중복 -> Test는 성공하지만 SpringBootApplication 부분 진행하면 에러
    // 에러 안 나게 하고 싶으면 properties에 spring.main.allow-bean-definition-overriding=true로 해야됨
    @Bean(name = "memoryMemberRepository")
    MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
