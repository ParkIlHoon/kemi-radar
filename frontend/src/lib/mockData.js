/**
 * Mock data for local development
 */

export const mockKoreaGovBond10YearsYield = {
  value: 3.31,
  date: "2024-12-23"
};

export const mockUsGovBond10YearsYield = {
  value: 4.54,
  date: "2024-12-23"
};

export const mockWonDollarExchangeRate = {
  value: 1458.0,
  date: "2024-12-23"
};

export const mockDollarIndex = {
  value: 108.09,
  date: "2024-12-23"
};

export const mockRPBalance = {
  currentMonth: 47.6,
  monthlyIncrease: 47.6,
  yearOverYear: "93.5",
  date: "2024-12-23"
};

export const mockForeignHolding = {
  percentage: 12.3,
  date: "2024-12-23"
};

export const mockAllIndicators = {
  koreaGovBond10YearsYield: mockKoreaGovBond10YearsYield,
  usGovBond10YearsYield: mockUsGovBond10YearsYield,
  wonDollarExchangeRate: mockWonDollarExchangeRate,
  dollarIndex: mockDollarIndex,
  rpBalance: mockRPBalance,
  foreignHolding: mockForeignHolding,
  lastUpdated: "2024-12-23T10:30:00",
  riskEvaluation: {
    level: "warning",
    message: "경계 필요 - 모니터링 강화",
    score: "0.80",
    risks: {
      bondYield: {
        level: "danger",
        message: "한미 금리차 -123bp - 외국인 이탈 위험",
        details: {
          spread: "-1.23",
          spreadBp: "-123"
        }
      },
      exchangeRate: {
        level: "warning",
        message: "경계 필요 - 환율 모니터링 강화",
        details: {
          rate: "1458.00"
        }
      },
      rpBalance: {
        level: "safe",
        message: "정상적인 유동성 공급",
        details: {
          monthlyIncrease: "47.6",
          yearOverYear: "93.5"
        }
      },
      foreignHolding: {
        level: "warning",
        message: "외국인 이탈 진행 중 - 모니터링 필요",
        details: {
          percentage: "12.3"
        }
      },
      dollarIndex: {
        level: "warning",
        message: "글로벌 달러 강세 - 신흥국 통화 압박",
        details: {
          dxyValue: "108.09"
        }
      }
    }
  }
};

// Simulate network delay
export function mockDelay(ms = 500) {
  return new Promise(resolve => setTimeout(resolve, ms));
}
