# Spring-Core-Principle

## 요약정리
1. SOLID
    (DIP: 인터페이스에 의존)
2. IoC 컨테이너 혹은 DI 컨테이너
    : 객체를 생성하고 관리하면서 의존관계를 연결해주는 것
3. Configuration과 Bean
    : 객체를 스프링 컨테이너에 등록
4. 스프링 컨테이너와 스프링 빈
    : ApplicationContext(스프링 컨테이너) = BeanFactory 기능(ApplicationContainer의 인터페이스) + 여러 부가 기능
      BeanDefinition을 통해 scope, lazyInit, factoryBeanName, factoryMethodName 등 알 수 O
5. 싱글톤 패턴
    : 클래스의 인스턴스가 딱 1개만 생성되는 것을 보장하는 디자인 패턴
    ```java
    public class SingletonService {
      //1. static 영역에 객체를 딱 1개만 생성
      private static final SingletonService instance = new SingletonService();

      //2. 이 static 메서드를 통해서만 조회하도록 허용
      public static SingletonService getInstance() {
          return instance;
      }

      //3. 생성자를 private으로 선언해서 외부에서 new 키워드를 사용한 객체 생성을 못하게 막는다. 
      private SingletonService() {}

      public void logic() { System.out.println("싱글톤 객체 로직 호출");
      } 
    }
    ```
    문제점:
      1. 테스트 어려움 -> 안티패턴
      2. 서버 환경에서는 1개만 생성된다는 보장이 X
      3. private 생성자로 상속이 힘듦
      4. 의존관계상 클라이언트가 구체 클래스에 의존. DIP, OCP 위반할 수 O
6. 싱글톤 컨테이너
    : 싱글톤 패턴의 문제점을 해결하면서, 객체 인스턴스를 싱글톤(1개만 생성)으로 관리하는 컨테이너
    (주의)필드 대신, 자바에서 공유되지 않는 지역변수, 파라미터, 스레드로컬 등을 사용해야 한다.
      →왜? 공유필드를 조심해야 한다.(동시성 문제)
       
     @Configuration 을 붙이면 바이트코드를 조작하는 CGLIB 기술을 사용해서 싱글톤을 보장!
     @Configuration을 빼고 @Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
7. 컴포넌트 스캔
    : 스프링은 설정 정보가 없어도 자동으로 스프링 빈을 등록하는 컴포넌트 스캔이라는 기능을 제공
    ```java
    @Configuration
    @ComponentScan(
            excludeFilters = @Filter(type = FilterType.ANNOTATION, classes =
    Configuration.class))
    public class AutoAppConfig {
    }
    ```
    스프링 빈이 등록될 때, 빈 이름이 같은 게 2개 있을 경우(자동빈 vs 자동빈), 
    ConflictingBeanDefinitionException 예외 발생 가능
8. 의존관계 주입 방식 4가지
    - 생성자 주입 방식
      : 생성자 호출 시점에 딱 한번만 호출되는 것이 보장(불변일 때 사용)
    - 수정자 주입 방식
      : 선택, 변경 가능성이 있는 관계에 사용한다.
    - 필드 주입 방식
      : 필드에 바로 주입(외부에서 변경이 불가능해서 테스트 어려움, DI 프레임워크가 없으면 아무것도 할 수 X)
    - 일반 메서드 주입 방식
      : 잘 사용 X
9. 빈 2개일 때 해결 방법
    1. @Qualifier 추가 구분자 옵션 하나 더 제공  
      원하는 mainDiscountPolicy를 못 찾으면 해당 이름의 빈을 추가로 찾는다.
      ```java
      @Component
      @Qualifier("mainDiscountPolicy")
      public class RateDiscountPolicy implements DiscountPolicy{
      }

      @Component
      public class OrderServiceImpl implements OrderService{

        private final MemberRepository memberRepository;
        private final DiscountPolicy discountPolicy;

        public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
            this.memberRepository = memberRepository;
            this.discountPolicy = discountPolicy;
        }
      }
      ```
    2. Primary 사용
      ```java
      @Component
      @Primary
      public class RateDiscountPolicy implements DiscountPolicy{
          private static  final  int DISCOUNT_PERCENT = 10;
      }
      ```
10. 빈 생명주기 콜백
    스프링 빈의 라이프사이클:  
    `스프링 컨테이너 생성 → 스프링 빈 생성 → 의존관계 주입 → 초기화 콜백 → 사용 → 소멸전 콜백 → 스프링 종료`
    <img width="739" alt="image" src="https://user-images.githubusercontent.com/46602874/174094954-0f0943d0-3ebf-44c1-b784-a57767d3e026.png">
    <img width="739" alt="image" src="https://user-images.githubusercontent.com/46602874/174095056-25918492-8610-448f-9aa7-657e6b76c906.png">
