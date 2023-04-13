package com.android.sowon.hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp // 종속성 주입을 위해 Hilt를 사용하는 애플리케이션임을 나타냄
/*
@HiltAndroidApp 주석은 종속성 주입을 위한 진입점 역할을 하는 BaseApplication 클래스와 같이 Hilt가 작동하는 데 필요한 코드를 생성하는 데 사용된다.
@HiltAndroidApp 주석은 Dagger/Hilt를 사용하여 종속성 주입 컨테이너를 만드는 애플리케이션의 기본 클래스를 생성한다.
개발자는 이 주석을 사용하여 Hilt가 애플리케이션 수준 종속성 주입을 스스로 처리하게 함으로써, 필요한 상용구 코드의 양을 줄일 수 있다.
 */
class HiltApplication: Application() {
}

/*
전역 애플리케이션 상태를 유지하기 위한 기본 클래스인 Application을 확장.
Application 클래스를 사용하는 이유는 앱 수명 주기에서 앱 수명 주기 동안 존재해야 하는 다양한 구성 요소를 구성하고 초기화할 수 있는 단일 중앙 지점이기 때문이다.
Application을 확장하고 @HiltAndroidApp으로 주석을 추가하면 Hilt는 활동, 프래그먼트 및 ViewModel을 포함한 앱의 모든 구성 요소에 대한 종속성을 생성하고 주입할 수 있기 때문에, 앱의 수명 주기 동안 이러한 종속성을 수동으로 생성하고 관리할 필요가 없다.
 */