# 코틀린 기본 

## 기본 문법들
1. class 를 object 로 변경하면 싱글턴 으로 생성된다. 
2. 접근 제어자 
   1. java - private, protected, public, default
   2. kotlin - private, protected, public, internal
      1. internal 같은 모듈 안에서만 볼 수 있다. 
3. 함수내 함수 클로져가 가능하다. 
4. 확장함수 - 클래스에 정의된 인스턴스 함수인 것처럼 객체를 호출할 수 있는 함
5. Null이 되는 타입과 안되는 타입이 구별된다. 
   1. null 불가 : String 
   2. null 가능 : String?
6. ?. 역참조 연산을 할때 null을 체크한다. 
   1. !!. 을 하면 null일 경우 null point exception 발생 
7. ?: 엘비스 연산자로 Optional.getOrElse() 와 동일한 효과다. 
   1. var city: City = country?.city ?: City.unknown
8. if 문에서 중괄호를 생략시 반듯이 else 를 같이 써야 한다. 
   1. if 문 안에서 return 하는 내용을 피해야 한다. 
   2. if else 문에서는 값만 리턴한다. 상태를 변경하는 행위는 피한다.
9. for
   1. for (i in 0 until 10 step 2)
   2. for (i in 0..10)
10. 코틀린 에는 static 이 없다 대신 companion object 를 사용한다. 
11. 코틀린에서 모든 예외는 비검사 예외다. 
12. 지연 계산
13. 변성 --- 잘 이해되지 않음 
14. 코틀린의 함수는 수학에서의 함수와 비슷하다. -> 상태를 바꾸지 않고 결과값 만을 리턴하는 ... 
15. 동일성(메모리까지 비교) 동등성(값만 비교)
    1. JAVA == 는 동일성 비교 equals 보통 동등성 비교로 재정의됨
    2. KOTLIN == 는 동등성 비교 === 이 동일성 비교
16. 함수값 사용 val add: (Int, Int) -> Int = {x, y -> x + y}
    1. fun 함수는 메서드에 가깝다.
    2. 함수를 데이터 처럼 쓰려면 람다 표현 함수를 이용해라 
17. operator 연산자 오버라이딩 
18. sealed class
    1. 객체를 생성할 수 없다. 
    2. 동일한 파일에서만 상속할 수 있다. 다른 파일에서는 상속 불가 
    3. 하위 클래스는 class, data class, object class 등으로 정의 가능하다. 
19. open kotlin class 은 기본적으로 final 그럼으로 상속이 필요한 class 의 경우엔 open 키워드를 추가 해줘야 한다.
20. 코틀린은 변수만 선언 해도 get, set 은 자동 생성된다. 이를 제정의 하고 싶다면 아래처럼 한다. 
    1. val isNew: Boolean get() = this.id == null


## 안전한 프로그래밍
- 가변 변수 참조를 피하라 
- 부수효과(상태 변경과 결과 리턴을 동시에 하는 메서드 작성)를 피하라
- 가능하면 제어 구조를 사용하지 않는다. 
- 효과를 내가 작성하는 프로그램의 일부 역역 안에서만 일어나도록 하라 - 일부 한정된 영역을 제외한 곳에서 dB에 데이터를 쓰거나 하는 행위를 하지 마라 
- 타입을 검사하고 강제로 형변환 하는 코드는 왠만하면 쓰지 말자 
- 논외 지만 JAVA 에서 객체 동등성엔 equal, 프리미티브 동등성엔 == 를 사용해야 한다. == 는 메모리도 같이 봄으로 


## 용어들 
- Person.getName "." 은 역참조 연산이다. 
- 자기 완결적 
- 결정적 코드 