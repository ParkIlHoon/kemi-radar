<script>
  export let overallRisk;
  
  $: riskColor = {
    safe: 'var(--color-safe)',
    warning: 'var(--color-warning)',
    danger: 'var(--color-danger)',
    critical: 'var(--color-critical)'
  }[overallRisk.level];
  
  $: riskLabel = {
    safe: '정상',
    warning: '경고',
    danger: '위험',
    critical: '매우 위험'
  }[overallRisk.level];
  
  $: riskIcon = {
    safe: '✓',
    warning: '⚠',
    danger: '⚠',
    critical: '⚠'
  }[overallRisk.level];
</script>

<div class="overall-risk" style="--risk-color: {riskColor}">
  <div class="risk-header">
    <div class="icon-container">
      <span class="risk-icon">{riskIcon}</span>
    </div>
    <div class="risk-info">
      <h2>종합 위험도</h2>
      <div class="risk-level">{riskLabel}</div>
    </div>
  </div>
  
  <div class="risk-message">{overallRisk.message}</div>
  
  <div class="risk-score">
    <div class="score-label">위험 점수</div>
    <div class="score-value">{overallRisk.score} / 3.00</div>
  </div>
  
  <div class="risk-bar">
    <div class="risk-fill" style="width: {(overallRisk.score / 3) * 100}%"></div>
  </div>
</div>

<style>
  .overall-risk {
    background: linear-gradient(135deg, var(--bg-card) 0%, var(--bg-secondary) 100%);
    border: 3px solid var(--risk-color);
    border-radius: 20px;
    padding: 32px;
    margin-bottom: 32px;
    position: relative;
    overflow: hidden;
    box-shadow: 0 0 40px var(--risk-color);
    animation: fadeIn 0.8s ease-out;
  }
  
  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: scale(0.95);
    }
    to {
      opacity: 1;
      transform: scale(1);
    }
  }
  
  .overall-risk::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, var(--risk-color) 0%, transparent 70%);
    opacity: 0.05;
    animation: rotate 20s linear infinite;
  }
  
  @keyframes rotate {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }
  
  .risk-header {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-bottom: 20px;
    position: relative;
    z-index: 1;
  }
  
  .icon-container {
    width: 80px;
    height: 80px;
    background: var(--risk-color);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 0 30px var(--risk-color);
    animation: iconPulse 2s ease-in-out infinite;
  }
  
  @keyframes iconPulse {
    0%, 100% {
      transform: scale(1);
      box-shadow: 0 0 30px var(--risk-color);
    }
    50% {
      transform: scale(1.05);
      box-shadow: 0 0 50px var(--risk-color);
    }
  }
  
  .risk-icon {
    font-size: 2.5rem;
    color: white;
    line-height: 1;
  }
  
  .risk-info h2 {
    font-family: var(--font-display);
    font-size: 1.2rem;
    font-weight: 700;
    color: var(--text-secondary);
    margin: 0 0 8px 0;
    text-transform: uppercase;
    letter-spacing: 2px;
  }
  
  .risk-level {
    font-family: var(--font-display);
    font-size: 2.5rem;
    font-weight: 900;
    color: var(--risk-color);
    text-transform: uppercase;
    letter-spacing: 2px;
    text-shadow: 0 0 20px var(--risk-color);
  }
  
  .risk-message {
    font-size: 1.1rem;
    color: var(--text-primary);
    margin-bottom: 24px;
    font-weight: 500;
    position: relative;
    z-index: 1;
  }
  
  .risk-score {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;
    position: relative;
    z-index: 1;
  }
  
  .score-label {
    font-family: var(--font-display);
    font-size: 0.9rem;
    color: var(--text-secondary);
    text-transform: uppercase;
    letter-spacing: 1px;
  }
  
  .score-value {
    font-family: var(--font-display);
    font-size: 1.1rem;
    font-weight: 700;
    color: var(--risk-color);
  }
  
  .risk-bar {
    width: 100%;
    height: 12px;
    background: var(--bg-primary);
    border-radius: 6px;
    overflow: hidden;
    position: relative;
    z-index: 1;
  }
  
  .risk-fill {
    height: 100%;
    background: linear-gradient(90deg, var(--risk-color), var(--risk-color));
    border-radius: 6px;
    transition: width 1s cubic-bezier(0.4, 0, 0.2, 1);
    box-shadow: 0 0 10px var(--risk-color);
    animation: fillGlow 2s ease-in-out infinite;
  }
  
  @keyframes fillGlow {
    0%, 100% {
      box-shadow: 0 0 10px var(--risk-color);
    }
    50% {
      box-shadow: 0 0 20px var(--risk-color);
    }
  }
</style>
