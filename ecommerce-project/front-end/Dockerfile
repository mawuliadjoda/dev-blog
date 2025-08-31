FROM node:24.7.0-alpine3.21

WORKDIR /app

COPY package.json .

RUN npm install

RUN npm i -g serve

COPY . .

RUN npm run build

RUN echo "ok" > /app/dist/healthz

EXPOSE 3000

CMD [ "serve", "-s", "dist" ]