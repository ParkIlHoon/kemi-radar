# KEMI RADAR (개미 레이더)
> Korea Economy Monitoring Intelligence RADAR

한국 경제의 주요 지표를 실시간으로 모니터링하고 위험 수준을 평가하는 개인투자자용 대시보드입니다.

## 주요 기능
### 모니터링 지표
1. **10년물 국채금리 스프레드**
   - 한국과 미국 10년물 국채금리 차이 분석
   - 한미 금리차 -50bp 이하: 경고
   - 한미 금리차 -100bp 이하: 위험

2. **원/달러 환율**
   - 1,500원 이상: 경고
   - 1,550원 이상: 위험
   - 1,600원 이상: 매우 위험

3. **한국은행 RP 잔액**
   - 월간 증가 2조원 이상: 경고 (금융시장 불안)
   - 월간 증가 3조원 이상: 위험 (부동산 매도 타이밍)
   - 월간 증가 5조원 이상: 매우 위험 (현금 확보)

4. **외국인 국채 보유 비중**
   - 15% 이하: 경고 (외국인 이탈)
   - 10% 이하: 위험 (시장 신뢰도 위기)

5. **달러 인덱스 (DXY)**
   - 달러 강세 + 원화 약세 조합 분석
   - 110 이상: 신흥국 전체 압박

### 종합 위험도 평가
- 5개 지표의 위험도를 종합하여 전체 경제 상황 평가
- 점수 기반 위험 수준 표시 (0~3점)

## 기술 스택
- **프로젝트 구조**: Gradle + npm 멀티모듈
- **프론트엔드**: Svelte + Vite (npm workspace)
- **백엔드**: Ktor (Kotlin) + Gradle
- **데이터 소스**:
  - 한국은행 ECOS API (국채금리, 환율, RP 잔액)
  - FRED API (미국 국채금리, 달러 인덱스)
  - 금융감독원 API (외국인 보유 비중)

## 설치 및 실행

### 사전 요구사항

- **Backend**: JDK 21 이상
- **Frontend**: Node.js 18 이상, npm 9 이상

### 빠른 시작

#### 옵션 1: Local 모드 (백엔드 없이 Mock 데이터)

프로젝트 루트에서:

```bash
# 의존성 설치
npm install

# Local 모드로 실행
npm run dev:local
```

브라우저에서 http://localhost:5173 접속

#### 옵션 2: Full Stack 실행

**1. 백엔드 실행**

```bash
cd backend

# 환경변수 설정 (Linux/Mac)
export ECOS_API_KEY=your_ecos_api_key
export FRED_API_KEY=your_fred_api_key

# 백엔드 실행
./gradlew run
```

**2. 프론트엔드 실행 (새 터미널)**

```bash
# 의존성 설치 (최초 1회)
npm install

# Remote 모드로 실행
npm run dev
```

브라우저에서 http://localhost:5173 접속

### 프로젝트 구조

```
kemi-radar/
├── frontend/           # Svelte 프론트엔드
└── backend/            # Ktor 백엔드
```

### 모듈별 상세 문서

- **Frontend**: [frontend/README.md](frontend/README.md)
- **Backend**: [backend/README.md](backend/README.md)


## 주의사항

⚠️ **면책 조항**
- 본 시스템은 참고용 정보 제공 목적으로 제작되었습니다.
- 투자 결정은 본인의 판단과 책임 하에 이루어져야 합니다.
- 제공되는 데이터의 정확성을 보장하지 않습니다.
- 실시간 데이터가 아닌 경우 지연이 발생할 수 있습니다.
