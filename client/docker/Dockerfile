FROM node:16
WORKDIR /usr/src/app
COPY package*.json ./
RUN npm ci --only=production
COPY . .
RUN npm run build

FROM nginx:1.23.3-alpine
EXPOSE 3000
COPY ./docker/nginx/default.conf /etc/nginx/conf.d/default.conf
COPY --from=0 ./usr/src/app/dist/ /usr/share/nginx/html