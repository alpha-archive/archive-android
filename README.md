# 📚 Archive - 나만의 활동 기록 & 추천 서비스 (Android)

> 사용자의 활동을 기록하고 활동을 추천받을 수 있는 안드로이드 앱

<br/>

## 📋 프로젝트 개요

**Archive**는 사용자가 일상 속 다양한 활동(여행, 운동, 문화생활 등)을 기록하고, 활동을 추천받을 수 있는 서비스의 **Android 클라이언트**입니다.

### 프로젝트 선정 이유
- 바쁜 일상 속에서 자신이 어떤 활동을 했는지 기억하기 어려움
- 새로운 활동을 찾고 싶지만 어디서부터 시작해야 할지 모르는 사용자 니즈
- 챗봇을 통한 자연스러운 활동 추천 경험 제공

### 협업 방식
- **GitHub Issues & PR**을 통한 코드 리뷰 및 기능 단위 개발
- **Feature Branch** 전략으로 기능별 독립적인 개발 진행

<br/>

## 🛠 기술 스택

| 분류 | 기술 |
|:---:|:---|
| **Language** | ![Kotlin](https://img.shields.io/badge/Kotlin_2.0-7F52FF?style=flat-square&logo=kotlin&logoColor=white) |
| **UI** | ![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?style=flat-square&logo=jetpackcompose&logoColor=white) ![Material3](https://img.shields.io/badge/Material_Design_3-757575?style=flat-square&logo=materialdesign&logoColor=white) |
| **Architecture** | ![MVVM](https://img.shields.io/badge/MVVM-FF6F00?style=flat-square) ![Clean Architecture](https://img.shields.io/badge/Clean_Architecture-00C853?style=flat-square) |
| **DI** | ![Hilt](https://img.shields.io/badge/Hilt-FF6F00?style=flat-square&logo=android&logoColor=white) |
| **Network** | ![Retrofit](https://img.shields.io/badge/Retrofit_3.0-48B983?style=flat-square) ![Gson](https://img.shields.io/badge/Gson-4285F4?style=flat-square) |
| **Async** | ![Coroutines](https://img.shields.io/badge/Coroutines-7F52FF?style=flat-square&logo=kotlin&logoColor=white) ![Flow](https://img.shields.io/badge/StateFlow-7F52FF?style=flat-square) |
| **Image** | ![Coil](https://img.shields.io/badge/Coil-4285F4?style=flat-square) |
| **Auth** | ![Kakao](https://img.shields.io/badge/Kakao_Login-FEE500?style=flat-square&logo=kakao&logoColor=black) |
| **Firebase** | ![Firebase](https://img.shields.io/badge/Analytics-FFCA28?style=flat-square&logo=firebase&logoColor=black) ![Crashlytics](https://img.shields.io/badge/Crashlytics-FFCA28?style=flat-square&logo=firebase&logoColor=black) ![Remote Config](https://img.shields.io/badge/Remote_Config-FFCA28?style=flat-square&logo=firebase&logoColor=black) |
| **기타** | ![Paging3](https://img.shields.io/badge/Paging_3-4285F4?style=flat-square) ![Navigation](https://img.shields.io/badge/Navigation_Compose-4285F4?style=flat-square) |

<br/>

## 🏛 아키텍처

### 전체 아키텍처

```
┌─────────────────────────────────────────────────────────────────┐
│                         Presentation Layer                      │
│  ┌──────────────────┐  ┌──────────────────┐  ┌───────────────┐  │
│  │   Compose UI     │  │    ViewModel     │  │   UI State    │  │
│  │   (Screen)       │◄─│  (StateFlow)     │──│               │  │
│  └──────────────────┘  └───────┬──────────┘  └───────────────┘  │
└────────────────────────────────┼────────────────────────────────┘
                                 │
┌────────────────────────────────┼────────────────────────────────┐
│                          Domain Layer                           │
│  ┌──────────────────┐  ┌───────┴──────────┐                     │
│  │   Repository     │  │    UseCase       │                     │
│  │   (Interface)    │◄─│   (Optional)     │                     │
│  └──────────────────┘  └──────────────────┘                     │
└────────────────────────────────┬────────────────────────────────┘
                                 │
┌────────────────────────────────┼────────────────────────────────┐
│                           Data Layer                            │
│  ┌──────────────────┐  ┌───────┴──────────┐  ┌───────────────┐  │
│  │   Repository     │  │     Remote       │  │    Local      │  │
│  │   (Impl)         │──│   (Retrofit)     │  │  (DataStore)  │  │
│  └──────────────────┘  └──────────────────┘  └───────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

### 패키지 구조

```
com.alpha.archive/
├── core/                    # 공통 모듈
│   ├── network/            # Retrofit, API 에러 처리, 인터셉터
│   ├── repository/         # Base Repository
│   ├── storage/            # Token 관리 (DataStore)
│   ├── ui/                 # 공통 UI 컴포넌트, 테마
│   ├── util/               # 유틸리티 함수
│   └── version/            # 앱 버전 관리
├── di/                      # Hilt 모듈
└── feature/                 # 기능별 모듈
    ├── intro/              # 로그인 화면
    │   ├── data/
    │   └── view/
    └── home/               # 메인 화면
        ├── recommend/      # 추천 기능
        ├── record/         # 기록 기능
        ├── chatbot/        # 챗봇
        ├── stats/          # 통계
        └── user/           # 사용자 설정
```

<br/>

## ✨ 주요 기능

### 1. 🔐 카카오 소셜 로그인
- 카카오 SDK를 활용한 간편 로그인
- 자동 로그인 / 토큰 갱신 처리
- Firebase Remote Config 기반 앱 버전 체크 및 강제 업데이트 안내

### 2. 💡 활동 추천
- 서버에서 제공하는 개인화 추천 목록 표시
- 카테고리별 필터링 (여행, 운동, 문화생활 등)
- 무한 스크롤을 통한 끊김 없는 UX

### 3. 📝 활동 기록
- 활동 제목, 장소, 카테고리, 날짜/시간, 이미지 등록
- 카카오 장소 API 연동 위치 검색
- Paging 3 기반 대용량 목록 처리
- 카테고리별 / 기간별 필터링

### 4. 🤖 챗봇
- 서버 챗봇 API 연동
- 추천 버튼을 통한 빠른 질문
- 로딩 인디케이터 표시

### 5. 📊 통계 대시보드
- 주간 / 월간 활동량 차트
- 캘린더 기반 월별 활동 현황
- 카테고리별 활동 비율 분석

<br/>

## 🎯 기여도와 역할

### Android 파트 구성
| 담당자 | 주요 업무 |
|:---:|:---|
| **이지상** | 프로젝트 설정, 인증, 추천, 챗봇, 통계, 공통 모듈 |
| **박성연** | 기록 상세/입력 페이지, 이미지 업로드, 리팩토링 |

### 담당 업무 상세

**이지상**
- **프로젝트 초기 설정**: 아키텍처 설계, Hilt DI 구성, 공통 모듈 개발
- **인증 플로우**: 카카오 로그인 연동, JWT 토큰 관리, 자동 로그인
- **추천 기능**: 추천 리스트, 상세, 필터링, 무한 스크롤
- **챗봇 기능**: 챗봇 UI 및 API 연동
- **통계 기능**: 주간/월간 차트, 캘린더, 카테고리별 통계
- **네트워크 레이어**: Retrofit 설정, API 에러 핸들링, Token Interceptor
- **버전 관리**: Firebase Remote Config 기반 앱 버전 체크
- **모니터링**: Firebase Crashlytics 연동

**박성연**
- **기록 상세 페이지**: 기록 상세 화면, 이미지 가로 스크롤, 다음 기록 이동
- **기록 입력 페이지**: 활동 기록 입력 UI 및 API 연동
- **이미지 업로드**: 이미지 업로드 API 연결
- **리팩토링**: RecordViewModel UiState 통일, BaseViewModel 공통화, UseCase 추가

<br/>

## 📈 결과 및 성과

### 기술적 성과
- **Jetpack Compose** 도입으로 선언적 UI 개발
- **Clean Architecture + MVVM** 패턴으로 유지보수성 향상
- **Paging 3** 적용으로 대용량 데이터 효율적 처리
- **Hilt** 기반 의존성 주입
- **StateFlow** 활용한 반응형 UI 상태 관리
- **Firebase Crashlytics** 연동으로 실시간 크래시 모니터링

### 문제 해결 경험
| 문제 | 해결 방법 |
|:---|:---|
| 카테고리 색상이 화면마다 랜덤하게 달라지는 문제 | `CategoryColorGenerator` 유틸 클래스 생성, 카테고리명의 해시값과 골든 앵글을 활용해 일관된 색상 생성 |
| 서버 응답의 Location 필드가 null일 때 앱 크래시 | DTO에서 nullable 처리 및 ViewModel에서 안전한 기본값 제공 |
| 추천 화면에서 필터와 목록 상태가 동기화되지 않는 문제 | ViewModel의 StateFlow를 단일 소스로 통합, UI에서 `collectAsStateWithLifecycle`로 수집 |
| 챗봇에서 기록 시 null 필드로 인한 오류 | 서버 응답 파싱 시 nullable 처리 및 기본값 적용 |
| 챗봇 키보드 표시 시 입력창이 가려지고 메시지 목록 레이아웃이 깨지는 문제 | `WindowInsets.ime`로 키보드 높이 계산, BottomBar 높이를 고려한 동적 패딩 적용, `reverseLayout`으로 최신 메시지 하단 고정 |

<br/>

## 🚀 향후 계획

- [ ] 오프라인 모드 지원 (Room DB 캐싱)
- [ ] 위젯을 통한 빠른 기록 등록
- [ ] 소셜 공유 기능
- [ ] 다크 모드 지원
- [ ] 앱 성능 최적화 (Baseline Profile)

<br/>

## 📚 배운 점

1. **Jetpack Compose**의 상태 관리와 Recomposition 최적화
2. **Clean Architecture** 적용을 통한 관심사 분리의 중요성
3. **Hilt**를 활용한 의존성 주입과 테스트 가능한 코드 작성
4. **Kotlin Coroutines & Flow**를 활용한 비동기 프로그래밍
5. **Firebase** 서비스 연동 및 앱 모니터링

<br/>

## 🔧 빌드 및 실행

### 요구사항
- Android Studio Ladybug 이상
- JDK 11
- Android SDK 26 (minSdk) ~ 36 (targetSdk)

### 설정
1. `local.properties`에 다음 값 추가:
```properties
serverUrl=YOUR_SERVER_URL
kakaoNaitiveStringAppKey=YOUR_KAKAO_NATIVE_KEY
kakaoAppKeyBuildConfig=YOUR_KAKAO_APP_KEY
```

2. Firebase 설정 파일 추가:
```
app/google-services.json
```

3. 빌드 및 실행:
```bash
./gradlew assembleDebug
```

<br/>

## 📄 라이선스

이 프로젝트는 국민대학교 알파프로젝트로 진행되었습니다.
