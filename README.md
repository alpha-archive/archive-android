# Archive 안드로이드 앱 개발 환경 구성 및 기술 구현

## 목차
1. [프로젝트 개요 및 서비스 구현 방향](#1-프로젝트-개요-및-서비스-구현-방향)
2. [아키텍처 설계 전략](#2-아키텍처-설계-전략)
3. [기술 스택 선정 및 사용 기술](#3-기술-스택-선정-및-사용-기술)
4. [의존성 주입 및 모듈화 전략](#4-의존성-주입-및-모듈화-전략)
5. [핵심 기능 구현](#5-핵심-기능-구현)
6. [보안 아키텍처 및 인증 체계](#6-보안-아키텍처-및-인증-체계)
7. [UI/UX 설계 및 Material Design 3](#7-uiux-설계-및-material-design-3)
8. [데이터 처리 및 최적화](#8-데이터-처리-및-최적화)
9. [최종 솔루션 및 서비스 구현 결과](#9-최종-솔루션-및-서비스-구현-결과)

---

## 1. 프로젝트 개요 및 서비스 구현 방향

### 1.1 서비스 비전 및 목표

Archive 안드로이드 앱은 사용자의 오프라인 활동을 디지털로 기록하고 관리하는 모바일 플랫폼입니다. AI 기반 대화형 입력, 공공 데이터 활용 추천, 데이터 시각화를 통해 사용자의 문화생활을 풍부하게 만듭니다.

**서비스의 핵심 차별점**:

1. **네이티브 모바일 경험**
   - Jetpack Compose를 활용한 현대적인 UI
   - Material Design 3 적용으로 일관된 디자인 언어
   - 반응형 레이아웃으로 다양한 화면 크기 지원

2. **AI 대화형 기록**
   - 챗봇 '아키'와 자연스러운 대화로 활동 기록
   - 복잡한 폼 입력 대신 자연어 처리
   - 백엔드 OpenAI API 연동

3. **실시간 데이터 시각화**
   - 주간 요일별 막대 그래프
   - 월간 캘린더 히트맵
   - 카테고리별 프로그레스 바 차트

4. **카카오 간편 로그인**
   - 복잡한 회원가입 절차 없음
   - OAuth 2.0 기반 안전한 인증
   - 사용자 정보 자동 동기화

5. **공공 데이터 활용**
   - 문화체육관광부, 서울시 등의 이벤트 정보 통합
   - 카테고리/위치 기반 필터링
   - 실시간 업데이트 제공

### 1.2 서비스 구현 흐름

```
[사용자 진입]
    ↓
[IntroActivity - 카카오 로그인]
    ↓
[백엔드 JWT 인증]
    ↓
[HomeActivity - 5개 탭 메인 화면]
    ↓
┌─────────┬─────────┬─────────┬─────────┬─────────┐
│  기록   │  추천   │  챗봇   │  통계   │ 프로필  │
└─────────┴─────────┴─────────┴─────────┴─────────┘
    ↓         ↓         ↓         ↓         ↓
[활동목록] [이벤트] [AI대화] [차트]  [설정]
```

**핵심 비즈니스 로직**:

- Jetpack Compose로 구현된 UI를 통해 사용자 상호작용
- Hilt를 통한 의존성 주입으로 ViewModel과 Repository 간 느슨한 결합
- Retrofit과 OkHttp를 통해 백엔드 REST API 통신
- AuthInterceptor가 토큰 갱신을 자동 처리
- Paging 3 라이브러리로 대량 데이터 효율적 로드
- StateFlow와 Kotlin Coroutines로 비동기 작업 안전 처리

### 1.3 개발 환경 및 요구사항

**필수 도구**:
- Android Studio Iguana (2023.2.1) 이상
- JDK 11
- Gradle 8.12
- Kotlin 2.0.21

**디바이스 요구사항**:
- minSdk: 26 (Android 8.0 Oreo)
- targetSdk: 36
- 권장: Android 12 이상 (Dynamic Color 지원)

**개발 환경 설정**:

`local.properties` 파일 필수 설정:
```properties
serverUrl=https://your-backend-api.com
kakaoNaitiveStringAppKey=your_kakao_native_key
kakaoAppKeyBuildConfig="your_kakao_app_key"
```

---

## 2. 아키텍처 설계 전략

### 2.1 Clean Architecture + MVVM 패턴

Archive 안드로이드 앱은 유지보수성, 테스트 가능성, 확장성을 최우선으로 고려하여 Clean Architecture와 MVVM 패턴을 결합했습니다.

**선택 이유**:

**1. 계층 분리를 통한 관심사 분리**
- Presentation Layer: UI와 ViewModel로 구성
- Domain Layer: Repository 인터페이스로 데이터 접근 추상화
- Data Layer: Repository 구현체와 API 통신
- 각 계층이 명확한 책임을 가지고 독립적으로 동작

**2. 단방향 데이터 흐름**
- UI는 ViewModel의 StateFlow를 관찰만 함
- 사용자 액션은 ViewModel 함수 호출로 전달
- ViewModel은 Repository를 통해 데이터 접근
- 예측 가능한 상태 관리로 버그 감소

**3. 테스트 용이성**
- 각 계층이 인터페이스를 통해 통신
- Mock 객체를 쉽게 주입 가능
- ViewModel은 순수 Kotlin 클래스로 Android 의존성 최소화
- Repository 패턴으로 데이터 소스 교체 용이

**4. 안드로이드 아키텍처 컴포넌트 활용**
- ViewModel: 생명주기 안전한 UI 상태 관리
- StateFlow: 반응형 UI 업데이트
- Kotlin Coroutines: 비동기 작업 간결 처리
- Hilt: 의존성 주입 자동화

### 2.2 레이어드 아키텍처 구조

```
┌─────────────────────────────────────────────────┐
│           Presentation Layer                    │
│   (Composable Functions + ViewModels)          │
│                                                 │
│   - @Composable: 선언적 UI 정의                 │
│   - ViewModel: UI 상태(StateFlow) 관리          │
│   - UiState: 불변 data class로 화면 상태 표현    │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│            Domain Layer                         │
│   (Repository Interfaces + Models)             │
│                                                 │
│   - Repository: 데이터 접근 추상화               │
│   - 순수 Kotlin 타입                            │
│   - Android Framework 의존성 없음               │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│            Data Layer                           │
│   (Repository Impl + API + DTO)                │
│                                                 │
│   - Retrofit API: 백엔드 통신                    │
│   - DTO: JSON 직렬화/역직렬화                    │
│   - TokenStore: SharedPreferences 래핑          │
│   - PagingSource: Paging 3 데이터 소스           │
└─────────────────────────────────────────────────┘
                      ↓
┌─────────────────────────────────────────────────┐
│         Infrastructure Layer                    │
│   (Network + Storage + External SDKs)          │
│                                                 │
│   - OkHttp/Retrofit: HTTP 클라이언트             │
│   - SharedPreferences: 영구 저장소               │
│   - Kakao SDK: 소셜 로그인                       │
│   - Coil: 이미지 로딩 및 캐싱                     │
└─────────────────────────────────────────────────┘
```

**의존성 흐름 원칙**:
- 상위 계층만 하위 계층 참조 (역방향 참조 금지)
- 인터페이스 기반 통신으로 느슨한 결합
- 불변 데이터 모델로 예측 가능한 상태 관리

### 2.3 Feature 기반 패키지 구조

```
com.alpha.archiveandroid
├── App.kt                      # Application 클래스 (Hilt, Kakao SDK 초기화)
│
├── core/                       # 공통 기능
│   ├── network/                # 네트워크 관련
│   │   ├── ApiResult.kt        # API 결과 래핑
│   │   ├── AuthInterceptor.kt  # JWT 자동 관리
│   │   ├── NetworkModule.kt    # Hilt 네트워크 모듈
│   │   └── RetrofitClient.kt   # Retrofit 설정
│   ├── storage/                # 로컬 저장소
│   │   └── TokenStore.kt       # 토큰 관리
│   ├── ui/                     # 공통 UI 컴포넌트
│   │   ├── components/         # 재사용 컴포넌트
│   │   └── theme/              # Material 3 테마
│   └── util/                   # 유틸리티
│       ├── DateFormatter.kt
│       └── CategoryColorGenerator.kt
│
├── di/                         # 의존성 주입
│   └── RepositoryModule.kt     # Repository 바인딩
│
└── feature/                    # 기능별 모듈
    ├── intro/                  # 로그인 화면
    │   ├── data/
    │   │   ├── remote/         # AuthApi, DTO
    │   │   └── repository/     # AuthRepository
    │   └── view/
    │       ├── IntroActivity.kt
    │       ├── IntroViewModel.kt
    │       └── ui/             # Composable
    │
    └── home/                   # 메인 화면
        ├── HomeActivity.kt     # 5개 탭 컨테이너
        │
        ├── chatbot/            # AI 챗봇 탭
        ├── record/             # 활동 기록 탭
        ├── recommend/          # 추천 탭
        ├── stats/              # 통계 탭
        └── user/               # 프로필 탭
```

**패키지 구조 설계 원칙**:
- Feature별로 완전히 독립적인 모듈 구성
- 각 Feature는 data/view로 분리
- data는 remote(API)/repository로 세분화
- view는 ViewModel과 ui(Composable)로 분리
- 공통 기능은 core 패키지로 추출

### 2.4 상태 관리 전략

**StateFlow 기반 반응형 UI**:

ViewModel에서 MutableStateFlow로 상태를 관리하고, asStateFlow()로 읽기 전용 StateFlow를 외부에 노출합니다. Composable 함수에서는 collectAsState()로 StateFlow를 관찰하여 상태가 변경될 때마다 자동으로 리컴포지션됩니다.

**Sealed Interface로 UI 상태 표현**:

UiState를 Sealed Interface로 정의하여 가능한 상태를 타입으로 제한합니다:
- Loading: 데이터 로딩 중
- Success: 데이터 로드 성공
- Error: 에러 발생

when 표현식으로 모든 상태를 분기 처리하면 컴파일러가 exhaustive 체크를 수행하여 누락된 분기를 방지합니다.

---

## 3. 기술 스택 선정 및 사용 기술

### 3.1 핵심 기술 스택

| 분류 | 기술 | 버전 | 선정 이유 |
|------|------|------|-----------|
| **언어** | Kotlin | 2.0.21 | • Null Safety로 NPE 방지<br>• Coroutines로 비동기 처리 간소화<br>• Data Class로 불변 객체 간편 생성<br>• Extension Function으로 기존 클래스 확장 |
| **UI 프레임워크** | Jetpack Compose | 2024.09 BOM | • 선언적 UI로 코드 간결성 향상<br>• XML 레이아웃 불필요<br>• State 기반 자동 리컴포지션<br>• Material Design 3 네이티브 지원 |
| **아키텍처** | MVVM + Clean | - | • UI와 비즈니스 로직 완전 분리<br>• ViewModel로 생명주기 안전 관리<br>• 테스트 용이성 극대화 |
| **의존성 주입** | Hilt | 2.57 | • Dagger 기반 컴파일 타임 DI<br>• @HiltViewModel로 자동 주입<br>• @Singleton으로 인스턴스 공유 |
| **네트워크** | Retrofit | 3.0.0 | • 타입 안전한 HTTP 클라이언트<br>• Coroutines 네이티브 지원<br>• Interceptor로 공통 처리 |
| **HTTP 클라이언트** | OkHttp | 4.x | • Connection Pool로 성능 향상<br>• Interceptor 체인 지원<br>• HTTP/2 지원 |
| **비동기 처리** | Coroutines + Flow | 1.7.x | • suspend 함수로 동기 코드처럼 작성<br>• StateFlow로 반응형 UI<br>• viewModelScope로 자동 취소 |
| **페이징** | Paging 3 | 3.2.1 | • 무한 스크롤 자동 구현<br>• 메모리 사용량 최적화<br>• Compose 통합 지원 |
| **이미지 로딩** | Coil | 2.6.0 | • Kotlin Coroutines 기반<br>• Compose 전용 API<br>• 메모리/디스크 자동 캐싱 |
| **소셜 로그인** | Kakao SDK | 2.20.6 | • 간편한 회원가입/로그인<br>• OAuth 2.0 표준 |
| **버전 관리** | Firebase Remote Config | - | • 앱 재배포 없이 설정 변경<br>• 강제 업데이트 제어 |
| **JSON 파싱** | Gson | 2.11.0 | • Retrofit Converter 통합<br>• Null-safe |

### 3.2 기술 적용 방법

**1. Jetpack Compose 선언적 UI**

모든 화면을 Composable Function으로 구현하여 XML 레이아웃을 완전히 제거했습니다. State Hoisting 패턴으로 상태를 상위 컴포저블로 올려 단방향 데이터 흐름을 구현했습니다.

**2. Hilt 자동 의존성 주입**

@HiltAndroidApp 어노테이션으로 Application 클래스에서 Hilt를 활성화하고, @HiltViewModel 어노테이션으로 ViewModel에 Repository를 자동 주입합니다.

**3. Retrofit + Coroutines 비동기 통신**

모든 API 메서드를 suspend 함수로 정의하여 순차 코드처럼 작성합니다. AuthInterceptor가 모든 요청에 JWT 토큰을 자동으로 추가하고, 401 응답 수신 시 자동으로 토큰을 갱신한 후 재요청합니다.

**4. Paging 3 효율적 데이터 로딩**

RecordPagingSource를 구현하여 백엔드 API를 Paging 라이브러리에 연결합니다. Pager 객체로 Flow<PagingData>를 생성하여 ViewModel에서 관리하고, Compose의 collectAsLazyPagingItems()로 LazyColumn에 바인딩합니다.

**5. SharedPreferences 토큰 저장**

TokenStore 인터페이스로 저장소를 추상화하여 구현 교체가 용이합니다. suspend 함수로 I/O 작업을 코루틴에서 안전하게 처리하며, 향후 EncryptedSharedPreferences로 전환 시 인터페이스 변경 없이 구현체만 교체 가능합니다.

**6. Firebase Remote Config 버전 관리**

앱 실행 시 Firebase Remote Config에서 minimum_version과 recommended_version을 조회합니다. 현재 앱 버전과 비교하여 강제 업데이트가 필요하면 다이얼로그를 표시합니다.

---

## 4. 의존성 주입 및 모듈화 전략

### 4.1 Hilt 모듈 구성

Archive 앱은 Hilt를 통해 의존성 주입을 자동화하고 모듈별로 제공하는 인스턴스를 명확히 구분합니다.

**Hilt 모듈 구조**:
```
@HiltAndroidApp (App.kt)
├── NetworkModule          # Retrofit, OkHttp, API 인터페이스
├── StorageModule          # TokenStore, SharedPreferences
└── RepositoryModule       # Repository 인터페이스 → 구현체 바인딩
```

**NetworkModule**:

Retrofit, OkHttp, 각 Feature의 API 인터페이스를 제공합니다.

주요 제공 인스턴스:
- @Named("AppRetrofit"): JWT 인증이 필요한 앱 API용 Retrofit
- ActivityApi, ChatbotApi, RecommendApi, StatsApi 등
- AuthInterceptor: 자동 토큰 첨부 및 401 응답 시 토큰 갱신
- GsonConverter: JSON 응답을 DTO로 자동 변환
- OkHttpClient: 타임아웃, 로깅 인터셉터 설정

**StorageModule**:

로컬 저장소인 SharedPreferences와 TokenStore를 제공합니다.

구현 방식:
- TokenStore 인터페이스로 저장소 추상화
- SharedPrefsTokenStore 구현체를 Hilt가 자동 주입
- suspend 함수로 I/O 작업을 코루틴에서 처리
- token_store_prefs 파일에 Access Token과 Refresh Token 저장

**RepositoryModule**:

Repository 인터페이스를 구현체에 바인딩합니다.

바인딩 방식:
- @Binds 어노테이션으로 인터페이스와 구현체 연결
- ActivityRepository ← ActivityRepositoryImpl
- ChatbotRepository ← ChatbotRepositoryImpl
- RecommendRepository ← RecommendRepositoryImpl
- StatsRepository ← StatsRepositoryImpl
- @Singleton 스코프로 앱 전체에서 단일 인스턴스 공유

### 4.2 Feature별 의존성 흐름

**Chatbot Feature 예시**:
```
ChatbotScreen (Composable)
    ↓ collectAsState()
ChatbotViewModel (@HiltViewModel)
    ↓ @Inject constructor
ChatbotRepository (인터페이스)
    ↓ @Binds
ChatbotRepositoryImpl
    ↓ @Inject constructor
ChatbotApi (Retrofit 인터페이스)
    ↓ @Provides
Retrofit (@Named("AppRetrofit"))
    ↓ Interceptor Chain
AuthInterceptor (@Inject)
    ↓
TokenStore (SharedPreferences)
```

**장점**:
- 각 계층이 인터페이스에만 의존하여 구현 교체 용이
- Hilt가 컴파일 타임에 의존성 그래프 생성하여 런타임 에러 방지
- 테스트 시 Mock Repository를 쉽게 주입 가능

---

## 5. 핵심 기능 구현

### 5.1 AI 챗봇 구현

**목적**:
자연어 대화만으로 활동 기록을 자동화하는 핵심 기능입니다.

**구현 방식**:

1. 사용자 메시지를 즉시 로컬에 추가하여 UI에 표시
2. 백엔드 POST /api/chatbot/message로 메시지 전송
3. 백엔드가 OpenAI API 호출 및 Redis에 대화 컨텍스트 관리
4. AI 응답을 받아 로컬 메시지 리스트에 추가
5. "성공적으로 기록되었어요" 패턴 감지 시 저장 완료로 간주

**UI 구조**:

ChatbotScreen은 다음 컴포넌트로 구성됩니다:
- ChatbotHeader: 상단 타이틀 및 알파 소개
- LazyColumn (reverseLayout = true): 최신 메시지가 하단에 위치
- UserMessage: 오른쪽 정렬, 파란색 말풍선
- BotMessage: 왼쪽 정렬, 회색 말풍선
- BotLoadingMessage: AI 응답 대기 중 애니메이션
- SuggestionButtons: 빠른 답변 버튼
- ChatInputBar: 하단 입력창 + 전송 버튼

**상태 관리**:

ChatbotViewModel은 다음 StateFlow를 관리합니다:
- messages: 전체 대화 기록
- inputText: 입력창의 현재 텍스트
- isLoading: AI 응답 대기 중 여부

### 5.2 공공 데이터 활용 및 추천

**백엔드 데이터 통합**:

백엔드는 문화체육관광부, 서울시 등의 공공 API를 통합하여 PublicEvent 테이블에 저장합니다. 앱은 이를 조회하고 필터링하여 사용자에게 제공합니다.

**RecommendScreen 구조**:

1. RecommendViewModel이 StateFlow로 이벤트 목록 관리
2. RecommendRepository.getRecommendations(filters) 호출
3. RecommendApi.getPublicEvents(category, location, title)로 백엔드 요청
4. 필터링된 이벤트 목록을 DTO로 수신
5. LazyColumn으로 UI 렌더링

**필터 기능**:

- 카테고리 필터: 문화, 스포츠, 예술, 축제, 콘서트 등
- 위치 필터: 서울, 경기, 강남구 등 지역별
- 제목 검색: 키워드로 이벤트 제목 검색
- RecommendFilterSheet: Material 3 BottomSheet로 필터 UI 제공
- FilterChips로 선택된 필터 시각적 표시

**상세 화면**:

RecommendDetailScreen은 다음 정보를 표시합니다:
- 이벤트 제목, 설명, 카테고리
- 기간 (시작일 ~ 종료일)
- 장소 (주소)
- 외부 링크 (원본 공공 데이터 출처)
- 이미지 (Coil로 로드 및 캐싱)

### 5.3 통계 및 데이터 시각화

**제공 통계**:

StatsScreen은 다음 통계를 제공합니다:
1. 전체 활동 횟수: 누적 활동 건수
2. 카테고리별 프로그레스 바: 각 카테고리의 활동 횟수 및 비율을 가로 막대로 표시
3. 주간 요일별 막대 그래프: 월~일 요일별 활동 횟수
4. 월간 캘린더: 특정 월의 일자별 활동 여부를 히트맵으로 표시

**StatsViewModel 데이터 흐름**:

1. init 블록에서 loadStatistics() 자동 호출
2. 전체 통계 API (getOverallStatistics) 호출
3. 주간 통계 API (getWeeklyStatistics) 호출
4. 월간 통계 API (getMonthlyStatistics) 호출
5. 백엔드 DTO를 UI용 Data Class로 변환
6. CategoryColorGenerator로 카테고리별 색상 자동 할당
7. reorderDailyDataToEndWithToday()로 오늘이 가장 오른쪽이 되도록 요일 재정렬

**차트 구현**:

**ActivityTypeChart (카테고리별 프로그레스 바)**:
- 각 카테고리를 세로로 나열
- 카테고리명, 퍼센트 표시
- 가로 프로그레스 바로 비율 시각화
- 카테고리별로 다른 색상 적용

**DailyChart (요일별 막대 그래프)**:
- 7개 막대를 Row로 가로 정렬
- 각 막대 높이는 count 값에 비례
- 클릭 시 해당 요일의 활동 개수 표시
- 선택된 요일은 진한 색상으로 강조

**MonthlyCalendar (캘린더 히트맵)**:
- LazyVerticalGrid가 아닌 Row를 사용한 커스텀 캘린더
- 각 날짜를 Circle 형태로 표시
- 활동 개수에 따라 4단계 색상 적용 (보라색 계열)
- 월 변경 버튼 (좌우 화살표)으로 이전/다음 월 이동
- 클릭 시 해당 날짜의 활동 개수 툴팁 표시

### 5.4 활동 기록 관리

**RecordScreen 구성**:

Paging 3를 활용한 활동 목록 화면입니다:
- LazyColumn으로 무한 스크롤
- RecordItem으로 각 활동을 Card 형태로 표시
- 필터링 (카테고리)
- 클릭 시 RecordDetailActivity로 이동

**RecordPagingSource**:

백엔드 API를 Paging 라이브러리에 연결하는 PagingSource 구현체입니다:
- load() 함수에서 API 호출
- LoadResult.Page로 데이터와 페이징 키 반환
- IOException, HttpException 등 에러 처리
- AppError로 사용자 친화적 에러 메시지 제공

**RecordDetailScreen**:

활동 상세 정보를 표시하고 편집/삭제가 가능한 화면입니다:
- 활동 제목, 날짜, 장소, 카테고리
- 메모
- 이미지 목록 (Coil로 로드)
- 편집 버튼: RecordInputActivity로 이동
- 삭제 버튼: 확인 다이얼로그 후 삭제

**RecordInputActivity**:

활동을 생성하거나 수정하는 화면입니다:
- 제목 입력
- 카테고리 선택
- 날짜/시간 선택 (DateTimePicker)
- 장소 입력 (카카오 맵 API로 장소 검색)
- 평점 선택
- 메모 입력
- 이미지 첨부

---

## 6. 보안 아키텍처 및 인증 체계

### 6.1 JWT 기반 인증 흐름

**전체 인증 흐름**:

```
1. 앱 실행 → IntroActivity
2. 카카오 SDK 초기화 (App.kt - onCreate)
3. 자동 로그인 시도 (저장된 토큰 확인)
   ├─ 성공 → HomeActivity 이동
   └─ 실패 → 로그인 화면 표시
4. "카카오 로그인" 버튼 클릭
5. Kakao SDK 로그인 (카카오톡/카카오계정)
6. Kakao Access Token 획득
7. 백엔드로 Kakao Access Token 전송
   POST /api/auth/kakao/login
8. 백엔드가 Kakao API로 사용자 정보 조회
9. 백엔드가 JWT 발급
10. TokenStore에 JWT 저장 (SharedPreferences)
11. HomeActivity로 이동
12. 이후 모든 API 요청에 JWT 자동 포함 (AuthInterceptor)
```

### 6.2 TokenStore 구현

**인터페이스 정의**:

TokenStore는 토큰을 안전하게 저장하고 관리하는 인터페이스입니다:
- save(access, refresh): 두 토큰을 모두 저장
- access(): Access Token 조회
- refresh(): Refresh Token 조회
- clear(): 모든 토큰 삭제

**SharedPreferences 구현**:

SharedPrefsTokenStore 클래스로 TokenStore를 구현합니다:
- Context.MODE_PRIVATE로 앱 전용 저장소 사용
- KEY_ACCESS, KEY_REFRESH 상수로 키 관리
- putString(), getString()으로 key-value 저장
- apply()로 비동기 저장

**보안 고려사항**:

향후 다음 방식으로 강화 가능합니다:
- EncryptedSharedPreferences: AES-256 암호화
- KeyStore: 암호화 키를 하드웨어 보안 모듈에 저장
- BiometricPrompt: 생체 인증 후 토큰 접근
- Root Detection: 루팅된 기기에서 토큰 저장 차단

### 6.3 AuthInterceptor 자동 토큰 관리

**역할**:

AuthInterceptor는 OkHttp의 Interceptor 인터페이스를 구현하여 모든 HTTP 요청을 가로챕니다:

1. 모든 요청에 JWT Access Token을 Authorization 헤더로 자동 추가
2. 401 Unauthorized 응답 수신 시 자동으로 토큰 갱신
3. 갱신 성공 시 원래 요청 재시도
4. 갱신 실패 시 토큰 삭제 후 로그인 화면 이동

**동작 과정**:

**요청 전처리**:
- 카카오 로그인(/api/auth/kakao/login)과 토큰 갱신(/api/auth/refresh) 요청은 토큰 없이 통과
- 그 외 모든 요청은 TokenStore에서 Access Token 조회
- runBlocking으로 suspend 함수를 동기적으로 호출
- Access Token이 존재하면 "Bearer {token}" 형식으로 헤더 추가

**401 응답 처리**:
- 응답 코드가 401이면 토큰 만료로 간주
- 기존 응답을 close()하고 TokenRefreshService로 토큰 갱신 시도
- 갱신 성공 시 새로운 Access Token으로 원래 요청 재시도
- 갱신 실패 시 tokenStore.clear()로 모든 토큰 삭제

**무한 루프 방지**:
- TokenRefreshService는 AuthInterceptor가 적용되지 않은 별도 Retrofit 사용
- @Named("AuthRetrofit")으로 구분

### 6.4 TokenRefreshService

**역할**:

Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 받아옵니다.

**토큰 갱신 과정**:

1. TokenStore에서 Refresh Token 조회
2. @Named("AuthRetrofit")으로 AuthInterceptor가 미적용된 Retrofit 사용
3. AuthApi.refreshToken()으로 백엔드에 Refresh Token 전송
4. 응답 성공 시 새로운 토큰을 TokenStore에 저장
5. 모든 예외를 catch하여 false 반환

**무한 루프 방지 전략**:

NetworkModule에서 두 개의 Retrofit 인스턴스를 제공합니다:
- @Named("AppRetrofit"): AuthInterceptor 적용, 일반 API 호출용
- @Named("AuthRetrofit"): AuthInterceptor 미적용, 토큰 갱신 전용

### 6.5 로그아웃 처리

**로그아웃 흐름**:

1. 사용자가 UserScreen에서 "로그아웃" 버튼 클릭
2. UserViewModel.logout() 함수 호출
3. viewModelScope.launch로 코루틴 시작
4. TokenStore.clear()로 모든 토큰 삭제
5. (선택) 백엔드 /api/auth/logout 호출
6. _logoutEvent StateFlow를 true로 변경
7. Activity에서 logoutEvent 관찰하여 IntroActivity로 이동
8. Intent에 FLAG_CLEAR_TASK | FLAG_NEW_TASK 추가하여 HomeActivity 스택 제거

---

## 7. UI/UX 설계 및 Material Design 3

### 7.1 Jetpack Compose 선택 배경

Archive 앱은 Jetpack Compose를 UI 프레임워크로 채택했습니다.

**장점**:

1. 선언적 UI: 상태를 정의하면 UI가 자동으로 업데이트
2. 코드 간결성: XML 레이아웃 불필요
3. 재사용성: Composable Function 조합
4. Material Design 3 네이티브 지원
5. 실시간 미리보기: @Preview로 빌드 없이 UI 확인
6. 타입 안전: 컴파일 타임에 UI 에러 검출

### 7.2 Material Design 3 적용

**핵심 요소**:

- Dynamic Color: Android 12+ 시스템 테마에 맞춰 앱 색상 자동 조정
- Color Scheme: Primary, Secondary, Tertiary, Surface 등 의미론적 색상
- Typography: Display, Headline, Body 등 타이포그래피 스케일
- Elevation: 그림자 대신 Surface Tint로 깊이 표현
- Shape: 둥근 모서리 적극 활용

**Theme 정의 전략**:

ArchiveAndroidTheme Composable로 앱 전체 테마를 정의합니다:
- Android 12+ dynamicColorScheme() 지원
- MaterialTheme으로 colorScheme, typography, shapes 통합

### 7.3 공통 UI 컴포넌트

**TopAppBar (상단 앱바)**:

CustomTopAppBar Composable로 재사용 가능한 앱바 구현:
- title 파라미터로 화면 제목 설정
- onBackClick 파라미터로 뒤로가기 버튼 제어
- actions 파라미터로 오른쪽 액션 버튼 추가
- TopAppBarDefaults.topAppBarColors()로 색상 커스터마이징

**BottomNavigationBar (하단 네비게이션)**:

HomeBottomBar Composable로 하단 네비게이션 구현:
- Material 3 NavigationBar 컴포넌트 사용
- selectedTab으로 현재 선택된 탭 전달
- onTabSelected 콜백으로 탭 선택 이벤트 처리
- HomeTab enum (RECORD, RECOMMEND, CHATBOT, STATS, USER)

**RecordItem (활동 카드)**:

Material 3 Card로 활동 카드 구현:
- fillMaxWidth()로 화면 너비에 맞춤
- clickable Modifier로 클릭 이벤트
- CardDefaults.cardElevation(2.dp)로 그림자
- RoundedCornerShape(12.dp)로 둥근 모서리

### 7.4 네비게이션 구조

**HomeNavGraph**:

Navigation Compose로 5개 탭 화면 관리:
- NavHost로 NavHostController와 startDestination 설정
- 각 HomeTab enum 값을 route로 사용
- composable()로 각 탭에 Composable 매핑
- hiltViewModel()로 각 화면의 ViewModel 자동 주입
- fadeIn/fadeOut 애니메이션

### 7.5 이미지 로딩 (Coil)

**구현 전략**:

AsyncImage Composable로 비동기 이미지 로딩:
- ImageRequest.Builder로 로딩 옵션 설정
- crossfade(true)로 페이드 인 효과
- placeholder()로 로딩 중 임시 이미지
- error()로 실패 시 에러 이미지
- ContentScale.Crop으로 이미지 크롭

**자동 캐싱**:
- 메모리 캐시: LRU 알고리즘
- 디스크 캐시: HTTP 캐시 헤더 기반
- 중복 요청 방지

---

## 8. 데이터 처리 및 최적화

### 8.1 Paging 3 구현

**RecordPagingSource**:

백엔드 API를 Paging에 연결:
- load() 함수에서 ActivityApi.getActivities() 호출
- LoadResult.Page로 데이터와 prevKey, nextKey 반환
- IOException, HttpException 등 에러를 LoadResult.Error로 변환

**ViewModel 통합**:

Pager 객체로 Flow<PagingData> 생성:
- PagingConfig로 페이지 크기 설정 (pageSize = 20)
- cachedIn(viewModelScope)로 캐싱 및 생명주기 관리

**Compose 통합**:

collectAsLazyPagingItems()로 PagingData를 LazyColumn에 바인딩:
- 무한 스크롤 자동 구현
- 로딩, 에러 상태 자동 처리

### 8.2 네트워크 에러 처리

**Result<T> 패턴**:

API 호출 결과를 Result<T>로 래핑:
- Result.success(data): 성공 시 데이터 반환
- Result.failure(exception): 실패 시 예외 반환
- onSuccess, onFailure로 분기 처리

**에러 메시지 변환**:

toUserFriendlyMessage() 확장 함수:
- IOException: "네트워크 연결을 확인해주세요"
- HttpException: "서버 오류: {코드}"
- 기타: "알 수 없는 오류가 발생했습니다"

### 8.3 메모리 최적화

**이미지 캐싱**:
- Coil의 메모리 캐시로 최근 이미지 보관
- 디스크 캐시로 영구 저장
- 이미지 크기를 뷰 크기에 맞춰 리사이징

**Paging 3**:
- 화면에 보이는 항목만 메모리에 로드
- 스크롤 시 자동으로 이전 항목 해제

**LazyColumn**:
- 화면에 보이는 아이템만 렌더링
- 스크롤 시 재사용

### 8.4 성능 최적화

**Compose 최적화**:
- remember로 불필요한 재계산 방지
- derivedStateOf로 파생 상태 최적화
- key로 LazyColumn 아이템 재사용

**Coroutines 최적화**:
- viewModelScope로 자동 취소
- Dispatchers.IO로 I/O 작업
- Dispatchers.Main으로 UI 업데이트

---

## 9. 최종 솔루션 및 서비스 구현 결과

### 9.1 기술 구현 성과

**1. 모던 안드로이드 개발 스택**:
- Kotlin 2.0 + Jetpack Compose로 선언적 UI
- Hilt로 의존성 주입 자동화
- Coroutines + Flow로 비동기 처리 간소화
- Paging 3로 대량 데이터 효율 처리

**2. 안정적인 네트워크 통신**:
- Retrofit + OkHttp로 백엔드 REST API 통신
- AuthInterceptor로 JWT 자동 관리 및 토큰 갱신
- Result<T>로 에러 핸들링 일원화

**3. 직관적인 UI/UX**:
- Material Design 3로 일관된 디자인
- 커스텀 차트로 통계 시각화
- Bottom Navigation으로 5개 핵심 기능 빠른 접근

**4. 보안 강화**:
- JWT 기반 Stateless 인증
- 카카오 소셜 로그인
- HTTPS 통신 및 토큰 자동 갱신

### 9.2 주요 화면 및 기능

**IntroScreen**: 카카오 로그인 + 자동 로그인 + 버전 체크  
**HomeActivity**: 5개 탭 (기록/추천/챗봇/통계/프로필)  
**RecordScreen**: 활동 목록 (Paging 3) + 필터링  
**RecordDetailScreen**: 활동 상세 + 편집/삭제  
**RecordInputActivity**: 활동 생성/수정  
**RecommendScreen**: 공공 이벤트 목록 + 필터  
**RecommendDetailScreen**: 이벤트 상세  
**ChatbotScreen**: AI 대화형 기록  
**StatsScreen**: 프로그레스 바 + 막대 그래프 + 캘린더  
**UserScreen**: 프로필 + 로그아웃  

### 9.3 빌드 및 배포

**빌드 명령어**:
```bash
# 디버그 빌드
./gradlew assembleDebug

# 릴리즈 빌드 (ProGuard 적용)
./gradlew assembleRelease

# 유닛 테스트
./gradlew test
```

### 9.4 향후 확장 계획

**단기 (3개월)**:
- Room Database로 오프라인 모드
- Firebase Cloud Messaging 푸시 알림
- EncryptedSharedPreferences 토큰 암호화

**중기 (6개월)**:
- 멀티 모듈 프로젝트 전환
- 개인화 추천 알고리즘

**장기 (1년)**:
- Wear OS 앱
- 다국어 지원

### 9.5 개발 환경 요약

**필수 환경**:
- Android Studio: Iguana (2023.2.1) 이상
- Gradle: 8.12
- Kotlin: 2.0.21
- JDK: 11
- minSdk: 26
- targetSdk: 36

**주요 의존성**:
- Compose BOM: 2024.09.00
- Hilt: 2.57
- Retrofit: 3.0.0
- Coil: 2.6.0
- Paging: 3.2.1
- Kakao SDK: 2.20.6

---

## 10. 결론

Archive 안드로이드 앱은 모던 안드로이드 개발 기술 스택과 사용자 중심 UI/UX 설계를 결합하여 오프라인 활동 기록을 간편하게 만드는 모바일 플랫폼입니다.

### 주요 성과

**1. 기술성**
- Jetpack Compose로 선언적 UI
- Clean Architecture + MVVM으로 유지보수성 극대화
- Hilt로 의존성 주입 자동화
- Paging 3로 대량 데이터 효율 로딩

**2. 서비스 차별성**
- 카카오 간편 로그인
- AI 챗봇으로 자연어 기록
- 공공 데이터 통합 추천
- 커스텀 차트로 통계 시각화

**3. 안정성**
- JWT 자동 관리 및 토큰 갱신
- Result<T>로 에러 핸들링
- StateFlow로 UI 상태 안전 관리
- HTTPS 통신

**4. 확장 가능성**
- Feature 기반 패키지 구조
- Repository 패턴
- Interface 기반 설계

Archive 안드로이드 앱은 AI와 공공 데이터를 결합한 차세대 문화생활 플랫폼으로 성장할 수 있는 견고한 기술 기반을 구축했습니다.
