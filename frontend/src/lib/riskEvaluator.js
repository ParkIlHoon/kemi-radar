/**
 * 위험 수준 상수
 */
export const RISK_LEVELS = {
  SAFE: 'safe',
  WARNING: 'warning',
  DANGER: 'danger',
  CRITICAL: 'critical'
};

/**
 * 위험 수준별 라벨
 */
export const RISK_LABELS = {
  safe: '정상',
  warning: '경고',
  danger: '위험',
  critical: '매우 위험'
};

/**
 * 위험 수준별 색상
 */
export const RISK_COLORS = {
  safe: '#22c55e',
  warning: '#f59e0b',
  danger: '#ef4444',
  critical: '#dc2626'
};

/**
 * 10년물 국채금리 위험도 평가
 * - 한미 금리차 -50bp 이하: 경고
 * - 한미 금리차 -100bp 이하: 위험
 */
export function evaluateBondYieldRisk(koreaYield, usYield) {
  const spread = koreaYield - usYield;
  const spreadBp = spread * 100; // basis points

  let level = RISK_LEVELS.SAFE;
  let message = '정상 범위';

  if (spreadBp <= -100) {
    level = RISK_LEVELS.DANGER;
    message = `한미 금리차 ${spreadBp.toFixed(0)}bp - 외국인 이탈 위험`;
  } else if (spreadBp <= -50) {
    level = RISK_LEVELS.WARNING;
    message = `한미 금리차 ${spreadBp.toFixed(0)}bp - 외국인 이탈 경고`;
  } else {
    message = `한미 금리차 ${spreadBp.toFixed(0)}bp - 정상`;
  }

  return {
    level,
    message,
    spread,
    spreadBp: spreadBp.toFixed(0)
  };
}

/**
 * 원/달러 환율 위험도 평가
 * - 1500원 이상: 경고
 * - 1550원 이상: 위험
 * - 1600원 이상: 매우 위험
 */
export function evaluateWonDollarExchangeRateRisk(rate) {
  let level = RISK_LEVELS.SAFE;
  let message = '정상 범위';

  if (rate >= 1600) {
    level = RISK_LEVELS.CRITICAL;
    message = '금융위기 수준 - 긴급 방어 조치 필요';
  } else if (rate >= 1550) {
    level = RISK_LEVELS.DANGER;
    message = '2차 경고선 돌파 - 외화 자산 확대 권고';
  } else if (rate >= 1500) {
    level = RISK_LEVELS.WARNING;
    message = '1차 경고선 돌파 - 외화 자산 일부 확보 권고';
  } else if (rate >= 1450) {
    level = RISK_LEVELS.WARNING;
    message = '경계 필요 - 환율 모니터링 강화';
  } else {
    message = '정상 범위';
  }

  return {
    level,
    message,
    rate: rate.toFixed(2)
  };
}

/**
 * RP 잔액 위험도 평가
 * - 월간 증가 2조원 이상: 경고
 * - 월간 증가 3조원 이상: 위험
 * - 월간 증가 5조원 이상: 매우 위험
 */
export function evaluateRPBalanceRisk(monthlyIncrease, yearOverYear) {
  let level = RISK_LEVELS.SAFE;
  let message = '정상 범위';

  if (monthlyIncrease >= 5) {
    level = RISK_LEVELS.CRITICAL;
    message = '현금 확보 필요 - 금융시장 심각한 불안';
  } else if (monthlyIncrease >= 3) {
    level = RISK_LEVELS.DANGER;
    message = '부동산 매도 타이밍 - 유동성 위기 신호';
  } else if (monthlyIncrease >= 2) {
    level = RISK_LEVELS.WARNING;
    message = '금융시장 불안 신호 - 방어적 자산 배분';
  } else {
    message = '정상적인 유동성 공급';
  }

  return {
    level,
    message,
    monthlyIncrease: monthlyIncrease.toFixed(1),
    yearOverYear
  };
}

/**
 * 외국인 국채 보유 비중 위험도 평가
 * - 15% 이하: 경고
 * - 10% 이하: 위험
 */
export function evaluateForeignHoldingRisk(percentage) {
  let level = RISK_LEVELS.SAFE;
  let message = '정상 범위';

  if (percentage <= 10) {
    level = RISK_LEVELS.DANGER;
    message = '외국인 대량 이탈 - 시장 신뢰도 위기';
  } else if (percentage <= 15) {
    level = RISK_LEVELS.WARNING;
    message = '외국인 이탈 진행 중 - 모니터링 필요';
  } else {
    message = '외국인 투자 양호';
  }

  return {
    level,
    message,
    percentage: percentage.toFixed(1)
  };
}

/**
 * 달러 인덱스 위험도 평가
 * - 달러 강세 + 원화 약세 조합 분석
 */
export function evaluateDollarIndexRisk(dxyValue, usdKrwRate) {
  let level = RISK_LEVELS.SAFE;
  let message = '정상 범위';

  // DXY 110 이상이면 글로벌 달러 강세
  const globalDollarStrength = dxyValue >= 110;
  
  // USD/KRW 1450 이상이면 원화 약세
  const wonWeakness = usdKrwRate >= 1450;

  if (globalDollarStrength && wonWeakness) {
    level = RISK_LEVELS.DANGER;
    message = '글로벌 달러 강세 + 원화 추가 약세 - 신흥국 전체 압박';
  } else if (globalDollarStrength) {
    level = RISK_LEVELS.WARNING;
    message = '글로벌 달러 강세 - 신흥국 통화 압박';
  } else if (wonWeakness) {
    level = RISK_LEVELS.WARNING;
    message = '한국 특수 요인 - 원화 선별적 약세';
  } else {
    message = '달러 지수 안정';
  }

  return {
    level,
    message,
    dxyValue: dxyValue.toFixed(2)
  };
}

/**
 * 전체 위험도 종합 평가
 */
export function evaluateOverallRisk(indicators) {
  const risks = [
    evaluateBondYieldRisk(indicators.koreaGovBond10YearsYield.value, indicators.usGovBond10YearsYield.value),
    evaluateWonDollarExchangeRateRisk(indicators.wonDollarExchangeRate.value),
    evaluateRPBalanceRisk(indicators.rpBalance.monthlyIncrease, indicators.rpBalance.yearOverYear),
    evaluateForeignHoldingRisk(indicators.foreignHolding.percentage),
    evaluateDollarIndexRisk(indicators.dollarIndex.value, indicators.wonDollarExchangeRate.value)
  ];

  // 위험 수준 점수화
  const riskScores = {
    safe: 0,
    warning: 1,
    danger: 2,
    critical: 3
  };

  const totalScore = risks.reduce((sum, risk) => sum + riskScores[risk.level], 0);
  const avgScore = totalScore / risks.length;

  let overallLevel = RISK_LEVELS.SAFE;
  let overallMessage = '경제 지표 정상';

  if (avgScore >= 2.5) {
    overallLevel = RISK_LEVELS.CRITICAL;
    overallMessage = '경제 위기 - 즉각적인 방어 조치 필요';
  } else if (avgScore >= 1.5) {
    overallLevel = RISK_LEVELS.DANGER;
    overallMessage = '고위험 - 자산 재배분 권고';
  } else if (avgScore >= 0.5) {
    overallLevel = RISK_LEVELS.WARNING;
    overallMessage = '경계 필요 - 모니터링 강화';
  }

  return {
    level: overallLevel,
    message: overallMessage,
    score: avgScore.toFixed(2),
    risks
  };
}
