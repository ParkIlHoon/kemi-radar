<script>
  export let title;
  export let value;
  export let subValue = '';
  export let risk;
  export let description = '';
  export let unit = '';
  export let date = '';

  $: riskColor = {
    safe: 'var(--color-safe)',
    warning: 'var(--color-warning)',
    danger: 'var(--color-danger)',
    critical: 'var(--color-critical)'
  }[risk.level];

  $: riskGlow = {
    safe: 'var(--glow-safe)',
    warning: 'var(--glow-warning)',
    danger: 'var(--glow-danger)',
    critical: 'var(--glow-critical)'
  }[risk.level];
</script>

<div class="indicator-card" style="--risk-color: {riskColor}; --risk-glow: {riskGlow}">
  <div class="card-header">
    <h3>{title}</h3>
    <div class="risk-badge" class:critical={risk.level === 'critical'}>
      {risk.level === 'safe' ? '정상' : 
       risk.level === 'warning' ? '경고' :
       risk.level === 'danger' ? '위험' : '매우 위험'}
    </div>
  </div>
  
  <div class="card-body">
    <div class="value-display">
      <span class="main-value">{value}</span>
      {#if unit}
        <span class="unit">{unit}</span>
      {/if}
    </div>
    
    {#if subValue}
      <div class="sub-value">{subValue}</div>
    {/if}
    
    <div class="risk-message">{risk.message}</div>

    {#if description}
      <div class="description">{description}</div>
    {/if}

    {#if date}
      <div class="base-date">기준일: {date}</div>
    {/if}
  </div>

  <div class="pulse-indicator"></div>
</div>

<style>
  .indicator-card {
    background: var(--bg-card);
    border: 2px solid var(--border-color);
    border-radius: 16px;
    padding: 24px;
    position: relative;
    overflow: hidden;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    animation: slideIn 0.5s ease-out backwards;
  }
  
  @keyframes slideIn {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
  
  .indicator-card:hover {
    border-color: var(--risk-color);
    box-shadow: 0 0 30px var(--risk-glow);
    transform: translateY(-4px);
  }
  
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
  }
  
  h3 {
    font-family: var(--font-display);
    font-size: 0.9rem;
    font-weight: 600;
    color: var(--text-secondary);
    text-transform: uppercase;
    letter-spacing: 1px;
    margin: 0;
  }
  
  .risk-badge {
    padding: 4px 12px;
    background: var(--risk-color);
    color: white;
    border-radius: 12px;
    font-size: 0.75rem;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    animation: badgePulse 2s ease-in-out infinite;
  }
  
  .risk-badge.critical {
    animation: criticalPulse 1s ease-in-out infinite;
  }
  
  @keyframes badgePulse {
    0%, 100% {
      opacity: 1;
    }
    50% {
      opacity: 0.8;
    }
  }
  
  @keyframes criticalPulse {
    0%, 100% {
      opacity: 1;
      transform: scale(1);
    }
    50% {
      opacity: 0.9;
      transform: scale(1.05);
    }
  }
  
  .card-body {
    position: relative;
    z-index: 1;
  }
  
  .value-display {
    display: flex;
    align-items: baseline;
    margin-bottom: 8px;
  }
  
  .main-value {
    font-family: var(--font-display);
    font-size: 2.5rem;
    font-weight: 700;
    color: var(--risk-color);
    line-height: 1;
    text-shadow: 0 0 20px var(--risk-glow);
  }
  
  .unit {
    font-family: var(--font-display);
    font-size: 1.2rem;
    color: var(--text-secondary);
    margin-left: 8px;
  }
  
  .sub-value {
    font-family: var(--font-display);
    font-size: 1rem;
    color: var(--text-secondary);
    margin-bottom: 12px;
  }
  
  .risk-message {
    font-size: 0.95rem;
    color: var(--text-primary);
    margin-bottom: 8px;
    font-weight: 500;
  }
  
  .description {
    font-size: 0.85rem;
    color: var(--text-secondary);
    line-height: 1.5;
  }

  .base-date {
    font-size: 0.75rem;
    color: var(--text-secondary);
    margin-top: 8px;
    opacity: 0.7;
    font-style: italic;
  }

  .pulse-indicator {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    height: 4px;
    background: linear-gradient(90deg, transparent, var(--risk-color), transparent);
    opacity: 0.6;
    animation: pulse 3s ease-in-out infinite;
  }
  
  @keyframes pulse {
    0%, 100% {
      transform: translateX(-100%);
    }
    50% {
      transform: translateX(100%);
    }
  }
</style>
