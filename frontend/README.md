# Frontend - KEMI RADAR
> 개미 레이더 프론트엔드

Svelte 기반 경제 모니터링 대시보드입니다.

## 개발 모드

### Local 모드 (Mock 데이터)

백엔드 없이 Mock 데이터로 개발:

```bash
npm run dev:local
```

### Remote 모드 (백엔드 연결)

백엔드 서버와 연결:

```bash
npm run dev
# 또는
npm run dev:remote
```

## 빌드

```bash
# Remote 모드로 빌드
npm run build

# Local 모드로 빌드
npm run build:local
```

## 환경 설정

`.env` 파일을 생성하여 설정:

```bash
# Local 모드
VITE_STAGE=local

# Remote 모드
VITE_STAGE=remote
VITE_API_BASE_URL=http://localhost:8080/api
```

자세한 내용은 루트 [README.md](../README.md)를 참고하세요.
