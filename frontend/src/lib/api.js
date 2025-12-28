import {
  mockKoreaGovBond10YearsYield,
  mockUsGovBond10YearsYield,
  mockWonDollarExchangeRate,
  mockDollarIndex,
  mockRPBalance,
  mockForeignHolding,
  mockAllIndicators,
  mockDelay
} from './mockData.js';
import { API_BASE_URL, IS_LOCAL_MODE } from '../config.js';

console.log(`[API] Running in ${IS_LOCAL_MODE ? 'LOCAL' : 'REMOTE'} mode`);

/**
 * 한국 10년물 국채금리 조회
 */
export async function getKoreaGovBond10YearsYield() {
  if (IS_LOCAL_MODE) {
    await mockDelay();
    return mockKoreaGovBond10YearsYield;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/indicators/bond-yields/korea-10years`);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Korea yield fetch error:', error);
    return { value: 0.0, date: new Date().toISOString().slice(0, 10) };
  }
}

/**
 * 미국 10년물 국채금리 조회
 */
export async function getUsGovBond10YearsYield() {
  if (IS_LOCAL_MODE) {
    await mockDelay();
    return mockUsGovBond10YearsYield;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/indicators/bond-yields/us-10years`);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('US yield fetch error:', error);
    return { value: 0.0, date: new Date().toISOString().slice(0, 10) };
  }
}

/**
 * 원/달러 환율 조회
 */
export async function getWonDollarExchangeRate() {
  if (IS_LOCAL_MODE) {
    await mockDelay();
    return mockWonDollarExchangeRate;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/indicators/exchange-rates/krw-usd`);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Exchange rate fetch error:', error);
    return { usd: 0.0, date: new Date().toISOString().slice(0, 10) };
  }
}

/**
 * 달러 인덱스 조회
 */
export async function getDollarIndex() {
  if (IS_LOCAL_MODE) {
    await mockDelay();
    return mockDollarIndex;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/indicators/dollar-index`);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Dollar index fetch error:', error);
    return { value: 0.09, date: new Date().toISOString().slice(0, 10) };
  }
}

/**
 * 모든 지표 데이터를 한 번에 조회 (RP 잔액과 외국인 국채 보유 비중 제외)
 */
export async function fetchAllIndicators() {
  if (IS_LOCAL_MODE) {
    await mockDelay(800); // Slightly longer delay for fetching all data
    return mockAllIndicators;
  }

  try {
    const response = await fetch(`${API_BASE_URL}/indicators/all`);
    const data = await response.json();
    return data;
  } catch (error) {
    console.error('Fetch all indicators error:', error);
    // Fallback to individual calls
    const [koreaGovBond10YearsYield, usGovBond10YearsYield, wonDollarExchangeRate, dollarIndex] = await Promise.all([
      getKoreaGovBond10YearsYield(),
      getUsGovBond10YearsYield(),
      getWonDollarExchangeRate(),
      getDollarIndex()
    ]);

    return {
      koreaGovBond10YearsYield,
      usGovBond10YearsYield,
      wonDollarExchangeRate,
      dollarIndex,
      lastUpdated: new Date().toISOString()
    };
  }
}
