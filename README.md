# 나의 마음 일지
🔗 https://play.google.com/store/apps/details?id=com.jys.mymindnotes

# 앱 설계 및 구조
## 모듈화 방식 선정

## Why? 
다음과 같은 이점을 얻기 위해 "나의 마음 일지" 모듈화를 진행하였다.

+ **안전하고 손쉬운 의존성 관리**: 의존성 규칙을 준수할 수 있도록 도와준다.
  + 멀티 모듈 구조를 사용하면, 한 모듈의 build.gradle 파일에 다른 모듈에 대한 의존성을 추가하지 않는 한 다른 모듈의 코드를 사용할 수 없기 때문에, Monolith 구조에서 쉽게 할 수 있는 의존성 규칙 위반 실수를 방지해 준다.

+ **관심사 분리(Seperation of Concerns)**: 모듈화는 코드를 특정 관심사 또는 기능에 따라 여러 모듈로 나누기 때문에, 관심사 분리를 실현한다. 그에 따른 이점들은 다음과 같다.
  + **코드 가독성 향상**: 코드를 이해하기가 더 쉬워진다.
    + 각 모듈이 하는 일이 명확하기 때문에, 코드를 이해하거나 변경하기가 더 쉬워진다.

  + **유지 관리성 및 확장성 향상**: 전체적인 유지 관리성이 향상된다.
    + 각 모듈은 독립적으로 작동하기 때문에, 한 모듈에서 문제가 발생하더라도 다른 모듈들(전체 시스템)에 영향을 미치는 것을 최소화할 수 있다. 
    + 한 모듈에서 문제가 발생하거나 변경이 필요할 때, 다른 모듈에 영향을 미치지 않고 보다 안전하게 수정할 수 있다.

  + **재사용성 향상**: 특정 모듈이 제공하는 기능을 재사용할 수 있다.
    + 어떤 모듈의 기능이 필요할 때 해당 모듈에 대한 의존성을 추가해서 사용하면 되기 때문에, 재사용성이 높아진다.
    
  + **테스트 용이성 향상**: 각 모듈을 독립적으로 테스트할 수 있다.
    + 각 모듈을 독립적으로 테스트할 수 있기 때문에, 테스트의 복잡성이 줄어든다.

  + **빌드 시간 개선**: 빌드 및 실행 시간을 줄일 수 있다.
    + 변경이 없는 모듈은 다시 빌드할 필요가 없고 변경된 모듈만 빌드하므로, 빌드 시간을 단축시킬 수 있다.

## How?
여러 가지 모듈화 방식에 대해서 검토하고, 그 중 "나의 마음 일지"에 가장 적합한 모듈화 방식을 선택하였다. 

### 모듈화 방식
#### 1. 계층별 모듈화(Modularization by Layer)
안드로이드 아키텍처의 각 레이어(data, domain, presentation)를 기반으로 모듈화한다.
+ 장점: 아키텍처 레이어는 각자 잘 정의된 책임을 가지고 시스템으로 작동하기 때문에, 응집력이 있다. 응집력이 좋다는 건, 각 모듈이 담당하는 기능이 명확하고, 그 기능에만 초점을 맞추어 구현되어 있기 때문에, 코드의 가독성, 재사용성, 유지 보수성이 좋다는 뜻이다.
+ 단점: 코드베이스(애플리케이션을 구성하는 모든 소스 코드의 집합)이 커질수록 모듈간의 결합도가 증가할 수 있기 때문에, 모듈의 세분화를 고려해야 한다.
  + 하지만 "나의 마음 일지"의 경우 코드베이스가 크지 않고, 향후 코드베이스가 큰폭으로 증가할 가능성은 적기 때문에, 계층 모듈화의 단점을 고려하지 않아도 된다.    

![image](https://github.com/jyslois/Projects/assets/106723882/c0dae96f-cc01-4817-a895-cb14258ef8d2)


#### 2. 기능 계층별 모듈화(Modularization by Feature & Layer)
화면에 해당하는 독립적인 앱 기능별로 모듈을 나누고, 각 기능 모듈을 다시 아키텍처의 레이어별로 나누어 모듈화한다. 
+ 장점: 모듈이 응집력이 있으면서도 각각이 독립적이기 때문에, 느슨하게 결합되어 있음으로 보장한다. 코드베이스가 쉽게 확장될 수 있고, 모듈이 세분화되어 있어 쉽게 재사용될 수 있다.
+ 단점: 모듈이 고도로 세분화되어 있기 때문에 오버헤드가 가중될 수 있다. 모듈이 너무 많아서 빌드 구성의 복잡성이 증가하기 때문이다. 
  + "나의 마음 일지"의 경우 코드베이스가 크지 않고 향후 코드베이스가 큰 폭으로 증가할 가능성도 낮기 때문에, 기능 계층별 모듈화를 사용할 경우 모듈화가 가지는 확장성 및 빌드 시간을 향상의 장점을 누리지 못하고, 오히려 오버헤드로 인한 성능 저하와 개발의 복잡성만 증가시킬 수 있다.

![image](https://github.com/jyslois/Projects/assets/106723882/2ae4e563-7cda-4a8c-89f3-5289f35e92c1)

#### 3. Now In Android 앱 모듈화
크게 기능별 모듈들과, 모든 모듈들에서 공통적으로 사용되는 핵심 모듈들로 나누어 모듈화한다. 
+ 장점: UI 관련 코드들은 기능별 모듈들로 나누고, 나머지는 핵심 모듈들로 나누었기 때문에, 응집력과 독립성은 제공한다.
+ 단점: 기능 계층별 모듈화보다는 덜 복잡하지만, 코드베이스가 작은 앱의 경우 여전히 모듈 세분화로 인한 오버헤드가 발생하여, 모듈화로 얻는 이점이 감소할 수 있다.

![image](https://github.com/jyslois/Projects/assets/106723882/e5d655e5-bea5-4bc0-99cf-84c0758cb910)


## Which?
### 판단 기준: 세분화와 오버헤드

안드로이드 앱을 세분화(granularity)하면, 여러 가지 오버헤드가 증가할 수 있다. 
+ 빌드 오버헤드: 그레이들의 증분 빌드 기능은 변경된 모듈만 다시 빌드하는 것이 가능해져 빌드 시간을 단축시킬 수 있다는 장점이 있지만, 모듈의 수가 과도하게 많아지면 모듈 간의 의존성 관리나 테스트 등의 시간이 증가해서 빌드 시간이 오히려 늘어날 수 있다.
+ 관리 오버헤드: 모듈이 많아지면 각 모듈에 대한 관리가 복잡해질 수 있다. 각 모듈의 버전 관리, 테스트, 문서화 등에 추가적인 시간과 노력이 필요해진다.
+ 통합 오버헤드: 모듈 간의 상호작용을 설정하고 관리하는 데 시간과 노력이 필요하다. 모듈 간의 인터페이스와 데이터 교환을 위한 코드를 작성하고 테스트해야 한다.
+ 의존성 오버헤드: 각 모듈이 독립적으로 유지되기 때문에, 각 모듈의 의존성을 관리하는 것이 더 복잡해질 수 있다. 예를 들어 여러 모듈이 동일한 라이브러리를 사용하는 경우, 해당 라이브러리의 버전을 일관되게 유지해야 할 필요가 있다. 

따라서, 안드로이드 앱을 세분화할 때는 이러한 오버헤드를 고려해야 한다. 앱의 복잡성에 따라 적절한 모듈화 수준을 결정해야 한다.

### 모듈화 선택 : 계층별 모듈화
**"나의 마음 일지" 앱의 가장 적합한 모듈화 방법은 계층별 모듈화(Modularization by Layer)이다.**
+ "나의 마음 일지"는 코드베이스가 크지 않은 앱이고, 향후 코드베이스가 크게 증가할 가능성도 낮다.
+ 따라서 모듈화 방법 중에 세분화가 가장 적은 계층별 모듈화 방법을 선택하는 것이 가장 적절하다.
+ 그렇게 함으로써 "나의 마음 일지"는 모듈화가 주는 이점들(보다 안전하고 쉬운 의존성 관리, 관심사 분리 등)을 누리는 방면, 지나친 세분화(너무 많은 모듈들)로 인해 발생하는 오버헤드 가중과 복잡성을 피할 수 있을 것이다.


## 아키텍처 및 의존성 방향 선정
클린 아키텍처(Clean Architecture)의 의존성 방향은 **Presentation -> Domain <- Data**인 반면, Android Developers가 권장하는 앱 아키텍처의 의존성 방향은 **Presentation -> Domain -> Data**이다. 
계층별 모듈화(Modularization by Layer)을 진행하려면, 어떤 아키텍처(와 의존성 방향)을 적용해야 할지 결정해야 한다.

### 아키텍처의 설계 원칙과 목표의 차이에 따른 의존성 방향
#### Android Developers 권장 앱 아키텍처
Android Developers가 권장하는 앱 아키텍처(이하 권장 아키텍처)는 UI-centric 설계를 기반으로 UI와 사용자 경험에 맞춘 설계 방식이며, UI 계층에서 시작하여 데이터 계층으로 내려가는 데이터 흐름을 갖는다.
+ 주된 목표: 사용자 경험의 최적화와 테스트 용이성.
+ trade-off: 안드로이드 플랫폼의 특성과 API에 의존함으로, 다른 플랫폼으로의 이식성이 제한적일 수 있다.

#### 클린 아키텍처(Clean Architecture)
Clean Architecture는 도메인(비즈니스 로직)을 중심으로 설계된다. 모든 것은 도메인 계층에서 시작하여 외부로 향하며, 모든 계층은 도메인 계층에 의존한다.
+ 주된 목표: 비즈니스 로직의 격리와 이식성. 이를 통해 비즈니스 로직은 UI나 데이터베이스 등 외부의 세부 사항에 영향받지 않고 유지될 수 있으며, 플랫폼에 독립적으로 비즈니스 로직을 적용할 수 있다.
+ trade-off: 구현이 복잡해진다.

### 아키텍처 선택 : Android Developers 권장 앱 아키텍처
"나의 마음 일지" 앱에 더 적합한 아키텍처는 **Android Developers가 권장하는 앱 아키텍처**이다.
+ 현재 "나의 마음 일지"는 **다양한 플랫폼에 서비스를 제공하는 목표가 없다**. 따라서 클린 아키텍처가 주는 이점인 비즈니스 로직의 격리와 이식성은 크게 필요하지 않는다.
+ 그러므로 권장 앱 아키텍처의 trade-off인 이식성 제한도 문제가 되지 않는다.
+ 반면, "나의 마음 일지"는 **사용자 경험이 중심인 일기 앱**으로, 사용자의 입력에 따라 UI가 동적으로 변화하는 것이 중요하며, 적어도 이러한 기능이 지속적으로 발전할 가능성이 높다. 이런 관점에서, 권장 아키텍처가 주는 다음의 장점들은 이 앱에 유익하다.
  + 권장 아키텍처는 **UI-centric**이며, **UI 계층에서 시작해서 데이터 계층으로 내려가는 데이터 흐름이 직관**적이다. 즉, 사용자 인터페이스에서 발생하는 이벤트가 어떻게 데이터를 생성하고, 변화시키는지를 명확하게 이해하고 추적할 수 있다는 것을 의미한다. 이렇게 데이터 흐름이 직관적이기 때문에:
    + **코드의 가독성이 좋아진다**: 코드를 이해하고 디버깅하기 쉽다. 개발자가 어떤 상태 변화가 어디서부터 시작되고 어떻게 전달되는지 명확하게 알 수 있기 때문에, 코드를 읽고 이해하거나 문제를 파악하고 해결하는 것이 용이해진다. 
    + **코드의 유지보수성이 향상된다**: 코드 수정과 기능 추가가 쉬워진다. 데이터 흐름이 명확하므로, 기존 코드를 수정하거나 새로운 기능을 추가할 때 어떤 부분을 건드려야 할지, 그리고 그로 인한 사이드 이펙트가 어디서 발생할 수 있는지 예측하기 쉬워진다. 
   + 이 두 가지의 장점은 권장 아키텍처의 주된 목표인 **사용자 경험의 최적화와 테스트 용이성**과도 이어진다. 사용자의 행동과 그 결과에 따른 앱의 반응을 빠르고 정확하게 이해하고 수정할 수 있으므로, 사용자 경험을 효과적으로 최적화할 수 있다. 또한 데이터의 흐름이 명확하고 예측 가능하므로, 각 단계에서의 데이터 상태를 확인하며 테스트를 진행하거나, 특정 UI 이벤트에 대한 결과를 정확히 예측하고 검증할 수 있다. 이는 애플리케이션의 안정성을 높이고 버그를 빠르게 찾아내는데 도움이 된다.

**따라서 "나의 마음 일지"는 Android Developers가 권장하는 앱 아키텍처에 따라 계층별 묘듈화(Modularization by Layer)를 하여, 의존성 방향을 Presentation -> Domain -> Data로 설정하기로 결정했다**


## 최종 설계 및 구조
## Android Developers 권장 앱 아키텍처에 따른 계층 모듈화 (Modularization by Layer) 
Android Developers가 권장하는 앱 아키텍처의 의존성 방향(**Presentation -> Domain -> Data**)를 따르는 아키텍처 계층을 기반으로 모듈화했다.

![image](https://github.com/jyslois/Projects/assets/106723882/1b81b1ff-c7a7-462d-b0a6-c19968a479aa)

+ **app module**: 앱 구조와 앱 수준의 설정을 담당한다. 앱의 초기화, 구성 및 설정과 같은 앱 전반적인 기능을 처리하고, 다른 모듈들을 연결하여 앱의 동작을 조정한다.
  + 의존성
    + 모듈: 모든 모듈들(core, presentation, domain, data)
    + Hilt, Retrofit2
  + 구성 
    +  AndroidManifest.xml(user-permission, HiltApplication, BroadCastReceiver 설정 등)
    +  Hilt: HiltApplication(@HiltAndroidApp), Hilt Modules

+ **core module**: 여러 모듈 간에 공유되는 핵심 기능과 공통 유틸리티 기능을 포함한다. 다른 모듈들에서 공통으로 사용되는 기능들을 중앙 집중화하여 관리한다.  
  + 의존성
    + Retrofit2, Gson Converter, OkHttp3, Logging-Interceptor, Hilt
  + 구성: 하나 이상의 모듈에서 공통적으로 사용되는 코드들
    + Hilt: CoroutineDispatcherModules, CoroutineScopeModules, SharedPreferencesModule
    + Model: UserDiary
    + Network: RetrofitService

+ **presentation module**: 사용자 인터페이스(UI)와 관련된 부분을 담당한다. 
  + 의존성
    + 모듈: domain module, core module
    + Hilt, Coroutine, Glide, Gson
  + 구성
    + AndroidManifest.xml: Activity 등록
    + ui
    + viewModels
    + alarm classes(AlarmReceiver, AlarmManagerHelper, NotificationHelper, AlarmBootReceiver)

+ **domain module**: 앱의 핵심 비즈니스 로직을 담당한다. presentation 모듈과 data 모듈 사이에서 중개자 역할을 한다.
  + 의존성
    + 모듈: data module, core module
    + Hilt
  + 구성
    + Usecases
    + AlarmInterface: AlarmManagerHelperInterface
  
+ **data module**: 데이터 관리와 관련된 기능들을 포함한다. 로컬 데이터베이스나 서버와의 통신을 담당하고, 데이터 소스를 제공하고 처리한다. 
  + 의존성
    + 모듈: core module
    + Retrofit2, Hilt
  + 구성
    + RepositoryInterfaces
    + RepostiryImpls
    + DataSources
    + Retrofit(apis and models)
