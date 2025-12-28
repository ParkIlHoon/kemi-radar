/**
 * Runtime Environment Configuration
 *
 * This config uses window.__ENV__ for runtime environment variables (Docker/K8s)
 * and falls back to import.meta.env for local development.
 */

function getEnv(key, defaultValue = '') {
  // Priority: Runtime env (Docker) > Build-time env (Vite) > Default value
  if (typeof window !== 'undefined' && window.__ENV__?.[key]) {
    return window.__ENV__[key];
  }
  if (import.meta.env[key]) {
    return import.meta.env[key];
  }
  return defaultValue;
}

// Application stage
export const STAGE = getEnv('VITE_STAGE', 'remote');
export const IS_LOCAL_MODE = STAGE === 'local';

// API Configuration
export const API_BASE_URL = getEnv('VITE_API_BASE_URL', 'http://localhost:8080/api');

// Google AdSense Configuration
export const ADSENSE_CLIENT_ID = getEnv('VITE_ADSENSE_CLIENT_ID', '');
export const ADSENSE_SLOT_GRID = getEnv('VITE_ADSENSE_SLOT_GRID', '');
export const ADSENSE_SLOT_FOOTER = getEnv('VITE_ADSENSE_SLOT_FOOTER', '');

// Manual Input Default Values
export const RP_CURRENT_MONTH = parseFloat(getEnv('VITE_RP_CURRENT_MONTH', '14.9'));
export const RP_LAST_MONTH = parseFloat(getEnv('VITE_RP_LAST_MONTH', '9.3'));
export const FOREIGN_HOLDING_PERCENTAGE = parseFloat(getEnv('VITE_FOREIGN_HOLDING_PERCENTAGE', '23'));

// Export all config as object for convenience
export const config = {
  stage: STAGE,
  isLocalMode: IS_LOCAL_MODE,
  apiBaseUrl: API_BASE_URL,
  adsense: {
    clientId: ADSENSE_CLIENT_ID,
    slotGrid: ADSENSE_SLOT_GRID,
    slotFooter: ADSENSE_SLOT_FOOTER,
  },
  defaults: {
    rpCurrentMonth: RP_CURRENT_MONTH,
    rpLastMonth: RP_LAST_MONTH,
    foreignHoldingPercentage: FOREIGN_HOLDING_PERCENTAGE,
  }
};

// Log configuration on load (useful for debugging)
if (typeof window !== 'undefined') {
  console.log('[Config] Stage:', STAGE);
  console.log('[Config] API Base URL:', API_BASE_URL);
  console.log('[Config] Using runtime env:', !!window.__ENV__);
}

export default config;
