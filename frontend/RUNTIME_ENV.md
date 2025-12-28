# 런타임 환경변수 사용 가이드

프론트엔드는 Docker 컨테이너에서 **런타임에 환경변수를 주입**받습니다.

## 동작 원리

1. **빌드 시점**: Vite가 환경변수 없이 애플리케이션을 빌드합니다
2. **컨테이너 시작**: `docker-entrypoint.sh`가 환경변수를 읽어서 `env-config.js` 생성
3. **런타임**: 애플리케이션이 `window.__ENV__`로 환경변수에 접근

## 코드에서 사용하기
```javascript
// ✅ 런타임 환경변수 사용
const apiUrl = window.__ENV__?.VITE_API_BASE_URL || 'http://localhost:8080/api';

// 또는 헬퍼 함수 사용
function getEnv(key, defaultValue = '') {
  // 런타임 환경변수 우선, 없으면 빌드 시점 환경변수, 마지막으로 기본값
  return window.__ENV__?.[key]
    || import.meta.env[key]
    || defaultValue;
}

const apiUrl = getEnv('VITE_API_BASE_URL', 'http://localhost:8080/api');
const stage = getEnv('VITE_STAGE', 'remote');
```

## 개발 환경

로컬 개발 시에는 `.env` 파일과 `import.meta.env`가 정상적으로 작동합니다.
`window.__ENV__`는 Docker 컨테이너에서만 생성되므로, 로컬에서는 자동으로 `import.meta.env`로 폴백됩니다.

## 사용 가능한 환경변수

- `VITE_STAGE`: "local" 또는 "remote"
- `VITE_API_BASE_URL`: 백엔드 API URL
- `VITE_ADSENSE_CLIENT_ID`: Google AdSense Client ID
- `VITE_ADSENSE_SLOT_GRID`: AdSense Slot ID (Grid)
- `VITE_ADSENSE_SLOT_FOOTER`: AdSense Slot ID (Footer)
- `VITE_RP_CURRENT_MONTH`: 현재 월 RP 기본값
- `VITE_RP_LAST_MONTH`: 지난 월 RP 기본값
- `VITE_FOREIGN_HOLDING_PERCENTAGE`: 외국인 보유 비율 기본값

## Docker/Kubernetes에서 환경변수 설정

### Docker Compose
```bash
docker-compose --env-file .env.docker up
```

### Kubernetes
```bash
# ConfigMap 수정
kubectl edit configmap kemi-radar-frontend-config

# Pod 재시작
kubectl rollout restart deployment/kemi-radar-frontend
```

## 예제

```javascript
// src/config.js
export const config = {
  stage: window.__ENV__?.VITE_STAGE || import.meta.env.VITE_STAGE || 'remote',
  apiBaseUrl: window.__ENV__?.VITE_API_BASE_URL || import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api',
  adsense: {
    clientId: window.__ENV__?.VITE_ADSENSE_CLIENT_ID || import.meta.env.VITE_ADSENSE_CLIENT_ID || '',
    slotGrid: window.__ENV__?.VITE_ADSENSE_SLOT_GRID || import.meta.env.VITE_ADSENSE_SLOT_GRID || '',
    slotFooter: window.__ENV__?.VITE_ADSENSE_SLOT_FOOTER || import.meta.env.VITE_ADSENSE_SLOT_FOOTER || '',
  },
  defaults: {
    rpCurrentMonth: parseFloat(window.__ENV__?.VITE_RP_CURRENT_MONTH || import.meta.env.VITE_RP_CURRENT_MONTH || '14.9'),
    rpLastMonth: parseFloat(window.__ENV__?.VITE_RP_LAST_MONTH || import.meta.env.VITE_RP_LAST_MONTH || '9.3'),
    foreignHoldingPercentage: parseFloat(window.__ENV__?.VITE_FOREIGN_HOLDING_PERCENTAGE || import.meta.env.VITE_FOREIGN_HOLDING_PERCENTAGE || '23'),
  }
};
```

```javascript
// src/api/client.js
import { config } from '../config.js';

const apiClient = axios.create({
  baseURL: config.apiBaseUrl,
  // ...
});
```
