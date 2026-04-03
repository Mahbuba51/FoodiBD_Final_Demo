FROM node:20-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
ARG VITE_API_BASE_URL=http://localhost:8080/api
ENV VITE_API_BASE_URL=${VITE_API_BASE_URL}
RUN npm run build

EXPOSE 4173
CMD ["npm", "run", "preview", "--", "--host", "--port", "4173"]