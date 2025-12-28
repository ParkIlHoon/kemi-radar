#!/bin/sh
set -e

# Generate runtime environment config
cat <<EOF > /usr/share/nginx/html/env-config.js
window.__ENV__ = {
  VITE_STAGE: "${VITE_STAGE:-remote}",
  VITE_API_BASE_URL: "${VITE_API_BASE_URL:-http://localhost:8080/api}",
  VITE_ADSENSE_CLIENT_ID: "${VITE_ADSENSE_CLIENT_ID:-}",
  VITE_ADSENSE_SLOT_GRID: "${VITE_ADSENSE_SLOT_GRID:-}",
  VITE_ADSENSE_SLOT_FOOTER: "${VITE_ADSENSE_SLOT_FOOTER:-}",
  VITE_RP_CURRENT_MONTH: "${VITE_RP_CURRENT_MONTH:-14.9}",
  VITE_RP_LAST_MONTH: "${VITE_RP_LAST_MONTH:-9.3}",
  VITE_FOREIGN_HOLDING_PERCENTAGE: "${VITE_FOREIGN_HOLDING_PERCENTAGE:-23}"
};
EOF

echo "Runtime environment config generated:"
cat /usr/share/nginx/html/env-config.js

# Execute the main command (start nginx)
exec "$@"
