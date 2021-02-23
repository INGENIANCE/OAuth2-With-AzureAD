# Stage 0, "build-stage", based on Node.js, to build and compile Vue.js
FROM node:lts-alpine as build-stage
LABEL maintainer="Cyrille PERROT"

# Define work directory
WORKDIR /app

# Copy all package.json to work directory
COPY package*.json ./

# Install dependencies
RUN npm i

# Define node.js environment
ENV NODE_ENV=production

# Copy project files to work directory
COPY . .

# Build and minify app for production
RUN npm run build

# Stage 1, based on Nginx, to have only the compiled app, ready for production with Nginx
FROM nginx:stable-alpine as production-stage

# Remove default nginx website
RUN rm -rf /usr/share/nginx/html/*

# Copy dist folder to default nginx public folder
COPY --from=build-stage /app/dist /usr/share/nginx/html

COPY nginx/nginx.conf etc/nginx/conf.d/default.conf

# Define exposed port
EXPOSE 80

# Run nginx
CMD ["nginx", "-g", "daemon off;"]
