# Build stage
FROM node:20-alpine AS builder

WORKDIR /app

# Copy package files
COPY package.json package-lock.json* ./

# Install dependencies
RUN npm ci

# Copy source code
COPY . .

# Build the application (without environment variables)
# Environment variables will be injected at runtime
RUN npm run build

# Production stage
FROM nginx:alpine

# Copy built files from builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

# Copy entrypoint script
COPY docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

# Default environment variables (can be overridden at runtime)
ENV VITE_STAGE=remote
ENV VITE_API_BASE_URL=http://localhost:8080/api
ENV VITE_ADSENSE_CLIENT_ID=""
ENV VITE_ADSENSE_SLOT_GRID=""
ENV VITE_ADSENSE_SLOT_FOOTER=""
ENV VITE_RP_CURRENT_MONTH=14.9
ENV VITE_RP_LAST_MONTH=9.3
ENV VITE_FOREIGN_HOLDING_PERCENTAGE=23

# Expose port 80
EXPOSE 80

# Use entrypoint to generate runtime config
ENTRYPOINT ["/docker-entrypoint.sh"]

# Start nginx
CMD ["nginx", "-g", "daemon off;"]
