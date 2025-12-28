<script>
  import { onMount } from 'svelte';
  import IndicatorCard from './components/IndicatorCard.svelte';
  import OverallRisk from './components/OverallRisk.svelte';
  import AdSense from './components/AdSense.svelte';
  import { fetchAllIndicators } from './lib/api.js';
  import { evaluateOverallRisk } from './lib/riskEvaluator.js';
  import config from './config.js';

  let indicators = null;
  let loading = true;
  let lastUpdated = '';
  let autoRefresh = false;
  let refreshInterval = null;

  // 사용자 입력 필드 (기본값 설정 - 런타임 환경변수에서 로드)
  let rpCurrentMonth = config.defaults.rpCurrentMonth;
  let rpLastMonth = config.defaults.rpLastMonth;
  let foreignHoldingPercentage = config.defaults.foreignHoldingPercentage;

  // RP 월간 증가액 계산
  $: rpMonthlyIncrease = rpCurrentMonth - rpLastMonth;
  $: rpYearOverYear = '0'; // 전년 대비 증가율은 더 이상 사용하지 않음

  async function loadData() {
    loading = true;
    try {
      const apiData = await fetchAllIndicators();

      // RP 잔액과 외국인 국채 보유 비중을 사용자 입력으로 추가
      indicators = {
        ...apiData,
        rpBalance: {
          currentMonth: rpCurrentMonth,
          monthlyIncrease: rpMonthlyIncrease,
          yearOverYear: rpYearOverYear,
          date: new Date().toISOString().slice(0, 10)
        },
        foreignHolding: {
          percentage: foreignHoldingPercentage,
          date: new Date().toISOString().slice(0, 10)
        }
      };

      lastUpdated = new Date().toLocaleString('ko-KR');
    } catch (error) {
      console.error('Failed to load data:', error);
    } finally {
      loading = false;
    }
  }

  function updateManualData() {
    if (indicators) {
      indicators = {
        ...indicators,
        rpBalance: {
          currentMonth: rpCurrentMonth,
          monthlyIncrease: rpMonthlyIncrease,
          yearOverYear: rpYearOverYear,
          date: new Date().toISOString().slice(0, 10)
        },
        foreignHolding: {
          percentage: foreignHoldingPercentage,
          date: new Date().toISOString().slice(0, 10)
        }
      };
    }
  }

  function handleRefresh() {
    loadData();
  }

  function toggleAutoRefresh() {
    autoRefresh = !autoRefresh;
    if (autoRefresh) {
      refreshInterval = setInterval(loadData, 300000); // 5분마다
    } else {
      if (refreshInterval) {
        clearInterval(refreshInterval);
        refreshInterval = null;
      }
    }
  }

  onMount(() => {
    loadData();
    return () => {
      if (refreshInterval) {
        clearInterval(refreshInterval);
      }
    };
  });

  // Evaluate risk in frontend
  $: riskEvaluation = indicators ? evaluateOverallRisk(indicators) : null;
  $: overallRisk = riskEvaluation ? {
    level: riskEvaluation.level,
    message: riskEvaluation.message,
    score: riskEvaluation.score
  } : null;
  $: bondYieldRisk = riskEvaluation?.risks?.[0] ? {
    level: riskEvaluation.risks[0].level,
    message: riskEvaluation.risks[0].message,
    details: { spreadBp: riskEvaluation.risks[0].spreadBp }
  } : null;
  $: exchangeRateRisk = riskEvaluation?.risks?.[1] || null;
  $: rpBalanceRisk = riskEvaluation?.risks?.[2] || null;
  $: foreignHoldingRisk = riskEvaluation?.risks?.[3] || null;
  $: dollarIndexRisk = riskEvaluation?.risks?.[4] || null;
</script>

<main>
  <div class="container">
    <header>
      <div class="title-section">
        <h1>
          <span class="title-main">KEMI RADAR</span>
          <span class="title-sub">개미 레이더</span>
        </h1>
        <p class="subtitle">Korea Economy Monitoring Intelligence RADAR</p>
      </div>
      
      <div class="controls">
        <button class="refresh-btn" on:click={handleRefresh} disabled={loading}>
          <span class:spinning={loading}>↻</span>
          새로고침
        </button>
        
        <button 
          class="auto-refresh-btn" 
          class:active={autoRefresh}
          on:click={toggleAutoRefresh}
        >
          <span class="indicator" class:active={autoRefresh}></span>
          자동 업데이트
        </button>
      </div>
    </header>

    {#if loading && !indicators}
      <div class="loading">
        <div class="spinner"></div>
        <p>데이터 로딩 중...</p>
      </div>
    {:else if indicators}
      <div class="content">
        <div class="update-info">
          <span>마지막 업데이트: {lastUpdated}</span>
        </div>

        <OverallRisk {overallRisk} />

        <div class="manual-input-section">
          <h3>수동 입력 데이터</h3>
          <p class="manual-input-description">아래 데이터는 자동으로 수집되지 않으며, 기본 입력값은 가끔 업데이트합니다. 정확한 분석을 위해 직접 입력해주세요.</p>

          <div class="input-grid">
            <div class="input-group">
              <label for="rpCurrentMonth">
                <span class="label-text">이번달 한국은행 RP 잔액 (조원)</span>
                <span class="label-hint">현재 월의 RP 잔액</span>
              </label>
              <input
                id="rpCurrentMonth"
                type="number"
                step="0.1"
                bind:value={rpCurrentMonth}
                on:change={updateManualData}
              />
            </div>

            <div class="input-group">
              <label for="rpLastMonth">
                <span class="label-text">저번달 한국은행 RP 잔액 (조원)</span>
                <span class="label-hint">이전 월의 RP 잔액 (월간 증가액 계산용)</span>
              </label>
              <input
                id="rpLastMonth"
                type="number"
                step="0.1"
                bind:value={rpLastMonth}
                on:change={updateManualData}
              />
            </div>

            <div class="input-group">
              <label for="foreignHoldingPercentage">
                <span class="label-text">외국인 국채 보유 비중 (%)</span>
                <span class="label-hint">외국인의 국채 보유 비율</span>
              </label>
              <input
                id="foreignHoldingPercentage"
                type="number"
                step="0.1"
                bind:value={foreignHoldingPercentage}
                on:change={updateManualData}
              />
            </div>
          </div>
        </div>

        <div class="indicators-grid">
          <IndicatorCard
            title="10년물 국채금리 스프레드"
            value={bondYieldRisk?.details?.spreadBp || '0'}
            unit="bp"
            subValue="한국 {indicators.koreaGovBond10YearsYield.value.toFixed(2)}% | 미국 {indicators.usGovBond10YearsYield.value.toFixed(2)}%"
            risk={bondYieldRisk}
            description="한미 금리차가 마이너스일수록 외국인 자금 이탈 가능성 증가"
            date={indicators.koreaGovBond10YearsYield.date}
          />

          <IndicatorCard
            title="원/달러 환율"
            value={indicators.wonDollarExchangeRate.value.toFixed(2)}
            unit="원"
            risk={exchangeRateRisk}
            description="1,500원 돌파 시 1차 경고, 1,550원 돌파 시 2차 경고"
            date={indicators.wonDollarExchangeRate.date}
          />

          <IndicatorCard
            title="한국은행 RP 잔액"
            value={indicators.rpBalance.currentMonth.toFixed(1)}
            unit="조원"
            subValue="월간 증가 {indicators.rpBalance.monthlyIncrease.toFixed(1)}조원"
            risk={rpBalanceRisk}
            description="월간 증가 2조원 이상 시 금융시장 불안 신호"
          />

          <IndicatorCard
            title="외국인 국채 보유 비중"
            value={indicators.foreignHolding.percentage.toFixed(1)}
            unit="%"
            risk={foreignHoldingRisk}
            description="15% 이하 시 외국인 이탈 경고, 10% 이하 시 위험"
          />

          <IndicatorCard
            title="달러 인덱스 (DXY)"
            value={indicators.dollarIndex.value.toFixed(2)}
            risk={dollarIndexRisk}
            description="110 돌파 시 신흥국 전체 압박, 원화 추가 약세 가능"
            date={indicators.dollarIndex.date}
          />

          <div class="ad-grid-item">
            <AdSense
              client={config.adsense.clientId}
              slot={config.adsense.slotGrid}
            />
          </div>
        </div>

        <div class="action-guide">
          <h3>위험 수준별 대응 가이드</h3>
          <div class="guide-grid">
            <div class="guide-item safe">
              <div class="guide-header">정상</div>
              <ul>
                <li>정기적인 모니터링 유지</li>
                <li>분산 투자 원칙 준수</li>
              </ul>
            </div>
            
            <div class="guide-item warning">
              <div class="guide-header">경고</div>
              <ul>
                <li>달러 자산 20% 확보 검토</li>
                <li>변동성 높은 자산 비중 축소</li>
                <li>모니터링 주기 단축</li>
              </ul>
            </div>
            
            <div class="guide-item danger">
              <div class="guide-header">위험</div>
              <ul>
                <li>달러 자산 30~40% 확대</li>
                <li>부동산 매도 타이밍 검토</li>
                <li>현금 비중 확대</li>
              </ul>
            </div>
            
            <div class="guide-item critical">
              <div class="guide-header">매우 위험</div>
              <ul>
                <li>해외 자산 50%까지 확대</li>
                <li>즉각적인 현금 확보</li>
                <li>방어적 포트폴리오 전환</li>
              </ul>
            </div>
          </div>
        </div>

        <AdSense
          client={config.adsense.clientId}
          slot={config.adsense.slotFooter}
        />

        <footer class="footer">
          <p class="disclaimer">
            ⚠️ 본 시스템은 참고용 정보 제공 목적으로 제작되었습니다.
            투자 결정은 본인의 판단과 책임 하에 이루어져야 합니다.
          </p>
          <p class="data-source">
            데이터 출처: 한국은행 ECOS, FRED (St. Louis Fed), 금융감독원
          </p>
        </footer>
      </div>
    {:else}
      <div class="error">
        <p>데이터를 불러올 수 없습니다.</p>
        <button on:click={handleRefresh}>다시 시도</button>
      </div>
    {/if}
  </div>
</main>

<style>
  main {
    min-height: 100vh;
    padding: 20px;
  }

  .container {
    max-width: 1400px;
    margin: 0 auto;
  }

  header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 40px;
    padding-bottom: 30px;
    border-bottom: 2px solid var(--border-color);
    animation: slideDown 0.6s ease-out;
  }

  @keyframes slideDown {
    from {
      opacity: 0;
      transform: translateY(-20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  .title-section h1 {
    font-family: var(--font-display);
    font-size: 2.5rem;
    font-weight: 900;
    margin: 0 0 8px 0;
    line-height: 1.2;
  }

  .title-main {
    display: block;
    color: var(--text-primary);
    text-shadow: 0 0 30px rgba(239, 68, 68, 0.3);
  }

  .title-sub {
    display: block;
    background: linear-gradient(90deg, #ef4444, #f59e0b, #22c55e);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
    animation: gradientShift 3s ease infinite;
  }

  @keyframes gradientShift {
    0%, 100% {
      filter: hue-rotate(0deg);
    }
    50% {
      filter: hue-rotate(20deg);
    }
  }

  .subtitle {
    font-family: var(--font-display);
    font-size: 0.9rem;
    color: var(--text-secondary);
    margin: 0;
    text-transform: uppercase;
    letter-spacing: 2px;
  }

  .controls {
    display: flex;
    gap: 12px;
  }

  .refresh-btn, .auto-refresh-btn {
    font-family: var(--font-display);
    padding: 12px 24px;
    background: var(--bg-card);
    border: 2px solid var(--border-color);
    border-radius: 12px;
    color: var(--text-primary);
    cursor: pointer;
    font-size: 0.9rem;
    font-weight: 600;
    transition: all 0.3s ease;
    display: flex;
    align-items: center;
    gap: 8px;
  }

  .refresh-btn:hover, .auto-refresh-btn:hover {
    border-color: var(--color-safe);
    box-shadow: 0 0 20px var(--glow-safe);
  }

  .refresh-btn:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }

  .spinning {
    display: inline-block;
    animation: spin 1s linear infinite;
  }

  @keyframes spin {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }

  .auto-refresh-btn.active {
    border-color: var(--color-safe);
    background: rgba(34, 197, 94, 0.1);
  }

  .indicator {
    width: 8px;
    height: 8px;
    background: var(--text-secondary);
    border-radius: 50%;
    transition: all 0.3s ease;
  }

  .indicator.active {
    background: var(--color-safe);
    box-shadow: 0 0 10px var(--color-safe);
    animation: blink 1.5s ease-in-out infinite;
  }

  @keyframes blink {
    0%, 100% {
      opacity: 1;
    }
    50% {
      opacity: 0.3;
    }
  }

  .loading, .error {
    text-align: center;
    padding: 60px 20px;
  }

  .spinner {
    width: 60px;
    height: 60px;
    border: 4px solid var(--border-color);
    border-top-color: var(--color-safe);
    border-radius: 50%;
    margin: 0 auto 20px;
    animation: spin 1s linear infinite;
  }

  .update-info {
    text-align: right;
    font-family: var(--font-display);
    font-size: 0.85rem;
    color: var(--text-secondary);
    margin-bottom: 20px;
  }

  .manual-input-section {
    background: var(--bg-card);
    border: 2px solid var(--border-color);
    border-radius: 16px;
    padding: 32px;
    margin-bottom: 40px;
  }

  .manual-input-section h3 {
    font-family: var(--font-display);
    font-size: 1.3rem;
    color: var(--text-primary);
    margin: 0 0 8px 0;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .manual-input-description {
    font-size: 0.9rem;
    color: var(--text-secondary);
    margin: 0 0 24px 0;
  }

  .input-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 20px;
  }

  .input-group {
    display: flex;
    flex-direction: column;
    gap: 8px;
  }

  .input-group label {
    display: flex;
    flex-direction: column;
    gap: 4px;
  }

  .label-text {
    font-family: var(--font-display);
    font-size: 0.9rem;
    font-weight: 600;
    color: var(--text-primary);
  }

  .label-hint {
    font-size: 0.75rem;
    color: var(--text-secondary);
  }

  .input-group input {
    font-family: var(--font-display);
    padding: 12px 16px;
    background: var(--bg-secondary);
    border: 2px solid var(--border-color);
    border-radius: 8px;
    color: var(--text-primary);
    font-size: 1rem;
    transition: all 0.3s ease;
  }

  .input-group input:focus {
    outline: none;
    border-color: var(--color-safe);
    box-shadow: 0 0 0 3px rgba(34, 197, 94, 0.1);
  }

  .input-group input:hover {
    border-color: var(--color-safe);
  }

  .calculated-info {
    margin-top: 20px;
    padding: 16px;
    background: var(--bg-secondary);
    border-radius: 8px;
    border: 1px solid var(--border-color);
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .calculated-label {
    font-family: var(--font-display);
    font-size: 0.9rem;
    color: var(--text-secondary);
  }

  .calculated-value {
    font-family: var(--font-display);
    font-size: 1.1rem;
    font-weight: 700;
    color: var(--text-primary);
  }

  .content {
    animation: fadeInUp 0.8s ease-out;
  }

  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(30px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  .indicators-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
    gap: 24px;
    margin-bottom: 40px;
  }

  .ad-grid-item {
    display: flex;
    align-items: stretch;
  }

  .ad-grid-item :global(.adsense-wrapper) {
    width: 100%;
    margin: 0;
    display: flex;
    flex-direction: column;
    justify-content: center;
  }

  .action-guide {
    background: var(--bg-card);
    border: 2px solid var(--border-color);
    border-radius: 16px;
    padding: 32px;
    margin-bottom: 40px;
  }

  .action-guide h3 {
    font-family: var(--font-display);
    font-size: 1.3rem;
    color: var(--text-primary);
    margin: 0 0 24px 0;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .guide-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
  }

  .guide-item {
    background: var(--bg-secondary);
    border-radius: 12px;
    padding: 20px;
    border: 2px solid transparent;
    transition: all 0.3s ease;
  }

  .guide-item.safe {
    border-color: var(--color-safe);
  }

  .guide-item.warning {
    border-color: var(--color-warning);
  }

  .guide-item.danger {
    border-color: var(--color-danger);
  }

  .guide-item.critical {
    border-color: var(--color-critical);
  }

  .guide-header {
    font-family: var(--font-display);
    font-size: 1.1rem;
    font-weight: 700;
    margin-bottom: 12px;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .guide-item.safe .guide-header {
    color: var(--color-safe);
  }

  .guide-item.warning .guide-header {
    color: var(--color-warning);
  }

  .guide-item.danger .guide-header {
    color: var(--color-danger);
  }

  .guide-item.critical .guide-header {
    color: var(--color-critical);
  }

  .guide-item ul {
    list-style: none;
    padding: 0;
    margin: 0;
  }

  .guide-item li {
    font-size: 0.9rem;
    color: var(--text-secondary);
    padding: 6px 0;
    padding-left: 20px;
    position: relative;
  }

  .guide-item li::before {
    content: '▸';
    position: absolute;
    left: 0;
    color: var(--text-secondary);
  }

  .footer {
    text-align: center;
    padding: 30px 20px;
    border-top: 2px solid var(--border-color);
  }

  .disclaimer {
    font-size: 0.9rem;
    color: var(--color-warning);
    margin-bottom: 10px;
  }

  .data-source {
    font-family: var(--font-display);
    font-size: 0.8rem;
    color: var(--text-secondary);
  }

  @media (max-width: 768px) {
    header {
      flex-direction: column;
      align-items: flex-start;
      gap: 20px;
    }

    .title-section h1 {
      font-size: 2rem;
    }

    .indicators-grid {
      grid-template-columns: 1fr;
    }

    .guide-grid {
      grid-template-columns: 1fr;
    }
  }
</style>
